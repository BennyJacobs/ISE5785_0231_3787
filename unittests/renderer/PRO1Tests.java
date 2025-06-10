package renderer;

import geometries.*;
import lighting.AmbientLight;
import lighting.DirectionalLight;
import lighting.PointLight;
import lighting.SpotLight;
import org.junit.jupiter.api.Test;
import primitives.*;
import scene.Scene;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static java.awt.Color.*;
import static renderer.StlReader.readSTL;

public class PRO1Tests {
    @Test
    void allEffects() {
        final Scene scene = new Scene("Test scene");
        final Camera.Builder cameraBuilder = Camera.getBuilder()
                .setRayTracer(scene, RayTracerType.SIMPLE);
        double bottleKD = 0.1;
        double bottleKS = 0.1;
        double bottleKT = 0.35;
        int bottleShininess = 40;
        Color bottleColor = new Color(5, 0, 10);
        Geometries wineBottle = new Geometries();
        wineBottle.add(
                new Cylinder(4, new Ray(new Point(-10, -20, -10), new Vector(0, 1, 0)), 20)
                        .setMaterial(new Material().setKD(bottleKD).setKS(bottleKS).setShininess(bottleShininess).setKT(bottleKT))
                        .setEmission(bottleColor),
                new Cylinder(1.5, new Ray(new Point(-10, 4, -10), new Vector(0, 1, 0)), 8)
                        .setMaterial(new Material().setKD(bottleKD).setKS(bottleKS).setShininess(bottleShininess).setKT(bottleKT))
                        .setEmission(bottleColor),
                new Sphere(new Point(-10, 0, -10), 4)
                        .setMaterial(new Material().setKD(bottleKD).setKS(bottleKS).setShininess(bottleShininess).setKT(bottleKT))
                        .setEmission(bottleColor),
                new Cylinder(3.85, new Ray(new Point(-10, -20, -10), new Vector(0, 1, 0)), 20)
                        .setMaterial(new Material().setKD(0.2).setKS(0.2).setKT(0.3).setShininess(50))
                        .setEmission(new Color(10, 0, 30)),
                new Cylinder(1.6, new Ray(new Point(-10, 12.1, -10), new Vector(0, -1, 0)), 5)
                        .setMaterial(new Material().setKD(0.1).setKS(0.1).setShininess(50))
                        .setEmission(new Color(0, 0, 0))
        );
        Geometries table = new Geometries();
        table.add(
                new Circle(new Point(-20, -20, -20), 40, new Vector(0, 1, 0))
                        .setMaterial(new Material().setKD(0.5).setKS(0.5).setShininess(20)),
                new Cylinder(3, new Ray(new Point(-20, -20, -20), new Vector(0, -1, 0)), 40)
                        .setMaterial(new Material().setKD(0.5).setKS(0.5).setShininess(20))
        );
        double cradleTopZ = -10;
        double cradleTopY = -5;
        double cradleTopX = -25;
        double cradleMinZ = -30;
        double cradleMinY = -20;
        double cradleMinX = -35;
        double cradleHeight = cradleTopY - cradleMinY;
        double cradleLength = cradleTopZ - cradleMinZ;
        double firstBallZ = -27;
        double firstBallY = -15;
        double firstBallX = -30;
        Geometries newtonCradle = new Geometries();
        newtonCradle.add(
                //base:
                new Cylinder(0.5, new Ray(new Point(cradleMinX, cradleMinY, cradleMinZ), new Vector(0, 1, 0)), cradleHeight)
                        .setMaterial(new Material().setKD(0.5).setKS(0.5).setShininess(20))
                        .setEmission(new Color(BLACK)),
                new Cylinder(0.5, new Ray(new Point(cradleTopX, cradleMinY, cradleMinZ), new Vector(0, 1, 0)), cradleHeight)
                        .setMaterial(new Material().setKD(0.5).setKS(0.5).setShininess(20))
                        .setEmission(new Color(BLACK)),
                new Cylinder(0.5, new Ray(new Point(cradleTopX, cradleMinY, cradleTopZ), new Vector(0, 1, 0)), cradleHeight)
                        .setMaterial(new Material().setKD(0.5).setKS(0.5).setShininess(20))
                        .setEmission(new Color(BLACK)),
                new Cylinder(0.5, new Ray(new Point(cradleMinX, cradleMinY, cradleTopZ), new Vector(0, 1, 0)), cradleHeight)
                        .setMaterial(new Material().setKD(0.5).setKS(0.5).setShininess(20))
                        .setEmission(new Color(BLACK)),
                new Cylinder(0.5, new Ray(new Point(cradleTopX, cradleTopY, cradleTopZ), new Vector(0, 0, -1)), cradleLength)
                        .setMaterial(new Material().setKD(0.5).setKS(0.5).setShininess(20))
                        .setEmission(new Color(BLACK)),
                new Cylinder(0.5, new Ray(new Point(cradleMinX, cradleTopY, cradleTopZ), new Vector(0, 0, -1)), cradleLength)
                        .setMaterial(new Material().setKD(0.5).setKS(0.5).setShininess(20))
                        .setEmission(new Color(BLACK)),
                //balls:
                new Sphere(new Point(firstBallX, firstBallY, firstBallZ), 1.5)
                        .setMaterial(new Material().setKD(0.3).setKR(0.7).setShininess(80))
                        .setEmission(new Color(BLACK)),
                new Sphere(new Point(firstBallX, firstBallY, firstBallZ + 3), 1.5)
                        .setMaterial(new Material().setKD(0.3).setKR(0.7).setShininess(80))
                        .setEmission(new Color(BLACK)),
                new Sphere(new Point(firstBallX, firstBallY, firstBallZ + 6), 1.5)
                        .setMaterial(new Material().setKD(0.3).setKR(0.7).setShininess(80))
                        .setEmission(new Color(BLACK)),
                new Sphere(new Point(firstBallX, firstBallY, firstBallZ + 9), 1.5)
                        .setMaterial(new Material().setKD(0.3).setKR(0.7).setShininess(80))
                        .setEmission(new Color(BLACK)),
                new Sphere(new Point(firstBallX, firstBallY + 2, firstBallZ + 18), 1.5)
                        .setMaterial(new Material().setKD(0.3).setKR(0.7).setShininess(80))
                        .setEmission(new Color(BLACK)),
                //strings:
                new Cylinder(0.2, new Ray(new Point(firstBallX, firstBallY, firstBallZ), new Vector(-5, 10, 0)), 11.180339887498949)
                        .setMaterial(new Material().setKD(0.5).setKS(0.5).setShininess(20))
                        .setEmission(new Color(BLACK)),
                new Cylinder(0.2, new Ray(new Point(firstBallX, firstBallY, firstBallZ), new Vector(5, 10, 0)), 11.180339887498949)
                        .setMaterial(new Material().setKD(0.5).setKS(0.5).setShininess(20))
                        .setEmission(new Color(BLACK)),
                new Cylinder(0.2, new Ray(new Point(firstBallX, firstBallY, firstBallZ + 3), new Vector(-5, 10, 0)), 11.180339887498949)
                        .setMaterial(new Material().setKD(0.5).setKS(0.5).setShininess(20))
                        .setEmission(new Color(BLACK)),
                new Cylinder(0.2, new Ray(new Point(firstBallX, firstBallY, firstBallZ + 3), new Vector(5, 10, 0)), 11.180339887498949)
                        .setMaterial(new Material().setKD(0.5).setKS(0.5).setShininess(20))
                        .setEmission(new Color(BLACK)),
                new Cylinder(0.2, new Ray(new Point(firstBallX, firstBallY, firstBallZ + 6), new Vector(-5, 10, 0)), 11.180339887498949)
                        .setMaterial(new Material().setKD(0.5).setKS(0.5).setShininess(20))
                        .setEmission(new Color(BLACK)),
                new Cylinder(0.2, new Ray(new Point(firstBallX, firstBallY, firstBallZ + 6), new Vector(5, 10, 0)), 11.180339887498949)
                        .setMaterial(new Material().setKD(0.5).setKS(0.5).setShininess(20))
                        .setEmission(new Color(BLACK)),
                new Cylinder(0.2, new Ray(new Point(firstBallX, firstBallY, firstBallZ + 9), new Vector(-5, 10, 0)), 11.180339887498949)
                        .setMaterial(new Material().setKD(0.5).setKS(0.5).setShininess(20))
                        .setEmission(new Color(BLACK)),
                new Cylinder(0.2, new Ray(new Point(firstBallX, firstBallY, firstBallZ + 9), new Vector(5, 10, 0)), 11.180339887498949)
                        .setMaterial(new Material().setKD(0.5).setKS(0.5).setShininess(20))
                        .setEmission(new Color(BLACK)),
                new Cylinder(0.2, new Ray(new Point(firstBallX, firstBallY + 2, firstBallZ + 18), new Vector(-5, 8, -6)), 11.180339887498949)
                        .setMaterial(new Material().setKD(0.5).setKS(0.5).setShininess(20))
                        .setEmission(new Color(BLACK)),
                new Cylinder(0.2, new Ray(new Point(firstBallX, firstBallY + 2, firstBallZ + 18), new Vector(5, 8, -6)), 11.180339887498949)
                        .setMaterial(new Material().setKD(0.5).setKS(0.5).setShininess(20))
                        .setEmission(new Color(BLACK))


        );
        double houseTopZ = 600;
        double houseTopY = 200;
        double houseTopX = 120;
        double houseMinZ = -200;
        double houseMinY = -60;
        double houseMinX = -120;
        Geometries house = new Geometries();
        house.add(
                //floor
                new Polygon(new Point(houseMinX, houseMinY, houseTopZ), new Point(houseMinX, houseMinY, houseMinZ), new Point(houseTopX, houseMinY, houseMinZ), new Point(houseTopX, houseMinY, houseTopZ))
                        .setMaterial(new Material().setKD(0.5).setKS(0.5).setShininess(20))
                        .setEmission(new Color(BLACK)),
                //ceiling
                new Polygon(new Point(houseMinX, houseTopY, houseTopZ), new Point(houseMinX, houseTopY, houseMinZ), new Point(houseTopX, houseTopY, houseMinZ), new Point(houseTopX, houseTopY, houseTopZ))
                        .setMaterial(new Material().setKD(0.5).setKS(0.5).setShininess(20))
                        .setEmission(new Color(BLACK)),
                //bottom left wall
                new Polygon(new Point(houseMinX, houseMinY, houseTopZ), new Point(houseMinX, houseMinY, houseMinZ), new Point(houseMinX, 0, houseMinZ), new Point(houseMinX, 0, houseTopZ))
                        .setMaterial(new Material().setKD(0.5).setKS(0.5).setShininess(20))
                        .setEmission(new Color(BLACK)),
                //left left wall
                new Polygon(new Point(houseMinX, 0, houseTopZ), new Point(houseMinX, 0, -80), new Point(houseMinX, 60, -80), new Point(houseMinX, 60, houseTopZ))
                        .setMaterial(new Material().setKD(0.5).setKS(0.5).setShininess(20))
                        .setEmission(new Color(BLACK)),
                //right left wall
                new Polygon(new Point(houseMinX, 0, -140), new Point(houseMinX, 0, houseMinZ), new Point(houseMinX, 60, houseMinZ), new Point(houseMinX, 60, -140))
                        .setMaterial(new Material().setKD(0.5).setKS(0.5).setShininess(20))
                        .setEmission(new Color(BLACK)),
                //top left wall
                new Polygon(new Point(houseMinX, 60, houseTopZ), new Point(houseMinX, 60, houseMinZ), new Point(houseMinX, houseTopY, houseMinZ), new Point(houseMinX, houseTopY, houseTopZ))
                        .setMaterial(new Material().setKD(0.5).setKS(0.5).setShininess(20))
                        .setEmission(new Color(BLACK)),
                //window left wall
                new Polygon(new Point(houseMinX, 0, -80), new Point(houseMinX, 0, -140), new Point(houseMinX, 60, -140), new Point(houseMinX, 60, -80))
                        .setMaterial(new Material().setKD(0.2).setKS(0.2).setShininess(20).setKT(0.6))
                        .setEmission(new Color(BLACK)),
                //middle wall
                new Polygon(new Point(houseMinX, houseMinY, houseMinZ), new Point(houseMinX, houseTopY, houseMinZ), new Point(houseTopX, houseTopY, houseMinZ), new Point(houseTopX, houseMinY, houseMinZ))
                        .setMaterial(new Material().setKD(0.5).setKS(0.5).setShininess(20))
                        .setEmission(new Color(0, 0, 30)),
                new Circle(new Point(0, 20, houseMinZ + 0.1), 30, new Vector(0, 0, 1))
                        .setMaterial(new Material().setKD(0.2).setKS(0.2).setShininess(30).setKR(0.6)),
                new Circle(new Point(0, 20, houseMinZ + 0.1), 31, new Vector(0, 0, 1))
                        .setMaterial(new Material().setKD(0.5).setKS(0.5).setShininess(70))
                        .setEmission(new Color(DARK_GRAY)),
                new Polygon(new Point(houseMinX + 50, houseMinY, houseMinZ + 300), new Point(houseMinX + 50, houseMinY + 150, houseMinZ + 300), new Point(houseMinX + 90, houseMinY + 150, houseMinZ + 300), new Point(houseMinX + 90, houseMinY, houseMinZ + 300))
                        .setMaterial(new Material().setKD(0.2).setKS(0.2).setShininess(20).setKT(0.6)),
                new Polygon(new Point(houseMinX + 100, houseMinY, houseMinZ + 300), new Point(houseMinX + 100, houseMinY + 150, houseMinZ + 300), new Point(houseMinX + 140, houseMinY + 150, houseMinZ + 300), new Point(houseMinX + 140, houseMinY, houseMinZ + 300))
                        .setMaterial(new Material().setKD(0.2).setKS(0.2).setShininess(20).setKT(0.6)
                                .setDiffuseProperties(30, 1, 80)),
                new Polygon(new Point(houseMinX + 150, houseMinY, houseMinZ + 300), new Point(houseMinX + 150, houseMinY + 150, houseMinZ + 300), new Point(houseMinX + 190, houseMinY + 150, houseMinZ + 300), new Point(houseMinX + 190, houseMinY, houseMinZ + 300))
                        .setMaterial(new Material().setKD(0.2).setKS(0.2).setShininess(20).setKT(0.6)
                                .setDiffuseProperties(20, 2, 60))
        );
        scene.geometries.add(
                wineBottle,
                table,
                house,
                new Polygon(new Point(houseMinX - 200, houseMinY, houseTopZ + 400), new Point(houseMinX - 200, houseMinY, houseMinZ - 400), new Point(houseTopX + 200, houseMinY, houseMinZ - 400), new Point(houseTopX + 200, houseMinY, houseTopZ + 400))
                        .setMaterial(new Material().setKD(0.5).setKS(0.5).setShininess(20))
                        .setEmission(new Color(BLACK)),
                newtonCradle

        );

        scene.setAmbientLight(new AmbientLight(new Color(40, 40, 40)));
        scene.lights.add(
                new SpotLight(new Color(200, 200, 200), new Point(50, 50, 30), new Vector(-6, -6, -4))
                        .setKl(4E-5).setKq(2E-7)
        );
        scene.lights.add(
                new SpotLight(new Color(200, 200, 200), new Point(50, -50, 30), new Vector(-70, 30, -40))
                        .setKl(4E-5).setKq(2E-7).setNarrowBeam(10)
        );
        scene.lights.add(
                new DirectionalLight(new Color(510, 510, 360), new Point(-20, -20, -20).subtract(new Point(-120, 40, -100)))
        );
        scene.setSamplingPattern(TargetArea.SamplingPattern.JITTERED);

        cameraBuilder
                .setLocation(new Point(200, 200, 1600))
                .setDirection(new Point(0, 0, -1))
                .setVpDistance(1000).setVpSize(200, 200)
                .setResolution(1000, 1000)
                .setMultithreading(4)
                .setDebugPrint(1)
                .build()
                .renderImage()
                .writeToImage("allEffects-PRO1");
    }
}