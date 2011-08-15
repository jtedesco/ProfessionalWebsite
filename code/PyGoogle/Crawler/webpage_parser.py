"""
  This file holds data for parsing data from raw HTML content
"""

import re
from Lib.beautiful_soup import BeautifulSoup

__author__ = 'Jon Tedesco'

def parse_data_from_content(content):
    """
      Parses the raw string data from the website. This function acts as a critical helper function to extract the
       title, keywords, and links using regular expressions.

       @param  content the raw content to parse
    """

    # Parse this HTML content using 'beautiful soup'
    soup = BeautifulSoup(content)

    # Pull out the title via BeautifulSoup
    try:
        title_tag = soup.html.head.title
        title = title_tag.string.encode('ascii', 'ignore').strip('\n')
    except:
        title = ''

    # Pull out the links via BeautifulSoup
    links = []
    try:
        for link_tag in soup.findAll('a'):
            link = link_tag['href']
            if link and len(link)>0:
                links.append(link.encode('ascii', 'ignore').strip())
    except:
        pass

    # Pull out the meta keywords using beautiful soup
    try:
        keywords_results = soup.findAll(attrs={"name":"keywords"})
        keywords_result = keywords_results[0]
        keywords_string = keywords_result['content']
        keywords_string = keywords_string.encode('ascii', 'ignore')
        keywords = keywords_string.split(',')
        for keyword in keywords:
            keywords.remove(keyword)
            keywords.append(keyword.strip())
    except:
        keywords = ''

    # Return the parsed data
    return title, keywords, links
