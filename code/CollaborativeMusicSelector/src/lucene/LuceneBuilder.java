package lucene;
import java.io.IOException;
import java.util.Collection;

import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.index.CorruptIndexException;
import org.apache.lucene.index.IndexWriter;

import entity.Song;

public class LuceneBuilder {
	
	private final IndexWriter theIndexWriter;
	
	public LuceneBuilder(IndexWriter aIndexWriter) {
		theIndexWriter = aIndexWriter;
	}
	
	public IndexWriter getIndexWriter() {
		return theIndexWriter;
	}
	
	public void addSong(Song aSong) {
		Document myDoc = new Document();
		myDoc.add(new Field("artist", aSong.getArtist(), Field.Store.YES, Field.Index.ANALYZED));
		myDoc.add(new Field("album", aSong.getAlbum(), Field.Store.YES, Field.Index.ANALYZED));
		myDoc.add(new Field("title", aSong.getTitle(), Field.Store.YES, Field.Index.ANALYZED));
		myDoc.add(new Field("songID", ""+aSong.getSongID(), Field.Store.YES, Field.Index.ANALYZED));
		Collection<String> mySongTags = aSong.getSongTags();
		
		for(String mySongTag : mySongTags) {
			myDoc.add(new Field("song_tag", mySongTag, Field.Store.YES, Field.Index.ANALYZED));
		}
		
		Collection<String> myArtistTags = aSong.getArtistTags();
		for(String myArtistTag : myArtistTags) {
			myDoc.add(new Field("artist_tag", myArtistTag, Field.Store.YES, Field.Index.ANALYZED));
		}
		
		Collection<String> myAlbumTags = aSong.getAlbumTags();
		for(String myAlbumTag : myAlbumTags) {
			myDoc.add(new Field("album_tag", myAlbumTag, Field.Store.YES, Field.Index.ANALYZED));
		}
		
		try {
			theIndexWriter.addDocument(myDoc);
		} catch (CorruptIndexException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}

}
