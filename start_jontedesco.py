import cherrypy
from src.factory import PageFactory
from src.page import Page

__author__ = 'Jon Tedesco'


if __name__ == '__main__':
    root_page = PageFactory.create_root_page()
    cherrypy.quickstart(root_page, config="site.conf")
