/*
 * SettingsPanel | Templates
 * Displays the settings panel.
 */
package woordjes;

import com.explodingpixels.macwidgets.HudWidgetFactory;
import java.awt.Color;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SpringLayout;

/**
 *
 * @author janbart
 * @version 20110102
 */
public class SettingsPanel extends JFrame {

    private JPanel pnlSettings;
    private SpringLayout layout;
    private JCheckBox chkChars, chkRandomWords;
    private JLabel lblFontsize;
    private JComboBox cbFontsize;

    public SettingsPanel() {
        initializeComponents();
        layoutComponents();
        initializeActionlisteners();
    }

    private void initializeComponents() {
        pnlSettings = new JPanel();
        layout = new SpringLayout();
        chkRandomWords = HudWidgetFactory.createHudCheckBox(java.util.ResourceBundle.getBundle("translations/Bundle").getString("SHOW WORDS IN RANDOM ORDER"));
        chkChars = HudWidgetFactory.createHudCheckBox(java.util.ResourceBundle.getBundle("translations/Bundle").getString("SHOW SPECIAL CHARACTERS"));
        lblFontsize = HudWidgetFactory.createHudLabel(java.util.ResourceBundle.getBundle("translations/Bundle").getString("FONT SIZE"));
        cbFontsize = HudWidgetFactory.createHudComboBox(new DefaultComboBoxModel(new Integer[]{10,12,14,16,18,20}));

    }

    private void layoutComponents() {
        pnlSettings.setBackground(Color.darkGray);
        pnlSettings.setLayout(layout);
        //Layout 
        layout.putConstraint(SpringLayout.WEST, chkRandomWords,20,SpringLayout.WEST, pnlSettings);
        layout.putConstraint(SpringLayout.NORTH, chkRandomWords,20,SpringLayout.NORTH, pnlSettings);
        layout.putConstraint(SpringLayout.WEST, chkChars,20,SpringLayout.WEST, pnlSettings);
        layout.putConstraint(SpringLayout.NORTH, chkChars,40,SpringLayout.NORTH, pnlSettings);
        layout.putConstraint(SpringLayout.WEST, lblFontsize,40,SpringLayout.WEST, pnlSettings);
        layout.putConstraint(SpringLayout.NORTH, lblFontsize,80,SpringLayout.NORTH, pnlSettings);
        layout.putConstraint(SpringLayout.WEST, getCbFontsize(),140,SpringLayout.WEST, pnlSettings);
        layout.putConstraint(SpringLayout.NORTH, getCbFontsize(),80,SpringLayout.NORTH, pnlSettings);

        pnlSettings.add(chkRandomWords);
        pnlSettings.add(chkChars);
        pnlSettings.add(lblFontsize);
        pnlSettings.add(getCbFontsize());

    }

    private void initializeActionlisteners(){

    }

    /**
     * @return the jcbFontsize
     */
    public JComboBox getJcbFontsize() {
        return getCbFontsize();
    }

    /**
     * @return the chkChars
     */
    public JCheckBox getChkChars() {
        return chkChars;
    }

    /**
     * @return the chkRandomWords
     */
    public JCheckBox getChkRandomWords() {
        return chkRandomWords;
    }

    /**
     * @return the pnlSettings
     */
    public JPanel getPnlSettings() {
        return pnlSettings;
    }

    /**
     * @return the cbFontsize
     */
    public JComboBox getCbFontsize() {
        return cbFontsize;
    }
}
