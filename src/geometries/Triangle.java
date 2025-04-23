package geometries;

import primitives.Point;
import primitives.Ray;
import primitives.Util;
import primitives.Vector;

import java.util.List;

/**
 * Class representing a triangle in 3D space.
 */
public class Triangle extends Polygon {
    /**
     * Constructor to create a triangle using three points.
     *
     * @param point1 the first vertex of the triangle
     * @param point2 the second vertex of the triangle
     * @param point3 the third vertex of the triangle
     */
    public Triangle(Point point1, Point point2, Point point3) {
        super(point1, point2, point3);
    }

    @Override
    public List<Point> findIntersections(Ray ray) {
        var intersections = plane.findIntersections(ray);
        if (intersections == null)
            return null;
        Point rayHead = ray.getHead();
        Vector rayDirection = ray.getDirection();

        Vector v1 = vertices.get(0).subtract(rayHead);
        Vector v2 = vertices.get(1).subtract(rayHead);
        Vector v3 = vertices.get(2).subtract(rayHead);

        Vector n1 = v1.crossProduct(v2).normalize();
        Vector n2 = v2.crossProduct(v3).normalize();
        Vector n3 = v3.crossProduct(v1).normalize();

        double s1 = Util.alignZero(rayDirection.dotProduct(n1));
        double s2 = Util.alignZero(rayDirection.dotProduct(n2));
        double s3 = Util.alignZero(rayDirection.dotProduct(n3));

        // If all dot products have the same sign, the point is inside
        if ((s1 > 0 && s2 > 0 && s3 > 0) || (s1 < 0 && s2 < 0 && s3 < 0)) {
            return intersections;
        }

        return null;

    }
}
