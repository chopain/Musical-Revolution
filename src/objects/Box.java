package objects;

import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;
import physics.LineSegment;
import physics.Point;
import physics.Ray;

import java.util.ArrayList;

public class Box {
    private ArrayList<LineSegment> walls;
    private Rectangle r;
    private double x;
    private double y;
    private double width;
    private double height;
    private static int counter = 0;
    public final int id;


    // Set outward to true if you want a box with outward pointed normals
    public Box(int x, int y, int width, int height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        walls = new ArrayList<LineSegment>();
        walls.add(new LineSegment(new Point(x, y), new Point(x + width, y)));
        walls.add(new LineSegment(new Point(x + width, y), new Point(x + width, y + height)));
        walls.add(new LineSegment(new Point(x + width, y + height), new Point(x, y + height)));
        walls.add(new LineSegment(new Point(x, y + height), new Point(x, y)));
        id = counter++;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public double getWidth() {
        return width;
    }

    public double getHeight() {
        return height;
    }

    public Ray bounceRay(Ray in, double time) {
        // For each of the walls, check to see if the Ray intersects the wall
        Point intersection = null;
        for (int n = 0; n < walls.size(); n++) {
            LineSegment seg = in.toSegment(time);
            intersection = walls.get(n).intersection(seg);
            if (intersection != null) {
                // If it intersects, find out when
                double t = in.getTime(intersection);
                // Reflect the Ray off the line segment
                Ray newRay = walls.get(n).reflect(seg, in.getSpeed());
                // Figure out where we end up after the reflection.
                Point dest = newRay.endPoint(time - t);
                return new Ray(dest, newRay.getV(), in.getSpeed());
            }
        }
        return null;
    }
    //check if other box and this box intersect -- return true if intersection, false otherwise
    public boolean boxIntersection(Box other){
        boolean intersection = false;
        for(int i = 0; i < 5; i ++){
            LineSegment segThis = this.walls.get(i);
            for(int x = 0; x < 5; x++){
                LineSegment otherSeg = other.walls.get(x);
                if(otherSeg.intersection(segThis) != null){
                    return true;
                }
            }
        }
        return false;
    }

    public void move(double deltaX, double deltaY) {
        for (int n = 0; n < walls.size(); n++)
            walls.get(n).move(deltaX, deltaY);
        x += deltaX;
        y += deltaY;
    }

    public boolean contains(Point p) {
        if (p.getX() >= x && p.getX() <= x + width && p.getY() >= y && p.getY() <= y + height)
            return true;
        return false;
    }

    public Shape getShape() {
        r = new Rectangle(x, y, width, height);
        r.setFill(Color.WHITE);
        r.setStroke(Color.BLACK);
        return r;
    }

    public void updateShape() {
        r.setX(x);
        r.setY(y);
    }
}
