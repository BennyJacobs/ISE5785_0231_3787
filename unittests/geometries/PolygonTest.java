package geometries;

import static org.junit.jupiter.api.Assertions.*;


import org.junit.jupiter.api.Test;


import primitives.*;

import java.util.List;

/**
 * Testing Polygons
 * @author Dan
 */
class PolygonTests {
    /**
     * Delta value for accuracy when comparing the numbers of type 'double' in
     * assertEquals
     */
    private static final double DELTA = 0.000001;

    /** Test method for {@link geometries.Polygon#Polygon(primitives.Point...)}. */
    @Test
    void testConstructor() {
        // ============ Equivalence Partitions Tests ==============

        // TC01: Correct concave quadrangular with vertices in correct order
        assertDoesNotThrow(() -> new Polygon(new Point(0, 0, 1),
                        new Point(1, 0, 0),
                        new Point(0, 1, 0),
                        new Point(-1, 1, 1)),
                "Failed constructing a correct polygon");

        // TC02: Wrong vertices order
        assertThrows(IllegalArgumentException.class, //
                () -> new Polygon(new Point(0, 0, 1), new Point(0, 1, 0), new Point(1, 0, 0), new Point(-1, 1, 1)), //
                "Constructed a polygon with wrong order of vertices");

        // TC03: Not in the same plane
        assertThrows(IllegalArgumentException.class, //
                () -> new Polygon(new Point(0, 0, 1), new Point(1, 0, 0), new Point(0, 1, 0), new Point(0, 2, 2)), //
                "Constructed a polygon with vertices that are not in the same plane");

        // TC04: Concave quadrangular
        assertThrows(IllegalArgumentException.class, //
                () -> new Polygon(new Point(0, 0, 1), new Point(1, 0, 0), new Point(0, 1, 0),
                        new Point(0.5, 0.25, 0.5)), //
                "Constructed a concave polygon");

        // =============== Boundary Values Tests ==================

        // TC10: Vertex on a side of a quadrangular
        assertThrows(IllegalArgumentException.class, //
                () -> new Polygon(new Point(0, 0, 1), new Point(1, 0, 0), new Point(0, 1, 0),
                        new Point(0, 0.5, 0.5)),
                "Constructed a polygon with vertix on a side");

        // TC11: Last point = first point
        assertThrows(IllegalArgumentException.class, //
                () -> new Polygon(new Point(0, 0, 1), new Point(1, 0, 0), new Point(0, 1, 0), new Point(0, 0, 1)),
                "Constructed a polygon with vertice on a side");

        // TC12: Co-located points
        assertThrows(IllegalArgumentException.class, //
                () -> new Polygon(new Point(0, 0, 1), new Point(1, 0, 0), new Point(0, 1, 0), new Point(0, 1, 0)),
                "Constructed a polygon with vertice on a side");

    }

    /** Test method for {@link geometries.Polygon#getNormal(primitives.Point)}. */
    @Test
    void testGetNormal() {
        // ============ Equivalence Partitions Tests ==============
        // TC01: There is a simple single test here - using a quad
        Point[] pts =
                { new Point(0, 0, 1), new Point(1, 0, 0), new Point(0, 1, 0), new Point(-1, 1, 1) };
        Polygon pol = new Polygon(pts);
        // ensure there are no exceptions
        assertDoesNotThrow(() -> pol.getNormal(new Point(0, 0, 1)), "");
        // generate the test result
        Vector result = pol.getNormal(new Point(0, 0, 1));
        // ensure |result| = 1
        assertEquals(1, result.length(), DELTA, "Polygon's normal is not a unit vector");
        // ensure the result is orthogonal to all the edges
        for (int i = 0; i < 3; ++i)
            assertEquals(0d, result.dotProduct(pts[i].subtract(pts[i == 0 ? 3 : i - 1])), DELTA,
                    "Polygon's normal is not orthogonal to one of the edges");
    }

    /**
     * Test method for {@link Polygon#findIntersections(Ray)}.
     */
    @Test
    public void FindIntersections() {
        final Polygon polygon = new Polygon(new Point(1, 0, 0), new Point(-1, 0, 0), new Point(-1, 2, 0), new Point(1, 2, 0));
        final Point rayPoint = new Point(0.5, 0.5, 2);

        // ============ Equivalence Partitions Tests ==============
        // TC01: Ray's line is outside the polygon, against edge(0 points)
        assertNull(polygon.findIntersections(new Ray(rayPoint, new Vector(-0.5, -2.5, -3))), "Ray's line out of polygon, against edge");

        // TC02: Ray's line is outside the polygon, against vertex (0 points)
        assertNull(polygon.findIntersections(new Ray(rayPoint, new Vector(3.5, -2.5, -3))), "Ray's line out of polygon, against vertex");

        // TC03: Ray's line crosses the polygon (1 point)
        Ray ray = new Ray(rayPoint, new Vector(-0.5, 0.5, -2));
        final var exp = List.of(new Point(0, 1, 0));
        final var result1 = polygon.findIntersections(ray);
        assertNotNull(result1, "Can't be empty list");
        assertEquals(1, result1.size(), "Wrong number of points");
        assertEquals(exp, result1, "Ray crosses polygon");

        // =============== Boundary Values Tests ==================
        // TC01: Ray's line is on the polygon's edge(0 points)
        assertNull(polygon.findIntersections(new Ray(rayPoint, new Vector(0.5, 0.5, -2))), "Ray's line on polygon's edge");

        // TC02: Ray's line is on the polygon's vertex (0 points)
        assertNull(polygon.findIntersections(new Ray(rayPoint, new Vector(0.5, 0.5, -2))), "Ray's line on polygon's vertex");

        // TC03: Ray's line is on the polygon's edge continuation (0 points)
        assertNull(polygon.findIntersections(new Ray(rayPoint, new Vector(1.5, -0.5, -2))), "Ray's line on polygon's edge continuation");
    }

    /**
     * Test method for {@link Polygon#calculateIntersections(Ray, double)}
     */
    @Test
    void calculateIntersections() {
        final Polygon polygon = new Polygon(new Point(1, 0, 0), new Point(-1, 0, 0), new Point(-1, 2, 0), new Point(1, 2, 0));

        // ============ Equivalence Partitions Tests ==============
        // Test Case 01 - Ray starts before polygon, and maxDistance is smaller than the distance between ray head and polygon
        Ray ray = new Ray(new Point(0, 1, 2), new Vector(0, 0, -1));
        assertNull(polygon.calculateIntersections(ray, 1),
                "Ray's intersection point is greater than maxDistance");

        // Test Case 02 - Ray starts before polygon, and maxDistance is greater than the distance between ray head and polygon
        var result = polygon.calculateIntersections(ray, 3);
        assertNotNull(result, "ERROR: the intersections' array should not be null");
        assertEquals(1, result.size(), "ERROR: Wrong number of intersections");

        // Test Case 03 - Ray starts after polygon
        ray = new Ray(new Point(0, 1, -1), new Vector(0, 0, -1));
        assertNull(polygon.calculateIntersections(ray, 1),
                "ERROR: Wrong number of intersections");

        // =============== Boundary Values Tests ==================
        // Test Case 01 - Ray ends at polygon
        ray = new Ray(new Point(0, 1, 2), new Vector(0, 0, -1));
        assertNull(polygon.calculateIntersections(ray, 2),
                "ERROR: Wrong number of intersections");
    }
}