package filter.score;

import entity.SongDirectory;
import entity.User;
import entity.UserDAO;
import filter.TagFilter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;
import java.util.PriorityQueue;

/**
 * User: Jon Date: 4/25/11 Time: 11:46 PM
 * 
 * This filter simply returns all the tags that the given users have in common,
 * in decreasing order
 */
public class ScoreFilter implements TagFilter {

	/**
	 * The song directory to reference
	 */
	private final SongDirectory songDirectory;

	/**
	 * The number of matching tags to return on queries
	 */
	private final int numberOfTagsToMatch;

	public ScoreFilter(SongDirectory songDirectory, int numberOfTagsToMatch) {
		this.songDirectory = songDirectory;
		this.numberOfTagsToMatch = numberOfTagsToMatch;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * This particular implementation of <code>TagFilter</code> simply takes the
	 * intersection of the user's interests and returns them in reducing order
	 * of score, up to the given limit.
	 * 
	 * @param userIds
	 *            The list of users whose common interests we're looking to
	 *            match
	 * @return The best set of tags
	 */
	public List<String> findBestRepresentativeTags(List<String> userIds) {

		// Get the list of users with these ids
		UserDAO userDAO = new UserDAO(songDirectory);
		List<User> users = userDAO.retrieveUsersByUserIDs(userIds);

		// Data to keep track of the best tags we find
		HashMap<String, Double> commonTags = new HashMap<String, Double>();

		// Iterate through each user, and update scores of occurrences
		for (User user : users) {
			// // Add a certain score for song tags
			// updateScores(commonTags, user, (int) TagType.song.getWeight());
			//
			// // Add a different score for album tags
			// updateScores(commonTags, user, (int) TagType.album.getWeight());
			//
			// // Add a heavier score for artist tags
			// updateScores(commonTags, user, (int) TagType.artist.getWeight());
			int songCount = user.getSongCount();
			
			int userMax = 0;
			
			for (Entry<String, Integer> entry : user.getTagWeights().entrySet()){
				if (entry.getValue() > userMax) {
					userMax = entry.getValue();
				}
			}
			
			
			for (Entry<String, Integer> entry : user.getTagWeights().entrySet()) {
				Double existingWeight = commonTags.get(entry.getKey());

				// Using logarithmic term frequency
				if (existingWeight != null) {
					commonTags.put( entry.getKey(),
							existingWeight	+  entry.getValue()/userMax);
				} else { commonTags.put(
							entry.getKey(), (double) entry.getValue()/userMax);
				}
			}
		}

		// Create a priority queue of these tags
		PriorityQueue<Tag> rankedTags = new PriorityQueue<Tag>();
		for (String sharedTag : commonTags.keySet()) {

			// Build a new tag for this string tag, update its score, and add it
			// to the ranked tags
			Tag tag = new Tag(sharedTag, commonTags.get(sharedTag));
			rankedTags.add(tag);
		}
		// Pull out the top tags up to the quota
		List<String> resultsList = new ArrayList<String>();

		int i = 0;
		while (i < numberOfTagsToMatch) {
			i++;
			Tag myTag = rankedTags.poll();
			if (myTag != null) {
				resultsList.add(myTag.getTagValue());
//				if (i < 20) {
//					System.out.println(myTag.getTagValue() + "----"
//							+ myTag.getTagScore());
//				}
//
			}
		}

		// Done!
		return resultsList;
	}

	/**
	 * Helper function to update scores here
	 * 
	 * @param commonTags
	 *            The hash map of tags and their counts so far
	 * @param user
	 *            The user we want to get data from
	 * @param weight
	 *            The weight to assign to this batch of tags
	 */
	private void updateScores(HashMap<String, Integer> commonTags, User user,
			int weight) {

		// Add a certain count for this type of tags
		for (String tag : user.getSongTags()) {
			if (commonTags.containsKey(tag)) {
				int currentScore = commonTags.get(tag);
				commonTags.remove(tag);
				commonTags.put(tag, currentScore + weight);
			} else {
				commonTags.put(tag, weight);
			}
		}
	}

	public SongDirectory getSongDirectory() {
		return songDirectory;
	}

	public int getNumberOfTagsToMatch() {
		return numberOfTagsToMatch;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * This particular implementation of <code>TagFilter</code> simply takes the
	 * intersection of the user's interests and returns them in reducing order
	 * of score, up to the given limit.
	 * 
	 * @param userIds
	 *            The list of users whose common interests we're looking to
	 *            match
	 * @return The best set of tags
	 */
	public List<String> findBestRepresentativeTagsNonWeighted(
			List<String> userIds) {

		// Get the list of users with these ids
		UserDAO userDAO = new UserDAO(songDirectory);
		List<User> users = userDAO.retrieveUsersByUserIDs(userIds);

		// Data to keep track of the best tags we find
		HashMap<String, Integer> commonTags = new HashMap<String, Integer>();

		// Iterate through each user, and update scores of occurrences
		for (User user : users) {
			// // Add a certain score for song tags
			// updateScores(commonTags, user, (int) TagType.song.getWeight());
			//
			// // Add a different score for album tags
			// updateScores(commonTags, user, (int) TagType.album.getWeight());
			//
			// // Add a heavier score for artist tags
			// updateScores(commonTags, user, (int) TagType.artist.getWeight());

			for (Entry<String, Integer> entry : user.getTagWeights().entrySet()) {
				Integer existingWeight = commonTags.get(entry.getKey());

				// Using logarithmic term frequency
				if (existingWeight != null) {
					commonTags.put(entry.getKey(),
							existingWeight + entry.getValue());
				} else {
					commonTags.put(entry.getKey(), entry.getValue());
				}
			}
		}

		// Create a priority queue of these tags
		PriorityQueue<Tag> rankedTags = new PriorityQueue<Tag>();
		for (String sharedTag : commonTags.keySet()) {

			// Build a new tag for this string tag, update its score, and add it
			// to the ranked tags
			Tag tag = new Tag(sharedTag, commonTags.get(sharedTag));
			rankedTags.add(tag);
		}
		// Pull out the top tags up to the quota
		List<String> resultsList = new ArrayList<String>();

		int i = 0;
		while (i < numberOfTagsToMatch) {
			i++;
			Tag myTag = rankedTags.poll();
			if (myTag != null) {
				resultsList.add(myTag.getTagValue());
//				if (i < 20) {
//					System.out.println(myTag.getTagValue() + "----"
//							+ myTag.getTagScore());
//				}

			}
		}

		// Done!
		return resultsList;
	}

}
