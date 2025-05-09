package primitives;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for {@link Ray} class
 */
class RayTest {
    /**
     * Test method for {@link Ray#getPoint(double)}
     */
    @Test
    void getPoint() {
        Ray ray = new Ray(new Point(0, 3, 0), new Vector(1, 0, 0));
        // ============ Equivalence Partitions Tests ==============
        // Test Case 01 - positive t
        assertEquals(new Point(5, 3, 0), ray.getPoint(5), "ERROR: Ray.getPoint() returns wrong point");
        // Test Case 02 - Negative t
        assertEquals(new Point(-5, 3, 0), ray.getPoint(-5), "ERROR: Ray.getPoint() returns wrong point");
        // =============== Boundary Values Tests ==================
        // Test Case 01 - t = 0
        assertEquals(new Point(0, 3, 0), ray.getPoint(0), "ERROR: Ray.getPoint() returns wrong point");
    }

    /**
     * Test method for {@link Ray#findClosestPoint(List)}
     */
    @Test
    void findClosestPoint() {
        final Ray ray = new Ray(new Point(0, 3, 0), new Vector(1, 0, 0));
        final Point point1 = new Point(3, 3, 0);
        final Point point2 = new Point(1, 3, 0);
        final Point point3 = new Point(4, 3, 0);

        // ============ Equivalence Partitions Tests ==============
        // Test Case 01 - closest point in the middle of the list
        List<Point> points = new LinkedList<>(List.of(point1, point2, point3));
        assertEquals(point2, ray.findClosestPoint(points), "ERROR: Ray.findClosestPoint() returns wrong point");

        // =============== Boundary Values Tests ==================
        // Test Case 01 - empty list
        assertNull(ray.findClosestPoint(null), "ERROR: Ray.findClosestPoint() should return null");
        // Test Case 02 - closest point at the first place of the list
        points.clear();
        points.addAll(List.of(point2, point1, point3));
        assertEquals(point2, ray.findClosestPoint(points), "ERROR: Ray.findClosestPoint() returns wrong point");
        // Test Case 03 - closest point at the last place of the list
        points.clear();
        points.addAll(List.of(point1, point3, point2));
        assertEquals(point2, ray.findClosestPoint(points), "ERROR: Ray.findClosestPoint() returns wrong point");
    }
}