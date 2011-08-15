"""
  This file holds the CrawlerThread class, essentially the worker thread for the web crawler
"""

import robotparser
import threading
import urllib2
import MySQLdb
import sys
import re
from crawler_constants import *
from webpage_parser import parse_data_from_content
from urlparse import urlparse
from url_item import UrlItem
from sys import exc_info
from time import sleep

__author__ = 'Jon Tedesco'

class CrawlerThread(threading.Thread):
    """
     The main thread for a crawler. When the crawler launches, each one of these will continuously grab an item from the
       queue, crawl it, and put its results into the database via the provided database_cursor object.

     <p> Extends Python's default threading package, so that 'run' is automatically called
    """


    def __init__(self, crawl_data_context, queue, verbose):
        """
          Creates a crawler thread object from the given parameters. Note that a lock is provided for the database, because
           the MySQL database API for python is not thread safe

           @param  crawl_data_context A context object that holds the data vital for connecting to the output database
           @param  queue           a persistent queue for creating a list of links to crawl
           @param  verbose         an optional parameter to have more thorough outputs to standard out
        """

        # Call the super constructor
        threading.Thread.__init__(self)

        # Create a connection to the database for this thread to use
        self.database = MySQLdb.connect(DEFAULT_HOST, crawl_data_context.user_name, crawl_data_context.password,
                                   crawl_data_context.database_name, crawl_data_context.database_port)
        self.database_cursor = self.database.cursor()
        self.verbose = verbose
        self.queue = queue

        # A handle for halting the thread
        self.halt = False

        # A handle for only running the thread a certain number of times
        self.iterations_remaining = -1

        # Parser for the robots list that helps determine if we are allowed to fetch a particular url
        self.robots_parser = robotparser.RobotFileParser()


    def run(self):
        """
          The main entry point of the thread. This function outlines the (very) high-level actions of each crawler thread.
           Essentially, each thread will continuously grab a url from the queue, crawl it, and process its content.
        """

        while True and self.iterations_remaining != 0 and not self.halt:
            try:
                # Update the number of iterations remaining
                if self.iterations_remaining > 0:
                    self.iterations_remaining = self.iterations_remaining-1

                # Try to grab a url from the queue if it's not empty, wait for a second otherwise
                if not self.queue.empty():

                    # Pull the next url to crawl from the queue
                    queued_url_item = self.queue.get()

                    # Crawl the url!
                    self.crawl(queued_url_item)
                else:
                    sleep(0.1)
            except:
                if self.verbose:
                    print (ERROR_PULLING_FROM_QUEUE) %  str(sys.exc_info())


    def crawl(self, queued_url_item):
        """
          The method that outlines the action of actually crawling and processing each URL in the queue. Essentially, this
           method will check if we're allowed to hit this site, grab the data, parse it, and record it in the database.

           @param  queued_url  the queued url item to crawl
        """

        try:
            # Parse the raw string url into its high-level components
            url = queued_url_item.url
            parsed_url = urlparse(url)

            # Print out status updates if we want verbose output
            if self.verbose:
                print("Now Crawling: URL: %s, parent: %s") % (url, queued_url_item.parent_url)

        except:
            print "Unexpected error: %s" % (exc_info())

        try:
            # Check to see if we are allowed to grab this url using our robots parser
            self.robots_parser.set_url('http://' + parsed_url[1] + '/robots.txt')
            self.robots_parser.read()

            # If we can't fetch it, print the url message to the screen
            if not self.robots_parser.can_fetch('*', url):
                if self.verbose:
                    print (ROBOTS_RESTRICTION_ERROR) % url
                return
        except:
            pass

        try:
            # Build a request object to grab the content of the url
            request = urllib2.Request(url)
            request.add_header("User-Agent", "PyGoogle")

            # Open the URL and read the content
            opener = urllib2.build_opener()
            content = opener.open(request).read()

        except:
            # If this URL fails to load, output the problem if we want verbose output, then skip it
            if self.verbose:
                print (ERROR_LOADING_URL) % url
            return

        # Parse the title, keywords, and links from the raw content
        title, keywords, links = parse_data_from_content(content)

        # Build the absolute links for this page
        absolute_links = self.build_absolute_links(links, url)

        # Queue up the links, rewriting each link to an absolute link
        self.queue_links(absolute_links, url)

        try:

            # Insert this current page into the database, along with all its data
            self.database_cursor.execute(INSERT_INTO_CRAWL_DATA, (url, title, content))
            for keyword in keywords:
                try:
                    self.database_cursor.execute(INSERT_INTO_CRAWL_DATA_KEYWORDS, (url, keyword))
                except:
                    pass
            for absolute_link in absolute_links:
                try:
                    self.database_cursor.execute(INSERT_INTO_CRAWL_DATA_LINKS, (url, absolute_link))
                except:
                    pass
            self.database.commit()

        except:

            # Ignore errors here, because they're most likely duplicates
            pass


    def queue_links(self, absolute_links, parent_url):
        """
          Helper function to rewrite the links from the page if necessary and actually queue up the links to be processed
           by the crawler.

           @param  links       the unmodified links, parsed directly from the website
           @param  url         the url parsed by Python's built-in parser
           @param  queued_url  the queued url item that we're currently processing (used to check for self-links)
        """

        # Create a new url queue item for each link, and add each item to the queue
        for link in absolute_links:
            queue_item = UrlItem(link, parent_url)

            # First check to see that the queue doesn't already contain this item
            if not self.queue.contains(queue_item):
                self.queue.put(queue_item)


    def build_absolute_links(self, links, parent_url):
        """
          Builds a list of absoute links for a given list of relative links and a parent/root url.
        """

        # Create a new list of absolute links
        absolute_links = []

        # Get the directory of the old link
        lastForwardSlash = parent_url.rfind('/')
        parent_directory = parent_url[:lastForwardSlash]

        # Rewrite all of the given links to absolute links
        for link in links:

            # Take care of absolute links
            if link.startswith('http'):
                absolute_links.append(link)

            # Ignore links linking back to this page
            if link.startswith('#') or len(link)==0:
                continue

            # Rewrite relative-absolute links
            elif link.startswith('/'):
                link = parent_directory + link
                absolute_links.append(link)

            # Rewrite relative links
            elif not link.startswith('http'):
                link = parent_directory + '/' + link
                absolute_links.append(link)

        # A regular expression for finding 'up' directories
        up_directory_regex = re.compile("/.*?/\.\./?")

        # Iterate over the absolute links and clean them up (remove 'up' directory paths)
        clean_absolute_links = []
        for absolute_link in absolute_links:

            # Pull this out of the list
            clean_absolute_link = absolute_link

            # Find all occurrences of the 'up' pattern
            occurrences = up_directory_regex.findall(clean_absolute_link)

            # Find the location of the right most '/', before the "/../" string, and replace the occurrences with the
            #   rightmost matches
            for occurrence in occurrences:
                if(occurrence.count("/")>3):

                    # Find the occurrences of '/--some-folder/../'
                    stripped_occurrence = occurrence.rstrip("/.")
                    index_of_last_slash = stripped_occurrence.rfind("/")
                    modified_occurrence = occurrence[index_of_last_slash:]

                    # Replace this occurrence with the modified one
                    occurrences.remove(occurrence)
                    occurrences.append(modified_occurrence)


            # Keep replacing the occurrences we found until none are left
            while len(occurrences) > 0:

                # Find the location of the right most '/', before the "/../" string, and replace the occurrences with the
                #   rightmost matches
                for occurrence in occurrences:
                    if(occurrence.count("/")>3):

                        # Find the occurrences of '/--some-folder/../'
                        stripped_occurrence = occurrence.rstrip("/.")
                        index_of_last_slash = stripped_occurrence.rfind("/")
                        modified_occurrence = occurrence[index_of_last_slash:]

                        # Replace this occurrence with the modified one
                        occurrences.remove(occurrence)
                        occurrences.append(modified_occurrence)

                # Replace each occurrence
                for occurrence in occurrences:
                    clean_absolute_link = clean_absolute_link.replace(occurrence, "/")

                # Find all remaining occurrences of this pattern
                occurrences = up_directory_regex.findall(clean_absolute_link)

            # Put this link back in the list
            clean_absolute_links.append(clean_absolute_link)

        return clean_absolute_links