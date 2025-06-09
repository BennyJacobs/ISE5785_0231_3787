package primitives;

import geometries.Geometries;
import geometries.Sphere;
import org.junit.jupiter.api.Test;
import renderer.Camera;
import renderer.RayTracerType;
import scene.Scene;

import java.awt.*;

import static java.awt.Color.*;


import static org.junit.jupiter.api.Assertions.*;

class TargetAreaTest {

    @Test
    void generatePoints() {
        renderSampling(TargetArea.SamplingPattern.RANDOM, "TargetAreaTest-random");
        renderSampling(TargetArea.SamplingPattern.GRID, "TargetAreaTest-grid");
        renderSampling(TargetArea.SamplingPattern.JITTERED, "TargetAreaTest-jittered");
    }

    private void renderSampling(TargetArea.SamplingPattern pattern, String fileName) {
        Scene scene = new Scene("Target Area - " + pattern);
        Ray test = new Ray(new Point(0, 0, 0), new Vector(0, 0, -1));
        TargetArea area = new TargetArea(test, 20, 20, 112, pattern);

        Geometries geometries = new Geometries();
        for (Point p : area.generatePoints()) {
            geometries.add(new Sphere(p, 1).setEmission(new Color(BLUE)));
        }
        scene.geometries.add(geometries);

        Camera.getBuilder()
                .setRayTracer(scene, RayTracerType.SIMPLE)
                .setLocation(new Point(0, 0, 1000))
                .setDirection(new Point(0, 0, -1))
                .setVpDistance(1000)
                .setVpSize(200, 200)
                .setResolution(1000, 1000)
                .build()
                .renderImage()
                .writeToImage(fileName);
    }

}