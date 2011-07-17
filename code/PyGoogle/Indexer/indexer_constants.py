"""
  Holds string constants for the indexer
"""

__author__ = 'Jon Tedesco'

# Defaults for data output of indexer
DEFAULT_QUEUE_FILE = "Demo/indexer_queue"
DEFAULT_HOST = "localhost"
DEFAULT_INDEX_DIRECTORY = "../Demo/index"
DEFAULT_TEXT_INDEX_FILE = "Demo/text_index"
DEFAULT_GRAPH_INDEX_FILE = "Demo/graph_index"
DEFAULT_INDEXED_LIST_FILE = "indexed_list"

# Common error messages
ERROR_PULLING_FROM_QUEUE = "Error pulling web page from queue: %s"
GENERIC_RUN_INDEXER_MESSAGE = "Error starting indexer: %s"
ERROR_INITIALIZING_THREADS_MESSAGE = "Error initializing thread %d"
ERROR_CREATING_DOCUMENT = "Error creating document to add to indexer: %s"
ERROR_WRITING_DOCUMENT = "Error creating document to add to indexer: %s"
ERROR_SYNCING_INDEX = "Error syncing the index to disk: %s"
ERROR_UPDATING_GRAPH = "Error adding url to graph: %s"

# Common Messages
INSERTING_WEB_PAGE = "Inserting web page into index: %s"

# Database Queries
RETRIEVE_ALL_EXCEPT_QUERY = "SELECT * FROM %s WHERE url NOT IN (%s);"
RETRIEVE_LINKS_QUERY = "SELECT link from crawl_data_links WHERE url='%s';"
RETRIEVE_KEYWORDS_QUERY = "SELECT keyword from crawl_data_keywords WHERE url='%s';"

# Database Connection Data
CRAWL_DATA_TABLE = "crawl_data"
CRAWL_DATA_KEYWORDS_TABLE = "crawl_data_keywords"
CRAWL_DATA_LINKS_TABLE = "crawl_data_links"