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
		this.mass = fixed ? Double.POSITIVE_INFINITY: mass;
		this.MOI = this.MOI*mass/this.area/24;
		centerX = centerX / (6 * this.area);
		centerY = centerY / (6 * this.area);

		this.center = new Point2D(centerX, centerY);
		this.startCenter = center;
		this.forces = new ArrayList<>();
		this.velocity = new Point2D(0,1);
		this.startVel = velocity;
		this.acceleration = new Point2D(0,0);
		this.spin = 0;
		this.startSpin = spin;
		this.angAccel = 0;
		this.density = mass/this.area;
		this.restitution = 0.95;
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
		//Shifts all points over by some amount
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
			double OX = xPoints[i] - center.getX();	//Shift to origin
			double OY = yPoints[i] - center.getY();
			newXP[i] = (OX * Math.cos(ang) + OY * Math.sin(ang)) + center.getX();	//Rotation matrix
			newYP[i] = (OY * Math.cos(ang) - OX * Math.sin(ang)) + center.getY();
		}
		this.update(newXP, newYP, this.center);
	}

	public void updateSpin(double timeStep){
		//System.out.println(this.spin);
		if(!fixed) {
			this.rotate(spin);
		}
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

	//If two rigidbodies are intersecting, moves them apart
	public static void penetrationFix(RigidBody a, RigidBody b, Point2D normal){
		//How deep the collision point is in the object
		double penetrationDepth = normal.magnitude();
		Point2D normalUnit = normal.normalize();

		//Pushes shapes out of each other if barely in
		final double correctPercent = 0.5;
		Point2D correction = normalUnit.multiply(penetrationDepth / (1.0 / a.mass + 1.0 / b.mass) * correctPercent);
		a.translate(-correction.multiply(1.0 / a.mass).getX(),  correction.multiply(1.0 / a.mass).getY());
		b.translate(correction.multiply(1.0 / b.mass).getX(), correction.multiply(1.0 / b.mass).getY());
	}


	//Updates the states of two rigid bodies colliding at a given point
	//Will change normal and rotational velocity
	public static void resolveCollision(RigidBody a, RigidBody b, Point2D[] info){
		//Updates velocities of 2 bodies that have collided
		Point2D normal = info[0];	//Vector of dist from point to side
		Point2D vertex = info[1];	//Point of collison

		//How deep the collision point is in the object
		Point2D normalUnit = normal.normalize();

		//Vector from center to vertex of collision
		Point2D relA = vertex.subtract(a.center);
		Point2D relB = vertex.subtract(b.center);

		Point2D rv = b.velocity.subtract(a.velocity);	//Relative velocity between 2 bodies
		double normalVel = rv.dotProduct(normalUnit) + (relB.magnitude()*b.spin - relA.magnitude()*a.spin);	//Find rv relative to normal vector

		double e = Math.min(a.restitution, b.restitution);	//How sticky the shapes are
		double rot = det(relA, normalUnit) * det(relA, normalUnit) / a.MOI + det(relB, normalUnit) * det(relB, normalUnit) / b.MOI;
		double numerator = -(1 + e) * normalVel;
		double denom = (1/a.mass + 1/b.mass + rot);
		double j = numerator/denom;

		//Apply impulse
		Point2D impulse = new Point2D(normalUnit.getX() * j, normalUnit.getY() * j);

		a.velocity = a.velocity.subtract(impulse.multiply(1 / a.mass));	//The object doing the collision is slowed
		b.velocity = b.velocity.add(impulse.multiply(1 / b.mass));	        //The object being hit is sped up

		//a.spin += 1/a.MOI * det(relA, normal) * normalUnit.multiply(j).magnitude();
		//b.spin += 1/b.MOI * det(relB, normal) * normalUnit.multiply(j).magnitude();

		//Causes objects to spin based on their MOI
		a.spin += (1/a.MOI) * det(relA, normalUnit) * j;
		b.spin -= (1/b.MOI) * det(relB, normalUnit) * j;
	}

	//Gets the normal if two rigidbodies are colliding else null
	public static void isColliding(RigidBody a, RigidBody b){
		if(a.polygon.getBoundsInLocal().intersects(b.polygon.getBoundsInLocal())){

			ObservableList<Double> aVertices = a.polygon.getPoints();
			double shortestDist = Double.POSITIVE_INFINITY;
			Point2D contact = null;
			Point2D normalDirection = new Point2D(0, 0);
			ObservableList<Double> bVertices = b.polygon.getPoints();
			Point2D[] info = {normalDirection, contact};

			for(int i = 0; i < a.sides*2; i+=2){ //Goes through all a polygon vertices
				if(b.polygon.contains(aVertices.get(i), aVertices.get(i+1))){ //Checks if vertex is in b.polygon
					for (int j = 0; j < b.sides*2; j += 2){ //Goes through all b polygon vertices

						double x0 = aVertices.get(i); double y0 = aVertices.get(i + 1);
						double x1 = bVertices.get(j); double y1 = bVertices.get(j + 1);
						double x2 = bVertices.get((j+2) % (b.sides*2-1)); double y2 = bVertices.get((j+3) % (b.sides*2-1));
						double sideLen = Math.sqrt((y2 - y1)*(y2 - y1) + (x2 - x1)*(x2 - x1));
						double tmpDist = Math.abs((y2 - y1)*x0 - (x2 - x1)*y0 + x2*y1 - y2*x1)/sideLen;

						if(tmpDist <= shortestDist){
							shortestDist = tmpDist;
							normalDirection = new Point2D(tmpDist*(y2 - y1)/sideLen, tmpDist*(x1 - x2)/sideLen);
							contact = new Point2D(aVertices.get(i), aVertices.get(i+1));
						}
					}
					info[0] = normalDirection; info[1] = contact;
					penetrationFix(a, b, normalDirection);
				}

			}
			if(contact != null) {
				resolveCollision(a, b, info);
			}

		}
	}

	//Runs all the methods on the rigidbody
	public void run(double simSpeed, Point2D gravity, ArrayList<RigidBody> rigidBodies){
		addForce(gravity);
		for(RigidBody body : rigidBodies) {
			if(!body.equals(this)) {
				isColliding(this, body);
			}
		}
		updateSpin(simSpeed);
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

	public static double det(Point2D a, Point2D b){
		return a.getX() * b.getY() - a.getY() * b.getX();
	}

}