package main;

import entity.SongDirectory;
import filter.TagFilter;
import filter.graph.TagGraphFilter;

import java.util.ArrayList;
import java.util.List;

public class GraphFilterMain {

	public static void main(String[] args) {

        // Time how long it takes to get all of the song data from the database
		long starttime = System.currentTimeMillis();
		final SongDirectory myDirectory = new SongDirectory(50000);
		long enddirectory = System.currentTimeMillis();
		System.out.println("Song Directory Build Time: " + (enddirectory - starttime) + "ms");
		System.out.println("READY TO QUERY");

        // Build a list of users to query
        List<String> netIds = new ArrayList<String>();
        netIds.add("gladden1");
        netIds.add("bnooka2"); // Who is this?
        netIds.add("akreher2");
        netIds.add("rotap2"); // Who is this?

        // Find the tags that best represent the shared interests of this group of users
        TagFilter myFilter = new TagGraphFilter(myDirectory, 10.0);
        List<String> bestTags = myFilter.findBestRepresentativeTags(netIds);
	}
}
