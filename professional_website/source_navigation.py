import os
from django.http import HttpResponse, Http404
from django.template.context import Context
from django.template.loader import get_template
import sys
from professional_website.common import get_root_directory, get_server_root, get_generic_keywords

__author__ = 'jon'

def view_source(request, path):
    """
        View the source code of the given file, in the standard <code>view_source</code> template.

            @param  path    The path of the file to display (relative to the root of the site)
    """

    languages = {
        "py": "python",
        "rb": "ruby",
        "js": "javascript",
        "html": "html",
        "htm": "html",
        "cpp": "cpp",
        "c": "c",
        "cc": "cpp",
        "php": "php",
        "java": "java",
        "xml": "xml"
    }

    # Read the source code file
    try:
        source_file = open(get_root_directory() + "/code/" + path, 'r')
        source_code = source_file.read()
        source_file.close()

        # Data structure to hold information for this page
        data = {
            'meta_description': 'Homepage of Jon Tedesco, a dedicated student and avid software developer at University' +
                                'of Illinois at Urbana-Champaign',
            'meta_keywords': ' '.join(get_generic_keywords()),
            'page_title': 'View Source &#183; %s (%s)',
            'word_cloud_name': 'about_me',
            'server_root': get_server_root(),
            'static': True,
            'path': None,
            'language': None,
            'content': None
        }

        # Check if this is a binary file
        binary_file_extensions = ['jpg', 'png', 'bmp', 'jpeg']
        file_extension = path[path.rfind('.') + 1:]
        if file_extension not in binary_file_extensions:
            # Update data
            data['content'] = source_code
            if file_extension in languages:
                data['language'] = languages[file_extension]
            else:
                data['language'] = 'plain'
            data['path'] = path
            data['page_title'] = data['page_title'] % (path, data['language'].title())

        else:
            data['path'] = path
            data['page_title'] = 'View Source &#183; ' + path
            data['image'] = True

        # Fill HTML template
        template = get_template('pages/view_source.html')
        html = template.render(Context(data))
        return HttpResponse(html)

    except IOError:

        # Throw a 404 if the file can't be read
        raise Http404

def navigate_source(request, path, project):
    """
        View the contents of a directory, and provide a simple interface with which to navigate the directory
        structure, and view the source of files in the hierarchy.

            @param  path    The path, relative to the root of the site, to display
            @param  project The name of the source project we're viewing
    """

    # Setup data structures
    root = get_root_directory() + '/code/'
    files = []
    directories = []

    # Collect the files & directories separately
    for file in os.listdir(root + path):
        # Add directories and files to separate lists
        if os.path.isfile(root + path + "/" + file):
            files.append((path + "/" + file, file))
        elif os.path.isdir(root + path):
            directories.append((path + "/" + file, file))

    try:
        # Try to read the 'exclude' file
        if os.path.exists(root + path + '/' + '.exclude'):
            exclude_file = open(root + path + '/' + '.exclude', 'r')

            # Remove the files or directories from the list gathered
            for exclude_file_name in exclude_file:
                exclude_file_name = exclude_file_name.strip()
                exclude_path = path + '/' + exclude_file_name
                if os.path.exists(root + exclude_path):
                    if os.path.isdir(root + exclude_path):
                        directories.remove(exclude_path)
                    else:
                        files.remove(exclude_path)
                else:
                    print "Error parsing exclude file, could not open path '%s'" % exclude_path

    except Exception:
        print "Error parsing 'exclude' file: '%s'" % str(sys.exc_info()[1])

    # Fill HTML template
    template = get_template('pages/navigate_source.html')
    html = template.render(Context({
        'meta_description': 'Homepage of Jon Tedesco, a dedicated student and avid software developer at University' +
                            'of Illinois at Urbana-Champaign',
        'meta_keywords': ' '.join(get_generic_keywords()),
        'page_title': 'Navigate Source &#183; ' + project,
        'word_cloud_name': 'about_me',
        'server_root': get_server_root(),
        'path': path,
        'project': project,
        'files': files,
        'static': True,
        'directories': directories
    }))
    return HttpResponse(html)