/*
   Environment.java                 2018 May 7th
   Adam Mehdi, Gary Sun, Leo Chen   2018 May 7th

   System containing all objects and
   runs through the interactions between them

   Known Bugs:
   	everything
 */

import javafx.geometry.Point2D;
import javafx.scene.control.ColorPicker;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;


import java.util.ArrayList;

public class Environment {

    double simulationSpeed;                                                     //How quickly the system passes through time
    double scale;                                                                    //How big the simulation appears
    ArrayList<RigidBody> rigidBodies = new ArrayList<RigidBody>();    //Contains physical rigid bodies
    public Pane environmentLayout;
    Point2D gravity;
    boolean allowRotate;                                                        //Keeps track if rotation is on or off

    protected static int nObjects = 0;

    public Environment(double simulationSpeed){
        this.simulationSpeed = simulationSpeed;
    }

    public Environment(){
        //Default values
        simulationSpeed = 1;
        scale = 100;
        gravity = new Point2D(0, 0);
        allowRotate = false;
    }

    //Goes through all rigid body interactions
    public void run(){
        //Applies all forces
        for(RigidBody rigidBody : rigidBodies){
            rigidBody.run(simulationSpeed, gravity, rigidBodies, allowRotate);  //Run each rigidbody every frame
        }
    }

    public void update(){

    }


    public Pane getGroup(){
        return environmentLayout;
    }

    public void startDefaultSim(Pane src)
    {
        for(RigidBody body : rigidBodies){
            body.removeShape();
        }
        rigidBodies.clear();
        environmentLayout = src;

        double[] x = {30, 40, 50, 30};
        double[] y = {100, 120, 130, 140};

        double[] x2 = {0, 1000, 1000, 0};
        double[] y2 = {600, 600, 700, 700};

        double[] x3 = {700,600,500,450};
        double[] y3 = {120,240,200,120};

        double[] x4 = {0, 1000, 1000, 0};
        double[] y4 = {10, 10, 50, 50};

        double[] x5 = {-30, 20, 20, -30};
        double[] y5 = {0, 0, 700, 700};

        double[] x6 = {960, 1000, 1000, 960};
        double[] y6 = {0, 0, 700, 700};

        double[] x9 = {520, 500, 480, 490, 500};
        double[] y9 = {400, 460, 450, 440, 420};


        RigidBody triangle = new RigidBody(x, y, 1, false, environmentLayout, Color.BLACK);
        RigidBody quadrilateral = new RigidBody(x3, y3, 4, false, environmentLayout,Color.BLACK);
        RigidBody pentagon = new RigidBody(x9, y9, 2, false, environmentLayout,Color.BLACK);
        RigidBody topWall = new RigidBody(x4, y4, 1, true, environmentLayout,Color.BLACK);
        RigidBody bottomWall = new RigidBody(x2, y2, 1, true, environmentLayout,Color.BLACK);
        RigidBody leftWall = new RigidBody(x5, y5, 1, true, environmentLayout,Color.BLACK);
        RigidBody rightWall = new RigidBody(x6, y6, 1, true, environmentLayout,Color.BLACK);


        triangle.setSerialNum(rigidBodies.size());
        rigidBodies.add(triangle);
        quadrilateral.setSerialNum(rigidBodies.size());
        rigidBodies.add(quadrilateral);
        pentagon.setSerialNum(rigidBodies.size());
        rigidBodies.add(pentagon);
        topWall.setSerialNum(rigidBodies.size());
        rigidBodies.add(topWall);
        bottomWall.setSerialNum(rigidBodies.size());
        rigidBodies.add(bottomWall);
        leftWall.setSerialNum(rigidBodies.size());
        rigidBodies.add(leftWall);
        rightWall.setSerialNum(rigidBodies.size());
        rigidBodies.add(rightWall);
    }

    //Adds a rigidbody to the rigidbody arraylist
    public void addRigidBody(RigidBody body){
        rigidBodies.add(body);
    }

    public ArrayList<RigidBody> getRigidBodies() {
        return rigidBodies;
    }

    //Resets all rigid bodies
    public void reset(boolean resetPos){ for(RigidBody body : rigidBodies) body.reset(scale, resetPos);}

    public void setGravity(Point2D grav){
        gravity = grav;
    }

    public void setSimulationSpeed(double simSpeed){
        simulationSpeed = simSpeed;
    }

    public void setScale(double newScale) { scale = newScale; }

    public boolean isRotate() {
        return allowRotate;
    }

    public void setRotate(boolean allowRotate) {
        this.allowRotate = allowRotate;
    }

    public void BackGroundMenu (Pane src) {
        //Generate environment for the main menu background
        environmentLayout = src;
        gravity = new Point2D(0, 1);
        rigidBodies.clear();
        for (int x = 200; x < 1000; x += 100) {
            for (int y = 150; y < 500; y += 100) {
                if (Math.random() > 0.6) {
                    double x1[] = {x + 20 * (0.5 - Math.random()), x + 45 + 20 * (0.5 - Math.random()), x + 45 + 20 * (0.5 - Math.random()), x + 20 * (0.5 - Math.random())};
                    double y1[] = {y + 20 * (0.5 - Math.random()), y + 20 * (0.5 - Math.random()), y + 45 + 20 * (0.5 - Math.random()), y + 45 + 20 * (0.5 - Math.random())};

                    RigidBody rect = new RigidBody(x1, y1, 1, false, environmentLayout,Color.BLACK);
                    rigidBodies.add(rect);
                }
            }
        }

        double[] x2 = {0, 1280, 1280, 0};       double[] y2 = {670, 670, 720, 720};
        double[] x3 = {0, 50, 50, 0};           double[] y3 = {0, 0, 720, 720};
        double[] x4 = {1230, 1280, 1280, 1230}; double[] y4 = {0, 0, 720, 720};
        double[] x5 = {0, 1280, 1280, 0};       double[] y5 = {0, 0, 50, 50};
        double[] x6 = {541, 739, 739, 541};     double[] y6 = {301, 301, 399, 399};
        double[] x7 = {561, 719, 719, 561};     double[] y7 = {441, 441, 519, 519};

        RigidBody bottomwall = new RigidBody(x2, y2, 1, true, environmentLayout,Color.BLACK);
        RigidBody leftwall = new RigidBody(x3, y3, 1, true, environmentLayout,Color.BLACK);
        RigidBody rightwall = new RigidBody(x4, y4, 1, true, environmentLayout,Color.BLACK);
        RigidBody topwall = new RigidBody(x5, y5, 1, true, environmentLayout,Color.BLACK);
        RigidBody buttonStart = new RigidBody(x6, y6, 1, true, environmentLayout, Color.BLACK);
        RigidBody buttonCredits = new RigidBody(x7, y7, 1, true, environmentLayout, Color.BLACK);

        rigidBodies.add(bottomwall);
        rigidBodies.add(leftwall);
        rigidBodies.add(rightwall);
        rigidBodies.add(topwall);
        rigidBodies.add(buttonStart);
        rigidBodies.add(buttonCredits);
    }

    //Creates the environment background for the credits
    public void creditsBG(Pane src){
        //Generate background environment for credits menu
        environmentLayout = src;
        gravity = new Point2D(0, 1);
        rigidBodies.clear();
        for (int x = 200; x < 1000; x += 100) {
            for (int y = 150; y < 500; y += 100) {
                if (Math.random() > 0.5) {
                    double x1[] = {x + 20 * (0.5 - Math.random()), x + 45 + 20 * (0.5 - Math.random()), x + 45 + 20 * (0.5 - Math.random()), x + 20 * (0.5 - Math.random())};
                    double y1[] = {y + 20 * (0.5 - Math.random()), y + 20 * (0.5 - Math.random()), y + 45 + 20 * (0.5 - Math.random()), y + 45 + 20 * (0.5 - Math.random())};

                    RigidBody rect = new RigidBody(x1, y1, 1, false, environmentLayout,Color.BLACK);
                    rect.setRestitution(0.99);
                    rigidBodies.add(rect);
                }
            }
        }

        double[] x2 = {0, 1280, 1280, 0};
        double[] y2 = {670, 670, 720, 720};
        double[] x3 = {0, 50, 50, 0};
        double[] y3 = {0, 0, 720, 720};
        double[] x4 = {1230, 1280, 1280, 1230};
        double[] y4 = {0, 0, 720, 720};
        double[] x5 = {0, 1280, 1280, 0};
        double[] y5 = {0, 0, 50, 50};
        double[] x6 = {340, 940, 940, 340};
        double[] y6 = {210, 210, 510, 510};

        RigidBody bottomwall = new RigidBody(x2, y2, 1, true, environmentLayout,Color.BLACK);
        RigidBody leftwall = new RigidBody(x3, y3, 1, true, environmentLayout,Color.BLACK);
        RigidBody rightwall = new RigidBody(x4, y4, 1, true, environmentLayout,Color.BLACK);
        RigidBody topwall = new RigidBody(x5, y5, 1, true, environmentLayout,Color.BLACK);
        RigidBody middle = new RigidBody(x6, y6, 1, true, environmentLayout, Color.BLACK);

        rigidBodies.add(bottomwall);
        rigidBodies.add(leftwall);
        rigidBodies.add(rightwall);
        rigidBodies.add(topwall);
        rigidBodies.add(middle);
    }
}
