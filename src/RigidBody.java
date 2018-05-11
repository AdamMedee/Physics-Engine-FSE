/*
   RigidBody.java                 2018 May 7th
   Adam Mehdi, Gary Sun, Leo Chen   2018 May 7th

   Basic unit in the physics engine,
   polygons which can collide with other
   polygons including rotational motion.

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
import java.awt.Polygon;
import javafx.geometry.Point2D;

public class RigidBody{
	private Point2D center;
	private Polygon points;
	//private ArrayList<Force> forces;

	private double velocity;
	private double acceleration;
	private double spin;
	private double angAccel;
	private double MOI;

	private double area;
	private double mass;


	public RigidBody(int[] xPoints, int[] yPoints){
		int sides = xPoints.length;

		area = 0;
		MOI = 0;
		double centerX = 0;
		double centerY = 0;
		for(int i=0; i < sides; i++){
			double shoelace = xPoints[i] * yPoints[(i+1) % sides] - xPoints[(i+1) % sides] * yPoints[i];
			area += shoelace;
			centerX += (xPoints[i] + xPoints[(i+1) % sides]) * shoelace;
			centerY += (yPoints[i] + yPoints[(i+1) % sides]) * shoelace;
			MOI += ((xPoints[i] * yPoints[(i+1) % sides] + 2*xPoints[i]*yPoints[i] + 2*xPoints[(i+1) % sides]*yPoints[(i+1) % sides] + xPoints[(i+1) % sides]*yPoints[i]) * shoelace);

		}
		area = Math.abs(area / 2);
		MOI /= 24;
		centerX = centerX / (6 * area);
		centerY = centerY / (6 * area);




		this.points = new Polygon(xPoints, yPoints, sides);
		this.center = new Point2D(centerX, centerY);
		//this.forces = {};
		this.velocity = 0;
		this.acceleration = 0;
		this.spin = 0;
		this.angAccel = 0;
		this.area = area;
		this.MOI = MOI;
		this.mass = 0; //change


	}

	public String toString(){
		return this.area + " " + this.center;
	}

	public static void main(String[] args){
		int[] x = {0,4,3,1,0};
		int[] y = {0,0,1,3,3};
		RigidBody leo = new RigidBody(x,y);
		System.out.println(leo);
	}

}