"""
  A handle to launch the crawler with the default database connection data
"""

from Common.crawl_data_context import CrawlDataContext
from Crawler.crawler import Crawler

# Build the context object and run the crawler!
context = CrawlDataContext('pygoogle', 3306, 'root', 'root', 'Crawler/crawllists/illinois_url_list', True)
crawler = Crawler(context)
crawler.start()

print "Launched crawler!"