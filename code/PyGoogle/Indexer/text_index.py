"""
  This file holds the TextIndex class, a Whoosh! - implemented text index of the crawled web content
"""

from indexer_constants import *
from Indexer.web_page import WebPage
from whoosh.qparser.default import QueryParser
from whoosh.fields import Schema, TEXT, ID, KEYWORD
from whoosh.index import create_in, exists_in, open_dir
from whoosh.highlight import highlight, HtmlFormatter, ContextFragmenter
import os
import sys
import re
from whoosh.spelling import SpellChecker

__author__ = 'Jon Tedesco'

class TextIndex:
    """
      Class that acts as a structure for the text index, used by the CompositeIndex class

      This class is also synced with disk, so that the index can be restored when resuming the indexer.
    """

    def __init__(self, index_dir, verbose):
        """
          Constructs the index, with an empty index index using Whoosh

			@param	index_dir	The directory in which to place the index data
			@param	verbose		Whether or not this index should output verbose errors
        """

        # Create the schema for this index, which denotes the types of each field, and next try to build the index itself
        #   using this schema. Note that this schema treats the URL as the unique identifier for documents in the index,
        #   and scores documents based on the title and content alone
        self.index_schema = Schema(url=ID(stored=True), title=TEXT(stored=True), content=TEXT(stored=True),
                                   keywords=KEYWORD(stored=True), links=KEYWORD(stored=True))

        try:
            
            # Try to create the index directory
            os.mkdir(index_dir)

            # Build a new index in this directory
            self.index = create_in(index_dir, self.index_schema)

        except OSError, error:

            # If the directory already exists, try to restore the index from the directory
            if(exists_in(index_dir)):
                self.index = open_dir(index_dir)
            else:
                raise error

        # Record verboseness
        self.verbose = verbose


    def insert(self, page):
        """
          Inserts a url into the index, parsing its content, title, keywords, links, and parent url as it inserts it into
            the database

            @param  page Holds the key information about the page used for indexing
            @retval		True or False, on corresponding success or failure
        """

        # Create an index writer to insert this document into the index
        writer = self.index.writer()

        try:

            # Convert the keywords and links into comma separated values
            keywords_string = str(page.keywords).strip(']').strip('[').strip('\'')
            links_string = str(page.links).strip(']').strip('[').strip('\'')

            # Strip any html tags from the content so that it is displayed correctly
            tag_regex = re.compile(r'<.*?>')
            page.content = tag_regex.sub('', page.content)


            # Convert all data into unicode
            unicode_url = unicode(page.url.strip(), 'utf-8')
            unicode_title = unicode(page.title.strip(), 'utf-8')
            unicode_content = unicode(page.content.strip(), 'utf-8')
            unicode_keywords = unicode(keywords_string.strip(), 'utf-8')
            unicode_links = unicode(links_string.strip(), 'utf-8')

            # Add this new page as a document to the index. Note that this library requires that input data be unicode
            writer.add_document(url=unicode_url, title=unicode_title, content=unicode_content,
                                     keywords=unicode_keywords, links=unicode_links)

        except:
            if self.verbose:
                print (ERROR_CREATING_DOCUMENT) % page.url, str(sys.exc_info())
            return False

        try:

            # Write the document to the index
            writer.commit()

        except:
            if self.verbose:
                print (ERROR_CREATING_DOCUMENT) % page.url
            return False

        # Success!
        return True


    def query(self, query):
        """
          Queries the index for data with the given text query

            @param  query   The text query to perform on the indexed data
            @retval			A list of page objects representing the results
        """

        # Create a searcher object for this index
        searcher = self.index.searcher()

        # Create a query parser, providing it with the schema of this index, and the default field to search, 'content'
        query_parser = QueryParser('content', schema=self.index_schema)

        # Build a query object from the query string
        query_object = query_parser.parse(query)

        # Build a spell checker in this index and add the "content" field to the spell checker
        self.spell_checker = SpellChecker(self.index.storage)
        self.spell_checker.add_field(self.index, 'title')
        self.spell_checker.add_field(self.index, 'content')

        # Extract the 'terms' that were found in the query string. This data can be used for highlighting the results
        search_terms = [text for fieldname, text in query_object.all_terms()]

        # Remove terms that are too short
        for search_term in search_terms:
            if len(search_term) < 4:
                search_terms.remove(search_term)

        # Perform the query itself
        search_results = searcher.search(query_object)

        # Get an analyzer for analyzing the content of each page for highlighting
        analyzer = self.index_schema['content'].format.analyzer

        # Build the fragmenter object, which will automatically split up excerpts. This fragmenter will split up excerpts
        #   by 'context' in the content
        terms_set = frozenset(search_terms)
        fragmenter = ContextFragmenter(frozenset(search_terms))

        # Build the formatter, which will dictate how to highlight the excerpts. In this case, we want to use HTML to
        #   highlight the results
        formatter = HtmlFormatter()

        # Build a list of pages containing the results
        web_page_results = []
        for search_result in search_results:

            # Grab the fields from the document
            url = search_result['url']
            title = search_result['title']
            content = search_result['content']
            keywords_string = search_result['keywords']
            links_string = search_result['links']

            # Parse out lists of keywords and links
            keywords = keywords_string.split(',')
            links = links_string.split(',')

            # Build a list of HTML-highlighted excerpts
            excerpts = highlight(content, search_terms, analyzer, fragmenter, formatter)

            # Build the web page object
            web_page = WebPage(url, title, content, keywords, links, excerpts)

            # Add this page to the list of results
            web_page_results.append(web_page)

        # Build a list of 'suggest' words using the spell checker
        suggestions = []
        for term in search_terms:
            suggestions.append(self.spell_checker.suggest(term))

        # Return the list of web pages along with the terms used in the search
        return web_page_results, search_terms, suggestions
