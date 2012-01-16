package cs242.illinois.edu.command;

import cs242.illinois.edu.graph.FlightGraphEdge;
import cs242.illinois.edu.graph.FlightGraphVertex;
import cs242.illinois.edu.graph.Graph;
import cs242.illinois.edu.graph.GraphException;
import cs242.illinois.edu.utilities.ConsoleUtilities;
import cs242.illinois.edu.utilities.FlightGraphUtilities;
import cs242.illinois.edu.utilities.TextIO;

import java.util.List;

/**
 * <p> Created By: Jon Tedesco
 * <p> Date: Sep 30, 2010
 *
 * <p> <p> A command to be executed on the flight graph
 */
public class AddFlightCommand extends FlightGraphCommand {

    /**
     * {@inheritDoc}
     *
     * Adds a specific flight to the graph
     * @param graph
     */
    public boolean execute(Graph graph) {

        //Error-check
        if(!super.execute(graph))
                return false;

        boolean repeat = true;
        do {

            //Grab the cities of the graph
            List<FlightGraphVertex> cities = FlightGraphUtilities.getFlightGraphVertices(graph);

            //Display a list, with name and codes only
            ConsoleUtilities.listNumberedCities(cities);

            //Prompt the user for source city for the flight
            String sourceCitySelectionPrompt = "From what city should the flight originate?";
            String cityErrorMessage = "Not a valid city selection!";
            int from = ConsoleUtilities.getIntegerFromInput(sourceCitySelectionPrompt, cityErrorMessage);

            //Prompt the user for source city for the flight
            String destinationCitySelectionPrompt = "At what city should the flight arrive?";
            TextIO.putln(destinationCitySelectionPrompt);
            int to = ConsoleUtilities.getIntegerFromInput(sourceCitySelectionPrompt, cityErrorMessage);

            //Prompt the user for the length of the flight
            String lengthPrompt = "How long is the flight?";
            String flightErrorMessage = "Not a valid distance!";
            double distance = ConsoleUtilities.getDoubleFromInput(lengthPrompt, flightErrorMessage);

            //Grab the cities from the graph
            FlightGraphVertex source = cities.get(from);
            FlightGraphVertex target = cities.get(to);
            
            //Actually build and insert the new flight
            FlightGraphEdge newEdge = new FlightGraphEdge(distance, source, target);
            try {
                graph.addEdge(newEdge);
                TextIO.putln("Successfully added the city to the graph!");
            } catch (GraphException e) {
                TextIO.putln("Error adding the city to the graph!");
            }

            //Would you like to search again?
            String repeatPrompt = "Would like to add another flight? (y/n)";
            repeat = ConsoleUtilities.promptYesNoResponse(repeatPrompt);

        } while (repeat);

        //Success
        return true;
    }
}
