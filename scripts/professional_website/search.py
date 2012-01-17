from datetime import datetime
import os
import re
import subprocess
from whoosh.analysis import CharsetFilter, StemmingAnalyzer
from whoosh.fields import TEXT, Schema
from whoosh.highlight import ContextFragmenter, HtmlFormatter, highlight
from whoosh.index import create_in, open_dir, exists_in
from whoosh.qparser.default import MultifieldParser
from whoosh.qparser.syntax import OrGroup
from whoosh.spelling import SpellChecker
from whoosh.support.charset import accent_map
from django.http import HttpResponse
from django.template.context import Context
from django.template.loader import get_template
from professional_website.common import get_server_root, get_generic_keywords
from professional_website.models import Post, Project

__author__ = 'jon'

# Global index schema & analyzer
analyzer = StemmingAnalyzer() | CharsetFilter(accent_map)
index_schema = Schema(content=TEXT(analyzer=analyzer, stored=True), title=TEXT(analyzer=analyzer, stored=True),
    url=TEXT(stored=True))

def search(request, query):
    """
      Search the page using Whoosh

        @param  query   The query to search
    """

    # Create the index if it doesn't already exist, or open it if it does
    if exists_in('.index'):
        index = open_dir('.index')
    else:
        raise Exception("Could not find index!")

    # Prepare defaults
    title = "Jon Tedesco &#183; Search"
    search_results = "Please enter a query to search"
    stats = ''
    spelling_suggestions = ''

    # Run query
    if query is not None and len(query) > 0:
        # Convert the query to unicode
        try:
            query = unicode(query, 'utf-8')
        except Exception:
            pass

        # Run the query
        start_time = datetime.now()
        search_results, search_terms, spelling_suggestions, result_count = run_query(query, index)
        end_time = datetime.now()
        stats = "Searched for <i>%s</i>, found %d hits in %1.3f seconds<br>" % (
        query, result_count, float((end_time - start_time).microseconds) / 1000000.0)
        spelling_suggestions = ""
        try:
            spelling_suggestions = "Did you mean <a href='../search/?query=%s'><i>%s</i></a>?" % (
            spelling_suggestions[0][0], spelling_suggestions[0][0])
        except Exception:
            pass

        title += " &#183; '%s'" % query


    # Fill in the search template
    template = get_template('pages/search.html')

    # HTML Data for this page
    html = template.render(Context({
        'meta_description': 'Homepage of Jon Tedesco, a dedicated student and avid software developer at University' +
                            'of Illinois at Urbana-Champaign',
        'meta_keywords': ' '.join(get_generic_keywords()),
        'page_title': title,
        'word_cloud_name': 'about_me',
        'server_root': get_server_root(),
        'spelling_suggestions': spelling_suggestions,
        'search_results': search_results,
        'query': query,
        'stats': stats
    }))

    return HttpResponse(html)


def create_index():
    # Create the schema for this index, which denotes the types of each field, and next try to build the index itself
    #   using this schema. Note that this schema treats the URL as the unique identifier for documents in the index,
    #   and scores documents based on the title and content alone
    index_dir = ".index"

    # Try to create the index directory
    os.mkdir(index_dir)

    # Build a new index in this directory
    index = create_in(index_dir, index_schema)

    # Get a writer for the index
    index_writer = index.writer()

    # Add the main pages to the index
    for main_page in ['about_me', 'research', 'resume']:
        insert_document(index_writer, main_page, get_server_root() + main_page, main_page)

    # Add the blog entries
    blog_posts = list(Post.objects.all())
    for blog_post in blog_posts:
        insert_document(index_writer, blog_post.title, get_server_root() + 'blog/' + blog_post.name, blog_post.name)

    # Add the projects
    projects = list(Project.objects.all())
    for project in projects:
        insert_document(index_writer, project.title, get_server_root() + 'projects/' + project.name, project.name)

    # Commit all the changes, so that every change is flushed to disk, and we can safely query the index
    index_writer.commit()

    return index, index_schema


def insert_document(index_writer, title, url, name):
    """
        Insert a given document into the index.

            @param  index_writer    A writer to access the text index
            @param  title           The title of this document
            @param  url             The url of this page
            @param  name
    """

    # Grab the content of the file
    subprocess.call(["wget", url])
    content = subprocess.check_output(["cat", name])
    subprocess.call(["rm", name])

    # Remove all HTML tags from content
    parsed_content = content.replace("<br/>", "\n")
    closing_tag_re = re.compile("</.*?>")
    tag_re = re.compile("<.*?>")
    white_space_re = re.compile("\s+")
    parsed_content = closing_tag_re.sub("\n", parsed_content)
    parsed_content = tag_re.sub(" ", parsed_content)
    parsed_content = white_space_re.sub(" ", parsed_content)
    parsed_content = unicode(parsed_content, 'utf-8')

    # Parse out the title
    try:
        title = unicode(title.replace(".html", ""), 'utf-8')
    except:
        pass

    # Put this content into index
    actualUrl = url.replace('http://localhost:8000/', 'http://jontedesco.net/')
    print actualUrl
    index_writer.add_document(content=parsed_content, title=title.title(), url=unicode(actualUrl))


def run_query(query, index):
    """
      Queries the index for data with the given text query

        @param  query   The text query to perform on the indexed data
        @return			A list of HTMl string snippets to return
    """

    # Create a searcher object for this index
    searcher = index.searcher()

    # Create a query parser that will parse multiple fields of the documents
    field_boosts = {
        'content': 1.0,
        'title': 3.0
    }
    query_parser = MultifieldParser(['content', 'title'], schema=index_schema, fieldboosts=field_boosts, group=OrGroup)

    # Build a query object from the query string
    query_object = query_parser.parse(query)

    # Build a spell checker in this index and add the "content" field to the spell checker
    spell_checker = SpellChecker(index.storage)
    spell_checker.add_field(index, 'content')
    spell_checker.add_field(index, 'title')

    # Extract the 'terms' that were found in the query string. This data can be used for highlighting the results
    search_terms = [text for fieldname, text in query_object.all_terms()]

    # Remove terms that are too short
    for search_term in search_terms:
        if len(search_term) <= 3:
            search_terms.remove(search_term)

    # Perform the query itself
    search_results = searcher.search(query_object)

    # Get an analyzer for analyzing the content of each page for highlighting
    analyzer = index_schema['content'].format.analyzer

    # Build the fragmenter object, which will automatically split up excerpts. This fragmenter will split up excerpts
    #   by 'context' in the content
    fragmenter = ContextFragmenter(frozenset(search_terms))

    # Build the formatter, which will dictate how to highlight the excerpts. In this case, we want to use HTML to
    #   highlight the results
    formatter = HtmlFormatter()

    # Create a dictionary of search result snippets, indexed by page, and a count of the number of results
    results = {}
    result_count = 0

    # Iterate through the search results
    for search_result in search_results:
        # Grab the fields from the document
        title = search_result['title']
        content = search_result['content']
        url = search_result['url']

        # Build a list of HTML-highlighted excerpts
        excerpt = highlight(content, search_terms, analyzer, fragmenter, formatter)

        # Add this new snippet to <code>results</code> dictionary
        if title not in results.keys():
            results[title] = {
                'excerpts': [],
                'url': url
            }
        results[title]['excerpts'].append(excerpt)
        result_count += 1

    # Build a list of 'suggest' words using the spell checker
    suggestions = []
    for term in search_terms:
        suggestions.append(spell_checker.suggest(term))

    # Format the results into a nicer format
    formatted_results = format_results(results, search_terms)

    # Return the list of web pages along with the terms used in the search
    return formatted_results, search_terms, suggestions, result_count


def format_results(results, search_terms):
    """
        Formats the results from the Whoosh! query into something nicely formatted for a web page
    """

    # Filter out whitespace from results
    white_space_re = re.compile("\s+")
    clean_results = {}

    # Clean the results
    for result in results.keys():
        clean_results[result] = {
            'excerpts': [],
            'url': results[result]['url']
        }
        for entry in results[result]['excerpts']:
            parsed_content = white_space_re.sub(" ", entry)
            if parsed_content not in clean_results[result]['excerpts']:
                clean_results[result]['excerpts'].append(parsed_content)

    formatted_results = ""

    # Loop through each key in the results (a page), and group it that way
    for title in clean_results.keys():
        if len(clean_results[title]['excerpts']) > 0:
            # Format the title of this section
            url = clean_results[title]['url']
            title_section = ("<h2><a href='%s'>" % url) + title.encode('ascii') + "</a></h2><p></p>"
            formatted_results += title_section

            for result in clean_results[title]['excerpts']:
                excerpt = result.encode('ascii')
                if len(excerpt) > 0:
                    formatted_results += "..." + excerpt + "...<br>"
            formatted_results += "<br><br>"

    return formatted_results


if __name__ == '__main__':
    create_index()