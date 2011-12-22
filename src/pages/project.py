from src.pages.projects import Projects
from common.common import get_server_root

__author__ = 'jon'

class Project(Projects):


    def __init__(self, project_content, project_name, project_id):
        """
          Initialize this project page to display a single project

            @param  projectContent  The content of the project to display
        """

        self.project_content = project_content
        self.project_name = project_name
        self.project_id = project_id

        super(Project, self).__init__()


    def title(self):
        return "Jon Tedesco &#183; " + self.project_name

    def description(self):
        return "Jon Tedesco's projects &#183; " + self.project_name

    def main_content(self):

        # Get the skeleton for the project description
        projects_template = open('content/templates/project.html').read()
        project_content = projects_template % self.project_content
        project_content = project_content.replace('%s', get_server_root())

        # Remove the 'see more...' link and extra sections to hide
        project_content = project_content.replace('toHide', '')
#        project_content = project_content.replace('permalink', '')
        project_content = project_content.replace('See more...', '')

        return project_content
