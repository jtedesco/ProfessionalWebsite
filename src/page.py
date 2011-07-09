import os
import re
import cherrypy
from datetime import datetime
from cherrypy.lib.static import serve_file
from whoosh.fields import Schema, TEXT
from whoosh.highlight import ContextFragmenter, HtmlFormatter, highlight
from whoosh.index import create_in, exists_in, open_dir
from whoosh.qparser.default import QueryParser
from whoosh.spelling import SpellChecker
from common.common import get_server_root
from util.file_reader import read_file

__author__ = 'Jon'

class Page(object):
    """
        Abstract class for a page on the website. This exposes the 'index' method, which returns the html
        content of this page.
    """


    def __init__(self):
        """
            Construct the shell of a page
        """

        # Build the HTMl content of this page
        self.build_content()

        # We haven't initialized the index for this page yet
        self.initialized = False


    @cherrypy.expose
    def index(self):
        """
            Returns the content of this page, or in this case, redirects to the 'home' page
        """
        raise cherrypy.HTTPRedirect("%shome/" % get_server_root(), 301)

    
    def build_content(self):
        """
                Helper function to allow pages to dynamically update their HTML content
        """

        # Get each individual component of the page
        meta_header = self.meta_header(self.title())
        page_header = self.header()
        content = self.main_content()
        menu = self.menu()
        sidebar = self.sidebar()
        footer = self.footer()

        # Build this page's content on creation
        self.content = read_file("content/template.html") % (meta_header, page_header, content, menu, sidebar, footer)


    @cherrypy.expose
    def search(self, query = None):
        """
            Searches the site's contents using the given query.

                @param  query   The query on which to search
        """

        # Build the site's search index
        if not self.initialized:
            self.create_index()

        # Get the template for the search results, and the search results themselves
        template_content = read_file("content/pages/search.html")

        # Default the search results if there isn't a query
        search_results = "<p>Please enter a query to search</p>"
        title = "Jon Tedesco &#183; Search"

        if query is not None and len(query) > 0:

            # Convert the query to unicode
            try:
                query = unicode(query, 'utf-8')
            except Exception:
                pass

            # Run the query
            start_time = datetime.now()
            results, search_terms, suggestions, result_count = self.run_query(query)
            end_time = datetime.now()
            stats = "Searched for <i>%s</i>, found %d hits in %1.3f seconds<br>" % (query, result_count, (end_time-start_time).microseconds/1000000.0)
            more_stats=""
            if result_count <= 1:
                more_stats = "Did you mean <a href='../search/?query=%s'><i>%s</i></a>?" % (suggestions[0][0], suggestions[0][0])

            # Update the page components
            title += " &#183; '%s'" % query
            search_results = template_content % (more_stats + stats, results)

        # Build the components of the page
        meta_header = self.meta_header(self.title())
        page_header = self.header()
        content = search_results
        menu = self.menu()
        sidebar = self.sidebar()
        footer = self.footer()

        return read_file("content/template.html") % (meta_header, page_header, content, menu, sidebar, footer)


    @cherrypy.expose
    def download(self, file_path):
        """
            Helper to allow any page to serve up a file
        """
        return serve_file(file_path, "application/x-download", "attachment")

    
    def main_content(self):
        """
            Returns the primary, bulk content of this page, to be implemented by each subclass.
            This root page simply redirects to the home page.
        """
        pass
        

    def title(self):
        """
            Abstract method implemented by all pages
        """
        pass


    def meta_header(self, title):
        """
            Returns the meta header, which is common to all pages

                @param  title   The title of this page
                @return The meta data to go in the HEAD tag of the page
        """
        return read_file("content/meta_header.html") % (title, " ".join(self.keywords()), self.description(), get_server_root())


    def keywords(self):
        """
            Gets a list of keywords for the site, which should be common to all pages

                @return The list of keywords for this page
        """
        return ["Jon Tedesco", "Jon", "Tedesco", "Jonathan", "Jonathan Tedesco", "Jon C Tedesco", "Jonathan C Tedesco",
                "Jonathan Christopher Tedesco", "University of Illinois", "UIUC", "software", "development", "develop",
                "Java", "c", "c++", "c#", "python", "ruby", "projects", "computer", "science", "projects", "tedesco1",
                "developer", "design", "architecture", "search", "deep web", "object", "oriented", "programming"]


    def description(self):
        """
          Gets the description of the site to be put in the meta data

            @return The description of the page
        """
        return "Jon Tedesco, student and software developer at University of Illinois at Urbana-Champaign"


    def header(self):
        """
            Gets the standard header of the page (logo, search box)
        """
        try:
            return read_file("content/header.html") % self.query
        except AttributeError:
            return read_file("content/header.html") % ""


    def footer(self):
        """
            Gets the footer for the site, automatically updating the copyright to the current year
        """
        return read_file("content/footer.html") % str(datetime.today().year)

    
    def menu(self):
        """
            Returns the code for the side menu (should be the same on every page)
        """
        return read_file("content/menu.html") % (get_server_root(), get_server_root(), get_server_root(), get_server_root(), get_server_root())


    def sidebar(self):
        """
            Get the sidebar code (traditionally the contact information)
        """
        return read_file("content/sidebar.html")


    def create_index(self):

        # Create the schema for this index, which denotes the types of each field, and next try to build the index itself
        #   using this schema. Note that this schema treats the URL as the unique identifier for documents in the index,
        #   and scores documents based on the title and content alone
        self.index_schema = Schema(content=TEXT(stored=True), title=TEXT(stored=True))
        index_dir = ".index"

        try:
            # Try to create the index directory
            os.mkdir(index_dir)

            # Build a new index in this directory
            self.index = create_in(index_dir, self.index_schema)

            # Get a writer for the index
            index_writer = self.index.writer()

            # Walk the pages folder for content
            for root_path, sub_directories, files in os.walk("content/pages"):
                for file in files:
                    self.insert_document(root_path + "/" + file, index_writer, file)

            # Commit all the changes, so that every change is flushed to disk, and we can safely query the index
            index_writer.commit()

        except OSError, error:
            
            # If the directory already exists, try to restore the index from the directory
            if exists_in(index_dir):
                self.index = open_dir(index_dir)
            else:
                raise "Error creating or opening index: '" + error.message + "'"

        # We've now been initialized
        self.initialized = True


    def insert_document(self, path, index_writer, title):
        """
            Insert a given document into the index.

                @param  path            The path to the file to insert into the index (HTML format)
                @param  index_writer    A writer to access the text index
                @param  title           The title of this document
        """

        # Grab the content of the file
        content = ""
        for line in open(path, 'r'):
            content += line

        # Parse out the HTMl content of the file
        tag_re = re.compile("<.*?>")
        white_space_re = re.compile("\s+")
        parsed_content = tag_re.sub(" ", content)
        parsed_content = white_space_re.sub(" ", parsed_content)
        parsed_content = unicode(parsed_content, 'utf-8')

        # Parse out the title
        title = unicode(title.replace(".html", "").title(), 'utf-8')

        # Put this content into index
        index_writer.add_document(content=parsed_content, title=title)


    def run_query(self, query):
        """
          Queries the index for data with the given text query

            @param  query   The text query to perform on the indexed data
            @return			A list of HTMl string snippets to return
        """

        # Create a searcher object for this index
        searcher = self.index.searcher()

        # Create a query parser, providing it with the schema of this index, and the default field to search, 'content'
        query_parser = QueryParser('content', schema=self.index_schema)

        # Build a query object from the query string
        query_object = query_parser.parse(query)

        # Build a spell checker in this index and add the "content" field to the spell checker
        self.spell_checker = SpellChecker(self.index.storage)
        self.spell_checker.add_field(self.index, 'content')
        self.spell_checker.add_field(self.index, 'title')

        # Extract the 'terms' that were found in the query string. This data can be used for highlighting the results
        search_terms = [text for fieldname, text in query_object.all_terms()]

        # Remove terms that are too short
        for search_term in search_terms:
            if len(search_term) <= 3:
                search_terms.remove(search_term)

        # Perform the query itself
        search_results = searcher.search(query_object)

        # Get an analyzer for analyzing the content of each page for highlighting
        analyzer = self.index_schema['content'].format.analyzer

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

            # Build a list of HTML-highlighted excerpts
            excerpt = highlight(content, search_terms, analyzer, fragmenter, formatter)

            # Add this new snippet to <code>results</code> dictionary
            if title not in results.keys():
                results[title] = []
            results[title].append(excerpt)
            result_count += 1

        # Build a list of 'suggest' words using the spell checker
        suggestions = []
        for term in search_terms:
            suggestions.append(self.spell_checker.suggest(term))

        # Format the results into a nicer format
        formatted_results = self.format_results(results)

        # Return the list of web pages along with the terms used in the search
        return formatted_results, search_terms, suggestions, result_count


    def format_results(self, results):
        """
            Formats the results from the Whoosh! query into something nicely formatted for a web page
        """

        # Filter out whitespace from results
        white_space_re = re.compile("\s+")
        clean_results = {}

        for result in results.keys():
            clean_results[result] = []
            for entry in results[result]:
                parsed_content = white_space_re.sub(" ", entry)
                if parsed_content not in clean_results[result]:
                    clean_results[result].append(parsed_content)

        formatted_results = ""

        # Loop through each key in the results (a page), and group it that way
        for title in clean_results.keys():
            if len(clean_results[title]) > 0:

                # Format the title of this section
                title_section = ("<h2><a href='../%s'>" % title.lower()) + title.encode('ascii') + "</a></h2><p></p>"
                formatted_results += title_section

                for result in clean_results[title]:
                    formatted_results += "..." + result.encode('ascii') + "...<br>"
                formatted_results += "<br><br>"

        return formatted_results


