package woordjes;

import java.awt.AlphaComposite;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.Closeable;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.io.Writer;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;
import com.explodingpixels.macwidgets.BottomBar;
import com.explodingpixels.macwidgets.BottomBarSize;
import com.explodingpixels.macwidgets.HudWidgetFactory;
import com.explodingpixels.macwidgets.SourceList;
import com.explodingpixels.macwidgets.SourceListCategory;
import com.explodingpixels.macwidgets.SourceListItem;
import com.explodingpixels.macwidgets.SourceListModel;
import java.awt.AWTException;
import java.awt.FontMetrics;
import java.awt.Insets;
import java.awt.Robot;
import java.awt.SplashScreen;
import java.awt.event.KeyEvent;
import javax.swing.JScrollPane;
import javax.swing.JTable;

public class Gui extends JFrame {

    private static final long serialVersionUID = 1L;
    public JFrame frame;
    // Classes
    public Words woordjes;
    // Menu
    private JPanel pnlMenu;
    private JMenuBar mbMenu;
    private JMenu mFile;
    private JMenuItem miNew, miOpen, miHighscores, miExit;
    private JMenu mVertaling;
    private JMenuItem miAtoB, miBtoA, miCreateList;
    private JMenuItem miTest;
    private JMenu mHelp;
    private JMenuItem miCheckUpdate;
    // Woordjes Layout
    private GridBagLayout gbl;
    private JPanel pnlCenter, pnlCenterS, pnlWoord;
    private JLabel lblWoord, lblVertaling;
    private JTextField tfInput;
    private JButton btnCheck, btnTip, btnSettings, btnNewGame;
    private JLabel lblMelding;
    // InputFrame (create new lists with words)
    private JPanel pnlInputFrame;
    private SourceListModel model;
    private SourceListCategory category;
    private SourceList sourceList;
    private JLabel lbl, lblWord, lblSeparator0, lblTranslation;
    private JTextField[] tmp; // declares an array of JTextFields
    private JLabel[] tmpL;
    private JButton btnSaveList;
    // EndGameFrame
    private JPanel pnlEndGame;
    private MyTableModel myModel;
    private JTable table;
    private JLabel lblTitleResult;
    // Settings
    private JPanel pnlSettings;
    private JCheckBox chkRandomWords, chkChars;
    private BottomBar bottomBar;
    // Special Characters
    private JPanel pnlChars;
    private JButton btnAaigu, btnAgrave, btnAcirconflexe, btnEaigu, btnEgrave,
            btnEcirconflexe;
    private JButton btnIcirconflexe, btnCcedille;
    // Variabels
    private JFileChooser chooser;
    public int currentWord = 1;
    public int fout, res;
    private String version, date;
    private boolean isFout;
    private boolean endOfList = false;
    private boolean dontJumpLeft = true;
    private boolean pageIsSettings, pageIsInputFrame, pageIsEndGame, showChars;
    private int nrOfImputFields = 12; // must be odd. always.
    Dimension withoutChar = new Dimension(550, 350); // width, length
    Dimension withChar = new Dimension(550, 460);

    static void renderSplashFrame(Graphics2D g, int frame) {
        final String[] comps = {"Woorden", "Instellingen", "Layout"};
        g.setComposite(AlphaComposite.Clear);
        g.fillRect(80, 140, 200, 40);//x,y,w,h
        g.setPaintMode();
        g.setColor(Color.BLACK);
        g.drawString("Laden: " + comps[(frame / 50) % 3] + "...", 80, 150);
    }

    public Gui(String version, String date) throws UnsupportedEncodingException {
        this.version = version;
        this.date = date;

        woordjes = new Words();

        // Ask for window decorations provided by the look and feel.
        // Changes the standard os borders...
        //JFrame.setDefaultLookAndFeelDecorated(true);

        /*try {
        if (woordjes.os.equalsIgnoreCase("Windows")) {
        // TODO: remove
        // System.out.println("System: " + woordjes.os);
        // Set cross-platform Java L&F (also called "Metal")
        UIManager.setLookAndFeel(UIManager
        .getCrossPlatformLookAndFeelClassName());
        } else {
        }
        } catch (UnsupportedLookAndFeelException e) {
        // handle exception
        } catch (ClassNotFoundException e) {
        // handle exception
        } catch (InstantiationException e) {
        // handle exception
        } catch (IllegalAccessException e) {
        // handle exception
        }*/

        // Create the frame.
        frame = new JFrame("Woordjes");

        // Set the frame icon to an image loaded from a file.
        java.net.URL imgURL = Gui.class.getResource("icon.gif");
        frame.setIconImage(new ImageIcon(imgURL).getImage());

        woordjes.getSettings();
        initialiseerComponenten();
        layoutComponenten();
        initialiseerActionlisteners();

        woordjes.laadWoorden(woordjes.wordlist);
        woordjes.randomize();
        lblWoord.setText(woordjes.getWoord());
        if (!woordjes.msg.isEmpty()) {
            JOptionPane.showMessageDialog(Gui.this, woordjes.toString());
        }

        frame.setSize(withoutChar);
        // center frame on screen
        Toolkit toolkit = Toolkit.getDefaultToolkit();
        Dimension ssize = toolkit.getScreenSize();
        int x = (int) (ssize.getWidth() - getWidth()) / 2;
        int y = (int) (ssize.getHeight() - getHeight()) / 2;
        frame.setLocation(x, y);
        frame.setDefaultCloseOperation(EXIT_ON_CLOSE);

        /* Splash*/
        final SplashScreen splash = SplashScreen.getSplashScreen();
        if (splash == null) {
            System.out.println("SplashScreen.getSplashScreen() returned null");
            return;
        }
        Graphics2D g = splash.createGraphics();
        if (g == null) {
            System.out.println("g is null");
            return;
        }
        for (int i = 0; i < 100; i++) {
            renderSplashFrame(g, i);
            splash.update();
            try {
                Thread.sleep(10);
            } catch (InterruptedException e) {
            }
        }
        splash.close();
        frame.setVisible(true);
        toFront();
        /*splash */

        showStartscreen();

    }

    private void initialiseerComponenten() {
        // menu
        pnlMenu = new JPanel();
        mbMenu = new JMenuBar();
        mFile = new JMenu("Bestand");
        chooser = new JFileChooser();
        miNew = new JMenuItem("Nieuw spel");
        miOpen = new JMenuItem("Open Bestand");
        miHighscores = new JMenuItem("Highscores");
        miExit = new JMenuItem("Afsluiten");
        mVertaling = new JMenu("Vertaling");
        miAtoB = new JMenuItem("A -> B");
        miBtoA = new JMenuItem("B -> A");
        miCreateList = new JMenuItem("Lijst opstellen");
        mHelp = new JMenu("Help");
        miTest = new JMenuItem("Test");
        miCheckUpdate = new JMenuItem("Controleren op Update's");
        // Center
        pnlCenter = new JPanel();
        pnlCenterS = new JPanel();
        gbl = new GridBagLayout();
        pnlWoord = new JPanel();
        lblWoord = new JLabel();
        lblVertaling = new JLabel();
        tfInput = new JTextField();
        btnCheck = new JButton("Check");
        btnTip = new JButton("Tip");
        btnSettings = new JButton("Settings");
        lblMelding = new JLabel(woordjes.getUiTranslation(4));
        // Settings - Special Characters
        pnlChars = new JPanel();
        btnAaigu = new JButton("\u00e0");
        btnAgrave = new JButton("\u00e1");
        btnAcirconflexe = new JButton("\u00e2");
        btnEaigu = new JButton("\u00e9");
        btnEgrave = new JButton("\u00e8");
        btnEcirconflexe = new JButton("\u00ea");
        btnIcirconflexe = new JButton("\u00ee");
        btnCcedille = new JButton("\u00e7");
        pnlSettings = new JPanel();
        chkRandomWords = HudWidgetFactory.createHudCheckBox("Woorden random weergeven");
        chkChars = HudWidgetFactory.createHudCheckBox("Speciale karakters weergeven");
        // InputFrame
        pnlInputFrame = new JPanel();
        tmp = new JTextField[nrOfImputFields]; // Allocates memory for 6
        // EndGameFrame
        pnlEndGame = new JPanel();
        myModel = new MyTableModel();
        table = new JTable(myModel);
        lblTitleResult = new JLabel("Resultaat");
        // JTextFields
        tmpL = new JLabel[nrOfImputFields / 2];
        model = new SourceListModel();
        category = new SourceListCategory("Woordenlijst");
        model.addCategory(category);
        model.addItemToCategory(new SourceListItem("Frans 1"), category);
        sourceList = new SourceList(model);
        btnSaveList = new JButton("Opslaan");
        // Create input fields
        lblWord = new JLabel("Woord");
        lblSeparator0 = new JLabel(" ; ");
        lblTranslation = new JLabel("Vertaling");
        for (int i = 0; i < nrOfImputFields; i++) { // create inputfields
            tmp[i] = new JTextField();
        }
        for (int i = 0; i < nrOfImputFields / 2; i++) { // create separators
            tmpL[i] = new JLabel(" ; ");
        }
        // Bottom
        bottomBar = new BottomBar(BottomBarSize.LARGE);
    }

    private void layoutComponenten() {
        frame.add(pnlCenter, BorderLayout.CENTER);
        frame.add(pnlMenu, BorderLayout.NORTH);
        frame.add(bottomBar.getComponent(), BorderLayout.SOUTH);
        // MenuBar
        // pnlMenu.setLayout(new BoxLayout(pnlMenu,BoxLayout.LINE_AXIS));
        pnlMenu.setLayout(new GridLayout(1, 1)); // rijen, kolommen
        mbMenu.setBorder(new EmptyBorder(0, 10, 0, 0)); // B,L,O,R
        pnlMenu.setForeground(Color.white);
        mbMenu.setForeground(Color.black);
        pnlMenu.add(mbMenu);
        // pnlMenu.add(Box.createHorizontalGlue());
        mbMenu.add(mFile);
        mFile.add(miNew);
        mFile.add(miOpen);
        mFile.add(miExit);
        mbMenu.add(mVertaling);
        mVertaling.add(miAtoB);
        mVertaling.add(miBtoA);
        mbMenu.add(miCreateList);
        mbMenu.add(mHelp);
        // TODO: remove miTest for production
        //mHelp.add(miTest);
        mHelp.add(miCheckUpdate);
        // Center Panel
        pnlCenter.setLayout(new GridLayout(2, 1)); // rijen, kolommen
        pnlCenter.add(pnlWoord);
        pnlCenter.add(pnlCenterS);
        pnlCenter.setBorder(new EmptyBorder(10, 10, 10, 10)); // B,L,O,R
        // Woord Panel
        pnlWoord.setLayout(gbl);
        pnlWoord.setBackground(Color.white);
        // Settings Panel
        pnlSettings.setBackground(Color.darkGray);
        pnlSettings.add(chkRandomWords);
        pnlSettings.add(chkChars);
        // private GridBagLayout gblSettings;

        // Place a component at cell location (1,1)
        GridBagConstraints gbc1 = new GridBagConstraints();
        gbc1.gridx = 1;
        gbc1.gridy = 1;

        GridBagConstraints gbc2 = new GridBagConstraints();
        gbc2.gridx = 1;
        gbc2.gridy = 2; // change to 3 will not affect distance to gbc1

        // Associate the gridbag constraints with the component
        gbl.setConstraints(lblWoord, gbc1);
        gbl.setConstraints(lblVertaling, gbc2);

        pnlWoord.setLayout(new GridLayout(2,1)); // r, c
        
        pnlWoord.add(lblWoord);
        pnlWoord.add(lblVertaling);
        lblWoord.setBorder(BorderFactory.createLineBorder(Color.white));
        lblWoord.setFont(new Font("sansserif", Font.BOLD, 15));
        lblWoord.setHorizontalTextPosition(JLabel.CENTER);
        //lblWoord.setVerticalTextPosition(JLabel.CENTER);
        lblVertaling.setFont(new Font("sansserif", Font.BOLD, 15));
        lblVertaling.setHorizontalTextPosition(JLabel.CENTER);
        //lblVertaling.setVerticalTextPosition(JLabel.BOTTOM);

        pnlCenterS.setLayout(new GridLayout(2, 1)); // rijen, kolommen
        pnlCenterS.add(tfInput);
        if (showChars) {
            setSize(withChar);
            pnlCenterS.add(pnlChars);
            pnlChars.add(btnAaigu);
            pnlChars.add(btnAgrave);
            pnlChars.add(btnAcirconflexe);
            pnlChars.add(btnEaigu);
            pnlChars.add(btnEgrave);
            pnlChars.add(btnEcirconflexe);
            pnlChars.add(btnIcirconflexe);
            pnlChars.add(btnCcedille);
            pack();
        } else {
            setSize(withoutChar);
            pnlCenterS.remove(pnlChars);
            getContentPane().validate();
        }
        // pnlCenterS.add(btnCheck);
        tfInput.setBorder(BorderFactory.createLoweredBevelBorder());
        //UnifiedToolBar toolBar = new UnifiedToolBar();

        btnSettings.putClientProperty("JButton.buttonType", "textured");
        // toolBar.addComponentToLeft(button);

        if (dontJumpLeft) {
            bottomBar.addComponentToLeft(btnSettings);
            bottomBar.addComponentToLeft(lblMelding,0);
        }else{
            bottomBar.addComponentToLeft(lblMelding, 0);
        }

        // getContentPane().add(toolBar.getComponent(), BorderLayout.NORTH);


        frame.getContentPane().add(bottomBar.getComponent(),
                BorderLayout.SOUTH);

    }

    private void initialiseerActionlisteners() {
        // Nieuw Spel
        miNew.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                try {
                    nieuwSpel();
                } catch (UnsupportedEncodingException e1) {
                    JOptionPane.showMessageDialog(Gui.this, "UnsupportedEncodingException"
                            + e1);
                }
            }
        });
        // OpenFile
        miOpen.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                try {
                    openFile();
                } catch (FileNotFoundException e1) {
                    JOptionPane.showMessageDialog(Gui.this, "File not found"
                            + e1);
                } catch (IOException e1) {
                    JOptionPane.showMessageDialog(Gui.this, "IOException" + e1);
                }
            }
        });
        // Highscores
        miHighscores.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                // toonHighScores();
            }
        });
        // Exit
        miExit.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });
        // Translation: a to b
        miAtoB.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                dontJumpLeft = false;
                if (currentWord != 1) {
                    JOptionPane.showMessageDialog(Gui.this, woordjes.getUiTranslation(0));
                } else {
                    woordjes.AtoB = true;
                    try {
                        nieuwSpel();
                    } catch (UnsupportedEncodingException e1) {
                        JOptionPane.showMessageDialog(Gui.this, "Unsupported Encoding Exception" + e1);
                    }
                }
            }
        });
        // Translation: b to a
        miBtoA.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                dontJumpLeft = false;
                if (currentWord != 1) {
                    JOptionPane.showMessageDialog(Gui.this, woordjes.getUiTranslation(0));
                } else {
                    woordjes.AtoB = false;
                    try {
                        nieuwSpel();
                    } catch (UnsupportedEncodingException e1) {
                        JOptionPane.showMessageDialog(Gui.this, "Unsupported Encoding Exception: " + e1);
                    }
                }
            }
        });
        // Create new list with words and translating
        miCreateList.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                if (pageIsInputFrame) {
                    // Do nothing
                } else {
                    createInputFrame();
                }
            }
        });
        // Test button, for everything
        miTest.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                JOptionPane.showMessageDialog(Gui.this, "Random: " + woordjes.randomWoordjes);
            }
        });

        // Check for updates
        miCheckUpdate.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                JOptionPane.showMessageDialog(Gui.this, "OS: "
                        + woordjes.getOs() + "\nDatum: " + date + "\nVersie: "
                        + version);
                try {
                    java.awt.Desktop.getDesktop().browse(
                            new URI(
                            "http://jan-bart.be/public/woordjes/update/?v="
                            + version));
                } catch (IOException e1) {
                    JOptionPane.showMessageDialog(Gui.this, "IOException: " + e1);
                } catch (URISyntaxException e1) {
                    JOptionPane.showMessageDialog(Gui.this, "URISyntaxExceptio: " + e1);
                }

            }
        });
        // Settings
        btnSettings.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                dontJumpLeft = false;
                if (pageIsSettings || pageIsInputFrame) {
                    showStartscreen();
                } else {
                    // Settings Page
                    frame.getContentPane().removeAll();
                    frame.getContentPane().add(bottomBar.getComponent(),
                            BorderLayout.SOUTH);
                    //getContentPane().add(pnlSouth, BorderLayout.SOUTH);
                    frame.getContentPane().add(pnlSettings, BorderLayout.CENTER);
                    frame.getContentPane().add(pnlMenu, BorderLayout.NORTH);
                    lblMelding.setText("");
                    btnSettings.setText("Ga Terug");
                    // setSize(withoutChar);
                    frame.getContentPane().repaint();
                    // validate();
                    pageIsSettings = true;
                }
            }
        });
        // Random Woordjes
        chkRandomWords.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                if (woordjes.randomWoordjes) {
                    woordjes.randomWoordjes = false;
                } else {
                    woordjes.randomWoordjes = true;
                    try {
                        nieuwSpel();
                    } catch (UnsupportedEncodingException e1) {
                        JOptionPane.showMessageDialog(Gui.this, "UnsupportedEncodingException" + e1);
                    }
                }
            }
        });
        // Check on enter
        tfInput.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                check();
            }
        });
        // Special Characters bar
        chkChars.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                if (showChars) {
                    showChars = false;
                } else {
                    showChars = true;
                }
            }
        });
        // A aigu
        btnAaigu.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                tfInput.setText(tfInput.getText() + "\u00e0");
                setCursorToEnd();
            }
        });
        // A grave
        btnAgrave.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                tfInput.setText(tfInput.getText() + "\u00e1");
                setCursorToEnd();
            }
        });
        // A circonflexe
        btnAcirconflexe.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                tfInput.setText(tfInput.getText() + "\u00e2");
                setCursorToEnd();
            }
        });
        // E aigu
        btnEaigu.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                tfInput.setText(tfInput.getText() + "\u00e9");
                setCursorToEnd();
            }
        });
        // E grave
        btnEgrave.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                tfInput.setText(tfInput.getText() + "\u00e8");
                setCursorToEnd();
            }
        });
        // E circonflexe
        btnEcirconflexe.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                tfInput.setText(tfInput.getText() + "\u00ea");
                setCursorToEnd();
            }
        });
        // I circonflexe
        btnIcirconflexe.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                tfInput.setText(tfInput.getText() + "\u00ee");
                setCursorToEnd();
            }
        });
        // C cedille
        btnCcedille.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                tfInput.setText(tfInput.getText() + "\u00e7");
                setCursorToEnd();
            }
        });
        // Save inputList
        btnSaveList.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                try {
                    saveInputList();
                } catch (FileNotFoundException e1) {
                    System.out.println("Error: " + e1);
                } catch (IOException e1) {
                    System.out.println("Error: " + e1);
                }
            }
        });
        // Check
        btnCheck.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                check();
            }
        });
    }

    private void showStartscreen() {
        frame.getContentPane().removeAll();
        layoutComponenten();
        btnSettings.setText("Settings");
        frame.getContentPane().repaint();
        frame.validate();
        pageIsSettings = false;
        pageIsInputFrame = false;
        pageIsEndGame = false;
        dontJumpLeft = false;
        if (!woordjes.AtoB) {
            lblMelding.setText(woordjes.getUiTranslation(4) + woordjes.lang1 + "\t - \t Woord: " + currentWord + " / "
                    + woordjes.woorden.size());
        } else {
            lblMelding.setText(woordjes.getUiTranslation(4) + woordjes.lang2 + "\t - \t Woord: " + currentWord + " / "
                    + woordjes.woorden.size());
        }
    }

    private void createInputFrame() {
        if (!getPageIsSettings() && !getPageIsInputFrame()) {
            frame.getContentPane().removeAll();
        }
        getSourceList().getComponent().setPreferredSize(new Dimension(150, 220)); // w,h
        frame.getContentPane().add(getBottomBar().getComponent(),
                BorderLayout.PAGE_END);
        // TODO: Remove for production if it doesn't work
        /*frame.getContentPane().add(getSourceList().getComponent(),
        BorderLayout.LINE_START);*/
        frame.getContentPane().add(getPnlInputFrame(), BorderLayout.CENTER);
        frame.getContentPane().add(getPnlMenu(), BorderLayout.PAGE_START);
        // pnlInputFrame.setLayout(new GridLayout((nrOfImputFields/2)+2, 3, 10,
        // 0)); // rijen, kolommen, H-space,-V-space
        getPnlInputFrame().setLayout(new GridBagLayout());
        getPnlInputFrame().setBorder(new EmptyBorder(5, 5, 5, 5)); // T,L,B,R
        GridBagConstraints c = new GridBagConstraints();
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
        getTmp()[0].setBorder(null);
        getTmp()[0].setToolTipText(woordjes.getUiTranslation(3));
        getTmp()[1].setBackground(Color.lightGray);
        getTmp()[1].setBorder(null);
        getTmp()[1].setToolTipText(woordjes.getUiTranslation(3));

        getPnlInputFrame().add(getBtnSaveList(), c);
        getLblMelding().setText("");
        getBtnSettings().setText(woordjes.getUiTranslation(1));
        frame.getContentPane().repaint();
        frame.validate();
        //frame.pack();

        setPageIsInputFrame(true);
    }

    private void createEndGame() {
        String strAorB = "";

        frame.getContentPane().removeAll();
        btnNewGame = new JButton(woordjes.getUiTranslation(2));
        // EndGame New Game button
        btnNewGame.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                try {
                    nieuwSpel();
                } catch (UnsupportedEncodingException ex) {
                    Logger.getLogger(Gui.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });
        frame.getContentPane().add(btnNewGame, BorderLayout.PAGE_END);
        frame.getContentPane().add(getPnlMenu(), BorderLayout.PAGE_START);

        Object[][] rowData2 = {
            {"Vertaling", strAorB},
            {"Random", woordjes.randomWoordjes},
            {"Woorden juist", res},
            {"Woorden fout", fout},
            {"Totaal: ", res + " / " + woordjes.woorden.size()}
        };

        myModel.setRowData(rowData2);
        // AtoB or BtoA?
        if (woordjes.AtoB == true) {
            strAorB = woordjes.lang1 + " -> " + woordjes.lang2;
        } else {
            strAorB = woordjes.lang2 + " -> " + woordjes.lang1;
        }
        if (woordjes.randomWoordjes) {
            table.setValueAt("Woordjes werden random weergegeven", 1, 1);
        } else {
            table.setValueAt("Woordjes werden niet random weergegeven", 1, 1);
        }
        table.setValueAt(strAorB, 0, 1);

        JScrollPane scrollPane = new JScrollPane(table);
        table.setFillsViewportHeight(true);
        table.setGridColor(Color.gray);
        table.setShowHorizontalLines(true);
        table.setShowVerticalLines(false);
        table.setRowHeight(30);
        table.setDragEnabled(false); // users can't change column order
        //table.setRowSelectionAllowed(false);
        //table.setCellSelectionEnabled(false);
        //table.setColumnSelectionAllowed(false);
        table.getColumnModel().getColumn(1).setPreferredWidth(100);

        pnlEndGame.setBorder(new EmptyBorder(15, 15, 15, 15)); // T,L,B,R
        pnlEndGame.setOpaque(true); //content panes must be opaque

        // add table to pnl
        frame.add(pnlEndGame, BorderLayout.CENTER);
        pnlEndGame.setLayout(new BorderLayout());
        pnlEndGame.add(lblTitleResult, BorderLayout.PAGE_START);
        //pnlEndGame.add(table.getTableHeader(), BorderLayout.PAGE_START);
        pnlEndGame.add(table, BorderLayout.CENTER);

        frame.getContentPane().repaint();
        frame.validate();
        pageIsEndGame = true;
    }

    private void setCursorToEnd() {
        tfInput.requestFocusInWindow();
        try {
            Robot robot = new Robot();
            // Simulate a key press
            robot.keyPress(KeyEvent.VK_RIGHT);
            robot.keyRelease(KeyEvent.VK_RIGHT);
        } catch (AWTException e) {
            System.out.println("Error: " + e);
        }

    }

    private void saveInputList() throws FileNotFoundException, IOException {
        String words = "";

        for (int i = 0; i < nrOfImputFields; i = i + 2) {
            if (!tmp[i].getText().isEmpty()) {
                words = words + tmp[i].getText() + ";" + tmp[i + 1].getText()
                        + "\n";
            }
        }
        JFileChooser jFileChooser = new JFileChooser();
        jFileChooser.setSelectedFile(new File("Woordenlijst"));

        int returnVal = jFileChooser.showSaveDialog(frame);
        if (returnVal == JFileChooser.APPROVE_OPTION) {
            File file = jFileChooser.getSelectedFile();
            String fileName = file.getPath();

            /*
             * Writer output = new BufferedWriter( new FileWriter(fileName +
             * ".wrd"));
             */
            OutputStream out = new FileOutputStream(fileName);
            Closeable stream = out;

            try {
                Writer output = new OutputStreamWriter(out, "UTF-8");
                stream = output;
                // FileWriter always assumes default encoding is OK!
                output.write(words);
            } finally {
                stream.close();
            }
        }
    }

    private void nieuwSpel() throws UnsupportedEncodingException {
        // Make sure the user sees the startscreen
        showStartscreen();
        // verwijder alle woorden uit de lijst
        currentWord = 1;
        fout = 0;
        res = 0;
        woordjes.woorden.clear();
        woordjes.gebruikt.clear();
        woordjes.vertaling.clear();
        tfInput.setText("");
        tfInput.setBackground(Color.white);
        tfInput.requestFocusInWindow();
        woordjes.laadWoorden(woordjes.wordlist);
        woordjes.randomize();
        lblWoord.setText(woordjes.getWoord());
        lblVertaling.setText("");
        tfInput.setEnabled(true);
        btnCheck.setEnabled(true);
        btnTip.setEnabled(true);
        endOfList = false;
    }

    public void openFile() throws FileNotFoundException, IOException {
        int returnVal = chooser.showOpenDialog(this);
        if (returnVal != JFileChooser.APPROVE_OPTION) {
            return; // cancelled
        }

        // Keep it this way. Because now woordjes.file is updated
        // woordjes.setSettings(woordjes.macSettings, "" +
        // chooser.getSelectedFile();
        // This ^ may be shorter but is NOT correct
        woordjes.wordlist = chooser.getSelectedFile();
        woordjes.setSettings(woordjes.Settings, "" + woordjes.wordlist);
        nieuwSpel();
    }

    private void check() {
        // End of list?
        if (currentWord == woordjes.woorden.size()) {
            endOfList = true;
        }
        // translation correct?
        if (woordjes.getVertaling().equalsIgnoreCase(tfInput.getText().trim())) {
            if (!endOfList) {
                currentWord++;
            }
            if (!isFout) {
                res++;
            }
            isFout = false;
            lblMelding.setText(" HOERA! \t - \t Woord: " + currentWord + " / "
                    + woordjes.woorden.size() + "\t - \t Score: " + res + " / "
                    + woordjes.woorden.size());

            // Set a new word if end of list isn't reached
            if (!endOfList) {
                lblVertaling.setText("");
                woordjes.randomize();
                lblWoord.setText(woordjes.getWoord());
                tfInput.setText("");
            }
            centerText(false);
        } else {
            // Word is wrong
            if (!isFout) {
                fout++;
            }
            isFout = true;
            lblVertaling.setForeground(Color.red);
            lblVertaling.setText("\n" + woordjes.getVertaling());
            centerText(true);
            lblMelding.setText(" Oooh :( \t - \t Woord: " + currentWord + " / "
                    + woordjes.woorden.size() + "\t - \t Score: " + res + " / "
                    + woordjes.woorden.size());
        }
        if (endOfList && !isFout) {
            // Set end of game
            //System.out.println("Score: " + res);
            createEndGame();

            /*lblWoord.setText("Afgelopen");
            btnCheck.setEnabled(false);
            btnTip.setEnabled(false);
            tfInput.setText("");
            lblMelding.setText("Afgelopen \t - \t Woord: " + currentWord
            + " / " + woordjes.woorden.size() + "\t - \t Score: " + res
            + " / " + woordjes.woorden.size());
            lblVertaling.setText("");
            tfInput.setEnabled(false);
            tfInput.setBackground(Color.gray);*/
        }

    }

    public void centerText(Boolean correction){
        // Get width of Panel Word to center text
        Dimension dimPnlWord = pnlWoord.getSize();
        double widthPnlWord = dimPnlWord.getWidth();
        int intWidthPnlWord = (int) widthPnlWord;

        // Get the size of the current string
         FontMetrics fmWord = lblWord.getFontMetrics(new Font("sansserif", Font.BOLD, 15));
         int widthWord = fmWord.stringWidth(lblWord.getText());
         int sizeWord =  (intWidthPnlWord - widthWord)/2;

        if(!correction){
            pnlWoord.setBorder(BorderFactory.createEmptyBorder(0,sizeWord,0,0)); //top,
        }else{
            // Get the size of the current correction
            FontMetrics fmTranslation = lblVertaling.getFontMetrics(new Font("sansserif", Font.BOLD, 15));
            int widthTranslation = fmTranslation.stringWidth(lblVertaling.getText());
            int sizeTranslation =  (intWidthPnlWord - widthTranslation)/2;

            // Prevent negative left margin
            if(sizeTranslation <= 0){
                sizeTranslation = 1;
            }

            pnlWoord.setBorder(BorderFactory.createEmptyBorder(0,sizeTranslation,0,0)); //top,
            lblWord.setBorder(BorderFactory.createEmptyBorder(0,sizeWord,0,0)); //top,

        }

    }


    // Getters
    public SourceList getSourceList() {
        return sourceList;
    }

    public JLabel getLblWord() {
        return lblWord;
    }

    public JLabel getLblSeparator0() {
        return lblSeparator0;
    }

    public JLabel getLblTranslation() {
        return lblTranslation;
    }

    public JLabel getLblMelding() {
        return lblMelding;
    }

    public JButton getBtnSaveList() {
        return btnSaveList;
    }

    public JButton getBtnSettings() {
        return btnSettings;
    }

    public int getNrOfImputFields() {
        return nrOfImputFields;
    }

    public JTextField[] getTmp() {
        return tmp;
    }

    public JLabel[] getTmpL() {
        return tmpL;
    }

    public JPanel getPnlCenter() {
        return pnlCenter;
    }

    public JPanel getPnlInputFrame() {
        return pnlInputFrame;
    }

    public Boolean getPageIsSettings() {
        return pageIsSettings;
    }

    public JPanel getPnlMenu() {
        return pnlMenu;
    }

    public Boolean getPageIsInputFrame() {
        return pageIsInputFrame;
    }

    public BottomBar getBottomBar() {
        return bottomBar;
    }

    // Setters
    public void setPageIsInputFrame(Boolean b) {
        pageIsInputFrame = b;
    }
}

/*
 * private void setTaAbout() { taAbout.setFont(new Font("sansserif", Font.BOLD,
 * 12)); taAbout.setBorder(new EmptyBorder(10, 10, 10, 10));
 * taAbout.setText("\nWoordjes\n\nVersion: " + version +
 * "\nAuthor: Jan-Bart.be\n" + "Date: " + date); taAbout.setOpaque(false);
 * taAbout.setForeground(Color.white); taAbout.setEnabled(false);
 * hud.setContentPane(taAbout); hud.getJDialog().setSize(200, 150); // width,
 * length hud.getJDialog().setLocationRelativeTo(null);
 * hud.getJDialog().setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
 * hud.getJDialog().setVisible(true); }
 */
