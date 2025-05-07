package geometries;

import primitives.Point;
import primitives.Ray;
import primitives.Util;
import primitives.Vector;

import java.util.ArrayList;
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
     * Constructor to create a cylinder with a given radius, axis, and height.
     *
     * @param radius the radius of the cylinder
     * @param axis   the axis of the cylinder
     * @param height the height of the cylinder
     */
    public Cylinder(double radius, Ray axis, double height) {
        super(radius, axis);
        this.height = height;
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
        if (Util.isZero(height-dotProduct)) {
            return direction;
        }
        return super.getNormal(point);
    }

    @Override
    public List<Point> findIntersections(Ray ray) {
        Point baseCenter = axis.getHead();
        List<Point> intersections = new ArrayList<>();
        List<Point> list = super.findIntersections(ray);
        if(list != null)
            for (Point point : list) {
                double distance = Util.alignZero(point.subtract(baseCenter).dotProduct(axis.getDirection()));
                if (distance > 0 && Util.alignZero(distance - height) < 0)
                    intersections.add(point);
            }

        // Check intersection with bottom base
        Circle bottomBase = new Circle(baseCenter, radius, getNormal(baseCenter));
        list = bottomBase.findIntersections(ray);
        if(list != null)
            intersections.add(list.getFirst());

        // Check intersection with top base
        Point topCenter = baseCenter.add(axis.getDirection().scale(height));
        Circle topBase = new Circle(topCenter, radius, getNormal(topCenter));
        list = topBase.findIntersections(ray);
        if(list != null)
            intersections.add(list.getFirst());

        if (intersections.isEmpty())
                return null;
        return intersections;
    }
}
