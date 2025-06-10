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
        renderSamplingCircle(TargetArea.SamplingPattern.RANDOM, "TargetAreaTest-random-circle");
        renderSamplingCircle(TargetArea.SamplingPattern.GRID, "TargetAreaTest-grid-circle");
        renderSamplingCircle(TargetArea.SamplingPattern.JITTERED, "TargetAreaTest-jittered-circle");
        renderSamplingRectangle(TargetArea.SamplingPattern.RANDOM, "TargetAreaTest-random-rectangle");
        renderSamplingRectangle(TargetArea.SamplingPattern.GRID, "TargetAreaTest-grid-rectangle");
        renderSamplingRectangle(TargetArea.SamplingPattern.JITTERED, "TargetAreaTest-jittered-rectangle");
    }

    private void renderSamplingCircle(TargetArea.SamplingPattern pattern, String fileName) {
        Scene scene = new Scene("Target Area - " + pattern + " circle");
        Ray test = new Ray(new Point(0, 0, 0), new Vector(0, 0, -1));
        CircleTargetArea area = new CircleTargetArea(20, test, 5, 100, pattern);

        Geometries geometries = new Geometries();
        var points = area.generatePoints();
        for (Point p : points) {
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
    private void renderSamplingRectangle(TargetArea.SamplingPattern pattern, String fileName) {
        Scene scene = new Scene("Target Area - " + pattern + " rectangle");
        Ray test = new Ray(new Point(0, 0, 0), new Vector(0, 0, -1));
        QuadrilateralTargetArea area = new QuadrilateralTargetArea(20, 30, test, 5, 100, pattern);

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