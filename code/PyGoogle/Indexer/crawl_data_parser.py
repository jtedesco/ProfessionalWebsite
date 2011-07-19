"""
  This file holds utilities for parsing data from the crawl database data
"""

from indexer_constants import *
from Indexer.web_page import WebPage

__author__ = 'Jon Tedesco'

def parse_crawl_data_item(database_data, database_cursor):
    """
      This function will take in a database entry directly, and its data into a CrawlDataItem

       @param   database_data       The result directly from the database
       @param   database_cursor     A handle on the database to perform queries
    """

    # Grab the url, title, and content of this row in the database
    url, title, content = database_data

    # Grab the corresponding links from the database
    links = []
    database_cursor.execute((RETRIEVE_LINKS_QUERY % url))
    for link_row in database_cursor.fetchall():
        links.append(link_row[0])

    # Grab the corresponding keywords from the database
    keywords = []
    database_cursor.execute((RETRIEVE_KEYWORDS_QUERY % url))
    for keyword_row in database_cursor.fetchall():
        keywords.append(keyword_row[0])

    # Create a WebPage object from this data and add it to our list
    web_page = WebPage(url, title, content, keywords, links)

    # Return the url itself and the web page object for this url
    return url, web_page
