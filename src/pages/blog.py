import os
from pprint import pprint
import cherrypy
from common.common import get_default_keywords, get_server_root
from src.page import Page
from util.file_reader import read_file

__author__ = 'Jon'

class Blog(Page):
    """
        The page object for the blog page.
    """

    def main_content(self):
        """
            Return the central HTML content of this page
        """
        blog_content = self.__build_blog_content()
        return blog_content

    def __build_blog_content(self):
        """
          Build the HTML content for the blog entries
        """

        # Collect the path & filename for each
        blog_entries = []
        blog_entry_root_path = 'content/pages/blog'
        current_id = 0
        for blog_file_name in os.listdir(blog_entry_root_path):

            # Parse the file name
            split_name = blog_file_name[:-5].split(':')
            date = split_name[0].strip()
            title = split_name[1].strip()

            # Add this new blog entry to the data structure
            blog_entries.append({
                'path' : blog_entry_root_path + '/' + blog_file_name,
                'date' : date,
                'title' : title,
                'id' : current_id
            })
            current_id += 1

        # Order them from oldest to newest
        blog_entries = sorted(blog_entries, key=lambda k: k['date'], reverse=True)

        # Build the blog HTML content
        blog_template = read_file('content/templates/blog.html')
        blog_posts_content = '<div class="content">\n'
        for blog_entry in blog_entries:
            blog_post_content = open(blog_entry['path']).read()
            blog_posts_content += blog_post_content
        blog_posts_content += '</div>\n'
        blog_content = blog_template % blog_posts_content

        return blog_content


    def description(self):
        return "Jon Tedesco's blog &#183; Jon Tedesco's collection of thoughts and experiences of his geeky endeavors."

    def keywords(self):
        return ["blog", "discussion", "geek", "projects", "software", "projects"] + get_default_keywords()

    def title(self):
        return "Jon Tedesco &#183; Blog"

    def sidebar(self):
        """
            Get the sidebar code (traditionally the contact information)
        """
        return read_file("content/widgets/sidebar.html") % (get_server_root(), get_server_root(), get_server_root(), get_server_root(), get_server_root(), "blog")

    @cherrypy.expose
    def index(self):
        """
            Returns the content of this page
        """
        self.build_content()
        return self.content

