package primitives;

import geometries.Intersectable.Intersection;

import java.util.List;

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
     * A constant to prevent self-intersecting
     */
    private static final double DELTA = 0.1;

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
     * Constructs a ray with a starting point, a direction vector, and a surface normal for biasing.
     * <p>
     * This constructor is used to offset the ray origin slightly along the normal direction,
     * which helps avoid self-intersections (e.g., in shadow rays or reflected rays).
     * The ray origin is moved by a small constant {@code DELTA} along the normal direction,
     * depending on whether the ray is pointing in the same direction as the normal or the opposite.
     * </p>
     *
     * @param head      the original starting point of the ray (before offset)
     * @param direction the direction vector of the ray (will be normalized)
     * @param normal    the surface normal at the point of origin, used for offsetting the ray to prevent self-intersection
     */
    public Ray(Point head, Vector direction, Vector normal) {
        this.direction = direction.normalize();
        double side = direction.dotProduct(normal);
        if (Util.isZero(side))
            this.head = head;
        else
            this.head = head.add(normal.scale(side > 0 ? DELTA : -DELTA));
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
        try {
            return head.add(direction.scale(t));
        } catch (IllegalArgumentException e) {
            return head;
        }
    }


    /**
     * Finds and returns the point from the given list that is closest to the ray's origin.
     *
     * @param points a list of points to search through
     * @return the closest point to the ray's origin, or {@code null} if the list is {@code null}
     */
    public Point findClosestPoint(List<Point> points) {
        return points == null ? null
                : findClosestIntersection(points.stream().map(p -> new Intersection(null, p)).toList()).point;
    }

    /**
     * Finds the intersection from the given list that is closest to the origin of the ray.
     *
     * @param intersections a list of intersections to evaluate
     * @return the closest intersection to the ray's origin, or {@code null} if the list is {@code null} or empty
     */
    public Intersection findClosestIntersection(List<Intersection> intersections) {
        if (intersections == null)
            return null;
        Intersection closestIntersection = null;
        double minDistance = Double.POSITIVE_INFINITY;
        for (Intersection intersection : intersections) {
            double distance = intersection.point.distanceSquared(head);
            if (distance < minDistance) {
                minDistance = distance;
                closestIntersection = intersection;
            }
        }
        return closestIntersection;
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
