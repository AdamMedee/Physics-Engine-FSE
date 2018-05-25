/*
   Environment.java                 2018 May 7th
   Adam Mehdi, Gary Sun, Leo Chen   2018 May 7th

   System containing all objects and
   runs through the interactions between them

   Known Bugs:
   	everything
 */

import javafx.geometry.Point2D;
import javafx.scene.layout.Pane;


import java.util.ArrayList;

public class Environment {

    double simulationSpeed; //How quickly the system passes through time
    double scale;
    ArrayList<RigidBody> rigidBodies = new ArrayList<RigidBody>(); //Contains physical rigid bodies
    public Pane environmentLayout;
    Point2D gravity;


    public Environment(double simulationSpeed){
        this.simulationSpeed = simulationSpeed;
    }

    public Environment(){
        simulationSpeed = 1;
        scale = 100;
        gravity = new Point2D(0, 0);
    }

    //Goes through all rigid body interactions
    public void run(){
        //Applies all forces
        for(RigidBody rigidBody : rigidBodies){
            rigidBody.run(simulationSpeed, gravity, rigidBodies);
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
        double[] x2 = {0, 1000, 1000, 0};
        double[] y2 = {600, 650, 700, 700};
        //RigidBody leo = new RigidBody(x, y, 1, false, environmentLayout);
        RigidBody leoo = new RigidBody(x2, y2, 1, true, environmentLayout);
        //rigidBodies.add(leo);
        rigidBodies.add(leoo);

        double[] x3 = {400,500,600,700};
        double [] y3 = {20,200,240,20};
        RigidBody test = new RigidBody(x3, y3, 1, false, environmentLayout);
        rigidBodies.add(test);
    }

    //Adds a rigidbody to the rigidbody arraylist
    public void addRigidBody(RigidBody body){
        rigidBodies.add(body);
    }

    public ArrayList<RigidBody> getRigidBodies() {
        return rigidBodies;
    }

    //Resets all rigid bodies
    public void reset(){ for(RigidBody body : rigidBodies) body.reset(scale);}

    public void setGravity(Point2D grav){
        gravity = grav;
    }

    public void setSimulationSpeed(double simSpeed){
        simulationSpeed = simSpeed;
    }

    public void setScale(double newScale) { scale = newScale; }
}
