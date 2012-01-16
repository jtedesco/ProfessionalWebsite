"""
 This portion of the project encompasses a multi-threaded indexer that will index the the data collected by the
   crawler and allow the web interface to query crawl data in a meaningful way. This indexer contains several components:

 <ul>
   <li> The launcher, which grabs data from the indexer and serves up data to be crawled to the indexer
   <li> The indexer threads, which will concurrently process the data from the crawler and build an index that can be queried
   <li> The graph builder, which will create a graph representation of the data for performing the pagerank algorithm
   <li> The text indexer, which will use lucene to index the raw text of the data
   <li> The index itself, which holds both a graph and raw representation of the crawl data for crawling, and which will
           sync itself with disk to avoid data loss. This index will be directly queried from the API.
   <li> An API through which to query data, which will query the index and rank the results using the different index data.
 </ul>

  This implementation likewise attempts to compile use a JIT compiler, Psyco, as a performance boost, but will only do
   so successfully on python 2.6 or earlier.
"""

__author__ = 'Jon Tedesco'

