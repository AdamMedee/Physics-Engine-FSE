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
	private int sides;								//# of sides
	private Point2D center;						//Coords of center (x,y)
	private Polygon polygon;						//Polygon defining the shape
	private double[] xPoints, yPoints;		//Set of x coords and y coords
	private ArrayList<Point2D> forces;		//The forces acting on the body

	private Point2D velocity;
	private Point2D acceleration;
	private double spin;							//Angular velocity
	private double angAccel;					//Angular acceleration
	private double MOI;							//Moment of Inertia
	private double restitution;					//Coefficient of restitution

	private double area;
	private double mass; 							//Mass and density is consistent throughout the body
	private double density;

	private Circle circle;


	public RigidBody(double[] xPoints, double[] yPoints, double mass,Group root){
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
		this.velocity = new Point2D(0,0);
		this.acceleration = new Point2D(0,0);
		this.spin = 0;
		this.angAccel = 0;
		this.mass = mass;
		this.density = mass/this.area;
		this.restitution = 0.5;
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

	//Moves the rigid body based on the forces acting on it
	public void updateForce(){
		double netX = 0;
		double netY = 0;
		for(Point2D f : forces){
			netX += f.getX();
			netY += f.getY();
		}
		netX /= mass;	//F = ma
		netY /= mass;

		double mag = Math.sqrt(netX * netX + netY * netY);	//Magnitude and direction of acceleration; m/s^2
		double dir = Math.atan2(netY, netX);
		this.acceleration = new Point2D(mag * Math.cos(dir), mag * Math.sin(dir));
	}

	//Updates the body based on acceleration
	public void move(int timeStep){
		Point2D vAccel = new Point2D(this.acceleration.getX() * timeStep, this.acceleration.getY() * timeStep);	//How much the velocity changes by
		this.velocity.add(vAccel);

		this.translate(this.velocity.getX() * timeStep, this.velocity.getY() * timeStep);
	}

	public void setRestitution(double restitution){
		this.restitution = restitution;
	}
	public void setMass(double mass){
		this.mass = mass;
	}
	public void addForce(Point2D force){
		this.forces.add(force);
	}
	public void delForce(Point2D force){
		if(this.forces.contains(force)){
			this.forces.remove(force);
		}
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

		r.addForce(new Point2D(0,-9.81));
		r.move(2);
		r.move(3);
		System.out.println(r);
	}
	*/

}