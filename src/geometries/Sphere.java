package geometries;

import primitives.Point;
import primitives.Ray;
import primitives.Vector;

import java.util.List;

/**
 * Class representing a sphere in 3D space.
 */
public class Sphere extends RadialGeometry {
    /**
     * The center point of the sphere
     */
    private final Point center;

    /**
     * Constructor to create a sphere with a given center and radius.
     *
     * @param center the center point of the sphere
     * @param radius the radius of the sphere
     */
    public Sphere(Point center, double radius) {
        super(radius);
        this.center = center;
    }

    @Override
    public Vector getNormal(Point point) {
        Vector normal = point.subtract(center);
        return normal.normalize();
    }

    @Override
    public List<Point> findIntersections(Ray ray) {
        return null;
    }
}
