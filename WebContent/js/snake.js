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

/* Context canvas. */
var ctx;

/* World */
var wWidth;
var wHeight;

var gWidth;
var gHeight;

var intervalID;

/* Food. */
var Apple =
{
	x : 0,
	y : 0,
	width : 0,
	height : 0
};

var appleImage;

/* Directions. */
var Direction = {UP : 1, DOWN : 2, LEFT : 3, RIGHT : 4};
Object.freeze(Direction);

/* -- Snake -- */
var startSize = 1;

var Snake =
{
	space : 3,
	width : 10,
	height : 10,
	pieces : [],
	colorBody : "#00b1ff",
	colorHead : "#00d975",
	playerName : "Undefined"
};

var Player =
{
	snake : null
};

var Players = [];

/* Init the game. */
function init()
{
	var canvas = document.getElementById("world");
	ctx = canvas.getContext("2d");
	ctx.fillStyle = "#00b1ff";
    Player.snake = Snake;
	
	wWidth = document.getElementById("world").scrollWidth;
	wHeight = document.getElementById("world").scrollHeight;
	
	gWidth = wWidth / Snake.width;
	gHeight = wHeight / Snake.height;
		
	Apple.x = parseInt( parseInt(Math.random()*wWidth) / (Snake.width+Snake.space) ) * (Snake.width+Snake.space);
	Apple.y = parseInt( parseInt(Math.random()*wHeight) / (Snake.height+Snake.space) ) * (Snake.height+Snake.space);
	
	appleImage = new Image();
	appleImage.src = "imgs/apple.png";
	
	Apple.width = appleImage.width;
	Apple.height = appleImage.height;
}

/* Key down event. */
function getSnakeMovement(evt)
{
	switch(evt.keyCode)
	{
		case 38:
		case 87:
			if(Snake.pieces[0].piece.dir != Direction.DOWN)
			{
				Snake.pieces[0].piece.dir = Direction.UP;
				sendMessage("updateData|" + JSON.stringify(Player));
			}
			break;
			
		case 40:
		case 83:
			if(Snake.pieces[0].piece.dir != Direction.UP)
			{
				Snake.pieces[0].piece.dir = Direction.DOWN;
				sendMessage("updateData|" + JSON.stringify(Player));
			}
			break;
			
		case 37:
		case 65:
			if(Snake.pieces[0].piece.dir != Direction.RIGHT)
			{
				Snake.pieces[0].piece.dir = Direction.LEFT;
				sendMessage("updateData|" + JSON.stringify(Player));
			}
			break;
			
		case 39:
		case 68:
			if(Snake.pieces[0].piece.dir != Direction.LEFT)
			{
				Snake.pieces[0].piece.dir = Direction.RIGHT;
				sendMessage("updateData|" + JSON.stringify(Player));
			}
			break;
			
		default:
			break;
	}
}

/* Start the game. */
function start()
{ return intervalID = setInterval(draw, 125); }

/* Pause the game. */
function pause()
{ clearInterval(intervalID); }

/* ---------------------------------------------------------------------
 * Intersection algorithm, tnx e.James
 * http://stackoverflow.com/questions/306316/determine-if-two-rectangles-overlap-each-other
 * ---------------------------------------------------------------------*/
function valueInRange(value, min, max)
{ return (value >= min) && (value <= max); }

function rectOverlap(o1x, o1y, o1width, o1height, 
		o2x, o2y, o2width, o2height)
{
    xOverlap = valueInRange(o1x, o2x, o2x + o2width) ||
                    valueInRange( o2x, o1x, o1x + o1width);

    yOverlap = valueInRange(o1y, o2y, o2y + o2height) ||
                    valueInRange(o2y, o1y, o1y + o1height);

    return xOverlap && yOverlap;
}

/* Draws the game. */
function draw()
{
	ctx.clearRect(0,0,wWidth,wHeight);
	drawMe();
	drawOthers();
}

function drawMe()
{
	var i = 0;
	var j = 0;
	var k = 0;
	ctx.fillStyle = Snake.colorBody;
	
	/* Adjusts the position of rest of the body. */
	for (i = Snake.pieces.length - 1; i > 0; i--)
	{
		Snake.pieces[i].piece.x = Snake.pieces[i-1].piece.x;
		Snake.pieces[i].piece.y = Snake.pieces[i-1].piece.y;
		Snake.pieces[i].piece.dir = Snake.pieces[i-1].piece.dir;
		ctx.fillRect(Snake.pieces[i].piece.x,Snake.pieces[i].piece.y,Snake.width,Snake.height);
	}
	
	/* Re-adjust new position of head. */
	switch (Snake.pieces[0].piece.dir)
	{
		case Direction.RIGHT:
			Snake.pieces[0].piece.x += (Snake.width + Snake.space);
			if(Snake.pieces[0].piece.x > wWidth)
				Snake.pieces[0].piece.x = 0;
			break;
			
		case Direction.LEFT:
			Snake.pieces[0].piece.x -= (Snake.width + Snake.space);
			if(Snake.pieces[0].piece.x < 0)
				Snake.pieces[0].piece.x = wWidth - (Snake.width + Snake.space);
			break;
			
		case Direction.UP:
			Snake.pieces[0].piece.y -= (Snake.width + Snake.space);
			if(Snake.pieces[0].piece.y < 0)
				Snake.pieces[0].piece.y = wHeight - (Snake.height + Snake.space);
			break;
			
		case Direction.DOWN:
			Snake.pieces[0].piece.y += (Snake.width + Snake.space);
			if(Snake.pieces[0].piece.y > wHeight)
				Snake.pieces[0].piece.y = 0;
			break;
	}
	
	/* Draws the head. */
	ctx.fillStyle = Snake.colorHead;
	ctx.fillRect(Snake.pieces[0].piece.x,Snake.pieces[0].piece.y,Snake.width,Snake.height);
	
	/* Checks if the head is touching another body. */
	for (i = 0; i < Players.length; i++)
	{
		for (j = 0; j < Players[i].pieces.length; j++)
		{
			if ( rectOverlap(Players[i].pieces[j].piece.x,Players[i].pieces[j].piece.y,Snake.width,Snake.height,
					Snake.pieces[0].piece.x,Snake.pieces[0].piece.y,Snake.width,Snake.height) )
			{
				var length = Snake.pieces.length;
				displayMessage("red", "<b>You died</b>", 5000, "top");
				
				for (k = 0; k < length - 1; k++)
					Snake.pieces.pop();
				
				Snake.pieces[0].piece.x = 0;
				Snake.pieces[0].piece.y = 0;
				Snake.pieces[0].piece.dir = Direction.RIGHT;
				Player.snake = Snake;
				
				$(".user[data-user=" + Snake.playerName + "]").html("<b><font color=\"" + Snake.colorBody + "\">" + 
						Snake.playerName + " - " + (Snake.pieces.length-1) + "</font></b>");
				
				sendMessage("updateData|" + JSON.stringify(Player));
				console.log("I touched in " + Players[i].playerName + " :/");
			}
		}
	}
	
	
	/* Checks if the head is touching an food. */
	if ( rectOverlap(Apple.x,Apple.y,Apple.width,Apple.height,
			Snake.pieces[0].piece.x,Snake.pieces[0].piece.y,Snake.width,Snake.height) )
	{
		Snake.pieces.push( {piece:{ x: Snake.pieces[Snake.pieces.length-1].piece.x, 
							y : Snake.pieces[Snake.pieces.length-1].piece.y, 
							dir : Snake.pieces[Snake.pieces.length-1].piece.dir}} );
		
		Apple.x = -1;
		Apple.y = -1;
		
		$(".user[data-user=" + Snake.playerName + "]").html("<b><font color=\"" + Snake.colorBody + "\">" + 
				Snake.playerName + " - " + (Snake.pieces.length-1) + "</font></b>");
		
		sendMessage("updateData|" + JSON.stringify(Player));
		sendMessage("updateApple");
	}
	
	if (Apple.x > 0 && Apple.y > 0)
		ctx.drawImage(appleImage, Apple.x, Apple.y, Apple.width, Apple.height);
}

function drawOthers()
{
	var i = 0;
	var j = 0;
	
	/* Draws everybody. */
	for (i = 0; i < Players.length; i++)
	{
		ctx.fillStyle = Players[i].colorBody;
		
		/* Adjusts the position of rest of the body. */
		for (j = Players[i].pieces.length - 1; j > 0; j--)
		{
			Players[i].pieces[j].piece.x = Players[i].pieces[j-1].piece.x;
			Players[i].pieces[j].piece.y = Players[i].pieces[j-1].piece.y;
			Players[i].pieces[j].piece.dir = Players[i].pieces[j-1].piece.dir;
			ctx.fillRect(Players[i].pieces[j].piece.x,Players[i].pieces[j].piece.y,Players[i].width,Players[i].height);
		}
		
		/* Re-adjust new position of head. */
		switch (Players[i].pieces[0].piece.dir)
		{
			case Direction.RIGHT:
				Players[i].pieces[0].piece.x += (Players[i].width + Players[i].space);
				if(Players[i].pieces[0].piece.x > wWidth)
					Players[i].pieces[0].piece.x = 0;
				break;
				
			case Direction.LEFT:
				Players[i].pieces[0].piece.x -= (Players[i].width + Players[i].space);
				if(Players[i].pieces[0].piece.x < 0)
					Players[i].pieces[0].piece.x = wWidth - (Players[i].width + Players[i].space);
				break;
				
			case Direction.UP:
				Players[i].pieces[0].piece.y -= (Players[i].width + Players[i].space);
				if(Players[i].pieces[0].piece.y < 0)
					Players[i].pieces[0].piece.y = wHeight - (Players[i].height + Players[i].space);
				break;
				
			case Direction.DOWN:
				Players[i].pieces[0].piece.y += (Players[i].width + Players[i].space);
				if(Players[i].pieces[0].piece.y > wHeight)
					Players[i].pieces[0].piece.y = 0;
				break;
		}
		
		/* Draws the head. */
		ctx.fillStyle = Players[i].colorHead;
		ctx.fillRect(Players[i].pieces[0].piece.x,Players[i].pieces[0].piece.y,Players[i].width,Players[i].height);
	}
}

/* ----------------------------------------------Networking functions----------------------------------*/
function addSnake(snake)
{
	displayMessage("green", "Connected :D", 4000, "top");
	Snake = JSON.parse(snake).snake;
	Player.snake = Snake;

	var d = document.createElement('div');
	$(d).addClass("user").html("<b><font color=\"" + Snake.colorBody + "\">" + Snake.playerName + "</font></b>")
		.attr("data-user", Snake.playerName).appendTo("#rankingBox");
	
	start();
}

function newPlayer(snake)
{
	Players.push( JSON.parse(snake).snake );
	var p = Players[Players.length-1];
	
	var d = document.createElement('div');
	$(d).addClass("user").html("<b><font color=\"" + p.colorBody + "\">" + 
			p.playerName + " - 0" + "</font></b>")
		.attr("data-user", p.playerName).appendTo("#rankingBox");
	
	console.log("Adding new player " + Players[Players.length-1].playerName);
	
	sendMessage( "iAM|" + Players[Players.length-1].playerName + "|" + JSON.stringify(Player));
	console.log("Introducing myself");
}

function addPlayer(snake)
{
	Players.push( JSON.parse(snake).snake );
	var p = Players[Players.length-1];
	
	var d = document.createElement('div');
	$(d).addClass("user").html("<b><font color=\"" + p.colorBody + "\">" + 
			p.playerName + " - " + (p.pieces.length-1) + "</font></b>")
		.attr("data-user", p.playerName).appendTo("#rankingBox");
	
	console.log("Adding an new/old player " + Players[Players.length-1].playerName);
}

function removePlayer(name)
{
	var i;
	for (i = 0; i < Players.length; i++)
	{
		if (Players[i].playerName == name)
		{
			$(".user[data-user=" + Players[i].playerName + "]").remove();
			Players.splice(i, 1);
			break;
		}
	}
	console.log("Removing player " + name);
}

function updatePlayer(snake)
{
	var i;
	var tmpPlayer = JSON.parse(snake).snake;
	
	for(i = 0; i < Players.length; i++)
	{
		if (Players[i].playerName == tmpPlayer.playerName)
		{
			Players[i] = tmpPlayer;
			$(".user[data-user=" + tmpPlayer.playerName + "]").html("<b><font color=\"" + tmpPlayer.colorBody + "\">" + 
				tmpPlayer.playerName + " - " + (tmpPlayer.pieces.length-1) + "</font></b>");
			break;
		}
	}
	console.log("Updating player " + tmpPlayer.playerName);
}

function updateApple(apple)
{
	Apple = JSON.parse(apple);
	console.log("Updating my food..");
}

function announceWinner(winner)
{
	var i;
	$(".user[data-user=" + Snake.playerName + "]").remove();
	for (i = 0; i < Players.length; i++)
		$(".user[data-user=" + Players[i].playerName + "]").remove();
	
	if (winner == Snake.playerName)
	{
		var html = "ヽ (· ∀ ·) ノ Congratulations (⌒ ▽ ⌒) ☆<br>" +
				"<center><b>YOU</b> win</center>";
		
		displayMessage("green", html, 15000, "center");
	}
	else
	{
		var html = "(╥﹏╥) It wasn't your day, but you can try again (╥﹏╥)<br>" +
				"The winner was " + winner;
		displayMessage("red", html, 15000, "center");
	}
	
	pause();
	ctx.clearRect(0,0,wWidth,wHeight);
	Snake = null;
	Player.snake = Snake;
	Players = [];
}
