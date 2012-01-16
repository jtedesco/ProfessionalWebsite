package cs242.illinois.edu.command;

import cs242.illinois.edu.graph.FlightGraphVertex;
import cs242.illinois.edu.graph.Graph;
import cs242.illinois.edu.graph.GraphException;
import cs242.illinois.edu.graph.Pair;
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
public class EditCityCommand extends FlightGraphCommand {

    /**
     * {@inheritDoc}
     *
     * Adds a specific city from the graph
     * @param graph
     */
    public boolean execute(Graph graph) {

        //Error-check
         if(!super.execute(graph))
                 return false;

        //Get the city to be edited
        FlightGraphVertex cityToBeEdited = getCityToBeEdited(graph);

        //Ask the user for
        boolean repeat = true;
        do {

            //Prompt the user for the name of the city
            String cityNamePrompt = "What is the new name of the city? (Type 'same' to leave it unchanged)";
            String name = ConsoleUtilities.getStringFromInput(cityNamePrompt);

            //Prompt the user for the airport code of the city
            String airportCodePrompt = "What is the new airport code of the city? (Type 'same' to leave it unchanged)";
            String airportCode = ConsoleUtilities.getStringFromInput(airportCodePrompt);

            //Prompt the user for the country of the city
            String countryPrompt = "What is the new country of the city? (Type 'same' to leave it unchanged)";
            String country =ConsoleUtilities.getStringFromInput(countryPrompt);

            //Prompt the user for the continent of the city
            String continentPrompt = "What is the new continent of the city? (Type 'same' to leave it unchanged)";
            String continent = ConsoleUtilities.getStringFromInput(continentPrompt);

            //Prompt the user for the region code
            String regionCodePrompt = "What is the new region code of the city?  (Type '-1' to leave it unchanged)";
            String regionCodeError = "Sorry, that is not a valid region code!";
            int region = ConsoleUtilities.getIntegerFromInput(regionCodePrompt, regionCodeError);

            //Prompt the user for the region code
            String timezonePrompt = "What is the new timezone code of the city?  (Type '-1' to leave it unchanged)";
            String timezoneError = "Sorry, that is not a valid timezone!";
            int timezone = ConsoleUtilities.getIntegerFromInput(timezonePrompt, timezoneError);

            //Prompt the user for the region code
            String populationPrompt = "What is the new population of the city?  (Type '-1' to leave it unchanged)";
            String populationError = "Sorry, that is not a valid population!";
            int population = ConsoleUtilities.getIntegerFromInput(populationPrompt, populationError);

            //Prompt the user for the longitude
            String longitudePrompt = "Please enter the new longitude of the city. (Formatted as C#, where C is a character, and # is a number) (Type 'N0' to leave it unchanged)";
            String longitudeError = "Sorry, that is not a valid input for longitude!";
            Pair<Character, Integer> longitude = ConsoleUtilities.getCoordinatesFromInput(longitudePrompt, longitudeError);

            //Prompt the user for the latitude
            String latitudePrompt = "Please enter the new latitude of the city. (Formatted as C#, where C is a character, and # is a number) (Type 'N0' to leave it unchanged)";
            String latitudeError = "Sorry, that is not a valid input for latitude!";
            Pair<Character, Integer> latitude = ConsoleUtilities.getCoordinatesFromInput(latitudePrompt, longitudeError);

            //Actually update the city data
            if(!airportCode.equals("same")){
                cityToBeEdited.setAirportCode(airportCode);
            }
            if(!name.equals("same")){
                cityToBeEdited.setName(name);
            }
            if(!country.equals("same")){
                cityToBeEdited.setCountry(country);
            }
            if(!continent.equals("same")){
                cityToBeEdited.setContinent(continent);
            }
            if(region!=-1){
                cityToBeEdited.setRegion(region);
            }
            if(timezone!=-1){
                cityToBeEdited.setTimezone(timezone);
            }
            if(population!=-1){
                cityToBeEdited.setPopulation(population);
            }
            if(!longitude.equals(new Pair('N', 0))){
                cityToBeEdited.setLongitude(longitude);
            }
            if(!latitude.equals(new Pair('N', 0))){
                cityToBeEdited.setLatitude(latitude);
            }

            //Would you like to search again?
            String repeatPrompt = "Would like to edit another city? (y/n)";
            repeat = ConsoleUtilities.promptYesNoResponse(repeatPrompt);
        } while (repeat);

        //Success
        return true;
    }

    private FlightGraphVertex getCityToBeEdited(Graph graph) {

        //Ask the user for
        List<FlightGraphVertex> cities = FlightGraphUtilities.getFlightGraphVertices(graph);

        //List the cities and wait for user to select an input
        ConsoleUtilities.listNumberedCities(cities);

        //Get user input
        boolean successful = true;
        int selection = 0;
        do{
            if(selection<0 || selection>cities.size()){
                TextIO.putln("Sorry, that is a valid choice!");
            }
            selection = ConsoleUtilities.getIntegerFromInput("Select a city from above:", "Sorry, that is not a valid input!");
        } while (selection<0 || selection>cities.size());

        //Pull out the city the user is interested in
        FlightGraphVertex selectedCity = cities.get(selection);

        //Remove the selected city
        try {
            graph.removeVertex(selectedCity);

        } catch (GraphException e) {
            TextIO.putln("Error removing city from graph!");
        }

        return selectedCity;
    }
}
