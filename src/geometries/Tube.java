package geometries;

import primitives.Point;
import primitives.Ray;
import primitives.Util;
import primitives.Vector;

import java.util.List;

/**
 * Class representing a tube in 3D space.
 */
public class Tube extends RadialGeometry {
    /**
     * The central axis of the tube represented as a ray
     */
    protected final Ray axis;

    /**
     * Constructor to create a tube with a given radius and axis.
     *
     * @param radius the radius of the tube
     * @param axis   the axis of the tube
     */
    public Tube(double radius, Ray axis) {
        super(radius);
        this.axis = axis;
    }

    @Override
    public Vector getNormal(Point point) {
        Vector u = point.subtract(axis.getHead());
        double dotProduct = u.dotProduct(axis.getDirection());
        Point o = axis.getPoint(dotProduct);
        Vector normal = point.subtract(o);
        return normal.normalize();
    }

    @Override
    public List<Point> findIntersections(Ray ray) {
        return null;
    }
}
