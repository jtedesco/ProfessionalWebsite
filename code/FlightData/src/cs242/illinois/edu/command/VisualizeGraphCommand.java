package cs242.illinois.edu.command;

import cs242.illinois.edu.graph.*;
import cs242.illinois.edu.utilities.FileMenuActions;
import cs242.illinois.edu.utilities.LabelConstants;
import cs242.illinois.edu.utilities.TextIO;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

/**
 * <p> Created By: Jon Tedesco
 * <p> Date: Sep 30, 2010
 *
 * <p> <p> A command to be executed on the flight graph
 */
public class VisualizeGraphCommand extends FlightGraphCommand{

    /**
     * The window for our program
     */
    private static JFrame frame;

    /**
     * URL to the map image from the internet
     */
    private static URL imageLink;

    /**
     * {@inheritDoc}
     *
     * <p>Parses an input <code>FlightGraph</code> into a URL to be submitted to the web for visualization via the API of
     * gcmap.com. Essentially, this function creates a list of vertices in the graph and generates the URL based off of
     * the airport codes of all <code>Vertices</code> in the <code>FlightGraph</code>.
     *
     * @param graph The input graph to parse for airports
     * @return The URL as a string
     */
    public boolean execute(Graph graph) {

        //Error-check
         if(!super.execute(graph))
                 return false;

        TextIO.putln("Would you like to open the map only?");
        String answer = TextIO.getln().trim().toLowerCase();

        String url = null;
        if("y".equals(answer) || "yes".equals(answer)){

            //Build the map URL
            url = "www.gcmap.com/map?P=";

        } else {

            //Build the full URL
            url  = "http://www.gcmap.com/mapui?P=";
        }

        List<Edge> edgeList = graph.getEdgeList();

        //For each edge in the list of edges
        for(Edge edge : edgeList){
            
            //Cast these vertices into the type we created
            Pair<Vertex, Vertex> currentVertexPair = edge.getVertices();
            FlightGraphVertex vertexOne = (FlightGraphVertex) currentVertexPair.getFirst();
            FlightGraphVertex vertexTwo = (FlightGraphVertex) currentVertexPair.getSecond();

            //Build the next part of the URL pattern
            url += (vertexOne.getAirportCode() + "-" + vertexTwo.getAirportCode() + ",");
        }

        //Strip off the ending comma
        url = url.substring(0, url.length()-1);

        if("y".equals(answer) || "yes".equals(answer)){

            //Open a window with the map in it
            url += "&MS=wls&MR=1800&MX=720x360&PM=*";
            openMap(url);

        } else {

            //Launch the URL
            launchUrl(url);
        }

        //Success
        return true;
    }

    /**
     * Desktop Browser
     * Description:
     *   Use Desktop library to launch user's
     *   default web browser (requires Java 6+)
     * http://www.centerkey.com/java/browser
     */
    private void launchUrl(String url) {
        try {
            java.awt.Desktop.getDesktop().browse(java.net.URI.create(url));
        }
        catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    /**
     * Opens the map to a specific URL
     *
     * @param url The URL to open
     */
   private void openMap(String url) {

       //Set look and feel
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {/*Ignore, just defaults to 'metal' look*/}

       //Initialize GUI components
        buildFrame(url);

       //Display it
       frame.pack();
       frame.setVisible(true);
      }

    /**
     * Builds the map window
     *
     * @param url The url
     */
    private void buildFrame(String url) {
        frame = new JFrame();
        ImageIcon imageIcon = null;
        try {
            imageIcon = createImageIcon(url, "The Flight Graph");
        } catch (MalformedURLException e) {
            TextIO.putln("Failed! Threw an exception!");
        }
        JLabel imageLabel = new JLabel(imageIcon);
        JMenuBar menuBar = new JMenuBar();
        frame.setJMenuBar(menuBar);

        //Add buttons and actions to gui
        JMenu fileMenu = new JMenu(LabelConstants.FILE);
        menuBar.add(fileMenu);
        JMenuItem exitItem = new JMenuItem(LabelConstants.EXIT);
        JMenuItem saveItem = new JMenuItem(LabelConstants.SAVE_IMAGE);
        fileMenu.add(saveItem);
        fileMenu.add(exitItem);
        exitItem.addActionListener(FileMenuActions.exitButton.getListener());
        saveItem.addActionListener(FileMenuActions.saveImageButton.getListener());

        //Piece together GUI
        frame.setTitle("Simple Browser");
        frame.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
        frame.getContentPane().add(imageLabel);
        frame.setResizable(false);
    }

    /**
     * Creates an ImageIcon if the path is valid.
     * This method taken from http://download.oracle.com/javase/tutorial/uiswing/examples/components/IconDemoProject/src/components/IconDemoApp.java
     *
     * @param path - resource path
     * @param description - description of the file
     */
    private ImageIcon createImageIcon(String path, String description) throws MalformedURLException {
        imageLink = new URL("HTTP", path.substring(0, path.indexOf("/")), path.substring(path.indexOf("/")));
        if (imageLink != null) {
            return new ImageIcon(imageLink, description);
        } else {
            System.err.println("Couldn't find file: " + path);
            return null;
        }
    }

    /* attempts to save the image */
    public static void saveImage(File file)
            throws IOException {
        BufferedImage image = ImageIO.read(imageLink);
        ImageIO.write(image, "jpg", file);
    }

    /**
     * Grabs the frame
     *
     * @return The frame
     */
    public static JFrame getFrame() {
        return frame;
    }
}
