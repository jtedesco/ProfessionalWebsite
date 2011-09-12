import cherrypy
from common.common import get_server_root
from src.page import Page
from util.file_reader import read_file

__author__ = 'Jon'

class Resume(Page):
    """
        The page object for the resume page.
    """

    def main_content(self): 
        """
            Return the central HTML content of this page
        """
        return read_file("content/pages/resume.html")

    def title(self):
        return "Jon Tedesco &#183; R&eacute;sum&eacute;"

    def sidebar(self):
        """
            Get the sidebar code (traditionally the contact information)
        """
        return read_file("content/sidebar.html") % "resume"

    @cherrypy.expose
    def index(self):
        """
            Returns the content of this page
        """
        self.build_content()
        return self.content
