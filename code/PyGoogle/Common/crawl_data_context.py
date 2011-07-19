"""
  Holds the data for connecting to the crawl data database
"""

from Crawler.crawler_constants import *

__author__ = 'Jon Tedesco'

class CrawlDataContext:
    """
     This class holds the data required for the configuration of the web crawler, and will be accessed by both the crawler
       itself and the indexer to implement the search engine.

      The class itself contains the following data:

       1) The name of the MySQL database to use
       2) The port on which to connect to the MySQL database   (Defaults to 3306)
       3) The user name by which to connect to the MySQL database (Defaults to 'root')
       4) The password by which to connect to the MySQL database (Defaults to 'root')
       5) The name of a file containing newline-delimited URLs from which to start crawling
    """


    def __init__(self, database_name=None, database_port=DEFAULT_MYSQL_PORT, user_name=DEFAULT_USER_NAME,
                 password=DEFAULT_PASSWORD, urls_file_name=None, verbose=False):
        """
         The constructor for this object, which takes in all the parameters required for building a crawler context

            @param  database_name   The name of the database to connect  to
            @param  database_port   The port on which to connect to the database
            @param  user_name       The username for the database connection
            @param  password        The password for the database connection
            @param  urls_file_name  The list of urls from which to begin crawling
            @param  verbose         Flag for printing out more thorough debugging information
        """

        self.database_name = database_name
        self.database_port = database_port
        self.user_name = user_name
        self.password = password
        self.urls_file_name = urls_file_name
        self.verbose = verbose