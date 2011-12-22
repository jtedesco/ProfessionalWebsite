import cherrypy
from common.common import  get_default_keywords
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
    
    def description(self):
        return "Jon Tedesco's r&eacute;sum&eacute; (resume) &#183; My education, experience,skills, coursework, publications, and awards"

    def keywords(self):
        return ["resume", "education", "experience", "skills", "coursework", "publications", "awards"] + get_default_keywords()

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
