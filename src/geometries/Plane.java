package geometries;

import primitives.Point;
import primitives.Vector;

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
        normal = null;
    }

    @Override
    public Vector getNormal(Point point) {
        return this.normal;
    }
}
