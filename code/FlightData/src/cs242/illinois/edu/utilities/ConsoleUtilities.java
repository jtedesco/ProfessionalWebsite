package cs242.illinois.edu.utilities;

import cs242.illinois.edu.graph.FlightGraphVertex;
import cs242.illinois.edu.graph.Pair;

import java.util.List;

/**
 * <p> Created By: Jon Tedesco
 * <p> Date: Oct 8, 2010
 *
 * <p> <p> Holds common actions that interact with the command line
 */
public class ConsoleUtilities {

    /**
     * Gets an integer from the input source (presumably the command line)
     *
     * @param prompt The message to display/output before reading the value from the command line
     * @param errorMessage The error message to display if the input value is invalid
     * @return The integer input integer
     */
    public static int getIntegerFromInput(String prompt, String errorMessage) {

        //Output the prompt
        TextIO.putln(prompt);

        //Grab the integer from input
        int inputValue = 0;
        boolean successful = true;
        do{
            try{
                String input = TextIO.getln();
                inputValue = Integer.parseInt(input);
            } catch(Exception e){
                successful = false;
                TextIO.putln(errorMessage);
            }
        } while(!successful);
        return inputValue;
    }

    /**
     * Gets a coordinate object from the input source (presumably the command line)
     *
     * @param prompt The prompt to show before accepting input
     * @param errorMessage The error message to display if input is invalid
     * @return The input coordinate
     */
    public static Pair<Character, Integer> getCoordinatesFromInput(String prompt, String errorMessage) {
        boolean successful;
        TextIO.putln(prompt);
        Pair<Character, Integer> longitude = null;
        successful = true;
        do{
            try{
                //Parse the input to the longitude pair
                String input = TextIO.getln();
                char northSouth = input.charAt(0);
                String textNumber = input.substring(1);
                int degrees = Integer.parseInt(textNumber);

                //Create the object
                longitude = new Pair<Character, Integer>(northSouth, degrees);

            } catch (Exception e){
                TextIO.putln(errorMessage);
            }
        } while(!successful);
        return longitude;
    }

    /**
     * Gets a double from the input source
     *
     * @param prompt The prompt to display
     * @param errorMessage The error message to display
     * @return The double from input
     */
    public static double getDoubleFromInput(String prompt, String errorMessage) {
        double distance = 0;
        TextIO.putln(prompt);
        String input = TextIO.getln();
        distance = Double.parseDouble(input);
        if(distance<0){
            TextIO.putln("Taking input as zero!");
        }
        return distance;
    }

    /**
     * Helper function to prompt the user with a specific message to repeat
     *
     * @param prompt The message to display
     * @return Whether to repeat
     */
    public static boolean promptYesNoResponse(String prompt) {
        boolean repeat = true;

        //Would you like to search again?
        TextIO.putln(prompt);
        String answer = TextIO.getln();
        if (!(answer.toLowerCase().equals("y") || answer.toLowerCase().equals("yes"))) {
            repeat = false;
        }
        return repeat;
    }

    /**
     * Prompts user with a given message for an input string
     *
     * @param prompt The message to display
     * @return The input string
     */
    public static String getStringFromInput(String prompt) {
        TextIO.putln(prompt);
        String name = TextIO.getln();
        return name;
    }

    /**
     * Lists the cities from a flight graph vertex list as a numbered list
     *
     * @param cities The cities to list
     */
    public static void listNumberedCities(List<FlightGraphVertex> cities) {
        for(int index = 0; index < cities.size(); index++){
            FlightGraphVertex city = cities.get(index);
            TextIO.putln(index + ") " + city.getName() + "(" + city.getAirportCode() +")");
        }
        TextIO.putln();
    }

    /**
     * Lists the cities from a flight graph vertex list as a unnumbered list
     *
     * @param cities The cities to list
     */
    public static void listUnnumberedCities(List<FlightGraphVertex> cities) {
        for(FlightGraphVertex city : cities){
            TextIO.putln(city.getName() + "(" + city.getAirportCode() +")");
        }
        TextIO.putln();
    }
}
