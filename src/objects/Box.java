package objects;

import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;
import physics.LineSegment;
import physics.Point;

import java.util.ArrayList;

public class Box {
    private final ArrayList<LineSegment> walls;
    private Rectangle r;
    private final double x;
    private final double y;
    private final double width;
    private final double height;
    private static int counter = 0;
    public int boxid;


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
        boxid = counter++;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public Shape getShape() {
        r = new Rectangle(x, y, width, height);
        r.setFill(Color.WHITE);
        r.setStroke(Color.BLACK);
        return r;
    }
}
