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
package lt.wc.pyl.Snake;

/**
 * Snake's part.
 * @author Davidson Francis
 */
public class Piece
{
	/*
	 * Current X-Axis position.
	 */
	public int x;
	
	/*
	 * Current Y-Axis position.
	 */
	public int y;
	
	/*
	 * Current direction, @see Direction.
	 */
	public int dir;
	
	/**
	 * Initialize the Piece.
	 * @param x X-Axis position.
	 * @param y Y-Axis position.
	 * @param dir Direction.
	 */
	public Piece(int x, int y, int dir)
	{
		this.x = x;
		this.y = y;
		this.dir = dir;
	}
}
