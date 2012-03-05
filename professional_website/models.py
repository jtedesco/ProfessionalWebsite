from django.db import models
from django.db.models.fields import CharField, BooleanField, URLField, DateTimeField
from django.db.models.fields.related import ForeignRelatedObjectsDescriptor, ForeignKey

class Project(models.Model):
    """
      Object representing a project
    """

    # The unique camelCase project name
    name = CharField(unique=True, max_length=255)

    # The project title to display
    title = CharField(max_length=255)
    subtitle = CharField(max_length=255)

    # Whether or not the project is still actively developed
    active = BooleanField()

    # The project description that is always visible
    shortDescription = CharField(max_length=4096)

    # The project description that is shown/hidden through the javascript link
    longDescription = CharField(max_length=4096)

    # The link to the source code location
    sourceUrl = URLField()

    # The link to the live URL
    liveUrl = URLField()


class Comment(models.Model):
    """
      Object representing a comment on a blog post
    """

    author = CharField(max_length=255)
    timestamp = DateTimeField()
    content = CharField(max_length=255)


class Post(models.Model):
    """
      Object model representing a blog post
    """

    # The unique camelCase name of the blog post
    name = CharField(unique=True, max_length=255)

    # The title of the blog post to display
    title = CharField(max_length=255)
    subtitle = CharField(max_length=255)

    # The date & content of the blog post
    timestamp = DateTimeField()
    content = CharField(max_length=16384)

    # The comments associated with this blog post
    comments = ForeignRelatedObjectsDescriptor(Comment)
