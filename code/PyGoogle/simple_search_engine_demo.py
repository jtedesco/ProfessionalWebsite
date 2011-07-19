"""
  A handle to demo launching both the indexer and the crawler, and querying the index for data
"""
from Common.crawl_data_context import CrawlDataContext
from Crawler.crawler import Crawler
from Indexer.indexer import Indexer
from Common.index import Index
from time import sleep

__author__ = 'Jon Tedesco'

# Build an index object and context object
index = Index('Indexer/index', True)
context = CrawlDataContext('pygoogle', 3306, 'root', 'root', 'Crawler/crawllists/illinois_url_list', False)

# Launch the crawler
crawler = Crawler(context)
crawler.start()

print "Launched web crawler!"

# Start the indexer
indexer = Indexer(context, index)
indexer.start()

print "Launched the indexer!"

sleep(1)

# Submit a query
result = index.query(u'research')

print "Query took %2.3f seconds" % (result.duration.microseconds / 1000000.0)
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
