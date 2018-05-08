/*
   Environment.java                 2018 May 7th
   Adam Mehdi, Gary Sun, Leo Chen   2018 May 7th

   System containing all objects and
   runs through the interactions between them

   Known Bugs:
   	everything
 */

import java.util.ArrayList;

public class Environment {

    double secondsPerSecond; //How quickly the system passes through time
    ArrayList<RigidBody> rigidBodies = new ArrayList<RigidBody>(); //Contains physical rigid bodies


    public Environment(double secondsPerSecond){
        this.secondsPerSecond = secondsPerSecond;
    }

    public void run(){

    }

    public void update(){

    }

}
