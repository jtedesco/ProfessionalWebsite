import cherrypy
from src.page import Page
from util.file_reader import read_file

__author__ = 'Jon'

class Code(Page):
    """
        The page object for the code page.
    """

    def main_content(self):
        """
            Return the central HTML content of this page
        """
        return read_file("content/pages/code.html")


    def title(self):
        return "Jon Tedesco &#183; Code"

    
    @cherrypy.expose
    def index(self):
        """
            Returns the content of this page
        """
        self.build_content()
        return self.content
  