"""
  This script launches the search engine, including the indexer and crawler, creates an index object, initializes the
    PyGoogle web root, and launches the configuration using the
"""
import cherrypy
import os
from WebUI.Common.constants import *
from Common.index import Index
from Common.crawl_data_context import CrawlDataContext
from Crawler.crawler import Crawler
from Indexer.indexer import Indexer
from WebUI.pages import HomePage

__author__ = 'Jon Tedesco'

# Global imports



if __name__ == '__main__':

    # Build an index object and context object
    index = Index(INDEX_FILE, True)
    context = CrawlDataContext(DB_NAME, DB_PORT, DB_USER_NAME, DB_PASSWORD, CRAWL_LIST, True)

    # Launch the crawler
    crawler = Crawler(context)
    #crawler.start()
    print "Launched web crawler!"

    # Start the indexer
    indexer = Indexer(context, index)
    indexer.start()
    print "Launched the indexer!"

    # Open/create the stdout file in the 'logs' directory
    if os.path.exists(STDOUT_REDIRECT_FILE) and os.path.isfile(STDOUT_REDIRECT_FILE):
        os.remove(STDOUT_REDIRECT_FILE)
    stdout_file_path = STDOUT_REDIRECT_FILE

    # Build the root page and store the necessary backend data
    root_page = HomePage(index=index, indexer=indexer, crawler=crawler, context=context, stdout_file_path=stdout_file_path)

    # Initialize CherryPy with PyGoogle's configuration and launch the application
    cherrypy.quickstart(root_page, config="WebUI/Config/pygoogle.conf")

