package renderer;

import primitives.*;
import scene.Scene;

import java.util.LinkedList;
import java.util.List;
import java.util.MissingResourceException;

import java.util.stream.*;

/**
 * Represents a camera in 3D space used for rendering scenes.
 * The camera is defined by its position and orientation (vTo, vUp, vRight),
 * along with its view plane size and distance.
 */
public class Camera implements Cloneable {
    /**
     * The location (position) of the camera in 3D space.
     */
    private Point p0;

    /**
     * The vector pointing upward relative to the camera.
     */
    private Vector vUp;

    /**
     * The vector pointing forward from the camera (toward the view plane).
     */
    private Vector vTo;

    /**
     * The vector pointing to the right relative to the camera.
     * This is perpendicular to both {@code vUp} and {@code vTo}.
     */
    private Vector vRight;

    /**
     * The width of the view plane.
     */
    private double width = 0.0;

    /**
     * The height of the view plane.
     */
    private double height = 0.0;

    /**
     * The distance from the camera to the view plane.
     */
    private double distance = 0.0;

    /**
     * The image writer responsible for writing the rendered image to file.
     */
    private ImageWriter imageWriter;

    /**
     * The ray tracer used to trace rays through the scene.
     */
    private RayTracerBase rayTracer;

    /**
     * Number of pixels in width (X-axis resolution).
     */
    private int nX = 1;

    /**
     * Number of pixels in height (Y-axis resolution).
     */
    private int nY = 1;

    /**
     * Number of super sampling rays for antialiasing algorithm.
     */
    private int numOfRaysAA = 1;

    /**
     * Number of super sampling rays for depth of field algorithm.
     */
    private int numOfRaysDOF = 1;

    /**
     * Camera's aperture window which depends on camera's aperture.
     */
    private TargetArea apertureWindow;

    /**
     * Focal plane distance from camera
     */
    private double distanceFocalPlane;

    /** Number of threads to use fore rendering image by the camera */
    private int threadsCount = 0;

    /**
     * Number of threads to spare for Java VM threads:<br>
     * Spare threads if trying to use all the cores
     */
    private static final int SPARE_THREADS = 2;

    /**
     * Debug a print interval in seconds (for progress percentage)<br>
     * if it is zero - there is no progress output
     */
    private double printInterval = 0;

    /**
     * Pixel manager for supporting:
     * <ul>
     * <li>multi-threading</li>
     * <li>debug print of progress percentage in Console window/tab</li>
     * </ul>
     */
    private PixelManager pixelManager;

    /**
     * Private constructor. Use {@link Builder} to construct a Camera instance.
     */
    private Camera() {
    }

    /**
     * Defines the sampling pattern used for generating points on a target area in the camera view plane.
     */
    private TargetArea.SamplingPattern samplingPattern = TargetArea.SamplingPattern.GRID;


    /**
     * Returns a new builder for constructing a {@link Camera}.
     *
     * @return a new Camera builder
     */
    public static Builder getBuilder() {
        return new Builder();
    }

    /**
     * Render image using multi-threading by parallel streaming
     * @return the camera object itself
     */
    private Camera renderImageStream() {
        IntStream.range(0, nY).parallel()
                .forEach(i -> IntStream.range(0, nX).parallel()
                        .forEach(j -> castRay(j, i)));
        return this;
    }

    /**
     * Render image without multi-threading
     * @return the camera object itself
     */
    private Camera renderImageNoThreads() {
        for (int i = 0; i < nY; ++i)
            for (int j = 0; j < nX; ++j)
                castRay(j, i);
        return this;
    }

    /**
     * Render image using multi-threading by creating and running raw threads
     * @return the camera object itself
     */
    private Camera renderImageRawThreads() {
        var threads = new LinkedList<Thread>();
        while (threadsCount-- > 0)
            threads.add(new Thread(() -> {
                PixelManager.Pixel pixel;
                while ((pixel = pixelManager.nextPixel()) != null)
                    castRay(pixel.col(), pixel.row());
            }));
        for (var thread : threads) thread.start();
        try {
            for (var thread : threads) thread.join();
        } catch (InterruptedException ignored) {}
        return this;
    }

    /**
     * Constructs rays through a specific pixel on the view plane.
     *
     * @param nX number of pixels in the X axis (width)
     * @param nY number of pixels in the Y axis (height)
     * @param j  column index (X)
     * @param i  row index (Y)
     * @return the constructed ray
     */
    public Ray constructRay(int nX, int nY, int j, int i) {
        Point pC = p0.add(vTo.scale(distance));
        double rY = height / nY;
        double rX = width / nX;
        double yI = -(i - 0.5 * (nY - 1)) * rY;
        double xJ = (j - 0.5 * (nX - 1)) * rX;
        Point pIJ = pC;
        if (!Util.isZero(xJ))
            pIJ = pIJ.add(vRight.scale(xJ));
        if (!Util.isZero(yI))
            pIJ = pIJ.add(vUp.scale(yI));
        return new Ray(p0, pIJ.subtract(p0));
    }

    /**
     * Constructs a beam of rays through a specific pixel on the view plane.
     *
     * @param nX the number of pixels along the X-axis (image width)
     * @param nY the number of pixels along the Y-axis (image height)
     * @param j the column index of the pixel (X-axis)
     * @param i the row index of the pixel (Y-axis)
     * @return a list of rays representing the beam through the specified pixel
     */
    public List<Ray> constructBeam(int nX, int nY, int j, int i) {
        Point pC = p0.add(vTo.scale(distance));
        double rY = height / nY;
        double rX = width / nX;
        double yI = -(i - 0.5 * (nY - 1)) * rY;
        double xJ = (j - 0.5 * (nX - 1)) * rX;
        Point pIJ = pC;
        if (!Util.isZero(xJ))
            pIJ = pIJ.add(vRight.scale(xJ));
        if (!Util.isZero(yI))
            pIJ = pIJ.add(vUp.scale(yI));
        Ray mainRay = new Ray(p0, pIJ.subtract(p0));
        if (numOfRaysAA == 1)
            return List.of(mainRay);
        QuadrilateralTargetArea targetArea = new QuadrilateralTargetArea(rY, rX, vRight, vTo, pIJ, numOfRaysAA, samplingPattern);
        return mainRay.createBeam(targetArea);
    }

    /** This function renders an image's pixel color map from the scene
     * included in the ray tracer object
     * @return the camera object itself
     */
    public Camera renderImage() {
        pixelManager = new PixelManager(nY, nX, printInterval);
        return switch (threadsCount) {
            case 0 -> renderImageNoThreads();
            case -1 -> renderImageStream();
            default -> renderImageRawThreads();
        };
    }

    /**
     * Prints a grid over the rendered image using the specified interval and color.
     *
     * @param interval the interval between grid lines
     * @param color    the color of the grid lines
     * @return this camera
     */
    public Camera printGrid(int interval, Color color) {
        for (int y = 0; y < nY; y += interval) {
            for (int x = 0; x < nX; x++) {
                imageWriter.writePixel(x, y, color);
            }
        }
        for (int y = 0; y < nY; y++) {
            for (int x = 0; x < nX; x += interval) {
                imageWriter.writePixel(x, y, color);
            }
        }
        return this;
    }

    /**
     * Writes the rendered image to a file with the given name.
     *
     * @param name the file name to write the image to
     * @return this camera
     */
    public Camera writeToImage(String name) {
        imageWriter.writeToImage(name);
        return this;
    }

    /**
     * Casts a single ray through a specific pixel (i, j).
     * This method is intended to be used internally during image rendering.
     *
     * @param j the pixel's column index
     * @param i the pixel's row index
     */
    private void castRay(int j, int i) {
        List<Ray> beamRays = constructBeam(nX, nY, j, i);
        Color pixelColor = Color.BLACK;
        for (Ray ray : beamRays) {
            if(apertureWindow != null && numOfRaysDOF > 1) {
                List<Ray> beamRaysDOF = ray.createBeamReverse(apertureWindow, distanceFocalPlane);
                Color rayColor = Color.BLACK;
                for (Ray rayDOF : beamRaysDOF)
                    rayColor = rayColor.add(rayTracer.traceRay(rayDOF));
                rayColor = rayColor.reduce(beamRaysDOF.size());
                pixelColor = pixelColor.add(rayColor);
            }

            else
                pixelColor = pixelColor.add(rayTracer.traceRay(ray));
        }
        imageWriter.writePixel(j, i, pixelColor.reduce(beamRays.size()));
        pixelManager.pixelDone();
    }

    /**
     * Builder class for constructing a {@link Camera} instance using the builder pattern.
     */
    public static class Builder {
        /**
         * Camera object for the builder
         */
        private final Camera camera = new Camera();

        /**
         * Sets the location (position) of the camera.
         *
         * @param p the camera position
         * @return this builder
         */
        public Builder setLocation(Point p) {
            camera.p0 = p;
            return this;
        }

        /**
         * Sets the orientation of the camera using "to" and "up" vectors.
         *
         * @param to the forward direction vector
         * @param up the upward direction vector
         * @return this builder
         * @throws IllegalArgumentException if the vectors are not orthogonal
         */
        public Builder setDirection(Vector to, Vector up) {
            if (!Util.isZero(up.dotProduct(to)))
                throw new IllegalArgumentException("Vectors up and to must to be orthogonal");
            camera.vUp = up.normalize();
            camera.vTo = to.normalize();
            return this;
        }

        /**
         * Sets the camera direction to look at a specific target point with a given up vector.
         *
         * @param target the point to look at
         * @param up     the upward direction vector
         * @return this builder
         */
        public Builder setDirection(Point target, Vector up) {
            camera.vTo = target.subtract(camera.p0);
            camera.vRight = camera.vTo.crossProduct(up);
            camera.vUp = camera.vRight.crossProduct(camera.vTo);
            camera.vTo = camera.vTo.normalize();
            camera.vUp = camera.vUp.normalize();
            camera.vRight = camera.vRight.normalize();
            return this;
        }

        /**
         * Sets the camera direction to look at a target point with a default up vector (Y-axis).
         *
         * @param target the point to look at
         * @return this builder
         */
        public Builder setDirection(Point target) {
            return setDirection(target, Vector.AXIS_Y);
        }

        /**
         * Sets the size of the view plane.
         *
         * @param width  the width of the view plane
         * @param height the height of the view plane
         * @return this builder
         * @throws IllegalArgumentException if width or height are not positive
         */
        public Builder setVpSize(double width, double height) {
            if (Util.alignZero(width) <= 0 || Util.alignZero(height) <= 0)
                throw new IllegalArgumentException("Width and height must be positive");
            camera.width = width;
            camera.height = height;
            return this;
        }

        /**
         * Sets the distance from the camera to the view plane.
         *
         * @param distance the distance value
         * @return this builder
         * @throws IllegalArgumentException if distance is not positive
         */
        public Builder setVpDistance(double distance) {
            if (Util.alignZero(distance) <= 0)
                throw new IllegalArgumentException("Distance must be positive");
            camera.distance = distance;
            return this;
        }

        /**
         * Sets the distance from the camera to the focal plane.
         *
         * @param distanceFocalPlane the distance value
         * @return this builder
         * @throws IllegalArgumentException if distance is not positive
         */
        public Builder setDistanceFocalPlane(double distanceFocalPlane) {
            if (Util.alignZero(distanceFocalPlane) <= 0)
                throw new IllegalArgumentException("Distance must be positive");
            camera.distanceFocalPlane = distanceFocalPlane;
            return this;
        }

        /**
         * Sets the resolution of the view plane.
         *
         * @param nX number of pixels in width
         * @param nY number of pixels in height
         * @return this builder
         */
        public Builder setResolution(int nX, int nY) {
            camera.nX = nX;
            camera.nY = nY;
            return this;
        }

        /**
         * Sets the ray tracer to be used by the camera for rendering.
         *
         * @param scene         the scene to be rendered
         * @param rayTracerType the type of ray tracer to use
         * @return this builder
         */
        public Builder setRayTracer(Scene scene, RayTracerType rayTracerType) {
            if (rayTracerType == RayTracerType.SIMPLE)
                camera.rayTracer = new SimpleRayTracer(scene);
            else
                camera.rayTracer = null;
            return this;
        }

        /**
         * Sets the number of super sampling rays for antialiasing algorithm
         *
         * @param numOfRaysAA number of super sampling rays for antialiasing algorithm
         * @return this builder
         * @throws IllegalArgumentException if numOfRays is smaller than 1
         */
        public Builder setNumOfRaysAA(int numOfRaysAA) {
            if (numOfRaysAA < 1)
                throw new IllegalArgumentException("Number of super sampling rays must not be smaller than 1");
            camera.numOfRaysAA = numOfRaysAA;
            return this;
        }

        /**
         * Sets the number of super sampling rays for depth of field algorithm
         *
         * @param numOfRaysDOF number of super sampling rays for depth of field algorithm
         * @return this builder
         * @throws IllegalArgumentException if numOfRays is smaller than 1
         */
        public Builder setNumOfRaysDOF(int numOfRaysDOF) {
            if (numOfRaysDOF < 1)
                throw new IllegalArgumentException("Number of super sampling rays must not be smaller than 1");
            camera.numOfRaysDOF = numOfRaysDOF;
            return this;
        }

        /**
         * Sets the number of super sampling rays for depth of field algorithm
         *
         * @param height the height of the aperture window
         * @param width the width of the aperture window
         * @return this builder
         * @throws IllegalArgumentException if parameters are negative
         * @throws IllegalArgumentException if necessary components for aperture window are not initialized yet
         */
        public Builder setApertureWindow(double height, double width) {
            if (camera.vRight == null || camera.vTo == null || camera.p0 == null)
                throw new IllegalArgumentException("Must initialize other components of the camera before setting aperture window");
            if (height <= 0 || width <= 0)
                throw new IllegalArgumentException("Height and width must not be negative");
            camera.apertureWindow = new QuadrilateralTargetArea(height, width, camera.vRight, camera.vTo, camera.p0, camera.numOfRaysDOF, camera.samplingPattern);
            return this;
        }

        /**
         * Set multi-threading <br>
         * Parameter value meaning:
         * <ul>
         * <li>-2 - number of threads is number of logical processors less 2</li>
         * <li>-1 - stream processing parallelization (implicit multi-threading) is used</li>
         * <li>0 - multi-threading is not activated</li>
         * <li>1 and more - literally number of threads</li>
         * </ul>
         * @param threads number of threads
         * @return builder object itself
         */
        public Builder setMultithreading(int threads) {
            if (threads < -3)
                throw new IllegalArgumentException("Multithreading parameter must be -2 or higher");
            if (threads == -2) {
                int cores = Runtime.getRuntime().availableProcessors() - SPARE_THREADS;
                camera.threadsCount = cores <= 2 ? 1 : cores;
            } else
                camera.threadsCount = threads;
            return this;
        }

        /**
         * Set debug printing interval. If it's zero - there won't be printing at all
         * @param interval printing interval in %
         * @return builder object itself
         */
        public Builder setDebugPrint(double interval) {
            if (interval < 0) throw new IllegalArgumentException("interval parameter must be non-negative");
            camera.printInterval = interval;
            return this;
        }

        /**
         * Sets the sampling pattern for the target area.
         *
         * @param pattern the sampling pattern to use, such as RANDOM, GRID, or JITTERED
         * @return this builder instance
         */
        public Builder setSamplingPattern(TargetArea.SamplingPattern pattern) {
            camera.samplingPattern = pattern;
            return this;
        }

        /**
         * Constants representing error message
         */
        private static final String MISSING_RENDER_DATA = "Missing rendering data";
        private static final String CAMERA_CLASS_NAME = "Camera";

        /**
         * Validates that a required field is not null.
         *
         * @param value     the value to check
         * @param fieldName the name of the field
         * @throws MissingResourceException if the value is null
         */
        private void checkNotNull(Object value, String fieldName) {
            if (value == null) {
                throw new MissingResourceException(MISSING_RENDER_DATA, CAMERA_CLASS_NAME, fieldName);
            }
        }

        /**
         * Validates that a double field is positive and not zero.
         *
         * @param value     the value to check
         * @param fieldName the name of the field
         * @throws MissingResourceException if the value is zero
         * @throws IllegalArgumentException if the value is negative
         */
        private void checkDouble(double value, String fieldName) {
            if (value == 0) {
                throw new MissingResourceException(MISSING_RENDER_DATA, CAMERA_CLASS_NAME, fieldName);
            }
            if (Util.alignZero(value) <= 0.0) {
                throw new IllegalArgumentException(fieldName + " must be positive");
            }
        }

        /**
         * Builds and returns a fully configured {@link Camera} instance.
         * Validates all necessary parameters before building.
         *
         * @return a clone of the configured camera
         * @throws MissingResourceException if required fields are missing
         * @throws IllegalArgumentException if vectors are not orthogonal or not normalized
         * @throws AssertionError           if cloning fails (should not occur)
         */
        public Camera build() {
            checkNotNull(camera.p0, "p0");
            checkNotNull(camera.vUp, "vUp");
            checkNotNull(camera.vTo, "vTo");
            checkDouble(camera.width, "width");
            checkDouble(camera.height, "height");
            checkDouble(camera.distance, "distance");
            checkDouble(camera.distanceFocalPlane, "distance");

            if (camera.vRight == null) {
                camera.vRight = camera.vTo.crossProduct(camera.vUp);
                camera.vRight = camera.vRight.normalize();
                checkNotNull(camera.vRight, "vRight");
            }

            if (!Util.isZero(camera.vTo.dotProduct(camera.vRight))
                    || !Util.isZero(camera.vUp.dotProduct(camera.vRight))
                    || !Util.isZero(camera.vUp.dotProduct(camera.vTo))) {
                throw new IllegalArgumentException("The vectors must be orthogonal");
            }

            if (!Util.isZero(camera.vTo.length() - 1)
                    || !Util.isZero(camera.vUp.length() - 1)
                    || !Util.isZero(camera.vRight.length() - 1)) {
                throw new IllegalArgumentException("The vectors must be normalized");
            }

            if (camera.nX <= 0 || camera.nY <= 0)
                throw new IllegalArgumentException("Nx and Ny must be positive");

            if (camera.threadsCount < -1)
                throw new IllegalArgumentException("Threads count must be -1 or higher");

            if (camera.printInterval < 0)
                throw new IllegalArgumentException("Print interval must be non-negative");

            if (camera.numOfRaysAA < 1)
                throw new IllegalArgumentException("Number of super sampling rays must not be smaller than 1");

            if (camera.numOfRaysDOF < 1)
                throw new IllegalArgumentException("Number of super sampling rays must not be smaller than 1");

            camera.imageWriter = new ImageWriter(camera.nX, camera.nY);

            if (camera.rayTracer == null)
                camera.rayTracer = new SimpleRayTracer(null);

            try {
                return (Camera) camera.clone();
            } catch (CloneNotSupportedException e) {
                throw new AssertionError(e);
            }
        }
    }
}
