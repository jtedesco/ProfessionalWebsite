package edu.illinois.cs242.view;

import edu.illinois.cs242.controller.ButtonConstants;
import edu.illinois.cs242.controller.MenuActionFactory;
import edu.illinois.cs242.controller.MenuConstants;
import edu.illinois.cs242.controller.MenuFactory;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.text.NumberFormat;

/**
 * <p> Created By: Jon Tedesco
 * <p> Date: Sep 15, 2010
 *
 * <p> <p> Class to encapsulate the details of the resize dialog
 */
public class ResizeWindow extends JPanel {

    //Formatting & labeling for the text fields
    NumberFormat widthFormat = NumberFormat.getNumberInstance();
    NumberFormat hieghtFormat = NumberFormat.getNumberInstance();
    JLabel widthLabel = new JLabel("Width: ");
    JLabel heightLabel = new JLabel("Height: ");

    //The text fields
    JFormattedTextField widthField = new JFormattedTextField(widthFormat);
    JFormattedTextField heightField = new JFormattedTextField(hieghtFormat);

    //The 'ok' button
    JButton okButton = null;

    //Field values
    int width;
    int height;

    //A frame that can contain this panel
    JFrame containingFrame = null;

    /**
     * Initializes the dialog as we expect
     */
    public void initialize(){
        height = ImageWindow.getFrame().getComponent(0).getHeight();
        width = ImageWindow.getFrame().getComponent(0).getWidth();
        widthField.setValue(width);
        widthField.setColumns(5);
        heightField.setValue(height);
        heightField.setColumns(5);

        //Create a new button to do the resizing of the image
        okButton = new JButton("Ok");
        MenuActionFactory menuActionFactory = MenuFactory.buildMenuActionFactory(MenuConstants.EFFECTS_MENU);
        okButton.addActionListener(menuActionFactory.buildMenuAction(ButtonConstants.OK_RESIZE_BUTTON));
        JPanel buttonPane = new JPanel();
        buttonPane.add(okButton);

        //Tell accessibility tools about label/textfield pairs.
        widthLabel.setLabelFor(widthField);
        heightLabel.setLabelFor(heightField);

        //Lay out the labels in a panel.
        JPanel widthPane = new JPanel();
        widthPane.add(widthLabel);
        widthPane.add(widthField);

        //Layout the text fields in a panel.
        JPanel heightPane = new JPanel();
        heightPane.add(heightLabel);
        heightPane.add(heightField);

        //Put the panels in this panel, labels on left,
        //text fields on right.
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        add(widthPane);
        add(heightPane);
        add(buttonPane);
    }

    /**
     * Returns the value of the 'width' field
     *
     * @return The value of the 'width' field
     */
    public int getWidthInput() {
        Number width = (Number) widthField.getValue();
        return width.intValue();
    }

    /**
     * Returns the value of the 'height' field
     *
     * @return The value of the 'height' field
     */
    public int getHeightInput() {
        Number height = (Number) heightField.getValue();
        return height.intValue();
    }

    /**
     * Create and return a JFrame with this panel contained in it
     *
     * @return A JFrame containing this data
     */
    public JFrame getFrame() {

        //Create a frame for this panel and return it
        JFrame frame = new JFrame("Choose New Dimensions of Image");
        containingFrame = frame;
        frame.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
        frame.add(this);
        setVisible(true);
        frame.setMinimumSize(new Dimension(400, 10));
        frame.pack();

        //Make the enter button hit 'ok'
        KeyListener keyListener = new KeyListener() {

            public void keyTyped(KeyEvent e) {
                if(e.getKeyChar()==KeyEvent.VK_ENTER){
                    okButton.doClick();
                }
            }

            public void keyPressed(KeyEvent e) {
            }

            public void keyReleased(KeyEvent e) {
            }
        };
        frame.addKeyListener(keyListener);
        widthField.addKeyListener(keyListener);
        heightField.addKeyListener(keyListener);

        return frame;
    }

    /**
     * Closes the frame containing the item (the one returned above)
     */
    public void close(){
        if(containingFrame!=null)
            containingFrame.setVisible(false);
    }
}
