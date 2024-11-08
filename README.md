# CSCI 2020U - HANGMAN

## Identification

Group Members: Kevin Lau (100746819), Thinh Le (100741899), Kenneth Tse (100746775), John Tovera (100742638) 

## Project Information
This program is a server/client based game of hangman, it also support multiplayer, but all players can only play in one game, the program have not supported multiple game threads yet. Each player will take turns to guess letters or the word. If the players do not guess the word before the hanging man is drawn then they lose. If they are able to guess the word before they are out of chances then they win. \
![Alt text](https://github.com/KevinLau24/csci2020u_hangman/blob/main/src/main/resources/images/clientConnect.PNG)
![Alt text](https://github.com/KevinLau24/csci2020u_hangman/blob/main/src/main/resources/images/menu.PNG)
![Alt text](https://github.com/KevinLau24/csci2020u_hangman/blob/main/src/main/resources/images/game.PNG)


## How to run
In order to successfully run the program, here is a step-by-step process:
1. Install Java, requires Java JDK version 8 or higher, [instruction](https://www.oracle.com/java/technologies/javase-downloads.html).
2. Install Gradle, recommend v6.8.3, [instruction](https://gradle.org/install/)
3. Install IntelliJ IDEA IDE: [download](https://www.jetbrains.com/idea/download/) (You are welcome to use other IDEs, but this step-by-step process will be covered using IntelliJ).
4. Clone [this](https://github.com/KevinLau24/csci2020u_hangman) repository into your local machine: [instruction](https://docs.github.com/en/github/creating-cloning-and-archiving-repositories/cloning-a-repository).
5. Once you have done the previous steps, you want to run Server.java first to start the host server, by using IntelliJ toolbar for Gradle on the right side and choose **custom (other)** task **start**, or by typing this command into the console: **gradle start**
6. After Server.java is running, run ClientGUI.java next, by using IntelliJ toolbar for Gradle on the right side and choose **application** task **run**, or by typing this command into the console: **gradle run**
7. Once ClientGUI is running, enter the Server's IP address, Server's Port, and click on the "Connect" button to establish a client connection.
8. You are now given two options: "Play" and "Exit". Click on "Exit" to exit the program and click on "Play" to play hangman.
9. Once you click "Play", you are directed to a screen where you can start to guess letters. 
10. You have a maximum of 6 wrong guesses, and each time you guess a wrong letter, a body part of the human will appear on the hangman stand. 
11. The objective of the game is to guess the word before you guessed 6 wrong letters. Once you have figure out the word, enter the word and you win!

## Other resources
[Random English Word Generator API](https://random-word-api.herokuapp.com/home) \
[JavaFX Application Icon](https://stackoverflow.com/questions/10121991/javafx-application-icon) \
[JavaFX TextArea](http://tutorials.jenkov.com/javafx/textarea.html#:~:text=A%20JavaFX%20TextArea%20control%20enables,scene) \
[Auto Updating JavaFX UI](https://riptutorial.com/javafx/example/7291/updating-the-ui-using-platform-runlater)
