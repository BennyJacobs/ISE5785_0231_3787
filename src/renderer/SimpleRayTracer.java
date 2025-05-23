package renderer;

import geometries.Intersectable.Intersection;
import lighting.LightSource;
import primitives.*;
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

    /**
     * A constant shadow ray bias to prevent self-shadowing
     */
    private static final double DELTA = 0.1;

    @Override
    public Color traceRay(Ray ray) {
        var intersections = scene.geometries.calculateIntersections(ray);
        return intersections == null
                ? scene.background
                : calcColor(ray.findClosestIntersection(intersections), ray);
    }

    /**
     * Calculates the color resulting from the interaction of a ray with an intersection point.
     *
     * @param intersection the intersection in the scene to calculate the color for
     * @param ray the ray that intersected the geometry
     * @return the calculated color at the intersection point
     */
    private Color calcColor(Intersection intersection, Ray ray) {
        if (!preprocessIntersection(intersection, ray.getDirection()))
            return Color.BLACK;
        return scene.ambientLight.getIntensity().scale(intersection.material.kA)
                .add(calcColorLocalEffects(intersection));
    }

    /**
     * Prepares intersection data for lighting calculations.
     *
     * @param intersection the intersection to process
     * @param rayDirection the direction of the ray that hit the geometry
     * @return true if the intersection is valid for lighting calculations, false otherwise
     */
    public Boolean preprocessIntersection(Intersection intersection, Vector rayDirection) {
        intersection.v = rayDirection;
        intersection.normal = intersection.geometry.getNormal(intersection.point);
        intersection.vNormal = intersection.v.dotProduct(intersection.normal);
        return !Util.isZero(intersection.vNormal);
    }

    /**
     * Sets the light source information for a given intersection.
     *
     * @param intersection the intersection to update
     * @param lightSource the light source affecting the intersection
     * @return true if the light contributes to the lighting calculation, false otherwise
     */
    public Boolean setLightSource(Intersection intersection, LightSource lightSource) {
        intersection.light = lightSource;
        intersection.l = lightSource.getL(intersection.point);
        intersection.lNormal = intersection.l.dotProduct(intersection.normal);
        return Util.alignZero(intersection.vNormal * intersection.lNormal) > 0;
    }

    /**
     * Calculates the local lighting effects (diffuse and specular) at the intersection point.
     *
     * @param intersection the intersection to evaluate
     * @return the resulting color from all local light sources
     */
    private Color calcColorLocalEffects(Intersection intersection) {
        Color color = intersection.geometry.getEmission();
        for (LightSource lightSource : scene.lights)
        {
            if (!setLightSource(intersection, lightSource) || !unshaded(intersection))
                continue;
            Color iL = lightSource.getIntensity(intersection.point);
            color = color.add(
                    iL.scale(calcDiffusive(intersection)
                            .add(calcSpecular(intersection)))
            );
        }
        return color;
    }

    /**
     * Calculates the specular reflection component at the intersection point.
     *
     * @param intersection the intersection to evaluate
     * @return the specular component as a scaling factor
     */
    private Double3 calcSpecular(Intersection intersection) {
        Vector r = intersection.l.add(intersection.normal.scale(intersection.lNormal * -2));
        return intersection.material.kS.scale(Math.pow(Math.max(0, -1 * intersection.v.dotProduct(r)), intersection.material.nShininess));
    }

    /**
     * Calculates the diffuse reflection component at the intersection point.
     *
     * @param intersection the intersection to evaluate
     * @return the diffuse component as a scaling factor
     */
    private Double3 calcDiffusive(Intersection intersection) {
        return intersection.material.kD.scale(Math.abs(intersection.lNormal));
    }

    /**
     * Determines whether the intersection point is not shadowed with respect to the current light source.
     *
     * @param intersection the intersection point to evaluate
     * @return true if the point is not shadowed (i.e., light reaches it), false otherwise
     */
    private boolean unshaded(Intersection intersection) {
        Vector pointToLight = intersection.l.scale(-1);
        Vector delta = intersection.normal.scale(intersection.lNormal < 0 ? DELTA : -DELTA);
        return scene.geometries.calculateIntersections(new Ray(intersection.point.add(delta), pointToLight),
                intersection.light.getDistance(intersection.point)) == null;
    }

}
