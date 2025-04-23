package geometries;

import primitives.Point;
import primitives.Ray;

import java.util.LinkedList;
import java.util.List;

/**
 * A container class for multiple geometric objects that implement {@link Intersectable}.
 * Allows treating a collection of geometries as a single unit for intersection tests.
 */
public class Geometries implements Intersectable{
    /**
     * Internal list holding the geometries in the collection.
     */
    private final List<Intersectable> geometries = new LinkedList<>();

    /**
     * Default constructor creating an empty collection of geometries.
     */
    public Geometries() {}

    /**
     * Constructs a collection containing the specified geometries.
     *
     * @param geometries the geometries to add to the collection
     */
    public Geometries(Intersectable... geometries) {
        add(geometries);
    }

    /**
     * Adds one or more geometries to the collection.
     *
     * @param geometries the geometries to add
     */
    public void add(Intersectable... geometries) {
        this.geometries.addAll(List.of(geometries));
    }

    @Override
    public List<Point> findIntersections(Ray ray) {
        List<Point> allIntersections = null;
        for (Intersectable geometry : geometries) {
            List<Point> intersections = geometry.findIntersections(ray);
            if (intersections != null)
                if (allIntersections == null)
                    allIntersections = new LinkedList<>(intersections);
                else
                    allIntersections.addAll(intersections);
        }
        return allIntersections;
    }
}
