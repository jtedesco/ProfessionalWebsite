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
        currentProjectsTitle = """
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
        projects = [currentProjectsTitle]

        # Get the list of current projects
        currentProjectsList = os.listdir('content/pages/projects/current')
        currentProjectsList.sort(reverse=True)
        for currentProject in currentProjectsList:

            # Build the project HTML content
            projectContent = open('content/pages/projects/current/' + currentProject).read()
            if '%s' in projectContent:
                projectContent.replace('%s', get_server_root())

            projects.append(projectContent)

        # Special block that says the rest are past projects
        pastProjectsTitle = """
            <br/>
            <h1 class="title">
                <span>past</span> projects
            </h1>
            <br/>
        """
        projects.append(pastProjectsTitle)

        # Get the list of past projects
        pastProjectsList = os.listdir('content/pages/projects/past')
        pastProjectsList.sort(reverse=True)
        for pastProject in pastProjectsList:

            # Build the project HTML content
            projectContent = open('content/pages/projects/past/' + pastProject).read()
            if '%s' in projectContent:
                projectContent.replace('%s', get_server_root())

            projects.append(projectContent)

        # Build the paginated projects
        projectsContent = self.__buildProjectsPages(projects)

        return projectsContent


    def __buildProjectsPages(self, projects):
        """
          Builds the pages HTML, splitting the projects list into pages
        """

        # The template for the link to show/hide a page
        linkTemplate = "<a class='page_%d_link' href='javascript:showPage(%d);'>%d</a> &#183;\n"

        # The template for a page
        pageTemplate = """
            <div class="page_%d">
                <div class="content">
                    %s
                </div>
            </div>
        """

        # Split the projects into 5 per page
        splitProjects = self.__group(projects, 6)

        # Build the HTML links content
        linksHtml = ""
        for linkNumber in xrange(1, len(splitProjects)):
            linksHtml += linkTemplate % tuple([linkNumber]*3)
        linksHtml += linkTemplate[0:-9] % tuple([len(splitProjects)]*3)

        # Build the project content HTML
        projectsContent = ""
        pageNumber = 1
        for projectGroup in splitProjects:
            pageContent = "\n".join(projectGroup)
            projectsContent += pageTemplate % (pageNumber, pageContent)
            pageNumber += 1

        projectsPageContentTemplate = open('content/templates/projects.html').read()
        projectsPageContent = projectsPageContentTemplate % (linksHtml, projectsContent, linksHtml)

        return projectsPageContent
    

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
  