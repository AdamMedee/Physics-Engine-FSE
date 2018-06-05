/*
   InputHandler.java                 2018 May 7th
   Adam Mehdi, Gary Sun, Leo Chen   2018 May 7th

   Class to gather user input such as
   mouse pressed, scrolling, and key presses.

   Known Bugs:
   	everything
 */


import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.input.KeyEvent;


import java.awt.*;
import java.util.ArrayList;

public class InputHandler {
    static ArrayList<String> keysPressed; //All keys being presses down
    static Scene scene; //Game scene
    static Point mouse; //Where the mouse location is

    //Constructor
    public InputHandler(Scene scene){
        this.scene = scene;
    }



    public void prepareActionHandlers()
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

        mouse = MouseInfo.getPointerInfo().getLocation();
    }

    public Point getMousePos(){
        return mouse;
    }

    public ArrayList<String> getKeysPressed(){
        return keysPressed;
    }


}
