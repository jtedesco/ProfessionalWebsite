package cs242.illinois.edu.command;

import cs242.illinois.edu.graph.Graph;

/**
 * <p> Created By: Jon Tedesco
 * <p> Date: Sep 28, 2010
 *
 * <p> <p> Data object that holds a constant representing the action and a label for the action
 */
public class FlightGraphCommandBean {

    /**
     * A <code>LabelConstants</code> that describes the specific action
     */
    private String label;

    /**
     * A command object, representing the actual command to call
     */
    private FlightGraphCommand command;

    /**
     * Constructor for this object
     * @param label The label for this action
     * @param command The command for this action
     */
    public FlightGraphCommandBean(String label, FlightGraphCommand command) {
        this.label = label;
        this.command = command;
    }

    /**
     * Gets the label that describes the specific action
     *
     * @return The label
     */
    public String getLabel() {
        return label;
    }

    /**
     * Sets the label that describes the specific action
     *
     * @param label The label
     */
    public void setLabel(String label) {
        this.label = label;
    }

    /**
     * Gets the command that identifies the specific action
     *
     * @return The command
     */
    public FlightGraphCommand getCommand() {
        return command;
    }

    /**
     * Sets the command that identifies the specific action
     *
     * @param command The command
     */
    public void setId(FlightGraphCommand command) {
        this.command = command;
    }

    /**
     * Executes the associated command object
     *
     * @param graph The graph
     */
    public void execute(Graph graph) {
        command.execute(graph);
    }

    /**
     * Checks the equality of this object with a given object
     *
     * @param otherObject The object with which to compare this object
     * @return Whether the objects are equal
     */
    @Override
    public boolean equals(Object otherObject) {
        if (this == otherObject) return true;
        if (!(otherObject instanceof FlightGraphCommandBean)) return false;

        FlightGraphCommandBean that = (FlightGraphCommandBean) otherObject;

        if (label != null ? !label.equals(that.label) : that.label != null) return false;

        return true;
    }

     /**
     * Generates a hash code for this ordered pair
     *
     * @return The generated hash code
     */
    @Override
    public int hashCode() {
        int result = label != null ? label.hashCode() : 0;
        result = 31 * result + (command != null ? command.hashCode() : 0);
        return result;
    }}
