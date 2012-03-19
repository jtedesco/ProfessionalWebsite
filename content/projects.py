import sys
from professional_website.models import Project

# The list of projects to insert into the database
projects = [

    Project(
        name = 'distributedSharedMemory',
        title = 'Distributed Shared Memory',
        subtitle = 'A distributed shared memory system implementing release consistency',
        active = False,
        shortDescription = '''
            <p>
                This project consisted of two components: first, the coordinator, a process that uses an input and
                configuration files to run simulations of real-world use cases, and second, the shared memory node.
            </p>
            ''',
        longDescription = '''
            <h4><br/>Coordinator</h4>
            <p>
                This node provides hooks for running simulations in the shared memory system, and handles
                mutual exclusion in our implementation. It reads input files to run commands in the
                memory system, and intermediates between the nodes of the memory system.
            </p>
            <br/>

            <h4><br/>Shared Memory Node</h4>
            <p>
                Each node of the memory system implements <a href="http://en.wikipedia.org/wiki/Release_consistency">release consistency</a>
                by locally caching data as long as it knows that it is the only process in the critical section. Whenever
                it must read some data, it contacts other nodes in the memory system, and itself holds only a small
                portion of the total data of the system.
            </p>
        ''',
        sourceUrl = '/navigate_source/DistributedSharedMemory,DistributedSharedMemory'
    ),
    
    Project(
        name = 'distributedHashTable',
        title = 'Distributed Hash Table',
        subtitle = 'A C++ implementation of a distributed hash table',
        active = False,
        shortDescription = '''
            <p>
                A C++ implementation of a <a href="http://en.wikipedia.org/wiki/Distributed_hash_table">distributed hash table</a>
                (DHT) that consisted of two components: first, a process that uses an input file to
                run simulations of real-world use cases, and second, a DHT node, which is only aware
                of each other node in its adjacency table.
            </p>
            ''',
        longDescription = '''
            <h4><br/>Listener</h4>
            <p>
                This process read input files, and sent commands to the entry point of the DHT
                to simulate clients requesting data or adding or leaving the DHT. This was
                used for performance analysis of the hash table, and to provide debugging hooks
                to test our implementation.<br/><br/>
            </p>
            <h4><br/>DHT Node</h4>
            <p>
                A node in the hash table represented a single process, that could leave or join
                the table arbitrarily. When the node was connected to the hash table, it
                was aware of its predecessor and successor in the ring, and held a table that
                connected to <code>m</code> other nodes. These tables at each node formed
                hypercube-like structure within the DHT, so that traversing the entire table to
                perform some action requires on <code>log(n)</code> time.
            </p>
            <p>
                Since each node was represented by a separate process, all communication between
                nodes occurred via network communication. Likewise, all requests, including
                finding data in the DHT, removing data from the DHT, or modifying the DHT
                structure itself, were processed concurrently. While simple, this implementation
                is scalable across many machines.
            </p>
        ''',
        sourceUrl = '/navigate_source/DistributedHashTable,DistributedHashTable'
    ),

    Project(
        name = 'distributedFileSystem',
        title = 'Distributed File System',
        subtitle = 'A fault-tolerant distributed file system',
        active = False,
        shortDescription = '''
            <p>
                This project consisted of three parts: server nodes in the distributed
                system, a load balancer that maintains the transparency of the server pool
                to the client, and the client, which requests files, and in our tests, kills
                server arbitrary processes to test our system and ensure file validity in the
                event of failure.
            </p>
            ''',
        longDescription = '''
            <h4><br/>Server Node</h4>
            <p>
                Each server node is represented by a process in our implementation, and each server
                sends periodic heartbeats to one other server, so that the pool of servers
                forms a heartbeating ring. In our trials, server faults were always detected and recovered
                within a second. Each server held two TCP connections for heartbeating, and a TCP connection
                to the load balancer for file requests and failure detection.<br/>
            </p>

            <h4><br/>Load Balancer</h4>
            <p>
                The load balancer was responsible for forming a layer of abstraction between the client and the
                server pool. It queued requests from the client, and recording server loads from the server pool.
                Each file requested by a client was stored at multiple server nodes, and the load balancer was responsible
                for determining the server currently under the lightest load. Likewise, the load balancer initialized the
                fault detetection system, and handled server crashes by redistributing files as necessaryr, even restarting
                active file transfers if necessary.<br/>
            </p>
            
            <h4><br/>Client</h4>
            <p>
                In our implementation, the client naively requested files from a input file, without pausing
                in between requests. We used the client in our trials to test the performance and fault tolerance
                of our system.
            </p>
        ''',
        sourceUrl = '/navigate_source/DistributedFileSystem'
    ),

    Project(
        name = 'bitLibrary',
        title = 'Bit Library',
        subtitle = 'Java Based Image Editor and Image Compression Engine',
        active = False,
        shortDescription = '''
            <p>
                This project is written purely in Java, and presents a GUI for
                exporting images to compressed RLE and other image compression formats
                using additional bitwise compression algorithms such as Gzip, Bzip, and
                zip archives commonly used in Windows. Compression functionality is
                highly configurable and allows both importing and exporting compressed
                and uncompressed images.
            </p>
            <p>
                Likewise, this project uses Java's Swing framework to built a complex GUI that
                allows arbitrary filters, distortions, and effects on images, as well as
                importing and exporting a variety of standard and custom image formats.
            </p>
            ''',
        sourceUrl = '/navigate_source/BitLibrary,BitLibrary'
    ),

    Project(
        name = 'fdClosureCalculator',
        title = 'FD Closure Calculator',
        subtitle = 'Calculator for Functional Dependencies of Databases',
        active = False,
        shortDescription = '''
            <p>
                This project takes inputs of dependency rules of a database and returns
                the list of simplified functional dependencies that the database maintains.
                It can be used to help create a <a href="http://en.wikipedia.org/wiki/Boyce-Codd_normal_form">BCNF</a>
                decomposition of a database or simply to analyze the data dependency in a database.
            </p>
            ''',
        sourceUrl = '/navigate_source/FDClosureCalculator,FDClosureCalculator'
    ),

    Project(
        name = 'flightData',
        title = 'FlightData',
        subtitle = 'Flight network statistics engine',
        active = False,
        shortDescription = '''
           <p>
               This project allows import and export of data for a (fictional) company's
               flight network between a graph structure stored in memory and an external
               <a href="http://www.json.org/">JSON</a> format.
               This allows calculation of mileage, costs to the airline, and routing
               information, and was used for programming studio as an exercise in good programming
               practices. This project uses the command design pattern to allow users to seamlessly
               communicate with the backend, Doxygen for extensive automated documentation,
               and Lucene to allow for robust queries in the graph structure.
           </p>
            ''',
        sourceUrl = '/navigate_source&FlightData'
    ),

    Project(
        name = 'sportSQL',
        title = 'SportSQL',
        subtitle = 'PHP &amp; Database-Driven Fantasy Football Trading Website',
        active = False,
        shortDescription = '''
            <p>
                For database systems fall 2010, one of my classmates and I implemented our
                first database driven website. It implements several interesting features,
                including an elegant interface, session-based login system for users, SQL
                injection protection, and automatic statistics-based game simulation.
            </p>
            <p>
                On a weekly basis, our website scrapes the NFL homepage for game results,
                and uses our own scoring heuristic to determine wins and losses for fantasy
                teams. Our interface allows users to create accounts, make live player trades
                with other users, and manage fantasy teams.
            </p>
            ''',
        sourceUrl = '/navigate_source/SportSQL,SportSQL'
    ),

    Project(
        name = 'pyGoogle',
        title = 'PyGoogle',
        subtitle = 'Python-based illinois.edu Search Engine',
        active = False,
        shortDescription = '''
            <p>
                For programming studio, I spent a few weeks learning the Python programming language, by developing a
                feature-rich search engine for the illinois.edu domain. It features an extensive test suite built on the
                <a href="http://code.google.com/p/mockito-python/">Mockito</a> test framework, and extensive auto-generated
                documentation using <a href="http://www.stack.nl/~dimitri/doxygen/index.html">Doxygen</a> customized for Python.
                Likewise, it uses HTML tags to weight keywords and titles, implements politeness checking, ranks results
                using Python toolkit <a href="http://whoosh.ca/">Whoosh!</a>, and re-ranks results using Google's
                <a href="http://en.wikipedia.org/wiki/PageRank">PageRank</a> algorithm on a graph structure stored in memory.
            </p>
            ''',
        longDescription = '''
            <p>
                Built entirely in Python, this project consists of three primary components, the crawler, indexer, and GUI,
                any of which can be run separately or combined with any of the other two components.
                The entire project is run on the <a href="http://en.wikipedia.org/wiki/Just-in-time_compilation">JIT</a>
                compiler <a href="http://pypy.org/">PyPy</a>, and is highly tunable and scalable. For a demo on my netbook
                at the end of this semester, I ran this project with roughly nearly 100 threads, but can easily tweak similar
                parameters to tune performance.<br/>
            </p>

            <h4><br/>Crawler</h4>
            <p>
                The crawler launches with an arbitrary amount of threads, scaling to the available number of CPUs,
                 and essentially outputs results into a MySQL database that is separately accessed by the indexer.
                 The crawler likewise implements common real world crawler features, including breadth-first crawling
                 and politeness checking, and uses a variety of toolkits, including
                 <a href="http://www.crummy.com/software/BeautifulSoup/">BeatifulSoup</a> for HTML parsing. Using this parser,
                 the crawler separates out the raw html content, title, and meta keywords to better search the pool of crawled pages.<br/>
            </p>

            <h4><br/>Indexer</h4>
            <p>
                The indexer scales to the resources available on the host computer, reads in the output of the crawler from
                 a shared MySQL database, and creates a text index on the data and graph structure representing pages crawled, while
                 simultaneously maintaining a graph in memory of pages and links between them for later use with the <a href="http://en.wikipedia.org/wiki/PageRank">PageRank</a>
                 algorithm. On the most recent demo with nearly 200,000 pages in the index, results were retrieved and reranked with PageRank
                 in roughly 1/2 second.<br/>
            </p>

            <h4><br/>Web Interface</h4>
            <p>
                Thi GUI is built on the simple Python web framework <a href="http://www.cherrypy.org/">CherryPy</a>, and simply launches the
                 crawler and indexer in the background. The interface mimics Google's very closely, searches the illinois.edu domain, and
                 even provides statistical and technical information about the backend of the application.
            </p>
            ''',
        sourceUrl = '/navigate_source/PyGoogle,PyGoogle'
    ),

    Project(
        name = 'collaborativeMusicFilter',
        title = 'Collaborative Music Filter',
        active = False,
        shortDescription = '''
            <p>
                As part of a semester project and a contribution to the open source ACM music player
                <a href="https://github.com/avuserow/amp/wiki/">Acoustics</a>, several classmates
                and myself developed a Java based music filter extension
                that searches for songs that best fit musical interests of groups of users. This project
                uses the open source toolkit Lucene, the Last.fm API, and a variety of scoring algorithms,
                one making use of the Floyd-Warshall shortest paths algorithm for graphs to compute a
                the barycenters of graphs.
            </p>
            ''',
        longDescription = '''
            <p>
                This extension will allow the ACM's collaborative music player to automatically
                select good songs to play based on users in the room at any given time.
            </p>
            <p>
               Our extension's implementation was broken into three primary components:<br/>
               <ul>
                    <li>
                        First, our application receives an socket-based request from Acoustics, which consists
                        of a set of users for which to find the set of tags that best represents the users'
                        common musical tastes.
                    </li><br/>
                   <li>
                       Using score-based and tag-based filtering mechanisms, find a set of tags that represents
                       the common interests of the given users.
                   </li><br/>
                   <li>
                       Use this ordered set of tags as a query to a Lucene index, where documents are represented
                       as songs indexed by tags. Finally, return this set of songs to Acoustics as the final result.
                   </li>
               </ul>
            </p>
            <p>
                Below, we show a full diagram of the architecture of our system:
                <center><img style="padding:20px; margin-left:-10px;" width=500 src="/static/admin/images/projects/collaborative_music_filter_architecture.png"/></center>

                The four most important components of this architecture we specifically:<br/><br/>
                <ul>
                    <li>Last.fm crawler</li><br/>

                    Runs separately from the core Java code of our project, polls the Acoustics database for new songs,
                    albums, and artists, and fetches tag information from Last.fm<br/><br/>

                    <li>Project API</li><br/>

                    Provides a simple interface to our project from the core Acoustics code, and allows our project to be
                    run separately from Acoustics<br/><br/>

                    <li>Tag filter</li><br/>

                    Retrieves a ranked list of high-level musical tags that best represent the musical interests of a set of users<br/><br/>

                    <li>Song index</li><br/>

                    Uses this list of tags as a query to generate a pseudo-randomized playlist of songs to suggest to Acoustics<br/><br/>
                </ul>

                For a full explanation of our projects, take a look at our <a href="/static/admin/papers/collaborative_music_filter_paper.pdf">project writeup</a>.
            </p>
            ''',
        sourceUrl = '/navigate_source/CollaborativeMusicSelector,CollaborativeMusicSelector'
    ),
    
    Project(
        name = 'personalCloudServer',
        title = 'Personal Cloud Server',
        subtitle = 'My own private server',
        active = True,
        shortDescription = '''
            <p>
                Each of the sites mention here are hosted on my own <b>V</b>irtual <b>P</b>rivate <b>S</b>erver, which I additionally use
                for personal websites, proxies, over a dozen private <a href="http://git-scm.com/">Git</a> repositories,
                and other miscellaneous projects. I am constantly looking for new ways to put this too use,
                and have a learned a tremendous amount about security and server administration in the short
                time that I have owned it.
            </p>
            '''
    ),

    Project(
        name = 'professionalWebsite',
        title = 'This Website',
        subtitle = 'Professional website and blog',
        active = True,
        shortDescription = '''
            <p>
                My professional website, which I regularly maintain to keep my projects, ideas, and r&eacute;sum&eacute; up to date. This
                site is built on <a href="http://www.cherrypy.org/">CherryPy</a>, a simple Python web framework with which I have had some
                experience in the past, and will soon feature full demos of projects and a fully functioning python-driven custom search
                engine, powered by <a href="https://bitbucket.org/mchaput/whoosh/wiki/Home">Whoosh!</a>. Likewise, the Python server, to improve performance and reduce memory footprint, makes use of
                the new JIT, or <b>J</b>ust <b>I</b>n <b>T</b>ime compiler called <a href="http://pypy.org/">PyPy</a>.
            </p>
            ''',
        sourceUrl = 'http://www.github.com/jtedesco/ProfessionalWebsite',
        liveUrl = 'http://www.jontedesco.net/'
    ),

    Project(
        name = 'generationsWebsite',
        title = 'Generations Website',
        subtitle = 'Band website',
        active = False,
        shortDescription = '''
            <p>
                Homepage of Generations, my cover band from home, which I spent several weeks late 2010 designing and implemented,
                and now update regularly. The site features audio and video recordings, photo albums,
                and a catalog of events and locations.
            </p>
            ''',
        longDescription = '''
            <p>
                The site uses some <a href="http://www.facebook.com">Facebook</a>
                integration, and even a personalized mass email system that we regularly use to send our 500+ fans personalized
                emails with instant unsubscribe functionality. Likewise, this same email system is used to automatically generate welcome emails
                for new fans, and add them to our mailing list, and offers an administrative interface from which band members
                can initiate an eblast, or even test it in debug mode before sending.
            </p>
            <p>
                Written in purely in PHP, this was my first experience building a personal, database-driven website. It integrates
                with Google Analytics for analysis statistics and relies on a MySQL backend. The site has been tested and adapts to
                Firefox, Chrome, and IE.
            </p>
            ''',
        sourceUrl = '/navigate_source/GenerationsWebsite,GenerationsWebsite',
        liveUrl = 'http://www.generationschicago.com/'
    ),

    Project(
        name = 'sockit',
        title = 'SockIt',
        subtitle = 'An asynchronous, client-side Jsavascript networking plugin',
        active = False,
        shortDescription = '''
            <p>
                This summer, I spent the first month of my internship working on a project reminiscent
                of <a href="http://nodejs.org/">node.js</a>, a powerful asynchronous server-side Javascript
                networking library. While node.js allows possesses tremendous ability to perform
                efficient Javascript networking, the current browser security model inhibits similar
                client-side functionality, until SockIt.
            </p>
            ''',
        longDescription = '''
            <p>
                SockIt, in a nutshell, is an NPAPI browser plugin that circumvents the traditional browser
                security model, allowing pages to perform low-level networking via client-side javascript.
                While it is being actively developed, it already possesses extensive TCP and UDP functionality,
                and even boasts a custom implementation of the heavily-used <a href="http://en.wikipedia.org/wiki/WebSocket">WebSockets</a>
                protocol that supports batch callbacks.
            </p>
            <p>
                Similar to node.js, its networking is performed entirely asynchronously, and it supports
                variety of cross platform browsers. It is newly open sourced, and hosted on
                <a href="http://github.com/sockit/sockit">GitHub</a>, and extensive documentation and
                tutorials are available <a href="http://sockit.github.com/">here</a>.
            </p>
            ''',
        sourceUrl = 'http://github.com/sockit/sockit',
        liveUrl = 'http://sockit.github.com/'
    ),

    Project(
        name = 'marchingIlliniSaxWebsite',
        title = 'Marching Illini Sax Website',
        subtitle = 'Homepage of the Marching Illini Saxophone Section',
        active = True,
        shortDescription = '''
            <p>
                Recently, I volunteered to create a website for the
                <a href="http://bands.illinois.edu/content/MI/">Marching Illini</a> saxophone section
                and while it is still a work in progress, it is off to a good start, already boasting
                Facebook and Google Groups integration for our public facebook page and private discussion forum.
            </p>
            ''',
        liveUrl = 'http://marchingillinisaxes.com/'
    ),

    Project(
        name = 'freeBox',
        title = 'FreeBox',
        subtitle = 'A free rsync-based file synchronization tool',
        active = True,
        shortDescription = '''
            <p>
                While I have only just started working on this project, I have big ideas for this
                project, and I hope that takes shape quickly once I find sufficient time to work on it.
                Essentially, this project is to create a platform independent file synchronization tool,
                much like <a href="http://dropbox.com">Dropbox</a>, but one which will capture the power
                of rsync to efficiently transfer files and improve upon several of Dropbox's fundamental
                weak points, in my opinion.
            </p>
            ''',
        longDescription = '''
            <p>
                This system will be based on <a href="http://en.wikipedia.org/wiki/Rsync">rsync</a>,
                a robust and efficient file transfer tool. Ideally, it will be extremely configurable and allow for
                high-speed LAN synchronization. As a truly free alternative to Dropbox, I hope to
                add support for free, unlimited online storage by attempting to unify the plethora
                free data storage space available on the internet.
            </p>
            ''',
        sourceUrl = 'https://github.com/jtedesco/FreeBox'
    ),
    Project(
        name = 'removeDuplicateCodeInConstructorsRefactoring',
        title = 'Java Eclipse Constructor Refactoring',
        subtitle = 'An Eclipse Java refactoring to remove duplicate code from constructors',
        active = False,
        shortDescription = '''
            <p>
                For our course project for <a href='https://wiki.engr.illinois.edu/display/cs427fa11/CS+427+Fall+2011'>software engineering</a>,
                I worked with three classmates to develop a plugin for the Eclipse refactoring engine that automatically
                detects and removes duplicate code from class constructors. The project was intended to teach us how to practice XP,
                and was entirely pair-programmed, strictly adhering to the extreme programming practices.
            </p>
            ''',
        longDescription = '''
            <p>
                The refactoring is able to analyze any arbitrary types for constructors, detect duplicate code by tracing
                through the Java program AST, and even allows the user to customize the extraction of duplicate code found
                by the refactoring by introducing new helper methods or specifying access modifiers for methods or constuctors
                created by the refactoring. Likewise, it provides refactoring previews and analysis of program errors to
                the user via the Eclipse refactoring wizard interface.
            </p>
            <p>
                In addition to our <a href='https://github.com/jtedesco/RemoveDuplicateCodeFromConstructors'>source code</a>
                below, we have a <a href='https://github.com/jtedesco/RemoveDuplicateCodeFromConstructorsTests'>separate repository</a>
                containing the tests for our refactoring, providing a thorough test suite of successful and unsuccessful
                refactoring scenarios. Likewise, our full documentation, including sample use cases and a full set of
                features, can be found <a href='static/admin/papers/refactoring_project_documentation.pdf'>here</a>.
            </p>
        ''',
        sourceUrl = 'https://github.com/jtedesco/RemoveDuplicateCodeFromConstructors'
    ),
    Project(
        name = 'eduwrite',
        title = 'EduWrite',
        subtitle = 'Real-time collaborative note-taking',
        active = True,
        shortDescription = '''
            <p>
                Still under active development, EduWrite is a semester project for
                <a href='https://wiki.engr.illinois.edu/display/cs428sp12/Home'>Software Development II</a> that
                leverages the open source project <a href='https://github.com/Pita/etherpad-lite'>Etherpad Lite</a> to
                provide real-time note taking system for lecture-style classes.
            </p>
            <p>
                This project is built on <a href='http://nodejs.org/'>node.js</a>, using modules such as <a href=''>express</a>,
                <a href='https://github.com/visionmedia/mocha'>mocha</a>, <a href='https://github.com/mbrevoort/docco-husky'>docco-husky</a>,
                and <a href='https://github.com/Pita/ueberDB'>ueberDB</a>, as well as Twitter's
                <a href='http://twitter.github.com/bootstrap/'>Bootstrap</a> framework. It is currently hosted at a private
                Github repository, but will be open-sourced soon.
            </p>
        '''
    ),
    Project(
        name = 'nimbos',
        title = 'Nimbos',
        subtitle = 'A Hadoop cluster prediction framework',
        active = True,
        shortDescription = '''
            <p>
                Through our course project for the <a href='https://wiki.engr.illinois.edu/display/cs598rco/Home'>graduate cloud computing course</a>,
                I have been working with two fellow BS/MS students in the department to study how we can effectively predict machine
                failures in Hadoop clusters. We hope to use Hadoop logs to improve upon previous work done in other cloud
                computing systems by leveraging machine learning to predict failures, the type of failure expected, as well
                as time of failure expected.
            </p>
            ''',
        longDescription = '''
            <p>
                Thus far, we have recreated previous work that predicts hardware failures in cloud systems using SVMs and
                a sliding window view of logs. As detailed in our <a href='https://wiki.engr.illinois.edu/download/attachments/195766887/paper.pdf?version=3&modificationDate=1331931973141'>project proposal</a>,
                we can take a similar sliding window approach, but plan to investigate alternative views of log data, as well
                as alternative machine learning algorithms.
            </p>
            ''',
        sourceUrl = 'https://github.com/jtedesco/Nimbos'
    )
]

# Sync these projects with the backend database
for project in projects:
    try:
        project.save()
    except Exception:
        try:

            # Retrieve the project object already in the database
            existingProject = Project.objects.get(name=project.name)

            # Update all fields of this project
            existingProject.title = project.title
            existingProject.subtitle = project.subtitle
            existingProject.active = project.active
            existingProject.shortDescription = project.shortDescription
            existingProject.longDescription = project.longDescription
            existingProject.sourceUrl = project.sourceUrl
            existingProject.liveUrl = project.liveUrl
            existingProject.save()

        except Exception:
            print "Skipping '%s': %s" % (project.name, str(sys.exc_info()[1]))