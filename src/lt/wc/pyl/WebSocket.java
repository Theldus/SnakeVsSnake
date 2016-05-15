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

import java.io.IOException;
import java.util.HashSet;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import javax.websocket.CloseReason;
import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;

import lt.wc.pyl.Snake.*;
import lt.wc.pyl.Snake.Food.Apple;

@ServerEndpoint("/game")
public class WebSocket
{
	private static Set<Session> conns = java.util.Collections
			.synchronizedSet(new HashSet<Session>());
	
	private static Map<Session, String> playerSession = new ConcurrentHashMap<Session, String>();
	private static Map<String, Snake> playerSnake = new ConcurrentHashMap<String, Snake>();
	private static Apple apple = null;
	
	private Session currentSession;

	public WebSocket()
	{
		Logging.println("WebSocket Constructed!");
	}

	@OnOpen
	public void onOpen(Session session)
	{
		Logging.println("WebSocket opened session: " + session.getId());
		conns.add(session);
		this.currentSession = session;
	}

	@OnMessage
	public void onMessage(String message)
	{
		String msg[] = message.split("\\|");
		Logging.println("Received: " + message);
		
		switch(msg[0])
		{
			/* onPlayerConnect. */
			case "connect":
				addPlayer(msg[1]);
				break;
			
			/* onPlayerUpdate. */
			case "updateData":
				Snake s = Snake.getSnake(msg[1]);
				if ((s.pieces.size()-1) != Game.snakeLength)
					broadcastMessage("updatePlayer|" + msg[1], playerSession.get(currentSession));
				else
					broadcastMessage("endGame|" + s.playerName, null);
				break;
			
			/* onEnemyConnect. */
			case "iAM":
				sendMessage("hello|" + msg[2], null, msg[1]);
				break;
			
			/* onEatApple. */
			case "updateApple":
				updateApple();
				break;
		}
	}

	@OnClose
	public void onClose(Session session, CloseReason reason)
	{		
		String nick = playerSession.get(session);
		conns.remove(session);
		playerSession.remove(session);
		
		if (nick != null)
		{
			playerSnake.remove(nick);
			broadcastMessage("removePlayer|" + nick, null);
		}
		
		Logging.println("Closing a WebSocket due to, code: " + reason.getCloseCode() + ", see u later " + nick);
	}

	@OnError
	public void onError(Session session, Throwable throwable)
	{
		String nick = playerSession.get(session);
		conns.remove(session);
		playerSession.remove(session);
		
		if (nick != null)
		{
			playerSnake.remove(nick);
			broadcastMessage("removePlayer|" + nick, null);
		}
		
		Logging.println("An error has ocurred in WebSocket, removing " + nick + ", see u later");
	}
	
	/**
	 * Adds the new user to the server.
	 * @param username Player name
	 */
	private void addPlayer(String username)
	{
		if ( !playerSnake.containsKey(username) && !playerSession.containsKey(currentSession) )
		{
			Snake snake = Game.generateSnake(playerSnake, username);
			if (snake != null)
			{
				/* Sends his snake for everyone. */
				broadcastMessage("newPlayer|" + username + "|" + snake, null);
				
				/* Sends the snake to him. */
				playerSession.put(currentSession, username);
				playerSnake.put(username, snake);
				
				sendMessage("yourSnake|" + snake, currentSession, null);

				Logging.println("User " + username + " entered in the world!");
				Logging.println("json: "+ snake);
				
				/* Generate the first Apple or not */
				if (playerSession.size() == 1 && apple == null)
				{
					apple = Apple.getRandomApple();
					broadcastMessage("newApple|" + apple, null);
					Logging.println("Generating new apple...");
				}
				else
					sendMessage("newApple|" + apple, currentSession, null);
			}
			else
			{
				sendMessage("serverFull", currentSession, null);
				Logging.println("Impossible to get an Snake, probably server is full?");
			}
		}
		else
		{
			sendMessage("tryAnotherName", currentSession, null);
			Logging.println("Attempting to use an existent name: " + username);
		}
	}
	
	/**
	 * Update all the apples of the players.
	 */
	private void updateApple()
	{
		apple = Apple.getRandomApple();
		broadcastMessage("newApple|" + apple, null);
	}
	
	/**
	 * Broadcast an string for all users if ignoreUser is null, otherwise<br>
	 * broadcast to all but him.
	 * @param message Message to send.
	 * @param ignoreUser User to be ignored or null.
	 */
	private void broadcastMessage(String message, String ignoreUser)
	{
		if (ignoreUser != null)
		{
			for (Entry<Session, String> entry : playerSession.entrySet())
			{
				try
				{
					if (!entry.getValue().equals(ignoreUser))
						entry.getKey().getBasicRemote().sendText(message);
				}
				catch (IOException ioe)
				{
					Logging.println("Failed while broadcasting this message: " + message);
				}
			}
		}
		else
		{
			for (Entry<Session, String> entry : playerSession.entrySet())
			{
				try
				{
					entry.getKey().getBasicRemote().sendText(message);
				}
				catch (IOException ioe)
				{
					Logging.println("Failed while broadcasting this message: " + message);
				}
			}
		}
	}
	
	/**
	 * Sends an message by an specific user, if session is not null<br>
	 * sends the message by using session, otherwise, uses the name<br>
	 * provided.
	 * @param message Message to be send.
	 * @param session Users session.
	 * @param name Users name.
	 */
	private void sendMessage(String message, Session session, String name)
	{
		if (session != null)
		{
			try
			{
				session.getBasicRemote().sendText(message);
			}
			catch (IOException ioe)
			{
				Logging.println("Failed while sending this message: " + message);
			}
		}
		else
		{
			for (Entry<Session, String> entry : playerSession.entrySet())
			{
				try
				{
					if (entry.getValue().equals(name))
					{
						entry.getKey().getBasicRemote().sendText(message);
						break;
					}
				}
				catch (IOException ioe)
				{
					Logging.println("Failed while broadcasting this message: " + message);
				}
			}
		}
	}
}
