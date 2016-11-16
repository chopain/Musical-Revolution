package physics;

/**
 * A class to facilitate physics of motion computations. To represent
 * a moving object we need to specify its location, direction, and
 * speed. The Ray class stores all of that information.
 **/
public class Ray {
    private Point origin;
    private Vector v;
    private double speed;


    public Ray(Point origin, Vector v, double speed) {
        this.origin = origin;
        this.v = v;
        this.v.normalize();
        this.speed = speed;
    }

    public Vector getV(){
        return v;
    }

    public double getSpeed(){
        return speed;
    }

    public Point getOrigin(){
        return origin;
    }

    // Construct and return a line segment representing the path
    // the object would take over the given span of time.
    public LineSegment toSegment(double time) {
        return new LineSegment(origin, this.endPoint(time));
    }

    // Compute the location after the given time span.
    public Point endPoint(double time) {
        double destX = origin.getX() + v.dX * time * speed;
        double destY = origin.getY() + v.dY * time * speed;
        return new Point(destX, destY);
    }

    // Compute and return the time at which this ray will
    // be closest to the given point.
    public double getTime(Point p) {
        if (Math.abs(v.dX) > Math.abs(v.dY))
            return (p.getX() - origin.getX()) / (v.dX * speed);
        return (p.getY() - origin.getY()) / (v.dY * speed);
    }
}
