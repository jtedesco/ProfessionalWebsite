package cs242.illinois.edu.command;

import cs242.illinois.edu.utilities.LabelConstants;
import junit.framework.TestCase;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

/**
 * <p> Created By: Jon Tedesco
 * <p> Date: Sep 30, 2010
 *
 * <p> <p> Tests building the commands from factory
 */
public class FlightGraphCommandFactoryTest extends TestCase {

    /**
     * Test that the list of commands is correct
     */
    @Test
    public void testGetCommands() {

        //Setup
        List<FlightGraphCommandBean> expectedCommands = new ArrayList<FlightGraphCommandBean>();
        expectedCommands.add(new FlightGraphCommandBean(LabelConstants.IMPORT_GRAPH, new CreateGraphFromJSONCommand()));
        expectedCommands.add(new FlightGraphCommandBean(LabelConstants.EXPORT_GRAPH, new CreateJSONFromGraphCommand()));
        expectedCommands.add(new FlightGraphCommandBean(LabelConstants.VISUALIZE_GRAPH, new VisualizeGraphCommand()));
        expectedCommands.add(new FlightGraphCommandBean(LabelConstants.ADD_NEW_CITY_MESSAGE, new AddCityCommand()));
        expectedCommands.add(new FlightGraphCommandBean(LabelConstants.ADD_NEW_FLIGHT_MESSAGE, new AddFlightCommand()));
        expectedCommands.add(new FlightGraphCommandBean(LabelConstants.REMOVE_CITY_MESSAGE, new RemoveCityCommand()));
        expectedCommands.add(new FlightGraphCommandBean(LabelConstants.REMOVE_FLIGHT_MESSAGE, new RemoveFlightCommand()));
        expectedCommands.add(new FlightGraphCommandBean(LabelConstants.FIND_CITY_DATA_MESSAGE, new FindCityDataCommand()));
        expectedCommands.add(new FlightGraphCommandBean(LabelConstants.LIST_CITIES_MESSAGE, new ListCitiesCommand()));
        expectedCommands.add(new FlightGraphCommandBean(LabelConstants.LIST_CONTINENTS_AND_CITIES_MESSAGE, new ListContinentsAndCitiesCommand()));
        expectedCommands.add(new FlightGraphCommandBean(LabelConstants.FIND_SHORTEST_FLIGHT_MESSAGE, new FindShortestFlightCommand()));
        expectedCommands.add(new FlightGraphCommandBean(LabelConstants.FIND_SMALLEST_CITY_MESSAGE, new FindSmallestCityCommand()));
        expectedCommands.add(new FlightGraphCommandBean(LabelConstants.FIND_AVERAGE_FLIGHT_LENGTH_MESSAGE, new FindAverageFlightLengthCommand()));
        expectedCommands.add(new FlightGraphCommandBean(LabelConstants.FIND_AVERAGE_CITY_SIZE_MESSAGE, new FindAverageCitySizeCommand()));
        expectedCommands.add(new FlightGraphCommandBean(LabelConstants.FIND_LONGEST_FLIGHT_MESSAGE, new FindLongestFlightCommand()));
        expectedCommands.add(new FlightGraphCommandBean(LabelConstants.FIND_LARGEST_CITY_MESSAGE, new FindLargestCityCommand()));
        expectedCommands.add(new FlightGraphCommandBean(LabelConstants.FIND_HUB_CITIES_MESSAGE, new FindHubCitiesCommand()));
        expectedCommands.add(new FlightGraphCommandBean(LabelConstants.SEARCH_FLIGHTS_MESSAGE, new SearchFlightsCommand()));
        expectedCommands.add(new FlightGraphCommandBean(LabelConstants.SEARCH_CITIES_MESSAGE, new SearchCitiesCommand()));
        expectedCommands.add(new FlightGraphCommandBean(LabelConstants.FIND_ROUTE_LENGTH_MESSAGE, new FindRouteDistanceCommand()));
        expectedCommands.add(new FlightGraphCommandBean(LabelConstants.FIND_PRICE_OF_ROUTE_MESSAGE, new FindRouteCostCommand()));
        expectedCommands.add(new FlightGraphCommandBean(LabelConstants.FIND_TIME_OF_ROUTE_MESSAGE, new FindRouteTimeCommand()));
        expectedCommands.add(new FlightGraphCommandBean(LabelConstants.EDIT_CITY_MESSAGE, new EditCityCommand()));


        //Test
        List<FlightGraphCommandBean> commands = FlightGraphCommandFactory.getCommands();

        //Verify
        assertNotNull(commands);
        assertEquals(23, commands.size());
        for(FlightGraphCommandBean command : commands){
            assertTrue(expectedCommands.contains(command));
        }
    }

    /**
     * Tests that we correctly build the commands with the factory
     */
    @Test
    public void testGetGraphCommand() {

        //Setup
        List<FlightGraphCommandBean> commands = FlightGraphCommandFactory.getCommands();
        List<FlightGraphCommandBean> expectedCommands = new ArrayList<FlightGraphCommandBean>();
        expectedCommands.add(new FlightGraphCommandBean(LabelConstants.IMPORT_GRAPH, new CreateGraphFromJSONCommand()));
        expectedCommands.add(new FlightGraphCommandBean(LabelConstants.EXPORT_GRAPH, new CreateJSONFromGraphCommand()));
        expectedCommands.add(new FlightGraphCommandBean(LabelConstants.VISUALIZE_GRAPH, new VisualizeGraphCommand()));
        expectedCommands.add(new FlightGraphCommandBean(LabelConstants.ADD_NEW_CITY_MESSAGE, new AddCityCommand()));
        expectedCommands.add(new FlightGraphCommandBean(LabelConstants.ADD_NEW_FLIGHT_MESSAGE, new AddFlightCommand()));
        expectedCommands.add(new FlightGraphCommandBean(LabelConstants.REMOVE_CITY_MESSAGE, new RemoveCityCommand()));
        expectedCommands.add(new FlightGraphCommandBean(LabelConstants.REMOVE_FLIGHT_MESSAGE, new RemoveFlightCommand()));
        expectedCommands.add(new FlightGraphCommandBean(LabelConstants.FIND_CITY_DATA_MESSAGE, new FindCityDataCommand()));
        expectedCommands.add(new FlightGraphCommandBean(LabelConstants.LIST_CITIES_MESSAGE, new ListCitiesCommand()));
        expectedCommands.add(new FlightGraphCommandBean(LabelConstants.LIST_CONTINENTS_AND_CITIES_MESSAGE, new ListContinentsAndCitiesCommand()));
        expectedCommands.add(new FlightGraphCommandBean(LabelConstants.FIND_SHORTEST_FLIGHT_MESSAGE, new FindShortestFlightCommand()));
        expectedCommands.add(new FlightGraphCommandBean(LabelConstants.FIND_SMALLEST_CITY_MESSAGE, new FindSmallestCityCommand()));
        expectedCommands.add(new FlightGraphCommandBean(LabelConstants.FIND_AVERAGE_FLIGHT_LENGTH_MESSAGE, new FindAverageFlightLengthCommand()));
        expectedCommands.add(new FlightGraphCommandBean(LabelConstants.FIND_AVERAGE_CITY_SIZE_MESSAGE, new FindAverageCitySizeCommand()));
        expectedCommands.add(new FlightGraphCommandBean(LabelConstants.FIND_LONGEST_FLIGHT_MESSAGE, new FindLongestFlightCommand()));
        expectedCommands.add(new FlightGraphCommandBean(LabelConstants.FIND_LARGEST_CITY_MESSAGE, new FindLargestCityCommand()));
        expectedCommands.add(new FlightGraphCommandBean(LabelConstants.FIND_HUB_CITIES_MESSAGE, new FindHubCitiesCommand()));
        expectedCommands.add(new FlightGraphCommandBean(LabelConstants.SEARCH_FLIGHTS_MESSAGE, new SearchFlightsCommand()));
        expectedCommands.add(new FlightGraphCommandBean(LabelConstants.SEARCH_CITIES_MESSAGE, new SearchCitiesCommand()));
        expectedCommands.add(new FlightGraphCommandBean(LabelConstants.FIND_ROUTE_LENGTH_MESSAGE, new FindRouteDistanceCommand()));
        expectedCommands.add(new FlightGraphCommandBean(LabelConstants.FIND_PRICE_OF_ROUTE_MESSAGE, new FindRouteCostCommand()));
        expectedCommands.add(new FlightGraphCommandBean(LabelConstants.FIND_TIME_OF_ROUTE_MESSAGE, new FindRouteTimeCommand()));
        expectedCommands.add(new FlightGraphCommandBean(LabelConstants.EDIT_CITY_MESSAGE, new EditCityCommand()));

        //Test & Verify
        assertEquals(23, commands.size());
        for(int i = 0; i<expectedCommands.size(); i++){
            assertEquals(expectedCommands.get(i).getLabel(), commands.get(i).getLabel());
        }
    }
}

