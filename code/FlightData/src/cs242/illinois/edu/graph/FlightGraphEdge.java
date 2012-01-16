package cs242.illinois.edu.graph;

/**
 * <p> Created By: Jon Tedesco
 * <p> Date: Sep 28, 2010
 *
 * <p> <p> The specific implementation of a flight graph edge. This vertex includes a critical data for a flight
 *          in the <code>FlightGraph</code> implementation
 */
public class FlightGraphEdge extends DirectedEdge{

    /**
     * A value representing the length of the flight in miles
     */
    private double length;

    /**
     * Constructs a flight graph edge
     *
     * @param length The length of the flight representing by the edge, in miles
     * @param source The source Vertex, representing the source airport
     * @param target The target Vertex, representing the destination airport
     */
    public FlightGraphEdge(double length, Vertex source, Vertex target) {
        super(source, target);
        this.length = length;
    }

    /**
     * Gets the length of this flight
     *
     * @return The length of the flight associated with this edge
     */
    public double getLength() {
        return length;
    }

    /**
     * Returns a list of strings representing the fields of this object
     *
     * @return A list of strings, representing the fields of this object
     */
    public static String[] getProperties(){
        return new String[]{"airportCode", "name", "country", "continent", "region", "timezone", "population", "longitude", "latitude"};
    }

    @Override
    public String toString() {
        return "From:" +
                "\n" + getSource().toString() +
                "\n" +
                "\n" +
                "To:" +
                "\n" + getTarget().toString() +
                "\n" +
                "\n" +
                "Length: " + length;
    }

    /**
      * <p> Checks the equality of this <code>DirectedEdge</code> to another <code>DirectedEdge</code>.
      *
      * <p> Equality of two vertices is based on the equality of their weights. If the two weights
      *      are equivalent, then the edges are equivalent.
      *
      * @param otherObject The object with which to compare this object
      * @return True or false, based on the equality of the object to this object
      */
    @Override
    public boolean equals(Object otherObject) {

        //If these two objects are the exact same object, then they are equal
        if (this == otherObject)
            return true;

        //If this is the wrong type, not equal
        if (!(otherObject instanceof FlightGraphEdge))
            return false;

        //Check to see that their abstract structures are equivalent
        if (!super.equals(otherObject))
            return false;

        //Compare the weights of the edges
        FlightGraphEdge that = (FlightGraphEdge) otherObject;
        if (length != that.getLength())
            return false;

        //If we got here, then they are equal
        return true;
    }

    /**
     * Generates a hash code for this ordered pair
     *
     * @return The generated hash code
     */
    @Override
    public int hashCode() {
        long temp = length != +0.0d ? Double.doubleToLongBits(length) : 0L;
        return (int) (temp ^ (temp >>> 32));
    }
}
