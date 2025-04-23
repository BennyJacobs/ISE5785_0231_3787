package geometries;

import org.junit.jupiter.api.Test;
import primitives.Point;
import primitives.Ray;
import primitives.Vector;

import static org.junit.jupiter.api.Assertions.*;
/**
 * Unit tests for {@link Geometries} class
 */
class GeometriesTest {
    /**
     * Test method for {@link Geometries#findIntersections(Ray)}
     */
    @Test
    void findIntersections() {
        Cylinder cylinder = new Cylinder(0.5, new Ray(new Point(3,0,0), new Vector(0,0,1)), 3);
        Plane plane = new Plane(new Point(5,0,0), new Point(5,1,0), new Point(5,0,1));
        Polygon polygon = new Polygon(new Point(-3,-1,0), new Point(-3,1,0), new Point(-3,1,2), new Point(-3,-1,2));
        Triangle triangle = new Triangle(new Point(-4,-1,0), new Point(-4,1,0), new Point(-4,1,2));
        Sphere sphere = new Sphere(new Point(-1,0,0),0.5);
        Tube tube = new Tube(1, new Ray(new Point(1,0,0), new Vector(0,0,1)));
        Geometries geometries = new Geometries(cylinder, plane, polygon, triangle, sphere, tube);

        // ============ Equivalence Partitions Tests ==============
        // Test Case 01 - Some shapes (but not all) are intersected
        Ray intersectRay = new Ray(new Point(-5,0.7,1), new Vector(1,0,0));
        var intersections = geometries.findIntersections(intersectRay);
        assertNotNull(intersections, "Intersections should not be null");
        assertEquals(3, intersections.size(), "Wrong number of intersections");

        // =============== Boundary Values Tests ==================
        // Test Case 01 - Empty geometries collection
        Geometries emptyGeometries = new Geometries();
        Ray testRay = new Ray(new Point(-5,0,1), new Vector(1,0,0));
        intersections = emptyGeometries.findIntersections(testRay);
        assertNull(intersections, "Intersections should be null");

        // Test Case 02 - No geometry is intersected
        Ray noIntersectRay = new Ray(new Point(-5,0,1), new Vector(0,0,1));
        intersections = geometries.findIntersections(noIntersectRay);
        assertNull(intersections, "Intersections should be null");

        // Test Case 03 - only one geometry is intersected
        intersectRay = new Ray(new Point(-5,-4,1), new Vector(1,0,0));
        intersections = geometries.findIntersections(intersectRay);
        assertNotNull(intersections, "Intersections should not be null");
        assertEquals(1, intersections.size(), "Wrong number of intersections");

        // Test Case 04 - All the geometries are intersected
        intersectRay = new Ray(new Point(-5,0,0.25), new Vector(1,0,0));
        intersections = geometries.findIntersections(intersectRay);
        assertNotNull(intersections, "Intersections should not be null");
        assertEquals(5, intersections.size(), "Wrong number of intersections");

    }
}