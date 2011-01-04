/*
 * InputPanel | Templates
 * Displays the inputpanel.
 */
package woordjes;

import java.awt.Color;
import java.awt.Dimension;
import javax.swing.border.EmptyBorder;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

/**
 *
 * @author janbart
 * @version 20110103
 */
public class InputPanel extends JFrame {

    private JPanel pnlInputFrame;
    private JLabel lblWord, lblSeparator0, lblTranslation;
    private JTextField[] tmp; // Declares an array of JTextFields
    private JLabel[] tmpL;
    private String[] tmpInputContent;
    GridBagConstraints c;
    private int nrOfInputFields=12; // Must be odd. always.

    public InputPanel() {
        pnlInputFrame = new JPanel();
        initializeComponents();
        layoutComponents();
    }

    private void initializeComponents() {

        lblWord = new JLabel(java.util.ResourceBundle.getBundle("translations/Bundle").getString("WORD"));
        lblSeparator0 = new JLabel(java.util.ResourceBundle.getBundle("translations/Bundle").getString(" ; "));
        lblTranslation = new JLabel(java.util.ResourceBundle.getBundle("translations/Bundle").getString("TRANSLATION"));
        tmp = new JTextField[nrOfInputFields]; // Allocates memory for 6
        tmpL = new JLabel[getNrOfImputFields() / 2];

        c = new GridBagConstraints();
        // Create input fields
        for (int i = 0; i < getNrOfImputFields(); i++) { // create inputfields
            tmp[i] = new JTextField();
        }
        for (int i = 0; i < getNrOfImputFields() / 2; i++) { // create separators
            tmpL[i] = new JLabel(java.util.ResourceBundle.getBundle("translations/Bundle").getString(" ; "));
        }
    }

    private void layoutComponents() {
        getPnlInputFrame().setLayout(new GridBagLayout());
        getPnlInputFrame().setBorder(new EmptyBorder(5, 5, 5, 5)); // T,L,B,R
        c.weighty = 1.0;
        c.weightx = 1.0;
        c.fill = GridBagConstraints.HORIZONTAL;
        c.insets = new Insets(0, 0, 2, 0); // bottom margin for elements
        c.fill = GridBagConstraints.CENTER; // center elements
        c.gridx = 0;
        c.gridy = 0;
        getPnlInputFrame().add(getLblWord(), c);
        c.gridx = 1;
        c.gridy = 0;
        getPnlInputFrame().add(getLblSeparator0(), c);
        c.gridx = 2;
        c.gridy = 0;
        getPnlInputFrame().add(getLblTranslation(), c);

        int j = 0; //
        int k = 0; //
        int l = 1; //
        for (int i = 0; i < getNrOfImputFields(); i++) {
            getTmp()[i].setPreferredSize(new Dimension(200, 30)); // inputfield size
            if (k == 0) {
                c.gridx = 0;
                k++;
            }
            if (k == 2) {
                c.gridx = 2;
                k = 0;
            }
            c.gridy = l;
            getPnlInputFrame().add(getTmp()[i], c); // add field
            if (k == 0) {
                l++;
            }
            if (i % 2 < 1) {
                c.gridx = 1;
                c.weighty = 0.5;
                c.weightx = 0.5;
                getPnlInputFrame().add(getTmpL()[j], c); // add separator
                c.weighty = 1.0;
                c.weightx = 1.0;
                j++;
                k++;
            }
        }
        c.gridx = 2;
        c.gridy = l + 1;
        getTmp()[0].setBackground(Color.lightGray);
        //getTmp()[0].setText(java.util.ResourceBundle.getBundle("translations/Bundle").getString("ENGLISH"));
        tmp[0].setToolTipText(java.util.ResourceBundle.getBundle("translations/Bundle").getString("PUT HERE THE NAME OF THE LANGUAGE. EG: ENGLISH"));
        getTmp()[1].setBackground(Color.lightGray);
        //getTmp()[1].setText(java.util.ResourceBundle.getBundle("translations/Bundle").getString("DUTCH"));
        tmp[1].setToolTipText(java.util.ResourceBundle.getBundle("translations/Bundle").getString("PUT HERE THE NAME OF THE TRANSLATION. EG: DUTCH"));


    }

    public void addInputField() {

        tmpInputContent = new String[nrOfInputFields+2];

        // If there's already content, save it
        for (int i = 0; i < nrOfInputFields; i++) {
            if (!tmp[i].getText().isEmpty()) {
                tmpInputContent[i] = tmp[i].getText();
            }
        }
        // Make the current panel empty
        pnlInputFrame.removeAll();
        // We put two more inputfiels up
        nrOfInputFields = nrOfInputFields + 2;
        // Create the new inputfield
        initializeComponents();
        layoutComponents();
        // Put the content back
        // We're starting from 0, so use -3
        for(int i=0;i<=nrOfInputFields -3;i++){
            //if (tmpInputContent[i].length() != 0) {
              tmp[i].setText("" +tmpInputContent[i]);
            //}else{
              //tmp[i].setText("leeg");
            //}
        }
        pnlInputFrame.getRootPane().repaint();
        pnlInputFrame.repaint();
        pnlInputFrame.updateUI();
    }

    /**
     * @return the lblWord
     */
    public JLabel getLblWord() {
        return lblWord;
    }

    /**
     * @return the lblSeparator0
     */
    public JLabel getLblSeparator0() {
        return lblSeparator0;
    }

    /**
     * @return the lblTranslation
     */
    public JLabel getLblTranslation() {
        return lblTranslation;
    }

    /**
     * @return the tmp
     */
    public JTextField[] getTmp() {
        return tmp;
    }

    /**
     * @return the tmpL
     */
    public JLabel[] getTmpL() {
        return tmpL;
    }

    /**
     * @return the nrOfImputFields
     */
    public int getNrOfImputFields() {
        return nrOfInputFields;
    }

    /**
     * @return the pnlInputFrame
     */
    public JPanel getPnlInputFrame() {
        return pnlInputFrame;
    }
}
