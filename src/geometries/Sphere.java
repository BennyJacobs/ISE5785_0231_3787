package geometries;

import primitives.Point;
import primitives.Ray;
import primitives.Util;
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
    public List<Intersection> calculateIntersectionsHelper(Ray ray, double maxDistance) {
        Point head = ray.getHead();
        if (center.equals(head))
            return Util.alignZero(radius - maxDistance) < 0 ? List.of(new Intersection(this, ray.getPoint(radius))) : null;
        Vector u = center.subtract(head);
        double tm = u.dotProduct(ray.getDirection());
        double d = Math.sqrt(u.lengthSquared() - tm * tm);
        if (Util.alignZero(d - radius) >= 0)
            return null;
        double th = Math.sqrt(radius * radius - d * d);
        double t1 = Util.alignZero(tm - th);
        double t2 = Util.alignZero(tm + th);
        if (t1 > 0 && t2 > 0 && Util.alignZero(t1 - maxDistance) < 0 && Util.alignZero(t2 - maxDistance) < 0)
            return List.of(new Intersection(this, ray.getPoint(t1)), new Intersection(this, ray.getPoint(t2)));
        else if (t1 > 0 && Util.alignZero(t1 - maxDistance) < 0)
            return List.of(new Intersection(this, ray.getPoint(t1)));
        else if (t2 > 0 && Util.alignZero(t2 - maxDistance) < 0)
            return List.of(new Intersection(this, ray.getPoint(t2)));
        else
            return null;
    }
}
