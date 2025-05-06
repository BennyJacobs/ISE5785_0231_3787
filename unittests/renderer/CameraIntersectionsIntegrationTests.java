package renderer;

import geometries.*;
import org.junit.jupiter.api.Test;
import primitives.*;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Integration tests between {@link renderer.Camera} and {@link geometries.Intersectable} geometries.
 * <p>
 * These tests verify the number of intersection points between rays constructed by a camera
 * and various geometries such as {@link geometries.Sphere}, {@link geometries.Plane}, and {@link geometries.Triangle}.
 * The tests are performed using two cameras and a 3x3 view plane, and the rays constructed
 * from each pixel are checked for intersections.
 */
public class CameraIntersectionsIntegrationTests {

    // Cameras used in the tests
    private final Camera.Builder cameraBuilder = Camera.getBuilder()
            .setVpDistance(1)
            .setDirection(new Vector(0, 0, -1), Vector.AXIS_Y)
            .setVpSize(3, 3)
            .setLocation(Point.ZERO);
    private final int nX = 3;
    private final int nY = 3;
    Camera camera1 = cameraBuilder.build();
    Camera camera2 = cameraBuilder.setLocation(new Point(0, 0, 0.5)).build();

    /**
     * Checks the total number of intersection points between rays constructed by the given camera
     * through each pixel in the camera's view plane and the given geometry.
     * <p>
     * For each pixel, a ray is constructed and tested for intersections
     * with the geometry. The total number of intersection points found is compared to the expected number.
     *
     * @param geometry                   the geometry to test intersections with
     * @param camera                     the camera from which the rays are constructed
     * @param expectedNumOfIntersections the expected total number of intersection points
     */
    private void checkIntersections(Intersectable geometry, Camera camera, int expectedNumOfIntersections) {
        int numOfIntersections = 0;
        for (int i = 0; i < nY; i++) {
            for (int j = 0; j < nX; j++) {
                Ray ray = camera.constructRay(nX, nY, j, i);
                var intersection = geometry.findIntersections(ray);
                if (intersection != null) {
                    numOfIntersections += intersection.size();
                }
            }
        }
        assertEquals(expectedNumOfIntersections, numOfIntersections, "ERROR: wrong number of intersections - expected: " + expectedNumOfIntersections);
    }

    /**
     * Integration tests for {@link geometries.Sphere} with various radius and positions.
     * Tests how many intersection points occur between camera rays and a sphere.
     */
    @Test
    void integrationTestSphere() {
        // Test Case 01 - Sphere with radius = 1, 2 intersections
        Sphere sphere = new Sphere(new Point(0, 0, -3), 1);
        checkIntersections(sphere, camera1, 2);

        // Test Case 02 - Sphere with radius = 2.5, 18 intersections
        sphere = new Sphere(new Point(0, 0, -2.5), 2.5);
        checkIntersections(sphere, camera2, 18);

        // Test Case 03 - Sphere with radius = 2, 10 intersections
        sphere = new Sphere(new Point(0, 0, -2), 2);
        checkIntersections(sphere, camera2, 10);

        // Test Case 04 - Sphere with radius = 4, 9 intersections
        sphere = new Sphere(new Point(0, 0, -1), 4);
        checkIntersections(sphere, camera1, 9);

        // Test Case 05 - Sphere with radius = 0.5, 0 intersections
        sphere = new Sphere(new Point(0, 0, 1), 0.5);
        checkIntersections(sphere, camera1, 0);
    }

    /**
     * Integration tests for {@link geometries.Plane} with different orientations.
     * Tests how many intersection points occur between camera rays and a plane.
     */
    @Test
    void integrationTestPlane() {
        // Test Case 01 - Plane parallel to y-axis, 9 intersections
        Plane plane = new Plane(new Point(0, 0, -3), new Vector(0, 0, 1));
        checkIntersections(plane, camera1, 9);

        // Test Case 02 - Plane not parallel to y-axis, 9 intersections
        plane = new Plane(new Point(-1, -1, -3), new Point(1, -1, -3), new Point(0, 1, -2));
        checkIntersections(plane, camera1, 9);

        // Test Case 03 - Plane doesn't intersect the bottom camera rays, 6 intersections
        plane = new Plane(new Point(-1, -1, -6), new Point(1, -1, -6), new Point(0, 1, -2));
        checkIntersections(plane, camera1, 6);
    }

    /**
     * Integration tests for {@link geometries.Triangle} in front of the camera.
     * Tests how many intersection points occur between camera rays and a triangle.
     */
    @Test
    void integrationTestTriangle() {
        // Test Case 01 - Triangle parallel to y-axis, 1 intersection
        Triangle triangle = new Triangle(new Point(0, 1, -2), new Point(-1, -1, -2), new Point(1, -1, -2));
        checkIntersections(triangle, camera1, 1);

        // Test Case 02 - Triangle parallel to y-axis, 2 intersections
        triangle = new Triangle(new Point(0, 20, -2), new Point(-1, -1, -2), new Point(1, -1, -2));
        checkIntersections(triangle, camera1, 2);
    }
}
