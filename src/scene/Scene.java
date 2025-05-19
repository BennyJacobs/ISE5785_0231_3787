package scene;

import geometries.Geometries;
import lighting.AmbientLight;
import lighting.LightSource;
import primitives.Color;

import java.util.LinkedList;
import java.util.List;

/**
 * Represents a 3D scene to be rendered.
 * <p>
 * A scene contains geometries, background color, and lighting information such as ambient light.
 * This class acts as a container for all elements required to describe a renderable scene.
 */
public class Scene {

    /**
     * The name of the scene.
     */
    public String name;

    /**
     * The background color of the scene. Default is black.
     */
    public Color background = Color.BLACK;

    /**
     * The ambient light of the scene. Default is no ambient light.
     */
    public AmbientLight ambientLight = AmbientLight.NONE;

    /**
     * The collection of geometries in the scene.
     */
    public Geometries geometries = new Geometries();

    /**
     * The collection of light sources in the scene.
     */
    public List<LightSource> lights = new LinkedList<>();

    /**
     * Constructs a scene with the specified name.
     *
     * @param name the name of the scene
     */
    public Scene(String name) {
        this.name = name;
    }

    /**
     * Sets the background color of the scene.
     *
     * @param background the background color
     * @return this Scene object for method chaining
     */
    public Scene setBackground(Color background) {
        this.background = background;
        return this;
    }

    /**
     * Sets the ambient light of the scene.
     *
     * @param ambientLight the ambient light
     * @return this Scene object for method chaining
     */
    public Scene setAmbientLight(AmbientLight ambientLight) {
        this.ambientLight = ambientLight;
        return this;
    }

    /**
     * Sets the geometries of the scene.
     *
     * @param geometries the geometries to include in the scene
     * @return this Scene object for method chaining
     */
    public Scene setGeometries(Geometries geometries) {
        this.geometries = geometries;
        return this;
    }

    /**
     * Sets the light sources of the scene.
     *
     * @param lights the light sources to include in the scene
     * @return this Scene object for method chaining
     */
    public Scene setLights(List<LightSource> lights) {
        this.lights = lights;
        return this;
    }
}
