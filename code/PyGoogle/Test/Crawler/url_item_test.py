import unittest
from Crawler.url_item import UrlItem

__author__ = 'Jon Tedesco'

class UrlItemTest(unittest.TestCase):
    """
      Tests the UrlItem class, a simple data holder for pairs of urls and their parents in the queue
    """

    def test_full_init(self):
        """
          Tests the constructor of the url item object
        """

        # Build an object with all data supplied
        full_url_item = UrlItem('test_url','test_parent_url')

        # Test the contents of the url item
        self.assertEquals(full_url_item.url, 'test_url')
        self.assertEquals(full_url_item.parent_url, 'test_parent_url')

        
    def test_partial_init(self):
        """
          Tests the default values of the UrlItem class
        """

        # Build a url item with no data
        partial_url_item = UrlItem(None)
        self.assertTrue(partial_url_item.url is None)
        self.assertEquals(partial_url_item.parent_url, "")
