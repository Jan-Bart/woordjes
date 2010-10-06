package woordjes;

import java.io.UnsupportedEncodingException;

/**
 * This is a little program that let you practice your grammar.
 *
 * @author Jan-Bart Vervoort
 * @version 2010-09-08 (v0.6)
 *
 */

public class Woordjes {
	/**
	 * TODO:
         * - Set min-size by inputFieldframe. To prevent inputFields becoming to
	 *   small to type
         * - Verwijder een woord uit de lijst als je het kent
         * - If users deleted the last used file, Woordjes can't use it.
         *   So check for that.
         *
	 */
	private static final long serialVersionUID = 3362510925477346867L;
	public static final String VERSION = "0.6.6";
	public static final String DATE = "2010-09-25";

	public Woordjes() {
	}

	public static void main(String[] args) throws UnsupportedEncodingException {
		 Gui gui =new Gui(VERSION, DATE);
	}
}