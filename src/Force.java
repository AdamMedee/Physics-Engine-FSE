/*
   Force.java                       2018 May 7th
   Adam Mehdi, Gary Sun, Leo Chen   2018 May 7th

   Forces contained in the system
   that is constantly exerted on the
   objects in the environment, such
   as uniform gravity.

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

public class Force {
    double xForce, yForce;

    public Force(double xForce, double yForce){
        this.xForce = xForce;
        this.yForce = yForce;
    }

}