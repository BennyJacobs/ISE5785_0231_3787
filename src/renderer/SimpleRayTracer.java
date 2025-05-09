package renderer;

import primitives.Color;
import primitives.Point;
import primitives.Ray;
import scene.Scene;

/**
 * A basic ray tracer for evaluating rays in a scene.
 * <p>
 * Provides a simple implementation for determining the color along a ray.
 */
public class SimpleRayTracer extends RayTracerBase {

    /**
     * Creates a new SimpleRayTracer for the given scene.
     *
     * @param scene the scene in which rays will be traced
     */
    protected SimpleRayTracer(Scene scene) {
        super(scene);
    }

    @Override
    public Color traceRay(Ray ray) {
        var intersections = scene.geometries.findIntersections(ray);
        if (intersections == null)
            return scene.background;
        Point pointToColor = ray.findClosestPoint(intersections);
        return calcColor(pointToColor);
    }

    /**
     * Calculates the color at a given point in the scene.
     *
     *
     * @param p the point in the scene to calculate the color for
     * @return the color at the point
     */
    private Color calcColor(Point p) {
        return scene.ambientLight.getIntensity();
    }
}
