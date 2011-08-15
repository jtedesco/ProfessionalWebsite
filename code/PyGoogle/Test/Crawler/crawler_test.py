"""
  This file checks the 'crawler' file, which launches the crawler threads
"""
import unittest
import threading
import os
from Common.crawl_data_context import CrawlDataContext
from Crawler.crawler import Crawler
from mockito import *
from Common.persistent_queue import PersistentQueue
from Crawler.crawler_constants import *

__author__ = 'Jon Tedesco'


class CrawlerTest(unittest.TestCase):
    """
      This class tests the crawler
    """

    def test_init(self):
        """
          Tests the constructor, specifically that the crawler is a thread and that the data is initialized correctly
        """

        # Build the crawl data object
        crawl_data = self.build_default_crawl_data()

        # Actually build the crawler
        crawler = Crawler(crawl_data)

        # Verify that the crawler is a thread and contains the crawl_data object
        self.assertTrue(isinstance(crawler, threading.Thread))
        self.assertEquals(crawler.crawl_data_context, crawl_data)


    def test_run(self):
        """
          Tests that the crawler thread actually builds a persistent queue and starts processing the urls
        """

        # Create a crawl data context object and the crawler
        crawl_data = self.build_default_crawl_data()
        crawler = Crawler(crawl_data)

        # Stub out the 'process_urls' method
        when(crawler).process_urls(any(int), any(PersistentQueue), any(str)).thenReturn(True)

        # 'Run' the crawler, which will create the database table if they don't already exist
        crawler.run()

        # Verify that process_urls was called
        verify(crawler, times(1)).process_urls(any(int), any(PersistentQueue), any(str))

        # Cleanup the file from the persistent queue
        crawler.persistent_queue.file.close()
        os.remove(DEFAULT_QUEUE_FILE)


    def build_default_crawl_data(self):
        """
          Builds the default crawl data object for connection to a local database
        """

        # The connection data to the database
        user_name = 'root'
        password = 'root'
        database_name = 'pygoogle'
        database_port = 3306

        # Build the crawler thread
        crawl_data = CrawlDataContext(database_name, database_port, user_name, password, '', False)
        return crawl_data
