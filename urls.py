from django.conf import settings
from professional_website.search import search
from professional_website.source_navigation import navigate_source, view_source
from professional_website.views import about_me, download, research, resume, projects, project, blog, blog_post
from django.conf.urls.defaults import patterns, url

urlpatterns = patterns('',

    # URLs for the simple pages
    url(r'^/?$', blog),
    url(r'^about_me/?$', about_me),
    url(r'^research/?$', research),
    url(r'^resume/?$', resume),

    # Projects-related pages
    url(r'^projects/?$', projects),
    url(r'^projects/(.+)$', project),

    # Blog-related pages
    url(r'^blog/$', blog),
    url(r'^blog/(.+)$', blog_post),

    # Functionality for navigating & viewing files
    url(r'^view_source/(.+)$', view_source),
    url(r'^navigate_source/(.+),(.+)$', navigate_source),

    # Search
    url(r'^search/(.+)$', search),

    # Download functionality
    url(r'^download/(.+)$', download),

    # Serve static files
    url(r'^static/admin/(.*)$', 'django.views.static.serve', {'document_root': settings.STATIC_ROOT}),
)