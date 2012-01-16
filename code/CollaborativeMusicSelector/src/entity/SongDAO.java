package entity;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class SongDAO {

	/**
	 * Retrieves aLimit songs from the database. Note that it does not pull
	 * artist/album tags with the songs
	 * 
	 * @param aLimit
	 *            The maximum number of songs to retrieve
	 * @return A map of songs
	 */
	public static Map<Integer, Song> retrieveSongs(int aLimit) {

		/**
		 * The query to retrieve a set number of songs from the acoustics
		 * database.
		 */
		String myQuery = "SELECT songs.song_id, songs.title, songs.artist, songs.album, group_concat(tags.value separator ', ') as tag_list "
				+ "FROM songs "
				+ "LEFT JOIN songs_tags ON (songs.song_id = songs_tags.song_id) "
				+ "LEFT JOIN tags ON (tags.id = songs_tags.tag_id) "
				+ "GROUP BY songs.song_id " + "LIMIT " + aLimit;

		try {
			ResultSet myResults = DB.getInstance().query(myQuery);
			Map<Integer, Song> mySongs = new HashMap<Integer, Song>();

			long starttime = System.currentTimeMillis();
			Set<String> fetchedSongs = new HashSet<String>();
			while (myResults.next()) {
				Integer mySongID = myResults.getInt("song_id");
				String myTitle = myResults.getString("title");
				String myArtist = myResults.getString("artist");
				String myAlbum = myResults.getString("album");
				String mySongTags = myResults.getString("tag_list");

				// TODO: Get artist tags, get album tags
				Song mySong = new Song(mySongTags, "", "", myTitle, myArtist,
						myAlbum, mySongID);
				
				String key = mySong.getTitle().trim().toLowerCase() + "||" + mySong.getArtist().trim().toLowerCase();
				if (!fetchedSongs.contains(key)) {
					mySongs.put(mySongID, mySong);
					fetchedSongs.add(key);
				}
				
			}
			long endtime = System.currentTimeMillis();
			System.out.println("Time to convert query: "
					+ (endtime - starttime));
			return mySongs;

		} catch (SQLException e) {
			System.err.println("Did not retrive the songs!");
			return null;
		}
	}

}
