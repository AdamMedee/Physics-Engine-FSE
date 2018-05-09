/*
   InputHandler.java                 2018 May 7th
   Adam Mehdi, Gary Sun, Leo Chen   2018 May 7th

   Class to gather user input such as
   mouse pressed, scrolling, and key presses.

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

public class InputHandler {
    ArrayList<String> keysPressed; //All keys being presses down
    Scene scene; //Game scene


    //Constructor
    public InputHandler(Scene scene){
        this.scene = scene;
    }


    //
    private static void prepareActionHandlers(Scene scene)
    {
        // use a set so duplicates are not possible
        ArrayList<String> keysPressed = new ArrayList<String>();
        scene.setOnKeyPressed(new EventHandler<KeyEvent>()
        {
            @Override
            public void handle(KeyEvent event)
            {
                keysPressed.add(event.getCode().toString());
            }
        });
        scene.setOnKeyReleased(new EventHandler<KeyEvent>()
        {
            @Override
            public void handle(KeyEvent event)
            {
                keysPressed.remove(event.getCode().toString());
            }
        });
    }


}
