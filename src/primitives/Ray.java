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
