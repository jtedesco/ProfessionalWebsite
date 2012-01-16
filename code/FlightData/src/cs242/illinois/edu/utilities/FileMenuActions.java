package cs242.illinois.edu.utilities;

import cs242.illinois.edu.command.VisualizeGraphCommand;

import javax.swing.*;
import javax.swing.filechooser.FileFilter;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;


/**
 * <p> Created By: Jon Tedesco
 * <p> Date: Sep 23, 2010

 * <p> <p> Enum that allows us to hash a key, to a specific <code>ActionListener</code>.
 *
 * NOTE: I took some of the effects code from CS125, some from http://www.javaworld.com/javaworld/jw-09-1998/jw-09-media.html?page=3
 *
 * @see java.awt.event.ActionListener
 * @see ButtonConstants
 *
 */

public enum FileMenuActions {

    //Action of the current image to disk as an uncompressed image
    saveImageButton {
        @Override
        ActionListener getActionListener() {
            return new ActionListener() {
                public void actionPerformed(ActionEvent ae) {

                    //Setup
                    JFileChooser chooser = buildImageFileChooser();
                    JFrame frame = VisualizeGraphCommand.getFrame();

                    //Open dialog
                    if (chooser.showSaveDialog(frame) == JFileChooser.APPROVE_OPTION){

                        //Get the file name to be saved
                        File imageToBeSaved = chooser.getSelectedFile();

                        //Save the file to disk
                        try {
                            VisualizeGraphCommand.saveImage(imageToBeSaved);
                        } catch (IOException e) {
                            JOptionPane.showMessageDialog(null, "Error saving the image to disk!\n" + e.getMessage());
                        }
                    }
                }
    		};
        }
    },


    //Action of exiting the program
    exitButton {
        @Override
        ActionListener getActionListener() {
            return new ActionListener() {
    			public void actionPerformed(ActionEvent ae) {
                    JFrame frame = VisualizeGraphCommand.getFrame();
	    			frame.setVisible(false);
		    		frame.dispose();
			    	System.exit(0);
			    }
		    };
        }
    };

    private static JFileChooser buildImageFileChooser() {
        JFileChooser chooser = new JFileChooser();

        //Set the file type filter on the JFileChooser depending on the algorithm we're using
        FileFilter filter = null;
        filter = new ImageFileFilter();
        chooser.setFileFilter(filter);
        chooser.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
        return chooser;
    }

    /**
     * Abstract method that we overload in each enum to create the actual <code>ActionListener</code> object.
     * @return An action listener that will be applied to a menu item.
     */
    abstract ActionListener getActionListener();

    public ActionListener getListener(){
        return getActionListener();
    }

}
