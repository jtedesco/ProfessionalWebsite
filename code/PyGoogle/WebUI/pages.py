"""
 This file holds a web page template, which will be the only object containing connections to the indexer and crawler, and
    will act as the root page model from which all pages are derived.
"""

from WebUI.Common import page_components
import cherrypy
import urllib2

__author__ = 'Jon Tedesco'

class HomePage():
    """
      This page acts as the base from which all pages in the PyGoogle application are derived. In this 'page' object
        will be stored all of the back end information, essentially acting as a singleton for access to the backend
    """

    def __init__(self, *args, **kwargs):
        """
          The constructor for the base 'page' object, which will simply get a handle on all of the backend and common data.
            Likewise, this will hold the history of queries as the application runs.

            @param  index       The index object which is built by the crawler and indexer
            @param  indexer     The indexer itself, which we can monitor
            @param  crawler     The crawler itself, which we can monitor
            @param  context     The connection data to the database
            @param  stdout_file The file to which standard out is redirected. We can read this file for updates on
        """

        # If this is constructed as the actual base page, create children and initialize the backend data
        if(len(kwargs) > 0):

            # Get a handle on all backend data
            self.backend_index = kwargs['index']
            self.indexer = kwargs['indexer']
            self.crawler = kwargs['crawler']
            self.context = kwargs['context']
            self.stdout_file_path = kwargs['stdout_file_path']

            # Build the 'about' and 'privacy' pages
            self.about = AboutPage()
            self.privacy = PrivacyPage()

            # Build the backend page
            self.backend = BackendPage()
            self.backend.backend_index = self.backend_index
            self.backend.indexer = self.indexer
            self.backend.crawler = self.crawler
            self.backend.context = self.context

            # Build the 'search' page, which will handle queries and render them as results
            self.search_page = SearchPage()

            # Build dummy pages too
            self.visualize = DummyPage()

            # Create an empty list of query-result pairs, which will act as the history for the search engine
            self.query_history = []


    @cherrypy.expose
    def index(self):
        """
          This function builds the home page of the application and returns it to the browser. Specifically, it will create
            a new homepage object, but only if one does not already exist

          @retval   The html code to generate the homepage of our application
        """

        # Build the components of this page
        header = page_components.header()
        main_search_form = page_components.large_search_form()
        main_logo = page_components.large_logo_without_link()
        footer = page_components.footer()

        # Return this page
        return header + main_search_form + main_logo + footer


    @cherrypy.expose
    def search(self, q=None, searchButton=None, luckyButton=None, reSearchButton=None):
        """
          This function directly recieves search requests from the page. This will either explicitly load the best match
            (I'm Feeling Lucky!), or it will render the search results
        """

        # Pull out the actual query from the page
        self.search_page.query = q
        unicode_query = unicode(q, 'utf-8')

        # Perform the query on the index
        search_result = self.backend_index.query(unicode_query)

        # Store the search result in this object
        self.search_page.search_result = search_result
        self.query_history.append(search_result)
        
        # If we hit "I'm Feeling lucky", redirect to that site
        if luckyButton is not None:
            if search_result is None:
                return cherrypy.HTTPRedirect("www.google.com", 301)
            else:
                raise cherrypy.HTTPRedirect((search_result.pages[0]).url, 301)

        # Otherwise, render the search result
        if searchButton is not None or reSearchButton is not None:
            return self.search_page.index()

    @cherrypy.expose
    def load_url(self, url):
        """
          This function acts as a utility method, loading a given url
        """

        # Build a request object to grab the content of the url
        request = urllib2.Request(url)
        request.add_header("User-Agent", "PyGoogle")

        # Open the URL and read the content
        opener = urllib2.build_opener()
        content = opener.open(request).read()

        # Return the content
        return content


class AboutPage(HomePage):
    """
      This page represents the 'about' section of the page
    """

    @cherrypy.expose
    def index(self):
        """
          This function returns the HTML code to render the 'about' page
        """

        # Build the individual components of the page
        header = page_components.header()
        logo = page_components.large_logo()
        about = page_components.about()
        footer = page_components.footer()

        # Put the page together
        return header + logo + about + footer


class PrivacyPage(HomePage):
    """
      Represents the page with privacy info
    """

    @cherrypy.expose
    def index(self):
        """
          Builds the privacy policy page and returns an html string to render it
        """

        # Build the individual components of the page
        header = page_components.header()
        logo = page_components.large_logo()
        about = page_components.privacy()
        footer = page_components.footer()

        # Put the page together
        return header + logo + about + footer


class SearchPage(HomePage):
    """
      This page handles search requests, rendering it using the 'search' interface
    """

    @cherrypy.expose
    def index(self):
        """
          This function returns the HTML code for rendering the list of search results
        """

        # Pull the data 
        search_result = self.search_result
        query = self.query

        # Build the components of the page
        header = page_components.header(page_components.search_result_styling(), query)
        search_form = page_components.small_search_form(query)
        search_results = page_components.search_results(search_result)
        search_results_nav = page_components.search_results_navigation()
        footer = page_components.footer()

        # Put the search results page together
        return (header + search_form + search_results + search_results_nav + footer)


class BackendPage(HomePage):
    """
      This page shows the status of the backend 
    """

    @cherrypy.expose
    def index(self):
        """
          This function returns the HTML code to render the 'about' page
        """

        # Build the individual components of the page
        header = page_components.header()
        logo = page_components.large_logo()
        about = page_components.backend(self.backend_index, self.indexer, self.crawler, self.context)
        footer = page_components.footer()

        # Put the page together
        return header + logo + about + footer




class DummyPage(HomePage):
    """
      This class implements visualization for the data crawled, and returns an image to the screen printed with graphviz.
    """

    @cherrypy.expose
    def index(self):
        """
         Returns a 'not' implemented page with Chuck Norris
        """

        return  """
                <html>
                <head>
                    <title>Yessss</title>
                </head>

                <center>
                <br><br>
                <img width=325 height=400 src="http://www.codesqueeze.com/wp-content/2009/06/geek-chuck-norris.jpg">
                <div style="width: 700px;">
                    <br><br>
                    <ol>
                        <li> When Chuck Norris throws exceptions, it's across the room.
                        <li> All arrays Chuck Norris declares are of infinite size, because Chuck Norris knows no bounds.
                        <li> Chuck Norris doesn't have disk latency because the hard drive knows to hurry the hell up.
                        <li> Chuck Norris writes code that optimizes itself.
                        <li> Chuck Norris can't test for equality because he has no equal.
                        <li> Chuck Norris doesn't need garbage collection because he doesn't call .Dispose(), he calls .DropKick().
                        <li> Chuck Norris's first program was kill -9.
                        <li> Chuck Norris burst the dot com bubble.
                        <li> All browsers support the hex definitions #chuck and #norris for the colors black and blue.
                        <li> MySpace actually isn't your space, it's Chuck's (he just lets you use it).
                        <li> Chuck Norris can write infinite recursion functions... and have them return.
                        <li> Chuck Norris can solve the Towers of Hanoi in one move.
                        <li> The only pattern Chuck Norris knows is God Object.
                        <li> Chuck Norris finished World of Warcraft.
                        <li> Project managers never ask Chuck Norris for estimations... ever.
                        <li> Chuck Norris doesn't use web standards as the web will conform to him.
                        <li> "It works on my machine" always holds true for Chuck Norris.
                        <li> Whiteboards are white because Chuck Norris scared them that way.
                        <li> Chuck Norris doesn't do Burn Down charts, he does Smack Down charts.
                        <li> Chuck Norris can delete the Recycling Bin.
                        <li> Chuck Norris's beard can type 140 wpm.
                        <li> Chuck Norris can unit test entire applications with a single assert.
                        <li> Chuck Norris doesn't bug hunt as that signifies a probability of failure, he goes bug killing.
                        <li> Chuck Norris's keyboard doesn't have a Ctrl key because nothing controls Chuck Norris.
                        <li> When Chuck Norris is web surfing websites get the message "Warning: Internet Explorer has deemed this user to be malicious or dangerous. Proceed?".
                    </ul>
                    <p>[<a href="../">Return</a>]</p>
                </div>
                </center>

                </html>
                """
