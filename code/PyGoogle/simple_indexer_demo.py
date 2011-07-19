"""
  A handle to launch the indexer with the default crawl database connection data, and perform a sample query
"""
from Common.index import Index
from Common.crawl_data_context import CrawlDataContext
from time import sleep
from Indexer.indexer import Indexer

# Build the context object to connect to the crawled data
context = CrawlDataContext('pygoogle', 3306, 'root', 'root', None, True)

# Build an index object
index = Index('Indexer/index', True)

# Start the indexer
indexer = Indexer(context, index)
indexer.start()

sleep(5)

result = index.query(u'illinois')

print "Query took %f seconds" % (result.duration.microseconds / 1000000.0)
print "Search terms were: %s\n" % result.search_terms
print "Showing %d items:" % len(result.pages)
i = 1
for page in result.pages:
    print "Page %d:" % i
    print "Page Title: %s" % page.title
    print "Page Url: %s" % page.url
    print "Page Keywords: %s" % page.keywords
    print "Page Links: %s\n" % page.links
    i = i+1
