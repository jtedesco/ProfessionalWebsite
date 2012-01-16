package filter;

import java.util.List;

/**
 * User: Jon
 * Date: 4/25/11
 * Time: 11:35 PM
 *
 * Represents a generic filter for tags, implemented by any number of specific schemes
 */
public interface TagFilter {

    /**
     * Find the best representative set of the interests of a set of users
     *
     *  @param  userIds The list of users whose common interests we're looking to match
     *  @return         The list of tags that best represents the given list of users, in descending order of relevance
     */
    public List<String> findBestRepresentativeTags(List<String> userIds);
    public List<String> findBestRepresentativeTagsNonWeighted(List<String> userIds);

}
