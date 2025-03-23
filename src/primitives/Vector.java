package primitives;

/**
 * Represents a vector in 3D space.
 * A vector is defined by its three coordinates (x, y, z).
 * It extends the Point class.
 */
public class Vector extends Point {
    /**
     * Constructs a vector using x, y, and z coordinates.
     *
     * @param x x-coordinate of the vector
     * @param y y-coordinate of the vector
     * @param z z-coordinate of the vector
     */
    public Vector(double x, double y, double z) {
        this(new Double3(x, y, z));
    }

    /**
     * Constructs a vector using a Double3 object.
     *
     * @param xyz the three-dimensional coordinate representation
     * @throws IllegalArgumentException if the vector is the zero vector
     */
    public Vector(Double3 xyz) {
        super(xyz);
        if (xyz.equals(Double3.ZERO))
            throw new IllegalArgumentException("ERROR: vector zero is illegal");
    }

    /**
     * Adds another vector to this vector.
     *
     * @param vector the vector to be added
     * @return the resulting vector after addition
     */
    public Vector add(Vector vector) {
        return new Vector(this.xyz.add(vector.xyz));
    }

    /**
     * Scales this vector by a scalar value.
     *
     * @param scalar the scalar multiplier
     * @return the resulting scaled vector
     */
    public Vector scale(double scalar) {
        return new Vector(this.xyz.scale(scalar));
    }

    /**
     * Computes the dot product of this vector and another vector.
     *
     * @param vector the other vector
     * @return the dot product value
     */
    public double dotProduct(Vector vector) {
        return this.xyz.d1() * vector.xyz.d1() + this.xyz.d2() * vector.xyz.d2() + this.xyz.d3() * vector.xyz.d3();
    }

    /**
     * Computes the cross product of this vector and another vector.
     *
     * @param vector the other vector
     * @return the resulting vector after the cross product
     */
    public Vector crossProduct(Vector vector) {
        return new Vector(
                this.xyz.d2() * vector.xyz.d3() - this.xyz.d3() * vector.xyz.d2(),
                this.xyz.d3() * vector.xyz.d1() - this.xyz.d1() * vector.xyz.d3(),
                this.xyz.d1() * vector.xyz.d2() - this.xyz.d2() * vector.xyz.d1()
        );
    }

    /**
     * Computes the squared length of this vector.
     *
     * @return the squared length of the vector
     */
    public double lengthSquared() {
        return this.dotProduct(this);
    }

    /**
     * Computes the length (magnitude) of this vector.
     *
     * @return the length of the vector
     */
    public double length() {
        return Math.sqrt(this.lengthSquared());
    }

    /**
     * Normalizes this vector to have a length of 1.
     *
     * @return the normalized vector
     */
    public Vector normalize() {
        return this.scale(1 / this.length());
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        return (obj instanceof Vector other) && super.equals(other);
    }

    @Override
    public String toString() {
        return "v" + super.toString();
    }
}
