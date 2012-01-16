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
public class RemoveFlightCommand extends FlightGraphCommand {

    /**
     * {@inheritDoc}
     *
     * Removes a specific flight from the graph
     * @param graph
     */
    public boolean execute(Graph graph) {

        //Error-check
         if(!super.execute(graph))
                 return false;

        //Ask the user for
        int selection = 0;
        List<FlightGraphEdge> flights;
        do {

            //Retrieve the graph data
            flights = FlightGraphUtilities.getFlightGraphEdges(graph);

            //List the cities and wait for user to select an input
            TextIO.putln("Please select the flight from below:\n");
            for (int index = 0; index < flights.size(); index++) {
                FlightGraphVertex source = (FlightGraphVertex) flights.get(index).getSource();
                FlightGraphVertex target = (FlightGraphVertex) flights.get(index).getTarget();
                TextIO.putln(index + ") " + source.getName() + " (" + source.getAirportCode() + ") --> " + target.getName() + " (" + target.getAirportCode() + ")");
            }
            TextIO.putln(flights.size() + ") Return to the previous menu");

            //Parse the input
            String prompt = "";
            String errorMessage = "Sorry, that is not a valid flight!";
            selection = ConsoleUtilities.getIntegerFromInput(prompt, errorMessage);

            //Error handle
            if (selection > flights.size() || selection < 0) {
                TextIO.putln("Sorry, invalid selection. Please try again");
            } else if (selection != flights.size()) {

                //Pull out the city the user is interested in
                FlightGraphEdge selectedFlight = flights.get(selection);

                //Remove the selected city
                try {
                    graph.removeEdge(selectedFlight);
                } catch (GraphException e) {
                    TextIO.putln("Error removing flight from graph!");
                }
            }

            String answer = "";
            if (selection != flights.size()) {
                //Would you like to search again?
                TextIO.putln("Would like to remove another flight? (y/n)");
                answer = TextIO.getln();
            }

            if (!(answer.toLowerCase().equals("y") || answer.toLowerCase().equals("yes"))) {
                selection = flights.size();
            }
        } while (selection != flights.size());

        //Success
        return true;
    }
}
