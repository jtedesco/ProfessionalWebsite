from src.pages.blog import Blog
from common.common import get_server_root

__author__ = 'jon'

class BlogPost(Blog):


    def __init__(self, post_content, post_title, post_id, post_date):
        """
          Initialize this blog post object to display a single post
        """

        self.post_content = post_content
        self.post_title = post_title
        self.post_id = post_id
        self.post_date = post_date

        super(BlogPost, self).__init__()


    def title(self):
        return "Jon Tedesco &#183; " + self.post_title

    def description(self):
        return "Jon Tedesco's blog &#183; " + self.post_title

    def main_content(self):

        # Get the skeleton for the project description
        post_template = open('content/templates/blog_post.html').read()
        post_content = post_template % self.post_content
        post_content = post_content.replace('%s', get_server_root())

        # Remove the 'see more...' link and extra sections to hide
        post_content = post_content.replace('toHide', '')
        post_content = post_content.replace('permalink', '')
        post_content = post_content.replace('See more...', '')

        return post_content
