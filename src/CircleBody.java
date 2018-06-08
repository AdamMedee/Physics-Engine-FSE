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

import java.util.ArrayList;

public class CircleBody extends RigidBody{
    private Point2D center;
    private double radius;

    private ArrayList<Point2D> forces;
    private Point2D velocity;
    private Point2D acceleration;
    private double restitution;
    private double mass;
    private double density;

    private boolean fixed;
    private double scale;
    private Point2D startCenter;
    private Point2D startVel;
    private boolean hasCollided;
    private Point2D tmpVel;

    private javafx.scene.shape.Circle polygon;

    public CircleBody(){
        new CircleBody(0,0,0,0,false, new Pane());
    }

    public CircleBody(double x, double y, double radius, double mass, boolean fixed, Pane root){
        this.center = new Point2D(x, y);
        this.radius = radius;
        this.mass = mass;
        this.fixed = fixed;
        density = mass / (Math.PI * radius * radius);
        forces = new ArrayList<Point2D>();
        velocity = new Point2D(0,0);
        acceleration = new Point2D(0,0);
        restitution = 0.95;
        scale = 1;
        startCenter = center;
        startVel = velocity;
        hasCollided = false;
        tmpVel = velocity;

        polygon = new javafx.scene.shape.Circle(x, y, radius);
        polygon.setFill(Color.BLACK);
        root.getChildren().add(polygon);
    }

    public void translate(double dx, double dy){
        this.center = new Point2D(center.getX() + dx, center.getY() + dy);
    }

    public static void isColliding(CircleBody a, CircleBody b, double simSpeed){
        double dx = a.getCenter().getX() - b.getCenter().getY();
        double dy = a.getCenter().getY() - b.getCenter().getY();
        if(dx * dx + dy * dy <= (a.radius + b.radius) * (a.radius + b.radius)){
            double angle = Math.atan2(b.center.getY() - a.center.getY(), b.center.getX() - a.center.getX());
            Point2D contact = new Point2D(a.center.getX() + a.radius * Math.cos(angle), a.center.getY() + a.radius * Math.sin(angle));
            Point2D normalDirection = a.center.subtract(b.center).normalize();
            Point2D[] info = {normalDirection, contact};
            if(contact != null && normalDirection != null){
                penetrationFix(a, b, normalDirection);
                resolveCollision(a, b, info, simSpeed);
            }
        }
    }

    public static Point2D isColliding(RigidBody r, CircleBody c){
        ObservableList<Double> verticies = r.getPolygon().getPoints();
        double shortestDist = Double.POSITIVE_INFINITY;
        Point2D contact = null;
        Point2D normalDirection = null;
        return null;
    }



    public static Point2D isColliding(CircleBody c, RigidBody r){
        return isColliding(r, c);
    }


    public CircleBody copy(Pane src){
        return new CircleBody(center.getX(), center.getY(), radius, mass, fixed, src);
    }

    public void update(Point2D newCenter){
        center = newCenter;
        polygon.setCenterX(center.getX() / scale);
        polygon.setCenterY(center.getY() / scale);
    }



    public void reset(double newScale){
        velocity = startVel;
        tmpVel = new Point2D(0, 0);
        acceleration = new Point2D(0, 0);
        this.setScale(newScale);
        this.update(startCenter);
    }


    public CircleBody clone(Pane canvas)
    {
        CircleBody temp = new CircleBody(this.center.getX(),this.center.getY(), this.radius,this.mass,this.fixed,canvas);
        polygon = new javafx.scene.shape.Circle(this.center.getX(), this.center.getY(), this.radius);
        temp.setScale(Math.max(polygon.getBoundsInLocal().getWidth()/100, polygon.getBoundsInLocal().getHeight()/100));
        temp.translate(100000, 100000);
        return new CircleBody(temp.center.getX(), temp.center.getY(), temp.radius, temp.mass, temp.fixed, canvas);
    }



    public Point2D getCenter() {
        return center;
    }

    public Point2D getSize(){
        return new Point2D(polygon.getBoundsInLocal().getWidth(), polygon.getBoundsInLocal().getHeight());
    }

    public Point2D getMin(){
        return new Point2D(polygon.getBoundsInLocal().getMinX(), polygon.getBoundsInLocal().getMinY());
    }


}
