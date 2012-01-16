package cs242.illinois.edu.graph;

import java.util.List;

/**
 * <p> Created By: Jon Tedesco
 * <p> Date: Sep 28, 2010
 *
 * <p> <p> The specific implementation of a flight graph vertex. This vertex includes a critical data for an airport
 *          in the <code>FlightGraph</code> implementation
 */
public class FlightGraphVertex extends Vertex implements Comparable<FlightGraphVertex>{

    /**
     * The airport code of the airport. This is guaranteed to be unique for each airport, and its value comes from'
     * <code>FlightGraphConstants</code>.
     */
    private String airportCode;

    /**
     * The name of the city in which this airport vertex is located. Not guaranteed to be unique for each airport.
     * This value also comes from <code>FlightGraphConstants</code>.
     */
    private String name;

    /**
     * The country where this airport is located. Not necessarily unique for any airport. The value will be a constant
     * from <code>FlightGraphConstants</code>.
     */
    private String country;

    /**
     * The continent in which this airport is located. Not necessarily unique for any airport. The value of the field will
     * be a constant from <code>FlightGraphConstants</code>.
     */
    private String continent;

    /**
     * The region in which this airport is located. Not necessarily unique for any airport. The value of the field will
     * be a constant from <code>FlightGraphConstants</code>.
     */
    private int region;

    /**
     * The timezone in which this airport is located. Not necessarily unique for any airport. The value of the field will
     * be a constant from <code>FlightGraphConstants</code>.
     */
    private int timezone;

    /**
     * The population of the city in which this airport is located
     */
    private int population;

    /**
     * The longitude at which this airport is located
     */
    private Pair<Character, Integer> longitude;

    /**
     * The latitude at which this airport is located
     */
    private Pair<Character, Integer> latitude;

    /**
     * The minimum distance of this vertex from another vertex (Used for Dijkstra's algorithm)
     */
    private double minDistance = Double.POSITIVE_INFINITY;

    /**
     * The 'previous' node in our traversal of the graph (Used for Dijkstra's algorithm)
     */
    private FlightGraphVertex previous = null;

    /**
     * Constructs a FlightGraphVertex with all necessary data for a vertex. This assumes that the vertex is added with
     *  no adjacent vertices initially.
     *
     * @param airportCode The airport code of the airport
     * @param name The name of the airport
     * @param country The country of the airport
     * @param continent The continent where the airport is located
     * @param region The region where the airport is located
     * @param timezone The timezone where this airport is located
     * @param population The population of the city in which this airport is located
     * @param longitude The longitude at which this airport is located
     * @param latitude The latitude at which this airport is located
     */
    public FlightGraphVertex(String airportCode, String name, String country, String continent, int timezone,
                             Pair<Character, Integer> longitude, Pair<Character, Integer> latitude, int population,
                             int region) {
        this.airportCode = airportCode;
        this.name = name;
        this.country = country;
        this.continent = continent;
        this.region = region;
        this.timezone = timezone;
        this.population = population;
        this.longitude = longitude;
        this.latitude = latitude;
    }

    /**
     * Constructs a FlightGraphVertex with all necessary data for a vertex. However, unlike the default constructor, this
     *  also initializes the list of adjacent vertices and their corresponding edges.
     *
     * @param airportCode The airport code of the airport
     * @param name The name of the airport
     * @param country The country of the airport
     * @param continent The continent where the airport is located
     * @param region The region where the airport is located
     * @param timezone The timezone where this airport is located
     * @param population The population of the city in which this airport is located
     * @param longitude The longitude at which this airport is located
     * @param latitude The latitude at which this airport is located
     */
    public FlightGraphVertex(String airportCode, String name, String country, String continent, int timezone,
                             Pair<Character, Integer> longitude, Pair<Character, Integer> latitude, int population,
                             int region, List<EdgeVertexBean> adjacentEdgesAndVertices) {
        super(adjacentEdgesAndVertices);
        this.airportCode = airportCode;
        this.name = name;
        this.country = country;
        this.continent = continent;
        this.region = region;
        this.timezone = timezone;
        this.population = population;
        this.longitude = longitude;
        this.latitude = latitude;
    }

    /**
     * Gets the airport code for this airport vertex
     *
     * @return The airport code for this Vertex
     */
    public String getAirportCode() {
        return airportCode;
    }

    /**
     * Sets the airport code
     *
     * @param airportCode The airport code
     */
    public void setAirportCode(String airportCode) {
        this.airportCode = airportCode;
    }

    /**
     * Gets the name of the airport
     *
     * @return The name of the airport represented by this Vertex
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the name
     *
     * @param name The name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Gets the country where this airport is located
     *
     * @return The country where this airport is located
     */
    public String getCountry() {
        return country;
    }

    /**
     * Sets the country of the city
     *
     * @param country The country
     */
    public void setCountry(String country) {
        this.country = country;
    }

    /**
     * Gets the continent where this airport is located
     *
     * @return The continent where this airport is located
     */
    public String getContinent() {
        return continent;
    }

    /**
     * Sets the continent of the city
     *
     * @param continent The continent
     */
    public void setContinent(String continent) {
        this.continent = continent;
    }

    /**
     * Gets the region where this airport is located
     *
     * @return The region where this airport is located
     */
    public int getRegion() {
        return region;
    }

    /**
     * Sets the region
     *
     * @param region The region
     */
    public void setRegion(int region) {
        this.region = region;
    }

    /**
     * Gets the timezone where this airport is located
     *
     * @return The timezone where this airport is located
     */
    public int getTimezone() {
        return timezone;
    }

    /**
     * Sets the timezone
     *
     * @param timezone The timezone
     */
    public void setTimezone(int timezone) {
        this.timezone = timezone;
    }

    /**
     * Gets the population of the city in which this airport is located
     *
     * @return The population of the city in which the airport is located
     */
    public int getPopulation() {
        return population;
    }

    /**
     * Sets the population
     *
     * @param population The population
     */
    public void setPopulation(int population) {
        this.population = population;
    }

    /**
     * Gets the longitude of the airport
     *
     * @return The longitude of the airport
     */
    public Pair<Character, Integer> getLongitude() {
        return longitude;
    }

    /**
     * Sets the longitude
     *
     * @param longitude The longitude
     */
    public void setLongitude(Pair<Character, Integer> longitude) {
        this.longitude = longitude;
    }

    /**
     * Gets the latitude of the airport
     *
     * @return The latitude of the airport
     */
    public Pair<Character, Integer> getLatitude() {
        return latitude;
    }

    /**
     * Sets the latitude
     *
     * @param latitude The latitude
     */
    public void setLatitude(Pair<Character, Integer> latitude) {
        this.latitude = latitude;
    }

    /**
     * Returns the minimum distance of this vertex at any given point in Dijkstra's
     *
     * @return The minimum distance
     */
    public double getMinDistance() {
        return minDistance;
    }

    /**
     * Sets the minimum distance of this vertex at any given point in Dijkstra's
     *
     * @param minDistance The minimum distance
     */
    public void setMinDistance(double minDistance) {
        this.minDistance = minDistance;
    }
    /**
     * Returns the previous node in our traversal of the graph in Dijkstra's
     *
     * @return The previous node
     */
    public FlightGraphVertex getPrevious() {
        return previous;
    }

    /**
     * Sets the previous node in our traversal of the graph in Dijkstra's
     *
     * @param previous The previous node
     */
    public void setPrevious(FlightGraphVertex previous) {
        this.previous = previous;
    }

    /**
     * The compare to method, implement from the <code>Comparable</code> interface
     */
    public int compareTo(FlightGraphVertex other) {
        return Double.compare(getMinDistance(), other.getMinDistance());
    }

    /**
     * <p> Checks the equality of this <code>FlightGraphVertex</code> to another <code>FlightGraphVertex</code>.
     *
     * <p> This implementation is a skeleton only, but is useful for looking up a vertex by its airport code alone. This
     *      allows us to add edges to the graph without having to write a special helper function to find a vertex given
     *      only its airport code 
     *
     * @param otherObject The object with which to compare this object
     * @return True or false, based on the equality of the object to this object
     */
    @Override
    public boolean equals(Object otherObject) {

        //First, check to see if the basic vertex structures are equal
        if((otherObject instanceof FlightGraphVertex)){
            FlightGraphVertex other = (FlightGraphVertex) otherObject;

            //Now, compare the airport code ONLY
            if (airportCode != null ? !airportCode.equals(other.airportCode) : other.airportCode != null)
                return false;

            //If we got here, then they are equal
            return true;
        }

        //Otherwise, they are not equal
        return false;
    }

    /**
     * Returns a list of strings representing the fields of this object
     *
     * @return A list of strings, representing the fields of this object
     */
    public static String[] getProperties(){
        return new String[]{"length", "source", "target"};
    }
    
    /**
     * Builds a string representation of this city. This will be the standard representation of the city in text
     *
     * @return
     */
    @Override
    public String toString() {
        return "Airport Code: '" + airportCode + "'\n" +
                "City Name: '" + name + "'\n" +
                "Country: '" + country + "'\n" +
                "Continent: '" + continent + "'\n" +
                "Region: " + region + "'\n" +
                "Timezone: " + timezone + "'\n" +
                "Population: " + population + "'\n" +
                "Longitude: " + longitude + "'\n" +
                "Latitude: " + latitude + "'\n";
    }

    /**
     * <p> Checks the equality of this <code>FlightGraphVertex</code> to another <code>FlightGraphVertex</code>.
     *
     * <p> Equality of two vertices is based on the equality of their fields. If the parent vertex structures are
     *      equivalent, and if the fields are equivalent of the <code>FlightGraphVertex</code> objects, then the two
     *      vertices are equivalent.
     *
     * @param otherObject The object with which to compare this object
     * @return True or false, based on the equality of the object to this object
     */
    public boolean fullEquals(Object otherObject) {

        FlightGraphVertex other = (FlightGraphVertex) otherObject;

        //First, check to see if the basic vertex structures are equal
        if(super.equals(otherObject) && (otherObject instanceof FlightGraphVertex)){

            //Now, compare the fields
            if(population != other.population)
                return false;
            if (region != other.region)
                return false;
            if (timezone != other.timezone)
                return false;
            if (airportCode != null ? !airportCode.equals(other.airportCode) : other.airportCode != null)
                return false;
            if (continent != null ? !continent.equals(other.continent) : other.continent != null)
                return false;
            if (country != null ? !country.equals(other.country) : other.country != null)
                return false;
            if (latitude != null ? !latitude.equals(other.latitude) : other.latitude != null)
                return false;
            if (longitude != null ? !longitude.equals(other.longitude) : other.longitude != null)
                return false;
            if (name != null ? !name.equals(other.name) : other.name != null)
                return false;

            //If we got here, then they are equal
            return true;
        }

        //Otherwise, they are not equal
        return false;
    }

    /**
     * Generates a hash code for this ordered pair
     *
     * @return The generated hash code
     */
    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + (airportCode != null ? airportCode.hashCode() : 0);
        result = 31 * result + (name != null ? name.hashCode() : 0);
        result = 31 * result + (country != null ? country.hashCode() : 0);
        result = 31 * result + (continent != null ? continent.hashCode() : 0);
        result = 31 * result + region;
        result = 31 * result + timezone;
        result = 31 * result + population;
        result = 31 * result + (longitude != null ? longitude.hashCode() : 0);
        result = 31 * result + (latitude != null ? latitude.hashCode() : 0);
        return result;
    }
}
