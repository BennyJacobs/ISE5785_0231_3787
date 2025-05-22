package geometries;

import primitives.Point;
import primitives.Ray;
import primitives.Util;
import primitives.Vector;

import java.util.List;


/**
 * Class representing a circle in 3D space.
 */
public class Circle extends RadialGeometry {
    /**
     * The center point of the circle
     */
    private final Point center;

    /**
     * Associated plane in which the circle lays
     */
    protected final Plane plane;


    /**
     * Constructor to create a circle with a given center and radius.
     *
     * @param center the center point of the circle
     * @param radius the radius of the circle
     * @param normal the normal to the circle's plane
     */
    public Circle(Point center, double radius, Vector normal) {
        super(radius);
        this.center = center;
        this.plane = new Plane(center, normal);
    }

    @Override
    public Vector getNormal(Point point) { return plane.getNormal(point); }

    @Override
    public List<Intersection> calculateIntersectionsHelper(Ray ray) {
        List<Point> intersections = plane.findIntersections(ray);
        if (intersections != null && Util.alignZero(center.distance(intersections.getFirst()) - radius) < 0)
            return List.of(new Intersection(this, intersections.getFirst()));
        return null;
    }
}
