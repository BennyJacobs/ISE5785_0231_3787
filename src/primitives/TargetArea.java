package primitives;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Represents a target area for distributing rays in a geometric region.
 * Used for implementing soft shadows and diffuse reflections.
 */
public class TargetArea {

    /**
     * Enumeration defining different patterns for sampling points on the target area.
     */
    public enum SamplingPattern {
        /**
         * Random sampling with uniform distribution
         */
        RANDOM,
        /**
         * Regular grid sampling
         */
        GRID,
        /**
         * Grid sampling with jittered (randomized) positions
         */
        JITTERED
    }

    /**
     * The radius of the target area.
     */
    private final double radius;

    /**
     * The distance from the ray origin to the target area
     */
    private final double distance;

    /**
     * X-axis for the target plane
     */
    private final Vector xVec;

    /**
     * Y-axis for the target plane
     */
    private final Vector yVec;

    /**
     * The ray to create the target area for
     */
    private final Ray ray;

    /**
     * The number of sample points to generate.
     */
    private final int numSamples;

    /**
     * The sampling pattern to use for generating points.
     */
    private final SamplingPattern samplingPattern;

    /**
     * Random number generator for sampling methods.
     */
    private static final Random RANDOM = new Random();

    /**
     * The ratio of the area of a square to the area of a circle with the same diameter.
     * <p>
     * This constant is calculated as {@code 4.0 / Math.PI}, which approximates to 1.27324.
     */
    private static final double SQUARE_TO_CIRCLE_RATIO = 4.0 / Math.PI;

    /**
     * Creates a new target area with the specified radius, number of samples, and sampling pattern.
     *
     * @param ray             the ray to create the target area for
     * @param radius          the radius of the target area
     * @param distance        the distance from the ray origin to the target area
     * @param numSamples      the number of sample points to generate
     * @param samplingPattern the pattern to use for distributing the sample points
     */
    public TargetArea(Ray ray, double radius, double distance, int numSamples, SamplingPattern samplingPattern) {
        this.ray = ray;
        this.radius = radius;
        this.distance = distance;
        this.numSamples = numSamples;
        this.samplingPattern = samplingPattern;
        Vector rayDirection = ray.getDirection();
        Vector baseVector;
        // Choose the axis that is more perpendicular to the ray direction
        if (Math.abs(rayDirection.dotProduct(Vector.AXIS_Y)) < Math.abs(rayDirection.dotProduct(Vector.AXIS_X)))
            baseVector = Vector.AXIS_Y;
        else
            baseVector = Vector.AXIS_X;
        // Create an orthogonal coordinate system on the target area
        this.xVec = baseVector.crossProduct(rayDirection).normalize();
        this.yVec = rayDirection.crossProduct(xVec).normalize();

    }

    /**
     * Generates points on the target area centered at the given point and perpendicular to the given direction.
     * @return a list of points distributed on the target area
     */
    public List<Point> generatePoints() {
        // Generate points according to the specified sampling pattern
        return samplingPattern == SamplingPattern.RANDOM ? generateRandomPoints()
                : generateGridPoints();
    }

    /**
     * Generates points using a random sampling pattern.
     */
    private List<Point> generateRandomPoints() {
        Point targetCenter = ray.getPoint(distance);
        List<Point> points = new ArrayList<>();
        for (int i = 0; i < numSamples; i++) {
            double theta = RANDOM.nextDouble() * 2 * Math.PI;
            double r = Math.sqrt(RANDOM.nextDouble()) * radius;
            double x = r * Math.cos(theta);
            double y = r * Math.sin(theta);
            Point targetPoint = targetCenter;
            if (!Util.isZero(x)) targetPoint = targetPoint.add(xVec.scale(x));
            if (!Util.isZero(y)) targetPoint = targetPoint.add(yVec.scale(y));
            points.add(targetPoint);
        }

        return points;
    }

    /**
     * Generates points using a grid sampling pattern.
     * Adding jitter when chosen
     */
    private List<Point> generateGridPoints() {
        Point targetCenter = ray.getPoint(distance);
        List<Point> points = new ArrayList<>();
        int adjustedNumSamples = (int) (numSamples * SQUARE_TO_CIRCLE_RATIO);
        int gridSize = (int) Math.ceil(Math.sqrt(adjustedNumSamples));
        double cellSize = 2 * radius / gridSize;
        for (int i = 0; i < gridSize && points.size() < numSamples; i++) {
            for (int j = 0; j < gridSize && points.size() < numSamples; j++) {
                double x = -radius + cellSize * (i + 0.5);
                double y = -radius + cellSize * (j + 0.5);
                if (samplingPattern == SamplingPattern.JITTERED) {
                    double jitterX = (RANDOM.nextDouble() - 0.5) * cellSize * 0.8;
                    double jitterY = (RANDOM.nextDouble() - 0.5) * cellSize * 0.8;
                    x += jitterX;
                    y += jitterY;
                }
                if (x * x + y * y <= radius * radius) {
                    Point targetPoint = targetCenter;
                    if (!Util.isZero(x)) targetPoint = targetPoint.add(xVec.scale(x));
                    if (!Util.isZero(y)) targetPoint = targetPoint.add(yVec.scale(y));
                    points.add(targetPoint);
                }
            }
        }

        return points;
    }

}