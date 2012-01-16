package edu.illinois.cs242.view;

import edu.illinois.cs242.controller.ButtonConstants;
import edu.illinois.cs242.controller.MenuActionFactory;
import edu.illinois.cs242.controller.MenuConstants;
import edu.illinois.cs242.controller.MenuFactory;
import edu.illinois.cs242.model.CompressionConstants;
import edu.illinois.cs242.model.ImageUtilities;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

/**
 * This is the main GUI file for the program. From here, the image window launches, and displays the splash screen
 *
 */
public class ImageWindow {

    /**
     * The currently selected compression algorithm
     */
    static String selectedCompressionAlgorithm = CompressionConstants.RLE;
    static String extraCompressionAlgorithm = CompressionConstants.NONE;

    /**
     * The path to the last selected directory
     */
    static String inputDirectory = ".";

    /**
     * The window for our program
     */
    private static JFrame frame;

    /**
     * A variable that holds the current image
     */
    private static BufferedImage image;

    /**
     * A variable that holds the last opened image (for reverting)
     */
    private static BufferedImage originalImage;

    /**
     * A double value representing the current scale of the open image
     */
    private static double imageScale = 1.00;

    /**
     * Dialog box to allow the user to resize the image
     */
    private static ResizeWindow resizeWindow;

    /**
     * Dialog box to allow the user to rotate the image
     */
    private static RotateWindow rotateWindow;
    
    /**
     * Variables to get a handle on menu radio buttons
     */
    static JMenuItem rleAlgorithmItem;
    static JMenuItem indexedColorAlgorithmItem;
    static JMenuItem zipAlgorithmItem;
    static JMenuItem gzipAlgorithmItem;
    static JMenuItem bzipAlgorithmItem;
    static JMenuItem noZipAlgorithmItem;

    /**
	 * Add drop-down menus and menu items to the JFrame, the frame that the image will be displayed in.
	 * 
	 * @param frame
	 */
	public static void addMenus(final JFrame frame) {
		JMenuBar menubar = new JMenuBar();
		JMenu fileMenu = buildFileMenu();
		JMenu editMenu = buildEditMenu();
        JMenu effectsMenu = buildEffectsMenu();
        JMenu zoomMenu = buildZoomMenu();
        JMenu algorithmMenu = algorithmsMenu();
		menubar.add(fileMenu);
		menubar.add(editMenu);
		menubar.add(effectsMenu);
		menubar.add(zoomMenu);
		menubar.add(algorithmMenu);

		frame.setJMenuBar(menubar);
	}

    /**
     * A helper function to build the effects menu
     *
     * @return A JMenu that corresponds to the effects menu
     */
    private static JMenu buildEffectsMenu() {

        JMenu effectsMenu = new JMenu(LabelConstants.EFFECTS);

        //Effects options

            //Rotate sub-menu
            JMenu rotateMenu = new JMenu(LabelConstants.ROTATE);
            JMenuItem rotateLeftItem = new JMenuItem(LabelConstants.ROTATE_LEFT);
            JMenuItem rotateRightItem = new JMenuItem(LabelConstants.ROTATE_RIGHT);
            JMenuItem rotateCustomItem = new JMenuItem(LabelConstants.ROTATE_CUSTOM);
            rotateMenu.add(rotateLeftItem);
            rotateMenu.add(rotateRightItem);
            rotateMenu.add(rotateCustomItem);

            //Flip sub-menu
            JMenu flipMenu = new JMenu(LabelConstants.FLIP);
            JMenuItem flipVerticallyItem = new JMenuItem(LabelConstants.FLIP_VERTICALLY);
            JMenuItem flipHorizontallyItem = new JMenuItem(LabelConstants.FLIP_HORIZONTALLY);
            flipMenu.add(flipVerticallyItem);
            flipMenu.add(flipHorizontallyItem);

        //Color effects sub-menu
        JMenu colorEffectsMenu = new JMenu(LabelConstants.COLOR_EFFECTS);
            JMenuItem invertItem = new JMenuItem(LabelConstants.INVERT_COLORS);
            JMenuItem grayScaleItem = new JMenuItem(LabelConstants.GRAYSCALE);
            JMenuItem redEyeItem = new JMenuItem(LabelConstants.REMOVE_REDEYE);
            JMenuItem solarizeItem = new JMenuItem(LabelConstants.SOLARIZE);
            JMenuItem ditherItem = new JMenuItem(LabelConstants.DITHER);
            JMenuItem adjustmentCurvesItem = new JMenuItem(LabelConstants.ADJUSTMENT_CURVES_FILTER);
            JMenuItem tritoneItem = new JMenuItem(LabelConstants.TRITONE);
            JMenuItem mixChannelsItem = new JMenuItem(LabelConstants.MIX_CHANNELS);
            JMenuItem grayItem = new JMenuItem(LabelConstants.GRAY);
            JMenuItem quantizeItem = new JMenuItem(LabelConstants.QUANTIZE);
            colorEffectsMenu.add(invertItem);
            colorEffectsMenu.add(grayScaleItem);
            colorEffectsMenu.add(redEyeItem);
            colorEffectsMenu.add(ditherItem);
            colorEffectsMenu.add(adjustmentCurvesItem);
            colorEffectsMenu.add(solarizeItem);
            colorEffectsMenu.add(tritoneItem);
            colorEffectsMenu.add(mixChannelsItem);
            colorEffectsMenu.add(grayItem);
            colorEffectsMenu.add(quantizeItem);

        //Distortion and warping sub-menu
        JMenu distortionEffectsMenu = new JMenu(LabelConstants.DISTORTION_EFFECTS);
            JMenuItem marbleItem = new JMenuItem(LabelConstants.MARBLE);
            JMenuItem kaleidoscopeItem = new JMenuItem(LabelConstants.KALEIDOSCOPE);
            JMenuItem rippleItem = new JMenuItem(LabelConstants.RIPPLE);
            JMenuItem lensItem = new JMenuItem(LabelConstants.LENS);
            JMenuItem twirlItem = new JMenuItem(LabelConstants.TWIRL);
            JMenuItem waterItem = new JMenuItem(LabelConstants.WATER);
            JMenuItem warpItem = new JMenuItem(LabelConstants.WARP);
            JMenuItem diffuseItem = new JMenuItem(LabelConstants.DIFFUSE);
            JMenuItem swimItem = new JMenuItem(LabelConstants.SWIM);
            distortionEffectsMenu.add(marbleItem);
            distortionEffectsMenu.add(kaleidoscopeItem);
            distortionEffectsMenu.add(rippleItem);
            distortionEffectsMenu.add(lensItem);
            distortionEffectsMenu.add(twirlItem);
            distortionEffectsMenu.add(waterItem);
            distortionEffectsMenu.add(warpItem);
            distortionEffectsMenu.add(diffuseItem);
            distortionEffectsMenu.add(swimItem);

        //Artistic Effects
        JMenu artisticEffectsMenu = new JMenu(LabelConstants.ARTISTIC_EFFECTS);
            JMenuItem posterizeItem = new JMenuItem(LabelConstants.POSTERIZE);
            JMenuItem chromeItem = new JMenuItem(LabelConstants.CHROME_FILTER);
            JMenuItem halftoneItem = new JMenuItem(LabelConstants.HALFTONE_FILTER);
            JMenuItem crystallizeItem = new JMenuItem(LabelConstants.CRYSTALLIZE);
            JMenuItem embossItem = new JMenuItem(LabelConstants.EMBOSS);
            JMenuItem feedbackItem = new JMenuItem(LabelConstants.FEEDBACK_FILTER);
            JMenuItem lightsItem = new JMenuItem(LabelConstants.LIGHTS_FILTER);
            JMenuItem noiseItem = new JMenuItem(LabelConstants.NOISE_FILTER);
            JMenuItem pointillizeItem = new JMenuItem(LabelConstants.POINTILLIZE);
            JMenuItem rubberStampItem = new JMenuItem(LabelConstants.RUBBER_STAMP_FILTER);
            JMenuItem weaveItem = new JMenuItem(LabelConstants.WEAVE_FILTER);
            JMenuItem detectEdgesItem = new JMenuItem(LabelConstants.DETECT_EDGES);
            artisticEffectsMenu.add(posterizeItem);
            artisticEffectsMenu.add(chromeItem);
            artisticEffectsMenu.add(halftoneItem);
            artisticEffectsMenu.add(crystallizeItem);
            artisticEffectsMenu.add(embossItem);
            artisticEffectsMenu.add(feedbackItem);
            artisticEffectsMenu.add(lightsItem);
            artisticEffectsMenu.add(noiseItem);
            artisticEffectsMenu.add(pointillizeItem);
            artisticEffectsMenu.add(rubberStampItem);
            artisticEffectsMenu.add(weaveItem);
            artisticEffectsMenu.add(detectEdgesItem);

        //Blur and sharpen effects
        JMenu blurAndSharpenEffectsMenu = new JMenu(LabelConstants.BLUR_AND_SHARPEN_EFFECTS);
            JMenuItem blurItem = new JMenuItem(LabelConstants.BLUR);
            JMenuItem sharpenItem = new JMenuItem(LabelConstants.SHARPEN);
            JMenuItem boxBlurItem = new JMenuItem(LabelConstants.BOX_BLUR);
            JMenuItem gaussianBlurItem = new JMenuItem(LabelConstants.GAUSSIAN_BLUR);
            JMenuItem highPassFilterItem = new JMenuItem(LabelConstants.HIGH_PASS_FILTER);
            JMenuItem lensBlurItem = new JMenuItem(LabelConstants.LENS_BLUR);
            JMenuItem erodeItem = new JMenuItem(LabelConstants.ERODE);
            JMenuItem dilateItem = new JMenuItem(LabelConstants.DILATE);
            JMenuItem oilItem = new JMenuItem(LabelConstants.OIL_FILTER);
            JMenuItem raysItem = new JMenuItem(LabelConstants.RAYS_FILTER);
            JMenuItem motionBlurItem = new JMenuItem(LabelConstants.MOTION_BLUR);
            JMenuItem smearItem = new JMenuItem(LabelConstants.SMEAR);
            JMenuItem sparkleItem = new JMenuItem(LabelConstants.SPARKLE);
            JMenuItem unsharpenItem = new JMenuItem(LabelConstants.UNSHARPEN);
            blurAndSharpenEffectsMenu.add(blurItem);
            blurAndSharpenEffectsMenu.add(sharpenItem);
            blurAndSharpenEffectsMenu.add(boxBlurItem);
            blurAndSharpenEffectsMenu.add(gaussianBlurItem);
            blurAndSharpenEffectsMenu.add(highPassFilterItem);
            blurAndSharpenEffectsMenu.add(lensBlurItem);
            blurAndSharpenEffectsMenu.add(erodeItem);
            blurAndSharpenEffectsMenu.add(dilateItem);
            blurAndSharpenEffectsMenu.add(oilItem);
            blurAndSharpenEffectsMenu.add(raysItem);
            blurAndSharpenEffectsMenu.add(motionBlurItem);
            blurAndSharpenEffectsMenu.add(smearItem);
            blurAndSharpenEffectsMenu.add(sparkleItem);
            blurAndSharpenEffectsMenu.add(unsharpenItem);


        JMenuItem resizeItem = new JMenuItem(LabelConstants.RESIZE);

        //Add items to the menu
        effectsMenu.add(rotateMenu);
        effectsMenu.add(flipMenu);
        effectsMenu.add(colorEffectsMenu);
        effectsMenu.add(distortionEffectsMenu);
        effectsMenu.add(artisticEffectsMenu);
        effectsMenu.add(blurAndSharpenEffectsMenu);
        effectsMenu.add(resizeItem);

        //Get the proper menu action factory
        MenuActionFactory menuActionFactory = MenuFactory.buildMenuActionFactory(MenuConstants.EFFECTS_MENU);

        //Add action listeners
        rotateLeftItem.addActionListener(menuActionFactory.buildMenuAction(ButtonConstants.ROTATE_LEFT_BUTTON));
        rotateRightItem.addActionListener(menuActionFactory.buildMenuAction(ButtonConstants.ROTATE_RIGHT_BUTTON));
        rotateCustomItem.addActionListener(menuActionFactory.buildMenuAction(ButtonConstants.ROTATE_CUSTOM_BUTTON));
        flipVerticallyItem.addActionListener(menuActionFactory.buildMenuAction(ButtonConstants.FLIP_VERTICALLY_BUTTON));
        flipHorizontallyItem.addActionListener(menuActionFactory.buildMenuAction(ButtonConstants.FLIP_HORIZONTALLY_BUTTON));
        resizeItem.addActionListener(menuActionFactory.buildMenuAction(ButtonConstants.RESIZE_BUTTON));
        blurItem.addActionListener(menuActionFactory.buildMenuAction(ButtonConstants.BLUR_BUTTON));
        sharpenItem.addActionListener(menuActionFactory.buildMenuAction(ButtonConstants.SHARPEN_BUTTON));
        invertItem.addActionListener(menuActionFactory.buildMenuAction(ButtonConstants.INVERT_BUTTON));
        grayScaleItem.addActionListener(menuActionFactory.buildMenuAction(ButtonConstants.GRAYSCALE_BUTTON));
        redEyeItem.addActionListener(menuActionFactory.buildMenuAction(ButtonConstants.REDEYE_BUTTON));
        posterizeItem.addActionListener(menuActionFactory.buildMenuAction(ButtonConstants.POSTERIZE_BUTTON));
        chromeItem.addActionListener(menuActionFactory.buildMenuAction(ButtonConstants.CHROME_BUTTON));
        halftoneItem.addActionListener(menuActionFactory.buildMenuAction(ButtonConstants.HALFTONE_BUTTON));
        crystallizeItem.addActionListener(menuActionFactory.buildMenuAction(ButtonConstants.CRYSTALLIZE_BUTTON));
        embossItem.addActionListener(menuActionFactory.buildMenuAction(ButtonConstants.EMBOSS_BUTTON));
        feedbackItem.addActionListener(menuActionFactory.buildMenuAction(ButtonConstants.FEEDBACK_BUTTON));
        lightsItem.addActionListener(menuActionFactory.buildMenuAction(ButtonConstants.LIGHTS_BUTTON));
        noiseItem.addActionListener(menuActionFactory.buildMenuAction(ButtonConstants.NOISE_BUTTON));
        pointillizeItem.addActionListener(menuActionFactory.buildMenuAction(ButtonConstants.POINTILLIZE_BUTTON));
        rubberStampItem.addActionListener(menuActionFactory.buildMenuAction(ButtonConstants.RUBBER_STAMP_BUTTON));
        weaveItem.addActionListener(menuActionFactory.buildMenuAction(ButtonConstants.WEAVE_BUTTON));
        detectEdgesItem.addActionListener(menuActionFactory.buildMenuAction(ButtonConstants.DETECT_EDGES_BUTTON));
        boxBlurItem.addActionListener(menuActionFactory.buildMenuAction(ButtonConstants.BOX_BLUR_BUTTON));
        gaussianBlurItem.addActionListener(menuActionFactory.buildMenuAction(ButtonConstants.GAUSSIAN_BLUR_BUTTON));
        highPassFilterItem.addActionListener(menuActionFactory.buildMenuAction(ButtonConstants.HIGH_PASS_FILTER_BUTTON));
        lensBlurItem.addActionListener(menuActionFactory.buildMenuAction(ButtonConstants.LENS_BLUR_BUTTON));
        erodeItem.addActionListener(menuActionFactory.buildMenuAction(ButtonConstants.ERODE_BUTTON));
        dilateItem.addActionListener(menuActionFactory.buildMenuAction(ButtonConstants.DILATE_BUTTON));
        oilItem.addActionListener(menuActionFactory.buildMenuAction(ButtonConstants.OIL_FILTER_BUTTON));
        raysItem.addActionListener(menuActionFactory.buildMenuAction(ButtonConstants.RAYS_FILTER_BUTTON));
        ditherItem.addActionListener(menuActionFactory.buildMenuAction(ButtonConstants.DITHER_BUTTON));
        solarizeItem.addActionListener(menuActionFactory.buildMenuAction(ButtonConstants.SOLARIZE_BUTTON));
        adjustmentCurvesItem.addActionListener(menuActionFactory.buildMenuAction(ButtonConstants.ADJUSTMENT_CURVES_BUTTON));
        mixChannelsItem.addActionListener(menuActionFactory.buildMenuAction(ButtonConstants.MIX_CHANNELS_BUTTON));
        tritoneItem.addActionListener(menuActionFactory.buildMenuAction(ButtonConstants.TRITONE_BUTTON));
        marbleItem.addActionListener(menuActionFactory.buildMenuAction(ButtonConstants.MARBLE_BUTTON));
        kaleidoscopeItem.addActionListener(menuActionFactory.buildMenuAction(ButtonConstants.KALEIDOSCOPE_BUTTON));
        rippleItem.addActionListener(menuActionFactory.buildMenuAction(ButtonConstants.RIPPLE_BUTTON));
        lensItem.addActionListener(menuActionFactory.buildMenuAction(ButtonConstants.LENS_BUTTON));
        twirlItem.addActionListener(menuActionFactory.buildMenuAction(ButtonConstants.TWIRL_BUTTON));
        waterItem.addActionListener(menuActionFactory.buildMenuAction(ButtonConstants.WATER_BUTTON));
        motionBlurItem.addActionListener(menuActionFactory.buildMenuAction(ButtonConstants.MOTION_BLUR_BUTTON));
        smearItem.addActionListener(menuActionFactory.buildMenuAction(ButtonConstants.SMEAR_BUTTON));
        unsharpenItem.addActionListener(menuActionFactory.buildMenuAction(ButtonConstants.UNSHARPEN_BUTTON));
        sparkleItem.addActionListener(menuActionFactory.buildMenuAction(ButtonConstants.SPARKLE_BUTTON));
        warpItem.addActionListener(menuActionFactory.buildMenuAction(ButtonConstants.WARP_BUTTON));
        diffuseItem.addActionListener(menuActionFactory.buildMenuAction(ButtonConstants.DIFFUSE_BUTTON));
        grayItem.addActionListener(menuActionFactory.buildMenuAction(ButtonConstants.GRAY_BUTTON));
        quantizeItem.addActionListener(menuActionFactory.buildMenuAction(ButtonConstants.QUANTIZE_BUTTON));
        swimItem.addActionListener(menuActionFactory.buildMenuAction(ButtonConstants.SWIM_BUTTON));

        return effectsMenu;
    }

    /**
     * A helper function to build the zoom menu
     *
     * @return A JMenu that corresponds to the zoom menu
     */
    private static JMenu buildZoomMenu() {
        JMenu zoomMenu = new JMenu(LabelConstants.ZOOM);

        //Zoom options
        JMenuItem zoomInItem = new JMenuItem(LabelConstants.ZOOM_IN);
        JMenuItem zoomOutItem = new JMenuItem(LabelConstants.ZOOM_OUT);
        zoomMenu.add(zoomInItem);
        zoomMenu.add(zoomOutItem);

        //Add tooltips
        zoomInItem.setToolTipText("Hit \"+\" to zoom in");
        zoomOutItem.setToolTipText("Hit \"-\" to zoom out");

        //Get the proper menu action factory
        MenuActionFactory menuActionFactory = MenuFactory.buildMenuActionFactory(MenuConstants.ZOOM_MENU);

        /*
         * Zoom actions
         */
        zoomInItem.addActionListener(menuActionFactory.buildMenuAction(ButtonConstants.ZOOM_IN_BUTTON));
        zoomOutItem.addActionListener(menuActionFactory.buildMenuAction(ButtonConstants.ZOOM_OUT_BUTTON));

        return zoomMenu;
    }

    /**
     * A helper function to build the algorithm selection menu
     *
     * @return A JMenu that corresponds to the algorithm menu
     */
    private static JMenu algorithmsMenu() {
        JMenu algorithmMenu = new JMenu(LabelConstants.COMPRESSION_ALGORITHMS);
        JMenuItem menuBreak = new JMenuItem("~~~~~~~~~~~~~~~~~~~~~~~~");
        menuBreak.setEnabled(false);

        //Algorithms
        rleAlgorithmItem = new JRadioButtonMenuItem(LabelConstants.RLE, true);
        indexedColorAlgorithmItem = new JRadioButtonMenuItem(LabelConstants.INDEXED_COLOR);
        noZipAlgorithmItem = new JRadioButtonMenuItem(LabelConstants.NO_ZIP_COMPRESSION, true);
        zipAlgorithmItem = new JRadioButtonMenuItem(LabelConstants.USE_ZIP_COMPRESSION);
        gzipAlgorithmItem = new JRadioButtonMenuItem(LabelConstants.USE_GZIP_COMPRESSION);
        bzipAlgorithmItem = new JRadioButtonMenuItem(LabelConstants.USE_BZIP_COMPRESSION);
        algorithmMenu.add(rleAlgorithmItem);
        algorithmMenu.add(indexedColorAlgorithmItem);
        algorithmMenu.add(menuBreak);
        algorithmMenu.add(noZipAlgorithmItem);
        algorithmMenu.add(zipAlgorithmItem);
        algorithmMenu.add(gzipAlgorithmItem);
        algorithmMenu.add(bzipAlgorithmItem);

        //Get the proper menu action factory
        MenuActionFactory menuActionFactory = MenuFactory.buildMenuActionFactory(MenuConstants.ALGORITHMS_MENU);

        /*
         * Algorithm menu actions
         */
        rleAlgorithmItem.addActionListener(menuActionFactory.buildMenuAction(ButtonConstants.RLE_BUTTON));
        indexedColorAlgorithmItem.addActionListener(menuActionFactory.buildMenuAction(ButtonConstants.INDEXED_COLOR_BUTTON));
        noZipAlgorithmItem.addActionListener(menuActionFactory.buildMenuAction(ButtonConstants.NO_ZIP_BUTTON));
        zipAlgorithmItem.addActionListener(menuActionFactory.buildMenuAction(ButtonConstants.ZIP_BUTTON));
        gzipAlgorithmItem.addActionListener(menuActionFactory.buildMenuAction(ButtonConstants.GZIP_BUTTON));
        bzipAlgorithmItem.addActionListener(menuActionFactory.buildMenuAction(ButtonConstants.BZIP_BUTTON));

        return algorithmMenu;
    }

    /**
     * Helper function to build the edit menu
     *
     * @return A JMenu that corresponds to the edit menu
     */
    private static JMenu buildEditMenu() {
        JMenu editMenu = new JMenu(LabelConstants.EDIT);

        //Edit buttons
        JMenuItem copyItem = new JMenuItem(LabelConstants.COPY);
        JMenuItem pasteItem = new JMenuItem(LabelConstants.PASTE);
        JMenuItem captureScreenItem = new JMenuItem(LabelConstants.CAPTURE_SCREEN);
        JMenuItem revertItem = new JMenuItem(LabelConstants.REVERT);
        editMenu.add(revertItem);
        editMenu.add(copyItem);
		editMenu.add(pasteItem);
		editMenu.add(captureScreenItem);

        //Add tooltips to items with keyboard shortcuts
        revertItem.setToolTipText("Hit \"r\" to revert to the last saved image");
        copyItem.setToolTipText("Hit \"c\" to copy the current image");
        pasteItem.setToolTipText("Hit \"v\" to paste the current image in the clipboard");
        captureScreenItem.setToolTipText("Hit \"p\" or \"print scrn\" to capture the screen and open it as an image");

        //Get the proper menu action factory
        MenuActionFactory menuActionFactory = MenuFactory.buildMenuActionFactory(MenuConstants.EDIT_MENU);

        /*
         * Edit menu actions
         */
		revertItem.addActionListener(menuActionFactory.buildMenuAction(ButtonConstants.REVERT_BUTTON));
		captureScreenItem.addActionListener(menuActionFactory.buildMenuAction(ButtonConstants.CAPTURE_SCREEN_BUTTON));
		copyItem.addActionListener(menuActionFactory.buildMenuAction(ButtonConstants.COPY_BUTTON));
		pasteItem.addActionListener(menuActionFactory.buildMenuAction(ButtonConstants.PASTE_BUTTON));

        return editMenu;
    }

    /**
     * Helper function to build the file menu
     *
     * @return A JMenu that corresponds to the file menu
     */
    private static JMenu buildFileMenu() {
        JMenu fileMenu = new JMenu(LabelConstants.FILE);

        //File buttons
         JMenuItem quitItem = new JMenuItem(LabelConstants.EXIT);
         JMenuItem openImageItem = new JMenuItem(LabelConstants.OPEN_IMAGE);
         JMenuItem openCompressedImageItem = new JMenuItem(LabelConstants.OPEN_COMPRESSED_IMAGE);
         JMenuItem saveAsCompressedImageItem = new JMenuItem(LabelConstants.SAVE_AS_COMPRESSED_IMAGE);
         JMenuItem saveAsDecompressImageItem = new JMenuItem(LabelConstants.SAVE_AS_DECOMPRESSED_IMAGE);

             //'Save' sub-menu in File menu
             JMenu saveMenu = new JMenu(LabelConstants.SAVE);
             fileMenu.add(saveMenu);
             saveMenu.add(saveAsCompressedImageItem);
             saveMenu.add(saveAsDecompressImageItem);

             //'Open' sub-menu in File menu
             JMenu openMenu = new JMenu(LabelConstants.OPEN);
             fileMenu.add(openMenu);
             openMenu.add(openImageItem);
             openMenu.add(openCompressedImageItem);

        //Add tooltips to items with keyboard shortcuts
        openImageItem.setToolTipText("Hit \"o\" to open an image");
        saveAsDecompressImageItem.setToolTipText("Hit \"s\" to save an image");
        quitItem.setToolTipText("Hit \"q\" or \"esc\" to quit");

        fileMenu.add(quitItem);

        //Get the proper menu action factory
        MenuActionFactory menuActionFactory = MenuFactory.buildMenuActionFactory(MenuConstants.FILE_MENU);

        /*
         * File menu actions
         */
        quitItem.addActionListener(menuActionFactory.buildMenuAction(ButtonConstants.EXIT_BUTTON));
        saveAsCompressedImageItem.addActionListener(menuActionFactory.buildMenuAction(ButtonConstants.COMPRESS_IMAGE_BUTTON));
        saveAsDecompressImageItem.addActionListener(menuActionFactory.buildMenuAction(ButtonConstants.DECOMPRESS_IMAGE_BUTTON));
        openCompressedImageItem.addActionListener(menuActionFactory.buildMenuAction(ButtonConstants.OPEN_COMPRESSED_IMAGE_BUTTON));
        openImageItem.addActionListener(menuActionFactory.buildMenuAction(ButtonConstants.OPEN_DECOMPRESSED_IMAGE_BUTTON));

        return fileMenu;
    }


    /**
     * Opens a file and displays the image. This method is used by main and open
     * item.
     *
     * @param filename
     *            the file to be opened
     */
    public static void openImageFile(String filename) {
        // Delegate the problem of reading the filename to another method!
        BufferedImage img = ImageUtilities.loadImage(filename);
        if (img == null) {
            JOptionPane.showMessageDialog(null, filename
                    + " could not be opened.");
        } else
            openImage(img, filename, true); // create the window with a title - see
        // below
    }

    /**
     * Creates a window ('jframe') and performs the necessary voodoo to display
     * the window and a menu too.
     */
    public static void openImage(BufferedImage img, String title, boolean resize) {
        frame.setVisible(false);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        if(title != null)
            frame.setTitle(title);
        addMenus(frame);
        setImageForFrame(frame, img, resize);
        frame.pack(); // calculate its size
        frame.setVisible(true); // display it
        frame.setResizable(false);
    }

    public static void setImageForFrame(JFrame frame, BufferedImage img, boolean resize) {
        // We track the mapping between a frame and an image
        frame.getContentPane().removeAll();

        // remove the old image (if it exists)
        // wrap the image up as an ImageIcon inside a JLabel...
        JLabel label;
        if (img != null){
            //Store the full-sized image
            image = img;
            BufferedImage scaledImage = image;

            //Resize image (just to render it -- not the real image)
            int width = scaledImage.getWidth();
            int height = scaledImage.getHeight();
            int originalWidth = width;
            while(width>800 && height>600 && resize){
                width = (int) (width * 0.8);
                height = (int) (height * 0.8);
                scaledImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);

                // Paint scaled version of image to new image
                Graphics2D graphics2D = scaledImage.createGraphics();
                graphics2D.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
                graphics2D.drawImage(image, 0, 0, width, height, null);

                width = scaledImage.getWidth();
                height = scaledImage.getHeight();

                // clean up
                graphics2D.dispose();
            }
            setImageScale(width/(double)originalWidth);

            ImageIcon icon = new ImageIcon(scaledImage);
            label = new JLabel(icon);
        }
        else {
            label = new JLabel("No image");
        }
        // And add the label to the frame:
        frame.getContentPane().add(label, BorderLayout.CENTER);
        frame.pack(); // auto resize if the img has changed size
    }

    /**
     * A simple getter for the <code>selectedCompressionAlgorithm</code> variable.
     *
     * @return selectedCompressionAlgorithm A string constant representing the compression algorithm currently selected
     *                                      from the GUI
     */
    public static String getSelectedCompressionAlgorithm() {
        return selectedCompressionAlgorithm;
    }

    /**
     * A simple setter for the <code>selectedCompressionAlgorithm</code> variable.

     */
    public static void setSelectedCompressionAlgorithm(String selectedCompressionAlgorithm) {
        ImageWindow.selectedCompressionAlgorithm = selectedCompressionAlgorithm;
    }

    /**
     * A simple getter for the <code>frame</code> variable.
     *
     * @return frame A <code>JFrame</code> object representing the current window, or frame.
     */
    public static JFrame getFrame() {
        return frame;
    }

    /**
     * A simple getter for the <code>image</code> variable.
     *
     * @return image An image of type <code>BufferedImage</code> that holds the full-size image rendered in the window.
     */
    public static BufferedImage getImage() {
        return image;
    }

    /**
     * A simple setter for the <code>image</code> variable.
     *
     * @param newImage A new image of type <code>BufferedImage</code> that will hold the full-size image rendered in the window.
     */
    public static void setImage(BufferedImage newImage) {
        image = newImage;
    }

    /**
     * A simple getter for the <code>rleAlgorithmItem</code> variable.
     *
     * @return rleAlgorithmItem The RLE algorithm menu radio button
     */
    public static JMenuItem getRleAlgorithmItem() {
        return rleAlgorithmItem;
    }

    /**
     * A simple getter for the <code>indexedColorAlgorithmItem</code> variable.
     *
     * @return indexedColorAlgorithmItem The Indexed Color algorithm menu radio button
     */
    public static JMenuItem getIndexedColorAlgorithmItem() {
        return indexedColorAlgorithmItem;
    }

    /**
     * A simple getter for the <code>selectedCompressionAlgorithm</code> variable.
     *
     * @return selectedCompressionAlgorithm A string constant representing the compression algorithm currently selected
     *                                      from the GUI
     */
    public static String getInputDirectory() {
        return inputDirectory;
    }

    /**
     * A simple getter for the <code>selectedCompressionAlgorithm</code> variable.
     *
     * @return selectedCompressionAlgorithm A string constant representing the compression algorithm currently selected
     *                                      from the GUI
     */
    public static void setInputDirectory(String inputDirectory) {
        ImageWindow.inputDirectory = inputDirectory;
    }

    /**
     * A simple getter for the <code>originalImage</code> variable.
     *
     * @return  The last opened image file
     */
    public static BufferedImage getOriginalImage() {
        return originalImage;
    }

    /**
     * A simple setter for the <code>originalImage</code> variable.
     *
     * @param originalImage The last opened image file
     */
    public static void setOriginalImage(BufferedImage originalImage) {
        ImageWindow.originalImage = originalImage;
    }

    /**
     * Simple getter for <code>imageScale</code> variable
     *
     * @return Double representing the current scale of the displayed image
     */
    public static double getImageScale() {
        return imageScale;
    }

    /**
     * Simple setter for <code>imageScale</code> variable
     *
     * @param imageScale Double representing the current scale of the displayed image
     */
    public static void setImageScale(double imageScale) {
        ImageWindow.imageScale = imageScale;
    }

    /**
     * Simple setter for the frame object
     *
     * @param frame The JFrame to become the new frame
     */
    public static void setFrame(JFrame frame) {
        ImageWindow.frame = frame;
    }

    /**
     * Getter for the <code>ResizeWindow</code> object associated with this class
     *
     * @return a <code>ResizeWindow</code> object
     */
    public static ResizeWindow getResizeDialog() {
        return resizeWindow;
    }

    /**
     * Setter for the <code>ResizeWindow</code> object associated with this class
     *
     * @param resizeWindow a <code>ResizeWindow</code> object
     */
    public static void setResizeDialog(ResizeWindow resizeWindow) {
        ImageWindow.resizeWindow = resizeWindow;
    }

    /**
     * Getter for the <code>RotateWindow</code> object associated with this class
     *
     * @return a <code>RotateWindow</code> object
     */
    public static RotateWindow getRotateDialog() {
        return rotateWindow;
    }

    /**
     * Setter for the <code>RotateWindow</code> object associated with this class
     *
     * @param rotateWindow a <code>RotateWindow</code> object
     */
    public static void setRotateDialog(RotateWindow rotateWindow) {
        ImageWindow.rotateWindow = rotateWindow;
    }

    /**
     * Getter for the <code>zipAlgorithmItem</code> object associated with this class
     *
     * @return the <code>zipAlgorithmItem</code> object
     */
    public static JMenuItem getZipAlgorithmItem() {
        return zipAlgorithmItem;
    }

    /**
     * Getter for the <code>gzipAlgorithmItem</code> object associated with this class
     *
     * @return the <code>gzipAlgorithmItem</code> object
     */
    public static JMenuItem getGzipAlgorithmItem() {
        return gzipAlgorithmItem;
    }

    /**
     * Getter for the <code>bzipAlgorithmItem</code> object associated with this class
     *
     * @return the <code>bzipAlgorithmItem</code> object
     */
    public static JMenuItem getBzipAlgorithmItem() {
        return bzipAlgorithmItem;
    }

    /**
     * Getter for the <code>noZipAlgorithmItem</code> object associated with this class
     *
     * @return the <code>noZipAlgorithmItem</code> object
     */
    public static JMenuItem getNoZipAlgorithmItem() {
        return noZipAlgorithmItem;
    }

    /**
     * Getter for the <code>extraCompressionAlgorithm</code> variable
     *
     * @return The current <code>extraCompressionAlgorithm</code>
     */
    public static String getExtraCompressionAlgorithm() {
        return extraCompressionAlgorithm;
    }

    /**
     * Setter for the <code>extraCompressionAlgorithm</code> variable
     *
     * @param extraCompressionAlgorithm The new <code>extraCompressionAlgorithm</code>
     */
    public static void setExtraCompressionAlgorithm(String extraCompressionAlgorithm) {
        ImageWindow.extraCompressionAlgorithm = extraCompressionAlgorithm;
    }
} // end class

