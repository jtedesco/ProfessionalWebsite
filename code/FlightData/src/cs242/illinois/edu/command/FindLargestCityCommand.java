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
public class FindLargestCityCommand extends FlightGraphCommand{

    /**
     * {@inheritDoc}
     *
     * Finds the largest city in the graph
     */
    public boolean execute(Graph graph){

        //Error-check
         if(!super.execute(graph))
                 return false;

        //Pull out the list of cities from the graph, and build a list of FlightGraphVertices
        List<FlightGraphVertex> cities = FlightGraphUtilities.getFlightGraphVertices(graph);

        //Find the largest city
        FlightGraphVertex largestCity = findLargestCity(cities);

        //Print the city
        TextIO.putln(largestCity.toString());

        //Success
        return true;
    }

    //Finds the largest city
    public FlightGraphVertex findLargestCity(List<FlightGraphVertex> cities) {
        double largestCityPopulation = 0;
        FlightGraphVertex largestCity = null;
        for(FlightGraphVertex city : cities){
            if(city.getPopulation() > largestCityPopulation){
                largestCityPopulation = city.getPopulation();
                largestCity = city;
            }
        }
        return largestCity;
    }
}
