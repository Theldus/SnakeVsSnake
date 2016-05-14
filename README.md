## Snake versus Snake
Snake vs Snake: An simple multiplayer Snake game in HTML5 and Java.

###Game
As I said, Snake vs Snake is the classic snake game with multiplayer addition.
The ultimate goal of the game is to eat 30 apples, the player to reach this mark first, wins. That's all.

#####Why?
I know that's not the first implementation of the game in multiplayer and there are very good versions and well known out there
as [SlitherIO](http://slither.io/) (my game even comes close to it), but unfortunately I only knew when I was already on the way (I was not searched before).

But it was not a lost of time, I really had to look for some concepts such as Canvas, WebSockets (which I had no idea before 
that), among others, so I can say I learned a lot with this project.

###Development
The game was built entirely on HTML5 + Javascript (Client-side) and 'JavaEE' (Server-side).<br />

Why JavaEE? True, I could have done it in many other ways as Node.JS or even a good old C, but, if you notice, there are actually
some good free hosts for JavaEE as [OpenShift](https://www.openshift.com/) (I'm currently using) and 
[Heroku](https://www.heroku.com/).

So without much difficulty you can download this source, modify (if you like), and put it online for free and without much 
pain. ;-).

#####Known bugs
Yes, unfortunately there are bugs, I am not a master in HTML + CSS then you will definitely encounter problems with the layout
(which is not responsive) and in other places as well.

Some I can mention are:
- Strange layout in resolutions less than or equal to 1024x768.

- Behavior very strange with compound names, so please do not use something like "Foo Bar" but only "FooBar" as your player
name.

- Unpredictable death system, sometimes: In order to saving bandwith, I opted to update the status of the player only when
there is a change, and not all the time, because of this, you can predict the path that each player does, until there is a
change of direction. This prediction can cause a loss of synchronization between the players, and you can end up killing your
enemies even unintentionally.

Even so, I believe the game is playable, so I don't plan to fix these bugs as early, if you believe it is impossible to play,
or the game didn't even work, let me know.
