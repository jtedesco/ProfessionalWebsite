import cherrypy
from common.common import get_server_root, get_default_keywords
from src.page import Page
from util.file_reader import read_file

__author__ = 'Jon'

class Projects(Page):
    """
        The page object for the projects page.
    """

    def main_content(self):
        """
            Return the central HTML content of this page
        """
        return read_file("content/pages/projects.html") % (get_server_root(), get_server_root(), get_server_root(), get_server_root(),
            get_server_root(), get_server_root(), get_server_root(), get_server_root(), get_server_root(), get_server_root())

    def title(self):
        return "Jon Tedesco &#183; Projects"

    def description(self):
        return "Jon Tedesco's projects &#183; Description, demos, and source code of Jon Tedesco's geeky projects"

    def keywords(self):
        return ["projects", "experience", "technology", "current", "past", "github", "sockit", "skills"] + get_default_keywords()

    def sidebar(self):
        """
            Get the sidebar code (traditionally the contact information)
        """
        return read_file("content/sidebar.html") % "projects"

    @cherrypy.expose
    def index(self):
        """
            Returns the content of this page
        """
        self.build_content()
        return self.content
  