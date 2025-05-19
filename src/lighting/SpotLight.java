package lighting;

import primitives.Color;
import primitives.Point;
import primitives.Vector;

/**
 * Represents a spotlight source, which emits light in a specific direction
 * with a specified narrowness (beam focus).
 */
public class SpotLight extends PointLight {
    /**
     * The direction the spotlight is pointing.
     */
    private final Vector direction;

    /**
     * Factor controlling the narrowness of the beam (higher = more focused beam).
     */
    private int narrowBeam = 1;

    /**
     * Constructs a spotlight with intensity, position, and direction.
     *
     * @param intensity the light intensity
     * @param position the position of the light
     * @param direction the direction the spotlight is pointing
     */
    public SpotLight(Color intensity, Point position, Vector direction) {
        super(intensity, position);
        this.direction = direction.normalize();
    }

    @Override
    public SpotLight setKc(double kC) {
        return (SpotLight) super.setKc(kC);
    }

    @Override
    public SpotLight setKl(double kL) {
        return (SpotLight) super.setKl(kL);
    }

    @Override
    public SpotLight setKq(double kQ) {
        return (SpotLight) super.setKq(kQ);
    }

    @Override
    public Color getIntensity(Point p) {
        return super.getIntensity(p).scale(Math.pow(Math.max(0, direction.dotProduct(getL(p))), narrowBeam));
    }

    /**
     * Sets the narrow beam factor that controls spotlight focus.
     *
     * @param narrowBeam the beam concentration (higher = narrower beam)
     * @return the current {@code SpotLight} instance
     */
    public SpotLight setNarrowBeam(int narrowBeam) {
        this.narrowBeam = narrowBeam;
        return this;
    }
}
