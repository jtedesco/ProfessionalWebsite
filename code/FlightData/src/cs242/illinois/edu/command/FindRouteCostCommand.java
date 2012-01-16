package cs242.illinois.edu.command;

import cs242.illinois.edu.graph.FlightGraphEdge;
import cs242.illinois.edu.graph.FlightGraphVertex;
import cs242.illinois.edu.graph.Graph;
import cs242.illinois.edu.graph.GraphException;
import cs242.illinois.edu.utilities.ConsoleUtilities;
import cs242.illinois.edu.utilities.Dijkstra;
import cs242.illinois.edu.utilities.FlightGraphUtilities;
import cs242.illinois.edu.utilities.TextIO;

import java.util.List;

/**
 * <p> Created By: Jon Tedesco
 * <p> Date: Oct 8, 2010
 *
 * <p> <p> Finds the total distance between two cities
 */
public class FindRouteCostCommand extends FlightGraphCommand{

    /**
     * Implements the command to find the total cost to fly two cities
     *
     * @param graph The graph on which to execute this command
     * @return success?
     */
    public boolean execute(Graph graph) {

        //Error-check
        if(!super.execute(graph)){
            return false;
        }

        boolean repeat;
        do {

            //Grab the cities of the graph
            List<FlightGraphVertex> cities = FlightGraphUtilities.getFlightGraphVertices(graph);

            //Display a list, with name and codes only
            ConsoleUtilities.listNumberedCities(cities);

            //Prompt the user for source city for the flight
            String sourceCitySelectionPrompt = "From what city would you like to find the route?";
            String cityErrorMessage = "Not a valid city selection!";
            int from = ConsoleUtilities.getIntegerFromInput(sourceCitySelectionPrompt, cityErrorMessage);

            //Prompt the user for destination city for the flight
            String destinationCitySelectionPrompt = "At what city should the route arrive?";
            TextIO.putln(destinationCitySelectionPrompt);
            int to = ConsoleUtilities.getIntegerFromInput(sourceCitySelectionPrompt, cityErrorMessage);

            //Grab the cities from the graph
            FlightGraphVertex source = cities.get(from);
            FlightGraphVertex target = cities.get(to);

            //Get the distance between these two cities
            double totalCost = 0;
            try {
                totalCost = findRouteCost(source, target, graph);
            } catch (Exception e) {
                TextIO.putln("Error tracing path!");
                return false;
            }

            //Output the total distance of the route
            TextIO.put("The total cost to fly between these two cities is:\n");
            TextIO.putf("    %.2f %s\n\n", totalCost, "dollars");

            //Would you like to search again?
            String repeatPrompt = "Would like to find another route cost? (y/n)";
            repeat = ConsoleUtilities.promptYesNoResponse(repeatPrompt);

        } while (repeat);

        //Success
        return true;
    }

    /**
     * Helper function to find the cost to fly between two cities
     *
     * @param source The source vertex
     * @param target The target vertex
     * @param graph The graph
     * @return The total distance
     */
    public double findRouteCost(FlightGraphVertex source, FlightGraphVertex target, Graph graph) throws Exception {
        //Perform Dijkstra's
        List<FlightGraphVertex> path = Dijkstra.findShortestPath(source, target, graph);

        //Find the total distance
        double cost = 0;
        double costPerMile = 0.35;
        for(int current = 1; current<path.size(); current++){

            //Find the edge connecting this city and the next
            FlightGraphEdge currentFlight = null;
            try {
                currentFlight = (FlightGraphEdge) graph.getEdge(path.get(current-1), path.get(current));
            } catch (GraphException e) {
                throw new Exception();
            }

            //Add this to our running total
            cost += (currentFlight.getLength()*costPerMile);
            if(costPerMile > 0){
                costPerMile -= 0.05;
            }
        }

        //Return the total distance
        return cost;
    }
}
