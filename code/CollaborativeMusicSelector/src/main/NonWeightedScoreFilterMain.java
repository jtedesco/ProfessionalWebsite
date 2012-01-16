package main;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

import lucene.Lucene;

import org.apache.lucene.index.CorruptIndexException;
import org.apache.lucene.queryParser.ParseException;
import org.apache.lucene.store.LockObtainFailedException;

import entity.SongDirectory;
import filter.TagFilter;
import filter.score.ScoreFilter;

public class NonWeightedScoreFilterMain {

	public static void main(String[] args) throws CorruptIndexException,
			LockObtainFailedException, IOException, ParseException {

		// Time how long it takes to get all of the song data from the database
		long starttime = System.currentTimeMillis();
		final SongDirectory myDirectory = new SongDirectory(50000);
		System.out.println(myDirectory.getSongs().size());
		long enddirectory = System.currentTimeMillis();
		System.out.println("Song Directory Build Time: "
				+ (enddirectory - starttime) + "ms");
		System.out.println("READY TO QUERY");

		// Find the tags that best represent the shared interests of this group
		// of users
		TagFilter myFilter = new ScoreFilter(myDirectory, 25);

		Scanner myScanner = new Scanner(System.in);

		while (true) {
			String line = myScanner.nextLine();

			List<String> tags = myFilter.findBestRepresentativeTagsNonWeighted(Arrays
					.asList(line.split(" ")));

			if (tags == null) {
				tags = Arrays.asList("beatles", "smash mouth");
			}
			Lucene lucene = new Lucene(myDirectory);
			StringBuilder query = new StringBuilder();
			for (String tag : tags) {
				query.append(tag + " ");
			}

			System.out.println(lucene.runQuery(query.toString(), 15));

			System.out.println("=====================");
		}

	}

}
