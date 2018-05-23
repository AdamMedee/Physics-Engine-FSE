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
    ArrayList<RigidBody> rigidBodies = new ArrayList<RigidBody>(); //Contains physical rigid bodies
    public Pane environmentLayout;
    Point2D gravity;


    public Environment(double secondsPerSecond){
        this.simulationSpeed = secondsPerSecond;
    }

    public Environment(){

    }

    //Goes through all rigid body interactions
    public void run(){
        //Applies all forces
        for(RigidBody rigidBody : rigidBodies){
            rigidBody.addForce(gravity);
            rigidBody.updateVelocity(simulationSpeed);
            //rigidBody.updatePosition(simulationSpeed);
            rigidBody.clearForces();
            //rigidBody.rotate(0.05);
            //rigidBody.translate(0.5, 0.8);
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
        RigidBody leo = new RigidBody(x,y, 1, environmentLayout);
        rigidBodies.add(leo);
    }

    //Adds a rigidbody to the rigidbody arraylist
    public void addRigidBody(RigidBody body){
        rigidBodies.add(body);
    }

    public ArrayList<RigidBody> getRigidBodies() {
        return rigidBodies;
    }

    public void setGravity(Point2D grav){
        gravity = grav;
    }

    public void setSimulationSpeed(double simSpeed){
        simulationSpeed = simSpeed;
    }
}
