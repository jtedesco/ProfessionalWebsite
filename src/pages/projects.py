import os
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


        # Special block that says the following are current projects
        current_projects_title = """
            <h1 class="title">
                <span>current</span> projects
            </h1>
            <br/>
            <p>
                A variety of current and past projects that I pursue in my free time, and find
                interesting. I hope you find them interesting too.
            </p>
            <br/>
        """

        # List of projects to display on the pages
        projects = [current_projects_title]

        # Get the list of current projects
        current_projects_list = os.listdir('content/pages/projects/current')
        current_projects_list.sort(reverse=True)
        for current_project in current_projects_list:

            # Build the project HTML content
            project_content = open('content/pages/projects/current/' + current_project).read()
            if '%s' in project_content:
                project_content.replace('%s', get_server_root())

            projects.append(project_content)

        # Special block that says the rest are past projects
        past_projects_title = """
            <br/>
            <h1 class="title">
                <span>past</span> projects
            </h1>
            <br/>
        """
        projects.append(past_projects_title)

        # Get the list of past projects
        past_projects_list = os.listdir('content/pages/projects/past')
        past_projects_list.sort(reverse=True)
        for past_project in past_projects_list:

            # Build the project HTML content
            project_content = open('content/pages/projects/past/' + past_project).read()
            if '%s' in project_content:
                project_content.replace('%s', get_server_root())

            projects.append(project_content)

        # Build the paginated projects
        projects_content = self.__build_projects_page_content(projects)

        return projects_content


    def __build_projects_page_content(self, projects):
        """
          Builds the pages HTML, splitting the projects list into pages
        """

        # The template for the link to show/hide a page
        show_page_link_template = "<a class='page_%d_link' href='javascript:showPage(%d, %d);'>%d</a> &#183;\n"

        # The template for a page
        page_template = """
            <div class="page_%d">
                <div class="content">
                    %s
                </div>
            </div>
        """

        # Split the projects into 5 per page
        split_projects = self.__group(projects, 6)

        # Build the HTML links content
        links_html = ""
        for link_number in xrange(1, len(split_projects)):
            links_html += show_page_link_template % (link_number, link_number, len(split_projects), link_number)
        links_html += show_page_link_template[0:-9] % tuple([len(split_projects)]*4)

        # Build the project content HTML
        projects_content = ""
        page_number = 1
        for project_group in split_projects:
            page_content = "\n".join(project_group)
            projects_content += page_template % (page_number, page_content)
            page_number += 1

        projects_page_content_template = open('content/templates/projects.html').read()
        projects_page_content = projects_page_content_template % (links_html, projects_content, links_html)

        return projects_page_content
    

    def __group(self, results, groupSize):
        return [results[i : i + groupSize] for i in xrange(0, len(results), groupSize)]

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
        return read_file("content/widgets/sidebar.html") % "projects"

    @cherrypy.expose
    def index(self):
        """
            Returns the content of this page
        """
        self.build_content()
        return self.content
  