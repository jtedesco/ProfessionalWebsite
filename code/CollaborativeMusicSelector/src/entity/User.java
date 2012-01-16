package entity;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import filter.graph.TagGraphVertex;

/**
 * Created by IntelliJ IDEA. User: Jon Date: 4/14/11 Time: 4:30 PM
 * 
 * Represents a user of Acoustics, and their musical interests as collected from
 * Acoustics.
 */
public class User extends TagGraphVertex {

	/**
	 * The (immutable) netId of this user
	 */
	private final String netId;

	/**
	 * The (immutable) songCount of this user
	 */
	private final int songCount;
	
	/**
	 * The (immutable) set of tags associated with songs this user likes
	 */
	private final Set<String> theSongTags;

	private Map<String, Integer> theTagWeights;

	/**
	 * The (immutable) set of tags associated with artists this user likes
	 */
	private final Set<String> theArtistTags;

	/**
	 * The (immutable) set of tags associated with albums this user likes
	 */
	private final Set<String> theAlbumTags;

	public User(String netId, Set<Song> songs, int count) {
		this.netId = netId;
		theSongTags = new HashSet<String>();
		theArtistTags = new HashSet<String>();
		theAlbumTags = new HashSet<String>();
		theTagWeights = new HashMap<String, Integer>();
		addTags(songs);
		
		songCount=count;
		
		addSongTags(songs);
	}


	private void addTags(Set<Song> songs) {
		for (Song mySong : songs) {
			for (String myTag : mySong.getSongTags()) {
				String myCleanedTag = myTag.trim().toLowerCase();

				Integer myWeight = theTagWeights.get(myCleanedTag);
				if (myWeight != null) {
					theTagWeights.put(myCleanedTag, ++myWeight);
				} else {
					theTagWeights.put(myCleanedTag, 1);
				}

			}
		}
	}

	public int getSongCount(){
		return songCount;
	}
	
	
	public String getID() {
		return netId;
	}

	public String getNetId() {
		return netId;
	}

	public Integer getWeightForTag(String aTag) {
		return theTagWeights.get(aTag);
	}

	public Map<String, Integer> getTagWeights() {
		return theTagWeights;
	}

	public Set<String> getSongTags() {
		return theSongTags;
	}

	public Set<String> getArtistTags() {
		return theArtistTags;
	}

	public Set<String> getAlbumTags() {
		return theAlbumTags;
	}

	/**
	 * Adds a set of song-associated set of tags to this user's liked tags from
	 * a set of songs.
	 * 
	 * @param songs
	 *            The songs containing these tags
	 */
	private void addSongTags(Set<Song> songs) {
		for (Song mySong : songs) {
			if (mySong != null) {
				getSongTags().addAll(mySong.getSongTags());
			}
		}
	}

	/**
	 * Adds a set of album-associated set of tags to this user's liked tags from
	 * a set of songs.
	 * 
	 * @param songs
	 *            The songs containing these tags
	 */
	private void addAlbumTags(Set<Song> songs) {
		for (Song mySong : songs) {
			getSongTags().addAll(mySong.getAlbumTags());
		}
	}

	/**
	 * Adds a set of artist-associated set of tags to this user's liked tags
	 * from a set of songs.
	 * 
	 * @param songs
	 *            The songs containing these tags
	 */
	private void addArtistTags(Set<Song> songs) {
		for (Song mySong : songs) {
			getSongTags().addAll(mySong.getArtistTags());
		}
	}

}
