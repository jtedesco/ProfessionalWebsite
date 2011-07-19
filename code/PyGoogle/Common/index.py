"""
  Forms the composite index for the indexer
"""

from Indexer.indexer_constants import *
from Indexer.text_index import TextIndex
from Indexer.graph_index import GraphIndex
import cPickle
from datetime import datetime
import sys
from Common.search_result import SearchResult
import threading

__author__ = 'Jon Tedesco'

class Index:
    """
      Class that acts as a composite structure for the graph and text index, allowing us to insert and delete entries from
       the index using this index alone.

      This class syncs its concrete index implementations to disk and attempts to restore them on initialization
    """

    
    def __init__(self, index_dir=DEFAULT_INDEX_DIRECTORY, verbose=False):
        """
          Constructs the index, with empty graph and text indexing objects

			@param	index_dir	The directory in which to place the index data
			@param	verbose		Whether or not this index should output verbose errors
        """

        # Create a text index (implemented with Whoosh!)
        self.text_index = TextIndex(index_dir, verbose)

        try:

            # Initialize basic attributes of this index
            self.file_name = DEFAULT_GRAPH_INDEX_FILE
            self.verbose = verbose

            # Create a lock for this object
            self.lock = threading.Lock()

            # Attempt to load this graph index from disk (Text index will load itself from disk)
            graph_index_file = file(self.file_name, 'r')
            self.graph_index = cPickle.load(graph_index_file)
            graph_index_file.close()

        except IOError, err:

            # If the file doesn't exist, continue, the file will be created, otherwise, we have a problem
            if err.errno == 2:

                # Create a new graph index
                self.graph_index = GraphIndex(verbose)

                # Write our data to disk initially
                graph_index_file = file(self.file_name, 'w')
                cPickle.dump(self.graph_index, graph_index_file, 1)
                graph_index_file.close()

            else:
                raise err

        except EOFError:

            #If this was the end of the file, it was either empty or null
            self.graph_index = GraphIndex(verbose)


    def insert(self, page):
        """
          Inserts a url into the index, parsing its content, title, keywords, links, and parent url as it inserts it into
            the database

            @param  page Holds the key information about the page used for indexing
            @retval		True or False, on corresponding success or failure
        """

        # Attempt to insert this into the text index first
        self.lock.acquire()
        if not self.text_index.insert(page):
            self.lock.release()
            return False

        # Attempt to insert this into the graph index
        if not self.graph_index.insert(page):
            self.lock.release()
            return False

        # Sync these changes to disk
        self.sync()
        self.lock.release()
		
        # Success!
        return True


    def query(self, query):
        """
          Queries the index for data with the given text query

            @param  query   The text query to perform on the indexed data
            @retval			A list of page objects representing the results
        """

        # Record the start time of the query
        start_time = datetime.now()

        # Perform the query itself
        self.lock.acquire()
        page_results, search_terms, spelling_suggestions = self.text_index.query(query)
        self.lock.release()

        # Calculate the duration of the query
        duration = datetime.now() - start_time

        # Create the actual ranked results
        search_result = SearchResult(page_results, duration, search_terms, spelling_suggestions)
        
        # Augment the rankings using the a graph structure and the pagerank algorithm
        re_ranked_result = self.graph_index.rerank(search_result, len(search_result.pages)/3+1)
        
        # Return the search result
        return re_ranked_result
		

    def sync(self):
        """
          Syncs the indeces of this composite index to disk, to their respective files

            @retval Whether the syncing was successful
        """

        try:
            # Try to sync the graph to disk
            graph_index_file = file(self.file_name, 'w')
            graph_index_file.seek(0)
            cPickle.dump(self.graph_index, graph_index_file, 1)
            graph_index_file.flush()

        except:
            if self.verbose:
                print (ERROR_SYNCING_INDEX) % str(sys.exc_info())
            return False

        # Success
        return True