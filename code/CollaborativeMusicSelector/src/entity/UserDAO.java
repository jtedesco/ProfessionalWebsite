package entity;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

/**
 * Lets us access user data from our database.
 */
public class UserDAO {

	private final SongDirectory theSongDirectory;


	public UserDAO(SongDirectory aSongDirectory) {
		this.theSongDirectory = aSongDirectory;
	}

	public SongDirectory getSongDirectory() {
		return theSongDirectory;
	}
	
	
	public List<User> retrieveUsersByUserIDs(List<String> aNetIDs) {
		if(aNetIDs.isEmpty()) {
			return new ArrayList<User>();
		}
		
		// UserID -> songs that the user has in history
		Map<String, Set<Song>> mySongHistories = new HashMap<String, Set<Song>>();
		for(String myUserID : aNetIDs) {
			mySongHistories.put(myUserID, new HashSet<Song>());
		}
		
		String query = buildUserHistoryQueryString(aNetIDs);
		try {
			ResultSet myResults = DB.getInstance().query(query);
			int songCount = 0;
			
			//add the songs to the sets for each person
			while(myResults.next()) {
				Set<Song> myUserSongList = mySongHistories.get(myResults.getString("who")); //grab the song list for the given person
                Song song = getSongDirectory().getSongByID(myResults.getInt("song_id"));
                if(song != null) {
                    myUserSongList.add(song); //add the song to said set
                }
                songCount++;
			}
			
			//create the list of users to return
			List<User> myUsers = new ArrayList<User>();
			for(Entry<String, Set<Song>> myEntry : mySongHistories.entrySet()) {
				myUsers.add(new User(myEntry.getKey(), myEntry.getValue(), songCount));
			}
			
			return myUsers;
			
		} catch (SQLException sqle) {
			//if we crash, just return an empty user list. . . probably not the best!
			return new ArrayList<User>();
		}

	}
	
	public List<String> retrieveUserNames() {
		String query = "SELECT DISTINCT(who) FROM history";
		
		try {
			ResultSet myResults = DB.getInstance().query(query);
			List<String> myNames = new ArrayList<String>();
			while(myResults.next()) {
				myNames.add(myResults.getString("who"));
			}
			
			return myNames;
		} catch(Exception e) {
			System.err.println("Did not retrieve users!");
			return null;
		}
	}
	
	
	/**
	 * Creates a query that will return all the songs the provided netIDs have upvoted
	 * @param aNetIDs
	 * @return a query to run to get those songs
	 */
	private String buildUserHistoryQueryString(List<String> aNetIDs) {
		
		StringBuilder myQuery = new StringBuilder();
		myQuery.append("SELECT song_id, who " +
				"FROM history WHERE (");
		
		boolean isFirst= true;
		for(String myNetID : aNetIDs) {
			if(isFirst) {
				isFirst = false;
			} else {
				myQuery.append(" OR ");
			}
			myQuery.append("who = '" + myNetID + "'");
		}
		
        myQuery.append(" AND FROM_UNIXTIME(`time`) > SUBDATE(NOW(), INTERVAL 3 MONTH)");
        myQuery.append(")");
System.out.println(myQuery);
		return myQuery.toString();
	}

}
