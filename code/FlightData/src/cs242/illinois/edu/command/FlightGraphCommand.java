package cs242.illinois.edu.command;

import cs242.illinois.edu.graph.Graph;
import cs242.illinois.edu.utilities.LabelConstants;
import cs242.illinois.edu.utilities.TextIO;


/**
 * <p> Created By: Jon Tedesco
 * <p> Date: Sep 30, 2010
 *
 * <p> <p> An abstract cs242.illinois.edu.command to be executed on the flight graph
 */
public class FlightGraphCommand {

    /**
     * Executes this particular cs242.illinois.edu.command on the flight graph
     *
     * @param graph The graph on which to execute this command
     */
    public boolean execute(Graph graph){
        //Error Check
        if (graph == null || graph.getVertexList().size() == 0) {
            TextIO.putln(LabelConstants.UNINITIZIALIZED_GRAPH_ERROR_MESSAGE);
            return false;
        }
        return true;
    }
}
