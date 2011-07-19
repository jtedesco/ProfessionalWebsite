"""
  Tests the web_page object
"""
import unittest
from Indexer.web_page import WebPage

__author__ = 'Jon Tedesco'

class WebPageTest(unittest.TestCase):
    """
      This class tests the functionality of the WebPage class, a high-level representation of a web page
    """

    def test_init(self):
        """
          Tests the web page constructor
        """

        # Prepare some sample data to test
        sample_url = "some URL!"
        sample_title = "some title!"
        sample_content = "some content!"
        sample_keywords = ["keyword 1", "keyword 2"]
        sample_links = ["link 1", "link 2"]

        # Create a web page object
        web_page = WebPage(sample_url, sample_title, sample_content, sample_keywords, sample_links)

        # Assert that the data is correct
        self.assertEquals(web_page.url, sample_url)
        self.assertEquals(web_page.title, sample_title)
        self.assertEquals(web_page.content, sample_content)
        self.assertEquals(web_page.keywords, sample_keywords)
        self.assertEquals(web_page.links, sample_links)


    def test_equals(self):
        """
          Tests the '__eq__' method, which checks equality
        """

        # Prepare some sample data to use to test
        sample_url = "some URL!"
        sample_title = "some title!"
        sample_content = "some content!"
        sample_keywords = ["keyword 1", "keyword 2"]
        sample_links = ["link 1", "link 2"]

        # Create web page objects
        web_page_1 = WebPage(sample_url, sample_title, sample_content, sample_keywords, sample_links)
        web_page_2 = WebPage(sample_url, sample_title, sample_content, sample_keywords, sample_links)
        web_page_3 = WebPage(sample_url, sample_title, sample_content, ["another keyword"], sample_links)

        # Check equality and inequality
        self.assertEquals(web_page_1, web_page_2)
        self.assertNotEquals(web_page_3, web_page_2)
