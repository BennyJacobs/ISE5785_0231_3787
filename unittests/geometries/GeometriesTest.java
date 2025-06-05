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
        assertEquals(5, intersections.size(), "Wrong number of intersections");

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
        assertEquals(9, intersections.size(), "Wrong number of intersections");
    }


    /**
     * Test method for {@link Geometries#calculateIntersections(Ray, double)}.
     */
    @Test
    void testCalculateIntersections() {
        // The axis vector of ray to (0,0,1)
        final Vector v001 = new Vector(0, 0, 1);
        // A ray for test
        final Ray ray = new Ray(new Point(1, 1, 1), v001);

        // A polygon used in some test cases - 1 intersection with ray
        final Polygon polygon = new Polygon(
                new Point(0, 2, 1),
                new Point(2, 2, 1),
                new Point(2, -1, 2),
                new Point(0, -1, 2)
        );
        // A triangle used in some test cases - 1 intersection with ray
        final Triangle triangle = new Triangle(
                new Point(0, 2, 2),
                new Point(2, 2, 2),
                new Point(0, -1, 4)
        );

        // A sphere used in some test cases - 1 intersection1 with ray
        final Plane plane = new Plane(new Point(-1, 3, 3), v001);
        // A sphere used in some test cases - 2 intersections with ray
        final Sphere sphere = new Sphere(new Point(1, 1, 4), 1);
        // A circle used in some test cases - 1 intersection with ray
        final Circle circle = new Circle(new Point(1, 3, 9), 3, v001);
        // A tube used in some test cases - 1 intersection with ray
        final Tube tube = new Tube(2, new Ray(new Point(1,1,0),new Vector(0,1,0)));
        // A cylinder used in some test cases - 2 intersections with ray
        final Cylinder cylinder = new Cylinder(2, new Ray(new Point(1,1,6),v001), 6);

        Geometries geometries = new Geometries(plane, polygon, triangle, circle, sphere, tube, cylinder);

        // ============ Equivalence Partitions Tests ==============
        // TC01: Some intersections within range and some not
        var result = geometries.calculateIntersections(ray, 1.8);
        assertNotNull(result, "ERROR: the intersections' array should not be null");
        assertEquals(3, result.size(), "ERROR: Wrong number of intersections");

        // =============== Boundary Values Tests ==================
        // TC01: No intersections within range at all
        assertNull(geometries.calculateIntersections(ray, 0.3),
                "ERROR: the intersections' array should be null");

        // TC02: Only one intersection within range
        result = geometries.calculateIntersections(ray, 0.7);
        assertNotNull(result, "ERROR: the intersections' array should not be null");
        assertEquals(1, result.size(), "ERROR: Wrong number of intersections");

        // TC03: All intersections within range
        result = geometries.calculateIntersections(ray, 12);
        assertNotNull(result, "ERROR: the intersections' array should not be null");
        assertEquals(9, result.size(), "ERROR: Wrong number of intersections");

        // TC04: Ray ends at some intersections
        result = geometries.calculateIntersections(ray, 2);
        assertNotNull(result, "ERROR: the intersections' array should not be null");
        assertEquals(3, result.size(), "ERROR: the intersections' array should not be null");
    }
}