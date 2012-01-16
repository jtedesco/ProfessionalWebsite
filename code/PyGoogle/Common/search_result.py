"""
  Stores the data of search result
"""

__author__ = 'Jon Tedesco'


class SearchResult:
    """
      This class stores a list of web pages as results, and a time value associated with the time it took to perform the
        search
    """

    def __init__(self, pages=None, duration=-1, search_terms=None, suggestions=None):
        """
          Builds a search result object, given a list of results and the time for the search to be performed

            @param  pages           A list of web page results
            @param  duration        The time taken to perform the query
            @param  search_terms    The terms used in the user's search to be used for highlighting
            @param  suggestions     A list of words that have been 'suggested' by the spell checker
        """

        self.pages = pages
        self.duration = duration
        self.search_terms = search_terms
        self.suggestions = suggestions