package renderer;

import static java.awt.Color.*;

import org.junit.jupiter.api.Test;

import geometries.*;
import lighting.*;
import primitives.*;
import scene.Scene;

/**
 * Tests for reflection and transparency functionality, test for partial
 * shadows
 * (with transparency)
 *
 * @author Dan Zilberstein
 */
class ReflectionRefractionTests {
    /**
     * Default constructor to satisfy JavaDoc generator
     */
    ReflectionRefractionTests() { /* to satisfy JavaDoc generator */ }

    /**
     * Scene for the tests
     */
    private final Scene scene = new Scene("Test scene");
    /**
     * Camera builder for the tests with triangles
     */
    private final Camera.Builder cameraBuilder = Camera.getBuilder()     //
            .setRayTracer(scene, RayTracerType.SIMPLE);

    /**
     * Produce a picture of a sphere lighted by a spotlight
     */
    @Test
    void twoSpheres() {
        scene.geometries.add( //
                new Sphere(new Point(0, 0, -50), 50d).setEmission(new Color(BLUE)) //
                        .setMaterial(new Material().setKD(0.4).setKS(0.3).setShininess(100).setKT(0.3)), //
                new Sphere(new Point(0, 0, -50), 25d).setEmission(new Color(RED)) //
                        .setMaterial(new Material().setKD(0.5).setKS(0.5).setShininess(100))); //
        scene.lights.add( //
                new SpotLight(new Color(1000, 600, 0), new Point(-100, -100, 500), new Vector(-1, -1, -2)) //
                        .setKl(0.0004).setKq(0.0000006));

        cameraBuilder
                .setLocation(new Point(0, 0, 1000)) //
                .setDirection(Point.ZERO, Vector.AXIS_Y) //
                .setVpDistance(1000).setVpSize(150, 150) //
                .setResolution(500, 500) //
                .build() //
                .renderImage() //
                .writeToImage("refractionTwoSpheres");
    }

    /**
     * Produce a picture of a sphere lighted by a spotlight
     */
    @Test
    void twoSpheresOnMirrors() {
        scene.geometries.add( //
                new Sphere(new Point(-950, -900, -1000), 400d).setEmission(new Color(0, 50, 100)) //
                        .setMaterial(new Material().setKD(0.25).setKS(0.25).setShininess(20) //
                                .setKT(new Double3(0.5, 0, 0))), //
                new Sphere(new Point(-950, -900, -1000), 200d).setEmission(new Color(100, 50, 20)) //
                        .setMaterial(new Material().setKD(0.25).setKS(0.25).setShininess(20)), //
                new Triangle(new Point(1500, -1500, -1500), new Point(-1500, 1500, -1500), //
                        new Point(670, 670, 3000)) //
                        .setEmission(new Color(20, 20, 20)) //
                        .setMaterial(new Material().setKR(1)), //
                new Triangle(new Point(1500, -1500, -1500), new Point(-1500, 1500, -1500), //
                        new Point(-1500, -1500, -2000)) //
                        .setEmission(new Color(20, 20, 20)) //
                        .setMaterial(new Material().setKR(new Double3(0.5, 0, 0.4))));
        scene.setAmbientLight(new AmbientLight(new Color(26, 26, 26)));
        scene.lights.add(new SpotLight(new Color(1020, 400, 400), new Point(-750, -750, -150), new Vector(-1, -1, -4)) //
                .setKl(0.00001).setKq(0.000005));

        cameraBuilder
                .setLocation(new Point(0, 0, 10000)) //
                .setDirection(Point.ZERO, Vector.AXIS_Y) //
                .setVpDistance(10000).setVpSize(2500, 2500) //
                .setResolution(500, 500) //
                .build() //
                .renderImage() //
                .writeToImage("reflectionTwoSpheresMirrored");
    }

    /**
     * Produce a picture of two triangles lighted by a spotlight with a
     * partially
     * transparent Sphere producing partial shadow
     */
    @Test
    void trianglesTransparentSphere() {
        scene.geometries.add(
                new Triangle(new Point(-150, -150, -115), new Point(150, -150, -135),
                        new Point(75, 75, -150))
                        .setMaterial(new Material().setKD(0.5).setKS(0.5).setShininess(60)),
                new Triangle(new Point(-150, -150, -115), new Point(-70, 70, -140), new Point(75, 75, -150))
                        .setMaterial(new Material().setKD(0.5).setKS(0.5).setShininess(60)),
                new Sphere(new Point(60, 50, -50), 30d).setEmission(new Color(BLUE))
                        .setMaterial(new Material().setKD(0.2).setKS(0.2).setShininess(30).setKT(0.6)));
        scene.setAmbientLight(new AmbientLight(new Color(38, 38, 38)));
        scene.lights.add(
                new SpotLight(new Color(700, 400, 400), new Point(60, 50, 0), new Vector(0, 0, -1))
                        .setKl(4E-5).setKq(2E-7));

        cameraBuilder
                .setLocation(new Point(0, 0, 1000)) //
                .setDirection(Point.ZERO, Vector.AXIS_Y) //
                .setVpDistance(1000).setVpSize(200, 200) //
                .setResolution(600, 600) //
                .build() //
                .renderImage() //
                .writeToImage("refractionShadow");
    }

    @Test
    void allEffects() {
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
                new Sphere(new Point(firstBallX, firstBallY, firstBallZ+3), 1.5)
                        .setMaterial(new Material().setKD(0.3).setKR(0.7).setShininess(80))
                        .setEmission(new Color(BLACK)),
                new Sphere(new Point(firstBallX, firstBallY, firstBallZ+6), 1.5)
                        .setMaterial(new Material().setKD(0.3).setKR(0.7).setShininess(80))
                        .setEmission(new Color(BLACK)),
                new Sphere(new Point(firstBallX, firstBallY, firstBallZ+9), 1.5)
                        .setMaterial(new Material().setKD(0.3).setKR(0.7).setShininess(80))
                        .setEmission(new Color(BLACK)),
                new Sphere(new Point(firstBallX, firstBallY+2, firstBallZ+18), 1.5)
                        .setMaterial(new Material().setKD(0.3).setKR(0.7).setShininess(80))
                        .setEmission(new Color(BLACK)),
                //strings:
                new Cylinder(0.2, new Ray(new Point(firstBallX, firstBallY, firstBallZ), new Vector(-5, 10, 0)), 11.180339887498949)
                        .setMaterial(new Material().setKD(0.5).setKS(0.5).setShininess(20))
                        .setEmission(new Color(BLACK)),
                new Cylinder(0.2, new Ray(new Point(firstBallX, firstBallY, firstBallZ), new Vector(5, 10, 0)), 11.180339887498949)
                        .setMaterial(new Material().setKD(0.5).setKS(0.5).setShininess(20))
                        .setEmission(new Color(BLACK)),
                new Cylinder(0.2, new Ray(new Point(firstBallX, firstBallY, firstBallZ+3), new Vector(-5, 10, 0)), 11.180339887498949)
                        .setMaterial(new Material().setKD(0.5).setKS(0.5).setShininess(20))
                        .setEmission(new Color(BLACK)),
                new Cylinder(0.2, new Ray(new Point(firstBallX, firstBallY, firstBallZ+3), new Vector(5, 10, 0)), 11.180339887498949)
                        .setMaterial(new Material().setKD(0.5).setKS(0.5).setShininess(20))
                        .setEmission(new Color(BLACK)),
                new Cylinder(0.2, new Ray(new Point(firstBallX, firstBallY, firstBallZ+6), new Vector(-5, 10, 0)), 11.180339887498949)
                        .setMaterial(new Material().setKD(0.5).setKS(0.5).setShininess(20))
                        .setEmission(new Color(BLACK)),
                new Cylinder(0.2, new Ray(new Point(firstBallX, firstBallY, firstBallZ+6), new Vector(5, 10, 0)), 11.180339887498949)
                        .setMaterial(new Material().setKD(0.5).setKS(0.5).setShininess(20))
                        .setEmission(new Color(BLACK)),
                new Cylinder(0.2, new Ray(new Point(firstBallX, firstBallY, firstBallZ+9), new Vector(-5, 10, 0)), 11.180339887498949)
                        .setMaterial(new Material().setKD(0.5).setKS(0.5).setShininess(20))
                        .setEmission(new Color(BLACK)),
                new Cylinder(0.2, new Ray(new Point(firstBallX, firstBallY, firstBallZ+9), new Vector(5, 10, 0)), 11.180339887498949)
                        .setMaterial(new Material().setKD(0.5).setKS(0.5).setShininess(20))
                        .setEmission(new Color(BLACK)),
                new Cylinder(0.2, new Ray(new Point(firstBallX, firstBallY+2, firstBallZ+18), new Vector(-5, 8, -6)), 11.180339887498949)
                        .setMaterial(new Material().setKD(0.5).setKS(0.5).setShininess(20))
                        .setEmission(new Color(BLACK)),
                new Cylinder(0.2, new Ray(new Point(firstBallX, firstBallY+2, firstBallZ+18), new Vector(5, 8, -6)), 11.180339887498949)
                        .setMaterial(new Material().setKD(0.5).setKS(0.5).setShininess(20))
                        .setEmission(new Color(BLACK))


        );
        double houseTopZ = 200;
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
                        .setEmission(new Color(DARK_GRAY))
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

        cameraBuilder
                .setLocation(new Point(400, 300, 1000)) //
                .setDirection(new Point(0, 0, -20)) //
                .setVpDistance(1000).setVpSize(200, 200) //
                .setResolution(1000, 1000) //
                .build() //
                .renderImage() //
                .writeToImage("allEffects");
    }
}