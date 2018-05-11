/*
   GraphicButton.java                 2018 May 7th
   Adam Mehdi, Gary Sun, Leo Chen   2018 May 7th

   Buttons to catch user interaction
   and contain custom graphic images.

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

public class GraphicButton{
    int x, y, width, height;
    Image img, imgP;
    boolean mouseOver;
    public GraphicButton(int x, int y, int width, int height, Image img, Image imgP){
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.img = img;
        this.imgP = imgP;
    }

    //Updates button state depending on mouse
    public void run(){

    }

    public void update(GraphicsContext graphic){
        graphic.drawImage(img, x, y);
    }


}
