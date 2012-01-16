package cs242.illinois.edu.graph;

/**
 * <p> Created By: Jon Tedesco
 * <p> Date: Sep 29, 2010
 * <p/>
 * <p> <p>
 */
public class GraphException extends Exception{

    /**
	 *
	 */
	private static final long serialVersionUID = 0;
	private Throwable cause;

    /**
     * Constructs a JSONException with an explanatory message.
     * @param message Detail about the reason for the exception.
     */
    public GraphException(String message) {
        super(message);
    }

    public GraphException(Throwable t) {
        super(t.getMessage());
        this.cause = t;
    }

    public Throwable getCause() {
        return this.cause;
    }

    @Override
    public String getMessage() {
        return "Graph exception: " + super.getMessage();
    }
}
