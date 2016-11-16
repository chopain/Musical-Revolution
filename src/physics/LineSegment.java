package physics;

import javafx.scene.shape.Line;

/* The LineSegment class represent a directed line segment with an attached
 * normal. The direction of the line segment is a vector running from point
 * a to point b. The normal is a unit vector rotated 90 degrees clockwise
 * from the direction vector in pixel coordinate space.
 */
public class LineSegment {
    public Point a;
    public Point b;

    public LineSegment(Point a, Point b) {
        this.a = a;
        this.b = b;
    }

    public void move(int deltaX, int deltaY) {
        a.setX(a.getX() + deltaX);
        a.setY(a.getY() + deltaY);
        b.setX(b.getX() + deltaX);
        b.setY(b.getY() + deltaY);
    }

    public Vector toVector() {
        return new Vector(b.getX() - a.getX(), b.getY() - a.getY());
    }

    // Return the point at which these two line segments intersect,
    // or null if they do not.
    public Point intersection(LineSegment other) {
        // The technique used to compute the intersection is described at
        // http://stackoverflow.com/questions/563198/how-do-you-detect-where-two-line-segments-intersect
        LineSegment connector = new LineSegment(this.a, other.a);
        Vector me = this.toVector();
        Vector them = other.toVector();
        Vector us = connector.toVector();
        double common = Vector.crossProduct(me, them);
        // If the cross product is positive, the other line segment is coming
        // from our right side, and we should not allow it to intersect us.
        if (common >= 0.0)
            return null;
        double t = Vector.crossProduct(us, them) / common;
        double u = Vector.crossProduct(us, me) / common;
        if (0 <= t && t <= 1 && 0 <= u && u <= 1) {
            return new Point(a.getX() + me.dX * t, a.getY() + me.dY * t);
        }
        return null;
    }

    // Return a Ray that represents the reflection of the
    // other line segment. For this to work correctly,
    // the other line segment's vector needs to be 
    // oriented in the opposite direction of our normal.
    // The returned Ray is positioned at the point of 
    // intersection and is oriented in the reflected direction.
    public Ray reflect(LineSegment other, double speed) {
        // Compute the normal to this segment
        Vector N = new Vector(a.getY() - b.getY(), b.getX() - a.getX());
        N.normalize();
        // Compute the relected direction
        Vector V = other.toVector();
        double dot = Vector.dotProduct(N, V);
        if (dot > 0)
            dot = -dot;
        Vector R = new Vector(V.dX - 2 * dot * N.dX, V.dY - 2 * dot * N.dY);
        // Construct and return the result Ray
        Point origin = this.intersection(other);
        return new Ray(origin, R, speed);
    }

    public Line toShape(){
        return new Line(a.getX(), a.getY(),b.getX() , b.getY());
    }
}
