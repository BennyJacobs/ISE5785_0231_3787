package geometries;

import org.junit.jupiter.api.Test;
import primitives.Point;
import primitives.Ray;
import primitives.Vector;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for {@link Circle} class
 */
class CircleTest {
    final Circle circle = new Circle(new Point(0, 0, 1), 1, new Vector(0, 0, 1));

    /**
     * Test method for {@link Circle#getNormal(Point)}
     */
    @Test
    void getNormal() {
        Point p1 = new Point(0, 0, 1);
        Point p2 = new Point(1, 0, 1);
        Point p3 = new Point(0, 1, 1);

        Vector v1 = p1.subtract(p2);
        Vector v2 = p3.subtract(p2);
        double accuracy = 0.00001;

        Vector normal = circle.getNormal(p1);

        // ============ Equivalence Partitions Tests ==============
        // Test Case 1 - Checking the normal vector for correctness
        assertEquals(0, v1.dotProduct(normal), accuracy, "ERROR: getNormal() the normal vector isn't orthogonal to plane vectors");
        assertEquals(0, v2.dotProduct(normal), accuracy, "ERROR: getNormal() the normal vector isn't orthogonal to plane vectors");
        assertEquals(1, normal.length(), accuracy, "ERROR: getNormal() the normal vector isn't normalized");
    }

    /**
     * Test method for {@link Circle#findIntersections(Ray)}
     */
    @Test
    void findIntersections() {

        Point rayHead = new Point(0, 0, 2);

        // ============ Equivalence Partitions Tests ==============
        // Test Case 01 - Ray intersects the circle
        Ray ray = new Ray(rayHead, new Vector(0.5, 0.5, -1));
        List<Point> result = circle.findIntersections(ray);
        assertNotNull(result, "ERROR: the intersections' array should not be null");
        assertEquals(1, result.size(), "ERROR: Wrong number of intersections");
        Point intersectionPoint = new Point(0.5,0.5,1);
        var exp = List.of(intersectionPoint);
        assertEquals(exp, result, "ERROR: Wrong intersection point");

        // Test Case 02 - Ray doesn't intersect the circle
        ray = new Ray(rayHead, new Vector(-0.5, -0.5, 1));
        result = circle.findIntersections(ray);
        assertNull(result, "ERROR: the intersections' array should be null");

        // =============== Boundary Values Tests ==================
        // Test Case 01 - Ray intersects the center of the circle
        ray = new Ray(rayHead, new Vector(0, 0, -1));
        result = circle.findIntersections(ray);
        assertNotNull(result, "ERROR: the intersections' array should not be null");
        assertEquals(1, result.size(), "ERROR: Wrong number of intersections");
        intersectionPoint = new Point(0,0,1);
        exp = List.of(intersectionPoint);
        assertEquals(exp, result, "ERROR: Wrong intersection point");

        // Test Case 02 - Ray intersects the circle's edge
        ray = new Ray(rayHead, new Vector(1, 0, -1));
        result = circle.findIntersections(ray);
        assertNull(result, "ERROR: the intersections' array should be null");
    }
}