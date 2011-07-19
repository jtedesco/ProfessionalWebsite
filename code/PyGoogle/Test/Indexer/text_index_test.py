"""
  This file holds the tests for the text index object
"""
import unittest
import os
import shutil
from Indexer.indexer_constants import *
from Indexer.text_index import TextIndex
from whoosh.fields import Schema, ID, TEXT, KEYWORD, KEYWORD
from whoosh.index import exists_in, create_in
from Indexer.web_page import WebPage
from mockito import *

__author__ = 'Jon Tedesco'

class TextIndexTest(unittest.TestCase):
    """
      A Test class for the TextIndex class
    """

    def test_init(self):
        """
          Tests that we create a schema, initialize the attributes for the index, and create or open an index directory
        """

        # Create the input data for the index
        test_verbosity = False
        test_index_dir = "test_index_dir"

        # Create a text index using the prepared data
        text_index = TextIndex(test_index_dir, test_verbosity)

        # Assert the the fields of the index are correct
        self.assertTrue(isinstance(text_index.index_schema, Schema))
        self.assertEquals(text_index.verbose, test_verbosity)

        # Assert that the index has been created in the given directory
        self.assertTrue(os.path.exists(test_index_dir))
        self.assertTrue(os.path.isdir(test_index_dir))
        self.assertTrue(exists_in(test_index_dir))

        # Remove the index directory
        shutil.rmtree(test_index_dir)


    def test_insert(self):
        """
          Test that the index creates a writer object and writes some data to it on an insert
        """

        # Create the input data for the index
        test_verbosity = False
        test_index_dir = "test_index_dir"
        input_page = WebPage("www.google.com", "Google", "THIS IS GOOGLE!", ["google"], ["we link nowhere!"])

        # Create a text index using the prepared data
        text_index = TextIndex(test_index_dir, test_verbosity)

        # Create a mocked out index and stub it out
        mock_writer = mock()
        text_index.index = mock()
        when(text_index.index).writer().thenReturn(mock_writer)

        # Try to insert the sample page
        success = text_index.insert(input_page)

        # Verify that the writer was called correctly, and a document was inserted
        inorder.verify(text_index.index).writer()
        inorder.verify(mock_writer).add_document(url=u"www.google.com", title=u"Google", content=u"THIS IS GOOGLE!", keywords=u"google", links=u"we link nowhere!")
        inorder.verify(mock_writer).commit()
        self.assertTrue(success)

        # Remove the index directory
        shutil.rmtree(test_index_dir)


    def test_query(self):
        """
          Test querying the text index
        """

        # Create the input data for the index
        test_verbosity = False
        test_index_dir = "test_index_dir"
        test_query = u"term_1, term_2, term_3"
        input_page = WebPage("www.google.com", "Google", "THIS IS GOOGLE!", ["google"], ["we link nowhere!"])

        # Create a text index using the prepared data
        text_index = TextIndex(test_index_dir, test_verbosity)

        # Create a mocked out index and stub it out
        mock_searcher = mock()
        index_schema = Schema(url=ID(stored=True), title=TEXT(stored=True), content=TEXT(stored=True),
                                   keywords=KEYWORD(stored=True), links=KEYWORD(stored=True))
        text_index.index = create_in(test_index_dir, index_schema)
        when(text_index.index).searcher().thenReturn(mock_searcher)
        when(mock_searcher).search(any(object)).thenReturn([])

        # Perform a query
        result, terms, spelling_suggestions = text_index.query(test_query)

        # Assert that the correct methods were called
        verify(text_index.index).searcher()
        verify(mock_searcher).search(any(object))

        # Assert that the output is correct
        self.assertEquals(result, [])
        self.assertEquals(terms, [u"term_2", u"term_1", u"term_3"])

        # Remove the index directory
        shutil.rmtree(test_index_dir)
        


  