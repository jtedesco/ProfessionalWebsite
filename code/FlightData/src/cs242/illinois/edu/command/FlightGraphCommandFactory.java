package cs242.illinois.edu.command;

import cs242.illinois.edu.utilities.LabelConstants;

import java.util.ArrayList;
import java.util.List;

/**
 * <p> Created By: Jon Tedesco
 * <p> Date: Sep 30, 2010
 *
 * <p> <p>
 */
public class FlightGraphCommandFactory {

    /**
     * Returns a list of the possible actions to perform on the <code>FlightGraph</code>. This method returns a list of
     * action titles and unique id numbers to help user interfaces map abstract possible actions to this class.
     *
     * @return A list of <code>FlightGraphCommandBean</code> objects representing descriptions and id numbers for each
     *          action this class know how to perform on a <code>FlightGraph</code>
     */
    public static List<FlightGraphCommandBean> getCommands() {

        //Create a new list to hold the list of
        List<FlightGraphCommandBean> commandList = new ArrayList<FlightGraphCommandBean>();

        //Add the actions to the list explicitly
        commandList.add(new FlightGraphCommandBean(LabelConstants.IMPORT_GRAPH, new CreateGraphFromJSONCommand()));
        commandList.add(new FlightGraphCommandBean(LabelConstants.EXPORT_GRAPH, new CreateJSONFromGraphCommand()));
        commandList.add(new FlightGraphCommandBean(LabelConstants.VISUALIZE_GRAPH, new VisualizeGraphCommand()));
        commandList.add(new FlightGraphCommandBean(LabelConstants.ADD_NEW_CITY_MESSAGE, new AddCityCommand()));
        commandList.add(new FlightGraphCommandBean(LabelConstants.ADD_NEW_FLIGHT_MESSAGE, new AddFlightCommand()));
        commandList.add(new FlightGraphCommandBean(LabelConstants.REMOVE_CITY_MESSAGE, new RemoveCityCommand()));
        commandList.add(new FlightGraphCommandBean(LabelConstants.REMOVE_FLIGHT_MESSAGE, new RemoveFlightCommand()));
        commandList.add(new FlightGraphCommandBean(LabelConstants.FIND_CITY_DATA_MESSAGE, new FindCityDataCommand()));
        commandList.add(new FlightGraphCommandBean(LabelConstants.LIST_CITIES_MESSAGE, new ListCitiesCommand()));
        commandList.add(new FlightGraphCommandBean(LabelConstants.LIST_CONTINENTS_AND_CITIES_MESSAGE, new ListContinentsAndCitiesCommand()));
        commandList.add(new FlightGraphCommandBean(LabelConstants.FIND_SHORTEST_FLIGHT_MESSAGE, new FindShortestFlightCommand()));
        commandList.add(new FlightGraphCommandBean(LabelConstants.FIND_SMALLEST_CITY_MESSAGE, new FindSmallestCityCommand()));
        commandList.add(new FlightGraphCommandBean(LabelConstants.FIND_AVERAGE_FLIGHT_LENGTH_MESSAGE, new FindAverageFlightLengthCommand()));
        commandList.add(new FlightGraphCommandBean(LabelConstants.FIND_AVERAGE_CITY_SIZE_MESSAGE, new FindAverageCitySizeCommand()));
        commandList.add(new FlightGraphCommandBean(LabelConstants.FIND_LONGEST_FLIGHT_MESSAGE, new FindLongestFlightCommand()));
        commandList.add(new FlightGraphCommandBean(LabelConstants.FIND_LARGEST_CITY_MESSAGE, new FindLargestCityCommand()));
        commandList.add(new FlightGraphCommandBean(LabelConstants.FIND_HUB_CITIES_MESSAGE, new FindHubCitiesCommand()));
        commandList.add(new FlightGraphCommandBean(LabelConstants.SEARCH_FLIGHTS_MESSAGE, new SearchFlightsCommand()));
        commandList.add(new FlightGraphCommandBean(LabelConstants.SEARCH_CITIES_MESSAGE, new SearchCitiesCommand()));
        commandList.add(new FlightGraphCommandBean(LabelConstants.FIND_ROUTE_LENGTH_MESSAGE, new FindRouteDistanceCommand()));
        commandList.add(new FlightGraphCommandBean(LabelConstants.FIND_PRICE_OF_ROUTE_MESSAGE, new FindRouteCostCommand()));
        commandList.add(new FlightGraphCommandBean(LabelConstants.FIND_TIME_OF_ROUTE_MESSAGE, new FindRouteTimeCommand()));
        commandList.add(new FlightGraphCommandBean(LabelConstants.EDIT_CITY_MESSAGE, new EditCityCommand()));

        return commandList;
    }
}
