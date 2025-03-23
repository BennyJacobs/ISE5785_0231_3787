package primitives;

/**
 * Represents a point in 3D space.
 * A point is defined by its three coordinates (x, y, z).
 */
public class Point {
    /**
     * The three-dimensional coordinates of the point
     */
    protected final Double3 xyz;

    /**
     * A constant representing the origin point (0,0,0)
     */
    public static final Point ZERO = new Point(0, 0, 0);

    /**
     * Constructs a point using x, y, and z coordinates.
     *
     * @param x x-coordinate of the point
     * @param y y-coordinate of the point
     * @param z z-coordinate of the point
     */
    public Point(double x, double y, double z) {
        this.xyz = new Double3(x, y, z);
    }

    /**
     * Constructs a point using a Double3 object.
     *
     * @param xyz the three-dimensional coordinate representation
     */
    public Point(Double3 xyz) {
        this.xyz = xyz;
    }

    /**
     * Computes the vector from another point to this point.
     *
     * @param point the point to subtract from this point
     * @return the resulting vector
     */
    public Vector subtract(Point point) {
        return new Vector(this.xyz.subtract(point.xyz));
    }

    /**
     * Adds a vector to this point, resulting in a new point.
     *
     * @param vector the vector to add
     * @return the new point after addition
     */
    public Point add(Vector vector) {
        return new Point(this.xyz.add(vector.xyz));
    }

    /**
     * Computes the squared distance between this point and another point.
     *
     * @param point the other point
     * @return the squared distance
     */
    public double distanceSquared(Point point) {
        double dx = this.xyz.d1() - point.xyz.d1();
        double dy = this.xyz.d2() - point.xyz.d2();
        double dz = this.xyz.d3() - point.xyz.d3();
        return dx * dx + dy * dy + dz * dz;
    }

    /**
     * Computes the Euclidean distance between this point and another point.
     *
     * @param point the other point
     * @return the distance between the two points
     */
    public double distance(Point point) {
        return Math.sqrt(this.distanceSquared(point));
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        return (obj instanceof Point other) && this.xyz.equals(other.xyz);
    }

    @Override
    public String toString() {
        return this.xyz.toString();
    }
}
