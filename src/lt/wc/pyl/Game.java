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

import lt.wc.pyl.Snake.Direction;
import lt.wc.pyl.Snake.Piece;
import lt.wc.pyl.Snake.Snake;

import java.util.Map;
import java.util.Map.Entry;

/**
 * Manages the general logic of the game.
 * @author Davidson Francis.
 */
public class Game
{
	/*
	 * Snake colors.
	 */
	public static final String[] COLORS =
	{
		"#00B1FF",
		"#FFA600",
		"#FF0400",
		"#B700FF",
		"#FF00E1",
		"#F6FF00"
	};
	
	/*
	 * Max capacity for the server.
	 */
	public static final int CAPACITY = COLORS.length;
	
	/*
	 * World width.
	 */
	public static final int wWIDTH = 598;
	
	/*
	 * World height.
	 */
	public static final int wHEIGHT = 299;
	
	/*
	 * Max snake length.
	 */
	public static final int snakeLength = 30;
	
	/**
	 * Gets an snake based to below parameters.
	 * @param players Map of players from all the game.
	 * @param name Player name.
	 * @return Snake prepared to be used.
	 */
	public static Snake generateSnake(Map<String, Snake> players, String name)
	{
		if (players.size() < CAPACITY)
		{
			int unique;
			String color;
			Snake snake = new Snake();
			Piece piece = new Piece(0,0,Direction.RIGHT);
			
			do
			{
				unique = 0;
				int num = (int)(Math.random()*((double)COLORS.length));
				color = new String( COLORS[num] );
				
				for (Entry<String, Snake> entry : players.entrySet())
				{
					if (entry.getValue().colorBody.equals(color))
					{
						unique++;
						break;
					}
				}
				
			}while(unique > 0);
			
			snake.playerName = name;
			snake.colorBody = color;
			snake.pieces.add(piece);
			
			return snake;
		}
		else
			return null;
	}
}
