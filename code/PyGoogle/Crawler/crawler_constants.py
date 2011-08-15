"""
 This file holds a list of global constants for use in the web crawler, such as database constants that we define
   in the crawler.
"""

__author__ = 'Jon Tedesco'


#  Defaults for the database access of the crawler
DEFAULT_HOST = "localhost"
DEFAULT_USER_NAME = "root"
DEFAULT_PASSWORD = "root"
DEFAULT_MYSQL_PORT = 3306
RESULT_TABLE_NAME = "crawl_data"
DEFAULT_QUEUE_FILE = "Demo/crawler_queue"

#  Common query forms to the database
CREATE_CRAWL_DATA = """CREATE TABLE IF NOT EXISTS crawl_data (url VARCHAR(256) PRIMARY KEY, title VARCHAR(256), content LONGTEXT);"""
CREATE_CRAWL_DATA_KEYWORDS = """CREATE TABLE IF NOT EXISTS crawl_data_keywords (url VARCHAR(256), keyword VARCHAR(256));"""
CREATE_CRAWL_DATA_LINKS = """CREATE TABLE IF NOT EXISTS crawl_data_links (url VARCHAR(256), link VARCHAR(256));"""
INSERT_INTO_CRAWL_DATA = """INSERT INTO crawl_data VALUES (%s, %s, %s);"""
INSERT_INTO_CRAWL_DATA_KEYWORDS = """INSERT INTO crawl_data_keywords VALUES (%s, %s);"""
INSERT_INTO_CRAWL_DATA_LINKS = """INSERT INTO crawl_data_links VALUES (%s, %s);"""

#  Common error messages
ERROR_INSERTING_CRAWL_DATA = "Error inserting url crawl data into database: %s"
ERROR_PULLING_FROM_QUEUE = "Error pulling url from queue: %s"
ERROR_PULLING_FROM_QUEUE = "Error pulling data from queue: %s"
ERROR_LOADING_URL = "Failed to load URL: %s"
ROBOTS_RESTRICTION_ERROR = "Cannot fetch url: %s. Not allowed by robots.txt!"
ERROR_INITIALIZING_THREADS_MESSAGE = "Error initializing thread %d"
ERROR_READING_FILE_MESSAGE = "Error when reading file: %s"""
GENERIC_RUN_CRAWLER_ERROR_MESSAGE = "Error starting crawler! Exception encountered!"
CRAWLER_USAGE_ERROR_MESSAGE = """Error in usage! Please specify:
                                \n\t (1) the name of the MySQL database to use
                                \n\t (2) the name of the file containing starting URLs
                                \n\t (3) the user name with which to connect to the MySQL database
                                \n\t (4) the password with which to connect to the MySQL database"""

#  Common messages
INITIALIZING_THREADS_MESSAGE = "Initialized thread %d"
READING_URL_LIST = "Reading URL list from %s"