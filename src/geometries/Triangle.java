package geometries;

import primitives.Point;

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
}
