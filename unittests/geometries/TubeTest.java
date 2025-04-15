package geometries;

import org.junit.jupiter.api.Test;
import primitives.Point;
import primitives.Ray;
import primitives.Vector;

import static org.junit.jupiter.api.Assertions.*;
/**
 * Unit tests for {@link Tube} class
 */
class TubeTest {

    /**
     * Test method for {@link Tube#getNormal(Point)}
     */
    @Test
    void getNormal() {
        Tube tube = new Tube(1, new Ray(Point.ZERO, new Vector(1, 0, 0)));

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

    }
}