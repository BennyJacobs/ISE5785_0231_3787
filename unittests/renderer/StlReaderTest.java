package renderer;

import geometries.Plane;
import geometries.Sphere;
import geometries.Triangle;
import lighting.AmbientLight;
import lighting.PointLight;
import lighting.SpotLight;
import org.junit.jupiter.api.Test;
import primitives.Color;
import primitives.Material;
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
        Point targetPos = new Point(0, -15, -50);
        List<Triangle> positioned = StlReader.readSTLPositioned("C:\\Users\\djyb0\\Downloads\\files\\BODY.stl", targetPos);
        List<Triangle> scale = StlReader.scaleTriangles(positioned, 0.8, targetPos);
        System.out.println("Number of triangles: " + positioned.size());
        Random rand = new Random();
        for (Triangle t : scale) {
            double chance = rand.nextDouble();
            if (chance < 0.8)
                t.setEmission(new Color(40,40,40));
            else
                t.setEmission(new Color(0,100,150));
            t.setMaterial(new Material().setKS(0.5).setKD(0.5).setShininess(40));
            scene.geometries.add(t);
        }
        scene.geometries.add(new Plane(new Point(0,0,-70), new Vector(0,0,1)).setMaterial(new Material().setKD(0.5).setKS(0.5).setShininess(20)));
        scene.setAmbientLight(new AmbientLight(new Color(40, 40, 40)));
        scene.lights.add(
                new PointLight(new Color(300, 300, 300), new Point(0, 0, 0))
                        .setKl(4E-5).setKq(2E-7)
        );
        cameraBuilder
                .setLocation(new Point(1000, 0, 200))
                .setDirection(new Point(0, 0, 0), Vector.AXIS_Z)
                .setVpDistance(1000).setVpSize(200, 200)
                .setResolution(900, 900)
                .setMultithreading(-2)
                .setDebugPrint(0.5)
                .build()
                .renderImage()
                .writeToImage("StlReaderTest-positioned");
    }
}