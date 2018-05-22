/*
   Environment.java                 2018 May 7th
   Adam Mehdi, Gary Sun, Leo Chen   2018 May 7th

   System containing all objects and
   runs through the interactions between them

   Known Bugs:
   	everything
 */

import javafx.scene.Group;
import javafx.scene.layout.Pane;

import java.util.ArrayList;

public class Environment {

    double secondsPerSecond; //How quickly the system passes through time
    ArrayList<RigidBody> rigidBodies = new ArrayList<RigidBody>(); //Contains physical rigid bodies
    public Pane environmentLayout;


    public Environment(double secondsPerSecond){
        this.secondsPerSecond = secondsPerSecond;
    }

    public Environment(){

    }

    //Goes through all rigid body interactions
    public void run(){
        for(RigidBody rigidBody : rigidBodies){
            //rigidBody.translate(0.3, 0.4);
            rigidBody.rotate(0.05);
            //s -= 0.001;

        }
    }

    public void update(){

    }

    public Pane getGroup(){
        return environmentLayout;
    }

    public void setGroup (Pane src)
    {
        environmentLayout = src;
        double[] x = {100, 200, 300, 600, 300, 200};
        double[] y = {300, 100, 300, 100, 320, 120};
        RigidBody leo = new RigidBody(x,y, 10, environmentLayout);
        rigidBodies.add(leo);
    }

    //Adds a rigidbody to the rigidbody arraylist
    public void addRigidBody(RigidBody body){
        rigidBodies.add(body);
    }

}
