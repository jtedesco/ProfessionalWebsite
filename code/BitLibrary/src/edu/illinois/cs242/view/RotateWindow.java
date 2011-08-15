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
 * <p> <p> Class to encapsulate the details of the rotate dialog
 */
public class RotateWindow extends JPanel {

    //Formatting & labeling for the text fields
    NumberFormat rotateFormat = NumberFormat.getNumberInstance();
    JLabel rotateLabel = new JLabel("Degrees: ");

    //The text fields
    JFormattedTextField rotateField = new JFormattedTextField(rotateFormat);

    //A frame that can contain this panel
    JFrame containingFrame = null;

    //The 'ok' button
    JButton okButton = null;
    /**
     * Initializes the dialog as we expect
     */
    public void initialize(){
        rotateField.setValue(0.0);
        rotateField.setColumns(5);

        //Create a new button to do the resizing of the image
        okButton = new JButton("Ok");
        MenuActionFactory menuActionFactory = MenuFactory.buildMenuActionFactory(MenuConstants.EFFECTS_MENU);
        okButton.addActionListener(menuActionFactory.buildMenuAction(ButtonConstants.OK_ROTATE_BUTTON));
        JPanel buttonPane = new JPanel();
        buttonPane.add(okButton);

        //Tell accessibility tools about label/textfield pairs.
        rotateLabel.setLabelFor(rotateField);

        //Lay out the labels in a panel.
        JPanel labelPane = new JPanel();
        labelPane.add(rotateLabel);

        //Layout the text fields in a panel.
        JPanel fieldPane = new JPanel();
        fieldPane.add(rotateField);

        //Put the panels in this panel, labels on left,
        //text fields on right.
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        add(labelPane);
        add(fieldPane);
        add(buttonPane);
    }

    /**
     * Returns the value of the 'width' field
     *
     * @return The value of the 'width' field
     */
    public int getRotateInput() {
        Number value = (Number) rotateField.getValue();
        return value.intValue();
    }

    /**
     * Create and return a JFrame with this panel contained in it
     *
     * @return A JFrame containing this data
     */
    public JFrame getFrame() {

        //Create a frame for this panel and return it
        JFrame frame = new JFrame("Choose Degrees By Which To Rotate Image:");
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
        rotateField.addKeyListener(keyListener);

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
