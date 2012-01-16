package cs242.illinois.edu;

import cs242.illinois.edu.command.FlightGraphCommandBean;
import cs242.illinois.edu.command.FlightGraphCommandFactory;
import cs242.illinois.edu.graph.DirectedGraph;
import cs242.illinois.edu.graph.Graph;
import cs242.illinois.edu.utilities.LabelConstants;
import cs242.illinois.edu.utilities.TextIO;

import java.util.List;

/**
 * <p> Created By: Jon Tedesco
 * <p> Date: Sep 27, 2010
 *
 * <p> <p> Controls the I/O for the cs242.illinois.edu.command line for this program
 */
public class CSAirConsole {

    private static Graph graph = new DirectedGraph();

    /**
     * The entry point for the flight data console user interface
     *
     * @param args The cs242.illinois.edu.command line arguments
     */
    public static void main(String[] args) {

        //Get the list of all available actions on our data
        List<FlightGraphCommandBean> commandList = FlightGraphCommandFactory.getCommands();

        int actionChoice = -1;
        do {

            //Prompt the user for input
            TextIO.putln(LabelConstants.ACTION_PROMPT);

            //Display the list of available actions
            for(int actionNumber = 0; actionNumber< commandList.size(); actionNumber++){
                TextIO.putln(LabelConstants.INDENT + (actionNumber+1) + ") " + commandList.get(actionNumber).getLabel());
            }
            TextIO.putln(LabelConstants.INDENT + (commandList.size()+1) + ") " + LabelConstants.EXIT);
            TextIO.putln();

            //Wait for user input, and keep looping until we get a valid choice
            do{
                try{
                    TextIO.readFile(null);
                    String input = TextIO.getln();
                    actionChoice = Integer.parseInt(input);
                } catch (Exception e) {
                    TextIO.putln();
                    TextIO.putln(LabelConstants.INVALID_CHOICE_MESSAGE);
                }

                //If the input is not valid, prompt for new input
                if((actionChoice-1)<0 || (actionChoice-1)> commandList.size()){
                    TextIO.putln();
                    TextIO.putln(LabelConstants.INVALID_CHOICE_MESSAGE);
                }

            } while ((actionChoice-1)<0 || (actionChoice-1)> commandList.size());

            if ((actionChoice-1)!= commandList.size()) {

                //Perform the action
                FlightGraphCommandBean commandBean = commandList.get(actionChoice-1);
                commandBean.execute(graph);

                //Prompt user to continue
                TextIO.putln(LabelConstants.CONTINUE_MESSAGE);
                TextIO.getln();
            }

        } while((actionChoice-1)!= commandList.size());
            
        //Print goodbye message
        TextIO.putln(LabelConstants.GOODBYE_MESSAGE);
    }

    public Graph getGraph() {
        return graph;
    }

    public void setGraph(Graph graph) {
        this.graph = graph;
    }
}
