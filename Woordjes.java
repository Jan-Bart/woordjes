package woordjes;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.SplashScreen;
import java.io.UnsupportedEncodingException;

/**
 * This is a little program that let you practice your grammar.
 *
 * @author Jan-Bart Vervoort
 *
 */
public class Woordjes {

    /**
     * TODO:
     * - Verwijder een woord uit de lijst als je het kent
     *
     */
    private static final long serialVersionUID = 3362510925477346867L;
    public static final String VERSION = "0.7.0";
    public static final String DATE = "2011-01-03";

    static void renderSplashFrame(Graphics2D g, int frame) {
        final String[] comps = {"Words", "Settings", "Layout"};
        //final String[] comps = {java.util.ResourceBundle.getBundle("translations/Bundle").getString("WORDS"), java.util.ResourceBundle.getBundle("translations/Bundle").getString("SETTINGS"), java.util.ResourceBundle.getBundle("translations/Bundle").getString("LAYOUT")};
        g.setComposite(AlphaComposite.Clear);
        g.fillRect(80, 140, 200, 40);//x,y,w,h
        g.setPaintMode();
        g.setColor(Color.BLACK);
        //g.drawString(java.util.ResourceBundle.getBundle("translations/Bundle").getString("LOADING: ") + comps[(frame / 50) % 3] + "...", 80, 150);
        g.drawString("Loading: " + comps[(frame / 50) % 3] + "...", 80, 150);
    }

    public Woordjes() {
    }

    public static void main(String[] args) throws UnsupportedEncodingException {
        // Splash
        final SplashScreen splash = SplashScreen.getSplashScreen();
        if (splash == null) {
            System.out.println("SplashScreen.getSplashScreen() returned null");
            return;
        }
        Graphics2D g = splash.createGraphics();
        if (g == null) {
            System.out.println(java.util.ResourceBundle.getBundle("translations/Bundle").getString("G IS NULL"));
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

        Gui gui = new Gui(VERSION, DATE);
    }
}
