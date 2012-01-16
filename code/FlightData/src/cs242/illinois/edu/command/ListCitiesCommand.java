package cs242.illinois.edu.command;

import cs242.illinois.edu.graph.FlightGraphVertex;
import cs242.illinois.edu.graph.Graph;
import cs242.illinois.edu.utilities.ConsoleUtilities;
import cs242.illinois.edu.utilities.FlightGraphUtilities;

import java.util.List;

/**
 * <p> Created By: Jon Tedesco
 * <p> Date: Sep 30, 2010
 *
 * <p> <p> A command to be executed on the flight graph
 */
public class ListCitiesCommand extends FlightGraphCommand{

    /**
     * {@inheritDoc}
     *
     * Lists the cities in the flight graph
     */
    public boolean execute(Graph graph) {

        //Error-check
         if(!super.execute(graph))
                 return false;

        //Grab the cities of the graph
        List<FlightGraphVertex> cities = FlightGraphUtilities.getFlightGraphVertices(graph);

        //Display a list, with name and codes only
        ConsoleUtilities.listUnnumberedCities(cities);

        //Success
        return true;
    }
}
