package geometries;

import org.junit.jupiter.api.Test;
import primitives.Point;
import primitives.Vector;

import static org.junit.jupiter.api.Assertions.*;

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
        assertEquals(0, v1.dotProduct(normal), accuracy, "ERROR: getNormal() the normal vector isn't orthogonal to plane vectors");
        assertEquals(0, v2.dotProduct(normal), accuracy, "ERROR: getNormal() the normal vector isn't orthogonal to plane vectors");
        assertEquals(1, normal.length(), accuracy, "ERROR: getNormal() the normal vector isn't normalized");
    }
}