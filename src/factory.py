from src.page import Page
from src.pages.blog import Blog
from src.pages.home import Home
from src.pages.projects import Projects
from src.pages.research import Research
from src.pages.resume import Resume

__author__ = 'Jon'


class PageFactory(object):
    """
        Page factory that builds page objects given keywords for the page, using enums representing the pages and
        projects.
    """
    @staticmethod
    def create_root_page():
        """
            Returns the generic top-level page
        """
        # Create a skeleton parent page
        root_page = Home()

        # Create the subpages for this parent page. These subpages are the root pages of the site.
        root_page.home = Home()
        root_page.projects = Projects() 
        root_page.resume = Resume()
        root_page.research = Research()
        root_page.blog = Blog()

        return root_page
