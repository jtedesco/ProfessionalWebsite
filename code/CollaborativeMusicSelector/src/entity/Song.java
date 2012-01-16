package entity;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;

/**
 * Represents a song with album, artist, and song-specific tags from Acoustics. For good practice, all fields are final.
 */
public class Song {

    /**
     * Tags specific to this song
     */
	private final Collection<String> theSongTags;

    /**
     * Tags specific to this artist
     */
	private final Collection<String> theArtistTags;

    /**
     * Tags specific to this album
     */
	private final Collection<String> theAlbumTags;

    /**
     * The title of the song
     */
	private final String theTitle;

    /**
     * The artist of this song
     */
	private final String theArtist;

    /**
     * The album
     */
	private final String theAlbum;

	/**
     * The songID
     */
	private final int theSongID;

	
    /**
     * Build a song object. Once this object is initialized, it is immutable.
     *
     * @param aSongTags     The tags specific to this song
     * @param aArtistTags   The tags specific to this song's associated artist
     * @param aAlbumTags    The tags specific to this song's asasociated album
     * @param aTitle        This song's title
     * @param aArtist       This song's artist
     * @param aAlbum        This song's album
     */
	public Song(Collection<String> aSongTags, Collection<String> aArtistTags, Collection<String> aAlbumTags, String aTitle, String aArtist, String aAlbum, int songID) {
		theTitle = aTitle;
		theAlbum = aAlbum;
		theArtist = aArtist;
		theSongTags = aSongTags;
		theArtistTags = aArtistTags;
		theAlbumTags = aAlbumTags;
		theSongID = songID; 
	}


    /**
     * Build a song object. Once this object is initialized, it is immutable.
     *
     * @param aSongTags     The tags specific to this song, as a comma separated string
     * @param aArtistTags   The tags specific to this song's associated artist, as a comma separated string
     * @param aAlbumTags    The tags specific to this song's asasociated album, as a comma separated string
     * @param aTitle        This song's title
     * @param aArtist       This song's artist
     * @param aAlbum        This song's album
     */
    public Song(String aSongTags, String aArtistTags, String aAlbumTags, String aTitle, String aArtist, String aAlbum, int aSongID) {
		theTitle = aTitle;
		theSongTags = convertTagStringToCollection(aSongTags);
		theArtistTags = convertTagStringToCollection(aArtistTags);
		theAlbumTags = convertTagStringToCollection(aAlbumTags);
		theAlbum = aAlbum;
		theArtist = aArtist;
		theSongID = aSongID;
	}

    /**
     * Builds a set of tags from comma separated string containing the tags
     *
     * @param aSongTags     The string representation of the tags
     * @return              The collection of tags
     */
	private Collection<String> convertTagStringToCollection(String aSongTags) {
		if(aSongTags == null) {
			return new ArrayList<String>();
		}
		String[] myTags = aSongTags.split(", ");
		return Arrays.asList(myTags);
	}

	public boolean hasSongTag(String aTag) {
		return getSongTags().contains(aTag);
	}
	
	public boolean hasArtistTag(String aTag) {
		return getArtistTags().contains(aTag);
	}
	
	public boolean hasAlbumTag(String aTag) {
		return getAlbumTags().contains(aTag);
	}

	public Collection<String> getArtistTags() {
		return theArtistTags;
	}

	public Collection<String> getSongTags() {
		return theSongTags;
	}
	
	public Collection<String> getAlbumTags() {
		return theAlbumTags;
	}

	public String getTitle() {
		return theTitle;
	}

	public String getArtist() {
		return theArtist;
	}

	public int getSongID(){
		return theSongID;
	}

	public String getAlbum() {
		return theAlbum;
	}

    /**
     * Prints out a nice representation of this song.
     */
	@Override
	public String toString() {
		return  "Title: " + getTitle() + "\n" +
                "Artist: " + getArtist() + "\n" +
                "Album: " + getAlbum() + "\n" +
                "Tags: " + getSongTags() + "\n";
	}
}
