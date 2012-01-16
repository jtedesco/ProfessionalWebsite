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
public class FindSmallestCityCommand extends FlightGraphCommand{

    /**
     * {@inheritDoc}
     *
     * Finds the smallest city in the graph
     */
    public boolean execute(Graph graph){

        //Error-check
         if(!super.execute(graph))
                 return false;

        //Pull out the list of cities from the graph, and build a list of FlightGraphVertices
        List<FlightGraphVertex> cities = FlightGraphUtilities.getFlightGraphVertices(graph);

        //Find the largest city
        FlightGraphVertex smallestCity = findSmallestCity(cities);

        //Output it
        TextIO.putln(smallestCity.toString());

        //Success
        return true;
    }

    //Finds the smallest city
    public FlightGraphVertex findSmallestCity(List<FlightGraphVertex> cities) {
        double smallestPopulation = Double.MAX_VALUE;
        FlightGraphVertex smallestCity = null;
        for(FlightGraphVertex city : cities){
            if(city.getPopulation() < smallestPopulation){
                smallestPopulation = city.getPopulation();
                smallestCity = city;
            }
        }
        return smallestCity;
    }
}
