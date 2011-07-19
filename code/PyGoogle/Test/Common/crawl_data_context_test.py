import unittest
from Common.crawl_data_context import CrawlDataContext
from Crawler import crawler_constants

__author__ = 'Jon Tedesco'

class CrawlDataContextTest(unittest.TestCase):
    """
      Tests the CrawlDataContext class, a simple data holder
    """

    def test_full_init(self):
        """
          Tests the constructor of the crawl data context object
        """

        # Build an object with all data supplied
        full_context = CrawlDataContext('test_database_name', 1, 'test_user_name', 'test_password', 'test_file_name', True)

        # Test the contents of the context
        self.assertEquals(full_context.database_name, 'test_database_name')
        self.assertEquals(full_context.database_port, 1)
        self.assertEquals(full_context.user_name, 'test_user_name')
        self.assertEquals(full_context.password, 'test_password')
        self.assertEquals(full_context.urls_file_name, 'test_file_name')
        self.assertTrue(full_context.verbose)

        
    def test_partial_init(self):
        """
          Tests the default values of the crawl data context class
        """

        # Build a context with empty data
        missing_verbose_context = CrawlDataContext()
        self.assertTrue(missing_verbose_context.database_name is None)
        self.assertTrue(missing_verbose_context.urls_file_name is None)
        self.assertFalse(missing_verbose_context.verbose)
        self.assertEquals(missing_verbose_context.database_port, crawler_constants.DEFAULT_MYSQL_PORT)
        self.assertEquals(missing_verbose_context.user_name, crawler_constants.DEFAULT_USER_NAME)
        self.assertEquals(missing_verbose_context.password, crawler_constants.DEFAULT_PASSWORD)
