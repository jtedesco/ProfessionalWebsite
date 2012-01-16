package cs242.illinois.edu.command;

import cs242.illinois.edu.graph.FlightGraphEdge;
import cs242.illinois.edu.graph.Graph;
import cs242.illinois.edu.utilities.FlightGraphUtilities;
import cs242.illinois.edu.utilities.TextIO;

import java.util.List;

/**
 * <p> Created By: Jon Tedesco
 * <p> Date: Sep 30, 2010
 *
 * <p> <p> A command to be executed on the flight graph
 */
public class FindLongestFlightCommand extends FlightGraphCommand{

    /**
     * {@inheritDoc}
     *
     * Finds the longest flight in the graph
     */
    public boolean execute(Graph graph) {

        //Error-check
         if(!super.execute(graph))
                 return false;

        //Pull out the list of flights from the graph, and build a list of FlightGraphVertices
        List<FlightGraphEdge> flights = FlightGraphUtilities.getFlightGraphEdges(graph);

        //Find the largest city
        FlightGraphEdge longestFlight = findLongestFlight(flights);

        //Output the flight data
        TextIO.putln(longestFlight.toString());

        //Success
        return true;
    }

    //Finds the longest flight
    public FlightGraphEdge findLongestFlight(List<FlightGraphEdge> flights) {
        double longestFlightLength = 0;
        FlightGraphEdge longestFlight = null;
        for(FlightGraphEdge flightGraphEdge : flights){
            if(flightGraphEdge.getLength() > longestFlightLength){
                longestFlightLength = flightGraphEdge.getLength();
                longestFlight = flightGraphEdge;
            }
        }
        return longestFlight;
    }
}
