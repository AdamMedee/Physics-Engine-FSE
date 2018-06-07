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

public class Circle extends RigidBody{
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

    private javafx.scene.shape.Circle instance;

    public Circle(double x, double y, double radius, double mass, boolean fixed, Pane root){
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

        instance = new javafx.scene.shape.Circle(x, y, radius);
        instance.setFill(Color.BLACK);
        root.getChildren().add(instance);
    }

    public void translate(double dx, double dy){
        this.center = new Point2D(center.getX() + dx, center.getY() + dy);
    }

    public static void isColliding(Circle a, Circle b, double simSpeed){
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

    public static Point2D isColliding(RigidBody r, Circle c){
        ObservableList<Double> verticies = r.getPolygon().getPoints();
        double shortestDist = Double.POSITIVE_INFINITY;
        Point2D contact = null;
        Point2D normalDirection = null;
        return null;
    }



    public static Point2D isColliding(Circle c, RigidBody r){
        return isColliding(r, c);
    }




    public void update(Point2D newCenter){
        center = newCenter;
        instance.setCenterX(center.getX() / scale);
        instance.setCenterY(center.getY() / scale);
    }



    public void reset(double newScale){
        velocity = startVel;
        tmpVel = new Point2D(0, 0);
        acceleration = new Point2D(0, 0);
        this.setScale(newScale);
        this.update(startCenter);
    }





    public Circle clone(Pane canvas)
    {
        Circle temp = new Circle(this.center.getX(),this.center.getY(), this.radius,this.mass,this.fixed,canvas);
        instance = new javafx.scene.shape.Circle(this.center.getX(), this.center.getY(), this.radius);
        temp.setScale(Math.max(instance.getBoundsInLocal().getWidth()/100, instance.getBoundsInLocal().getHeight()/100));
        temp.translate(100000, 100000);
        return new Circle(temp.center.getX(), temp.center.getY(), temp.radius, temp.mass, temp.fixed, canvas);
    }



    public Point2D getCenter() {
        return center;
    }

    public void setCenter(Point2D center) {
        this.center = center;
    }

    public double getRadius() {
        return radius;
    }

    public void setRadius(double radius) {
        this.radius = radius;
    }


}
