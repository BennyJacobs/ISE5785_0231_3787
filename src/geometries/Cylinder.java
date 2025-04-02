package geometries;

import primitives.Point;
import primitives.Ray;
import primitives.Vector;

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
        Vector tempVector = point.subtract(head);
        double dotProduct = tempVector.dotProduct(direction);
        Vector normal;
        if (dotProduct > 0 &&  dotProduct < height) {
            Point projection = head.add(direction.scale(dotProduct));
            normal = point.subtract(projection);
        } else if (dotProduct == height) {
            normal = direction;
        } else {
            normal = direction.scale(-1);
        }

        return normal.normalize();
    }
}
