package lighting;

import primitives.Color;
import primitives.Vector;
import primitives.Point;

/**
 * Represents a point light source that emits light equally in all directions
 * from a specific position in space. Includes attenuation factors.
 */
public class PointLight extends Light implements LightSource {
    /**
     * The position of the light source.
     */
    private final Point position;

    /**
     * Constant attenuation factor (kC).
     */
    private double kC = 1;

    /**
     * Linear attenuation factor (kL).
     */
    private double kL = 0;

    /**
     * Quadratic attenuation factor (kQ).
     */
    private double kQ = 0;


    /**
     * Constructs a point light with specified intensity and position.
     *
     * @param intensity the light intensity
     * @param position the position of the light source
     */
    public PointLight(Color intensity, Point position) {
        super(intensity);
        this.position = position;
    }

    /**
     * Sets the constant attenuation factor.
     *
     * @param kC the constant attenuation coefficient
     * @return the current {@code PointLight} instance
     */
    public PointLight setKc(double kC) {
        this.kC = kC;
        return this;
    }

    /**
     * Sets the linear attenuation factor.
     *
     * @param l the linear attenuation coefficient
     * @return the current {@code PointLight} instance
     */
    public PointLight setKl(double l) {
        this.kL = l;
        return this;
    }

    /**
     * Sets the quadratic attenuation factor.
     *
     * @param q the quadratic attenuation coefficient
     * @return the current {@code PointLight} instance
     */
    public PointLight setKq(double q) {
        this.kQ = q;
        return this;
    }

    @Override
    public Color getIntensity(Point p) {
        return intensity.scale(1 / (kC + kL * (position.distance(p)) + kQ * (position.distanceSquared(p))));
    }

    @Override
    public Vector getL(Point p) {
        Vector l = p.subtract(position);
        return l.normalize();
    }
}
