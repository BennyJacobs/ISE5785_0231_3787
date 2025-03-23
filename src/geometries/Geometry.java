package geometries;

import primitives.Point;
import primitives.Vector;

/**
 * Abstract class representing a geometric object.
 */
public abstract class Geometry {
    /**
     * Abstract method to calculate the normal vector to the geometry at a given point.
     *
     * @param point the point on the geometry
     * @return the normal vector at the given point
     */
    public abstract Vector getNormal(Point point);
}
