package geometries;

import org.junit.jupiter.api.Test;
import primitives.Point;
import primitives.Ray;
import primitives.Vector;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for {@link Sphere} class
 */
class SphereTest {
    /**
     * Test method for {@link Sphere#getNormal(Point)}
     */
    @Test
    void getNormal() {
        // ============ Equivalence Partitions Tests ==============
        // Test Case 1 - Checking the normal vector for correctness
        Sphere sphere = new Sphere(Point.ZERO, 1);
        assertEquals(new Vector(1, 0, 0), sphere.getNormal(new Point(1, 0, 0)), "ERROR: the normal vector isn't orthogonal to tangent at the point");
    }

    /**
     * Test method for {@link Sphere#findIntersections(Ray)}
     */
    @Test
    void findIntersectionsTest() {
        Sphere sphere = new Sphere(new Point(1, 2, 3), 2);
        // ============ Equivalence Partitions Tests ==============
        // Test Case 01 - Ray doesn't intersect the sphere
        Ray notIntersect = new Ray(new Point(-2, 3, 3), new Vector(-2, -2, 0));
        var result = sphere.findIntersections(notIntersect);
        assertNull(result, "ERROR: the intersections' array should be null");

        // Test Case 02 - Ray starts before and crosses the sphere
        Ray intersect = new Ray(new Point(4, 4, 3), new Vector(-2, -2, 0));
        result = sphere.findIntersections(intersect);
        assertNotNull(result, "ERROR: the intersections' array should not be null");
        assertEquals(2, result.size(), "ERROR: Wrong number of intersections");
        Point intersectionPoint = new Point(2.822875655532295, 2.822875655532295, 3);
        Point intersectionPoint2 = new Point(0.177124344467706, 0.177124344467706, 3);
        var exp = List.of(intersectionPoint, intersectionPoint2);
        assertEquals(exp, result, "ERROR: Wrong intersection point");

        // Test Case 03 - Ray starts inside the sphere
        intersect = new Ray(new Point(2, 2, 3), new Vector(-2, -2, 0));
        result = sphere.findIntersections(intersect);
        assertNotNull(result, "ERROR: the intersections' array should not be null");
        assertEquals(1, result.size(), "ERROR: Wrong number of intersections");
        intersectionPoint = new Point(0.177124344467706, 0.177124344467706, 3);
        exp = List.of(intersectionPoint);
        assertEquals(exp, result, "ERROR: Wrong intersection point");

        // Test Case 04 - Ray starts after the sphere
        notIntersect = new Ray(new Point(-1, -1, 3), new Vector(-2, -2, 0));
        result = sphere.findIntersections(notIntersect);
        assertNull(result, "ERROR: the intersections' array should be null");

        // =============== Boundary Values Tests ==================
        // **** Group 1: Ray's line crosses the sphere (but not the center)
        // TC11: Ray starts at sphere and goes inside (1 point)
        intersect = new Ray(new Point(-1, 2, 3), new Vector(2, 2, 0));
        result = sphere.findIntersections(intersect);
        assertNotNull(result, "ERROR: the intersections' array should not be null");
        assertEquals(1, result.size(), "ERROR: Wrong number of intersections");
        intersectionPoint = new Point(1, 4, 3);
        exp = List.of(intersectionPoint);
        assertEquals(exp, result, "ERROR: Wrong intersection point");

        // TC12: Ray starts at sphere and goes outside (0 points)
        intersect = new Ray(new Point(-1, 2, 3), new Vector(-2, -2, 0));
        result = sphere.findIntersections(intersect);
        assertNull(result, "ERROR: the intersections' array should be null");

        // **** Group 2: Ray's line goes through the center
        // TC21: Ray starts before the sphere (2 points)
        intersect = new Ray(new Point(-1, 0, 3), new Vector(2, 2, 0));
        result = sphere.findIntersections(intersect);
        assertNotNull(result, "ERROR: the intersections' array should not be null");
        assertEquals(2, result.size(), "ERROR: Wrong number of intersections");
        intersectionPoint = new Point(-0.414213562373095, 0.585786437626905, 3);
        intersectionPoint2 = new Point(2.414213562373095, 3.414213562373095, 3);
        exp = List.of(intersectionPoint, intersectionPoint2);
        assertEquals(exp, result, "ERROR: Wrong intersection point");

        // TC22: Ray starts at sphere and goes inside (1 point)
        intersect = new Ray(new Point(1, 0, 3), new Vector(0, 1, 0));
        result = sphere.findIntersections(intersect);
        assertNotNull(result, "ERROR: the intersections' array should not be null");
        assertEquals(1, result.size(), "ERROR: Wrong number of intersections");
        intersectionPoint = new Point(1, 4, 3);
        exp = List.of(intersectionPoint);
        assertEquals(exp, result, "ERROR: Wrong intersection point");

        // TC23: Ray starts inside (1 point)
        intersect = new Ray(new Point(1, 3, 3), new Vector(0, 1, 0));
        result = sphere.findIntersections(intersect);
        assertNotNull(result, "ERROR: the intersections' array should not be null");
        assertEquals(1, result.size(), "ERROR: Wrong number of intersections");
        intersectionPoint = new Point(1, 4, 3);
        exp = List.of(intersectionPoint);
        assertEquals(exp, result, "ERROR: Wrong intersection point");

        // TC24: Ray starts at the center (1 point)
        intersect = new Ray(new Point(1, 2, 3), new Vector(0, 1, 0));
        result = sphere.findIntersections(intersect);
        assertNotNull(result, "ERROR: the intersections' array should not be null");
        assertEquals(1, result.size(), "ERROR: Wrong number of intersections");
        intersectionPoint = new Point(1, 4, 3);
        exp = List.of(intersectionPoint);
        assertEquals(exp, result, "ERROR: Wrong intersection point");

        // TC25: Ray starts at sphere and goes outside (0 points)
        intersect = new Ray(new Point(1, 4, 3), new Vector(0, 1, 0));
        result = sphere.findIntersections(intersect);
        assertNull(result, "ERROR: the intersections' array should be null");

        // TC26: Ray starts after sphere (0 points)
        intersect = new Ray(new Point(1, 5, 3), new Vector(0, 1, 0));
        result = sphere.findIntersections(intersect);
        assertNull(result, "ERROR: the intersections' array should be null");

        // **** Group 3: Ray's line is tangent to the sphere (all tests 0 points)
        // TC31: Ray starts before the tangent point
        intersect = new Ray(new Point(3, 1, 3), new Vector(0, 1, 0));
        result = sphere.findIntersections(intersect);
        assertNull(result, "ERROR: the intersections' array should be null");

        // TC32: Ray starts at the tangent point
        intersect = new Ray(new Point(3, 2, 3), new Vector(0, 1, 0));
        result = sphere.findIntersections(intersect);
        assertNull(result, "ERROR: the intersections' array should be null");

        // TC33: Ray starts after the tangent point
        intersect = new Ray(new Point(3, 3, 3), new Vector(0, 1, 0));
        result = sphere.findIntersections(intersect);
        assertNull(result, "ERROR: the intersections' array should be null");

        // **** Group 4: Special cases
        // TC41: Ray's line is outside sphere, ray is orthogonal to ray start to sphere's center line
        intersect = new Ray(new Point(4, 2, 3), new Vector(0, 1, 0));
        result = sphere.findIntersections(intersect);
        assertNull(result, "ERROR: the intersections' array should be null");

        // TC42: Ray's starts inside, ray is orthogonal to ray start to sphere's center line
        intersect = new Ray(new Point(2, 2, 3), new Vector(0, 1, 0));
        result = sphere.findIntersections(intersect);
        assertNotNull(result, "ERROR: the intersections' array should not be null");
        assertEquals(1, result.size(), "ERROR: Wrong number of intersections");
        intersectionPoint = new Point(2, 3.732050807568878, 3);
        exp = List.of(intersectionPoint);
        assertEquals(exp, result, "ERROR: Wrong intersection point");

    }
}