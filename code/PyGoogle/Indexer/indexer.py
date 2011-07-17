"""
  This file holds the Indexer class, which represents the master thread for the indexer
"""

import multiprocessing
import sys
import threading
import MySQLdb
from Common.persistent_queue import PersistentQueue
from Indexer.indexer_constants import *
from Crawler.crawler_constants import INITIALIZING_THREADS_MESSAGE
from Indexer.indexer_thread import IndexerThread

__author__ = 'Jon Tedesco'

class Indexer(threading.Thread):
    """
      This class extends the threading module, so that this 'master' indexer thread won't block 
    """

    def __init__(self, crawl_data_context, index):
        """
          Initializes the main indexer thread

            @param  crawl_data_context  The context object holding information about the connection to the crawler database
            @param  index   The index object into which our indexed data will be inserted. This will also synced with disk,
                                and accessible from outside this module
        """

        # Initialize the data for this thread
        self.crawl_data_context = crawl_data_context
        self.index = index

        # A handle to only iterate a certain number of times through the database or stop the indexer
        self.halt = False
        self.iterations_remaining = -1

        # Keep track of number of indexed items
        self.count = 0

        # Call the super constructor
        threading.Thread.__init__(self)

        
    def run(self):
        """
         Function that will launch the indexer, given the data about the crawler configuration. Specifically, this function
           will grab pages from the crawl data one by one, and distribute them to worker threads to be indexed and analyzed.

         <p> Rather than ensuring that pages are processed in order correctly, if we find a link to a page that hasn't been
                crawled yet, we will create an empty entry for that missing link, and fill it in when it is crawled later.

         <p> Likewise, to prevent any data loss and to keep the crawler and indexer modular, the indexer will not remove entries
                from the crawl data databases as it indexes entries, but will rather perform intelligent queries on the crawl
                data, ensuring that results have not already been grabbed.

            @see    Common.index
        """

        try:
            # Detect the number of CPUs that this machine has
            cpu_count = multiprocessing.cpu_count()

            # Build a queue for the threads to use
            self.persistent_queue = PersistentQueue(DEFAULT_QUEUE_FILE)

        except:
            print (GENERIC_RUN_INDEXER_MESSAGE) % str(sys.exc_info())
            sys.exit()

        # Launch the indexer threads
        self.launch_indexer_threads(self.crawl_data_context, cpu_count, self.persistent_queue, self.index)

        # Connect to the crawler database
        database = MySQLdb.connect(DEFAULT_HOST, self.crawl_data_context.user_name, self.crawl_data_context.password,
                                   self.crawl_data_context.database_name, self.crawl_data_context.database_port)
        database_cursor = database.cursor()

        # Create a comma separated list of
        retrieved_urls_comma_separated_list = "'dummy'"

        while True and self.iterations_remaining != 0 and not self.halt:

            # Update the number of iterations remaining
            if self.iterations_remaining > 0:
                self.iterations_remaining = self.iterations_remaining-1
            self.count = self.count+1

            # Fetch all entries from this table that we haven't already fetched
            query = (RETRIEVE_ALL_EXCEPT_QUERY % (CRAWL_DATA_TABLE, retrieved_urls_comma_separated_list))
            database_cursor.execute(query)
            results = database_cursor.fetchall()

            # Process the data we got
            for row in results:

                # Pull out the basic data from the tuple
                url, title, content = row

                # Add the url to the comma-separated list of retrieved urls
                retrieved_urls_comma_separated_list = retrieved_urls_comma_separated_list + ", '" + url + "'"

                # Put this tuple into the queue for the indexer threads
                self.persistent_queue.put(row)


        # Stop all the worker threads
        for thread in self.threads:
            thread.halt = True


    def launch_indexer_threads(self, crawl_data_context, cpu_count, persistent_queue, index):
        """
          Function to launch the worker threads to do the polling of the database

           @param  crawl_data_context   A context variable holding information about connecting to the crawler data database
           @param  cpu_count            The number of processors available on this machine
           @param  persistent_queue     The shared queue for the indexer threads
           @param  index                The composite index object into which all 
        """

        # Launch a number of threads proportional to the number of concurrent threads this machine can run, and keep a list
        #   of the active threads
        self.threads = []
        for thread_number in range(1, cpu_count+1):

            try:

                # Build the indexer thread, add it to the list, and launch it
                thread = IndexerThread(persistent_queue, crawl_data_context, index)
                self.threads.append(thread)
                thread.start()

                if crawl_data_context.verbose:
                    print (INITIALIZING_THREADS_MESSAGE) % thread_number

            except:
                print (ERROR_INITIALIZING_THREADS_MESSAGE) % thread_number, str(sys.exc_info())
                sys.exit()

        # Return the list of thread object
        return self.threads
