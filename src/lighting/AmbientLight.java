package lighting;

import primitives.Color;

/**
 * Represents ambient light in a scene.
 * <p>
 * Ambient light is a non-directional, constant light source that uniformly illuminates all objects in the scene.
 * It simulates indirect light that is scattered and reflected in the environment.
 */
public class AmbientLight {

    /**
     * The color intensity of the ambient light.
     */
    private final Color intensity;

    /**
     * A constant representing no ambient light (intensity of black).
     */
    public static final AmbientLight NONE = new AmbientLight(Color.BLACK);

    /**
     * Constructs an ambient light source with the specified intensity.
     *
     * @param intensity the color intensity of the ambient light
     */
    public AmbientLight(Color intensity) {
        this.intensity = intensity;
    }

    /**
     * Returns the intensity of the ambient light.
     *
     * @return the {@link Color} representing the ambient light intensity
     */
    public Color getIntensity() {
        return intensity;
    }
}
