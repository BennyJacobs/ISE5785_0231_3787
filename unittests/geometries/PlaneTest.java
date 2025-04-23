package geometries;

import org.junit.jupiter.api.Test;
import primitives.Ray;
import primitives.Vector;
import primitives.Point;


import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for {@link Plane} class
 */
class PlaneTest {
    Point point1 = new Point(1, 6, 3);
    Point point2 = new Point(4, 5, 2);
    Point point3 = new Point(3, 6, 9);
    Vector v1 = point1.subtract(point2);
    Vector v2 = point3.subtract(point2);
    Plane plane = new Plane(point1, point2, point3);

    /**
     * Test method for {@link Plane#Plane(Point, Point, Point)}
     */
    @Test
    void constructorTest() {
        Point point4 = new Point(2, 12, 6);
        Point point5 = new Point(3, 18, 9);

        // ============ Equivalence Partitions Tests ==============
        // Test Case 1 - Checking the normal vector for correctness
        getNormalTest();

        // =============== Boundary Values Tests ==================
        // Test Case 1 - Checking the correctness of the constructor, in cases of identical points or on the same line
        assertThrows(IllegalArgumentException.class, () -> new Plane(point1, point1, point2), "ERROR: constructor should throw an exception or wrong exception has been thrown");
        assertThrows(IllegalArgumentException.class, () -> new Plane(point1, point2, point1), "ERROR: constructor should throw an exception or wrong exception has been thrown");
        assertThrows(IllegalArgumentException.class, () -> new Plane(point2, point1, point1), "ERROR: constructor should throw an exception or wrong exception has been thrown");
        assertThrows(IllegalArgumentException.class, () -> new Plane(point1, point1, point1), "ERROR: constructor should throw an exception or wrong exception has been thrown");
        assertThrows(IllegalArgumentException.class, () -> new Plane(point1, point4, point5), "ERROR: constructor should throw an exception or wrong exception has been thrown");
    }

    /**
     * Test method for {@link Plane#getNormal(Point)}
     */
    @Test
    void getNormalTest() {
        double accuracy = 0.00001;
        Vector normal = plane.getNormal(v1);
        // ============ Equivalence Partitions Tests ==============
        // Test Case 1 - Checking the normal vector for correctness
        assertEquals(0, v1.dotProduct(normal), accuracy, "ERROR: the normal vector isn't orthogonal to plane vectors");
        assertEquals(0, v2.dotProduct(normal), accuracy, "ERROR: the normal vector isn't orthogonal to plane vectors");
        assertEquals(1, normal.length(), accuracy, "ERROR: the normal vector isn't normalized");
    }

    /**
     * Test method for {@link Plane#findIntersections(Ray)}
     */
    @Test
    void findIntersectionsTest() {
        // ============ Equivalence Partitions Tests ==============
        // Test Case 01 - Ray intersects the plane
        Ray crossRay = new Ray(new Point(-4, 4, 0), new Vector(-5, 5, -2));
        var result = plane.findIntersections(crossRay);
        assertNotNull(result, "ERROR: the intersections' array should not be null");
        assertEquals(1, result.size(), "ERROR: Wrong number of intersections");
        Point intersectionPoint = new Point(-8.324324324324323, 8.324324324324323, -1.72972972972973);
        var exp = List.of(intersectionPoint);
        assertEquals(exp, result, "ERROR: Wrong intersection point");

        // Test Case 02 - Ray doesn't intersect the plane
        Ray notCrossRay = new Ray(new Point(-4, 4, 0), new Vector(5, -5, 2));
        result = plane.findIntersections(notCrossRay);
        assertNull(result, "ERROR: the intersections' array should be null");

        // =============== Boundary Values Tests ==================
        // **** Group 1: Ray is parallel to the plane
        // Test Case 11 - Ray is parallel to the plane
        Ray parallelRay = new Ray(new Point(-4, 4, 0), new Vector(3, -1, -1));
        result = plane.findIntersections(parallelRay);
        assertNull(result, "ERROR: the intersections' array should be null");

        // Test Case 12 - Ray is included in the plane
        Ray includedRay = new Ray(new Point(4, 5, 2), new Vector(3, -1, -1));
        result = plane.findIntersections(includedRay);
        assertNull(result, "ERROR: the intersections' array should be null");

        // **** Group 2: Ray is orthogonal to the plane
        // Test Case 21 - Ray is orthogonal to the plane (before)
        Ray orthogonalBefore = new Ray(new Point(-2, 8, 0), new Vector(-6, -20, 2));
        result = plane.findIntersections(orthogonalBefore);
        assertNotNull(result, "ERROR: the intersections' array should not be null");
        assertEquals(1, result.size(), "ERROR: Wrong number of intersections");
        intersectionPoint = new Point(-2.381818181818182, 6.727272727272728, 0.12727272727272727);
        exp = List.of(intersectionPoint);
        assertEquals(exp, result, "ERROR: Wrong intersection point");

        // Test Case 22 - Ray is orthogonal to the plane (in)
        Ray orthogonalIn = new Ray(new Point(4, 5, 2), new Vector(-6, -20, 2));
        result = plane.findIntersections(orthogonalIn);
        assertNull(result, "ERROR: the intersections' array should be null");

        // Test Case 23 - Ray is orthogonal to the plane (after)
        Ray orthogonalAfter = new Ray(new Point(-4, 2, 0), new Vector(-6, -20, 2));
        result = plane.findIntersections(orthogonalAfter);
        assertNull(result, "ERROR: the intersections' array should be null");

        // **** Group 3: Ray is neither orthogonal nor parallel to and begins at the plane
        // Test Case 31 - Ray intersects the plane (Begins at the plane)
        Ray beginsAtThePlane = new Ray(new Point(4, 5, 2), new Vector(-5, 5, -2));
        result = plane.findIntersections(beginsAtThePlane);
        assertNull(result, "ERROR: the intersections' array should be null");

        // Test Case 32 - Ray intersects the plane (Begins at the plane - Q)
        Ray beginsAtThePlaneQ = new Ray(new Point(1, 6, 3), new Vector(-5, 5, -2));
        result = plane.findIntersections(beginsAtThePlaneQ);
        assertNull(result, "ERROR: the intersections' array should be null");

    }
}