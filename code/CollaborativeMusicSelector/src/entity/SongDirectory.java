package entity;

import java.util.Collection;
import java.util.Map;

/**
 * Stores all tagged song data from Acoustics.
 */
public class SongDirectory {

    /**
     * The songs from the database
     */
	private static Map<Integer, Song> theSongs;

    /**
     * Build this song directory using at most <code>aLimit</code> songs.
     *
     * @param aLimit    The maximum number of songs to grab from Acoustics
     */
	public SongDirectory(int aLimit) {
		theSongs = SongDAO.retrieveSongs(aLimit);
	}

    /**
     * Retrieve a song from our <code>Map</code> by its id
     *
     * @param id    The id of the song we want to grab
     * @return      A tagged song
     */
	public Song getSongByID(int id) {
		return theSongs.get(id);
	}

    /**
     * Return all songs that this directory has
     *
     * @return  A collection of song objects
     */
	public  Collection<Song> getSongs() {
		return theSongs.values();
	}

}