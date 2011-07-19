"""
  Holds the crawler class, representing the master thread for the crawler
"""

import multiprocessing
import sys
import MySQLdb
import threading
from crawler_constants import *
from url_item import UrlItem
from crawler_thread import CrawlerThread
from Common.persistent_queue import PersistentQueue

__author__ = 'Jon Tedesco'


class Crawler(threading.Thread):
    """
      The class encapsulating the master crawler thread. This thread launches worker threads to actually crawl the data,
        but being a thread itself, it will block without blocking the entire application.
    """

    def __init__(self, crawl_data_context=None):
        """
          Initializes the master crawling thread class

           @param context A 'context' object, holding the configuration data for running the crawler

           The context object contains the following attributes:
             <ul>
               <li> The name of the MySQL database to use
               <li> The port on which to connect to the MySQL database   (Defaults to 3306)
               <li> The user name by which to connect to the MySQL database (Defaults to 'root')
               <li> The password by which to connect to the MySQL database (Defaults to 'root')
               <li> The name of a file containing newline-delimited URLs from which to start crawling
             </ul>
        """

        # Initialize the crawl data context
        self.crawl_data_context = crawl_data_context

        # Call the super constructor
        threading.Thread.__init__(self)

       # Check the command line arguments
        if crawl_data_context is None:
            print CRAWLER_USAGE_ERROR_MESSAGE
            sys.exit()


    def run(self):
        """
          Runs the web crawler, using the specified options.
        """

        try:

            # Create the output tables in the database
            database = MySQLdb.connect(DEFAULT_HOST, self.crawl_data_context.user_name, self.crawl_data_context.password,
                                       self.crawl_data_context.database_name, self.crawl_data_context.database_port)
            database_cursor = database.cursor()
            database_cursor.execute(CREATE_CRAWL_DATA)
            database_cursor.execute(CREATE_CRAWL_DATA_KEYWORDS)
            database_cursor.execute(CREATE_CRAWL_DATA_LINKS)

            # Detect the number of processors on this machine
            cpu_count = multiprocessing.cpu_count()

            # Build a queue for the threads to use
            self.persistent_queue = PersistentQueue(DEFAULT_QUEUE_FILE)

            # Launch threads to handle the urls and use these initialized data structures
            self.process_urls(cpu_count, self.persistent_queue, self.crawl_data_context.urls_file_name)

        except:

            print GENERIC_RUN_CRAWLER_ERROR_MESSAGE + str(sys.exc_info())
            sys.exit()


    def process_urls(self, cpu_count, persistent_queue, urls_file_name):
        """
          Helper function that processes the urls in the file 'urlsFileName' using multithreading.

           @param  cpu_count   The number of CPUs on this machine
           @param  queue   A queue that the threads will use to traverse the web
           @param  start_urls_file_name    A file containing a newline delimited list of URLs from which to crawl
        """

        try:

            # Read in the list of URLs from the input file
            if self.crawl_data_context.verbose:
                print (READING_URL_LIST) % urls_file_name
            urls = []
            for url in open(urls_file_name, 'r'):
                urls.append(url)

        except:
            print (ERROR_READING_FILE_MESSAGE) % str(urls_file_name)

        # Create a list of threads
        self.threads = []

        # For each url we want to crawl from, crawl until there are no more links
        for url in urls:

            # Create a url queue item with the parent of this url
            url_item = UrlItem(url.strip('\n').strip('\r'))

            # Add this URL to the queue
            persistent_queue.put(url_item)

        # Build and launch a bunch of crawler threads for each processor available starting from this url, giving it the
        #   necessary data structures
        for thread_number in range(1, 3*cpu_count+1):

            try:

                # Build the crawler thread, add it to the list, and launch it
                thread = CrawlerThread(self.crawl_data_context, persistent_queue, self.crawl_data_context.verbose)
                self.threads.append(thread)
                thread.start()

                if self.crawl_data_context.verbose:
                    print (INITIALIZING_THREADS_MESSAGE) % thread_number

            except:
                print (ERROR_INITIALIZING_THREADS_MESSAGE) % thread_number
                sys.exit()

        # Wait for threads to be finished, should block this thread of computation until crawler is done. If it never finishes,
        #   this thread will sleep forever
        for thread in self.threads:
            thread.join()