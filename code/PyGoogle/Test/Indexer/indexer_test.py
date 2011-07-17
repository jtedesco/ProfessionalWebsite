"""
  Tests the Indexer class, or the master thread for the indexer
"""
import unittest
import threading
import os
from Indexer.indexer_constants import *
from Indexer.indexer import Indexer
from Common.crawl_data_context import CrawlDataContext
from mockito import *
from Common.persistent_queue import PersistentQueue

__author__ = 'Jon Tedesco'

class IndexerTest(unittest.TestCase):
    """
      This class tests the functionality of the Index class, the master indexer thread
    """

    def test_init(self):
        """
          Tests the Indexer constructor
        """

        # Prepare some sample data to test
        sample_crawl_data = "a crawl data context object"
        sample_index = "an index object"

        # Create a web page object
        indexer = Indexer(sample_crawl_data, sample_index)

        # Assert that the data is correct
        self.assertEquals(indexer.crawl_data_context, sample_crawl_data)
        self.assertEquals(indexer.index, sample_index)

        # Assert the the indexer is a thread
        self.assertTrue(isinstance(indexer, threading.Thread))


    def test_run(self):
        """
          Tests the 'run' method, which builds a queue, launches the worker threads, and pulls data from the database
        """

        # Create an indexer thread
        indexer = self.build_default_indexer()

        # Make the indexer halts after one iteration
        indexer.halt = True

        # Stub out the 'launch threads' method so we don't actually launch any threads
        when(indexer).launch_indexer_threads(any(CrawlDataContext), any(int), any(PersistentQueue), None).thenReturn([])

        # Run the indexer
        indexer.threads = []
        indexer.run()

        # Verify the persistent queue
        self.assertTrue(isinstance(indexer.persistent_queue, PersistentQueue))
        self.assertTrue(os.path.exists(DEFAULT_QUEUE_FILE))
        
        # Verify that we launched the threads (tried)
        verify(indexer).launch_indexer_threads(any(CrawlDataContext), any(int), any(PersistentQueue), None)

        # Cleanup the queue file
        indexer.persistent_queue.file.close()
        os.remove(DEFAULT_QUEUE_FILE)


    def build_default_indexer(self):
        """
          Builds an indexer master thread with the default connection to the database
        """

        # The connection data to the database
        user_name = 'root'
        password = 'root'
        database_name = 'pygoogle'
        database_port = 3306

        # Build the indexer
        crawl_data = CrawlDataContext(database_name, database_port, user_name, password, '', False)
        crawler_thread = Indexer(crawl_data, None)

        # Return the indexer
        return crawler_thread

