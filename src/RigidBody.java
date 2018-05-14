/*
   RigidBody.java                 2018 May 7th
   Adam Mehdi, Gary Sun, Leo Chen   2018 May 7th

   Basic unit in the physics engine,
   polygons which can collide with other
   polygons including rotational motion.

   Known Bugs:
   	everything
 */

import javafx.scene.Group;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Polygon;
import java.util.ArrayList;
import javafx.geometry.Point2D;

public class RigidBody{
	//test
	private int sides;
	private Point2D center;
	private Polygon polygon;
	private double[] xPoints, yPoints;
	private ArrayList<Force> forces;

	private double velocity;
	private double acceleration;
	private double spin;
	private double angAccel;
	private double MOI;

	private double area;
	private double mass; //Mass and density is consistent throughout the body
	private double density;

	private Circle circle;


	public RigidBody(double[] xPoints, double[] yPoints, double mass, Group root){
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

		this.polygon = new Polygon();

		Double[] tmpPoints = new Double[sides*2];
		for(int i = 0; i < sides; i++){
			tmpPoints[i*2] = xPoints[i];
			tmpPoints[i*2+1] = yPoints[i];
		}
		this.polygon.getPoints().addAll(tmpPoints);
		root.getChildren().add(polygon);

		//Creates a center of mass as a circle for the polygon for testing purposes
		this.circle = new Circle(centerX, centerY, 3);
		circle.setFill(javafx.scene.paint.Color.RED);
		root.getChildren().add(circle);

		this.center = new Point2D(centerX, centerY);
		this.forces = new ArrayList<Force>();
		this.velocity = 0;
		this.acceleration = 0;
		this.spin = 0;
		this.angAccel = 0;
		this.area = area;
		this.MOI = MOI;
		this.mass = mass;
		this.density = mass/area;
	}

	//Allows the rigidbody to be printed
	public String toString(){
		return this.area + " " + this.center;
	}

	//Updates the state of the rigidbody polygon
	public void update(Point2D newCenter){
		//translate(1,0);
		polygon.getPoints().clear();
		for(int i = 0; i < sides; i++){
			polygon.getPoints().add(xPoints[i] );
			polygon.getPoints().add(yPoints[i]);
		}
		center = newCenter;
	}

	//Moves the coordinates of the polygon over by dx and dy
	public void translate(double dx, double dy){
		double[] newXP = new double[sides];
		double[] newYP = new double[sides];
		for (int i = 0; i < sides; i++){
			newXP[i] = xPoints[i] + dx;
			newYP[i] = yPoints[i] + dy;
		}
		this.update(new Point2D(center.getX() + dx, center.getY() + dy));
	}
}