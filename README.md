# Who wants to be a Millionaire game in Java
## Short description of the project
This game was done using **JavaFX** and a connection to an **Oracle** Database. It contains a LogIn and a CreateAccount page, and there are two types of users: players and admins (the role is given through the database). If the user logs as an admin, they can add a question to the database, but as a player, the game will be initialized. <br>
The game features the three lifelines: FiftyFifty, AskAudience and PhoneFriend. <br>
Unlike the television game, this one only stops if the player fiinishes all the questions that are in the database. 
## Before runnig the project
I used VS Code for this program, so I had to download the necessary libraries:
* For the connection with the Database, you need a [Connector](https://www.oracle.com/database/technologies/appdev/jdbc-downloads.html). 
* For the GUI libraries, I used [JavaFX](https://gluonhq.com/products/javafx/).
* For the Test classes, I used JUnit, and it can be downloaded from within VS Code. It is not mandatory for the program to run. <br>

You may want to update the launch.json and the settings.json in order for the program to see the respective libraries.
## Setting the Database
In order to have a Database, you have to download [Oracle Database](https://www.oracle.com/ro/database/technologies/xe-downloads.html) and a [Software](https://www.oracle.com/database/sqldeveloper/technologies/download/) for easier use. <br>
After the download, you'll have to open the SQL Plus command prompt, and input the username (**SYS as SYSDBA**) and any password you wish (in my code, **BlaBlaBla**). <br> 
After opening the software and making a connection, you'll have to make two tables. <br>
1. ACCOUNT
   - USERNAME, PASSWORD, BOSS
2. QUESTIONS
   - QUESTIONID, QUESTIONTEXT, CORRECTANSWER, ANSWERA, ANSWERB, ANSWERC, ANSWERD, AVAILABLE <br>

***Don't forget to update any part of the code if necessary!*** <br>
## Running on Linux
```
sudo apt-get install openjfx
javac --module-path /usr/share/openjfx/lib --add-modules javafx.controls -cp .:lib/ojdbc11.jar Main.java
java --module-path /usr/share/openjfx/lib --add-modules javafx.controls -cp .:lib/ojdbc11.jar Main
```
***In order to make it work, don't forget to install SQL Plus!*** <br>


