import unittest
from Common.search_result import SearchResult

__author__ = 'Jon Tedesco'

class SearchResultTest(unittest.TestCase):
    """
      Tests the SearchResult class, a simple data holder
    """

    def test_full_init(self):
        """
          Tests the constructor of the search result object
        """

        # Build an object with all data supplied
        pages = ['test_page']
        duration = 5
        search_terms = ['test', 'search', 'terms']
        full_search_result = SearchResult(pages, duration, search_terms)

        # Test the contents of the context
        self.assertEquals(full_search_result.pages, pages)
        self.assertEquals(full_search_result.duration, duration)
        self.assertEquals(full_search_result.search_terms, search_terms)

        
    def test_partial_init(self):
        """
          Tests the default values of the search result constructor
        """

        # Build a context with empty data
        empty_search_result = SearchResult()
        self.assertTrue(empty_search_result.pages is None)
        self.assertEquals(empty_search_result.duration, -1)
        self.assertTrue(empty_search_result.search_terms is None)