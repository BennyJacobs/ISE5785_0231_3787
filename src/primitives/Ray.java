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
