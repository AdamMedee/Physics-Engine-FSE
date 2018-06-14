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
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Polygon;
import java.util.ArrayList;
import java.util.Arrays;
import javafx.geometry.Point2D;
//
public class RigidBody{
	private int sides;							            //# of sides
	private Point2D center;						        //Coords of center (x,y)
	private Polygon polygon;					            //Polygon defining the shape
	private double[] xPoints, yPoints;		        //Set of x coords and y coords
	private ArrayList<Point2D> forces;		        //The forces acting on the body

	private Point2D velocity;
	private Point2D acceleration;
	private double spin;						            //Angular velocity
	private double angAccel;					        //Angular acceleration
	private double MOI;							        //Moment of Inertia
	private double restitution;					        //Coefficient of restitution
	private double area; 						            //Constant area of the polygon
	private double mass; 						            //Mass and density is consistent throughout the body
	private boolean fixed; 						        // Whether the rigid body can move

	private double scale;
	private Point2D startCenter;				        //Start position of the center of the rigidbody
	private Point2D startVel;					        //Starting velocity of the body
	private double[] startXPoints, startYPoints;//Start polygon points
	private double startSpin;					        //Start angular velocity

	private Point2D tmpVel;                             //
	private double tmpSpin;                             //
	private int serialNum;                                //Allows rigidbodies to be distinguishable

	protected  Color colour;                            //Object color
	private Circle circle;                                 //Center of mass marker
	private Pane root;                                      //Which screen it is being drawn on

	public RigidBody(double mass, boolean fixed, Pane root, Color Colour){
		this.mass = mass;
	}


	public RigidBody(double[] xPoints, double[] yPoints, double mass, boolean fixed, Pane root,Color Colour){
		this.sides = xPoints.length;
		this.colour = Colour;
		this.area = 0;			//Init area, Moment of inertia and Center
		this.MOI = 0;
		double centerX = 0;
		double centerY = 0;
		for(int i=0; i < sides; i++){
			//Thanks Green for finding closed form formulas for these quantities.
			double shoelace = xPoints[i] * yPoints[(i+1) % sides] - xPoints[(i+1) % sides] * yPoints[i];
			this.area += shoelace;
			centerX += (xPoints[i] + xPoints[(i+1) % sides]) * shoelace;
			centerY += (yPoints[i] + yPoints[(i+1) % sides]) * shoelace;
			this.MOI += ((xPoints[i] * yPoints[(i+1) % sides] + 2*xPoints[i]*yPoints[i] + 2*xPoints[(i+1) % sides]*yPoints[(i+1) % sides] + xPoints[(i+1) % sides]*yPoints[i]) * shoelace);
		}
		this.area = Math.abs(this.area / 2);
		this.mass = fixed ? Double.POSITIVE_INFINITY : mass;
		this.MOI = this.MOI*mass/this.area/24;
		centerX = Math.abs(centerX) / (6 * this.area);
		centerY = Math.abs(centerY) / (6 * this.area);

		this.center = new Point2D(centerX, centerY);
		this.startCenter = center;
		this.forces = new ArrayList<>();
		this.velocity = new Point2D(0,1);
		this.startVel = velocity;
		this.tmpVel = velocity;
		this.acceleration = new Point2D(0,0);
		this.spin = 0;
		this.startSpin = spin;
		this.tmpSpin = spin;
		this.angAccel = 0;
		this.restitution = 1;
		this.xPoints = xPoints;
		this.yPoints = yPoints;
		this.startXPoints = xPoints;
		this.startYPoints = yPoints;
		this.fixed = fixed;
		this.scale = 1;
		this.root = root;

		//Creates polygon shape to add to group
		this.polygon = new Polygon();
		Double[] tmpPoints = new Double[sides*2];
		for(int i = 0; i < sides; i++){
			tmpPoints[i*2] = xPoints[i];
			tmpPoints[i*2+1] = yPoints[i];
		}
		this.polygon.getPoints().addAll(tmpPoints);
		this.polygon.setFill(this.colour);
		root.getChildren().add(polygon);

		//Creates a center of mass as a circle for the polygon for testing purposes
		this.circle = new Circle(centerX, centerY, 2);
		circle.setFill(javafx.scene.paint.Color.RED);
		root.getChildren().add(circle);
	}


	//Allows the rigidbody to be printed
	public String toString(){
		return Arrays.toString(xPoints) + " " + Arrays.toString(yPoints) + " " + this.center;
	}

	//Creates a copy of the rigidbody for the systemMenu
	public RigidBody copy(Pane src,Color colour){
		return new RigidBody(xPoints, yPoints, mass, fixed, src,colour);
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
		spin += tmpSpin;
		if(!fixed) {
			this.rotate(spin);
		}
	}

	//Moves the rigid body based on the forces acting on it
	public void updateVelocity(double timeStep, boolean allowRotate){
		if(!fixed) {
			velocity = velocity.add(tmpVel);
			double netX = 0;
			double netY = 0;
			Point2D prevAccel = acceleration;
			//Update position based previous frame's forces
			this.translate(velocity.getX() * timeStep, velocity.getY() * timeStep);
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

			velocity = velocity.add(avgAccel);
			if(allowRotate){
			    //Prevents bouncing when spin approaches 0
				if(spin > 0.00001){
					spin -= mass*timeStep/1000.0;
				}
				else if(spin < -0.00001){
					spin += mass*timeStep/1000.0;
				}
				else{
					spin = 0;
				}
			}
			else{
				spin = 0;
			}



		}
	}

	//If two rigidbodies are intersecting, moves them apart
	public static void penetrationFix(RigidBody a, RigidBody b, Point2D normal){
		//How deep the collision point is in the object
		double penetrationDepth = normal.magnitude();

		//Pushes shapes out of each other if barely in
		Point2D correction = normal.normalize().multiply(penetrationDepth);                     //How big of a correction we must make
		double aRatio = b.fixed ? 1 : (b.mass/(a.mass + b.mass));                                       //Multiplier based on a mass
		double bRatio = a.fixed ? 1 : (a.mass/(a.mass + b.mass));                                       //Multiplier based on b mass
		a.translate(correction.getX() * aRatio, correction.getY() * aRatio);        //Apply the fix
		b.translate(-correction.getX() * bRatio, -correction.getY() * bRatio);
	}


	//Updates the states of two rigid bodies colliding at a given point
	//Will change normal and rotational velocity
	public static void resolveCollision(RigidBody a, RigidBody b, Point2D[] info, double simSpeed, boolean allowRotate){
		//Updates velocities of 2 bodies that have collided
		Point2D normal = info[0];	//Vector of dist from point to side
		Point2D vertex = info[1];	//Point of collision

		//How deep the collision point is in the object
		Point2D normalUnit = normal.normalize();

		//Vector from center to vertex of collision
		Point2D relA = vertex.subtract(a.center);
		Point2D relB = vertex.subtract(b.center);


		Point2D rv = b.velocity.subtract(a.velocity);	//Relative velocity between 2 bodies
		double normalVel = rv.dotProduct(normalUnit);//Find rv relative to normal vector

		double e = Math.min(a.restitution, b.restitution);	//How sticky the shapes are
		double rot = allowRotate ?  det(relA, normalUnit) * det(relA, normalUnit) / a.MOI + det(relB, normalUnit) * det(relB, normalUnit) / b.MOI : 0; //2D cross product for size of spin (since we dont incorperate 3D)
		double numerator = -(1 + e) * normalVel;
		double denom = (1/a.mass + 1/b.mass + rot);
		double j = numerator/denom;                                 //Impulse scaler

		//Apply impulse
		Point2D impulse = new Point2D(normalUnit.getX() * j, normalUnit.getY() * j);

		a.velocity = a.velocity.subtract(impulse.multiply(1 / a.mass));	//The object doing the collision is slowed
		b.velocity = b.velocity.add(impulse.multiply(1 / b.mass));	        //The object being hit is sped up

		if(allowRotate) {
			a.spin += (1 / a.MOI) * det(relA, normalUnit) * j;
			b.spin -= (1 / b.MOI) * det(relB, normalUnit) * j;
		}
	}

	//Gets the normal if two rigidbodies are colliding else null
	public static void isColliding(RigidBody a, RigidBody b, double simSpeed, boolean allowRotate){
		if(a.polygon.getBoundsInLocal().intersects(b.polygon.getBoundsInLocal())){
			//First check if they are close together
			ObservableList<Double> aVertices = a.polygon.getPoints();	//Verticies of polygons
			ObservableList<Double> bVertices = b.polygon.getPoints();
			double shortestDist = Double.POSITIVE_INFINITY;			//Make the shortest distance as large as possible
			Point2D contact = null;														//Point of contact (Vertex)
			Point2D normalDirection = new Point2D(0, 0);			//Normal vector of collison
			Point2D[] info = {normalDirection, contact};							//Information we need to return


			for(int i = 0; i < a.sides*2; i+=2){ //Goes through all a polygon vertices
				if(b.polygon.contains(aVertices.get(i), aVertices.get(i+1))){ //Checks if vertex is in b.polygon
					shortestDist = Double.POSITIVE_INFINITY;
					contact = null;
					normalDirection = new Point2D(0, 0);
					info[0] = normalDirection; info[1] = contact;
					for (int j = 0; j < b.sides*2; j += 2){ //Goes through all b polygon vertices

						double x0 = aVertices.get(i); double y0 = aVertices.get(i + 1); 														//Vertex hitting polygon
						double x1 = bVertices.get(j); double y1 = bVertices.get(j + 1); 														//x1, x2, y1, y2 form an edge that we test for collison
						double x2 = bVertices.get((j+2) % (b.sides*2)); double y2 = bVertices.get((j+3) % (b.sides*2));

						double sideLen = Math.sqrt((y2 - y1)*(y2 - y1) + (x2 - x1)*(x2 - x1));
						double tmpDist2 = Math.abs((y2 - y1)*(x0) - (x2 - x1)*(y0) + x2*y1 - y2*x1)/sideLen;					//Distance from point to line
						if(tmpDist2 <= shortestDist){
							//Update shortest distance if we found one
							shortestDist = tmpDist2;
							tmpDist2 = Math.abs((y2 - y1)*(x0) - (x2 - x1)*(y0) + x2*y1 - y2*x1)/sideLen;
							normalDirection = new Point2D(tmpDist2*(y2 - y1)/sideLen, tmpDist2*(x1 - x2)/sideLen);
							contact = new Point2D(aVertices.get(i), aVertices.get(i+1));
						}
					}
					info[0] = normalDirection; info[1] = contact;
					if (contact != null) {
						//Push polygons out of each other if a collision happened
						penetrationFix(a, b, info[0]);
					}
				}
			}
			if (contact != null) {
				//Apply forces for the collision
				resolveCollision(a, b, info, simSpeed, allowRotate);
			}
		}
	}

	//Runs all the methods on the rigidbody
	public void run(double simSpeed, Point2D gravity, ArrayList<RigidBody> rigidBodies, boolean allowRotate){
		addForce(gravity.multiply(mass));			//Add a constant gravity
		for(RigidBody body : rigidBodies) {
			if(!body.equals(this) && (!this.fixed || !body.fixed)){
				isColliding(this, body, simSpeed, allowRotate);
			}
		}
		updateSpin(simSpeed);							//Update object variables
		updateVelocity(simSpeed, allowRotate);
		clearForces();
	}


	//Puts rigid body back to starting state
	public void reset(double newScale, boolean resetPos){
        this.setScale(newScale);
	    if(resetPos){
            spin = startSpin;
            velocity = startVel;
            tmpSpin = 0;
            tmpVel = new Point2D(0, 0);
            acceleration = new Point2D(0, 0);
            angAccel = 0;
            this.update(startXPoints, startYPoints, startCenter);
        }
        else{
            this.update(xPoints, yPoints, center);
        }
	}

	public Point2D getSize(){
		//Returns size of bounding box. Used in the systemMenu
		return new Point2D(polygon.getBoundsInLocal().getWidth(), polygon.getBoundsInLocal().getHeight());
	}

	public Point2D getMin(){
		//Returns the min cords of the polygon. Used in the systemMenu
		return new Point2D(polygon.getBoundsInLocal().getMinX(), polygon.getBoundsInLocal().getMinY());
	}

	//Setter methods
	public void setRestitution(double restitution){
		this.restitution = restitution;
	}
	public void setMass(double mass){
		this.mass = mass;
		setFixed(mass == Double.POSITIVE_INFINITY);
	}

	public void setFixed(boolean fixed) {
		this.fixed = fixed;
		if(fixed) {
			this.velocity = new Point2D(0, 0);	//Fixed objects dont move
			clearForces();
		}
	}

	public void setStartCenter(double x, double y) {
		//reset starting center location
		for(int i = 0; i < sides; i++){
			startXPoints[i] += x - startCenter.getX();
			startYPoints[i] += y - startCenter.getY();
		}
		startCenter = new Point2D(x, y);
	}

	public void addForce(Point2D force){
		this.forces.add(force);
	} 		//Add a force to the object

	//Clears all forces and vel from rigid body
	public void clearForces(){
		forces.clear();
	}

	public void setScale(double newScale) {
		//Draw the object at a new size
		scale = newScale;
		update(xPoints, yPoints, center);
	}

	public static double det(Point2D a, Point2D b){
		//Cross product with z=0
		return a.getX() * b.getY() - a.getY() * b.getX();
	}

	public Point2D getCenter(){
		return center;
	}

	public Point2D getStartCenter(){
		return startCenter;
	}

	public double getRestitution(){
		return restitution;
	}

	public double getMass()
    {
        return this.mass;
    }

    public Polygon getPolygon(){
		return this.polygon;
	}

    public int getSides() { return this.sides; }

    public double getScale(){
    	return this.scale;
	}

	public double[] getXPoints(){
    	return xPoints;
	}

	public double[] getYPoints(){
		return yPoints;
	}

	public boolean getFixed(){
		return fixed;
	}

	public Color getColour()
	{
		return this.colour;
	}

	public int getSerialNum()
	{
		return  this.serialNum;
	}

	public void setSerialNum(int n)
	{
		this.serialNum = n;
	}

	public void removeShape(){
		//Stops the rigidbody from being displayed onscreen
		root.getChildren().removeAll(polygon, circle);
	}
}