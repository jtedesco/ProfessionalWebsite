package cs242.illinois.edu.command;

import cs242.illinois.edu.graph.*;
import cs242.illinois.edu.utilities.ConsoleUtilities;
import cs242.illinois.edu.utilities.Dijkstra;
import cs242.illinois.edu.utilities.FlightGraphUtilities;
import cs242.illinois.edu.utilities.TextIO;

import java.util.ArrayList;
import java.util.List;

/**
 * <p> Created By: Jon Tedesco
 * <p> Date: Oct 8, 2010
 *
 * <p> <p> Finds the total distance between two cities
 */
public class FindRouteTimeCommand extends FlightGraphCommand{
    public static final double CONVERSION_FACTOR_FROM_MILES_TO_KILOMETERS = 1.609344;
    public static final int AIRPLANE_VELOCITY = 350;

    /**
     * Implements the command to find the total cost to fly two cities
     *
     * @param graph The graph on which to execute this command
     * @return success?
     */
    public boolean execute(Graph graph) {

        //Error-check
        if(!super.execute(graph)){
            return false;
        }

        boolean repeat;
        do {

            //Grab the cities of the graph
            List<FlightGraphVertex> cities = FlightGraphUtilities.getFlightGraphVertices(graph);

            //Display a list, with name and codes only
            ConsoleUtilities.listNumberedCities(cities);

            //Prompt the user for source city for the flight
            String sourceCitySelectionPrompt = "From what city would you like to find the route?";
            String cityErrorMessage = "Not a valid city selection!";
            int from = ConsoleUtilities.getIntegerFromInput(sourceCitySelectionPrompt, cityErrorMessage);

            //Prompt the user for destination city for the flight
            String destinationCitySelectionPrompt = "At what city should the route arrive?";
            TextIO.putln(destinationCitySelectionPrompt);
            int to = ConsoleUtilities.getIntegerFromInput(sourceCitySelectionPrompt, cityErrorMessage);

            //Grab the cities from the graph
            FlightGraphVertex source = cities.get(from);
            FlightGraphVertex target = cities.get(to);

            //Get the distance between these two cities
            double totalMinutes = 0;
            try {
                totalMinutes = findRouteTime(source, target, (DirectedGraph) graph);
            } catch (Exception e) {
                TextIO.putln("Error tracing path!");
                return false;
            }

            //Calculate the time in hours and minutes
            int hours = ((int)totalMinutes)/60;
            double minutes = totalMinutes - hours*60;

            //Output the total distance of the route
            TextIO.put("The total time to fly between these two cities is:\n");
            TextIO.putf("    %.2f %s\n", totalMinutes, "minutes");
            TextIO.putf("Or, equivalently:\n    %d %s, %.2f %s\n\n", hours, "hours", minutes, "minutes");

            //Would you like to search again?
            String repeatPrompt = "Would like to find another route cost? (y/n)";
            repeat = ConsoleUtilities.promptYesNoResponse(repeatPrompt);

        } while (repeat);

        //Success
        return true;
    }

    /**
     * Helper function to find the cost to fly between two cities
     *
     * @param source The source vertex
     * @param target The target vertex
     * @param graph The graph
     * @return The total distance
     */
    public double findRouteTime(FlightGraphVertex source, FlightGraphVertex target, DirectedGraph graph) throws Exception {
        //Perform Dijkstra's
        List<FlightGraphVertex> path = Dijkstra.findShortestPath(source, target, graph);

        //Find the total length, without layovers
        double timeInMinutes = 0;
        for(int current = 1; current<path.size(); current++){

            //Find the edge connecting this city and the next
            FlightGraphEdge currentFlight = null;
            try {
                currentFlight = (FlightGraphEdge) graph.getEdge(path.get(current-1), path.get(current));
            } catch (GraphException e) {
                throw new Exception();
            }

            //Grab this length, and convert it into kilometers
            double currentLengthInKilometers = (currentFlight.getLength()*CONVERSION_FACTOR_FROM_MILES_TO_KILOMETERS);

            //Add the length of this flight to our running total
            timeInMinutes += ((currentLengthInKilometers/AIRPLANE_VELOCITY)*60);    //Convert from hours to kilometers
        }

        //If there are no layovers, we're done
        if(path.size()==2){
            return timeInMinutes;
        }

        //Remove the smallest hub city from the list (don't use it in the computation), but only if we're not about to remove it
        if(!path.get(path.size()-1).equals(source) && !path.get(path.size()-1).equals(target)){
            path.remove(path.size()-1);
        }

        //Get rid of the starting and ending cities
        path.remove(source);
        path.remove(target);

        //Compute layovers -- first add two hours for the one city we removed
        timeInMinutes += 120;
        List<FlightGraphVertex> rankedCities = rankCities(graph, path);
        for(FlightGraphVertex layoverCity : rankedCities){
            int extraOutBoundFlights = graph.findOutDegree(layoverCity)-1;

            //Calculate this city's layover
            int layover = 120 - (extraOutBoundFlights*10);
            if(layover<0){
                layover=0;
            }

            //Add this layover to the total time
            timeInMinutes += layover;
        }

        //Return the total distance
        return timeInMinutes;
    }

    //Helper function that ranks cities by their number of outgoing flights
    private List<FlightGraphVertex> rankCities(Graph graph, List<FlightGraphVertex> path) {

        //The sorted list of cities
        List<FlightGraphVertex> hubs = new ArrayList<FlightGraphVertex>();

        //Find the hub cities
        for(FlightGraphVertex city : path){

            //Find the highest degree node in the vertex not already in the list of hubs
            FlightGraphVertex hub = null;
            try {
                hub = findMaxDegreeNotInList((DirectedGraph) graph, hubs);
            } catch (GraphException e) {
                TextIO.putln("Error reading graph!");
            }

            //Add this hub to the list of hubs found
            hubs.add(hub);
        }

        //Return the list
        return hubs;
    }

    //Helper function for creating the sorted list of cities
    private FlightGraphVertex findMaxDegreeNotInList(DirectedGraph graph, List<FlightGraphVertex> hubs) throws GraphException {

        //Convert the input list into a FlightGraphVertex list
        List<FlightGraphVertex> cities = FlightGraphUtilities.getFlightGraphVertices(graph);

        //Find the maximum degree city in the list
        FlightGraphVertex biggestHub = null;
        int mostFlightsSoFar = -1;
        for(FlightGraphVertex city : cities){

            //If this city has the most flights going through it of any we've found so far, record it
            if(!(hubs.contains(city)) && graph.findOutDegree(city)>mostFlightsSoFar){
                biggestHub = city;
                mostFlightsSoFar = graph.findDegree(city);
            }
        }

        //Return the best we found
        return biggestHub;
    }
}
