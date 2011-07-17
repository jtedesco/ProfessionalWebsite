"""
  This file holds the UrlItem class, a simple data holder for urls and their parents urls
"""

__author__ = 'Jon Tedesco'

class UrlItem:
    """
      Simple data container object that holds a url and its parent url. This is used the queue so that for any URL, we can
       finding its source is trivial
    """


    def __init__(self, url, parent_url=""):
        """
          Initializes a queue item

           @param  url         the url to add
           @param  parent_url  the parent url of the url
        """

        self.url = url
        self.parent_url = parent_url