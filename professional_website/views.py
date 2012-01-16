from django.http import HttpResponse
from django.template.context import Context
from django.template.loader import get_template
from professional_website.common import get_server_root, get_generic_keywords, chunks, get_root_directory
from professional_website.models import Project, Post


def home(request):
    """
      Builds and returns the homepage of the site
    """

    # The HTML template
    template = get_template('pages/home.html')

    # HTML Data for this page
    html = template.render(Context({
        'meta_description' : 'Homepage of Jon Tedesco, a dedicated student and avid software developer at University' +
                             'of Illinois at Urbana-Champaign',
        'meta_keywords' : ' '.join(get_generic_keywords()),
        'page_title' : 'Jon Tedesco &#183; Home',
        'word_cloud_name' : 'home',
        'server_root' : get_server_root()
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
        'meta_description' : 'Jon Tedesco\'s research &#183; Research interests, projects, and papers',
        'meta_keywords' : ' '.join(["research", "papers", "interests", "projects"] + get_generic_keywords()),
        'page_title' : 'Jon Tedesco &#183; Research',
        'word_cloud_name' : 'research',
        'server_root' : get_server_root()
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
        'meta_description' : "Jon Tedesco's r&eacute;sum&eacute; (resume) &#183; My education, experience,skills, coursework, papers, and awards",
        'meta_keywords' : ' '.join(["resume", "education", "experience", "skills", "coursework", "papers", "awards"] + get_generic_keywords()),
        'page_title' : 'Jon Tedesco &#183; R&eacute;sum&eacute;',
        'word_cloud_name' : 'resume',
        'server_root' : get_server_root()
    }))

    return HttpResponse(html)


def projects(request):
    """
      Builds the projects page
    """

    # Get all projects, ordered by ID & grouped by active/inactive
    projects = list(Project.objects.extra(order_by=['-active', '-id']))

    chunkedProjects = chunks(projects, 5)

    # Split projects into numbered pages
    pages = []
    num = 1
    for projectChunk in chunkedProjects:
        pages.append({
            'projects_list' : projectChunk,
            'number' : num
        })
        num += 1

    # The HTML template
    template = get_template('pages/projects.html')

    # HTML Data for this page
    html = template.render(Context({
        'meta_description' : 'Jon Tedesco\'s projects &#183; Description, demos, and source code of Jon Tedesco\'s geeky projects',
        'meta_keywords' : ' '.join(["projects", "experience", "technology", "current", "past", "github", "sockit", "skills"] + get_generic_keywords()),
        'page_title' : 'Jon Tedesco &#183; Projects',
        'word_cloud_name' : 'projects',
        'server_root' : get_server_root(),
        'pages' : pages,
        'number_of_pages' : len(list(pages))
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
        'meta_description' : 'Jon Tedesco\'s "%s" project &#183; Description, demos, and source code of Jon Tedesco\'s geeky projects' % project.title,
        'meta_keywords' : ' '.join(["projects", "experience", "technology", "current", "past", "github", "sockit", "skills"] + get_generic_keywords()),
        'page_title' : 'Jon Tedesco &#183; Projects &#183; ' + project.title,
        'word_cloud_name' : 'projects',
        'server_root' : get_server_root(),
        'static' : True,
        'project' : project
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
            'posts_list' : postChunk,
            'number' : num
        })
        num += 1

    # The HTML template
    template = get_template('pages/blog.html')

    # HTML Data for this page
    html = template.render(Context({
        'meta_description' : 'Jon Tedesco\'s blog &#183; Jon Tedesco\'s collection of thoughts and experiences of his geeky endeavors.',
        'meta_keywords' : ' '.join(["blog", "discussion", "geek", "projects", "software", "projects"] + get_generic_keywords()),
        'page_title' : 'Jon Tedesco &#183; Blog',
        'word_cloud_name' : 'blog',
        'server_root' : get_server_root(),
        'pages' : pages,
        'static' : True,
        'number_of_pages' : len(list(pages))
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
        'meta_description' : 'Jon Tedesco\'s blog &#183; Jon Tedesco\'s collection of thoughts and experiences of his geeky endeavors.',
        'meta_keywords' : ' '.join(["blog", "discussion", "geek", "projects", "software", "projects"] + get_generic_keywords()),
        'page_title' : 'Jon Tedesco &#183; Blog &#183; ' + post.title,
        'word_cloud_name' : 'blog',
        'server_root' : get_server_root(),
        'post' : post
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