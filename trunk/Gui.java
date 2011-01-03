package woordjes;

import com.explodingpixels.macwidgets.IAppWidgetFactory;
import com.explodingpixels.macwidgets.HudWindow;
import javax.swing.JTextArea;
import java.awt.AlphaComposite;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics2D;
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
import com.explodingpixels.macwidgets.SourceList;
import com.explodingpixels.macwidgets.SourceListCategory;
import com.explodingpixels.macwidgets.SourceListItem;
import com.explodingpixels.macwidgets.SourceListModel;
import java.awt.AWTException;
import java.awt.Robot;
import java.awt.SplashScreen;
import java.awt.event.KeyEvent;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.KeyStroke;
import static java.awt.event.InputEvent.CTRL_DOWN_MASK;

public class Gui extends JFrame {

    private static final long serialVersionUID = 1L;
    public JFrame frame;
    // Classes
    public Words woordjes;
    public SettingsPanel settingsPanel;
    public InputPanel inputPanel;
    // Menu
    private JPanel pnlMenu;
    private JMenuBar mbMenu;
    private JMenu mFile;
    private JMenuItem miNew, miOpen, miHighscores, miExit;
    private JMenu mVertaling, mEdit;
    private JMenuItem miAtoB, miBtoA, miCreateList;
    private JMenuItem miTest;
    private JMenu mHelp;
    private JMenuItem miHelp, miCheckUpdate;
    // Woordjes Layout
    private JPanel pnlCenter, pnlCenterS, pnlWords, pnlWoord, pnlVertaling;
    private JLabel lblWoord, lblVertaling;
    private JTextField tfInput;
    private JButton btnCheck, btnTip, btnSettings, btnNewGame;
    private JLabel lblMelding;
    // InputFrame (create new lists with words)
    private SourceListModel model;
    private SourceListCategory category;
    private SourceList sourceList;
    // EndGameFrame
    private JPanel pnlEndGame;
    private MyTableModel myModel;
    private JTable table;
    private JLabel lblTitleResult;
    //bottom
    private BottomBar bottomBar;
    // Special Characters
    private JPanel pnlChars;
    private JButton btnAaigu, btnAgrave, btnAcirconflexe, btnEaigu, btnEgrave,
            btnEcirconflexe;
    private JButton btnIcirconflexe, btnCcedille;
    // Help
    private JTextArea taAbout;
    // Variabels
    private JFileChooser chooser;
    public int currentWord = 1;
    public int fout, res;
    private int fontSize;
    private String fontName;
    private int fontStyle;
    private String version, date;
    private boolean isFout;
    private boolean endOfList = false;
    private boolean dontJumpLeft = true;
    private boolean pageIsSettings, pageIsInputFrame, pageIsEndGame, showChars;
    
    Dimension withoutChar = new Dimension(560, 350); // width, length
    Dimension withChar = new Dimension(550, 460);

    static void renderSplashFrame(Graphics2D g, int frame) {
        final String[] comps = {"Woorden", "Instellingen", "Layout"};
        //final String[] comps = {java.util.ResourceBundle.getBundle("woordjes/NL").getString("WORDS"), java.util.ResourceBundle.getBundle("woordjes/NL").getString("SETTINGS"), java.util.ResourceBundle.getBundle("woordjes/NL").getString("LAYOUT")};
        g.setComposite(AlphaComposite.Clear);
        g.fillRect(80, 140, 200, 40);//x,y,w,h
        g.setPaintMode();
        g.setColor(Color.BLACK);
        //g.drawString(java.util.ResourceBundle.getBundle("woordjes/NL").getString("LOADING: ") + comps[(frame / 50) % 3] + "...", 80, 150);
        g.drawString("Laden: " + comps[(frame / 50) % 3] + "...", 80, 150);
    }

    public Gui(String version, String date) throws UnsupportedEncodingException {     
        // Splash
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

        this.version = version;
        this.date = date;
        // Create classes
        woordjes = new Words();
        settingsPanel = new SettingsPanel();
        inputPanel = new InputPanel();

        // Create the frame.
        frame = new JFrame("Woordjes");
        // Set the frame icon to an image loaded from a file.
        java.net.URL imgURL = Gui.class.getResource("icon.gif");
        frame.setIconImage(new ImageIcon(imgURL).getImage());
        
        woordjes.laadWoorden(woordjes.wordlist);
        woordjes.randomize();
        
        initialiseerComponenten();
        showStartscreen();
        initialiseerActionlisteners();

        lblWoord.setFont(new Font("sansserif", Font.BOLD, 15));
        lblVertaling.setFont(new Font("sansserif", Font.BOLD, 15));
        lblWoord.setText(woordjes.getWoord());
        if (!woordjes.msg.isEmpty()) {
            JOptionPane.showMessageDialog(Gui.this, woordjes.toString());
        }
        
        frame.setSize(withoutChar);
        // Center frame on screen
        Toolkit toolkit = Toolkit.getDefaultToolkit();
        Dimension ssize = toolkit.getScreenSize();
        int x = (int) (ssize.getWidth() - getWidth()) / 2;
        int y = (int) (ssize.getHeight() - getHeight()) / 2;
        frame.setLocation(x, y);
        frame.setDefaultCloseOperation(EXIT_ON_CLOSE);
        frame.setVisible(true);
        toFront();
    }

    private void initialiseerComponenten() {
        // menu
        pnlMenu = new JPanel();
        mbMenu = new JMenuBar();
        //mFile = new JMenu("<html><u>F</u>ile");
        mFile = new JMenu("File");
        chooser = new JFileChooser();
        miNew = new JMenuItem("Start again");
        miOpen = new JMenuItem("Open file");
        miHighscores = new JMenuItem("Highscores");
        miExit = new JMenuItem("Exit");
        mVertaling = new JMenu("Translation");
        miAtoB = new JMenuItem("A -> B");
        miBtoA = new JMenuItem("B -> A");
        mEdit = new JMenu("Edit");
        miCreateList = new JMenuItem("Compose list");
        mHelp = new JMenu("Help");
        miHelp = new JMenuItem("Help contents");
        miTest = new JMenuItem("Test");
        miCheckUpdate = new JMenuItem("Check for updates");
        //
        taAbout = new JTextArea();
        // Center
        pnlCenter = new JPanel();
        pnlCenterS = new JPanel();
        pnlWoord = new JPanel();
        pnlWords = new JPanel();
        pnlVertaling = new JPanel();
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
        
        // EndGameFrame
        pnlEndGame = new JPanel();
        myModel = new MyTableModel();
        table = new JTable(myModel);
        btnNewGame = new JButton("Speel opnieuw");
        lblTitleResult = new JLabel("Result"); // resultaat
        // JTextFields
        
        model = new SourceListModel();
        category = new SourceListCategory("Woordenlijst");
        model.addCategory(category);
        model.addItemToCategory(new SourceListItem("Frans 1"), category);
        sourceList = new SourceList(model);
        
        // Bottom
        bottomBar = new BottomBar(BottomBarSize.LARGE);
    }

    private void layoutComponenten() {
        frame.add(pnlCenter, BorderLayout.CENTER);
        frame.add(pnlMenu, BorderLayout.NORTH);
        frame.add(bottomBar.getComponent(), BorderLayout.SOUTH);
        // MenuBar
        pnlMenu.setLayout(new GridLayout(1, 1)); // rows, columns
        mbMenu.setBorder(new EmptyBorder(0, 10, 0, 0)); // t,l,b,r
        pnlMenu.setForeground(Color.white);
        mbMenu.setForeground(Color.black);
        pnlMenu.add(mbMenu);
        mbMenu.add(mFile);
        mFile.setMnemonic(KeyEvent.VK_F);
        mFile.add(miNew);
        miNew.setAccelerator(KeyStroke.getKeyStroke('N', CTRL_DOWN_MASK));
        mFile.add(miOpen);
        miOpen.setAccelerator(KeyStroke.getKeyStroke('O', CTRL_DOWN_MASK));
        mFile.add(miExit);
        miExit.setAccelerator(KeyStroke.getKeyStroke('Q', CTRL_DOWN_MASK));
        // mFile.addSeparator();
        mbMenu.add(mVertaling);
        mVertaling.setMnemonic(KeyEvent.VK_T);
        mVertaling.add(miAtoB);
        mVertaling.add(miBtoA);
        mbMenu.add(mEdit);
        mEdit.setMnemonic(KeyEvent.VK_E);
        mEdit.add(miCreateList);
        mbMenu.add(mHelp);
        mHelp.setMnemonic(KeyEvent.VK_H);
        mHelp.add(miHelp);
        mHelp.add(miCheckUpdate);
        // Center Panel
        pnlCenter.setLayout(new GridLayout(2, 1)); // rows, columns
        pnlCenter.add(pnlWords);
        pnlCenter.add(pnlCenterS);
        pnlCenter.setBorder(new EmptyBorder(10, 10, 10, 10)); // t,l,b,r
        // Woord Panel
        pnlWords.setLayout(new GridLayout(2, 1));
        pnlWords.setBackground(Color.white);
        // Create panel to show Words
        pnlWoord.add(lblWoord);
        pnlVertaling.add(lblVertaling);
        pnlWords.setLayout(new GridLayout(2, 1)); // r, c
        pnlWoord.setBackground(Color.white);
        pnlVertaling.setBackground(Color.white);
        pnlWords.add(pnlWoord);
        pnlWords.add(pnlVertaling);
        lblWoord.setHorizontalTextPosition(JLabel.CENTER);
        lblWoord.setVerticalTextPosition(JLabel.BOTTOM);
        lblWoord.setBorder(BorderFactory.createEmptyBorder(pnlWoord.getHeight() / 2, 0, 0, 0)); //top,left
        lblVertaling.setHorizontalTextPosition(JLabel.CENTER);
        // Create the inputfield and (if needed) special characters
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
        tfInput.setBorder(BorderFactory.createLoweredBevelBorder());
        //UnifiedToolBar toolBar = new UnifiedToolBar();
        btnSettings.putClientProperty("JButton.buttonType", "textured");
        // toolBar.addComponentToLeft(button);

        if (dontJumpLeft) {
            bottomBar.addComponentToLeft(btnSettings);
            bottomBar.addComponentToLeft(lblMelding, 0);
        } else {
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
        // Show Help files
        miHelp.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                setTaAbout();
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
                    frame.getContentPane().add(settingsPanel.getPnlSettings(), BorderLayout.CENTER);
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
        settingsPanel.getChkRandomWords().addActionListener(new ActionListener() {

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
        settingsPanel.getChkChars().addActionListener(new ActionListener() {

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
        inputPanel.getBtnSaveList().addActionListener(new ActionListener() {

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
        // Set fontsize
        settingsPanel.getCbFontsize().addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                fontSize = (Integer) settingsPanel.getCbFontsize().getSelectedItem();
                setFont(fontSize);

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
        frame.getContentPane().removeAll();
        getSourceList().getComponent().setPreferredSize(new Dimension(150, 220)); // w,h
        frame.getContentPane().add(getBottomBar().getComponent(),
                BorderLayout.PAGE_END);
        // TODO: Remove for production if it doesn't work
        /*frame.getContentPane().add(getSourceList().getComponent(),
        BorderLayout.LINE_START);*/
        frame.getContentPane().add(inputPanel.getPnlInputFrame(), BorderLayout.CENTER);
        frame.getContentPane().add(getPnlMenu(), BorderLayout.PAGE_START);
        // pnlInputFrame.setLayout(new GridLayout((nrOfImputFields/2)+2, 3, 10,
        // 0)); // rijen, kolommen, H-space,-V-space
        lblMelding.setText("");
        getBtnSettings().setText(woordjes.getUiTranslation(1));
        frame.getContentPane().repaint();
        frame.validate();
        setPageIsInputFrame(true);
    }

    private void createEndGame() {
        String strAorB = "";

        frame.getContentPane().removeAll();
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

        for (int i = 0; i < inputPanel.getNrOfImputFields(); i = i + 2) {
            if (!inputPanel.getTmp()[i].getText().isEmpty()) {
                words = words + inputPanel.getTmp()[i].getText() + ";" + inputPanel.getTmp()[i + 1].getText()
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

    private void setTaAbout() {
        JScrollPane scrollPane = new JScrollPane(taAbout);
        IAppWidgetFactory.makeIAppScrollPane(scrollPane);

        taAbout.setBorder(new EmptyBorder(10, 10, 10, 10));
        taAbout.setText("\nWoordjes\n\nPress alt \nto use menu \nshortcuts");
        taAbout.setForeground(Color.white);
        taAbout.setEnabled(false);  // Prevent users to edit help content
        HudWindow hud = new HudWindow("Help");

        hud.setContentPane(scrollPane);
        scrollPane.setBackground(Color.darkGray);
        taAbout.setBackground(Color.darkGray);

        hud.getJDialog().setLocationRelativeTo(null);
        hud.getJDialog().setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        hud.getJDialog().setVisible(true);
        hud.getJDialog().setSize(200, 150); // width,length
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
        } else {
            // Word is wrong
            if (!isFout) {
                fout++;
            }
            isFout = true;
            lblVertaling.setForeground(Color.red);
            lblVertaling.setText("\n" + woordjes.getVertaling());
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

    // Getters
    public SourceList getSourceList() {
        return sourceList;
    }

    public JLabel getLblMelding() {
        return lblMelding;
    }

    public JButton getBtnSettings() {
        return btnSettings;
    }

    public JPanel getPnlCenter() {
        return pnlCenter;
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

    public void setFont(int fontsize) {
        fontName = "sansserif";
        fontStyle = Font.PLAIN;
        fontSize = fontsize;
        Font f = new Font(fontName, fontStyle, fontSize);
        // Menu
        mFile.setFont(f);
        miNew.setFont(f);
        mVertaling.setFont(f);
        miOpen.setFont(f);
        miHighscores.setFont(f);
        miExit.setFont(f);
        miAtoB.setFont(f);
        miBtoA.setFont(f);
        mEdit.setFont(f);
        miCreateList.setFont(f);
        miTest.setFont(f);
        mHelp.setFont(f);
        miHelp.setFont(f);
        miCheckUpdate.setFont(f);
        // Help
        taAbout.setFont(f);
        tfInput.setFont(f);
        btnSettings.setFont(f);
        lblWoord.setFont(new Font("sansserif", Font.BOLD, fontSize));
        lblVertaling.setFont(new Font("sansserif", Font.BOLD, fontSize));
        // End game
        lblTitleResult.setFont(f);
        table.setFont(f);
        btnNewGame.setFont(f);
        // BottomBar
        lblMelding.setFont(f);
        // Edit Panel
        inputPanel.getLblWord().setFont(f);
        inputPanel.getLblSeparator0().setFont(f);
        inputPanel.getLblTranslation().setFont(f);
        inputPanel.getBtnSaveList().setFont(f);
        // Special Characters
        btnAaigu.setFont(f);
        btnAgrave.setFont(f);
        btnAcirconflexe.setFont(f);
        btnEaigu.setFont(f);
        btnEgrave.setFont(f);
        btnEcirconflexe.setFont(f);
        btnIcirconflexe.setFont(f);
        btnCcedille.setFont(f);
    }
}
