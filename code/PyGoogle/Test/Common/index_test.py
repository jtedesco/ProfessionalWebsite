import unittest
import os
import shutil
from Indexer import indexer_constants
from Common.index import Index
from mockito import *
from Indexer.web_page import WebPage
from Common.search_result import SearchResult

__author__ = 'Jon Tedesco'

class IndexTest(unittest.TestCase):
    """
      Tests the Index class, which holds the two index objects
    """

    def test_init_attributes(self):
        """
          Tests the constructor of the index object
        """

        # Build an object with all data supplied
        full_index = Index('text_index_directory', True)

        # Test the contents of the context
        self.assertTrue(full_index.text_index is not None)
        self.assertTrue(full_index.graph_index is not None)
        self.assertTrue(full_index.lock is not None)
        self.assertTrue(full_index.verbose)
        self.assertEquals(full_index.file_name, indexer_constants.DEFAULT_GRAPH_INDEX_FILE)


    def test_init_files(self):
        """
          Tests that creating the index object creates the graph and text index on disk
        """

        # Build a index with all data supplied
        full_index = Index('text_index_directory', True)

        # Check that the text index was created
        self.assertTrue(os.path.exists('text_index_directory'))
        self.assertTrue(os.path.isdir('text_index_directory'))

        # Check that the graph index was created
        self.assertTrue(os.path.exists(indexer_constants.DEFAULT_GRAPH_INDEX_FILE))
        self.assertTrue(os.path.isfile(indexer_constants.DEFAULT_GRAPH_INDEX_FILE))

        # Delete the files
        shutil.rmtree('text_index_directory')
        os.remove(indexer_constants.DEFAULT_GRAPH_INDEX_FILE)


    def test_insert_calls_indexes(self):
        """
          Tests that inserting a record into the index calls the text and graph indexes, using mockito
        """

        # Build mocked out graph and text indexes
        mocked_graph_index = mock()
        mocked_text_index = mock()

        # Setup the behavior of the mocked objects
        when(mocked_graph_index).insert(any(WebPage)).thenReturn(True)
        when(mocked_text_index).insert(any(WebPage)).thenReturn(True)

        # Build a mocked out index
        mocked_index = Index('text_index_directory', True)
        mocked_index.graph_index = mocked_graph_index
        mocked_index.text_index = mocked_text_index
        empty_page = WebPage('', '', '', [], [])

        # Test inserting into the index
        mocked_index.insert(empty_page)

        # Verify that the correct methods were called
        verify(mocked_text_index, times(1)).insert(empty_page)
        verify(mocked_graph_index, times(1)).insert(empty_page)

        # Cleanup unwanted files
        shutil.rmtree('text_index_directory')
        os.remove(indexer_constants.DEFAULT_GRAPH_INDEX_FILE)


    def test_query_propagates_to_indexes(self):
        """
          Test that calling a query on the index will call query and rank on the text and graph indexes, respectively
        """

        # Build mocked out graph and text indexes
        mocked_graph_index = mock()
        mocked_text_index = mock()
        when(mocked_text_index).query(any(str)).thenReturn(([],[],[]))

        # Build a mocked out index
        mocked_index = Index('text_index_directory', True)
        mocked_index.graph_index = mocked_graph_index
        mocked_index.text_index = mocked_text_index

        # Test querying the index
        query = 'some_query'
        mocked_index.query(query)

        # Verify that the correct methods were called
        verify(mocked_text_index, times(1)).query(query)
        verify(mocked_graph_index, times(1)).rerank(any(SearchResult), any(int))

        # Cleanup unwanted files
        shutil.rmtree('text_index_directory')
        os.remove(indexer_constants.DEFAULT_GRAPH_INDEX_FILE)


    def test_sync_called_on_insert(self):
        """
          Test that sync is called after we call insert an object into the index
        """

        # Create an index and spy it
        index = Index('text_index_directory', True)
        spied_index = spy(index)

        # Try to insert something into the index
        empty_page = WebPage('', '', '', [], [])
        spied_index.insert(empty_page)

        # Verify that sync was called after we inserted this elemnt into the index
        verify(spied_index, times(1)).insert(empty_page)

       # Cleanup unwanted files
        shutil.rmtree('text_index_directory')
        os.remove(indexer_constants.DEFAULT_GRAPH_INDEX_FILE)