__author__ = 'Jon Tedesco'


def get_root_directory():
    return "/Users/jontedesco/Documents/Dropbox/Projects/ProfessionalWebsite"


def get_server_root():
    return "http://localhost:8000/"


def get_generic_keywords():
    """
      Returns the generic meta keywords pertinent to the site
    """

    return ["Tedesco", "Jon", "jontedesco", "software", "programming", "developer", "geek", "University of Illinois",
            "cs",
            "computer science", "Jonathan", "Christopher", "Java", "C", "C++", "C#", "Python", "Ruby", "projects",
            "projects", "undergraduate", "graduate", "research", "programming", "finance", "math"]


def chunks(list, size):
    """ Yield successive n-sized chunks from l.
    """
    for i in xrange(0, len(list), size):
        yield list[i:i + size]