"""
  This file tests the indexer thread
"""
import unittest
from Common.crawl_data_context import CrawlDataContext
from Indexer.indexer_thread import IndexerThread
import threading
from mockito import *
from Indexer.web_page import WebPage

__author__ = 'Jon Tedesco'

class IndexerThreadTest(unittest.TestCase):
    """
      This class tests the indexer thread's basic functionality
    """

    def test_init(self):
        """
          Tests the indexer thread constructor
        """

        # Build a default indexer thread
        indexer_thread = self.build_default_indexer_thread()

        # Assert that the data is correct and that this is actually a thread
        self.assertTrue(isinstance(indexer_thread, threading.Thread))
        self.assertEquals(["a queue item"], indexer_thread.queue)
        self.assertEquals("an index", indexer_thread.index)
        self.assertTrue(indexer_thread.database_cursor is not None)
        self.assertTrue(isinstance(indexer_thread.crawl_data_context, CrawlDataContext))


    def test_run(self):
        """
          Tests that the 'run' method calls the functions expected
        """

        # Build a default crawler thread
        indexer_thread = self.build_default_indexer_thread()

        # Force it to only complete one iteration and mock out its queue, index, and database cursor
        indexer_thread.iterations_remaining = 1
        indexer_thread.index = mock()
        indexer_thread.queue = mock()
        indexer_thread.database_cursor = mock()

        # Stub out the behavior of the queue and database cursor
        when(indexer_thread.queue).empty().thenReturn(False)
        when(indexer_thread.queue).get().thenReturn(("","",""))
        when(indexer_thread.database_cursor).fetchall().thenReturn([])

        # Run the indexer thread
        indexer_thread.run()

        # Assert that the functions were called as expected
        inorder.verify(indexer_thread.queue).empty()
        inorder.verify(indexer_thread.queue).get()
        inorder.verify(indexer_thread.index).insert(any(WebPage))


    def build_default_indexer_thread(self):
        """
          Builds a crawler thread with the default connection to the database
        """

        # The connection data to the database
        user_name = 'root'
        password = 'root'
        database_name = 'pygoogle'
        database_port = 3306

        # Build the crawler thread
        crawl_data = CrawlDataContext(database_name, database_port, user_name, password, '', False)
        indexer_thread = IndexerThread(["a queue item"], crawl_data, "an index")

        # Return the crawler thread
        return indexer_thread

