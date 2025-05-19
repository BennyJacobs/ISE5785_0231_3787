package lighting;

import primitives.Color;
import primitives.Point;
import primitives.Vector;

/**
 * Represents a directional light source that has constant direction and intensity,
 * like sunlight. The light is assumed to come from infinity.
 */
public class DirectionalLight extends Light implements LightSource {
    /**
     * The direction of the light rays.
     */
    private final Vector direction;

    /**
     * Constructs a directional light with specified intensity and direction.
     *
     * @param intensity the light intensity
     * @param direction the direction vector of the light
     */
    public DirectionalLight(Color intensity, Vector direction) {
        super(intensity);
        this.direction = direction.normalize();
    }

    @Override
    public Color getIntensity(Point p) {
        return intensity;
    }

    @Override
    public Vector getL(Point p) {
        return direction;
    }
}
