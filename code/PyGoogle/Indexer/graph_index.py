"""
  This file holds the graph index, a graph structure used to store the link structure of crawled data
"""

import itertools
from Lib.pygraph.classes.digraph import digraph
from Lib.pygraph.algorithms.pagerank import pagerank
from Lib.pygraph.algorithms.searching import breadth_first_search
from Lib.pygraph.readwrite.markup import write
from indexer_constants import *
from operator import itemgetter

__author__ = 'Jon Tedesco'

class GraphIndex:
    """
      Class that acts as a structure for the graph index, used by the CompositeIndex class.  This class is also synced with 
        disk, so that the index can be restored when resuming the indexer. This class is essentially used for creating the link
        structure of the pages as they are read from the database.

      <p>  In the graph structure, the nodes will be indexed by the page's url, and its attribute will be the page object
        containing the data for that url
    """


    def __init__(self, verbose):
        """
          Constructs an empty graph and text indexing objects

			@param	verbose		Whether or not this index should output verbose errors
        """

        # Build an empty directed graph
        self.graph = digraph()

        # Record verboseness
        self.verbose = verbose


    def insert(self, page):
        """
          Inserts a page into the index, using its url as the unique 'key' for the node, and adding all
            all connected edges and nodes in the graph.

            @param  page Holds the key information about the page used for indexing
            @retval		True or False, on corresponding success or failure
        """

        try:
            # Add this url to the graph if it doesn't already exist
            if not self.graph.has_node(page.url):
                self.graph.add_node(page.url, [page]) #Attributes must be a list

            # If we added a skeleton node before, add this page object as the node attribute
            elif self.graph.node_attributes(page.url) is None:
                self.graph.add_node_attribute(page.url, page)

            # Add all links from this page to the graph structure as skeleton nodes
            for link in page.links:
                if not self.graph.has_node(link):
                    self.graph.add_node(link, [])

            # Add the outgoing links for this graph
            for link in page.links:
                if not self.graph.has_edge((page.url, link)):
                    self.graph.add_edge((page.url, link))
                
        except:
            if self.verbose:
                print (ERROR_UPDATING_GRAPH) % page.url
            return False

        # Success!
        return True


    def visualize_tree(self, file_name, root_url=None):
        """
          Renders the graph structure of the pages passed in, or of the entire structure in memory, and
            returns this visualization

            @param  file_name   The name of the file in which to output the graph visualization
            @param  root_url    The node in the graph to use as the root of the tree
        """

        # Get the spanning tree from the root page
        spanning_tree, order = breadth_first_search(self.graph, root=root_url)
        spanning_graph = digraph()
        spanning_graph.add_spanning_tree(spanning_tree)

        # Write the formatted graph structure using the dot library
        dot = write(spanning_graph)
        graph_visualization = gv.readstring(dot)

        # Output the the visualization
        gv.layout(graph_visualization, 'dot')
        gv.render(graph_visualization, 'png', file_name)


    def rerank(self, search_result, num_buckets=1):
        """
          Segments the search result into 'num_buckets' different buckets, and reorders each bucket by the pagerank algorithm

            @param  urls            The list of urls from the query
            @param  num_buckets     The number of buckets into which to segment the search result
            @retval		            The list of urls ranked using pagerank, with the first item being the highest ranked
        """

        # Pull out the list of web pages from the search result
        web_pages = search_result.pages

        # Partition the list of web pages into 'num_buckets' different buckets
        web_pages_buckets = split_list(web_pages, num_buckets)

        # Reorder each bucket using pageranking
        for web_page_bucket_index in range(0, len(web_pages_buckets)):
            web_pages_buckets[web_page_bucket_index] = self.rank(web_pages_buckets[web_page_bucket_index])

        # Join the buckets into one list        
        reranked_web_pages =  list(itertools.chain(*web_pages_buckets))

        # Update the pages variable in the search result to this new list
        search_result.pages = reranked_web_pages

        # Finally return a list of the urls, ordered using the pagerank algorithm
        return search_result


    def rank(self, web_pages):
        """
          Helper function that will rank the given web pages using the pagerank algorithm
        """

        # Create a dictionary of these pages indexed by their urls
        web_pages_dictionary = {}
        for web_page in web_pages:
            web_pages_dictionary[web_page.url] = web_page

        # Get the list of urls that we want to filter to
        urls = web_pages_dictionary.keys()

        # Now, run the pagerank algorithm on the graph structure as it is
        ranked_nodes = pagerank(self.graph) # Gets back a dictionary of nodes and their rank values

        # Create a list of tuples of urls and their pageranks, sorted by their pagerank values
        pageranked_urls = sorted(ranked_nodes.keys(), key=itemgetter(1))
        pageranked_urls.reverse()

        # Reverse the sorted list, so it's actually 'ranked'
        pageranked_urls.reverse()

        # Take out all pages that aren't in the list of urls given
        for pageranked_url in pageranked_urls:

            # Remove this tuple if it's not in the list of urls
            unicode_url = unicode(pageranked_url, 'utf-8')
            if unicode_url not in urls:
                pageranked_urls.remove(pageranked_url)


        # Create a new list of ranked urls, containing only the urls, but ranked using the pagerank algorithm
        ranked_web_pages = []
        for pageranked_url in pageranked_urls:
            if pageranked_url in web_pages_dictionary.keys():
                ranked_web_pages.append(web_pages_dictionary[pageranked_url])

        # Return the ranked list of web pages
        return ranked_web_pages


def split_list(data_list, bucket_size):
    """
      Return a list of successive buckets of a list
    """
    return list(chunks(data_list, bucket_size))


def chunks(data_list, bucket_size):
    """
      Yield successive buckets of a list
    """
    for i in xrange(0, len(data_list), bucket_size):
        yield data_list[i:i+bucket_size]
