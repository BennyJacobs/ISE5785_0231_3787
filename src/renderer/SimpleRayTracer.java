package renderer;

import geometries.Intersectable.Intersection;
import lighting.LightSource;
import primitives.*;
import scene.Scene;

import java.util.List;

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
     * Maximum recursion depth for calculating global lighting effects (reflection and refraction).
     * <p>
     * This prevents infinite loops and excessive computation when tracing reflection/refraction rays.
     * </p>
     */
    private static final int MAX_CALC_COLOR_LEVEL = 10;

    /**
     * Minimum attenuation factor (k) for continuing recursive global lighting calculations.
     * <p>
     * When the accumulated transparency or reflection coefficient drops below this threshold,
     * further contribution is considered negligible and the recursion stops.
     * </p>
     */
    private static final double MIN_CALC_COLOR_K = 0.001;

    /**
     * Initial attenuation factor used when starting the global lighting calculation.
     * <p>
     * Represents full intensity (no attenuation) and is scaled down during recursive tracing
     * by transparency and reflection coefficients.
     * </p>
     */
    private static final Double3 INITIAL_K = Double3.ONE;

    @Override
    public Color traceRay(Ray ray) {
        Intersection intersections = findClosestIntersection(ray);
        return intersections == null
                ? scene.background
                : calcColor(intersections, ray);
    }

    /**
     * Computes the color at a given intersection point using recursive color calculation.
     * <p>
     * This method prepares the intersection and delegates the full computation
     * to the recursive {@code calcColor} method with initial depth and attenuation parameters.
     *
     * @param intersection the intersection point to evaluate
     * @param ray the ray that hit the geometry at the intersection
     * @return the resulting color at the intersection point
     */
    private Color calcColor(Intersection intersection, Ray ray) {
        return preprocessIntersection(intersection, ray.getDirection()) ?
                calcColor(intersection, MAX_CALC_COLOR_LEVEL, INITIAL_K)
                        .add(scene.ambientLight.getIntensity().scale(intersection.material.kA)) : Color.BLACK;
    }

    /**
     * Calculates the total color at an intersection point including local and global lighting effects.
     *
     * @param intersection the intersection point for which to calculate the color
     * @param level the recursion level for global lighting (reflection/refraction)
     * @param k the accumulated transparency/reflection coefficient
     * @return the resulting color at the intersection
     */
    private Color calcColor(Intersection intersection, int level, Double3 k) {
        Color color = calcLocalEffects(intersection, k);
        return 1 == level ? color : color.add(calcGlobalEffects(intersection, level, k));
    }

    /**
     * Prepares intersection data for lighting calculations.
     *
     * @param intersection the intersection to process
     * @param rayDirection the direction of the ray that hit the geometry
     * @return true if the intersection is valid for lighting calculations, false otherwise
     */
    private Boolean preprocessIntersection(Intersection intersection, Vector rayDirection) {
        intersection.v = rayDirection;
        intersection.normal = intersection.geometry.getNormal(intersection.point);
        intersection.vNormal = intersection.v.dotProduct(intersection.normal);
        return !Util.isZero(intersection.vNormal);
    }

    /**
     * Sets the light source information for a given intersection.
     *
     * @param intersection the intersection to update
     * @param lightSource  the light source affecting the intersection
     * @return true if the light contributes to the lighting calculation, false otherwise
     */
    private Boolean setLightSource(Intersection intersection, LightSource lightSource) {
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
    private Color calcLocalEffects(Intersection intersection, Double3 k) {
        Color color = intersection.geometry.getEmission();
        for (LightSource lightSource : scene.lights) {
            if (!setLightSource(intersection, lightSource))
                continue;
            Double3 ktr = transparency(intersection);
            if (!ktr.product(k).lowerThan(MIN_CALC_COLOR_K)) {
                Color iL = lightSource.getIntensity(intersection.point).scale(ktr);
                color = color.add(
                        iL.scale(calcDiffusive(intersection)
                                .add(calcSpecular(intersection)))
                );
            }
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
        var intersections = scene.geometries.calculateIntersections(
                new Ray(intersection.point, intersection.l.scale(-1), intersection.normal),
                intersection.light.getDistance(intersection.point));
        if (intersections == null)
            return true;
        else {
            for (Intersection i : intersections) {
                if (i.material.kT.lowerThan(MIN_CALC_COLOR_K))
                    return false;
            }
        }
        return true;

    }

    /**
     * Calculates the transparency factor (ktr) from the intersection point toward the light source.
     *
     * @param intersection the intersection point being evaluated
     * @return the accumulated transparency factor along the path to the light source
     */
    private Double3 transparency(Intersection intersection) {
        var intersections = scene.geometries.calculateIntersections(
                new Ray(intersection.point, intersection.l.scale(-1), intersection.normal),
                intersection.light.getDistance(intersection.point));
        Double3 ktr = Double3.ONE;
        if (intersections == null)
            return ktr;
        else {
            for (Intersection i : intersections) {
                if (i.material.kT.lowerThan(MIN_CALC_COLOR_K))
                    return Double3.ZERO;
                ktr = ktr.product(i.material.kT);
            }
        }
        return ktr;

    }

    /**
     * Calculates the global lighting effects (reflection and refraction) at the intersection point.
     *
     * @param intersection the intersection point being evaluated
     * @param level the recursion level for global lighting
     * @param k the accumulated transparency/reflection coefficient
     * @return the resulting color from global lighting effects
     */
    private Color calcGlobalEffects(Intersection intersection, int level, Double3 k) {
        Color refractedColor = Color.BLACK;
        Color reflectedColor = Color.BLACK;

        if (intersection.material.targetAreaSizeBlurry == 0.0 ||
                intersection.material.targetAreaDistanceBlurry == 0.0) {
            Ray refractedRay = constructRefractedRay(intersection);
            refractedColor = calcGlobalEffect(refractedRay, level, k, intersection.material.kT);

        }
        else {
            List<Ray> refractedBeam = constructRefractedRay(intersection)
                    .createBeam(intersection.material.targetAreaDistanceBlurry,
                            intersection.material.targetAreaSizeBlurry,
                            intersection.material.numOfRaysBlurry,
                            scene.samplingPattern);

            for (Ray ray : refractedBeam) {
                if (Util.alignZero(ray.getDirection().dotProduct(intersection.geometry.getNormal(intersection.point))) > 0)
                    refractedColor = refractedColor.add(calcGlobalEffect(ray, level, k, intersection.material.kT));
            }

            refractedColor = refractedColor.reduce(refractedBeam.size());
        }

        if (intersection.material.targetAreaSizeGlossy == 0.0 ||
                intersection.material.targetAreaDistanceGlossy == 0.0) {
            Ray reflectedRay = constructReflectedRay(intersection);
            reflectedColor = calcGlobalEffect(reflectedRay, level, k, intersection.material.kR);

        }
        else {
            List<Ray> reflectedBeam = constructReflectedRay(intersection)
                    .createBeam(intersection.material.targetAreaDistanceGlossy,
                            intersection.material.targetAreaSizeGlossy,
                            intersection.material.numOfRaysGlossy,
                            scene.samplingPattern);

            for (Ray ray : reflectedBeam) {
                if (Util.alignZero(ray.getDirection().dotProduct(intersection.normal)) > 0)
                    reflectedColor = reflectedColor.add(calcGlobalEffect(ray, level, k, intersection.material.kR));
            }

            reflectedColor = reflectedColor.reduce(reflectedBeam.size());
        }

        return refractedColor.add(reflectedColor);
    }


    /**
     * Calculates the global color contribution from a single reflected or refracted ray.
     *
     * @param ray the reflected or refracted ray
     * @param level the current recursion level
     * @param k the accumulated attenuation factor so far
     * @param kx the reflection or refraction coefficient for the current step
     * @return the color contribution from this global effect
     */
    private Color calcGlobalEffect(Ray ray, int level, Double3 k, Double3 kx) {
        Double3 kkx = k.product(kx);
        if (kkx.lowerThan(MIN_CALC_COLOR_K)) return Color.BLACK;
        Intersection intersection = findClosestIntersection(ray);
        if (intersection == null) return scene.background.scale(kx);
        return preprocessIntersection(intersection, ray.getDirection())
                ? calcColor(intersection, level - 1, kkx).scale(kx) : Color.BLACK;
    }

    /**
     * Constructs a refracted ray starting from the intersection point in the same direction as the incoming ray.
     *
     * @param intersection the intersection point
     * @return the refracted ray
     */
    private Ray constructRefractedRay(Intersection intersection) {
        return new Ray(intersection.point, intersection.v, intersection.normal);
    }

    /**
     * Constructs a reflected ray starting from the intersection point.
     *
     * @param intersection the intersection point
     * @return the reflected ray
     */
    private Ray constructReflectedRay(Intersection intersection) {
        Vector r = intersection.v.add(intersection.normal.scale(intersection.vNormal * -2));
        return new Ray(intersection.point, r, intersection.normal);
    }

    /**
     * Finds the closest intersection point along the given ray.
     *
     * @param ray the ray to trace
     * @return the closest intersection, or {@code null} if there are no intersections
     */
    private Intersection findClosestIntersection(Ray ray) {
        var intersections = scene.geometries.calculateIntersections(ray);
        return intersections == null ? null : ray.findClosestIntersection(intersections);
    }

}
