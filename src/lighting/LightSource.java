package lighting;

import primitives.Color;
import primitives.Vector;
import primitives.Point;

/**
 * Interface representing a light source in the scene.
 * Provides methods to get light intensity and direction at a given point.
 */
public interface LightSource {
    /**
     * Returns the light intensity at a specific point.
     *
     * @param p the point where the intensity is evaluated
     * @return the light intensity as a {@link Color}
     */
    Color getIntensity(Point p);

    /**
     * Returns the direction vector from the light source to the given point.
     *
     * @param p the target point
     * @return normalized direction vector pointing toward the light
     */
    Vector getL(Point p);
}
