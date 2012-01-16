package filter.score;

/**
 * User: Jon
 * Date: 4/23/11
 * Time: 4:54 PM
 *
 * Represents a high - level tag, or some arbitrary type. This extends <code>TagGraphVertex</code>
 *  to be used as a type of vertex in the graph structure we build.
 *
 *  This is needed for the graph structure!
 */
public class Tag implements Comparable<Tag> {

    /**
     * The value of this tag
     */
    private final String tagValue;

    /**
     * The 'score' of this tag, computed by the number of users that have this tag in common
     */
    private double tagScore;

    public Tag(String tagValue, double tagScore) {
        this.tagValue = tagValue;
        this.tagScore = tagScore;
    }

    public String getTagValue() {
        return tagValue;
    }

    public double getTagScore() {
        return tagScore;
    }

    public void setTagScore(int tagScore) {
        this.tagScore = tagScore;
    }

    /**
     * {@inheritDoc}
     *
     * Compares this tag to another, based on the tag scores and string value length. We weight 'more agreeable' tags above
     *  less agreed-upon ones, but break ties using tag value lenghts. Longer tags are considered better than shorter ones,
     *  because we assume longer tags to be more specific.
     *
     * @param other The tag to which to compare this tag
     * @return  -1, 0, or 1 as is standard
     */
    public int compareTo(Tag other) {

        // Compare scores, Break tie based on tag value lengths
        if(other.getTagScore() == getTagScore()) {

            if(other.getTagValue().length() == getTagValue().length()) {
                return 0;
            } else if(other.getTagValue().length() > getTagValue().length()) {
                return -1;
            } else {
                return 1;
            }

        } else if (other.getTagScore() < getTagScore()) {
            return -1;
        } else {
            return 1;
        }
    }
}
