package lucene;

import entity.Song;
import entity.SongDirectory;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.CorruptIndexException;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.queryParser.MultiFieldQueryParser;
import org.apache.lucene.queryParser.ParseException;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopScoreDocCollector;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.LockObtainFailedException;
import org.apache.lucene.store.RAMDirectory;
import org.apache.lucene.util.Version;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class Lucene {
	

	private final Directory theLuceneDirectory;
	private final Analyzer theAnalyzer;

    private static final int MAX_PRELIMINARY_RESULTS = 200;
	
	public Lucene(SongDirectory aSongDirectory) throws CorruptIndexException, LockObtainFailedException, IOException {
		this.theLuceneDirectory = new RAMDirectory();
		this.theAnalyzer =  new StandardAnalyzer(Version.LUCENE_CURRENT);
		buildIndex(aSongDirectory);
	}
	
	public Directory getLuceneIndex() {
		return this.theLuceneDirectory;
	}
	
	public Analyzer getAnalyzer() {
		return this.theAnalyzer;
	}
	
	private void buildIndex(SongDirectory aSongDirectory) throws CorruptIndexException, LockObtainFailedException, IOException {
		IndexWriter w = new IndexWriter(getLuceneIndex(), getAnalyzer(), true, IndexWriter.MaxFieldLength.UNLIMITED);
		
		LuceneBuilder myLuceneBuilder = new LuceneBuilder(w);
		for(Song mySong : aSongDirectory.getSongs()) {
			myLuceneBuilder.addSong(mySong);
			//System.out.println(mySong);
		}
		w.close();
	}
	
	public String runQuery(String aQuery, int maxResults) throws ParseException, CorruptIndexException, IOException {
		String inputQuery = aQuery;

		// NOTE: We never validate the user's input query -- could be bad
		String querystr = inputQuery.length() > 0 ? inputQuery : "lucene";
		
		System.out.println(querystr);
		
		// the "title" arg specifies the default field to use
		// when no field is explicitly specified in the query.

		String[] myFieldsToParse = {"song_tag"};
		Query q = new MultiFieldQueryParser(Version.LUCENE_CURRENT, myFieldsToParse, getAnalyzer()).parse(querystr);
		// 3. search
		int hitsPerPage = MAX_PRELIMINARY_RESULTS;
		boolean isReadOnly = true;
		IndexSearcher searcher = new IndexSearcher(getLuceneIndex(), isReadOnly);
		
		
		TopScoreDocCollector collector = TopScoreDocCollector.create(hitsPerPage, true);
		searcher.search(q, collector);
		ScoreDoc[] hits = collector.topDocs().scoreDocs;

		// 4. display results
		
		List<String> myResults = new ArrayList<String>();
		System.out.println("Found " + hits.length + " hits.");

		int j = 0;
		for (int i = 0; i<hits.length && j < MAX_PRELIMINARY_RESULTS; ++i) {
			if (hits.length == 0){
				return "No Results Dumbass";
			}
			int docId = hits[i].doc;
			Document d = searcher.doc(docId);
			
			String key = d.get("artist").trim().toLowerCase();
	
            myResults.add(d.get("songID") + ": " + d.get("title") + " - "+ d.get("artist")  +  "\n");
            j++;
		}

		// searcher can only be closed when there
		// is no need to access the documents any more.
		searcher.close();

        // Shuffle the list of result strings randomly, using current time as a random seed
        Collections.shuffle(myResults, new Random(System.currentTimeMillis()));

		String retv = "";
        for (int i = 0; i < maxResults && i < myResults.size(); i++){
            retv += (i+1) + ". " + myResults.get(i);
        }
		return retv;
	}
	

}
