package primitives;

/**
 * Represents a ray in 3D space.
 * A ray is defined by a starting point (head) and a direction vector.
 */
public class Ray {
    /**
     * The starting point of the ray
     */
    private final Point head;

    /**
     * The direction of the ray
     */
    private final Vector direction;

    /**
     * Constructs a ray with a starting point and a direction.
     *
     * @param head      the starting point of the ray
     * @param direction the direction of the ray
     */
    public Ray(Point head, Vector direction) {
        this.head = head;
        this.direction = direction.normalize();
    }


    /**
     * Returns the head of the vector.
     *
     * @return a {@link Point} object representing the head of the vector.
     */
    public Point getHead() {
        return head;
    }

    /**
     * Returns the direction of the vector.
     *
     * @return a {@link Vector} object representing the direction of the vector.
     */
    public Vector getDirection() {
        return direction;
    }

    /**
     * Calculates a point along the ray at a given distance from the ray's origin.
     *
     * @param t the distance from the ray's origin at which to calculate the point.
     * @return the point located at the specified distance along the ray.
     */
    public Point getPoint(double t) {
        if (Util.isZero(t))
            return head;
        return head.add(direction.scale(t));
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        return (obj instanceof Ray other)
                && this.head.equals(other.head)
                && this.direction.equals(other.direction);
    }

    @Override
    public String toString() {
        return "Ray: head: " + head.toString() + " -> " + direction.toString();
    }
}
