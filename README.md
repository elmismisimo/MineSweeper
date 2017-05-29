# MineSweeper
Little minesweeper game based in the windows classic

This project borns from the sudden need of creating a little game in less than 24 hrs. Wish me luck.

Using Android Studio 2.3

21/May/2017
12:30pm -> Project creation

(Im using a potato as computer, it works but Android Studio is a bit heavy for it and sometimes I have to wait a little so it responds)
2:30pm -> First files uploaded

6:10pm -> Working of cells and cell functions

10:44pm -> Board full functionality, Multi theme is prepared, and top bar updates with mines left and time of playing

12:47am (next day) -> Game screen fully functional Yeah!

2:12pm (next day) -> Finished the menu and the splash screen. Challenge almost completed!!!

Althought I didnt finished the game in 24 hours, I really didnt invest the hole 24 hours to the game. Had a lot of thing to do, like attend my family, sleep, eat and work. but its nice to see how much can be accomplished in little time when the planning is good (had this project plan beforehand, at least in my head ;) )

Buuut, the game is not finished, and im going to complete it:
-Sound Manager is going to be added
-Google Play Games lib is going to be added
-Ads are needed (but im going to place them like I always do, only when required)
-and little thing that make a game publishable (at least on my SanderSoft Standars), and Im going to publish it in the playstore!
 
Please feel free to grab any code from here, this was just an experiment and it turned out perfect!
If you got questions please send an email to the developer in the playstore website https://play.google.com/store/apps/dev?id=5844133407192700083

Cheers!

7:18pm (next day) -> Sound Manger added. everything have sound now

29/May/2017
Game is finished and published!
It was ready before but I had some trouble implementing the Google Games Api.
Two problems: 

-My debug ceritificate Sha1 did not worked with leaderboards but it did with achievements (even with that certificate added on the api panel), there was always a problem, but the game worked good with the released Sha1. So Im good with that (I spend a lot of time trying to solve that, but in the end what matters to me is the relesaed version).

-Couldnt make the Google Games api Popups work with multiple activities. The achievements where unlocked but the popup never showed. I had to change the game logic to work in only one activity in the end, and it was easy because i used fragments ;)

One other consideration I found was that I had to reduce the number of columns to 15 max. You know that use a Scrolview inde a Scroll view is not a good practice (and Mine sweeper has a scroll view with a recyclerview inside), so in order to avoid the recyclerview to do a lot of rendering elements not visible because of the number of columns, i reduced the max columns size to 15. That number overflows the Scrollview so you have to slide to get to the end, but is not that much.

If you encounter problems like this, feel free to check my code for solutions, or contact me if there is something I can help you with.

Well! This game is finished! And Im so happy with it! Enjoy!

You can download it directly from the playstore at https://play.google.com/store/apps/details?id=com.sandersoft.games.minesweeper or you can download the apk placed in the root folder od this repository

Cheers!
