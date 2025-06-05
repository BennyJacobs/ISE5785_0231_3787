package primitives;

/**
 * Class representing material properties for a geometry's surface.
 * <p>
 * Material parameters define how the surface interacts with light, such as
 * ambient reflection, diffuse reflection, specular reflection, and shininess.
 * </p>
 */
public class Material {

    /**
     * Ambient reflection coefficient.
     * Controls how much ambient light is reflected from the surface.
     */
    public Double3 kA = Double3.ONE;

    /**
     * Specular reflection coefficient.
     * Controls the strength of the specular highlight.
     */
    public Double3 kS = Double3.ZERO;

    /**
     * Diffuse reflection coefficient.
     * Controls how much diffuse light is scattered from the surface.
     */
    public Double3 kD = Double3.ZERO;

    /**
     * Transparency coefficient.
     * Controls how much light is transmitted through the surface (used for refraction).
     * A value of 0 means fully opaque; higher values allow more light to pass through.
     */
    public Double3 kT = Double3.ZERO;

    /**
     * Reflection coefficient.
     * Controls how much light is reflected by the surface in a mirror-like manner.
     * A value of 0 means no mirror reflection; higher values increase reflection.
     */
    public Double3 kR = Double3.ZERO;

    /**
     * Shininess factor (Phong model).
     * Higher values produce smaller and sharper specular highlights.
     */
    public int nShininess = 0;
    /**
     * Defines the number of sampled rays used for glossy reflection calculations.
     */
    public int numOfRaysGlossy = 1;

    /**
     * Represents the number of rays used for blur in a material.
     * This value determines the level of sampling used for creating realistic blur effects.
     */
    public int numOfRaysBlurry = 1;

    /**
     * Represents the size of the target area for a glossy surface.
     */
    public double targetAreaSizeGlossy = 0.0;

    /**
     * Represents the size of the target area for a refractive surface.
     */
    public double targetAreaSizeBlurry = 0.0;

    /**
     * Represents the distance of the target area for a glossy surface.
     */
    public double targetAreaDistanceGlossy = 0.0;

    /**
     * Represents the distance of the target area for a refractive surface.
     */
    public double targetAreaDistanceBlurry = 0.0;

    /**
     * Sets the ambient reflection coefficient using a {@link Double3}.
     *
     * @param kA the ambient coefficient as a {@code Double3}
     * @return the current {@code Material} object for method chaining
     */
    public Material setKA(Double3 kA) {
        this.kA = kA;
        return this;
    }

    /**
     * Sets the ambient reflection coefficient using a single double value.
     * The same value is applied to all RGB components.
     *
     * @param kA the ambient coefficient value
     * @return the current {@code Material} object for method chaining
     */
    public Material setKA(double kA) {
        return setKA(new Double3(kA));
    }

    /**
     * Sets the specular reflection coefficient using a {@link Double3}.
     *
     * @param kS the specular coefficient as a {@code Double3}
     * @return the current {@code Material} object for method chaining
     */
    public Material setKS(Double3 kS) {
        this.kS = kS;
        return this;
    }

    /**
     * Sets the specular reflection coefficient using a single double value.
     * The same value is applied to all RGB components.
     *
     * @param kS the specular coefficient value
     * @return the current {@code Material} object for method chaining
     */
    public Material setKS(double kS) {
        return setKS(new Double3(kS));
    }

    /**
     * Sets the diffuse reflection coefficient using a {@link Double3}.
     *
     * @param kD the diffuse coefficient as a {@code Double3}
     * @return the current {@code Material} object for method chaining
     */
    public Material setKD(Double3 kD) {
        this.kD = kD;
        return this;
    }

    /**
     * Sets the diffuse reflection coefficient using a single double value.
     * The same value is applied to all RGB components.
     *
     * @param kD the diffuse coefficient value
     * @return the current {@code Material} object for method chaining
     */
    public Material setKD(double kD) {
        return setKD(new Double3(kD));
    }

    /**
     * Sets the shininess factor.
     *
     * @param nShininess the shininess value
     * @return the current {@code Material} object for method chaining
     */
    public Material setShininess(int nShininess) {
        this.nShininess = nShininess;
        return this;
    }

    /**
     * Sets the transparency coefficient using a {@link Double3}.
     *
     * @param kT the transparency coefficient as a {@code Double3}
     * @return the current {@code Material} object for method chaining
     */
    public Material setKT(Double3 kT) {
        this.kT = kT;
        return this;
    }

    /**
     * Sets the transparency coefficient using a single double value.
     * The same value is applied to all RGB components.
     *
     * @param kT the transparency coefficient value
     * @return the current {@code Material} object for method chaining
     */
    public Material setKT(double kT) {
        return setKT(new Double3(kT));
    }

    /**
     * Sets the reflection coefficient using a {@link Double3}.
     *
     * @param kR the reflection coefficient as a {@code Double3}
     * @return the current {@code Material} object for method chaining
     */
    public Material setKR(Double3 kR) {
        this.kR = kR;
        return this;
    }

    /**
     * Sets the reflection coefficient using a single double value.
     * The same value is applied to all RGB components.
     *
     * @param kR the reflection coefficient value
     * @return the current {@code Material} object for method chaining
     */
    public Material setKR(double kR) {
        return setKR(new Double3(kR));
    }

    /**
     * Sets the diffuse/blurry properties for the material.
     *
     * @param numOfRaysBlurry the number of rays for diffuse calculations; must be at least 2
     * @param targetAreaSizeBlurry the size of the target area for diffuse calculations; must be greater than 0
     * @param targetAreaDistanceBlurry the distance to the target area for diffuse calculations; must be greater than 0
     * @return the current {@code Material} object for method chaining
     * @throws IllegalArgumentException if {@code numOfRaysBlurry < 2}, {@code targetAreaSizeBlurry <= 0},
     *                                   or {@code targetAreaDistanceBlurry <= 0}
     */
    public Material setDiffuseProperties(int numOfRaysBlurry, double targetAreaSizeBlurry, double targetAreaDistanceBlurry) {
        if (numOfRaysBlurry < 2 || targetAreaSizeBlurry <= 0 || targetAreaDistanceBlurry <= 0)
            throw new IllegalArgumentException("Invalid number of rays or target area size or distance");
        this.numOfRaysBlurry = numOfRaysBlurry;
        this.targetAreaSizeBlurry = targetAreaSizeBlurry;
        this.targetAreaDistanceBlurry = targetAreaDistanceBlurry;
        return this;
    }

    /**
     * Sets the glossy properties for the material.
     *
     * @param numOfRaysGlossy the number of rays for glossy calculations; must be at least 2
     * @param targetAreaSizeGlossy the size of the target area for glossy calculations; must be greater than 0
     * @param targetAreaDistanceGlossy the distance to the target area for glossy calculations; must be greater than 0
     * @return the current {@code Material} object for method chaining
     * @throws IllegalArgumentException if {@code numOfRaysGlossy < 2}, {@code targetAreaSizeGlossy <= 0},
     *                                   or {@code targetAreaDistanceGlossy <= 0}
     */
    public Material setGlossyProperties(int numOfRaysGlossy, double targetAreaSizeGlossy, double targetAreaDistanceGlossy) {
        if (numOfRaysGlossy < 2 || targetAreaSizeGlossy <= 0 || targetAreaDistanceGlossy <= 0)
            throw new IllegalArgumentException("Invalid number of rays or target area size or distance");
        this.numOfRaysGlossy = numOfRaysGlossy;
        this.targetAreaSizeGlossy = targetAreaSizeGlossy;
        this.targetAreaDistanceGlossy = targetAreaDistanceGlossy;
        return this;
    }

}
