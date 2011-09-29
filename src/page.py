import os, re, cherrypy, sys
from datetime import datetime
from cherrypy.lib.static import serve_file
from whoosh.fields import Schema, TEXT
from whoosh.highlight import ContextFragmenter, HtmlFormatter, highlight
from whoosh.index import create_in, exists_in, open_dir
from whoosh.qparser.default import QueryParser
from whoosh.spelling import SpellChecker
from common.common import get_server_root, get_root_directory, setup_error_handling, get_default_keywords
from util.file_reader import read_file

__author__ = 'Jon'

thePage = None
class Page(object):
    """
        Abstract class for a page on the website. This exposes the 'index' method, which returns the html
        content of this page.
    """


    def __init__(self):
        """
            Construct the shell of a page
        """
        thePage = self

        # Build the HTMl content of this page
        self.build_content()

        # We haven't initialized the index for this page yet
        self.initialized = False


    @cherrypy.expose
    def index(self):
        """
            Returns the content of this page, or in this case, redirects to the 'home' page
        """
        raise cherrypy.HTTPRedirect("%shome/" % get_server_root(), 301)


    def build_content(self):
        """
                Helper function to allow pages to dynamically update their HTML content
        """

        # Get each individual component of the page
        meta_header = self.meta_header(self.title())
        page_header = self.header()
        content = self.main_content()
        menu = self.menu()
        sidebar = self.sidebar()
        footer = self.footer()

        # Build this page's content on creation
        self.content = read_file("content/template.html") % (meta_header, page_header, content, menu, sidebar, footer)


    @cherrypy.expose
    def get404(self):
        raise cherrypy.HTTPError(404)

    @cherrypy.expose
    def get500(self):
        raise cherrypy.HTTPError(500)

    @cherrypy.expose
    def search(self, query = None):
        """
            Searches the site's contents using the given query.

                @param  query   The query on which to search
        """

        # Build the site's search index
        if not self.initialized:
            self.create_index()

        # Get the template for the search results, and the search results themselves
        template_content = read_file("content/pages/search.html")

        # Default the search results if there isn't a query
        search_results = "<p>Please enter a query to search</p>"
        title = "Jon Tedesco &#183; Search"

        if query is not None and len(query) > 0:

            # Convert the query to unicode
            try:
                query = unicode(query, 'utf-8')
            except Exception:
                pass

            # Run the query
            start_time = datetime.now()
            results, search_terms, suggestions, result_count = self.run_query(query)
            end_time = datetime.now()
            stats = "Searched for <i>%s</i>, found %d hits in %1.3f seconds<br>" % (query, result_count, (end_time-start_time).microseconds/1000000.0)
            more_stats=""
            try:
                more_stats = "Did you mean <a href='../search/?query=%s'><i>%s</i></a>?" % (suggestions[0][0], suggestions[0][0])
            except:
                pass

            # Update the page components
            title += " &#183; '%s'" % query
            search_results = template_content % (more_stats + stats, results)

        # Build the components of the page
        meta_header = self.meta_header("Jon Tedesco &#183; Search '%s'" % query)
        page_header = self.header(query)
        content = search_results
        menu = self.menu()
        sidebar = self.sidebar()
        footer = self.footer()

        return read_file("content/template.html") % (meta_header, page_header, content, menu, sidebar, footer)


    @cherrypy.expose
    def download(self, file_path):
        """
            Helper to allow any page to serve up a file

                @param  file_path   The file to download, relative to the root of the site_
        """
        return serve_file(get_root_directory() + "/" + file_path, "application/x-download", "attachment")

    
    def main_content(self):
        """
            Returns the primary, bulk content of this page, to be implemented by each subclass.
            This root page simply redirects to the home page.
        """
        pass
        

    def title(self):
        """
            Abstract method implemented by all pages
        """
        pass


    def meta_header(self, title):
        """
            Returns the meta header, which is common to all pages

                @param  title   The title of this page
                @return The meta data to go in the HEAD tag of the page
        """

        # There are a lot of server root entries because there are many syntax highlighting brushes, and each
        #   brush is imported separately
        return read_file("content/meta_header.html") % (title, " ".join(self.keywords()), self.description(), get_server_root(), get_server_root(),
                                                        get_server_root(), get_server_root(), get_server_root(), get_server_root(), get_server_root(),
                                                        get_server_root(), get_server_root(), get_server_root(), get_server_root(),
                                                        get_server_root(), get_server_root(), get_server_root(), get_server_root())


    def keywords(self):
        """
            Gets a list of keywords for the site, which should be common to all pages

                @return The list of keywords for this page
        """
        return get_default_keywords()

 
    def description(self):
        """
          Gets the description of the site to be put in the meta data

            @return The description of the page
        """
        return "Homepage of Jon Tedesco, a dedicated student and avid software developer at University of Illinois at Urbana-Champaign"


    def header(self, query = None):
        """
            Gets the standard header of the page (logo, search box)
        """
        if query is not None:
            # Fill the query into the text box if a query was submitted
            return read_file("content/header.html") % query
        else:
            return read_file("content/header.html") % ""


    def footer(self):
        """
            Gets the footer for the site, automatically updating the copyright to the current year
        """
        return read_file("content/footer.html") % str(datetime.today().year)

    
    def menu(self):
        """
            Returns the code for the side menu (should be the same on every page)
        """
        return read_file("content/menu.html") % (get_server_root(), get_server_root(), get_server_root(), get_server_root(), get_server_root())


    def sidebar(self):
        """
            Get the sidebar code (traditionally the contact information)
        """
        return read_file("content/sidebar.html") % "home"


    def create_index(self):

        # Create the schema for this index, which denotes the types of each field, and next try to build the index itself
        #   using this schema. Note that this schema treats the URL as the unique identifier for documents in the index,
        #   and scores documents based on the title and content alone
        self.index_schema = Schema(content=TEXT(stored=True), title=TEXT(stored=True))
        index_dir = ".index"

        try:
            # Try to create the index directory
            os.mkdir(index_dir)

            # Build a new index in this directory
            self.index = create_in(index_dir, self.index_schema)

            # Get a writer for the index
            index_writer = self.index.writer()

            # Walk the pages folder for content
            for root_path, sub_directories, files in os.walk("content/pages"):
                for file in files:
                    self.insert_document(root_path + "/" + file, index_writer, file)

            # Commit all the changes, so that every change is flushed to disk, and we can safely query the index
            index_writer.commit()

        except OSError, error:
            
            # If the directory already exists, try to restore the index from the directory
            if exists_in(index_dir):
                self.index = open_dir(index_dir)
            else:
                raise "Error creating or opening index: '" + error.message + "'"

        # We've now been initialized
        self.initialized = True


    def insert_document(self, path, index_writer, title):
        """
            Insert a given document into the index.

                @param  path            The path to the file to insert into the index (HTML format)
                @param  index_writer    A writer to access the text index
                @param  title           The title of this document
        """

        # Grab the content of the file
        content = ""
        for line in open(path, 'r'):
            content += line

        # Parse out the HTMl content of the file
        parsed_content = content.replace("<br/>", "\n")
        closing_tag_re = re.compile("</.*?>")
        tag_re = re.compile("<.*?>")
        white_space_re = re.compile("\s+")
        parsed_content = closing_tag_re.sub("\n", parsed_content)
        parsed_content = tag_re.sub(" ", parsed_content)
        parsed_content = white_space_re.sub(" ", parsed_content)
        parsed_content = unicode(parsed_content, 'utf-8')

        # Parse out the title
        title = unicode(title.replace(".html", "").title(), 'utf-8')

        # Put this content into index
        index_writer.add_document(content=parsed_content, title=title)


    def run_query(self, query):
        """
          Queries the index for data with the given text query

            @param  query   The text query to perform on the indexed data
            @return			A list of HTMl string snippets to return
        """

        # Create a searcher object for this index
        searcher = self.index.searcher()

        # Create a query parser, providing it with the schema of this index, and the default field to search, 'content'
        query_parser = QueryParser('content', schema=self.index_schema)

        # Build a query object from the query string
        query_object = query_parser.parse(query)

        # Build a spell checker in this index and add the "content" field to the spell checker
        self.spell_checker = SpellChecker(self.index.storage)
        self.spell_checker.add_field(self.index, 'content')
        self.spell_checker.add_field(self.index, 'title')

        # Extract the 'terms' that were found in the query string. This data can be used for highlighting the results
        search_terms = [text for fieldname, text in query_object.all_terms()]

        # Remove terms that are too short
        for search_term in search_terms:
            if len(search_term) <= 3:
                search_terms.remove(search_term)

        # Perform the query itself
        search_results = searcher.search(query_object)

        # Get an analyzer for analyzing the content of each page for highlighting
        analyzer = self.index_schema['content'].format.analyzer

        # Build the fragmenter object, which will automatically split up excerpts. This fragmenter will split up excerpts
        #   by 'context' in the content
        fragmenter = ContextFragmenter(frozenset(search_terms))

        # Build the formatter, which will dictate how to highlight the excerpts. In this case, we want to use HTML to
        #   highlight the results
        formatter = HtmlFormatter()

        # Create a dictionary of search result snippets, indexed by page, and a count of the number of results
        results = {}
        result_count = 0

        # Iterate through the search results
        for search_result in search_results:

            # Grab the fields from the document
            title = search_result['title']
            content = search_result['content']

            # Build a list of HTML-highlighted excerpts
            excerpt = highlight(content, search_terms, analyzer, fragmenter, formatter)

            # Add this new snippet to <code>results</code> dictionary
            if title not in results.keys():
                results[title] = []
            results[title].append(excerpt)
            result_count += 1

        # Build a list of 'suggest' words using the spell checker
        suggestions = []
        for term in search_terms:
            suggestions.append(self.spell_checker.suggest(term))

        # Format the results into a nicer format
        formatted_results = self.format_results(results)

        # Return the list of web pages along with the terms used in the search
        return formatted_results, search_terms, suggestions, result_count


    def format_results(self, results):
        """
            Formats the results from the Whoosh! query into something nicely formatted for a web page
        """

        # Filter out whitespace from results
        white_space_re = re.compile("\s+")
        clean_results = {}

        for result in results.keys():
            clean_results[result] = []
            for entry in results[result]:
                parsed_content = white_space_re.sub(" ", entry)
                if parsed_content not in clean_results[result]:
                    clean_results[result].append(parsed_content)

        formatted_results = ""

        # Loop through each key in the results (a page), and group it that way
        for title in clean_results.keys():
            if len(clean_results[title]) > 0:

                # Format the title of this section
                title_section = ("<h2><a href='../%s'>" % title.lower()) + title.encode('ascii') + "</a></h2><p></p>"
                formatted_results += title_section

                for result in clean_results[title]:
                    formatted_results += "..." + result.encode('ascii') + "...<br>"
                formatted_results += "<br><br>"

        return formatted_results


    @cherrypy.expose
    def view_source(self, path, language):
        """
            View the source code of the given file, in the standard <code>view_source</code> template.

                @param  path    The path of the file to display (relative to the root of the site)
        """
        
        # Read the source code file
        source_file = open(get_root_directory() + "/" + path, 'r')
        source_code = source_file.read()
        source_file.close()

        # Check if this is a binary file
        binary_file_extensions = ['jpg', 'png', 'bmp', 'jpeg']
        if path[path.find('.')+1:] not in binary_file_extensions:
            
            # Escape '<' characters
            source_code = source_code.replace('<', '&lt')
            source_code = source_code.replace('>', '&gt')

            # Form the page content
            content = read_file("content/view_source.html") % (path, language, source_code.decode('utf-8', 'ignore'))

        else:

            # Form the page content
            image_source = "<img width=600 src='%s'></img>" % (get_server_root() + path)
            content = read_file("content/view_source.html").replace("<pre class=\"brush: %s\">%s</pre>", image_source)
            content = content % path.encode('ascii')
            
        # Build the components of the page
        meta_header = self.meta_header("View Source &#183; %s (%s)" % (path, language.title()))
        page_header = self.header()
        menu = self.menu()
        sidebar = read_file("content/short_sidebar.html")
        footer = self.footer()

        # Put the page together and return it
        return read_file("content/template.html") % (meta_header, page_header, content, menu, sidebar, footer)


    @cherrypy.expose
    def navigate_source(self, path, project):
        """
            View the contents of a directory, and provide a simple interface with which to navigate the directory
            structure, and view the source of files in the heirarchy.

                @param  path    The path, relative to the root of the site, to display
                @param  project The name of the source project we're viewing
        """

        # Matches file extensions with programming languages
        languages = {
            "py"    : "python",
            "rb"    : "ruby",
            "js"    : "javascript",
            "html"  : "html",
            "htm"   : "html",
            "cpp"   : "cpp",
            "c"     : "c",
            "cc"    : "cpp",
            "php"   : "php",
            "java"  : "java",
            "xml"   : "xml"
        }

        # List the files in the directory, and import them into a list of tuples, as '(filename, type)'
        root = get_root_directory()
        file_names = os.listdir(root + "/" + path)
        files = []
        directories = []
        for file in file_names:

            # Add directories and files to separate lists
            period_index = file.find('.')
            if os.path.isfile(root + "/" + path + "/" + file):
                if period_index > 0:
                    extension = file[file.find('.')+1:]
                    if extension in languages.keys():
                        files.append((path + "/" + file, languages[extension], file))
                    else:
                        files.append((path + "/" + file, "plain", file))
                else:
                    files.append((path + "/" + file, "plain", file))

            elif period_index == -1 and os.path.isdir(root + "/" + path):
                directories.append((path + "/" + file, file))

        # Read the 'exclude' file
        try:
            if os.path.exists(root + '/' + path + '/' + '.exclude'):
                exclude_file = open(root + '/' + path + '/' + '.exclude', 'r')
                for exclude_file_name in exclude_file:
                    exclude_file_name = exclude_file_name.strip()
                    exclude_path = path + '/' + exclude_file_name
                    if os.path.exists(root + '/' + exclude_path):
                        if os.path.isdir(root + '/' + exclude_path):
                            directories.remove((exclude_path, exclude_file_name))
                        else:
                            files.remove((exclude_path, "plain", exclude_file_name))
                    else:
                        print "Error parsing exclude file, could not open path '%s'" % exclude_path
        except Exception:
            print "Error parsing 'exclude' file: '%s'" % str(sys.exc_info()[1])

        # Form the list of links for the file browser
        directory_links = []
        file_links = []
        for directory in directories:
            directory_links.append((directory[1], '../navigate_source/?path=%s&project=%s' % (directory[0], project.replace(' ', '%20'))))
        for file_data in files:
            file_links.append((file_data[2], '../view_source/?path=%s&language=%s' % (file_data[0], file_data[1])))

        # Form the main content itself
        content = "<div class='source_navigation'>"
        if len(directory_links) > 0:
            content += "Directories:<br/><br/>"
        content += "<div style='margin-left: 20px;'>"
        for link in directory_links:
            content += "<a href=%s>%s</a><br/><br/>" % (link[1], link[0])
        if len(file_links) > 0:
            content += "</div>Files:<br/><br/><div style='margin-left: 20px;'>"
            for link in file_links:
                content += "<a href=%s>%s</a><br/><br/>" % (link[1], link[0])
        content += "</div></div>"

        # Form the page content
        content = read_file("content/navigate_source.html") % (project, content)

        # Build the components of the page
        meta_header = self.meta_header("Navigate Source &#183; %s" % project)
        page_header = self.header()
        menu = self.menu()
        sidebar = read_file("content/short_sidebar.html")
        footer = self.footer()

        # Put the page together and return it
        return read_file("content/template.html") % (meta_header, page_header, content, menu, sidebar, footer)

    setup_error_handling()
