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

    protected static int nObjects = 0;

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
        removeOffscreen(1080, 720);
    }
    public void removeOffscreen(int maxX, int maxY){
        for(int i=rigidBodies.size(); i < 0; i--){
            RigidBody r = rigidBodies.get(i);
            Point2D min = r.getMinCoords();
            Point2D max = r.getMaxCoords();
            if((min.getX() > maxX && min.getY() > maxY) || (max.getX() < 0 && max.getY() < 0)){
                rigidBodies.remove(r);
            }
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
        double[] y2 = {580, 500, 700, 700};
        double[] x3 = {700,600,500,450};
        double[] y3 = {120,240,200,120};
        double[] x4 = {0, 1000, 1000, 0};
        double[] y4 = {10, 10, 50, 50};
        double[] x5 = {0, 10, 10, 0};
        double[] y5 = {100, 100, 495, 495};
        double[] x6 = {980, 1000, 1000, 980};
        double[] y6 = {100, 100, 750, 750};
        double[] x7 = {400, 900, 900, 400};
        double[] y7 = {400, 400, 450, 450};
        double[] x8 = {400, 600, 600, 400};
        double[] y8 = {250, 250, 295, 295};
        double[] x9 = {520, 500, 480, 490, 500};
        double[] y9 = {400, 460, 450, 440, 420};

        RigidBody leo = new RigidBody(x, y, 1, false, environmentLayout);
        RigidBody leoo = new RigidBody(x2, y2, 1, true, environmentLayout);
        RigidBody leoooooo = new RigidBody(x3, y3, 4, false, environmentLayout);
        RigidBody leooo = new RigidBody(x4, y4, 1, true, environmentLayout);
        RigidBody leoooo = new RigidBody(x5, y5, 1, true, environmentLayout);
        RigidBody leooooo = new RigidBody(x6, y6, 1, true, environmentLayout);
        RigidBody leooooooooo = new RigidBody(x9, y9, 2, false, environmentLayout);
        rigidBodies.add(leo);
        rigidBodies.add(leoo);
        rigidBodies.add(leooo);
        rigidBodies.add(leoooo);
        rigidBodies.add(leooooo);
        rigidBodies.add(leoooooo);
        rigidBodies.add(leooooooooo);
        /*
        double[] x10 = {900, 1000, 1000, 900};
        double[] y10 = {500, 500, 550,550};
        RigidBody adamBogBoi = new RigidBody(x10, y10, 1.45, true, environmentLayout);
        rigidBodies.add(adamBogBoi);
        */
        /*

        RigidBody leooooooo = new RigidBody(x7, y7, 1, true, environmentLayout);
        RigidBody leoooooooo = new RigidBody(x8, y8, 1, false, environmentLayout);
        rigidBodies.add(leooooooo);
        rigidBodies.add(leoooooooo);
           */

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
