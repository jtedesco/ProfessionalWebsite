"""
  This file builds common components of the web interface, and its functions can be called as utility methods to return
    the code for components such as footers, headers, forms, etc.
"""

import locale
import re
from styles import *
import multiprocessing
import Crawler.crawler_constants
import Indexer.indexer_constants

__author__ = 'Jon Tedesco'


def header(styling=None, query=None):
    """
      Builds the page header, including the opening html tag, and the entired <head> tag, and the opening <body> tag
    """

    # Build the title string if this includes a query
    query_string = ""
    if query is not None:
        query_string = " - '%s'" % query

    # Build the styling string
    if styling is None:
        styling = common_styling()

    return """
            <!doctype html>
            <html>
            <head>
                <meta http-equiv="content-type" content="text/html; charset=UTF-8">
                <title>PyGoogle%s</title>
                %s
            </head>
            <body>
           """ % (query_string, styling)


def small_search_form(last_query=""):
    """
      Builds the small search
    """

    return """
            <div class=jsrp id=searchform style="top:41px">
                <form action="/search/" id=tsf method=POST name=f style="display:block;margin:0 auto;background:none">
                    <div class=tsf-p style=position:relative>
                        <div id=logocont style="left:0;margin-top:-8px; margin-left:-30px; padding:0 12px;position:absolute">
                            <a href="../">
                                <img src="http://www.generationschicago.com/temp/pygoogle_logo_small.jpg" width=200 height=57 alt="PyGoogle" title="PyGoogle" style="margin-top: 5px;" border=0/>
                            </a>
                        </div>
                        <div style="padding-bottom:2px;padding-right:8px">
                            <table border=0 cellpadding=0 cellspacing=0 width=100%>
                                <tr>
                                    <td width=100%>
                                        <table border=0 cellpadding=0 cellspacing=0 id=sftab style="border-bottom:1px solid #e7e7e7;padding:8px 0 0;position:relative" width=100%>
                                            <tr>
                                                <td width=100%>
                                                    <div style=position:relative>
                                                        <table border=0 cellpadding=0 cellspacing=0 width=100%>
                                                            <tr>
                                                                <td id=lst-xbtn class="lst-td lst-td-xbtn" style="border-top:1px solid #ccc" width=100%>
                                                                    <div style="position:relative;zoom:1">
                                                                        <div style="position:relative;background:transparent">
                                                                            <input class=lst type=text name="q" title="Search" value=\"""" + last_query + """\">
                                                                        </div>
                                                                    </div>
                                                        </table>
                                                    </div>
                                                <td>
                                                    <div class=nojsb>
                                                        <div class=ds>
                                                            <div class=lsbb><input type=submit name="reSearchButton" class=lsb value="Search">
                                                            </div>
                                                        </div>
                                                    </div>
                                        </table>
                                    <td>
                            </table>
                        </div>
                    </div>
                </form>
            </div>
            """


def large_search_form():
    """
      Builds the default, 'large' search form to search PyGoogle
    """

    return """
            <center>
            <div id=search_form_div>
                <form action="search" method=POST name=search_form id=search_form>
                    <div style="padding-bottom:2px; padding-right:8px">
                        <table class="invisible_table">
                            <tr>
                                <td class="full_cell">
                                    <table border=0 cellpadding=0 cellspacing=0 id=sftab class="search_box">
                                        <tr>
                                            <td class="full_cell">
                                                <table border=0 cellpadding=0 cellspacing=0 width=100%>
                                                    <tr>
                                                        <td id=lst-xbtn class="lst-td lst-td-xbtn search_buttons">
                                                            <div style="position:relative;zoom:1">
                                                                <input id="query_box" class=lst type=text name="q"  value="" title="Search"/>
                                                            </div>
                                                    </tr>
                                                </table>
                                            <td>
                                            <div class=jsb style="background:#999;height:32px;width:1px"></div>
                                    </table>
                                <td>
                                    <div class=jsb style="font-size:11px;position:absolute;padding-top:5px;padding-left:12px">
                                        <a href="/backend" class=fl style="color:#4373db">Monitor <br> Backend</a><br>
                                    </div>
                                    <div class=nojsv id=sfopt style="position:relative;height:30px">
                                        <div class=lsd>
                                            <br></div>
                                    </div>
                        </table>
                    </div>
                    <div class=jsb style=padding-top:2px>
                            <span class=ds>
                                <span class=lsbb>
                                    <input name=searchButton type=submit value="PyGoogle Search" class=lsb>
                                </span>
                            </span>
                            <span class=ds>
                                <span class=lsbb>
                                    <input name=luckyButton type=submit value="I&#39;m Feeling Lucky"class=lsb>
                                </span>
                            </span>
                    </div>
                </form>
            </div>
            </center>
           """


def large_logo():
    """
      Builds the large PyGoogle logo and formats it
    """

    return """
            <center>
            <span id=main>
                <div id=ghead>
                    <div id=lga style="padding-top:22px">
                        <a href="../">
                            <img alt="PyGoogle" height=170 width=597 src="http://www.generationschicago.com/temp/pygoogle_logo.jpg" id=hplogo style="padding-top:26px"><br><br>
                        </a>
                    </div>
                </div>
            </span>
            </center>
           """


def large_logo_without_link():
    """
      Builds the large PyGoogle logo and formats it
    """

    return """
            <center>
            <span id=main>
                <div id=ghead>
                    <div id=lga style="padding-top:22px">
                        <img alt="PyGoogle" height=170 width=597 src="http://www.generationschicago.com/temp/pygoogle_logo.jpg" id=hplogo style="padding-top:26px"><br><br>
                    </div>
                    <div style=height:126px>
                    </div>
                    <div style="font-size:83%;min-height:3.5em">
                        <br><br>
                    </div>
                </div>
            </span>
            </center>
           """


def footer():
    """
      Builds the default footer, including the copyright, exterior links, closing body and html tags
    """

    return """
                <center>
                <div style="font-size:10pt">
                    <div id=fll>
                        <a href="/about/">About PyGoogle</a>
                        <a href="/privacy/">Privacy Policy</a>
                    </div>
                </div>
                <p style="color:#767676;font-size:8pt">&copy; 2010 - Jon Tedesco</p>
                </center>
            </body>
            </html>
           """

def backend(index, indexer, crawler, context):
    """
      This builds the HTML code to display statistics about the backend
    """

    # Build the opening to the page
    stats_beginning = """ <center><div style="width:600px; text-align: justify;"> <div>"""

    # Build the general statistics
    general_statistics = """
                <h2>General Statistics</h2>
                <table>
                    <tr>
                        <td width=500px> Version </td> <td><b> %1.1f </b></td>
                    </tr>
                    <tr>
                        <td width=500px> JIT Compiler </td> <td><b> %s </b></td>
                    </tr>
                    <tr>
                        <td width=500px> CPUs Avaiable </td> <td><b> %d </b></td>
                    </tr>
                    <tr>
                        <td width=500px> Database Name </td> <td><b> %s </b></td>
                    </tr>
                    <tr>
                        <td width=500px> Database Port </td> <td><b> %d </b></td>
                    </tr>
                    <tr>
                        <td width=500px> Database Username </td> <td><b> %s </b></td>
                    </tr>
                    <tr>
                        <td width=500px> Database Password </td> <td><b> %s </b></td>
                    </tr>
                    <tr>
                        <td width=500px> Starting URLs File </td> <td><b> %s </b></td>
                    </tr>
                    <tr>
                        <td width=500px> Verbose Output</td> <td><b> %s </b></td>
                    </tr>
                </table></br></br></br>
            """ % (1.1, "PyPy", multiprocessing.cpu_count(), context.database_name, context.database_port, context.user_name,
                   context.password, context.urls_file_name, context.verbose)

    # Build the crawler statistics
    crawler_statistics = """
                <h2>Crawler Statistics</h2>
                <table>
                    <tr>
                        <td width=500px> Number of Crawler Threads Running</td> <td><b> %d </b></td>
                    </tr>
                    <tr>
                        <td width=500px> Queue File </td> <td><b> %s </b></td>
                    </tr>
                    <tr>
                        <td width=500px> Politeness Checking Data </td> <td><b> %s </b></td>
                    </tr>
                </table></br></br></br>
            """ % (len(crawler.threads), Crawler.crawler_constants.DEFAULT_QUEUE_FILE, "Crawler/robots.txt")

    # Build the crawler statistics
    indexer_statistics = """
                <h2>Indexer Statistics</h2>
                <table>
                    <tr>
                        <td width=500px> Number of Indexer Threads Running</td> <td><b> %d </b></td>
                    </tr>
                    <tr>
                        <td width=500px> Indexed Items </td> <td><b> %d </b></td>
                    </tr>
                    <tr>
                        <td width=500px> Queue File </td> <td><b> %s </b></td>
                    </tr>
                    <tr>
                        <td width=500px> Text Index Directory</td> <td><b> %s </b></td>
                    </tr>
                    <tr>
                        <td width=500px> Graph Index File </td> <td><b> %s </b></td>
                    </tr>
                </table></br></br></br>
            """ % (len(indexer.threads), indexer.count, Indexer.indexer_constants.DEFAULT_QUEUE_FILE, "Indexer/index",  Indexer.indexer_constants.DEFAULT_GRAPH_INDEX_FILE)

    # End this section
    stats_ending = """</div></div></center><br><br><br><br><br>"""

    return stats_beginning + general_statistics + crawler_statistics + indexer_statistics + stats_ending








def about():
    """
     Builds the 'about' text section
    """

    return """
            <center>
            <div style="width:600px; text-align: justify;">
                <p>&nbsp; &nbsp; &nbsp; This project aims to create a (relatively) simple search engine in Python. Essentially, the project will include a
                multi-threaded web crawler, which will store data both in a text indexing system and graph structure, and provide
                a simple web interface to query the data. The focus on this project will not be scale or full-featured software,
                but rather on fitting each component together to form an interesting, purely python-based search engine.
                <p>&nbsp; &nbsp; &nbsp; Specifically, this project will use external python wrappers and libraries for text indexing, pagerank
                    algorithm and graph representation, and web crawling. Rather than the implementation of each of these
                    pieces, the focus for this project will be on fitting these pre-existing pieces together, in an interesting
                    way, and present the data via a web interface.
                    Although this project will be compiled and run as one unit, there will be several conceptual parts of the
                    project. The project will consist of:
                    <ol style="margin-left:50px;">
                        <li>
                            A multi-threaded web crawler
                        </li>
                        <li>
                            A multi-threaded indexer
                        </li>
                        <li>
                            A python-driven web interface for querying the data
                        </li>
                    </ol>
                    <p>&nbsp; &nbsp; &nbsp; Primarily, the GUI for this project will not be a high priority. The majority of time and effort will be
                    spent on the data structures and algorithms behind the searching and indexing, and the GUI will simply
                    be a finishing touch to the project.
            </div>
            <br>
            <br>
            <br>
            <br>
            """


def privacy():
    """
      Builds the entire privacy page
    """
    
    return """
            <center>
            <div style="width:600px; text-align: justify;">
                <div id="promo"></div>
                <div id="intro" class="alt">

                    <p>&nbsp; &nbsp; &nbsp; &nbsp; At PyGoogle, we are keenly aware of the trust you place in us and our responsibility to protect your privacy.
                        As part of this responsibility, we let you know what information we collect when you use our products and
                        services, why we collect it and how we use it to improve your experience.

                    <p>&nbsp; &nbsp; &nbsp; &nbsp; We have five <b>privacy principles</b> that describe how we approach
                        privacy and user information across all of our products:
                    <ol style="margin-left: 50px;">
                        <li>Use information to provide our users with valuable products and services.
                        <li>Develop products that reflect strong privacy standards and practices.
                        <li>Make the collection of personal information transparent.
                        <li>Give users meaningful choices to protect their privacy.
                        <li>Be a responsible steward of the information we hold.
                    </ol>
                    <p>&nbsp; &nbsp; &nbsp; &nbsp; This Privacy Center was created to provide you with easy-to-understand information about our products and
                        policies to help you make more informed choices about which products you use, how to use them and what
                        information you provide to us.

                    <p>&nbsp; &nbsp; &nbsp; &nbsp; For information and advice on how to help your family stay safe online, see the PyGoogle Family Safety Center.
                </div>
                <div id="policies">
                    <h3>Privacy policies</h3>
                    <p>&nbsp; &nbsp; &nbsp; &nbsp; <a href=""><strong>PyGoogle's Privacy Policy</strong></a> describes how we treat personal
                        information when you use PyGoogle's products and services.
                    <p>&nbsp; &nbsp; &nbsp; &nbsp; The following statements explain specific privacy practices with respect to certain products and services:
                </div>
            </div>
            </center>
            """


def search_results(search_result):
    """
      Builds the HTML to render the list of search results, including information about the number or results and the
        time to complete the search
    """

    # Pull out the list of terms (words) in the query
    if search_result is not None:
        terms = "Search Terms: "
        for search_term in search_result.search_terms:
            terms = terms + "<b>" + search_term.encode('ascii', 'ignore') + "</b>, "
        terms = terms.strip(", ")
    else:
        terms = "No query performed!"

    # Pull out the number of seconds taken
    if search_result is not None:
        seconds = (search_result.duration.microseconds / 1000000.0)
    else:
        seconds = 0

    # Create a nicely formatted string showing the number of results
    if search_result is not None:
        number_of_results = locale.format("%d", len(search_result.pages), grouping=True)
    else:
        number_of_results = "0"

    # Create the string of spelling suggestions

    if search_result is not None:

        # Remove duplicate suggestions
        spelling_suggestions_list = []
        for spelling_suggestion_list2 in search_result.suggestions:
            for spelling_suggestion in spelling_suggestion_list2:
                if spelling_suggestion not in spelling_suggestions_list:
                    spelling_suggestions_list.append(spelling_suggestion)

        # Build the suggestions string
        spelling_suggestions = "Did you mean: "
        for spelling_suggestion in spelling_suggestions_list:
            spelling_suggestions = spelling_suggestions + "<b> %s </b>, " % (spelling_suggestion.encode('ascii', 'ignore'))
        spelling_suggestions = spelling_suggestions.strip(", ") + "?"

    else:
        spelling_suggestions_string = ""


    # Create the HTML for the beginning of the search result list
    results_header = """
        <div id=main>
            <div id=cnt style="margin-top: 75px;" >
                <div id=subform_ctrl>
                    <div style="float:right">
                            %s
                    </div>
                    <div style="float:left">
                        <div id=resultStats>About %s results
                            <nobr> (%1.2f seconds)&nbsp;</nobr>
                        </div>
                    </div>
                    <center>
                    <div>
                        %s
                    </div>
                    </center>
                </div>
                <div id=nr_container style="position:relative;zoom:1;">
                    <div id=center_col>
                        <div id=res>
                            <div>
                                <ol>
        """ % (terms, number_of_results, seconds, spelling_suggestions)

    # Create a template for outputting the search results
    search_result_template_string = """
        <li class="g w0">
            <div>
                <span>
                    <h3 class="r">
                        <a href="%s" class=l>%s</a>
                    </h3>
                </span>
                <div style="width: 800px;">
                    %s
                    <br>
                    <span class=f>
                        <cite>
                            %s
                        </cite>
                    </span>
                </div>
            </div>
        """


    # Iterate through each search result, and print out the result item
    results_list = ""
    if search_result is not None:
        for page in search_result.pages:

            # Pull the data out of the current result page
            url = page.url
            title = page.title
            highlighted_excerpts = page.excerpts


            # Fill the template string with the result data
            results_list = results_list + search_result_template_string % (url, title, highlighted_excerpts, url)
    else:
        results_list = "<center><b>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;No Results</b></center>"


    # The HTML code that will end the list of results
    results_footer = """
                                    </ol>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
           """

    # Build the full search result list and return it
    final_search_result_list = results_header + results_list + results_footer 
    return final_search_result_list


def search_results_navigation():
    """
      Builds the navigation for the bottom of the search results page
    """

    return """
            <center>
            <div style="margin-top: 20px; margin-bottom: 20px">
                <div>
                    <table id=nav style="border-collapse:collapse;text-align:left;direction:ltr;margin:auto">
                        <tr valign=top>
                            <td class=b>
                                <span class="csb" style="background-position:-24px 0;width:60px"></span>
                            <td class=cur>
                                <span class="csb" style="background-position:-84px 0;width:20px"></span>
                                1
                            <td>
                                    <span class="csb ch" style="background-position:-104px 0;width:20px"></span>
                                    2
                            <td>
                                    <span class="csb ch" style="background-position:-104px 0;width:20px"></span>
                                    3
                            <td>
                                    <span class="csb ch" style="background-position:-104px 0;width:20px"></span>
                                    4
                            <td>
                                    <span class="csb ch" style="background-position:-104px 0;width:20px"></span>
                                    5
                            <td>
                                    <span class="csb ch" style="background-position:-104px 0;width:20px"></span>
                                    6
                            <td class=b>
                                    <span class="csb ch" style="background-position:-125px 0;width:71px"></span>
                                    <span style="display:block;margin-left:53px;text-decoration:underline"></span>
                    </table>
                </div>
             </div>
            </center>
            """
