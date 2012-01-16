package edu.illinois.cs242.controller;

import edu.illinois.cs242.model.*;
import edu.illinois.cs242.view.ImageFileFilter;
import edu.illinois.cs242.view.ImageWindow;
import edu.illinois.cs242.view.IndexedColorFileFilter;
import edu.illinois.cs242.view.RLEFileFilter;

import javax.swing.*;
import javax.swing.filechooser.FileFilter;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
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

    //The action of opening a standard image file and displaying it in the window
    openDecompressedImageButton {
        @Override
        ActionListener getActionListener() {
            return new ActionListener() {
                public void actionPerformed(ActionEvent ae) {

                    //Setup
                    JFileChooser chooser = buildImageFileChooser();
                    JFrame frame = ImageWindow.getFrame();

                    //Show dialog for opening image files
                    if (chooser.showOpenDialog(frame) == JFileChooser.APPROVE_OPTION) {
                        File selectedFile = chooser.getSelectedFile();
                        ImageWindow.openImageFile(selectedFile.getPath());
                        ImageWindow.setInputDirectory(selectedFile.getParent());
                        ImageWindow.setOriginalImage(ImageWindow.getImage());
                    }
                }
            };
        }
    },

    //Action associated with opening and decompressing the image file
    openCompressedImageButton {
        @Override
        ActionListener getActionListener() {
            return new ActionListener() {
                public void actionPerformed(ActionEvent ae) {

                    //Setup
                    JFrame frame = ImageWindow.getFrame();
                    JFileChooser chooser = buildCompressedImageFileChooser();

                    //Open the file dialog window
                    if (chooser.showOpenDialog(frame) == JFileChooser.APPROVE_OPTION) {
                        File compressedImage = chooser.getSelectedFile();
                        ImageWindow.setInputDirectory(compressedImage.getParent());

                        //Set the compression algorithm based on the file ending
                        String compressedImageName = compressedImage.getName();
                        int indexOfEnding = compressedImageName.indexOf('.');
                        String ending = compressedImageName.substring(indexOfEnding);

                        //Build data for decompressing the image and get a compression client
                        CompressionData data = new CompressionData();
                        data.setCompressionAlgorithm(ImageWindow.getSelectedCompressionAlgorithm());
                        CompressionClient client = CompressionClientFactory.buildCompressionClient(data);

                        //Decompress image
                        BufferedImage decompressedImage = null;
                        compressedImage = new File(compressedImage.getAbsolutePath());
                        try {
                            compressedImage.setReadable(true);
                            decompressedImage = ImageCompressor.decompressImage(compressedImage, client);
                        } catch (Exception e) {
                            JOptionPane.showMessageDialog(null, "Error decompressing the selected file!\n" + e.getMessage());
                        }

                        //Open decompressed image
                        ImageWindow.openImage(decompressedImage, compressedImage.getName(), true);

                        ImageWindow.setOriginalImage(ImageWindow.getImage());
                    }
                }
    		};
        }
    },

    //The action of saving the current image in compressed form using the selected compression algorithm
    compressImageButton {
        @Override
        ActionListener getActionListener() {
            return new ActionListener() {
                public void actionPerformed(ActionEvent ae) {

                    //Setup
                    JFrame frame = ImageWindow.getFrame();
                    JFileChooser chooser = buildCompressedImageFileChooser();

                    //Opeen file dialog
                    if (chooser.showSaveDialog(frame) == JFileChooser.APPROVE_OPTION){
                        BufferedImage image = ImageWindow.getImage();

                        //Get the file name to be saved and append the correct file extension
                        File compressedImage = chooser.getSelectedFile();
                        if (compressedImage.getName().indexOf('.')==-1){
                            if(ImageWindow.getSelectedCompressionAlgorithm().equals(CompressionConstants.RLE)){
                                compressedImage = new File(compressedImage.getAbsolutePath()+CompressionConstants.RLE_FILE_SUFFIX);
                            }else if(ImageWindow.getSelectedCompressionAlgorithm().equals(CompressionConstants.INDEXED_COLOR)){
                                compressedImage = new File(compressedImage.getAbsolutePath()+CompressionConstants.INDEXED_COLOR_FILE_SUFFIX);
                            }
                        }

                        //Delete and recreate the file if it exists and the user approoves overwriting the file
                        if(!compressedImage.exists()){
                            try {
                                compressedImage.createNewFile();
                            } catch (IOException e) {
                                JOptionPane.showMessageDialog(null, "Error creating compressed image file to be saved to disk!\n" + e.getMessage());
                            }
                        } else {
                            if(JOptionPane.showConfirmDialog(null, "This file already exists, would you like to replace it?") == JFileChooser.APPROVE_OPTION){
                                compressedImage.delete();
                                try {
                                    compressedImage.createNewFile();
                                } catch (IOException e) {
                                    JOptionPane.showMessageDialog(null, "Error overwriting file!\n" + e.getMessage());
                                }
                            }
                        }

                        //Build data for compressing the image and get a compression client
                        CompressionData data = new CompressionData();
                        data.setCompressionAlgorithm(ImageWindow.getSelectedCompressionAlgorithm());
                        CompressionClient client = CompressionClientFactory.buildCompressionClient(data);

                        //Compress the image
                        try {
                            ImageCompressor.compressImage(image, client, compressedImage);
                        } catch (Exception e) {
                            JOptionPane.showMessageDialog(null, "Error decompressing file!\n" + e.getMessage());
                        }
                    }
                }
            };
        }
    },

    //Action of the current image to disk as an uncompressed image
    decompressImageButton {
        @Override
        ActionListener getActionListener() {
            return new ActionListener() {
                public void actionPerformed(ActionEvent ae) {

                    //Setup
                    JFileChooser chooser = buildImageFileChooser();
                    JFrame frame = ImageWindow.getFrame();

                    //Open dialog
                    if (chooser.showSaveDialog(frame) == JFileChooser.APPROVE_OPTION){

                        //Get the file name to be saved
                        File imageToBeSaved = chooser.getSelectedFile();

                        //Save the file to disk
                        try {
                            ImageUtilities.saveImage(ImageWindow.getImage(), imageToBeSaved);
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
                    JFrame frame = ImageWindow.getFrame();
	    			frame.setVisible(false);
		    		frame.dispose();
			    	System.exit(0);
			    }
		    };
        }
    };

    private static JFileChooser buildCompressedImageFileChooser() {
        JFileChooser chooser = new JFileChooser();
        chooser.setCurrentDirectory(new File(ImageWindow.getInputDirectory()));

        //Set the file type filter on the JFileChooser depending on the algorithm we're using
        FileFilter filter = null;
        if(ImageWindow.getSelectedCompressionAlgorithm().equals(CompressionConstants.RLE)){
            filter = new RLEFileFilter();
        } else if(ImageWindow.getSelectedCompressionAlgorithm().equals(CompressionConstants.INDEXED_COLOR)){
            filter = new IndexedColorFileFilter();
        }
        chooser.setFileFilter(filter);
        chooser.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
        return chooser;
    }

    private static JFileChooser buildImageFileChooser() {
        JFileChooser chooser = new JFileChooser();
        chooser.setCurrentDirectory(new File(ImageWindow.getInputDirectory()));

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

}
