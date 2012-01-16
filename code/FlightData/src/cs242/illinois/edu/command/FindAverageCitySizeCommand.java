package cs242.illinois.edu.command;

import cs242.illinois.edu.graph.FlightGraphVertex;
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
public class FindAverageCitySizeCommand extends FlightGraphCommand{

    /**
     * {@inheritDoc}
     *
     * Finds the average size of all cities in the graph
     */
    public boolean execute(Graph graph) {

        //Error-check
         if(!super.execute(graph))
                 return false;

        //Pull out the list of cities from the graph, and build a list of FlightGraphEdges
         List<FlightGraphVertex> cities = FlightGraphUtilities.getFlightGraphVertices(graph);

         //Find the average size
        double averageSize = findAverageCitySize(cities);

         //Return a string representation of the average length
         TextIO.putln(Double.toString(averageSize));

        //Success
        return true;
    }

    //Finds the average city size
    public double findAverageCitySize(List<FlightGraphVertex> cities) {
        double averageSize = 0, total = 0;
        for(FlightGraphVertex city : cities){
            total += city.getPopulation();
        }
        averageSize = total/(cities.size());
        return averageSize;
    }
}
