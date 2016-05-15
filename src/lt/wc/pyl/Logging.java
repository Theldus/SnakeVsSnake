/*
Snake vs Snake - An Snake multiplayer game.
Copyright (C) 2016  Davidson Francis <davidsondfgl@gmail.com>

This program is free software: you can redistribute it and/or modify
it under the terms of the GNU Affero General Public License as
published by the Free Software Foundation, either version 3 of the
License, or (at your option) any later version.

This program is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU Affero General Public License for more details.

You should have received a copy of the GNU Affero General Public License
along with this program.  If not, see <http://www.gnu.org/licenses/>.
*/
package lt.wc.pyl;

/*
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
*/

/**
 * Loggin class, should provide an simple interface<br>
 * to generate a log file.
 * @author Davidson Francis
 */
public class Logging
{
	/*
	 * File to be recorded.
	 */
	/*
	private static final String FILE = "log";
	
	private static FileWriter fw;
	private static BufferedWriter bw;
	private static PrintWriter out;
	*/
	
	/**
	 * Write in standard output and in the log file<br>
	 * the message providade by <i>txt</i>.
	 * @param txt Message.
	 */
	public static void println(String txt)
	{
		/*
		I was thinking about how to see the file on a Wildfly/JBOSS/TomCAT server :/
		
		if (fw == null)
		{
			try
			{
				fw = new FileWriter(FILE, true);
				bw = new BufferedWriter(fw);
				out = new PrintWriter(bw);
			}
			catch(IOException ioe)
			{
				System.err.println("An error ocurred opening the file: " + FILE);
			}
		}
		*/
		
		String text = DateHelper.getDate() + ": " + txt;
		System.out.println(text);
		
		/*
		out.println(text);
		out.flush();
		*/
	}
}
