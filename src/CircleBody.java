/*
   Circle.java                 2018 June 15th
   Adam Mehdi, Gary Sun, Leo Chen   2018 May 7th

   Basic unit in the physics engine,
   Circles which can collide with other and rigidBodies

   Known Bugs:
   	everything
 */

import javafx.collections.ObservableList;
import javafx.geometry.Point2D;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

import java.util.ArrayList;

public class CircleBody extends RigidBody{
    //Describes the shape
    private Point2D center;
    private double radius;

    //Describes the shapes properties
    private ArrayList<Point2D> forces; //All forces being applied in a certain frame
    private Point2D velocity;
    private Point2D acceleration;
    private double restitution; //Bounciness of object
    private double density;
    private double mass;
    private boolean fixed; //True represents infinite mass

    private double scale; //Size of shape relative to screen (larger value = smaller shape)
    private boolean hasCollided;
    private Point2D tmpVel;

    //Stats at start of each sim
    private Point2D startCenter;
    private Point2D startVel;

    //The actual circle shape added to layout
    private Pane root;
    private javafx.scene.shape.Circle polygon;
    private Circle circle;

    public CircleBody(){
        new CircleBody(0,0,0,0,false, new Pane());
    }

    //Constructor for circlebody
    public CircleBody(double x, double y, double radius, double mass, boolean fixed, Pane root){
        this.center = new Point2D(x, y);
        this.radius = radius;
        this.mass = mass;
        this.fixed = fixed;
        this.density = mass / (Math.PI * radius * radius);
        this.forces = new ArrayList<Point2D>();
        this.velocity = new Point2D(0,0);
        this.acceleration = new Point2D(0,0);
        this.restitution = 0.95;
        this.scale = 1;
        this.startCenter = center;
        this.startVel = velocity;
        this.hasCollided = false;
        this.tmpVel = velocity;
        this.root = root;

        //Adds shape of circle to pane
        this.polygon = new javafx.scene.shape.Circle(x, y, radius);
        this.polygon.setFill(Color.BLACK);
        this.circle = new Circle(x, y, 2);
        this.circle.setFill(Color.RED);
        root.getChildren().addAll(polygon, circle);


    }

    //Moves circle over to new location
    public void translate(double dx, double dy){
        this.center = new Point2D(center.getX() + dx, center.getY() + dy);
        this.update(null, null, this.center);
    }

    //Checks and computes collision normal between two circlebodies
    public static void isCollidingCC(CircleBody a, CircleBody b, double simSpeed, boolean allowRotate){
        if(a.polygon.getBoundsInLocal().intersects(b.polygon.getBoundsInLocal())){
            Point2D contact = null;
            Point2D normalDirection = new Point2D(0, 0);
            Point2D[] info = {normalDirection, contact};
            Point2D distance = new Point2D(b.center.getX() - a.center.getX(), b.center.getY() - a.center.getY());
            if(a.radius + b.radius >= distance.magnitude()){
                normalDirection = a.center.subtract(b.center).normalize().multiply(distance.magnitude()-b.radius);
                contact = normalDirection.add(a.center);
                info[0] = normalDirection; info[1] = contact;
                if (contact != null) {
                    resolveCollision(a, b, info, simSpeed, allowRotate);
                    penetrationFix(a, b, info[0]);
                }
            }
        }
    }

    //Checks and computes collision normal between a rigidbody and circlebody
    public static void isCollidingRC(RigidBody a, CircleBody b, double simSpeed, boolean allowRotate) {
        if (a.getPolygon().getBoundsInLocal().intersects(b.polygon.getBoundsInLocal())) {


            Point2D[] info = new Point2D[2];
            double shortestDist = Double.POSITIVE_INFINITY;
            Point2D contact = null;
            Point2D contactTmp = null;
            Point2D sideTmp = null;
            Point2D perpLine;
            Point2D normalDirection = new Point2D(0, 0);
            info[0] = normalDirection;
            info[1] = contact;
            for (int j = 0; j < a.getSides() * 2; j += 2) { //Goes through all b polygon vertices
                ObservableList<Double> aVertices = a.getPolygon().getPoints();

                double x1 = aVertices.get(j);
                double y1 = aVertices.get(j + 1); //Side being hit
                double x2 = aVertices.get((j + 2) % (a.getSides() * 2));
                double y2 = aVertices.get((j + 3) % (a.getSides() * 2));
                double sideLen = Math.sqrt((y2 - y1) * (y2 - y1) + (x2 - x1) * (x2 - x1));
                perpLine = new Point2D(b.radius * (y2 - y1) / sideLen, b.radius * (x1 - x2) / sideLen);
                contactTmp = new Point2D(b.center.getX(), b.center.getY());
                double x0 = b.center.getX();
                double y0 = b.center.getY(); //Vertex hitting polygon
                double tmpDist2 = Math.abs((y2 - y1) * (x0) - (x2 - x1) * (y0) + x2 * y1 - y2 * x1) / sideLen;


                if (tmpDist2 <= b.radius) {
                    normalDirection = new Point2D(tmpDist2 * (y2 - y1) / sideLen, tmpDist2 * (x1 - x2) / sideLen);
                    contact = contactTmp;
                }
            }
            info[0] = normalDirection;
            info[1] = contact;
            if (contact != null) {
                resolveCollision(a, b, info, simSpeed, allowRotate);
                penetrationFix(a, b, info[0]);
            }
        }
    }

    //Checks and computes collision normal between a circlebody and rigidbody
    public static void isCollidingCR(CircleBody a, RigidBody b, double simSpeed, boolean allowRotate){
        isCollidingRC(b, a, simSpeed, allowRotate);
    }

    //Makes a deepcopy duplicate of the shape
    public CircleBody copy(Pane src){
        return new CircleBody(center.getX(), center.getY(), radius, mass, fixed, src);
    }

    //Updates where shape is located
    public void update(double[] XP, double[] YP, Point2D newCenter){
        center = newCenter;
        polygon.setCenterX(center.getX() / scale);
        polygon.setCenterY(center.getY() / scale);
        circle.setCenterX(center.getX() / scale);
        circle.setCenterY(center.getY() / scale);
    }

    //Puts circle back to starting state at beginning of simulation
    public void reset(double newScale){
        velocity = startVel;
        tmpVel = new Point2D(0, 0);
        acceleration = new Point2D(0, 0);
        this.setScale(newScale);
        this.update(null, null, startCenter);
    }


    public CircleBody clone(Pane canvas)
    {
        CircleBody temp = new CircleBody(this.center.getX(),this.center.getY(), this.radius,this.mass,this.fixed,canvas);
        polygon = new javafx.scene.shape.Circle(this.center.getX(), this.center.getY(), this.radius);
        temp.setScale(Math.max(polygon.getBoundsInLocal().getWidth()/100, polygon.getBoundsInLocal().getHeight()/100));
        temp.translate(100000, 100000);
        return new CircleBody(temp.center.getX(), temp.center.getY(), temp.radius, temp.mass, temp.fixed, canvas);
    }


    //Getters for circlebody
    public Point2D getCenter() {
        return center;
    }

    public Point2D getSize(){
        return new Point2D(polygon.getBoundsInLocal().getWidth(), polygon.getBoundsInLocal().getHeight());
    }

    public Point2D getMin(){
        return new Point2D(polygon.getBoundsInLocal().getMinX(), polygon.getBoundsInLocal().getMinY());
    }

    //Removes the shapes of the body from the pane
    public void removeShape(){
        root.getChildren().removeAll(polygon, circle);
    }


}
