/*
   SystemMenu.java                 2018 May 7th
   Adam Mehdi, Gary Sun, Leo Chen   2018 May 7th

   This si where the user will be
   playing around with the physics
   engine, will run a single environment
   and allow user input.

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
import javafx.stage.Stage;
import java.util.ArrayList;

public class SystemMenu {

    Environment environment; //The environment being used

    //Constructor for the menu
    public SystemMenu(Environment environment){
        this.environment = environment;
    }


    //Goes through the actions inputted and acts accordingly
    public void run(){
        environment.run();
    }


    //Displayes menu to the screen
    public void update(GraphicsContext graphics){
        environment.update(graphics);
    }
}
