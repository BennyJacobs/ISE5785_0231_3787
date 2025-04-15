package geometries;

import primitives.Point;
import primitives.Ray;
import primitives.Util;
import primitives.Vector;

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
        Vector tempVector = point.subtract(head);
        double dotProduct = tempVector.dotProduct(direction);
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
        return super.findIntersections(ray);
    }
}
