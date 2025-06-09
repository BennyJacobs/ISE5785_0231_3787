package renderer;

import geometries.*;
import lighting.AmbientLight;
import lighting.DirectionalLight;
import lighting.SpotLight;
import org.junit.jupiter.api.Test;
import primitives.*;
import scene.Scene;

import java.util.ArrayList;
import java.util.List;

import static java.awt.Color.*;

public class PRO1Tests {
    @Test
    void allEffects() {
        final Scene scene = new Scene("Test scene");
        final Camera.Builder cameraBuilder = Camera.getBuilder()
                .setRayTracer(scene, RayTracerType.SIMPLE);
        scene.setAmbientLight(new AmbientLight(new Color(40, 40, 40)));
        cameraBuilder
                .setLocation(new Point(0, 0, 500))
                .setDirection(new Point(0, 0, -1))
                .setVpDistance(1000).setVpSize(200, 200)
                .setResolution(1000, 1000)
                .build()
                .renderImage()
                .writeToImage("allEffects-PRO1");
    }
}