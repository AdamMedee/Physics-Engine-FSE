/*
   Circle.java                 2018 June 15th
   Adam Mehdi, Gary Sun, Leo Chen   2018 May 7th

   Basic unit in the physics engine,
   Circles which can collide with other and rigidBodies

   Known Bugs:
   	everything
 */

import javafx.geometry.Point2D;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;

import java.util.ArrayList;

public class Circle{
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

    public static boolean isColliding(Circle a, Circle b){
        double dx = a.getCenter().getX() - b.getCenter().getY();
        double dy = a.getCenter().getY() - b.getCenter().getY();
        if(dx * dx + dy * dy > (a.radius + b.radius) * (a.radius + b.radius)){
            return false;
        }
        return true;
    }

    public static boolean isColliding(RigidBody r, Circle c){
        return true;
    }
    public static boolean isColliding(Circle c, RigidBody r){
        return isColliding(r, c);
    }

    public static void draw(Circle obj,Pane root, double newScale)
    {
        Circle tmp = new Circle(obj.center.getX(),obj.center.getY(),obj.radius, obj.mass,obj.fixed,root);
        tmp.setScale(newScale);
        tmp.update(obj.center);
    }

    public void setScale(double newScale) {
        scale = newScale;
    }

    public void update(Point2D newCenter){
        center = newCenter;
        instance.setCenterX(center.getX() / scale);
        instance.setCenterY(center.getY() / scale);
    }

    public void updateVelocity(double timeStep){
        if(!fixed) {
            velocity = velocity.add(tmpVel);
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

    public void reset(double newScale){
        velocity = startVel;
        tmpVel = new Point2D(0, 0);
        acceleration = new Point2D(0, 0);
        this.setScale(newScale);
        this.update(startCenter);
    }

    public void run(double simSpeed, Point2D gravity, ArrayList<Circle> circles){
        addForce(gravity.multiply(mass));
        for(Circle c : circles) {
            if(!c.equals(this) && !this.hasCollided && !c.hasCollided) {
                isColliding(this, c);
            }
        }
        updateVelocity(simSpeed);
        clearForces();
        clearCollide();
    }

    public void clearForces(){
        this.forces.clear();
    }

    public void clearCollide(){
        hasCollided = false;
    }

    public Circle clone(Pane canvas)
    {
        Circle temp = new Circle(this.center.getX(),this.center.getY(), this.radius,this.mass,this.fixed,canvas);
        instance = new javafx.scene.shape.Circle(this.center.getX(), this.center.getY(), this.radius);
        temp.setScale(Math.max(instance.getBoundsInLocal().getWidth()/100, instance.getBoundsInLocal().getHeight()/100));
        temp.translate(100000, 100000);
        return new Circle(temp.center.getX(), temp.center.getY(), temp.radius, temp.mass, temp.fixed, canvas);
    }

    public void addForce(Point2D force){
        this.forces.add(force);
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

    public ArrayList<Point2D> getForces() {
        return forces;
    }

    public void setForces(ArrayList<Point2D> forces) {
        this.forces = forces;
    }

    public Point2D getVelocity() {
        return velocity;
    }

    public void setVelocity(Point2D velocity) {
        this.velocity = velocity;
    }

    public Point2D getAcceleration() {
        return acceleration;
    }

    public void setAcceleration(Point2D acceleration) {
        this.acceleration = acceleration;
    }

    public double getRestitution() {
        return restitution;
    }

    public void setRestitution(double restitution) {
        this.restitution = restitution;
    }

    public double getMass() {
        return mass;
    }

    public void setMass(double mass) {
        this.mass = mass;
    }

    public double getDensity() {
        return density;
    }

    public void setDensity(double density) {
        this.density = density;
    }

    public boolean isFixed() {
        return fixed;
    }

    public void setFixed(boolean fixed) {
        this.fixed = fixed;
    }

    public double getScale() {
        return scale;
    }

    public Point2D getStartCenter() {
        return startCenter;
    }

    public void setStartCenter(Point2D startCenter) {
        this.startCenter = startCenter;
    }

    public Point2D getStartVel() {
        return startVel;
    }

    public void setStartVel(Point2D startVel) {
        this.startVel = startVel;
    }

    public boolean isHasCollided() {
        return hasCollided;
    }

    public void setHasCollided(boolean hasCollided) {
        this.hasCollided = hasCollided;
    }

    public Point2D getTmpVel() {
        return tmpVel;
    }

    public void setTmpVel(Point2D tmpVel) {
        this.tmpVel = tmpVel;
    }

    public javafx.scene.shape.Circle getInstance() {
        return instance;
    }

    public void setInstance(javafx.scene.shape.Circle instance) {
        this.instance = instance;
    }
}
