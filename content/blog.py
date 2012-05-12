import sys
from datetime import datetime
from professional_website.models import Post

__author__ = 'jon'

posts = [
    Post(
        name = 'sockIt',
        title = 'SockIt',
        subtitle = 'An asynchronous, client-side Javascript networking plugin',
        timestamp = datetime.strptime('08/01/2011', '%m/%d/%Y'),
    ),
    Post(
        name = 'automatedUbuntuBackupAndRestore',
        title = 'Automated Ubuntu Backup and Restore',
        subtitle = 'Backup your Ubuntu system safely, efficiently, and effortlessly',
        timestamp = datetime.strptime('09/01/2011', '%m/%d/%Y')
    ),
    Post(
        name = 'personalPasswordSecurity',
        title = 'Secure Your Digital Life',
        subtitle = 'Lock down your digital life without sacrificing personal convenience',
        timestamp = datetime.strptime('02/08/2012', '%m/%d/%Y')
    ),
    Post(
        name = 'pageRankFetching',
        title = 'Python PageRank Fetching',
        subtitle = 'How to programmatically fetch the PageRank of any URL with Python',
        timestamp = datetime.strptime('03/19/2012', '%m/%d/%Y')
    )
]

# Sync these projects with the backend database
for post in posts:
    try:
        post.save()
    except Exception:
        try:

            # Retrieve the project object already in the database
            existingProject = Post.objects.get(name=post.name)

            # Update all fields of this project
            existingProject.title = post.title
            existingProject.subtitle = post.subtitle
            existingProject.timestamp = post.timestamp
            with open('blog/' + post.name + '.html') as f:
                content = f.read()
            existingProject.content =  content
            existingProject.save()

        except Exception:
            print "Skipping '%s': %s" % (post.name, str(sys.exc_info()[1]))