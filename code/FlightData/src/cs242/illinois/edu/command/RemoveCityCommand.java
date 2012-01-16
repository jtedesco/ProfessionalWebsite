package cs242.illinois.edu.command;

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
public class RemoveCityCommand extends FlightGraphCommand {

    /**
     * {@inheritDoc}
     *
     * Removes a specific city from the graph
     * @param graph
     */
    public boolean execute(Graph graph) {

        //Error-check
         if(!super.execute(graph))
                 return false;

        //Ask the user for
        int selection = 0;
        List<FlightGraphVertex> cities;
        do {

            //Retrieve the graph data
            cities = FlightGraphUtilities.getFlightGraphVertices(graph);

            //List the cities and wait for user to select an input
            TextIO.putln("Please select the airport from below:\n");
            ConsoleUtilities.listNumberedCities(cities);
            TextIO.putln(cities.size() + ") Return to the previous menu");

            //Parse the input
            String prompt = "";
            String errorMessage = "Sorry, that is not a valid city!";
            selection = ConsoleUtilities.getIntegerFromInput(prompt, errorMessage);

            //Error handle
            if (selection > cities.size() || selection < 0) {
                TextIO.putln("Sorry, invalid selection. Please try again");
            } else if (selection != cities.size()) {

                //Pull out the city the user is interested in
                FlightGraphVertex selectedCity = cities.get(selection);

                //Remove the selected city
                try {
                    graph.removeVertex(selectedCity);

                } catch (GraphException e) {
                    TextIO.putln("Error removing city from graph!");
                }
            }

            String answer = "";
            if (selection != cities.size()) {
                //Would you like to search again?
                TextIO.putln("Would like to remove another city? (y/n)");
                answer = TextIO.getln();
            }

            if (!(answer.toLowerCase().equals("y") || answer.toLowerCase().equals("yes"))) {
                selection = cities.size();
            }
        } while (selection != cities.size());

        //Success
        return true;
    }
}
