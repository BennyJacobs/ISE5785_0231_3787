package lighting;

import primitives.Color;

/**
 * Represents ambient light in a scene.
 * <p>
 * Ambient light is a non-directional, constant light source that uniformly illuminates all objects in the scene.
 * It simulates indirect light that is scattered and reflected in the environment.
 */
public class AmbientLight extends Light {

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
        super(intensity);
    }
}
