"""
  This file tests the crawler thread (worker thread) functionality
"""
import unittest
import robotparser
import threading
from Crawler.crawler_constants import *
from Crawler.crawler_thread import CrawlerThread
from Common.crawl_data_context import CrawlDataContext
from mockito import *
from Crawler.url_item import UrlItem
from time import sleep

__author__ = 'Jon Tedesco'

class CrawlerThreadTest(unittest.TestCase):
    """
      Class that tests the crawler thread
    """

    def test_init(self):
        """
          Tests that:
            <ul>
            <li> The object is really a thread object
            <li> All object data was correctly initialized
            </ul>
        """

        # Build a crawler thread with this data
        actual_crawler_thread = self.build_default_crawler_thread()

        # Check that the object is, actually, a thread object
        self.assertTrue(isinstance(actual_crawler_thread, threading.Thread))

        # Compare the data for this object
        self.assertEquals(False, actual_crawler_thread.verbose)
        self.assertEquals([], actual_crawler_thread.queue)
        self.assertTrue(isinstance(actual_crawler_thread.robots_parser, robotparser.RobotFileParser))


    def test_run(self):
        """
          Tests that 'run' calls the expected functions in the correct order
        """

        # Create a crawler thread
        crawler_thread = self.build_default_crawler_thread()

        # Now, take this thread object, and build a mocked out queue object and spy on the thread
        crawler_thread.queue = mock()
        when(crawler_thread.queue).empty().thenReturn(False)
        when(crawler_thread.queue).get().thenReturn(None)
        when(crawler_thread).crawl(None).thenReturn(True)

        # Do a 'run'
        crawler_thread.iterations_remaining = 1
        crawler_thread.start()
        sleep(0.1)
        crawler_thread.halt = True

        # Assert that the correct methods were called on the queue and the thread, in order
        inorder.verify(crawler_thread.queue).empty()
        inorder.verify(crawler_thread.queue).get()
        inorder.verify(crawler_thread).crawl(None)


    def test_crawl(self):
        """
          Test the 'crawl' method

          <p> Checks the following:

          <ul>
            <li> We use the robot parsing to implement politeness
            <li> We load the url
            <li> We queue and build the absolute links
            <li> We insert the data into the database
          </ul>
        """

        # Build a crawler thread
        crawler_thread = self.build_default_crawler_thread()

        # Mock out the robot parser and connection to the database
        crawler_thread.robots_parser = mock()
        crawler_thread.database_cursor = mock()
        crawler_thread.database = mock()

        # Stub out the other methods for the crawler
        when(crawler_thread).build_absolute_links(any(list), any(str)).thenReturn(['absolute_link'])
        when(crawler_thread).queue_links(any(list), any(str)).thenReturn(True)
        when(crawler_thread.robots_parser).can_fetch(any(str), any(str)).thenReturn(True)

        # Build the url item parameter to the method
        url_item = UrlItem("http://www.google.com/", "")

        # Crawl!
        crawler_thread.crawl(url_item)

        # Verify that the correct methods were called, in the correct order
        inorder.verify(crawler_thread.robots_parser).set_url("http://www.google.com/robots.txt")
        inorder.verify(crawler_thread.robots_parser).read()
        inorder.verify(crawler_thread.robots_parser).can_fetch("*", url_item.url)
        inorder.verify(crawler_thread).build_absolute_links(any(list), any(str))
        inorder.verify(crawler_thread).queue_links(['absolute_link'], any(str))
        inorder.verify(crawler_thread.database).commit()


    def test_queue_links(self):
        """
          Test the 'queue_links' function
        """

        # Create a crawler thread
        crawler_thread = self.build_default_crawler_thread()

        # Mock out the queue
        crawler_thread.queue = mock()

        # Stub out the behavior of the queue
        when(crawler_thread.queue).contains(any(UrlItem)).thenReturn(False)
        when(crawler_thread.queue).put(any(UrlItem)).thenReturn(True)

        # Queue up some bogus links
        absolute_links = ["absolute_link_1"]
        parent_url = "parent_url"
        crawler_thread.queue_links(absolute_links, parent_url)

        # Verify that the correct methods were called
        inorder.verify(crawler_thread.queue).contains(any(UrlItem))
        inorder.verify(crawler_thread.queue).put(any(UrlItem))


    def test_build_absolute_links(self):
        """
          Test the 'build_absolute_links' function given some sample data
        """

        # Build the crawler thread
        crawler_thread = self.build_default_crawler_thread()

        # Build some sample links to parse
        sample_links = ["#section", "/relative", "another_relative", "http://www.some_absolute_link.com"]
        parent_url = "parent_url/"
        expected_absolute_links = ["parent_url/relative", "parent_url/another_relative", "http://www.some_absolute_link.com"]

        # Build the absolute links
        actual_absolute_links = crawler_thread.build_absolute_links(sample_links, parent_url)

        # Check that the actual links match the expected ones
        self.assertEquals(expected_absolute_links, actual_absolute_links)


    def build_default_crawler_thread(self):
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
        crawler_thread = CrawlerThread(crawl_data, [], False)

        # Return the crawler thread
        return crawler_thread

