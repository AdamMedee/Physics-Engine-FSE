/*
   SelectMenu.java                  2018 May 7th
   Adam Mehdi, Gary Sun, Leo Chen   2018 May 7th

   Previous saved files are selected here
   along with the option to create a new
   file. Works with a page flipping system.

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

public class SelectMenu {

    private String newScene;

    public SelectMenu(){
        newScene = "SelectMenu";

    }

    public String run(){
        return newScene;
    }

    public void update()
    {


    }
}
