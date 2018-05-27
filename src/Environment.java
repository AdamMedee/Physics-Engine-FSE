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
        double[] x = {30, 40, 50, 30};
        double[] y = {100, 120, 130, 140};
        double[] x2 = {0, 1000, 1000, 0};
        double[] y2 = {500, 700, 700, 700};
        double[] x4 = {0, 1000, 1000, 0};
        double[] y4 = {10, 10, 50, 50};
        double[] x5 = {0, 10, 10, 0};
        double[] y5 = {100, 100, 495, 495};
        double[] x6 = {980, 1000, 1000, 980};
        double[] y6 = {100, 100, 600, 600};
        RigidBody leo = new RigidBody(x, y, 1, false, environmentLayout);
        RigidBody leoo = new RigidBody(x2, y2, 5, true, environmentLayout);
        RigidBody leooo = new RigidBody(x4, y4, 1, true, environmentLayout);
        RigidBody leoooo = new RigidBody(x5, y5, 1, true, environmentLayout);
        RigidBody leooooo = new RigidBody(x6, y6, 1, true, environmentLayout);
        rigidBodies.add(leo);
        rigidBodies.add(leoo);
        rigidBodies.add(leooo);
        rigidBodies.add(leoooo);
        rigidBodies.add(leooooo);

        double[] x3 = {450,500,600,700};
        double [] y3 = {120,200,240,120};
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
