package cs242.illinois.edu.command;

import cs242.illinois.edu.graph.FlightGraphVertex;
import cs242.illinois.edu.graph.Graph;
import cs242.illinois.edu.utilities.ConsoleUtilities;
import cs242.illinois.edu.utilities.FlightGraphUtilities;
import cs242.illinois.edu.utilities.TextIO;

import java.util.ArrayList;
import java.util.List;

/**
 * <p> Created By: Jon Tedesco
 * <p> Date: Sep 30, 2010
 *
 * <p> <p> A command to be executed on the flight graph
 */
public class ListContinentsAndCitiesCommand extends FlightGraphCommand {

    /**
     * {@inheritDoc}
     *
     * Lists the continents represented in the graph and all cities in each continent
     */
    public boolean execute(Graph graph) {

        //Error-check
         if(!super.execute(graph))
                 return false;

        //Grab a list of all the cities in the graph
        List<FlightGraphVertex> cities = FlightGraphUtilities.getFlightGraphVertices(graph);

        //Ask if the user would like to see all city data for each city, or just the city name and airport code
        String fullDataPrompt = "Would you like to see full city data for each city? (y/n)";
        boolean displayFullData = ConsoleUtilities.promptYesNoResponse(fullDataPrompt);

        //Gather a list of the unique continents
        List<String> continents = new ArrayList<String>();
        for(FlightGraphVertex city : cities){
            if(!(continents.contains(city.getContinent()))){
                continents.add(city.getContinent());
            }
        }

        //Print out a list of all continents represented, and each city in each continent
        for(String continent : continents){
            TextIO.putln(continent);
            TextIO.putln("~~~~~~~~~~~~~~~~~~~~~~~~\n");
            for(FlightGraphVertex city : cities){

                //Display the actual data for each city
                if(city.getContinent().equals(continent) && displayFullData){
                    TextIO.putln(city.toString());
                } else if(city.getContinent().equals(continent)) {
                    TextIO.putln(city.getName() + " (" + city.getAirportCode() + ")");
                }
            }
            TextIO.putln();
        }

        //Success
        return true;
    }
}
