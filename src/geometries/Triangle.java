package geometries;

import primitives.Point;
import primitives.Ray;
import primitives.Vector;

import java.util.List;

import static primitives.Util.alignZero;

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
    public List<Intersection> calculateIntersectionsHelper(Ray ray) {
        var intersections = plane.findIntersections(ray);
        if (intersections == null || intersections.getFirst().equals(vertices.get(0)))
            return null;
        
        Point intersect = intersections.getFirst();

        Vector v1 = vertices.get(2).subtract(vertices.get(0));
        Vector v2 = vertices.get(1).subtract(vertices.get(0));
        Vector v3 = intersect.subtract(vertices.get(0));

        double dot00 = v1.dotProduct(v1);
        double dot01 = v1.dotProduct(v2);
        double dot02 = v1.dotProduct(v3);
        double dot11 = v2.dotProduct(v2);
        double dot12 = v2.dotProduct(v3);

        double denominator = dot00 * dot11 - dot01 * dot01;

        double u = alignZero((dot11 * dot02 - dot01 * dot12) / denominator);
        double v = alignZero((dot00 * dot12 - dot01 * dot02) / denominator);
        
        if ((u > 0) && (v > 0) && (u + v < 1))
            return List.of(new Intersection(this, intersect));

        return null;
    }
}
