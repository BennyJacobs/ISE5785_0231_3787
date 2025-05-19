package lighting;

import primitives.Color;

/**
 * Abstract base class for all light sources in the rendering engine.
 * <p>
 * A light is defined by its {@code intensity}—a {@link Color} that encodes
 * both the spectral hue and the power (brightness) of the light it emits.
 * Concrete subclasses (e.g.&nbsp;{@code PointLight}, {@code DirectionalLight}, {@code SpotLight})
 * add spatial information such as position, direction, or attenuation.
 * </p>
 */
abstract class Light {

    /** The RGB intensity (color × strength) of the light source. */
    protected final Color intensity;

    /**
     * Creates a new light with the specified intensity.
     *
     * @param intensity the RGB intensity of the light; must not be {@code null}
     */
    protected Light(Color intensity) {
        this.intensity = intensity;
    }

    /**
     * Returns the intensity of the light source.
     *
     * @return the light's {@link Color} intensity
     */
    public Color getIntensity() {
        return intensity;
    }
}
