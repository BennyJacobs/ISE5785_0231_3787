package geometries;

import org.junit.jupiter.api.Test;
import primitives.Vector;
import primitives.Point;


import static org.junit.jupiter.api.Assertions.*;

class PlaneTest {
    Point point1 = new Point(1, 6, 3);
    Point point2 = new Point(4, 5, 2);

    /**
     * Test method for {@link Plane#Plane(Point, Point, Point)}
     */
    @Test
    void constructorTest() {
        Point point4 = new Point(2, 12, 6);
        Point point5 = new Point(3, 18, 9);

        // ============ Equivalence Partitions Tests ==============
        getNormalTest();

        // =============== Boundary Values Tests ==================
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
        Point point3 = new Point(3, 6, 9);
        Vector v1 = point1.subtract(point2);
        Vector v2 = point3.subtract(point2);
        Plane plane = new Plane(point1, point2, point3);
        double accuracy = 0.00001;
        Vector normal = plane.getNormal(v1);
        // ============ Equivalence Partitions Tests ==============
        assertEquals(0, v1.dotProduct(normal), accuracy, "ERROR: the normal vector isn't orthogonal to plane vectors");
        assertEquals(0, v2.dotProduct(normal), accuracy, "ERROR: the normal vector isn't orthogonal to plane vectors");
        assertEquals(1, normal.length(), accuracy, "ERROR: the normal vector isn't normalized");
    }
}