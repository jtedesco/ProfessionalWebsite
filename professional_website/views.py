from pprint import pprint
from django.http import HttpResponse
from django.template.context import Context
from django.template.loader import get_template
from professional_website.common import get_server_root, get_generic_keywords, chunks
from professional_website.models import Project, Post


def about_me(request):
    """
      Builds and returns the homepage of the site
    """

    # The HTML template
    template = get_template('pages/about_me.html')

    # HTML Data for this page
    html = template.render(Context({
        'meta_description': 'Information for Jon Tedesco, a dedicated student and avid software developer at University' +
                            'of Illinois at Urbana-Champaign',
        'meta_keywords': ' '.join(get_generic_keywords()),
        'page_title': 'About Me &#183; Jon Tedesco',
        'word_cloud_name': 'about_me',
        'static': True,
        'server_root': get_server_root()
    }))

    return HttpResponse(html)


def research(request):
    """
      Builds and returns the research page of the site
    """

    # The HTML template
    template = get_template('pages/research.html')

    # HTML Data for this page
    html = template.render(Context({
        'meta_description': 'Jon Tedesco\'s research &#183; Research interests, projects, and papers',
        'meta_keywords': ' '.join(["research", "papers", "interests", "projects"] + get_generic_keywords()),
        'page_title': 'Research &#183; Jon Tedesco',
        'word_cloud_name': 'research',
        'server_root': get_server_root()
    }))

    return HttpResponse(html)


def resume(request):
    """
      Builds and returns the resume page of the site
    """

    # The HTML template
    template = get_template('pages/resume.html')

    # HTML Data for this page
    html = template.render(Context({
        'meta_description': "Jon Tedesco's r&eacute;sum&eacute; (resume) &#183; My education, experience,skills, coursework, papers, and awards"
        ,
        'meta_keywords': ' '.join(
            ["resume", "education", "experience", "skills", "coursework", "papers", "awards"] + get_generic_keywords()),
        'page_title': 'R&eacute;sum&eacute; &#183; Jon Tedesco',
        'word_cloud_name': 'resume',
        'server_root': get_server_root()
    }))

    return HttpResponse(html)


def projects(request):
    """
      Builds the projects page
    """

    # Get all projects, ordered by ID & grouped by active/inactive
    projects = list(Project.objects.extra(order_by=['-active', '-id']))

    # Split the projects ilnto chunks of 5 and add 'next' pointers
    chunkedProjects = list(chunks(projects, 5))
    for projectChunk in chunkedProjects:
        for projectIndex in xrange(0, len(projectChunk)-1):
            projectChunk[projectIndex].next = projectChunk[projectIndex+1]
        projectChunk[len(projectChunk)-1].next = None

    # Split projects into numbered pages
    pages = []
    num = 1
    for projectChunk in chunkedProjects:
        pages.append({
            'projects_list': projectChunk,
            'number': num
        })
        num += 1

    # The HTML template
    template = get_template('pages/projects.html')

    # HTML Data for this page
    html = template.render(Context({
        'meta_description': 'Jon Tedesco\'s projects &#183; Description, demos, and source code of Jon Tedesco\'s geeky projects'
        ,
        'meta_keywords': ' '.join(["projects", "experience", "technology", "github", "sockit",
                                   "skills"] + get_generic_keywords()),
        'page_title': 'Projects &#183; Jon Tedesco',
        'word_cloud_name': 'projects',
        'server_root': get_server_root(),
        'pages': pages,
        'number_of_pages': len(list(pages))
    }))

    return HttpResponse(html)


def project(request, name):
    """
      Builds a page containing information about a given project

        @param  name    The name of the project
    """

    project = Project.objects.get(name=name)

    # The HTML template
    template = get_template('pages/project.html')

    # HTML Data for this page
    html = template.render(Context({
        'meta_description': 'Jon Tedesco\'s "%s" project &#183; Description, demos, and source code of Jon Tedesco\'s geeky projects' % project.title
        ,
        'meta_keywords': ' '.join(["projects", "experience", "technology", "current", "past", "github", "sockit",
                                   "skills"] + get_generic_keywords()),
        'page_title': 'Projects &#183; Jon Tedesco &#183;' + project.title,
        'word_cloud_name': 'projects',
        'server_root': get_server_root(),
        'static': True,
        'project': project
    }))

    return HttpResponse(html)


def blog(request):
    """

    """

    # Get all posts, from most recent to least recent
    posts = list(Post.objects.extra(order_by=['-timestamp']))

    chunkedPosts = chunks(posts, 2)

    # Split posts into numbered pages
    pages = []
    num = 1
    for postChunk in chunkedPosts:
        pages.append({
            'posts_list': postChunk,
            'number': num
        })
        num += 1

    # The HTML template
    template = get_template('pages/blog.html')

    # HTML Data for this page
    html = template.render(Context({
        'meta_description': 'Jon Tedesco\'s blog &#183; Jon Tedesco\'s collection of thoughts and experiences of his geeky endeavors.'
        ,
        'meta_keywords': ' '.join(
            ["blog", "discussion", "geek", "projects", "software", "projects"] + get_generic_keywords()),
        'page_title': 'Blog &#183; Jon Tedesco',
        'word_cloud_name': 'blog',
        'server_root': get_server_root(),
        'pages': pages,
        'number_of_pages': len(list(pages))
    }))

    return HttpResponse(html)


def blog_post(request, name):
    """
      Builds a page containing a single blog post

        @param  name    The name of the post
    """

    # Get all posts, from most recent to least recent
    post = Post.objects.get(name=name)

    # The HTML template
    template = get_template('pages/blog_post.html')

    # HTML Data for this page
    html = template.render(Context({
        'meta_description': 'Jon Tedesco\'s blog &#183; Jon Tedesco\'s collection of thoughts and experiences of his geeky endeavors.'
        ,
        'meta_keywords': ' '.join(
            ["blog", "discussion", "geek", "projects", "software", "projects"] + get_generic_keywords()),
        'page_title': 'Blog &#183; Jon Tedesco &#183; ' + post.title,
        'word_cloud_name': 'blog',
        'server_root': get_server_root(),
        'post': post
    }))

    return HttpResponse(html)


def download(request, fileName):
    """
      Exposes functionality to download a file
    """

    response = HttpResponse(mimetype='application/force-download')
    response['Content-Disposition'] = 'attachment; filename=%s' % fileName
    response['X-Sendfile'] = fileName

    return response