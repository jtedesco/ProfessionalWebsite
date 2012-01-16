package edu.illinois.cs242.controller;

import edu.illinois.cs242.model.CompressionConstants;
import edu.illinois.cs242.view.ImageWindow;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;


/**
 * <p> Created By: Jon Tedesco
 * <p> Date: Sep 23, 2010

 * <p> <p> Enum that allows us to hash a key, to a specific <code>ActionListener</code>.
 *
 * NOTE: I took some of the effects code from CS125, some from http://www.javaworld.com/javaworld/jw-09-1998/jw-09-media.html?page=3
 *
 * @see java.awt.event.ActionListener
 * @see edu.illinois.cs242.controller.ButtonConstants
 *
 */

public enum AlgorithmsMenuActions {

    //Algorithm menu
    //Action of selecting the RLE compression algorithm
    rleButton {
        @Override
        ActionListener getActionListener() {
            return new ActionListener() {
             public void actionPerformed(ActionEvent ae) {
                   ImageWindow.setSelectedCompressionAlgorithm(CompressionConstants.RLE);
                   ImageWindow.getIndexedColorAlgorithmItem().setSelected(false);
               }
            };
        }
    },

    //Action of selecting the Indexed Color compression algorithm
    indexedColorButton {
        @Override
        ActionListener getActionListener() {
            return new ActionListener() {
                public void actionPerformed(ActionEvent ae) {
                    ImageWindow.setSelectedCompressionAlgorithm(CompressionConstants.INDEXED_COLOR);
                    ImageWindow.getRleAlgorithmItem().setSelected(false);
                }
            };
        }
    },

    //Action of selecting the no zip compression algorithm
    noZipButton {
        @Override
        ActionListener getActionListener() {
            return new ActionListener() {
                public void actionPerformed(ActionEvent ae) {
                    ImageWindow.setExtraCompressionAlgorithm(CompressionConstants.NONE);
                    ImageWindow.getNoZipAlgorithmItem().setSelected(true);
                    ImageWindow.getZipAlgorithmItem().setSelected(false);
                    ImageWindow.getGzipAlgorithmItem().setSelected(false);
                    ImageWindow.getBzipAlgorithmItem().setSelected(false);
                }
            };
        }
    },

    //Action of selecting the zip compression algorithm
    zipButton {
        @Override
        ActionListener getActionListener() {
            return new ActionListener() {
                public void actionPerformed(ActionEvent ae) {
                    ImageWindow.setExtraCompressionAlgorithm(CompressionConstants.ZIP);
                    ImageWindow.getNoZipAlgorithmItem().setSelected(false);
                    ImageWindow.getZipAlgorithmItem().setSelected(true);
                    ImageWindow.getGzipAlgorithmItem().setSelected(false);
                    ImageWindow.getBzipAlgorithmItem().setSelected(false);
                }
            };
        }
    },
    //Action of selecting the gzip compression algorithm
    gzipButton {
        @Override
        ActionListener getActionListener() {
            return new ActionListener() {
                public void actionPerformed(ActionEvent ae) {
                    ImageWindow.setExtraCompressionAlgorithm(CompressionConstants.GZIP);
                    ImageWindow.getNoZipAlgorithmItem().setSelected(false);
                    ImageWindow.getZipAlgorithmItem().setSelected(false);
                    ImageWindow.getGzipAlgorithmItem().setSelected(true);
                    ImageWindow.getBzipAlgorithmItem().setSelected(false);
                }
            };
        }
    },

    //Action of selecting the bzip compression algorithm
    bzipButton {
        @Override
        ActionListener getActionListener() {
            return new ActionListener() {
                public void actionPerformed(ActionEvent ae) {
                    ImageWindow.setExtraCompressionAlgorithm(CompressionConstants.BZIP);
                    ImageWindow.getNoZipAlgorithmItem().setSelected(false);
                    ImageWindow.getZipAlgorithmItem().setSelected(false);
                    ImageWindow.getGzipAlgorithmItem().setSelected(false);
                    ImageWindow.getBzipAlgorithmItem().setSelected(true);
                }
            };
        }
    };
    /**
     * Abstract method that we overload in each enum to create the actual <code>ActionListener</code> object.
     * @return An action listener that will be applied to a menu item.
     */
    abstract ActionListener getActionListener();

}
