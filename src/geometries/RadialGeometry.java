package geometries;

/**
 * Abstract class representing geometries with a radial characteristic (such as spheres and tubes).
 */
public abstract class RadialGeometry extends Geometry {
    /**
     * The radius of the geometry
     */
    protected final double radius;

    /**
     * Constructor to initialize the radial geometry with a given radius.
     *
     * @param radius the radius of the geometry
     */
    public RadialGeometry(double radius) {
        this.radius = radius;
    }
}