"""
  This file holds the WebPage class, a hihg-level representation of a webpage
"""
__author__ = 'Jon Tedesco'

class WebPage():
    """
      This class represents a high-level webpage object, with the following attributes:

      <ul>
        <li> The url of the page
        <li> The title of the page
        <li> The actual raw (text) content of the page
        <li> The meta keywords of the page
        <li> The outgoing links from the page
      </ul>

      <p> This class simple acts as a data holder, and will act as the object that the indexer threads will use to index
            the crawl data
    """


    def __init__(self, url, title, content, keywords, links, excerpts=None):
        """
          Constructs a data object with the given parameters, pulled directly from the crawl data database.

           @param  url          The url whose data this object holds
           @param  title        The title of the page
           @param  content      The raw content parsed from the web page
           @param  keywords     The list of keywords tagged in the metadata of the page
           @param  links        The outgoing links for this page
           @param  excerpts     A list of strings formatted in html, with highlighted excerpts using search terms
        """
        self.url = url
        self.title = title
        self.content = content
        self.keywords = keywords
        self.links = links
        self.excerpts = excerpts

        
    def __eq__(self, other):
        """
          Checks if other is equal to self

          @param    other  The page to which to compare this page
        """

        if other.url != self.url:
            return False
        if other.title != self.title:
            return False
        if other.content != self.content:
            return False
        if other.keywords != self.keywords:
            return False
        if other.links != self.links:
            return False
        return True
