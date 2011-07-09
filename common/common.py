from util.file_reader import read_file


__author__ = 'Jon Tedesco'

def get_root_directory():
    return "/home/jon/Documents/Dropbox/Projects/ProfessionalWebsite"

def get_server_root():
    return "http://localhost:8080/"

def get_under_construction_content():
    return read_file("content/under_construction.html")