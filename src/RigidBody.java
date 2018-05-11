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
import javafx.scene.shape.Circle;
import javafx.stage.Stage;
import java.awt.*;
import javafx.scene.shape.Polygon;
import java.util.ArrayList;

import javafx.geometry.Point2D;

public class RigidBody{
	private int sides;
	private Point2D center;
	private Polygon points;
	private double[] xPoints, yPoints;
	private ArrayList<Force> forces;

	private double velocity;
	private double acceleration;
	private double spin;
	private double angAccel;
	private double MOI;

	private double area;
	private double mass;


	public RigidBody(double[] xPoints, double[] yPoints, Group root){
		this.sides = xPoints.length;

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



		this.xPoints = xPoints;
		this.yPoints = yPoints;

		this.points = new Polygon();

		Double[] tmpPoints = new Double[sides*2];
		for(int i = 0; i < sides; i++){
			tmpPoints[i*2] = xPoints[i];
			tmpPoints[i*2+1] = yPoints[i];
		}
		this.points.getPoints().addAll(tmpPoints);
		root.getChildren().add(points);

		Circle circle = new Circle(centerX, centerY, 3);
		circle.setFill(javafx.scene.paint.Color.RED);
		root.getChildren().add(circle);

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

	public void update(){
		Double[] tmpPoints = new Double[sides*2];
		for(int i = 0; i < sides; i++){
			tmpPoints[i*2] = xPoints[i];
			tmpPoints[i*2+1] = yPoints[i];
		}
		points = new Polygon();
		points.getPoints().addAll(tmpPoints);
	}
/*
	public static void main(String[] args){
		double[] x = {0,4,3,1,0};
		double[] y = {0,0,1,3,3};
		RigidBody leo = new RigidBody(x,y);
		System.out.println(leo);
	}
*/

}