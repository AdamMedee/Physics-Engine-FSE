/*
   Environment.java                 2018 May 7th
   Adam Mehdi, Gary Sun, Leo Chen   2018 May 7th

   System containing all objects and
   runs through the interactions between them

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

public class Environment {

    double secondsPerSecond; //How quickly the system passes through time
    ArrayList<RigidBody> rigidBodies = new ArrayList<RigidBody>(); //Contains physical rigid bodies


    public Environment(double secondsPerSecond){
        this.secondsPerSecond = secondsPerSecond;
    }

    public Environment(Group root){
        double[] x = {100, 200, 300, 600, 300, 200};
        double[] y = {300, 100, 300, 100, 320, 120};
        RigidBody leo = new RigidBody(x,y, 10, root);
        rigidBodies.add(leo);
    }

    //Goes through all rigid body interactions
    public void run(){
        for(RigidBody rigidBody : rigidBodies){
            rigidBody.translate(2, 3);
        }
    }

    public void update(GraphicsContext graphics){

    }

}
