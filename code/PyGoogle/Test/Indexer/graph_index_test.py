"""
  This file tests the graph index
"""
import unittest
from Indexer.graph_index import GraphIndex
from Lib.pygraph.classes.digraph import digraph
from mockito import *
from Indexer.web_page import WebPage
from Common.search_result import SearchResult

__author__ = 'Jon Tedesco'

class GraphIndexTest(unittest.TestCase):
    """
      This class contains tests for the graph index of the search engine
    """

    def test_init(self):
        """
          Tests the constructor for the graph index
        """

        # Build a graph index
        graph_index = GraphIndex(False)

        # Assert that the fields are created correctly
        self.assertTrue(isinstance(graph_index.graph, digraph))
        self.assertFalse(graph_index.verbose)


    def test_insert(self):
        """
          Tests inserting a page into the index
        """

        # Build a graph index and mock out the graph object
        graph_index = GraphIndex(False)
        graph_index.graph = mock()

        # Build a sample page to insert into the graph
        url = "www.illinois.edu"
        link = "www.illinois.edu/chief!"
        page = WebPage(url, "University of Illinois at Urbana-Champaign", "content", ["illinois", "chief"], [link])

        # Stub out the behavior for the mock graph
        when(graph_index.graph).has_node(url).thenReturn(False)
        when(graph_index.graph).has_node(link).thenReturn(False)

        # Try to insert this page
        result = graph_index.insert(page)

        # Verify that the correct behavior was followed
        inorder.verify(graph_index.graph).has_node(url)
        inorder.verify(graph_index.graph).add_node(url, [page])
        inorder.verify(graph_index.graph).has_node(link)
        inorder.verify(graph_index.graph).add_node(link, [])
        inorder.verify(graph_index.graph).has_edge((url, link))
        inorder.verify(graph_index.graph).add_edge((url, link))
        self.assertTrue(result)


    def test_rerank(self):
        """
          Tests reranking a set of pages using pagerank
        """

        # Create the graph index
        graph_index = GraphIndex(True)

        # Create some dummy pages from wikipedia
        page_1 = WebPage("http://en.wikipedia.org/wiki/Snake", "Snake", u"""Snakes are elongate, legless, carnivorous reptiles of the suborder Serpentes that can be distinguished from legless lizards by their lack of eyelids and external ears. Like all squamates, snakes are ectothermic, amniote vertebrates covered in overlapping scales. Many species of snakes have skulls with many more joints than their lizard ancestors, enabling them to swallow prey much larger than their heads with their highly mobile jaws. To accommodate their narrow bodies, snakes' paired organs (such as kidneys) appear one in front of the other instead of side by side, and most have only one functional lung. Some species retain a pelvic girdle with a pair of vestigial claws on either side of the cloaca.
        Living snakes are found on every continent except Antarctica and on most islands. Fifteen families are currently recognized, comprising 456 genera and over 2,900 species.[1][2] They range in size from the tiny, 10 cm-long thread snake to pythons and anacondas of up to 7.6 metres (Template:Rnd/c4dec0 |25|(0)}} ft) in length. The recently discovered fossil Titanoboa was 15 metres (Template:Rnd/c4dec0 |49|(-0)}} ft) long. Snakes are thought to have evolved from either burrowing or aquatic lizards during the Cretaceous period (c 150 Ma). The diversity of modern snakes appeared during the Paleocene period (c 66 to 56 Ma).
        Most species are nonvenomous and those that have venom use it primarily to kill and subdue prey rather than for self-defense. Some possess venom potent enough to cause painful injury or death to humans. Nonvenomous snakes either swallow prey alive or kill by constriction.""", ["snake", "animal", "wikipedia"], ["http://en.wikipedia.org/wiki/Leopard"])
        page_2 = WebPage("http://en.wikipedia.org/wiki/Leopard", "Leopard", u"""The leopard (pronounced /?l?p?rd/), Panthera pardus, is a member of the Felidae family and the smallest of the four "big cats" in the genus Panthera, the other three being the tiger, lion and jaguar. Once distributed across eastern and southern Asia and Africa, from Siberia to South Africa, the leopard's range of distribution has decreased radically because of hunting and loss of habitat. It is now chiefly found in sub-Saharan Africa; there are also fragmented populations in Pakistan, India, Sri Lanka, Indochina, Malaysia, and China. Because of its declining range and population, it is listed as a "Near Threatened" species by the IUCN.[2]
        Compared to other members of the Felidae family, the leopard has relatively short legs and a long body with a large skull. It is similar in appearance to the jaguar, but is smaller and more slightly built. Its fur is marked with rosettes similar to those of the jaguar, but the leopard's rosettes are smaller and more densely packed, and do not usually have central spots as the jaguars do. Both leopards and jaguars that are melanistic (completely black or very dark) are known as black panthers.
        The species' success in the wild is in part due to its opportunistic hunting behavior, its adaptability to habitats, its ability to run at speeds approaching 58 kilometres per hour (36 mph), its unequaled ability to climb trees even when carrying a heavy carcass,[3] and its notorious ability for stealth. The leopard consumes virtually any animal it can hunt down and catch. Its habitat ranges from rainforest to desert terrains.""", ["leopard", "animal", "wikipedia"], [])
        page_3 = WebPage("http://en.wikipedia.org/wiki/Mac_OS_X_Leopard", "Mac OS X Leopard", u"""Security enhancements
        New security features intend to provide better internal resiliency to successful attacks, in addition to preventing attacks from being successful in the first place.
        Library Randomization
        Leopard implements library randomization[45], which randomizes the locations of some libraries in memory. Vulnerabilities that corrupt program memory often rely on known addresses for these library routines, which allow injected code to launch processes or change files. Library randomization is presumably a stepping-stone to a more complete implementation of address space layout randomization at a later date.
        Application Layer Firewall
        Leopard ships with two firewall engines: the original BSD IPFW, which was present in earlier releases of Mac OS X, and the new Leopard Application Layer Firewall. Unlike IPFW, which intercepts and filters IP datagrams before the kernel performs significant processing, the Application Layer Firewall operates at the socket layer, bound to individual processes. The Application Layer Firewall can therefore make filtering decisions on a per-application basis. Of the two-firewall engines, only the Application Layer Firewall is fully exposed in the Leopard user interface. The new firewall offers less control over individual packet decisions (users can decide to allow or deny connections system wide or to individual applications, but must use IPFW to set fine-grained TCP/IP header level policies). It also makes several policy exceptions for system processes: neither mDNSResponder nor programs running with superuser privileges are filtered.[46]
        Sandboxes
        Leopard includes kernel-level support for role-based access control (RBAC). RBAC is intended to prevent, for example, an application like Mail from editing the password database.
        Application Signing
        Leopard provides a framework to use public key signatures for code signing to verify, in some circumstances, that code has not been tampered with. Signatures can also be used to ensure that one program replacing another is truly an "update", and carry any special security privileges across to the new version. This reduces the number of user security prompts, and the likelihood of the user being trained to simply clicking "OK" to everything.
        Secure Guest Account
        Guests can be given access to a Leopard system with an account that the system erases and resets at logout.[47]
        Security features in Leopard have been criticized as weak or ineffective, with the publisher Heise Security documenting that the Leopard installer downgraded firewall protection and exposed services to attack even when the firewall was re-enabled. Several researchers noted that the Library Randomization feature added to Leopard was ineffective compared to mature implementations on other platforms, and that the new "secure Guest account" could be abused by Guests to retain access to the system even after the Leopard log out process erased their home director""", ["leopard", "mac", "wikipedia"], ["http://en.wikipedia.org/wiki/Leopard"])

        # Insert the pages into the graph
        graph_index.insert(page_1)
        graph_index.insert(page_3)
        graph_index.insert(page_2)

        # Create a search result and rerank it
        page_list = [page_1, page_2, page_3]
        search_result = SearchResult(page_list, 5, ["wikipedia"])
        reranked_search_result = graph_index.rerank(search_result, 2)

        # Assert that the search result is as we expect
        self.assertEquals(search_result.pages[0], page_2)