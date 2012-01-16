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
public class FindAverageFlightLengthCommand extends FlightGraphCommand {

    /**
     * {@inheritDoc}
     *
     * Finds the average length of flights in the graph
     */
    public boolean execute(Graph graph) {

        //Error-check
         if(!super.execute(graph))
                 return false;

        //Pull out the list of flights from the graph, and build a list of FlightGraphEdges
        List<FlightGraphEdge> flights = FlightGraphUtilities.getFlightGraphEdges(graph);

        //Find the average size
        double averageLength = findAverageFlightLength(flights);

        //Return a string representation of the average length
        TextIO.putln(Double.toString(averageLength));

        //Success
        return true;
    }

    //Finds the average flight length
    public double findAverageFlightLength(List<FlightGraphEdge> flights) {
        double averageLength, total = 0;
        for(FlightGraphEdge flightGraphEdge : flights){
             total += flightGraphEdge.getLength();
         }
        averageLength = total/(flights.size());
        return averageLength;
    }

}
