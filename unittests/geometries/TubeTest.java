package geometries;

import org.junit.jupiter.api.Test;
import primitives.Point;
import primitives.Ray;
import primitives.Vector;

import static org.junit.jupiter.api.Assertions.*;

class TubeTest {

    /**
     * Test method for {@link Tube#getNormal(Point)}
     */
    @Test
    void getNormal() {
        Tube tube = new Tube(1, new Ray(Point.ZERO, new Vector(1, 0, 0)));

        // ============ Equivalence Partitions Tests ==============
        assertEquals(new Vector(0, 1, 0), tube.getNormal(new Point(1, 1, 0)), "ERROR: the normal vector isn't orthogonal to tangent at the point");

        // =============== Boundary Values Tests ==================
        assertEquals(new Vector(0, 1, 0), tube.getNormal(new Point(0, 1, 0)), "ERROR: the normal vector isn't orthogonal to tangent at the point");

    }
}