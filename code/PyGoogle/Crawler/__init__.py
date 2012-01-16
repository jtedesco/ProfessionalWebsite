"""
  This package encompasses a relatively simple multi-threaded web crawler. This implementation makes use of:
  <ul>
     <li> A multithreaded implementation of a crawler, sharing a global queue for pages to be crawled
     <li> Robots checking
     <li> A persistent queue of pages to crawl, so that closing the program and reopening will resume the program rather than
             start over
     <li> A MySQL database for the parsed data from the crawler. This database will be used by the indexer for searching
  </ul>

  This implementation likewise attempts to compile use a JIT compiler, Psyco, as a performance boost, but will only do
   so successfully on python 2.6 or earlier.
"""
__author__ = 'Jon Tedesco'

