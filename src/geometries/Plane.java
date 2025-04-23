package geometries;

import primitives.Point;
import primitives.Ray;
import primitives.Util;
import primitives.Vector;

import java.util.List;

/**
 * Class representing a plane in 3D space.
 */
public class Plane extends Geometry {
    /**
     * A reference point on the plane
     */
    private final Point q;
    /**
     * The normal vector of the plane
     */
    private final Vector normal;

    /**
     * Constructor to create a plane using a point and a normal vector.
     *
     * @param q      a point on the plane
     * @param normal the normal vector of the plane
     */
    public Plane(Point q, Vector normal) {
        this.q = q;
        this.normal = normal.normalize();
    }

    /**
     * Constructor to create a plane using three points.
     *
     * @param point1 the first point defining the plane
     * @param point2 the second point defining the plane
     * @param point3 the third point defining the plane
     */
    public Plane(Point point1, Point point2, Point point3) {
        this.q = point1;
        Vector v1 = point1.subtract(point2);
        Vector v2 = point2.subtract(point3);
        Vector normal = v1.crossProduct(v2);
        this.normal = normal.normalize();
    }

    @Override
    public Vector getNormal(Point point) {
        return this.normal;
    }

    @Override
    public List<Point> findIntersections(Ray ray) {
        Point head = ray.getHead();
        if (q.equals(head))
            return null;
        Vector u = q.subtract(head);
        double nominator = u.dotProduct(normal);
        double denominator = normal.dotProduct(ray.getDirection());
        if (Util.isZero(denominator))
            return null;
        double t = Util.alignZero(nominator / denominator);
        if (t > 0)
            return List.of(ray.getPoint(t));
        return null;
    }
}
