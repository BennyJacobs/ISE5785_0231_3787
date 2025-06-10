package renderer;

import primitives.Color;
import primitives.Ray;
import primitives.Point;
import primitives.Vector;
import scene.Scene;

/**
 * Represents the base for ray tracing functionality.
 * <p>
 * Used to determine the color seen along a ray within a given scene.
 */
public abstract class RayTracerBase {

    /**
     * The scene through which rays are traced.
     */
    protected final Scene scene;

    /**
     * Constructs a ray tracer for the specified scene.
     *
     * @param scene the scene to be used for ray tracing
     */
    protected RayTracerBase(Scene scene) {
        this.scene = scene;
    }

    /**
     * Determines the color seen along the specified ray in the scene.
     *
     * @param ray the ray to be evaluated
     * @return the resulting color seen along the ray
     */
    public abstract Color traceRay(Ray ray);
}
