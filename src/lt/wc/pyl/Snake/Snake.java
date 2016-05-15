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

import java.util.ArrayList;
import org.json.JSONObject;
import org.json.JSONArray;

/**
 * Snake POJO for mapping JavaScript/Java
 * @author Davidson Francis
 */
public class Snake
{
	/*
	 * Snake space between your 'pieces'.
	 */
	public static final int space = 3;
	
	/*
	 * Width of each piece.
	 */
	public static final int width = 10;
	
	/*
	 * Height of each piece.
	 */
	public static final int height = 10;
	
	public ArrayList<Piece> pieces;
	public String colorBody;
	public String colorHead = "#00d975";
	public String playerName;
	
	/**
	 * Default constructor.
	 */
	public Snake()
	{
		pieces = new ArrayList<Piece>();
	}

	/**
	 * Gets an snake object by an JavaScript json
	 * @param jsonSnake snake's json.
	 * @return Snake object from json.
	 */
	public static Snake getSnake(String jsonSnake)
	{
		Snake s = new Snake();
		JSONObject job = new JSONObject(jsonSnake);

		job = job.getJSONObject("snake");
		s.colorBody = job.getString("colorBody");
		s.colorHead = job.getString("colorHead");
		s.playerName = job.getString("playerName");
		
		JSONArray jar = job.getJSONArray("pieces");
		for(int i = 0; i < jar.length(); i++)
		{
			int x,y,dir;
			job = jar.getJSONObject(i).getJSONObject("piece");
			
			x = job.getInt("x");
			y = job.getInt("y");
			dir = job.getInt("dir");

			s.pieces.add( new Piece(x,y,dir) );
		}
		return s;
	}
	
	/**
	 * Gets an snake json by this object.
	 * @return Snake json.
	 */
	public String toString()
	{
		JSONObject job = new JSONObject();
		JSONObject jobMain = new JSONObject();

		job.put("space", Snake.space);
		job.put("width", Snake.width);
		job.put("height", Snake.height);

		JSONArray jar = new JSONArray();
		for(int i = 0; i < this.pieces.size(); i++)
		{
			JSONObject piece = new JSONObject();
			JSONObject elements = new JSONObject();

			elements.put("x", this.pieces.get(i).x);
			elements.put("y", this.pieces.get(i).y);
			elements.put("dir", this.pieces.get(i).dir);
			piece.put("piece",elements);

			jar.put(piece);
		}

		job.put("pieces", jar);
		job.put("colorBody", this.colorBody);
		job.put("colorHead", this.colorHead);
		job.put("playerName", this.playerName);

		jobMain.put("snake", job);
		return jobMain.toString();
	}
}
