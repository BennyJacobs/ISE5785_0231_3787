package lighting;

import primitives.*;

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
     * Represents the radius of the light source.
     */
    private double radius = 0.0;

    /**
     * Represents the number of rays used for the super sampling algorithm in
     */
    private int numOfRays = 1;

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

    /**
     * Sets the radius of the light source.
     *
     * @param radius the radius of the light source
     * @return the current {@code PointLight} instance
     * @throws IllegalArgumentException if radius is negative
     */
    public PointLight setRadius(double radius) {
        if (Util.alignZero(radius) < 0)
            throw new IllegalArgumentException("Radius must not be negative");
        this.radius = radius;
        return this;
    }

    /**
     * Sets the number of super sampling rays for soft-shadows algorithm
     *
     * @param numOfRays the distance value
     * @return the current {@code PointLight} instance
     * @throws IllegalArgumentException if numOfRays is smaller than 1
     */
    public PointLight setNumOfRays(int numOfRays) {
        if (numOfRays < 1)
            throw new IllegalArgumentException("Number of super sampling rays must not be smaller than 1");
        this.numOfRays = numOfRays;
        return this;
    }

    /**
     * Gets the radius of the light source.
     *
     * @return the radius of the light source
     */
    public double getRadius() {
        return this.radius;
    }

    /**
     * Gets the number of super sampling rays for soft-shadows algorithm
     *
     * @return the number of super sampling rays for soft-shadows algorithm
     */
    public int getNumOfRays() {
        return this.numOfRays;
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

    @Override
    public double getDistance(Point p) {
        return position.distance(p);
    }
}
