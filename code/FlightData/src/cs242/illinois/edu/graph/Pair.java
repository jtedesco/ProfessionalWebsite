package cs242.illinois.edu.graph;

/**
 * <p> Created By: Jon Tedesco
 * <p> Date: Sep 28, 2010
 *
 * <p> <p> A simple data structure for holding an ordered pair of objects. Code taken from www.stackoverflow.com.
 */
public class Pair<A, B> {

    /**
     * First item
     */
    private A first;

    /**
     * Second item
     */
    private B second;

    public Pair(A first, B second) {
        super();
        this.first = first;
        this.second = second;
    }


    /**
     * Generates a hash code for this ordered pair
     *
     * @return The generated hash code
     */
    public int hashCode() {
        int hashFirst = first != null ? first.hashCode() : 0;
        int hashSecond = second != null ? second.hashCode() : 0;

        return (hashFirst + hashSecond) * hashSecond + hashFirst;
    }

    /**
     * Compares two <code>Pair</code> objects, returning true iff the two objects appear in the pair, not necessarily in
     *  the same order.
     *
     * @param other The object to compare with <code>this</code>
     * @return The equality of the objects
     */
    public boolean equals(Object other) {
        if (other instanceof Pair) {
                Pair otherPair = (Pair) other;

                //Same order
                boolean sameFirstsOrdered = (first==null && otherPair.getFirst()==null) || (first.equals(otherPair.getFirst()));
                boolean sameSecondsOrdered = (second==null && otherPair.getSecond()==null) || (second.equals(otherPair.getSecond()));

                //Check other order
                boolean sameFirstsUnordered = (first==null && otherPair.getSecond()==null) || (first.equals(otherPair.getSecond()));
                boolean sameSecondsUnordered = (second==null && otherPair.getFirst()==null) || (second.equals(otherPair.getFirst()));

                //True if either of these pairs are equal
                return (sameFirstsOrdered && sameSecondsOrdered) || (sameFirstsUnordered && sameSecondsUnordered);

        }

        //False otherwise
        return false;
    }

    /**
     * Displays the ordered pair, using the default <code>toString()</code> functionality
     *
     * @return The string representation of this ordered pair
     */
    public String toString()
    {
           return "(" + first + ", " + second + ")";
    }

    public A getFirst() {
        return first;
    }

    public void setFirst(A first) {
        this.first = first;
    }

    public B getSecond() {
        return second;
    }

    public void setSecond(B second) {
        this.second = second;
    }
}
