package renderer;

import geometries.Triangle;
import lighting.AmbientLight;
import lighting.SpotLight;
import org.junit.jupiter.api.Test;
import primitives.Color;
import primitives.Point;
import primitives.Vector;
import scene.Scene;

import java.io.IOException;
import java.util.List;
import java.util.Random;

import static java.awt.Color.*;
import static org.junit.jupiter.api.Assertions.*;
class StlReaderTest {

    @Test
    void readSTLPositioned() throws IOException {
        final Scene scene = new Scene("Test scene");
        final Camera.Builder cameraBuilder = Camera.getBuilder()
                .setRayTracer(scene, RayTracerType.SIMPLE);
        Point targetPos = new Point(0, 0, -50);
        List<Triangle> positioned = StlReader.readSTLPositioned("C:\\Users\\djyb0\\Downloads\\files\\BODY.stl", targetPos);
        List<Triangle> scale = StlReader.scaleTriangles(positioned, 0.4, targetPos);
        System.out.println("Number of triangles: " + positioned.size());
        Color[] colors = {
                new Color(GRAY),
                new Color(BLACK),
                new Color(40,40,40),
        };

        Random rand = new Random();
        for (Triangle t : positioned) {
            t.setEmission(colors[rand.nextInt(colors.length)]);
            scene.geometries.add(t);
        }
        scene.setAmbientLight(new AmbientLight(new Color(40, 40, 40)));
        scene.lights.add(
                new SpotLight(new Color(200, 200, 200), new Point(50, 50, 30), new Vector(-6, -6, -4))
                        .setKl(4E-5).setKq(2E-7)
        );
        cameraBuilder
                .setLocation(new Point(1000, 0, 0))
                .setDirection(new Point(0, 0, 0), Vector.AXIS_Z)
                .setVpDistance(1000).setVpSize(200, 200)
                .setResolution(900, 900)
                .setMultithreading(6)
                .setDebugPrint(0.5)
                .build()
                .renderImage()
                .writeToImage("StlReaderTest-positioned");
    }

    @Test
    void scaleTriangles() {
    }
}