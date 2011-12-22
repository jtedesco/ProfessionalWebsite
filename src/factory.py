from src.page import Page
from src.pages.blog import Blog
from src.pages.home import Home
from src.pages.project import Project
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
        root_page.projects = PageFactory.create_projects_page()
        root_page.resume = Resume()
        root_page.research = Research()
        root_page.blog = Blog()

        return root_page


    @staticmethod
    def create_projects_page():
        """
          Builds the projects page, and all project page subpages
        """

        projects_page = Projects()

        # Expose each project as a subpage for this page
        for current_project_name in projects_page.current_projects:
            exec("projects_page.%s = Project(projects_page.current_projects[current_project_name]['content'], projects_page.current_projects[current_project_name]['name'], %d)" %
                 (projects_page.current_projects[current_project_name]['name'], projects_page.current_projects[current_project_name]['id']))
        for past_project_name in projects_page.past_projects:
            exec("projects_page.%s = Project(projects_page.past_projects[past_project_name]['content'], projects_page.past_projects[past_project_name]['name'], %d)" %
                 (projects_page.past_projects[past_project_name]['name'], projects_page.past_projects[past_project_name]['id']))

        return projects_page
