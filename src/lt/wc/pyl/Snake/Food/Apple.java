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
package lt.wc.pyl.Snake.Food;

import lt.wc.pyl.Game;

import org.json.JSONObject;

/**
 * Apple POJO for mapping JavaScript/Java
 * @author Davidson Francis
 */
public class Apple
{
	/*
	 * Space between the food.
	 */
	public static final int space = 3;
	
	/*
	 * Apple width.
	 */
	public static final int width = 10;
	
	/*
	 * Apple height.
	 */
	public static final int height = 10;
	
	/*
	 * Apple current position X.
	 */
	public int x;
	
	/*
	 * Apple current position Y.
	 */
	public int y;
	
	public Apple(){}
	
	/**
	 * Initializes the Apple.
	 * @param x Axis-X position.
	 * @param y Axis-Y position.
	 */
	public Apple(int x, int y)
	{
		this.x = x;
		this.y = y;
	}
	
	/**
	 * Gets an Apple object by an JavaScript json.
	 * @param jsonApple Apple's json.
	 * @return Apple object from json.
	 */
	public static Apple getApple(String jsonApple)
	{
		Apple apple = new Apple();
		JSONObject job = new JSONObject(jsonApple);
		apple.x = job.getInt("x");
		apple.y = job.getInt("y");	
		return apple;
	}
	
	/**
	 * Gets an random apple to the game.<br>
	 * This should be used when a player eats the current apple<br>
	 * and the server needs to re-criate an new apple to the game.
	 * @return Apple object.
	 */
	public static Apple getRandomApple()
	{
		int x = (((int)(Math.random()*Game.wWIDTH)) / (Apple.width+Apple.space))*(Apple.width+Apple.space);
		int y = (((int)(Math.random()*Game.wHEIGHT)) / (Apple.height+Apple.space))*(Apple.height+Apple.space);
		
		if (x == 0)
			x = 1;
		
		if (y == 0)
			y = 1;
			
		return new Apple(x,y);
	}
	
	/**
	 * Gets an Apple json by this object.
	 * @return Apple json.
	 */
	public String toString()
	{
		JSONObject job = new JSONObject();
		job.put("x", this.x);
		job.put("y", this.y);
		job.put("width", Apple.width);
		job.put("height", Apple.height);
		return job.toString();
	}
}
