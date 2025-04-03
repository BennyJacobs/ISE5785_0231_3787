package primitives;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for {@link Point} class
 */
class PointTest {
    Point p1 = new Point(1, 2, 3);
    Point p2 = new Point(2, 3, 4);


    /**
     * Test method for {@link Point#subtract(Point)}
     */
    @Test
    void subtract() {
        // ============ Equivalence Partitions Tests ==============

        // Test Case 1 - Regular subtraction between points
        Vector vResult = new Vector(-1, -1, -1);
        assertEquals(vResult, p1.subtract(p2), "ERROR: subtract() wrong result");

        // =============== Boundary Values Tests ==================

        // Test Case 1 - Subtraction of a point from itself
        assertThrows(IllegalArgumentException.class, () -> p1.subtract(p1), "ERROR: subtract() zero vector is not allowed or wrong exception has been thrown");
    }


    /**
     * Test method for {@link Point#add(Vector)}
     */
    @Test
    void add() {
        // ============ Equivalence Partitions Tests ==============

        // Test Case 1 - Regular addition between point and vector
        Vector v2 = new Vector(2, 3, 4);
        Vector v1Opposite = new Vector(-1, -2, -3);
        Point pResult = new Point(3, 5, 7);
        assertEquals(pResult, p1.add(v2), "ERROR: add() wrong result");

        //Test Case 2 - Addition of a point and its opposite vector
        assertEquals(Point.ZERO, p1.add(v1Opposite), "ERROR: add() wrong result");
    }


    /**
     * Test method for {@link Point#distanceSquared(Point)}
     */
    @Test
    void distanceSquared() {
        // ============ Equivalence Partitions Tests ==============

        // Test Case 1 - Calculating distance squared between two points
        double result = 3;
        assertEquals(result, p1.distanceSquared(p2), "ERROR: distanceSquared() wrong result");
        assertEquals(result, p2.distanceSquared(p1), "ERROR: distanceSquared() wrong result");

        // Test Case 2 - Calculating distance squared between a point to itself
        assertEquals(0, p1.distanceSquared(p1), "ERROR: distanceSquared() wrong result when calculating distance from a point to itself");
    }


    /**
     * Test method for {@link Point#distance(Point)}
     */
    @Test
    void distance() {
        // ============ Equivalence Partitions Tests ==============

        // Test Case 1 - Calculating distance between two points
        double result = Math.sqrt(3);
        double accuracy = 0.0001;
        assertEquals(result, p1.distance(p2), accuracy, "ERROR: distance() wrong result");
        assertEquals(result, p2.distance(p1), accuracy, "ERROR: distance() wrong result");

        // Test Case 2 - Calculating distance between a point to itself
        assertEquals(0, p1.distance(p1), "ERROR: distance() wrong result when calculating distance from a point to itself");
    }
}