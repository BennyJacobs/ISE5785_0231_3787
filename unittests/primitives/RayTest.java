package primitives;

import org.junit.jupiter.api.Test;

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
        assertEquals(new Point(5,3,0), ray.getPoint(5),"ERROR: Ray.getPoint() returns wrong point");
        // Test Case 02 - Negative t
        assertEquals(new Point(-5,3,0), ray.getPoint(-5),"ERROR: Ray.getPoint() returns wrong point");
        // =============== Boundary Values Tests ==================
        // Test Case 01 - t = 0
        assertEquals(new Point(0,3,0), ray.getPoint(0),"ERROR: Ray.getPoint() returns wrong point");
    }
}