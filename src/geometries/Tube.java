package geometries;

import primitives.Point;
import primitives.Ray;
import primitives.Vector;

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
        Point head = axis.getHead();
        Vector direction = axis.getDirection();
        Vector tempVector = point.subtract(head);
        double dotProduct = tempVector.dotProduct(direction);
        Vector normal;
        if (dotProduct != 0) {
            head = head.add(direction.scale(dotProduct));
        }
        normal = point.subtract(head);
        return normal.normalize();
    }
}
