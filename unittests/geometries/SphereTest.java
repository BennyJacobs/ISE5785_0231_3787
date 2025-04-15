package geometries;

import org.junit.jupiter.api.Test;
import primitives.Point;
import primitives.Ray;
import primitives.Vector;

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

    }
}