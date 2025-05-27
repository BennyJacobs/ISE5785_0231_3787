package geometries;

import primitives.Point;
import primitives.Ray;
import primitives.Util;
import primitives.Vector;

import java.util.LinkedList;
import java.util.List;

/**
 * Class representing a cylinder in 3D space.
 */
public class Cylinder extends Tube {
    /**
     * The height of the cylinder
     */
    private final double height;

    /**
     * The bottom base of the cylinder, represented as a circle.
     */
    private final Circle bottomBase;

    /**
     * The top base of the cylinder, represented as a circle.
     */
    private final Circle topBase;

    /**
     * Constructs a finite cylinder with a given radius, axis, and height.
     *
     * @param radius the radius of the cylinder
     * @param axis   the central axis of the cylinder (defined by a ray)
     * @param height the height of the cylinder
     */
    public Cylinder(double radius, Ray axis, double height) {
        super(radius, axis);
        this.height = height;
        Point baseCenter = axis.getHead();
        bottomBase = new Circle(baseCenter, radius, getNormal(baseCenter));
        Point topCenter = baseCenter.add(axis.getDirection().scale(height));
        topBase = new Circle(topCenter, radius, getNormal(topCenter));
    }

    @Override
    public Vector getNormal(Point point) {
        Point head = axis.getHead();
        Vector direction = axis.getDirection();
        if (point.equals(head)) {
            return direction.scale(-1);
        }
        Vector u = point.subtract(head);
        double dotProduct = u.dotProduct(direction);
        if (Util.isZero(dotProduct)) {
            return direction.scale(-1);
        }
        if (Util.isZero(height - dotProduct)) {
            return direction;
        }
        return super.getNormal(point);
    }

    @Override
    public List<Intersection> calculateIntersectionsHelper(Ray ray, double maxDistance) {
        Point baseCenter = axis.getHead();
        List<Intersection> intersections = null;
        var list = super.calculateIntersectionsHelper(ray, maxDistance);
        if (list != null)
            for (Intersection intersection : list) {
                double distance = Util.alignZero(intersection.point.subtract(baseCenter).dotProduct(axis.getDirection()));
                if (distance > 0 && Util.alignZero(distance - height) < 0) {
                    if (intersections == null)
                        intersections = new LinkedList<>();
                    intersections.add(new Intersection(this, intersection.point));
                }
            }

        // Check intersection with the bottom base
        intersections = getIntersections(ray, bottomBase, intersections, maxDistance);

        // Check intersection with top base
        intersections = getIntersections(ray, topBase, intersections, maxDistance);

        return intersections;
    }

    /**
     * A helper method to calculate intersections between a ray and a circular base of the cylinder.
     *
     * @param ray            the ray to intersect
     * @param circle         the circular base (either bottom or top)
     * @param intersections  the existing list of intersections
     * @param maxDistance the maximum allowed distance from the ray's origin to an intersection point
     * @return an updated list of intersections including any new intersection with the given circle
     */
    private List<Intersection> getIntersections(Ray ray, Circle circle, List<Intersection> intersections, double maxDistance) {
        var list = circle.calculateIntersections(ray, maxDistance);
        if (list != null) {
            if (intersections == null)
                intersections = new LinkedList<>();
            intersections.add(new Intersection(this, list.getFirst().point));
        }
        return intersections;
    }
}
