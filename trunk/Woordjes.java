package woordjes;

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
         * - Set min-size by inputFieldframe. To prevent inputFields becoming to
	 *   small to type
         * - Verwijder een woord uit de lijst als je het kent
         *
	 */
	private static final long serialVersionUID = 3362510925477346867L;
	public static final String VERSION = "0.6.8";
	public static final String DATE = "2010-10-12";

	public Woordjes() {
	}

	public static void main(String[] args) throws UnsupportedEncodingException {
		 Gui gui =new Gui(VERSION, DATE);
	}
}