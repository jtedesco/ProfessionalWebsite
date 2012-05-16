import os
import sys
from professional_website.models import Project

# The list of projects to insert into the database
projects = [

    Project(
        name = 'distributedSharedMemory',
        title = 'Distributed Shared Memory',
        subtitle = 'A distributed shared memory system implementing release consistency',
        active = False,
        sourceUrl = '/navigate_source/DistributedSharedMemory,DistributedSharedMemory'
    ),
    
    Project(
        name = 'distributedHashTable',
        title = 'Distributed Hash Table',
        subtitle = 'A C++ implementation of a distributed hash table',
        active = False,
        sourceUrl = '/navigate_source/DistributedHashTable,DistributedHashTable'
    ),

    Project(
        name = 'distributedFileSystem',
        title = 'Distributed File System',
        subtitle = 'A fault-tolerant distributed file system',
        active = False,
        sourceUrl = '/navigate_source/DistributedFileSystem'
    ),

    Project(
        name = 'bitLibrary',
        title = 'Bit Library',
        subtitle = 'Java Based Image Editor and Image Compression Engine',
        active = False,
        sourceUrl = '/navigate_source/BitLibrary,BitLibrary'
    ),

    Project(
        name = 'fdClosureCalculator',
        title = 'FD Closure Calculator',
        subtitle = 'Calculator for Functional Dependencies of Databases',
        active = False,
        sourceUrl = '/navigate_source/FDClosureCalculator,FDClosureCalculator'
    ),

    Project(
        name = 'flightData',
        title = 'FlightData',
        subtitle = 'Flight network statistics engine',
        active = False,
        sourceUrl = '/navigate_source&FlightData'
    ),

    Project(
        name = 'sportSQL',
        title = 'SportSQL',
        subtitle = 'PHP &amp; Database-Driven Fantasy Football Trading Website',
        active = False,
        sourceUrl = '/navigate_source/SportSQL,SportSQL'
    ),

    Project(
        name = 'pyGoogle',
        title = 'PyGoogle',
        subtitle = 'Python-based illinois.edu Search Engine',
        active = False,
        sourceUrl = '/navigate_source/PyGoogle,PyGoogle'
    ),

    Project(
        name = 'collaborativeMusicFilter',
        title = 'Collaborative Music Filter',
        active = False,
        sourceUrl = '/navigate_source/CollaborativeMusicSelector,CollaborativeMusicSelector'
    ),
    
    Project(
        name = 'personalCloudServer',
        title = 'Personal Cloud Server',
        subtitle = 'My own private server',
        active = True,
    ),

    Project(
        name = 'professionalWebsite',
        title = 'This Website',
        subtitle = 'Professional website and blog',
        active = True,
        sourceUrl = 'http://www.github.com/jtedesco/ProfessionalWebsite',
        liveUrl = 'http://www.jontedesco.net/'
    ),

    Project(
        name = 'generationsWebsite',
        title = 'Generations Website',
        subtitle = 'Band website',
        active = False,
        sourceUrl = '/navigate_source/GenerationsWebsite,GenerationsWebsite',
        liveUrl = 'http://www.generationschicago.com/'
    ),

    Project(
        name = 'sockIt',
        title = 'SockIt',
        subtitle = 'An asynchronous, client-side Jsavascript networking plugin',
        active = False,
        sourceUrl = 'http://github.com/sockit/sockit',
        liveUrl = 'http://sockit.github.com/'
    ),

    Project(
        name = 'marchingIlliniSaxWebsite',
        title = 'Marching Illini Sax Website',
        subtitle = 'Homepage of the Marching Illini Saxophone Section',
        active = True,
        liveUrl = 'http://marchingillinisaxes.com/'
    ),

    Project(
        name = 'freeBox',
        title = 'FreeBox',
        subtitle = 'A free rsync-based file synchronization tool',
        active = True,
        sourceUrl = 'https://github.com/jtedesco/FreeBox'
    ),
    Project(
        name = 'removeDuplicateCodeInConstructorsRefactoring',
        title = 'Java Eclipse Constructor Refactoring',
        subtitle = 'An Eclipse Java refactoring to remove duplicate code from constructors',
        active = False,
        sourceUrl = 'https://github.com/jtedesco/RemoveDuplicateCodeFromConstructors'
    ),
    Project(
        name = 'eduWrite',
        title = 'EduWrite',
        subtitle = 'Real-time collaborative note-taking',
        active = True,
    ),
    Project(
        name = 'nimbos',
        title = 'Nimbos',
        subtitle = 'A Hadoop cluster prediction framework',
        active = True,
        sourceUrl = 'https://github.com/jtedesco/Nimbos'
    )
]

# Sync these projects with the backend database
for project in projects:
    try:
        project.save()
    except Exception:

        existingProject = None
        while existingProject is None:

            # Retrieve the project object already in the database
            try:
                existingProject = Project.objects.get(name=project.name)
            except Exception, exception:
                print "Retrying '%s': %s" % (project.name, sys.exc_info()[1])

        # Update all fields of this project
        existingProject.title = project.title
        existingProject.subtitle = project.subtitle
        existingProject.active = project.active
        existingProject.sourceUrl = project.sourceUrl
        existingProject.liveUrl = project.liveUrl

        # Add the content for the project from disk if it exists
        shortDescriptionPath = os.path.join('projects', existingProject.name, 'shortDescription.html')
        if os.path.exists(shortDescriptionPath):
            with open(shortDescriptionPath) as f:
                existingProject.shortDescription = f.read()

        longDescriptionPath = os.path.join('projects', existingProject.name, 'longDescription.html')
        if os.path.exists(longDescriptionPath):
            with open(longDescriptionPath) as f:
                existingProject.longDescription = f.read()

        existingProject.save()