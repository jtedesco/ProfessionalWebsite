__author__ = 'Jon'


def read_file(path):
    """
        Reads the content of a file from a given file path.

        @param  path    The file to read
        @return The text content of the file
    """

    file = open(path, 'r')
    content = ""
    for line in file:
        content += line
    file.close()
    return content
