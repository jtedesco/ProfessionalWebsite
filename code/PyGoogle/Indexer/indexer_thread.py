"""
  Holds the indexer thread class, which acts the worker thread for the indexer
"""

import threading
import MySQLdb
import sys
from indexer_constants import *
from Indexer.crawl_data_parser import parse_crawl_data_item
from time import sleep

__author__ = 'Jon Tedesco'

class IndexerThread(threading.Thread):
    """
      This class represents a thread for pulling data from the database and constructing an web page object to be indexed.
        In this implementation, multiple threads, in this class, will poll the crawl data database while the master thread
        takes the resulting web pages and inserts them into the index.

      <p> This design is motivated by the fact that little computation will actually be achieved by multithreading the index
            itself, since the index must be thread safe and keep all parts of it in sync. Rather, more computation time
            will be used polling the database for pages.
    """


    def __init__(self, queue, crawl_data_context, index):
        """
          The constructor for an indexer thread, which takes all shared data that the thread will need

           @param  crawl_data_context               The configuration data for the crawler data database
           @param  queue                            A shared queue for indexing page data
           @param  index                            The shared index in which to put the site data
        """

        # Call the super constructor
        threading.Thread.__init__(self)

        # Initialize the data for this thread
        self.crawl_data_context = crawl_data_context
        self.queue = queue
        self.index = index

        # Handle to control how many times this thread iterates
        self.iterations_remaining = -1

        # Handle to halt the thread
        self.halt = False

        # Create a database connection for this thread
        database = MySQLdb.connect(DEFAULT_HOST, self.crawl_data_context.user_name, self.crawl_data_context.password,
                                   self.crawl_data_context.database_name, self.crawl_data_context.database_port)
        self.database_cursor = database.cursor()


    def run(self):
        """
          The main entry point of the thread. This function outlines the (very) high-level actions of each indexer thread.
           Essentially, each thread will continuously grab entries from the shared queue and insert it into the index.
        """

        # Indefinitely loop, pulling data out of the queue and putting it into the index
        while True and not self.halt and self.iterations_remaining != 0:
            try:
                # Update the iterations remaining
                if self.iterations_remaining > 0:
                    self.iterations_remaining = self.iterations_remaining-1

                # Try to grab a web page from the queue if it's not empty, wait for an instant otherwise
                if not self.queue.empty():

                    # Pull the next url to index out of the queue
                    basic_page_data = self.queue.get()

                    # Parse the basic page data into a full web page object and url
                    url, web_page = parse_crawl_data_item(basic_page_data, self.database_cursor)

                    # Send this page over to the index
                    self.index.insert(web_page)

                    if self.crawl_data_context.verbose:
                        print (INSERTING_WEB_PAGE) % web_page.url

                else:
                    sleep(0.1)
            except:
                print (ERROR_PULLING_FROM_QUEUE) %  str(sys.exc_info())

