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
import java.util.Arrays;
import javafx.geometry.Point2D;

public class RigidBody{
	private int sides;
	private Point2D center;
	private Polygon polygon;
	private double[] xPoints, yPoints;
	private ArrayList<Point2D> forces;

	private double velocity;
	private double acceleration;
	private double spin;
	private double angAccel;
	private double MOI;

	private double area;
	private double mass; 	//Mass and density is consistent throughout the body
	private double density;

	private Circle circle;


	public RigidBody(double[] xPoints, double[] yPoints, double mass, Group root){
		this.sides = xPoints.length;

		this.area = 0;			//Init area, Moment of inertia and Center
		this.MOI = 0;
		double centerX = 0;
		double centerY = 0;
		for(int i=0; i < sides; i++){
			//Thanks Green!
			double shoelace = xPoints[i] * yPoints[(i+1) % sides] - xPoints[(i+1) % sides] * yPoints[i];
			this.area += shoelace;
			centerX += (xPoints[i] + xPoints[(i+1) % sides]) * shoelace;
			centerY += (yPoints[i] + yPoints[(i+1) % sides]) * shoelace;
			this.MOI += ((xPoints[i] * yPoints[(i+1) % sides] + 2*xPoints[i]*yPoints[i] + 2*xPoints[(i+1) % sides]*yPoints[(i+1) % sides] + xPoints[(i+1) % sides]*yPoints[i]) * shoelace);
		}
		this.area = Math.abs(this.area / 2);
		this.MOI /= 24;
		centerX = centerX / (6 * this.area);
		centerY = centerY / (6 * this.area);

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
		this.forces = new ArrayList<>();
		this.velocity = 0;
		this.acceleration = 0;
		this.spin = 0;
		this.angAccel = 0;
		this.mass = mass;
		this.density = mass/this.area;
	}

	//Allows the rigidbody to be printed
	public String toString(){
		return Arrays.toString(xPoints) + " " + Arrays.toString(yPoints) + " " + this.center;
	}

	//Updates the state of the rigidbody polygon
	public void update(double[] XP, double[] YP, Point2D newCenter){
		polygon.getPoints().clear();
		for(int i = 0; i < sides; i++){
			polygon.getPoints().add(XP[i] );
			polygon.getPoints().add(YP[i]);
		}
		center = newCenter;
		xPoints = XP;
		yPoints = YP;
	}

	//Moves the coordinates of the polygon over by dx and dy
	public void translate(double dx, double dy){
		double[] newXP = new double[sides];
		double[] newYP = new double[sides];
		for (int i = 0; i < sides; i++){
			newXP[i] = xPoints[i] + dx;
			newYP[i] = yPoints[i] + dy;
		}
		this.update(newXP, newYP, new Point2D(center.getX() + dx, center.getY() + dy));
	}

	//Rotates all the points about the center of mass
	public void rotate(double ang){
		double[] newXP = new double[sides];
		double[] newYP = new double[sides];
		for(int i = 0; i < sides; i++){
			double OX = xPoints[i] - center.getX();
			double OY = yPoints[i] - center.getY();
			newXP[i] = OX * Math.cos(ang) + OY * Math.sin(ang);
			newYP[i] = OY * Math.cos(ang) - OX * Math.sin(ang);
		}
		this.update(newXP, newYP, this.center);
	}
/*
	public static void main(String[] args){
		double[] x = {1,0,-1,0};
		double[] y = {0,1,0,-1};
		RigidBody r = new RigidBody(x, y, 1, new Group());
		r.translate(1,0);
		System.out.println(r);
		r.translate(-1,0);
		System.out.println(r);

		r.rotate(0.785398);
		System.out.println(r);
	}
	*/
}