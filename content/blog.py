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
        content = """
            <p>
                This summer, I spent the first month of my internship working on a project reminiscent
                of <a href="http://nodejs.org/">node.js</a>, a powerful asynchronous server-side Javascript
                networking library. While node.js allows possesses tremendous ability to perform
                efficient Javascript networking, the current browser security model inhibits similar
                client-side functionality, until SockIt.
            </p>
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
            """
    ),
    Post(
        name = 'automatedUbuntuBackupAndRestore',
        title = 'Automated Ubuntu Backup and Restore',
        subtitle = 'Backup your Ubuntu system safely, efficiently, and effortlessly',
        timestamp = datetime.strptime('09/01/2011', '%m/%d/%Y'),
        content = """
            <p>
                Recently, I discovered that on Ubuntu, or any Debian-based Linux operating system, you can backup
                and restore and restore all packages, independent of the system or architectures itself.
                In fact, I sought out this solution because I wanted to switch to 64-bit Ubuntu from 32-bit,
                and didn't want to manually restore my programs and settings.
            </p>
            <p>
                To backup all installed applications, we first backup <code>sources.list</code> and
                the list of installed packages:<br/>

                <div style="margin-left: 30px;">
                    <pre class="brush: plain">
                        sudo cp /etc/apt/sources.list ./
                        dpkg --get-selections > installed-software
                    </pre><br/>
                </div>

            <p>
                To restore these packages on a clean build, we simply need to perform the following:
            </p>

            <div style="margin-left: 30px;">
                <pre class="brush: plain">
                    sudo cat files/sources.list > /etc/apt/sources.list
                    sudo apt-get update
                    sudo apt-get install dselect
                    sudo dpkg --set-selections < installed-software
                    sudo dselect
                </pre><br/>
            </div>

            <p>
                I modified my own script to automatically install third-party applications that don't
                include PPA's for for Ubuntu, such as <a href="http://www.jetbrains.com/idea/">IntelliJ IDEA</a> and
                the <a href="http://developer.android.com/sdk/index.html">Android SDK</a>, and perform
                other automated configuration, such as configuring Java alternatives, and setting the default
                shell to <a href="http://www.zsh.org/">Zsh</a>.
            </p>
            <p>
                The true benefit of using this method is that backups can be used for different Ubuntu architectures
                and versions, and backups require minimal space.
            </p>
            """
    ),
    Post(
        name = 'personalPasswordSecurity',
        title = 'Secure Your Digital Life',
        subtitle = 'Lock down your digital life without sacrificing personal convenience',
        timestamp = datetime.strptime('02/08/2012', '%m/%d/%Y'),
        content = """
            <h5>Cloud Data</h5>
            <p>
                This semester, I have been inspired by my Cloud Computing class to take a hard look at how cloud services
                have changed my digital life, particularly with regards to security.
            </p>
            <p>
                The plethora of services offered by companies like Google, Facebook, Dropbox, Microsoft, and countless others
                have changed the world. The geekiest (or perhaps most foolhardy) of us have taken a headfirst dive into
                this services, embracing the unprecedented convenience, namely in terms of reliability and availability,
                that these services provide.
            </p>
            <p>
                With these remarkable services, and so many like-minded people nonchalantly embracing them, it's easy to
                forget to really consider security, particularly when a certain level of trust is associated with these
                companies. While these services provide reasonable protection as-is, take a moment to think about the
                hell that would ensue if a malicious user were able to hack your primary Google email account, or change
                the password to your Facebook account.
            </p>
            <p>
                In general larger banks like Chase and Bank of America that I've used are tight in terms of online security,
                so I skip these sites in my discussion. In my own life, I've noticed I tend towards two extremes when it comes to my cloud data:
                <ul>
                    <li>
                        Primary accounts
                            <br/><br/>
                        A few key accounts like Google, Facebook, or Dropbox, where a tremendous amount of data is stored.
                        If someone were to access this accounts, it would be disastrous.
                    </li>
                    <br/>
                    <li>
                        Secondary accounts
                            <br/><br/>
                        Extremely common, and specialized accounts, such as Ebay, Amazon, and countless others
                        for which I undoubtedly registered and promptly forgot.
                    </li>
                </ul>
                Based on the nature of these two types of cloud accounts, I've devised what seems to be a reasonably convenient
                way of improving my digital security in my own life.
            </p>

            <h5>Primary Accounts</h5>
            <p>
                Many of these providers, in particular Google and Facebook that I will mention here, allow users to upgrade
                to two-step sign-in. Even if you frequent public computers or find yourself constantly on the go, you may
                be able to enable these services without inconveniencing yourself too much.
            </p>
            <p>
                For instance, if you Google allows
                <a href="http://lifehacker.com/5756977/set-up-googles-two+step-verification-now-for-seriously-enhanced-security-for-your-google-account">two-step verification</a> which
                few users seem to take advantage of. Using this service from home requires a one-time setup, and
                for Android users, <a href="https://market.android.com/details?id=com.google.android.apps.authenticator&hl=en">Google Authenticator</a>
                allows you to access your account from a public computer in seconds, and even provides text message support
                for the flip phone fanatics. In fact, if you lose your phone <i>and</i>
                only have access to public computers, it's still possible to recover your account by taking advantage
                of <a href="http://support.google.com/accounts/bin/answer.py?hl=en&answer=1187538">Google's backup code system</a>.
            </p>
            <p>
                In my opinion, this tremendously increases the security of you Google account, and can be done with relatively
                little trouble if you have a cell phone -- surprisingly under advertised in my opinion.
            </p>
            <p>
                Facebook's two step verification system is less complex, and only relies on have a phone with text messaging
                support, but likewise prompts for an extra verification step on the first use from an unrecognized device.
                I've never had any trouble accessing my account, but this nonetheless adds an extra layer of security around
                your account, and considering the time many of us spend on the site, poses a reasonable investment.
            </p>

            <h5>Secondary accounts</h5>
            <p>
                For these accounts, I think it's best to create a pattern that lets you create passwords based on the service's name --
                your own, secret algorithm, given the name of the site. This could be as simple as changing certain letters to numbers
                and adding a numerical sequence, or involve language translations, but nonetheless allows you generate
                different passwords for each site, in a way that you can guess if you forget.
            </p>
            <p>
                However, even with this system, it's often nice to have a fallback for when you inevitably forget a password.
                Using <a href="http://keepass.info/">KeePass</a>, a cross-platform password management application for
                Windows, Linux, Mac, Android, iOS, and more, this process can be easy. I use this service
                <a href="http://lifehacker.com/5063176/how-to-use-dropbox-as-the-ultimate-password-syncer">in combination with Dropbox</a>
                to securely store my passwords so that they are accessible anywhere.
            </p>

            <h5>Separation of Concerns</h5>
            <p>
                In order for this system to be effective, it requires us to take a look at our accounts be cautious of how we link
                them together. In particular, 'secondary accounts' should really have minimal personal data associated with the account.
                In most cases, I defer all payments to Paypal or Google Shopper, storing only a name, address, and my shopping history
                on each online shopping site that I use.
            </p>
            <br/>
            <p>
                I hope that this is as useful for someone else as it was for me -- let's cautiously embrace the new age of cloud services!
            </p>
            """
    ),
    Post(
        name = 'pageRankFetching',
        title = 'Python PageRank Fetching',
        subtitle = 'How to programmatically fetch the PageRank of any URL with Python',
        timestamp = datetime.strptime('03/19/2012', '%m/%d/%Y'),
        content = """
            <h5>Origin and Usage</h5>
            <p>
                I first came up with this code, adopted from <a href='http://www.djangosnippets.org/snippets/221/'>Corey Goldberg</a>,
                during my research for my senior thesis, studying entity search on the web. I adapted it slightly for my
                own use, but credit for this code goes to its original author. Although Google does not explicitly provide
                a utility for fetching the PageRank of a website, using the <a href='http://toolbar.google.com/'>Google toolbar</a>,
                we can programmatically fetch what I assume to be the <i>approximate</i> PageRank of any URL, on a scale
                from 0 to 10, the least to most reputable.
            </p>
            <h5>Code</h5>
            <p>
                Below, you can see the code required to fetch the PageRank of the URL. At the top-level, all the work
                can be abstracted into a single function call:
                <div style="margin-left: 0px;">
                    <pre class="brush: python">
def getPageRank(url):
    hashedUrl = checkHash(hashUrl(url))
    url = 'http://toolbarqueries.google.com/tbr' &#92;
            + '?client=navclient-auto&features=Rank' &#92;
            + '&ch=%s&q=info:%s' % (hashedUrl, urllib.quote(url))
    try:
        rank = int((urllib.urlopen(url)).read().strip()[9:])
    except Exception:
        rank = None
    return rank
                    </pre><br/>
                </div>
                Albeit this may not be the most elegant code on earth, but it gets the job done, handling error cases by
                simply assigning <code>None</code> in the case that:
                <ul><br/>
                    <li>The PageRank could not be found (infrequent in practice)<br/></li><br/>
                    <li>Your IP address has been rate limited by Google<br/></li><br/>
                    <li>Miscellaneous errors, such as URL not found<br/></li>
                </ul>
                These error cases can (and should) be handled individually, but for the sake of conciseness, this is omitted.
            </p>
            <p>
                The helper functions used for hashing the URL to make use of the PageRank feature are shown below:
                <div style="margin-left: 0px;">
                    <pre class="brush: python">
def intStr(string, integer, factor):
    for i in range(len(string)) :
        integer *= factor
        integer &= 0xFFFFFFFF
        integer += ord(string[i])
    return integer


def hashUrl(string):
    c1 = intStr(string, 0x1505, 0x21)
    c2 = intStr(string, 0, 0x1003F)

    c1 >>= 2
    c1 = ((c1 >> 4) & 0x3FFFFC0) | (c1 & 0x3F)
    c1 = ((c1 >> 4) & 0x3FFC00) | (c1 & 0x3FF)
    c1 = ((c1 >> 4) & 0x3C000) | (c1 & 0x3FFF)

    t1 = (c1 & 0x3C0) << 4
    t1 |= c1 & 0x3C
    t1 = (t1 << 2) | (c2 & 0xF0F)

    t2 = (c1 & 0xFFFFC000) << 4
    t2 |= c1 & 0x3C00
    t2 = (t2 << 0xA) | (c2 & 0xF0F0000)

    return t1 | t2


def checkHash(hashInt):
    hash_str = '%u' % hashInt
    flag = 0
    checkByte = 0

    i = len(hash_str) - 1
    while i >= 0:
        byte = int(hash_str[i])
        if 1 == (flag % 2):
            byte *= 2
            byte = byte / 10 + byte % 10
        checkByte += byte
        flag += 1
        i -= 1

    checkByte %= 10
    if 0 != checkByte:
        checkByte = 10 - checkByte
        if 1 == flag % 2:
            if 1 == checkByte % 2:
                checkByte += 9
            checkByte >>= 1

    return '7' + str(checkByte) + hash_str
                    </pre><br/>
                </div>
                If you do choose to use this code, please respect the terms of service laid out by Google, so that this feature
                remains available for free use.
            </p>
            """
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
            existingProject.content = post.content
            existingProject.save()

        except Exception:
            print "Skipping '%s': %s" % (post.name, str(sys.exc_info()[1]))