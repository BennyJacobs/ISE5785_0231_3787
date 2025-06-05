package geometries;

import org.junit.jupiter.api.Test;
import primitives.Point;
import primitives.Ray;
import primitives.Vector;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
/**
 * Unit tests for {@link Tube} class
 */
class TubeTest {
    Tube tube = new Tube(1, new Ray(new Point(0, 0, 1), new Vector(0, 0, 1)));

    /**
     * Test method for {@link Tube#getNormal(Point)}
     */
    @Test
    void getNormal() {
        final Tube tube = new Tube(1, new Ray(Point.ZERO, new Vector(1, 0, 0)));

        // ============ Equivalence Partitions Tests ==============
        // Test Case 1 - Checking the normal vector for correctness
        assertEquals(new Vector(0, 1, 0), tube.getNormal(new Point(1, 1, 0)), "ERROR: the normal vector isn't orthogonal to tangent at the point");

        // =============== Boundary Values Tests ==================
        // Test Case 1 - Checking the normal vector for correctness (In case the point is on the same circle as the reference point)
        assertEquals(new Vector(0, 1, 0), tube.getNormal(new Point(0, 1, 0)), "ERROR: the normal vector isn't orthogonal to tangent at the point");
    }

    /**
     * Test method for {@link Tube#findIntersections(Ray)}
     */
    @Test
    void findIntersectionsTest() {
        // ============ Equivalence Partitions Tests ==============
        // Test Case 01 - Ray doesn't intersect the tube (0 points)
        Ray ray = new Ray(new Point(-6, 0, 1), new Vector(6, -4, 3));
        var result = tube.findIntersections(ray);
        assertNull(result, "ERROR: the intersections' array should be null");

        // Test Case 02 - Ray starts before and intersects the tube (but not through the tube's ray) (2 points)
        ray = new Ray(new Point(-6, 0, 1), new Vector(1, -0.1, 0));
        result = tube.findIntersections(ray);
        assertNotNull(result, "ERROR: the intersections' array should not be null");
        assertEquals(2, result.size(), "ERROR: Wrong number of intersections");
        Point intersectionPoint = new Point(-0.857649282009763,-0.514235071799024,1);
        Point intersectionPoint2 = new Point(0.738837400821645, -0.673883740082165, 1);
        var exp = List.of(intersectionPoint, intersectionPoint2);
        assertEquals(exp, result, "ERROR: Wrong intersection point");

        // Test Case 03 - Ray starts inside the tube (1 point)
        ray = new Ray(new Point(-0.5, 0, 0), new Vector(-0.5, 0, 1));
        result = tube.findIntersections(ray);
        assertNotNull(result, "ERROR: the intersections' array should not be null");
        assertEquals(1, result.size(), "ERROR: Wrong number of intersections");
        intersectionPoint = new Point(-1, 0, 1);
        exp = List.of(intersectionPoint);
        assertEquals(exp, result, "ERROR: Wrong intersection point");

        // Test Case 04 - Ray starts after the tube (0 points)
        ray = new Ray(new Point(-6, 0, 1), new Vector(-1, 0.1, 0));
        result = tube.findIntersections(ray);
        assertNull(result, "ERROR: the intersections' array should be null");



        // =============== Boundary Values Tests ==================
        // **** Group 1: Ray starts at tube
        // TC11: Ray starts at tube and intersects the tube (but not through the tube's ray) (1 point)
        ray = new Ray(new Point(0, -1, 1), new Vector(1, 1, 0));
        result = tube.findIntersections(ray);
        assertNotNull(result, "ERROR: the intersections' array should not be null");
        assertEquals(1, result.size(), "ERROR: Wrong number of intersections");
        intersectionPoint = new Point(1, 0, 1);
        exp = List.of(intersectionPoint);
        assertEquals(exp, result, "ERROR: Wrong intersection point");

        // TC12: Ray starts at tube and goes outside (continuation does not cross through the tube's ray) (0 points)
        ray = new Ray(new Point(0, -1, 1), new Vector(-1, -1, 0));
        result = tube.findIntersections(ray);
        assertNull(result, "ERROR: the intersections' array should be null");


        // **** Group 2: Ray (or it's continuation) intersects tube through it's ray (not head)
        // TC21 - Ray starts before and is not orthogonal to tube's ray (2 points)
        ray = new Ray(new Point(-6, 0, 1), new Vector(6, 0, 1));
        result = tube.findIntersections(ray);
        assertNotNull(result, "ERROR: the intersections' array should not be null");
        assertEquals(2, result.size(), "ERROR: Wrong number of intersections");
        intersectionPoint = new Point(-1, 0, 1.833333333333333);
        intersectionPoint2 = new Point(1.000000000000001, 0, 2.166666666666667);
        exp = List.of(intersectionPoint, intersectionPoint2);
        assertEquals(exp, result, "ERROR: Wrong intersection point");

        // TC22 - Ray starts before and is orthogonal to tube's ray (2 points)
        ray = new Ray(new Point(-6, 0, 2), new Vector(6, 0, 0));
        result = tube.findIntersections(ray);
        assertNotNull(result, "ERROR: the intersections' array should not be null");
        assertEquals(2, result.size(), "ERROR: Wrong number of intersections");
        intersectionPoint = new Point(-1, 0, 2);
        intersectionPoint2 = new Point(1, 0, 2);
        exp = List.of(intersectionPoint, intersectionPoint2);
        assertEquals(exp, result, "ERROR: Wrong intersection point");

        // TC23: Ray starts at tube and is not orthogonal to tube's ray (1 point)
        ray = new Ray(new Point(0, -1, 1), new Vector(0, 1, 1));
        result = tube.findIntersections(ray);
        assertNotNull(result, "ERROR: the intersections' array should not be null");
        assertEquals(1, result.size(), "ERROR: Wrong number of intersections");
        intersectionPoint = new Point(0, 1, 3);
        exp = List.of(intersectionPoint);
        assertEquals(exp, result, "ERROR: Wrong intersection point");

        // TC24: Ray starts at tube and is orthogonal to tube's ray (1 point)
        ray = new Ray(new Point(0, -1, 2), new Vector(0, 1, 0));
        result = tube.findIntersections(ray);
        assertNotNull(result, "ERROR: the intersections' array should not be null");
        assertEquals(1, result.size(), "ERROR: Wrong number of intersections");
        intersectionPoint = new Point(0, 1, 2);
        exp = List.of(intersectionPoint);
        assertEquals(exp, result, "ERROR: Wrong intersection point");

        // TC25: Ray starts inside tube and is not orthogonal to tube's ray (1 point)
        ray = new Ray(new Point(-0.5, 0, 0), new Vector(0.5, 0, 2));
        result = tube.findIntersections(ray);
        assertNotNull(result, "ERROR: the intersections' array should not be null");
        assertEquals(1, result.size(), "ERROR: Wrong number of intersections");
        intersectionPoint = new Point(1, 0, 5.999999999999999);
        exp = List.of(intersectionPoint);
        assertEquals(exp, result, "ERROR: Wrong intersection point");

        // TC26: Ray starts inside tube and is orthogonal to tube's ray (1 point)
        ray = new Ray(new Point(-0.5, 0, 2), new Vector(0.5, 0, 0));
        result = tube.findIntersections(ray);
        assertNotNull(result, "ERROR: the intersections' array should not be null");
        assertEquals(1, result.size(), "ERROR: Wrong number of intersections");
        intersectionPoint = new Point(1, 0, 2);
        exp = List.of(intersectionPoint);
        assertEquals(exp, result, "ERROR: Wrong intersection point");

        // TC27: Ray starts after tube and is not orthogonal to tube's ray (0 points)
        ray = new Ray(new Point(-6, 0, 1), new Vector(-6, 0, -1));
        result = tube.findIntersections(ray);
        assertNull(result, "ERROR: the intersections' array should be null");

        // TC28: Ray starts after tube and is orthogonal to tube's ray (0 points)
        ray = new Ray(new Point(-6, 0, 2), new Vector(-6, 0, 0));
        result = tube.findIntersections(ray);
        assertNull(result, "ERROR: the intersections' array should be null");


        // **** Group 3: Ray (or it's continuation) intersects tube through it's head
        // TC31 - Ray starts before and is not orthogonal to tube's ray (2 points)
        ray = new Ray(new Point(-6, 0, 2), new Vector(6, 0, -1));
        result = tube.findIntersections(ray);
        assertNotNull(result, "ERROR: the intersections' array should not be null");
        assertEquals(2, result.size(), "ERROR: Wrong number of intersections");
        intersectionPoint = new Point(-1, 0, 1.166666666666667);
        intersectionPoint2 = new Point(1.000000000000001, 0, 0.833333333333333);
        exp = List.of(intersectionPoint, intersectionPoint2);
        assertEquals(exp, result, "ERROR: Wrong intersection point");

        // TC32 - Ray starts before and is orthogonal to tube's ray (2 points)
        ray = new Ray(new Point(-6, 0, 1), new Vector(6, 0, 0));
        result = tube.findIntersections(ray);
        assertNotNull(result, "ERROR: the intersections' array should not be null");
        assertEquals(2, result.size(), "ERROR: Wrong number of intersections");
        intersectionPoint = new Point(-1, 0, 1);
        intersectionPoint2 = new Point(1, 0, 1);
        exp = List.of(intersectionPoint, intersectionPoint2);
        assertEquals(exp, result, "ERROR: Wrong intersection point");

        // TC33: Ray starts at tube and is not orthogonal to tube's ray (1 point)
        ray = new Ray(new Point(0, -1, 2), new Vector(0, 1, -1));
        result = tube.findIntersections(ray);
        assertNotNull(result, "ERROR: the intersections' array should not be null");
        assertEquals(1, result.size(), "ERROR: Wrong number of intersections");
        intersectionPoint = new Point(0, 1, 0);
        exp = List.of(intersectionPoint);
        assertEquals(exp, result, "ERROR: Wrong intersection point");

        // TC34: Ray starts at tube and is orthogonal to tube's ray (1 point)
        ray = new Ray(new Point(0, -1, 1), new Vector(0, 1, 0));
        result = tube.findIntersections(ray);
        assertNotNull(result, "ERROR: the intersections' array should not be null");
        assertEquals(1, result.size(), "ERROR: Wrong number of intersections");
        intersectionPoint = new Point(0, 1, 1);
        exp = List.of(intersectionPoint);
        assertEquals(exp, result, "ERROR: Wrong intersection point");

        // TC35: Ray starts inside tube and is not orthogonal to tube's ray (1 point)
        ray = new Ray(new Point(-0.5, 0, 0), new Vector(0.5, 0, 1));
        result = tube.findIntersections(ray);
        assertNotNull(result, "ERROR: the intersections' array should not be null");
        assertEquals(1, result.size(), "ERROR: Wrong number of intersections");
        intersectionPoint = new Point(1, 0, 3);
        exp = List.of(intersectionPoint);
        assertEquals(exp, result, "ERROR: Wrong intersection point");

        // TC36: Ray starts inside tube and is orthogonal to tube's ray (1 point)
        ray = new Ray(new Point(-0.5, 0, 1), new Vector(0.5, 0, 0));
        result = tube.findIntersections(ray);
        assertNotNull(result, "ERROR: the intersections' array should not be null");
        assertEquals(1, result.size(), "ERROR: Wrong number of intersections");
        intersectionPoint = new Point(1, 0, 1);
        exp = List.of(intersectionPoint);
        assertEquals(exp, result, "ERROR: Wrong intersection point");

        // TC37: Ray starts inside tube, but after it's head and is not orthogonal to tube's ray (1 point)
        ray = new Ray(new Point(-0.5, 0, 0), new Vector(-0.5, 0, -1));
        result = tube.findIntersections(ray);
        assertNotNull(result, "ERROR: the intersections' array should not be null");
        assertEquals(1, result.size(), "ERROR: Wrong number of intersections");
        intersectionPoint = new Point(-1, 0, -1);
        exp = List.of(intersectionPoint);
        assertEquals(exp, result, "ERROR: Wrong intersection point");

        // TC38: Ray starts inside tube, but after it's head and is orthogonal to tube's ray (1 point)
        ray = new Ray(new Point(-0.5, 0, 1), new Vector(-0.5, 0, 0));
        result = tube.findIntersections(ray);
        assertNotNull(result, "ERROR: the intersections' array should not be null");
        assertEquals(1, result.size(), "ERROR: Wrong number of intersections");
        intersectionPoint = new Point(-1, 0, 1);
        exp = List.of(intersectionPoint);
        assertEquals(exp, result, "ERROR: Wrong intersection point");

        // TC39: Ray starts at tube, but after it's head and is not orthogonal to tube's ray (1 point)
        ray = new Ray(new Point(0, -1, 2), new Vector(0, -1, 1));
        result = tube.findIntersections(ray);
        assertNull(result, "ERROR: the intersections' array should be null");

        // TC3-10: Ray starts at tube, but after it's head and is orthogonal to tube's ray (1 point)
        ray = new Ray(new Point(0, -1, 1), new Vector(0, -1, 0));
        result = tube.findIntersections(ray);
        assertNull(result, "ERROR: the intersections' array should be null");

        // TC3-11: Ray starts after tube and is not orthogonal to tube's ray (0 points)
        ray = new Ray(new Point(-6, 0, 2), new Vector(-6, 0, 1));
        result = tube.findIntersections(ray);
        assertNull(result, "ERROR: the intersections' array should be null");

        // TC3-12: Ray starts after tube and is orthogonal to tube's ray (0 points)
        ray = new Ray(new Point(-6, 0, 1), new Vector(-6, 0, 0));
        result = tube.findIntersections(ray);
        assertNull(result, "ERROR: the intersections' array should be null");

        // TC3-13: Ray starts on tube's head and is not orthogonal to tube's ray (1 point)
        ray = new Ray(new Point(0, 0, 1), new Vector(1, 0, 1));
        result = tube.findIntersections(ray);
        assertNotNull(result, "ERROR: the intersections' array should not be null");
        assertEquals(1, result.size(), "ERROR: Wrong number of intersections");
        intersectionPoint = new Point(1, 0, 2);
        exp = List.of(intersectionPoint);
        assertEquals(exp, result, "ERROR: Wrong intersection point");

        // TC3-14: Ray starts on tube's head and is orthogonal to tube's ray (1 point)
        ray = new Ray(new Point(0, 0, 1), new Vector(1, 0, 0));
        result = tube.findIntersections(ray);
        assertNotNull(result, "ERROR: the intersections' array should not be null");
        assertEquals(1, result.size(), "ERROR: Wrong number of intersections");
        intersectionPoint = new Point(1, 0, 1);
        exp = List.of(intersectionPoint);
        assertEquals(exp, result, "ERROR: Wrong intersection point");



        // **** Group 4: Ray perpendicular to tube's ray
        // TC41: Ray and tube's ray unite in the same direction (not through head) (0 points)
        ray = new Ray(new Point(0, 0, 2), new Vector(0, 0, 1));
        result = tube.findIntersections(ray);
        assertNull(result, "ERROR: the intersections' array should be null");

        // TC42: Ray and tube's ray unite in the same direction (through head) (0 points)
        ray = new Ray(new Point(0, 0, -1), new Vector(0, 0, 1));
        result = tube.findIntersections(ray);
        assertNull(result, "ERROR: the intersections' array should be null");

        // TC43: Ray and tube's ray unite in opposite directions (not through head) (0 points)
        ray = new Ray(new Point(0, 0, -1), new Vector(0, 0, -1));
        result = tube.findIntersections(ray);
        assertNull(result, "ERROR: the intersections' array should be null");

        // TC44: Ray and tube's ray unite in opposite directions (through head) (0 points)
        ray = new Ray(new Point(0, 0, 2), new Vector(0, 0, -1));
        result = tube.findIntersections(ray);
        assertNull(result, "ERROR: the intersections' array should be null");

        // TC45: Ray and tube's ray unite in the same direction (starting at head) (0 points)
        ray = new Ray(new Point(0, 0, 1), new Vector(0, 0, 1));
        result = tube.findIntersections(ray);
        assertNull(result, "ERROR: the intersections' array should be null");

        // TC46: Ray and tube's ray unite in opposite directions (starting at head) (0 points)
        ray = new Ray(new Point(0, 0, 1), new Vector(0, 0, -1));
        result = tube.findIntersections(ray);
        assertNull(result, "ERROR: the intersections' array should be null");

        // TC47: Ray is inside tube, and it is perpendicular to tube's ray in same direction (0 points)
        ray = new Ray(new Point(-0.5, 0, 0), new Vector(0, 0, 1));
        result = tube.findIntersections(ray);
        assertNull(result, "ERROR: the intersections' array should be null");

        // TC48: Ray is inside tube, and it is perpendicular to tube's ray in opposite directions (0 points)
        ray = new Ray(new Point(-0.5, 0, 0), new Vector(0, 0, -1));
        result = tube.findIntersections(ray);
        assertNull(result, "ERROR: the intersections' array should be null");

        // TC49: Ray is on tube's side, and it is perpendicular to tube's ray in same direction (0 points)
        ray = new Ray(new Point(1, 0, 0), new Vector(0, 0, 1));
        result = tube.findIntersections(ray);
        assertNull(result, "ERROR: the intersections' array should be null");

        // TC4-10: Ray is on tube's side, and it is perpendicular to tube's ray in opposite directions (0 points)
        ray = new Ray(new Point(1, 0, 0), new Vector(0, 0, -1));
        result = tube.findIntersections(ray);
        assertNull(result, "ERROR: the intersections' array should be null");

        // TC4-11: Ray is outside of tube, and it is perpendicular to tube's ray in same direction (0 points)
        ray = new Ray(new Point(2, 0, 0), new Vector(0, 0, 1));
        result = tube.findIntersections(ray);
        assertNull(result, "ERROR: the intersections' array should be null");

        // TC4-12: Ray is outside of tube, and it is perpendicular to tube's ray in opposite directions (0 points)
        ray = new Ray(new Point(2, 0, 0), new Vector(0, 0, -1));
        result = tube.findIntersections(ray);
        assertNull(result, "ERROR: the intersections' array should be null");


        // **** Group 5: Ray tangent to tube
        // TC51: Ray starts before tube and is tangent to it's side (0 points)
        ray = new Ray(new Point(-1, -1, 1), new Vector(1, 0, 0));
        result = tube.findIntersections(ray);
        assertNull(result, "ERROR: the intersections' array should be null");

        // TC52: Ray starts on tube and is tangent to it's side (0 points)
        ray = new Ray(new Point(0, -1, 1), new Vector(1, 0, 0));
        result = tube.findIntersections(ray);
        assertNull(result, "ERROR: the intersections' array should be null");

        // TC53: Ray starts after tube, and it's continuation is tangent to tube's side (0 points)
        ray = new Ray(new Point(1, -1, 1), new Vector(1, 0, 0));
        result = tube.findIntersections(ray);
        assertNull(result, "ERROR: the intersections' array should be null");
    }


    /**
     * Test method for {@link Tube#calculateIntersections(Ray, double)}
     */
    @Test
    void calculateIntersections() {
        // A vector used in some test cases to (1,0,0)
        Vector v100 = new Vector(1, 0, 0);

        // ============ Equivalence Partitions Tests ==============
        // Test Case 01 - Ray starts before tube, and maxDistance is smaller than the distance between ray head and tube
        Ray ray = new Ray(new Point(-2, 0, 0), v100);
        assertNull(tube.calculateIntersections(ray, 0.5), "Ray's intersection point is greater than maxDistance");

        // TC02: Ray starts before the tube and ends inside it
        var result = tube.calculateIntersections(ray, 2);
        assertNotNull(result, "ERROR: the intersections' array should not be null");
        assertEquals(1, result.size(), "ERROR: Wrong number of intersections");

        //TC03: Ray starts and ends inside the tube
        ray = new Ray(new Point(-0.5, 0, 0), v100);
        assertNull(tube.calculateIntersections(ray, 1), "ray starts and stops inside the sphere");

        // TC04: Ray starts inside the tube and ends after it
        result = tube.calculateIntersections(ray, 3.5);
        assertNotNull(result, "ERROR: the intersections' array should not be null");
        assertEquals(1, result.size(), "ERROR: Wrong number of intersections");

        // TC05: Ray starts after tube
        ray = new Ray(new Point(2, 0, 0), v100);
        assertNull(tube.calculateIntersections(ray, 3.5), "ray starts after the tube");

        // TC06: Ray crosses the tube, starts before it and ends after it
        ray = new Ray(new Point(-2, 0, 0), v100);
        result = tube.calculateIntersections(ray, 8);
        assertNotNull(result, "ERROR: the intersections' array should not be null");
        assertEquals(2, result.size(), "ERROR: Wrong number of intersections");

        // =============== Boundary Values Tests ==================
        // TC01: Ray starts before the tube and ends at the first intersection point
        result = tube.calculateIntersections(ray, 1);
        assertNull(result, "ERROR: the intersections' array should not be null");

        // TC02: Ray starts before the tube and ends at the second intersection point
        result = tube.calculateIntersections(ray, 3);
        assertNotNull(result, "ERROR: the intersections' array should not be null");
        assertEquals(1, result.size(), "ERROR: Wrong number of intersections");

        // TC03: Ray starts inside the tube and ends at the intersection point
        result = tube.calculateIntersections(new Ray(new Point(0, 0, 0), v100), 1);
        assertNull(result, "ERROR: the intersections' array should not be null");
    }
}