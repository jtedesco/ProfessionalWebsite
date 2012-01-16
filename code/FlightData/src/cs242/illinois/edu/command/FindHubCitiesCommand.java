package cs242.illinois.edu.command;

import cs242.illinois.edu.graph.FlightGraphVertex;
import cs242.illinois.edu.graph.Graph;
import cs242.illinois.edu.graph.GraphException;
import cs242.illinois.edu.utilities.ConsoleUtilities;
import cs242.illinois.edu.utilities.FlightGraphUtilities;
import cs242.illinois.edu.utilities.TextIO;

import java.util.ArrayList;
import java.util.List;

/**
 * <p> Created By: Jon Tedesco
 * <p> Date: Sep 30, 2010
 *
 * <p> <p> A command to be executed on the flight graph
 */
public class FindHubCitiesCommand extends FlightGraphCommand {

    /**
     * {@inheritDoc}
     *
     * Lists the cities in the flight graph that are designated as 'hubs'
     */
    public boolean execute(Graph graph) {

        //Error-check
         if(!super.execute(graph))
                 return false;

        //The list of cities with lots of flights, ordered from most to least
        List<FlightGraphVertex> hubs = new ArrayList<FlightGraphVertex>();

        //Get the user's input as to  how many of the largest degree city to find
        boolean valid = false;
        int numberOfHubs = 0;
        do {

            //Ask for user input
            TextIO.putln("How many 'hub' cities would you like to find from the list?");
            String input = TextIO.getln();
            try{

                //Parse the input
                numberOfHubs = Integer.parseInt(input);
                valid = true;

                //If this is bigger than the number of possible hubs, it's invalid
                if(numberOfHubs>graph.getVertexList().size()){
                    valid = false;
                }

            } catch(Exception e) {
                TextIO.putln("Sorry, that number isn't valid, please try again.");
                valid = false;
            }

        } while (!valid);

        //Find the hub cities
        for(int hubsFound = 0; hubsFound<numberOfHubs; hubsFound++){

            //Find the highest degree node in the vertex not already in the list of hubs
            FlightGraphVertex hub = null;
            try {
                hub = findMaxDegreeNotInList(graph, hubs);
            } catch (GraphException e) {
                TextIO.putln("Error reading graph!");
                return false;
            }

            //Add this hub to the list of hubs found
            hubs.add(hub);
        }

        //Print out the list
        TextIO.putln("The list of 'hub' cities, in order of largest to smallest:");
        ConsoleUtilities.listNumberedCities(hubs);

        //Success
        return true;
    }

    private FlightGraphVertex findMaxDegreeNotInList(Graph graph, List<FlightGraphVertex> hubs) throws GraphException {

        //Convert the input list into a FlightGraphVertex list
        List<FlightGraphVertex> cities = FlightGraphUtilities.getFlightGraphVertices(graph);

        //Find the maximum degree city in the list
        FlightGraphVertex biggestHub = null;
        int mostFlightsSoFar = -1;
        for(FlightGraphVertex city : cities){

            //If this city has the most flights going through it of any we've found so far, record it
            if(!(hubs.contains(city)) && graph.findDegree(city)>mostFlightsSoFar){
                biggestHub = city;
                mostFlightsSoFar = graph.findDegree(city);
            }
        }

        //Return the best we found
        return biggestHub;
    }
}
