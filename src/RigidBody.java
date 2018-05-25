/*
   RigidBody.java                 2018 May 7th
   Adam Mehdi, Gary Sun, Leo Chen   2018 May 7th

   Basic unit in the physics engine,
   polygons which can collide with other
   polygons including rotational motion.

   Known Bugs:
   	everything
 */

import javafx.collections.ObservableList;
import javafx.scene.Group;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Polygon;
import java.util.ArrayList;
import java.util.Arrays;
import javafx.geometry.Point2D;

public class RigidBody{
	private int sides;							//# of sides
	private Point2D center;						//Coords of center (x,y)
	private Polygon polygon;					//Polygon defining the shape
	private double[] xPoints, yPoints;			//Set of x coords and y coords
	private ArrayList<Point2D> forces;			//The forces acting on the body

	private Point2D velocity;
	private Point2D acceleration;
	private double spin;						//Angular velocity
	private double angAccel;					//Angular acceleration
	private double MOI;							//Moment of Inertia
	private double restitution;					//Coefficient of restitution
	private double kineticFriction, staticFriction;
	private double area; 						//Constant area of the polygon
	private double mass; 						//Mass and density is consistent throughout the body
	private double density;
	private boolean fixed; 						// Whether the rigid body can move

	private double scale;
	private Point2D startCenter;				//Start position of the center of the rigidbody
	private Point2D startVel;					//Starting velocity of the body
	private double[] startXPoints, startYPoints;//Start polygon points
	private double startSpin;					//Start angular velocity

	private Circle circle;


	public RigidBody(double[] xPoints, double[] yPoints, double mass, boolean fixed, Pane root){
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

		this.center = new Point2D(centerX, centerY);
		this.startCenter = center;
		this.forces = new ArrayList<>();
		this.velocity = new Point2D(0,0);
		this.startVel = velocity;
		this.acceleration = new Point2D(0,0);
		this.spin = 0;
		this.startSpin = spin;
		this.angAccel = 0;
		this.mass = mass;
		this.density = mass/this.area;
		this.restitution = 0.5;
		this.xPoints = xPoints;
		this.yPoints = yPoints;
		this.startXPoints = xPoints;
		this.startYPoints = yPoints;
		this.kineticFriction = 0.1;
		this.staticFriction = 0.2;
		this.fixed = fixed;
		this.scale = 1;

		//Creates polygon shape to add to group
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
	}


	//Allows the rigidbody to be printed
	public String toString(){
		return Arrays.toString(xPoints) + " " + Arrays.toString(yPoints) + " " + this.center;
	}


	//Updates the state of the rigidbody polygon
	public void update(double[] XP, double[] YP, Point2D newCenter){
		//Updates polygon point coords
		polygon.getPoints().clear();
		for(int i = 0; i < sides; i++){
			polygon.getPoints().add(XP[i]/scale);
			polygon.getPoints().add(YP[i]/scale);
		}
		xPoints = XP;
		yPoints = YP;

		//Resets center of mass for circle
		center = newCenter;
		circle.setCenterX(center.getX()/scale);
		circle.setCenterY(center.getY()/scale);
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
			newXP[i] = (OX * Math.cos(ang) + OY * Math.sin(ang)) + center.getX();
			newYP[i] = (OY * Math.cos(ang) - OX * Math.sin(ang)) + center.getY();
		}
		this.update(newXP, newYP, this.center);
	}


	//Moves the rigid body based on the forces acting on it
	public void updateVelocity(double timeStep){
		if(!fixed) {
			double netX = 0;
			double netY = 0;
			Point2D prevAccel = acceleration;
			//Update position based previous frame's forces
			this.translate(velocity.getX() * timeStep + (0.5 * prevAccel.getX() * timeStep * timeStep), velocity.getY() * timeStep + (0.5 * prevAccel.getY() * timeStep * timeStep));

			//Calculates net force of current frame
			for (Point2D f : forces) {
				netX += f.getX();
				netY += f.getY();
			}
			netX /= mass;    //F = ma
			netY /= mass;
			acceleration = new Point2D(netX, netY);
			Point2D avgAccel = prevAccel.add(acceleration);
			avgAccel = new Point2D(avgAccel.getX() / 2, avgAccel.getY() / 2);
			velocity = velocity.add(new Point2D(avgAccel.getX() * timeStep, avgAccel.getY() * timeStep));
		}
	}


	//Updates the states of two rigid bodies colliding at a given point
	public static void resolveCollison(RigidBody a, RigidBody b, Point2D normal){
		//Updates velocities of 2 bodies that have collided
		Point2D rv = b.velocity.subtract(a.velocity);	//Relative velocity between 2 bodies
		double normalVel = rv.dotProduct(normal);	//Find rv relative to normal vector

		if(true){		//Dont so anything if objects headed away from each other
			double e = Math.min(a.restitution, b.restitution);
			double numerator = -(1 + e) * normalVel;
			double denom = (1/a.mass + 1/b.mass);
			double j = numerator/denom;

			//Apply impulse
			Point2D impulse = new Point2D(normal.getX() * j, normal.getY() * j);

			System.out.println(impulse);
			a.velocity = a.velocity.subtract(impulse.multiply(1.0 / a.mass));	//The object doing the collision is slowed
			b.velocity = b.velocity.add(impulse.multiply(1.0 / b.mass));	        //The object being hit is sped up
			//System.out.println(a.velocity + " " + b.velocity);
		}
	}

	//Gets the normal if two rigidbodies are colliding else null
	public Point2D isColliding(RigidBody a, RigidBody b){
		if(a.polygon.getBoundsInLocal().intersects(b.polygon.getBoundsInLocal())){
			//System.out.println("Collide");
			ObservableList<Double> aVertices = a.polygon.getPoints();
			double shortestDist = Double.POSITIVE_INFINITY;
			Point2D normalDirection = new Point2D(0, 0);
			ObservableList<Double> bVertices = b.polygon.getPoints();

			for(int i = 0; i < a.sides*2; i+=2){ //Goes through all a polygon vertices
				if(b.polygon.contains(aVertices.get(i), aVertices.get(i+1))){ //Checks if vertex is in b.polygon
					for (int j = 0; j < b.sides*2; j += 2){ //Goes through all b polygon vertices

						double dx =bVertices.get((j+2) % b.sides*2) - bVertices.get(j);
						double dy = bVertices.get((j+3) % b.sides*2) - bVertices.get(j+1);
						double m = dx/dy;
						double c = -bVertices.get(j+1) + m * bVertices.get(j);
						//double tmpDist = Math.abs(m*aVertices.get(i) + aVertices.get(i+1) + c)/Math.sqrt(m * m + 1);
						double tmpDist = Math.abs(m * aVertices.get(i) - aVertices.get(i+1) + bVertices.get(j+1) - m * bVertices.get(j)) / Math.sqrt(m * m + 1);
						if(tmpDist <= shortestDist){
							shortestDist = tmpDist;
							//normalDirection = new Point2D(- dy / Math.sqrt(dx * dx + dy * dy), dx / Math.sqrt(dx * dx + dy * dy));
							normalDirection = new Point2D(-dx / Math.sqrt(dx * dx + dy * dy) , -dy / Math.sqrt(dx * dx + dy * dy));
						}
					}
					//System.out.println(normalDirection);
					return normalDirection;
				}
			}
		}
		return null;
	}

	//Runs all the methods on the rigidbody
	public void run(double simSpeed, Point2D gravity, ArrayList<RigidBody> rigidBodies){
		addForce(gravity);
		for(RigidBody body : rigidBodies) {
			if(!body.equals(this)) {
				Point2D tmpNorm = isColliding(this, body);
				if (tmpNorm != null) {
					resolveCollison(this, body, tmpNorm);
				}
			}
		}
		updateVelocity(simSpeed);
		clearForces();
	}


	//Puts rigid body back to starting state
	public void reset(double newScale){
		spin = startSpin;
		velocity = startVel;
		acceleration = new Point2D(0, 0);
		angAccel = 0;
		this.setScale(newScale);
		this.update(startXPoints, startYPoints, startCenter);
	}

	//Setter methods
	public void setRestitution(double restitution){
		this.restitution = restitution;
	}
	public void setMass(double mass){
		this.mass = mass;
	}

	public void addForce(Point2D force){
		this.forces.add(force);
	}

	//Removes force from body
	public void delForce(Point2D force){
		if(this.forces.contains(force)){
			this.forces.remove(force);
		}
	}

	//Clears all forces and vel from rigid body
	public void clearForces(){
		forces.clear();
	}

	public void setScale(double newScale) {
		scale = newScale;
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