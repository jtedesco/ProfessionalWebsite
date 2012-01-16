package cs242.illinois.edu.command;

import cs242.illinois.edu.graph.FlightGraphVertex;
import cs242.illinois.edu.graph.Graph;
import cs242.illinois.edu.graph.GraphException;
import cs242.illinois.edu.graph.Pair;
import cs242.illinois.edu.utilities.ConsoleUtilities;
import cs242.illinois.edu.utilities.TextIO;

/**
 * <p> Created By: Jon Tedesco
 * <p> Date: Sep 30, 2010
 *
 * <p> <p> A command to be executed on the flight graph
 */
public class AddCityCommand extends FlightGraphCommand {

    /**
     * {@inheritDoc}
     *
     * Adds a specific city to the graph
     * @param graph
     */
    public boolean execute(Graph graph) {

        //Error-check
        if(!super.execute(graph))
                return false;

        //Ask the user for
        boolean repeat;
        do {

            //Prompt the user for the name of the city
            String cityNamePrompt = "What is the name of the city you would like to insert?";
            String name = ConsoleUtilities.getStringFromInput(cityNamePrompt);

            //Prompt the user for the airport code of the city
            String airportCode = ConsoleUtilities.getStringFromInput("What is the airport code of the city you would like to insert?");

            //Prompt the user for the country of the city
            String country = ConsoleUtilities.getStringFromInput("What is the country of the city you would like to insert?");

            //Prompt the user for the continent of the city
            String continent = ConsoleUtilities.getStringFromInput("What is the continent of the city you would like to insert?");

            //Prompt the user for the region code
            String regionPrompt = "What is the region code of the city you would like to insert?";
            String regionErrorMessage = "Sorry, that is not a valid region code!";
            int region = ConsoleUtilities.getIntegerFromInput(regionPrompt, regionErrorMessage);

            //Prompt the user for the timezone
            String timezonePrompt = "What is the timezone code of the city you would like to insert?";
            String timezoneErrorMessage = "Sorry, that is not a valid timezone!";
            int timezone = ConsoleUtilities.getIntegerFromInput(timezonePrompt, timezoneErrorMessage);

            //Prompt the user for the population
            String populationPrompt = "What is the population of the city you would like to insert?";
            String populationErrorMessage = "Sorry, that is not a valid population!";
            int population = ConsoleUtilities.getIntegerFromInput(populationPrompt, populationErrorMessage);

            //Prompt the user for the longitude
            String longitudePrompt = "Please enter the longitude of the city you would like to insert? (Formatted as C#, where C is a character, and # is a number)";
            String longitudeErrorMessage = "Sorry, that is not a valid input for longitude!";
            Pair<Character, Integer> longitude = ConsoleUtilities.getCoordinatesFromInput(longitudePrompt, longitudeErrorMessage);

            //Prompt the user for the latitude
            Pair<Character, Integer> latitude = ConsoleUtilities.getCoordinatesFromInput("Please enter the latitude of the city you would like to insert? (Formatted as C#, where C is a character, and # is a number)", "Sorry, that is not a valid input for latitude!");

            //Actually build and insert the new city
            FlightGraphVertex newCity = new FlightGraphVertex(airportCode, name, country, continent, timezone, longitude, latitude, population, region);
            try {
                graph.addVertex(newCity);
                TextIO.putln("Successfully added the city to the graph!");
            } catch (GraphException e) {
                TextIO.putln("Error adding the city to the graph!");
            }

            //Prompt for user to repeat
            repeat = ConsoleUtilities.promptYesNoResponse("Would like to add another city? (y/n)");

        } while (repeat);

        //Success
        return true;
    }
}
