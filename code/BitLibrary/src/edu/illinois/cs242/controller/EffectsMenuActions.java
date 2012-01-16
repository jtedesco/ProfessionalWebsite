package edu.illinois.cs242.controller;

import edu.illinois.cs242.effects.*;
import edu.illinois.cs242.effects.image.ScaleFilter;
import edu.illinois.cs242.model.ImageUtilities;
import edu.illinois.cs242.view.ImageWindow;
import edu.illinois.cs242.view.ResizeWindow;
import edu.illinois.cs242.view.RotateWindow;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.awt.image.BufferedImageOp;


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

public enum EffectsMenuActions {

    //Rotate and flip buttons
    //Action of rotating the current image left by 90 degrees
    rotateLeftButton {
        @Override
        ActionListener getActionListener() {
            return new ActionListener() {
                public void actionPerformed(ActionEvent ae) {

                    BufferedImage image = ImageWindow.getImage();
                    int[][] imageData = ImageUtilities.getRGBPixels(image);
                    int[][] rotatedImageData  = SimpleEffects.rotateLeft(imageData);
                    BufferedImage rotatedImage = ImageUtilities.setRGBPixels(null, rotatedImageData);

                    ImageWindow.openImage(rotatedImage, null, true);

                 }
            };
        }
    },

    //Action of rotating the current image right by 90 degrees
    rotateRightButton {
        @Override
        ActionListener getActionListener() {
            return new ActionListener() {
                public void actionPerformed(ActionEvent ae) {

                    BufferedImage image = ImageWindow.getImage();
                    int[][] imageData = ImageUtilities.getRGBPixels(image);
                    int[][] rotatedImageData  = SimpleEffects.rotateLeft(SimpleEffects.rotateLeft( SimpleEffects.rotateLeft(imageData)));
                    BufferedImage rotatedImage = ImageUtilities.setRGBPixels(null, rotatedImageData);

                    ImageWindow.openImage(rotatedImage, null, true);
                 }
            };
        }
    },

    //Action that displays the rotate dialog
    rotateCustomButton {
         @Override
         ActionListener getActionListener() {
             return new ActionListener() {
                 public void actionPerformed(ActionEvent ae) {

                     //Add contents to the window.
                     RotateWindow rotateWindow = new RotateWindow();
                     rotateWindow.initialize();
                     JFrame frame = rotateWindow.getFrame();

                     //Show
                     ImageWindow.setRotateDialog(rotateWindow);
                     rotateWindow.setVisible(true);
                     frame.setVisible(true);
                  }
             };
         }
     },

    //Action that reads the rotate data from the dialog and rotates the image accordingly
    okRotateButton {
         @Override
         ActionListener getActionListener() {
             return new ActionListener() {
                 public void actionPerformed(ActionEvent ae) {

                     //Get the rotate dialog and hide it
                     RotateWindow frame = ImageWindow.getRotateDialog();
                     frame.close();

                     //Get the degrees by which to rotate the image
                     double degrees = frame.getRotateInput();

                     //Rotate the image
                     BufferedImage image = ImageWindow.getImage();
                     BufferedImage rotatedImageWithTransparency = SimpleEffects.rotate(image, degrees);

                     //Open the image, but set the real image to be the unrotated one
                     ImageWindow.openImage(rotatedImageWithTransparency, null, true);
                     ImageWindow.setImage(image);

                  }
             };
         }
     },

    //Action that vertically flips the image
    flipVerticallyButton {
        @Override
        ActionListener getActionListener() {
            return new ActionListener() {
                public void actionPerformed(ActionEvent ae) {

                    BufferedImage image = ImageWindow.getImage();
                    int[][] imageData = ImageUtilities.getRGBPixels(image);
                    int[][] flippedImageData  = SimpleEffects.flip(imageData);
                    BufferedImage rotatedImage = ImageUtilities.setRGBPixels(null, flippedImageData);

                    ImageWindow.openImage(rotatedImage, null, true);
                }
            };
        }
    },

    //Action that horizontally flips the image
    flipHorizontallyButton {
        @Override
        ActionListener getActionListener() {
            return new ActionListener() {
                public void actionPerformed(ActionEvent ae) {

                    BufferedImage image = ImageWindow.getImage();
                    int[][] imageData = ImageUtilities.getRGBPixels(image);
                    int[][] mirroredImageData  = SimpleEffects.mirror(imageData);
                    BufferedImage rotatedImage = ImageUtilities.setRGBPixels(null, mirroredImageData);

                    ImageWindow.openImage(rotatedImage, null, true);
                 }
            };
        }
    },

    //Effects buttons
    //Action that opens the resize dialog
    resizeButton {
         @Override
         ActionListener getActionListener() {
             return new ActionListener() {
                 public void actionPerformed(ActionEvent ae) {

                     //Add contents to the window.
                     ResizeWindow resizeWindow = new ResizeWindow();
                     resizeWindow.initialize();
                     JFrame frame = resizeWindow.getFrame();

                     //Show
                     ImageWindow.setResizeDialog(resizeWindow);
                     resizeWindow.setVisible(true);
                     frame.setVisible(true);
                  }
             };
         }
     },

    //ACtion that reads the data from the resize dialog and resizes the image accordingly
    okResizeButton {
         @Override
         ActionListener getActionListener() {
             return new ActionListener() {
                 public void actionPerformed(ActionEvent ae) {

                     //Get the resize dialog and hide it
                     ResizeWindow frame = ImageWindow.getResizeDialog();
                     frame.close();

                     //Get the new width and height from the dialog
                     int newWidth = frame.getWidthInput();
                     int newHeight = frame.getHeightInput();

                     //Resize the image
                     BufferedImage image = ImageWindow.getImage();
                     BufferedImageOp scaleOperation = new ScaleFilter(newWidth, newHeight);
                     BufferedImage resizedImageWithTransparency = scaleOperation.filter(image, null);

                     //Remove transparency
                     ImageWindow.openImage(resizedImageWithTransparency, null, true);

                  }
             };
         }
     },

    //Action that blurs the image
    blurButton {
        @Override
        ActionListener getActionListener() {
            return new ActionListener() {
                public void actionPerformed(ActionEvent ae) {

                    //Get the image
                    BufferedImage image = ImageWindow.getImage();
                    BufferedImage blurredImage = BlurAndSharpenEffects.simpleBlur(image);


                    //Display the new image
                    ImageWindow.openImage(blurredImage, null, true);

                 }
            };
        }
    },

    //Action that performs the sharpen effect on the image
    sharpenButton {
        @Override
        ActionListener getActionListener() {
            return new ActionListener() {
                public void actionPerformed(ActionEvent ae) {

                    //Get the image
                    BufferedImage image = ImageWindow.getImage();
                    BufferedImage sharpenedImage = BlurAndSharpenEffects.sharpen(image);

                    //Display the modified image
                    ImageWindow.openImage(sharpenedImage, null, true);
                 }
            };
        }
    },

    //Action that performs the posterize effect on the image
    posterizeButton {
         @Override
         ActionListener getActionListener() {
             return new ActionListener() {
                 public void actionPerformed(ActionEvent ae) {

                     //Get the image
                     BufferedImage image = ImageWindow.getImage();
                     BufferedImage posterizedImage = ArtisticEffects.posterize(image);

                     //Display the modified image
                     ImageWindow.openImage(posterizedImage, null, true);

                  }
             };
         }
     },

    //Action that applies the 'chrome' filter to the current image
    chromeButton {
        @Override
        ActionListener getActionListener() {
            return new ActionListener() {
                public void actionPerformed(ActionEvent ae) {

                    //Get the image
                    BufferedImage image = ImageWindow.getImage();
                    BufferedImage invertedImage = ArtisticEffects.chrome(image);

                    //Display the modified image
                    ImageWindow.openImage(invertedImage, null, true);

                }
            };
        }
    },

    //Action that applies the 'halftone' filter to the current image
    halftoneButton {
        @Override
        ActionListener getActionListener() {
            return new ActionListener() {
                public void actionPerformed(ActionEvent ae) {

                    //Get the image
                    BufferedImage image = ImageWindow.getImage();
                    BufferedImage invertedImage = ArtisticEffects.halftone(image);

                    //Display the modified image
                    ImageWindow.openImage(invertedImage, null, true);

                }
            };
        }
    },

    //Action that applies the 'crystallize' filter to the current image
    crystallizeButton {
        @Override
        ActionListener getActionListener() {
            return new ActionListener() {
                public void actionPerformed(ActionEvent ae) {

                    //Get the image
                    BufferedImage image = ImageWindow.getImage();
                    BufferedImage invertedImage = ArtisticEffects.crystallize(image);

                    //Display the modified image
                    ImageWindow.openImage(invertedImage, null, true);

                }
            };
        }
    },

    //Action that applies the 'emboss' filter to the current image
    embossButton {
        @Override
        ActionListener getActionListener() {
            return new ActionListener() {
                public void actionPerformed(ActionEvent ae) {

                    //Get the image
                    BufferedImage image = ImageWindow.getImage();
                    BufferedImage invertedImage = ArtisticEffects.emboss(image);

                    //Display the modified image
                    ImageWindow.openImage(invertedImage, null, true);

                }
            };
        }
    },

    //Action that applies the 'feedback' filter to the current image
    feedbackButton {
        @Override
        ActionListener getActionListener() {
            return new ActionListener() {
                public void actionPerformed(ActionEvent ae) {

                    //Get the image
                    BufferedImage image = ImageWindow.getImage();
                    BufferedImage invertedImage = ArtisticEffects.feedback(image);

                    //Display the modified image
                    ImageWindow.openImage(invertedImage, null, true);

                }
            };
        }
    },

    //Action that applies the 'lights' filter to the current image
    lightsButton {
        @Override
        ActionListener getActionListener() {
            return new ActionListener() {
                public void actionPerformed(ActionEvent ae) {

                    //Get the image
                    BufferedImage image = ImageWindow.getImage();
                    BufferedImage invertedImage = ArtisticEffects.lights(image);

                    //Display the modified image
                    ImageWindow.openImage(invertedImage, null, true);

                }
            };
        }
    },

    //Action that applies the 'noise' filter to the current image
    noiseButton {
        @Override
        ActionListener getActionListener() {
            return new ActionListener() {
                public void actionPerformed(ActionEvent ae) {

                    //Get the image
                    BufferedImage image = ImageWindow.getImage();
                    BufferedImage modifiedImage = ArtisticEffects.noise(image);

                    //Display the modified image
                    ImageWindow.openImage(modifiedImage, null, true);

                }
            };
        }
    },

    //Action that applies the 'pointillize' filter to the current image
    pointillizeButton {
        @Override
        ActionListener getActionListener() {
            return new ActionListener() {
                public void actionPerformed(ActionEvent ae) {

                    //Get the image
                    BufferedImage image = ImageWindow.getImage();
                    BufferedImage modifiedImage = ArtisticEffects.pointillize(image);

                    //Display the modified image
                    ImageWindow.openImage(modifiedImage, null, true);

                }
            };
        }
    },

    //Action that applies the 'rubber stamp' filter to the current image
    rubberStampButton {
        @Override
        ActionListener getActionListener() {
            return new ActionListener() {
                public void actionPerformed(ActionEvent ae) {

                    //Get the image
                    BufferedImage image = ImageWindow.getImage();
                    BufferedImage modifiedImage = ArtisticEffects.rubberStamp(image);

                    //Display the modified image
                    ImageWindow.openImage(modifiedImage, null, true);

                }
            };
        }
    },

    //Action that applies the 'weave' filter to the current image
    weaveButton {
        @Override
        ActionListener getActionListener() {
            return new ActionListener() {
                public void actionPerformed(ActionEvent ae) {

                    //Get the image
                    BufferedImage image = ImageWindow.getImage();
                    BufferedImage modifiedImage = ArtisticEffects.weave(image);

                    //Display the modified image
                    ImageWindow.openImage(modifiedImage, null, true);

                }
            };
        }
    },

    //Action that applies the 'detect edges' filter to the current image
    detectEdgesButton {
        @Override
        ActionListener getActionListener() {
            return new ActionListener() {
                public void actionPerformed(ActionEvent ae) {

                    //Get the image
                    BufferedImage image = ImageWindow.getImage();
                    BufferedImage modifiedImage = ArtisticEffects.detectEdges(image);

                    //Display the modified image
                    ImageWindow.openImage(modifiedImage, null, true);

                }
            };
        }
    },

    //Action that converts the colors on the image
    invertButton {
        @Override
        ActionListener getActionListener() {
            return new ActionListener() {
                public void actionPerformed(ActionEvent ae) {

                    //Get the image
                    BufferedImage image = ImageWindow.getImage();
                    BufferedImage modifiedImage = ColorEffects.invert(image);

                    //Display the modified image
                    ImageWindow.openImage(modifiedImage, null, true);

                }
            };
        }
    },

    //Apply the 'box blur' filter to the image
    boxBlurButton {
        @Override
        ActionListener getActionListener() {
            return new ActionListener() {
                public void actionPerformed(ActionEvent ae) {

                    //Get the image
                    BufferedImage image = ImageWindow.getImage();
                    BufferedImage modifiedImage = BlurAndSharpenEffects.boxBlur(image);

                    //Display the modified image
                    ImageWindow.openImage(modifiedImage, null, true);

                }
            };
        }
    },

    //Apply the 'gaussian blur' filter to the image
    gaussianBlurButton {
        @Override
        ActionListener getActionListener() {
            return new ActionListener() {
                public void actionPerformed(ActionEvent ae) {

                    //Get the image
                    BufferedImage image = ImageWindow.getImage();
                    BufferedImage modifiedImage = BlurAndSharpenEffects.gaussianBlur(image);

                    //Display the modified image
                    ImageWindow.openImage(modifiedImage, null, true);

                }
            };
        }
    },

    //Apply the 'high pass' filter to the image
    highPassFilterButton {
        @Override
        ActionListener getActionListener() {
            return new ActionListener() {
                public void actionPerformed(ActionEvent ae) {

                    //Get the image
                    BufferedImage image = ImageWindow.getImage();
                    BufferedImage modifiedImage = BlurAndSharpenEffects.highPass(image);

                    //Display the modified image
                    ImageWindow.openImage(modifiedImage, null, true);

                }
            };
        }
    },

    //Apply the 'lens blur' filter to the image
    lensBlurButton {
        @Override
        ActionListener getActionListener() {
            return new ActionListener() {
                public void actionPerformed(ActionEvent ae) {

                    //Get the image
                    BufferedImage image = ImageWindow.getImage();
                    BufferedImage modifiedImage = BlurAndSharpenEffects.lensBlur(image);

                    //Display the modified image
                    ImageWindow.openImage(modifiedImage, null, true);

                }
            };
        }
    },

    //Apply the 'erode' filter to the image
    erodeButton {
        @Override
        ActionListener getActionListener() {
            return new ActionListener() {
                public void actionPerformed(ActionEvent ae) {

                    //Get the image
                    BufferedImage image = ImageWindow.getImage();
                    BufferedImage modifiedImage = BlurAndSharpenEffects.erode(image);

                    //Display the modified image
                    ImageWindow.openImage(modifiedImage, null, true);

                }
            };
        }
    },

    //Apply the 'dilate' filter to the image
    dilateButton {
        @Override
        ActionListener getActionListener() {
            return new ActionListener() {
                public void actionPerformed(ActionEvent ae) {

                    //Get the image
                    BufferedImage image = ImageWindow.getImage();
                    BufferedImage modifiedImage = BlurAndSharpenEffects.dilate(image);

                    //Display the modified image
                    ImageWindow.openImage(modifiedImage, null, true);

                }
            };
        }
    },

    //Apply the 'oil' filter to the image
    oilFilterButton {
        @Override
        ActionListener getActionListener() {
            return new ActionListener() {
                public void actionPerformed(ActionEvent ae) {

                    //Get the image
                    BufferedImage image = ImageWindow.getImage();
                    BufferedImage modifiedImage = BlurAndSharpenEffects.oil(image);

                    //Display the modified image
                    ImageWindow.openImage(modifiedImage, null, true);

                }
            };
        }
    },

    //Apply the 'rays' filter to the image
    raysFilterButton {
        @Override
        ActionListener getActionListener() {
            return new ActionListener() {
                public void actionPerformed(ActionEvent ae) {

                    //Get the image
                    BufferedImage image = ImageWindow.getImage();
                    BufferedImage modifiedImage = BlurAndSharpenEffects.rays(image);

                    //Display the modified image
                    ImageWindow.openImage(modifiedImage, null, true);

                }
            };
        }
    },

    //Apply the 'marble' filter to the image
    marbleButton {
        @Override
        ActionListener getActionListener() {
            return new ActionListener() {
                public void actionPerformed(ActionEvent ae) {

                    //Get the image
                    BufferedImage image = ImageWindow.getImage();
                    BufferedImage modifiedImage = DistortionEffects.marble(image);

                    //Display the modified image
                    ImageWindow.openImage(modifiedImage, null, true);

                }
            };
        }
    },

    //Apply the 'kaleidoscope' filter to the image
    kaleidoscopeButton {
        @Override
        ActionListener getActionListener() {
            return new ActionListener() {
                public void actionPerformed(ActionEvent ae) {

                    //Get the image
                    BufferedImage image = ImageWindow.getImage();
                    BufferedImage modifiedImage = DistortionEffects.kaleidoscope(image);

                    //Display the modified image
                    ImageWindow.openImage(modifiedImage, null, true);

                }
            };
        }
    },

    //Apply the 'ripple' filter to the image
    rippleButton {
        @Override
        ActionListener getActionListener() {
            return new ActionListener() {
                public void actionPerformed(ActionEvent ae) {

                    //Get the image
                    BufferedImage image = ImageWindow.getImage();
                    BufferedImage modifiedImage = DistortionEffects.ripple(image);

                    //Display the modified image
                    ImageWindow.openImage(modifiedImage, null, true);

                }
            };
        }
    },

    //Apply the 'lens' filter to the image
    lensButton {
        @Override
        ActionListener getActionListener() {
            return new ActionListener() {
                public void actionPerformed(ActionEvent ae) {

                    //Get the image
                    BufferedImage image = ImageWindow.getImage();
                    BufferedImage modifiedImage = DistortionEffects.lens(image);

                    //Display the modified image
                    ImageWindow.openImage(modifiedImage, null, true);

                }
            };
        }
    },

    //Apply the 'twirl' filter to the image
    twirlButton {
        @Override
        ActionListener getActionListener() {
            return new ActionListener() {
                public void actionPerformed(ActionEvent ae) {

                    //Get the image
                    BufferedImage image = ImageWindow.getImage();
                    BufferedImage modifiedImage = DistortionEffects.twirl(image);

                    //Display the modified image
                    ImageWindow.openImage(modifiedImage, null, true);

                }
            };
        }
    },

    //Apply the 'warp' filter to the image
    warpButton {
        @Override
        ActionListener getActionListener() {
            return new ActionListener() {
                public void actionPerformed(ActionEvent ae) {

                    //Get the image
                    BufferedImage image = ImageWindow.getImage();
                    BufferedImage modifiedImage = DistortionEffects.warp(image);

                    //Display the modified image
                    ImageWindow.openImage(modifiedImage, null, true);

                }
            };
        }
    },

    //Apply the 'water' filter to the image
    waterButton {
        @Override
        ActionListener getActionListener() {
            return new ActionListener() {
                public void actionPerformed(ActionEvent ae) {

                    //Get the image
                    BufferedImage image = ImageWindow.getImage();
                    BufferedImage modifiedImage = DistortionEffects.water(image);

                    //Display the modified image
                    ImageWindow.openImage(modifiedImage, null, true);

                }
            };
        }
    },

    //Apply the 'dither' filter to the image
    ditherButton {
        @Override
        ActionListener getActionListener() {
            return new ActionListener() {
                public void actionPerformed(ActionEvent ae) {

                    //Get the image
                    BufferedImage image = ImageWindow.getImage();
                    BufferedImage modifiedImage = ColorEffects.dither(image);

                    //Display the modified image
                    ImageWindow.openImage(modifiedImage, null, true);

                }
            };
        }
    },

    //Apply the 'diffuse' filter to the image
    diffuseButton {
        @Override
        ActionListener getActionListener() {
            return new ActionListener() {
                public void actionPerformed(ActionEvent ae) {

                    //Get the image
                    BufferedImage image = ImageWindow.getImage();
                    BufferedImage modifiedImage = DistortionEffects.diffuse(image);

                    //Display the modified image
                    ImageWindow.openImage(modifiedImage, null, true);

                }
            };
        }
    },

    //Apply the 'adjustment curves' filter to the image
    adjustmentCurvesButton {
        @Override
        ActionListener getActionListener() {
            return new ActionListener() {
                public void actionPerformed(ActionEvent ae) {

                    //Get the image
                    BufferedImage image = ImageWindow.getImage();
                    BufferedImage modifiedImage = ColorEffects.adjustmentCurves(image);

                    //Display the modified image
                    ImageWindow.openImage(modifiedImage, null, true);

                }
            };
        }
    },

    //Apply the 'tritone' filter to the image
    tritoneButton {
        @Override
        ActionListener getActionListener() {
            return new ActionListener() {
                public void actionPerformed(ActionEvent ae) {

                    //Get the image
                    BufferedImage image = ImageWindow.getImage();
                    BufferedImage modifiedImage = ColorEffects.tritone(image);

                    //Display the modified image
                    ImageWindow.openImage(modifiedImage, null, true);

                }
            };
        }
    },

    //Apply the 'mix channels' filter to the image
    mixChannelsButton {
        @Override
        ActionListener getActionListener() {
            return new ActionListener() {
                public void actionPerformed(ActionEvent ae) {

                    //Get the image
                    BufferedImage image = ImageWindow.getImage();
                    BufferedImage modifiedImage = ColorEffects.mixChannels(image);

                    //Display the modified image
                    ImageWindow.openImage(modifiedImage, null, true);

                }
            };
        }
    },

    //Apply the 'solarize' filter to the image
    solarizeButton {
        @Override
        ActionListener getActionListener() {
            return new ActionListener() {
                public void actionPerformed(ActionEvent ae) {

                    //Get the image
                    BufferedImage image = ImageWindow.getImage();
                    BufferedImage modifiedImage = ColorEffects.solarize(image);

                    //Display the modified image
                    ImageWindow.openImage(modifiedImage, null, true);

                }
            };
        }
    },

    //Apply the 'motion blur' filter to the image
    motionBlurButton {
        @Override
        ActionListener getActionListener() {
            return new ActionListener() {
                public void actionPerformed(ActionEvent ae) {

                    //Get the image
                    BufferedImage image = ImageWindow.getImage();
                    BufferedImage modifiedImage = BlurAndSharpenEffects.motionBlur(image);

                    //Display the modified image
                    ImageWindow.openImage(modifiedImage, null, true);

                }
            };
        }
    },

    //Apply the 'unsharpen' filter to the image
    unsharpenButton {
        @Override
        ActionListener getActionListener() {
            return new ActionListener() {
                public void actionPerformed(ActionEvent ae) {

                    //Get the image
                    BufferedImage image = ImageWindow.getImage();
                    BufferedImage modifiedImage = BlurAndSharpenEffects.unsharpen(image);

                    //Display the modified image
                    ImageWindow.openImage(modifiedImage, null, true);

                }
            };
        }
    },

    //Apply the 'smear' filter to the image
    smearButton {
        @Override
        ActionListener getActionListener() {
            return new ActionListener() {
                public void actionPerformed(ActionEvent ae) {

                    //Get the image
                    BufferedImage image = ImageWindow.getImage();
                    BufferedImage modifiedImage = BlurAndSharpenEffects.smear(image);

                    //Display the modified image
                    ImageWindow.openImage(modifiedImage, null, true);

                }
            };
        }
    },

    //Apply the 'quantize' filter to the image
    quantizeButton {
        @Override
        ActionListener getActionListener() {
            return new ActionListener() {
                public void actionPerformed(ActionEvent ae) {

                    //Get the image
                    BufferedImage image = ImageWindow.getImage();
                    BufferedImage modifiedImage = ColorEffects.quantize(image);

                    //Display the modified image
                    ImageWindow.openImage(modifiedImage, null, true);

                }
            };
        }
    },

    //Apply the 'quantize' filter to the image
    swimButton {
        @Override
        ActionListener getActionListener() {
            return new ActionListener() {
                public void actionPerformed(ActionEvent ae) {

                    //Get the image
                    BufferedImage image = ImageWindow.getImage();
                    BufferedImage modifiedImage = DistortionEffects.swim(image);

                    //Display the modified image
                    ImageWindow.openImage(modifiedImage, null, true);

                }
            };
        }
    },

    //Apply the 'gray' filter to the image
    grayButton {
        @Override
        ActionListener getActionListener() {
            return new ActionListener() {
                public void actionPerformed(ActionEvent ae) {

                    //Get the image
                    BufferedImage image = ImageWindow.getImage();
                    BufferedImage modifiedImage = ColorEffects.gray(image);

                    //Display the modified image
                    ImageWindow.openImage(modifiedImage, null, true);

                }
            };
        }
    },

    //Apply the 'sparkle' filter to the image
    sparkleButton {
        @Override
        ActionListener getActionListener() {
            return new ActionListener() {
                public void actionPerformed(ActionEvent ae) {

                    //Get the image
                    BufferedImage image = ImageWindow.getImage();
                    BufferedImage modifiedImage = BlurAndSharpenEffects.sparkle(image);

                    //Display the modified image
                    ImageWindow.openImage(modifiedImage, null, true);

                }
            };
        }
    },

    //Action that puts the image into grayscale
    grayscaleButton {
        @Override
        ActionListener getActionListener() {
            return new ActionListener() {
                public void actionPerformed(ActionEvent ae) {

                    //Get the image
                    BufferedImage image = ImageWindow.getImage();
                    int[][] imageData = ImageUtilities.getRGBPixels(image);

                    //Turn the image to grayscale
                    int[][] grayscaleImageData  = ColorEffects.toGrayscale(imageData);
                    BufferedImage grayscaleImage = ImageUtilities.setRGBPixels(null, grayscaleImageData);

                    //Display the image
                    ImageWindow.openImage(grayscaleImage, null, true);
                 }
            };
        }
    },

    //Action that removes the redeye in the image
    redeyeButton {
        @Override
        ActionListener getActionListener() {
            return new ActionListener() {
                public void actionPerformed(ActionEvent ae) {

                    //Get the image
                    BufferedImage image = ImageWindow.getImage();
                    int[][] imageData = ImageUtilities.getRGBPixels(image);

                    //Remove redeye in the image
                    int[][] adjustedImageData  = ColorEffects.redeye(imageData);
                    BufferedImage rotatedImage = ImageUtilities.setRGBPixels(null, adjustedImageData);

                    //Display the modified image
                    ImageWindow.openImage(rotatedImage, null, true);
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
