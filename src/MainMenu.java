/*
   MainMenu.java                 2018 May 7th
   Adam Mehdi, Gary Sun, Leo Chen   2018 May 7th

   Class for the main menu
   which is opened when the program
   first runs.

   Known Bugs:
   	everything
 */

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.input.KeyEvent;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import java.util.ArrayList;

public class MainMenu {

    Image background = new Image("resources/images/LEOO.png");

    //Constructor for the menu
    public MainMenu(){

    }


    //Goes through the actions inputted and acts accordingly
    public void run(){

    }


    //Displayes menu to the screen
    public void update(GraphicsContext graphics){

        Font ourFont = Font.loadFont(getClass().getResourceAsStream("resources/fonts/modern.ttf"),20);

        graphics.setFont(ourFont);
        graphics.strokeText("L.A.G Physics Engine",640,360);
        graphics.drawImage(background, 0, 0);

    }
}
