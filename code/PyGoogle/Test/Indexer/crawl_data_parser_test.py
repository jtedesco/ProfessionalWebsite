"""
  This file holds tests for the crawl_data_parser file
"""
import unittest
from mockito import *
from Indexer.crawl_data_parser import parse_crawl_data_item
from Indexer.indexer_constants import *
from Indexer.web_page import WebPage

__author__ = 'Jon Tedesco'

class CrawlDataParserTest(unittest.TestCase):
    """
      This class tests the functionality of the crawl data parser
    """

    def test_parse_crawl_data_item(self):
        """
          Tests that the parse_crawl_data_item function calls the database as expected, and produces the expected result
            given some sample data
        """

        # Create some sample data to parse
        sample_database_data = ("www.google.com", "Google", "THIS IS GOOGLE!!")
        expected_web_page = WebPage("www.google.com", "Google", "THIS IS GOOGLE!!", ["sample_data"], ["sample_data"])

        # Mock out the database connection (cursor)
        mock_database_cursor = mock()

        # Stub out the behavior of the database cursor when queried with the right data
        when(mock_database_cursor).fetchall().thenReturn([("sample_data",)])

        # Parse this data
        actual_url, actual_web_page = parse_crawl_data_item(sample_database_data, mock_database_cursor)

        # Verify that the correct methods were called on the cursor
        inorder.verify(mock_database_cursor).execute(RETRIEVE_LINKS_QUERY % "www.google.com")
        inorder.verify(mock_database_cursor, times(2)).fetchall()
        inorder.verify(mock_database_cursor).execute(RETRIEVE_KEYWORDS_QUERY % "www.google.com")
        inorder.verify(mock_database_cursor, times(2)).fetchall()

        # Assert that the return values are what we expect
        self.assertEquals(actual_url, "www.google.com")
        self.assertEquals(actual_web_page, expected_web_page)
