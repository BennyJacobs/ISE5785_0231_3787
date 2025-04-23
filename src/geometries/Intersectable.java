package geometries;

import primitives.Point;
import primitives.Ray;

import java.util.List;

/**
 * Interface for geometric objects that can be intersected by rays.
 * Provides a method to find intersection points between a ray and the geometry.
 */
public interface Intersectable {
    /**
     * Finds the intersection points between a ray and the geometry.
     *
     * @param ray the {@link Ray} to intersect with the geometry
     * @return a list of {@link Point} objects where the ray intersects the geometry,
     * or {@code null} if there are no intersections
     */
    List<Point> findIntersections(Ray ray);
}
