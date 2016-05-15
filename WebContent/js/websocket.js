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
var socket;
var registered = false;

function startClient() 
{
	var wsUri;
	console.log("opening socket");
	
	/* Localhost. */
	if (document.domain == "127.0.0.1")
		wsUri = "ws://127.0.0.1:8080/snake/game";
	
	/* My private server. */
	else if (document.domain == "tlds.ddns.net")
		wsUri = "ws://" + document.location.hostname + ":8080/snake/game";
	
	/* OpenShift with Wildfly. */
	else
		wsUri = "ws://" + document.location.hostname + ":8000"+ document.location.pathname + "/../game";

	socket = new WebSocket(wsUri);

	/* When the connection is opened, login. */
	socket.onopen = function()
	{
		console.log("Opened socket.");
		var nickname = $("#txtName").val();
		socket.send("connect|" + nickname);
		$(document).keydown(getSnakeMovement);
	};

	/* When received a message, parse it and either add/remove user or post message. */
	socket.onmessage = function(a)
	{
		/* Process the message here. */
		console.log("received message: " + a.data);
		var message = a.data.split("|");	
		
		switch(message[0])
		{
			case "yourSnake":
				addSnake(message[1]);
				break;
				
			case "tryAnotherName":
				displayMessage("red", "There is already someone with that name, please try another!", 5000, "top");
				break;
				
			case "serverFull":		
				displayMessage("red", "The server is full, try again later! :\'(", 5000, "top");
				break;
				
			case "newPlayer":
				var nick = message[1];
				var color = JSON.parse(message[2]).snake.colorBody;
				displayMessage("green", "~~ <b><font color=\"" + color + "\">" + nick + "</font></b> entered in the world! ~~", 5000, "top");
				newPlayer(message[2]);
				break;
				
			case "removePlayer":
				var nick = message[1];
				displayMessage("red", "~~ <b>" + nick + "</b> left the world! ~~", 5000, "top");
				removePlayer(nick);
				break;
				
			case "updatePlayer":
				updatePlayer(message[1]);
				break;
				
			case "hello":
				addPlayer(message[1]);
				break;
				
			case "newApple":
				updateApple(message[1]);
				break;
				
			case "endGame":
				announceWinner(message[1]);
				socket.close();
				break;
		}
	};
			
	socket.onclose = function()
	{
		console.write("Closed socket.");
	};
		
	socket.onerror = function()
	{
		console.write("Error during transfer.");
	};
}

function displayMessage(color, msg, time, align)
{
	var type;
	
	switch(color)
	{
		case "red":
			type = "error";
			break;
			
		case "yellow":
			type = "warning";
			break;
			
		case "green":
			type = "success";
			break;
			
		case "blue":
			type = "information";
			break;
			
		default:
			type = "success";
			break;
	}
	
	if (align == "top")
	{
		var noty_id = noty({
			layout: 'topCenter',
			type: type,
	   		text: msg,
	   		theme: 'relax',
			timeOut: 500,
		});
	    
		setTimeout(function(){
	  		$(".noty_message").click();
		}, 500+time);
	}
	else if (align == "center")
	{
		var noty_id = noty({
			layout: 'center',
			type: type,
	   		text: msg,
	   		theme: 'relax',
			timeOut: 500,
		});
	    
		setTimeout(function(){
	  		$(".noty_message").click();
		}, 500+time);
	}
}

function sendMessage(msg)
{
	if (msg != null && msg != "")
		socket.send(msg);
}

$(document).ready(function()
{
	$('#btnPlay').click(function()
	{
		if ($("#txtName").val())
			startClient();
		else
			alert("Plz, type your nickname!");
	});
});