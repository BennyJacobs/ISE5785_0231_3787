package geometries;

import org.junit.jupiter.api.Test;
import primitives.Point;
import primitives.Ray;
import primitives.Vector;

import static org.junit.jupiter.api.Assertions.*;
/**
 * Unit tests for {@link Cylinder} class
 */
class CylinderTest {
    /**
     * Test method for {@link Cylinder#getNormal(Point)}
     */
    @Test
    void getNormal() {
        Cylinder cylinder = new Cylinder(1, new Ray(Point.ZERO, new Vector(1, 0, 0)), 4);

        // ============ Equivalence Partitions Tests ==============
        // Test Case 1 - Normal on the round surface
        assertEquals(new Vector(0, 1, 0), cylinder.getNormal(new Point(1, 1, 0)),
                "ERROR: The normal vector on the curved surface is incorrect. Expected a vector perpendicular to the tube's tangent at this point.");

        // Test Case 2 - Normal on the bottom base
        assertEquals(new Vector(-1, 0, 0), cylinder.getNormal(new Point(0, 0.5, 0.5)),
                "ERROR: The normal vector on the bottom base is incorrect. Expected an outward-facing normal perpendicular to the base.");

        // Test Case 3 - Normal on the top base
        assertEquals(new Vector(1, 0, 0), cylinder.getNormal(new Point(4, 0.5, 0.5)),
                "ERROR: The normal vector on the top base is incorrect. Expected an outward-facing normal perpendicular to the base.");


        // =============== Boundary Values Tests ==================
        // Test Case 1 - Normal at the center of the bottom base
        assertEquals(new Vector(-1, 0, 0), cylinder.getNormal(new Point(0, 0, 0)),
                "ERROR: The normal vector at the center of the bottom base is incorrect. Expected the normal to be perpendicular to the base and directed outward.");

        // Test Case 2 - Normal at the center of the top base
        assertEquals(new Vector(1, 0, 0), cylinder.getNormal(new Point(4, 0, 0)),
                "ERROR: The normal vector at the center of the top base is incorrect. Expected the normal to be perpendicular to the base and directed outward.");

        // Test Case 3 - Normal at the edge of the bottom base
        assertEquals(new Vector(-1, 0, 0), cylinder.getNormal(new Point(0, 1, 0)),
                "ERROR: The normal vector at the edge of the bottom base is incorrect. Expected the normal to be consistent with the base's orientation.");

        // Test Case 4 - Normal at the edge of the top base
        assertEquals(new Vector(1, 0, 0), cylinder.getNormal(new Point(4, 1, 0)),
                "ERROR: The normal vector at the edge of the top base is incorrect. Expected the normal to be consistent with the base's orientation.");
    }

}