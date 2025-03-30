package geometries;

import org.junit.jupiter.api.Test;
import primitives.Point;
import primitives.Vector;

import static org.junit.jupiter.api.Assertions.*;

class SphereTest {
    /**
     * Test method for {@link Sphere#getNormal(Point)}
     */
    @Test
    void getNormal() {
        // ============ Equivalence Partitions Tests ==============
        Sphere sphere = new Sphere(Point.ZERO, 1);
        assertEquals(new Vector(1, 0, 0), sphere.getNormal(new Point(1, 0, 0)), "ERROR: the normal vector isn't orthogonal to tangent at the point");
    }
}