package geometries;

import org.junit.jupiter.api.Test;
import primitives.Point;
import primitives.Ray;
import primitives.Vector;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for {@link Triangle} class
 */
class TriangleTest {
    /**
     * Test method for {@link Triangle#getNormal(Point)}
     */
    @Test
    void getNormal() {
        Point p1 = new Point(1, 1, 1);
        Point p2 = new Point(3, 6, 9);
        Point p3 = new Point(4, 3, 6);
        Triangle triangle = new Triangle(p1, p2, p3);
        Vector v1 = p1.subtract(p2);
        Vector v2 = p3.subtract(p2);
        double accuracy = 0.00001;
        Vector normal = triangle.getNormal(v1);
        // ============ Equivalence Partitions Tests ==============
        // Test Case 1 - Checking the normal vector for correctness
        assertEquals(0, v1.dotProduct(normal), accuracy, "ERROR: getNormal() the normal vector isn't orthogonal to plane vectors");
        assertEquals(0, v2.dotProduct(normal), accuracy, "ERROR: getNormal() the normal vector isn't orthogonal to plane vectors");
        assertEquals(1, normal.length(), accuracy, "ERROR: getNormal() the normal vector isn't normalized");
    }

    /**
     * Test method for {@link Triangle#findIntersections(Ray)}
     */
    @Test
    void findIntersectionsTest() {

        final Triangle triangle = new Triangle(new Point(1, 0, 0), new Point(0, 1, 0), new Point(0, 0, 1));
        final Point rayPoint = new Point(2, 1, 2);

        // ============ Equivalence Partitions Tests ==============
        // TC01: Ray's line is outside the triangle, against edge(0 points)
        assertNull(triangle.findIntersections(new Ray(rayPoint, new Vector(-1, -2, -2))), "Ray's line out of triangle, against edge");

        // TC02: Ray's line is outside the triangle, against vertex (0 points)
        assertNull(triangle.findIntersections(new Ray(rayPoint, new Vector(1, -2, -2))), "Ray's line out of triangle, against vertex");

        // TC03: Ray's line crosses the triangle (1 point)
        Ray ray = new Ray(rayPoint, new Vector(-2, -0.5, -2));
        final var exp = List.of(new Point(0.222222222222222, 0.555555555555556, 0.222222222222222));
        final var result1 = triangle.findIntersections(ray);
        assertNotNull(result1, "Can't be empty list");
        assertEquals(1, result1.size(), "Wrong number of points");
        assertEquals(exp, result1, "Ray crosses triangle");

        // =============== Boundary Values Tests ==================
        // TC01: Ray's line is on the triangle's edge(0 points)
        assertNull(triangle.findIntersections(new Ray(rayPoint, new Vector(-1.5, -1, -1.5))), "Ray's line on triangle's edge");

        // TC02: Ray's line is on the triangle's vertex (0 points)
        assertNull(triangle.findIntersections(new Ray(rayPoint, new Vector(-1, -1, -2))), "Ray's line on triangle's vertex");

        // TC03: Ray's line is on the triangle's edge continuation (0 points)
        assertNull(triangle.findIntersections(new Ray(rayPoint, new Vector(-7, -1, 4))), "Ray's line on triangle's edge continuation");
    }

    /**
     * Test method for {@link Triangle#calculateIntersections(Ray, double)}
     */
    @Test
    void calculateIntersections() {
        // ============ Equivalence Partitions Tests ==============
        final Triangle triangle = new Triangle(new Point(1, 0, 0), new Point(0, 1, 0), new Point(0, 0, 0));

        // Test Case 01 - Ray starts before triangle, and maxDistance is smaller than the distance between ray head and triangle
        Ray ray = new Ray(new Point(0.3, 0.3, 2), new Vector(0, 0, -1));
        assertNull(triangle.calculateIntersections(ray, 1),
                "Ray's intersection point is greater than maxDistance");

        // Test Case 02 - Ray starts before triangle, and maxDistance is greater than the distance between ray head and triangle
        var result = triangle.calculateIntersections(ray, 3);
        assertNotNull(result, "ERROR: the intersections' array should not be null");
        assertEquals(1, result.size(), "ERROR: Wrong number of intersections");

        // Test Case 03 - Ray starts after triangle
        ray = new Ray(new Point(0.3, 0.3, -1), new Vector(0, 0, -1));
        assertNull(triangle.calculateIntersections(ray, 1),
                "ERROR: Wrong number of intersections");

        // =============== Boundary Values Tests ==================
        // Test Case 01 - Ray ends at triangle
        ray = new Ray(new Point(0.3, 0.3, 1), new Vector(0, 0, -1));
        assertNull(triangle.calculateIntersections(ray, 1),
                "ERROR: Wrong number of intersections");
    }
}