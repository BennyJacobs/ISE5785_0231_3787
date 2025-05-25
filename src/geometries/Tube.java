package geometries;

import primitives.Point;
import primitives.Ray;
import primitives.Util;
import primitives.Vector;

import java.util.LinkedList;
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
    public List<Intersection> calculateIntersectionsHelper(Ray ray, double maxDistance) {
        Point rayHead = ray.getHead();
        Point tubeHead = axis.getHead();

        Vector rayDirection = ray.getDirection();
        Vector axisDirection = axis.getDirection();

        double dirV = rayDirection.dotProduct(axisDirection);

        if (rayHead.equals(tubeHead)) {
            if (Util.isZero(dirV))
                return Util.alignZero(radius - maxDistance) < 0 ? List.of(new Intersection(this, ray.getPoint(radius))) : null;

            if (rayDirection.equals(axisDirection.scale(dirV)))
                return null;
            double t = radius / rayDirection.subtract(axisDirection.scale(dirV)).length();
            return Util.alignZero(t - maxDistance) < 0 ? List.of(new Intersection(this, ray.getPoint(t))) : null;
        }

        Vector deltaP = ray.getHead().subtract(tubeHead);
        double dpV = deltaP.dotProduct(axisDirection);

        double a = 1 - dirV * dirV;
        double b = 2 * (rayDirection.dotProduct(deltaP) - dirV * dpV);
        double c = deltaP.lengthSquared() - dpV * dpV - radius * radius;

        if (Util.isZero(a)) {
            if (Util.isZero(b))
                return null;

            return Util.alignZero(-c / b - maxDistance) < 0 ? List.of(new Intersection(this, ray.getPoint(-c / b))) : null;
        }

        double discriminant = Util.alignZero(b * b - 4 * a * c);

        if (discriminant < 0)
            return null;

        double t1 = Util.alignZero(-(b + Math.sqrt(discriminant)) / (2 * a));
        double t2 = Util.alignZero(-(b - Math.sqrt(discriminant)) / (2 * a));

        if (discriminant <= 0)
            return null;

        List<Intersection> intersections = null;

        if (t1 > 0 && Util.alignZero(t1 - maxDistance) < 0) {
            Point point = ray.getPoint(t1);
            if (!point.equals(rayHead)) {
                intersections = new LinkedList<>();
                intersections.add(new Intersection(this, point));
            }
        }
        if (t2 > 0 && Util.alignZero(t2 - maxDistance) < 0) {
            Point point = ray.getPoint(t2);
            if (!point.equals(rayHead)) {
                if (intersections == null)
                    intersections = new LinkedList<>();
                intersections.add(new Intersection(this, ray.getPoint(t2)));
            }
        }

        return intersections;
    }
}
