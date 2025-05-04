package renderer;

import primitives.Point;
import primitives.Ray;
import primitives.Util;
import primitives.Vector;

import java.util.MissingResourceException;


public class Camera implements Cloneable {
    private Point p0;
    private Vector vUp;
    private Vector vTo;
    private Vector vRight;
    private double width = 0.0;
    private double height = 0.0;
    private double distance = 0.0;

    private Camera() {
    }

    public static Builder getBuilder() {
        return new Builder();
    }

    public Ray constructRay(int nX, int nY, int j, int i) {
        return null;
    }

    public static class Builder {
        private final Camera camera = new Camera();

        public Builder setLocation(Point p) {
            camera.p0 = p;
            return this;
        }

        public Builder setDirection(Vector to, Vector up) {
            if (!Util.isZero(up.dotProduct(to)))
                throw new IllegalArgumentException("Vectors up and to must to be orthogonal");
            camera.vUp = up.normalize();
            camera.vTo = to.normalize();
            return this;
        }

        public Builder setDirection(Point target, Vector up) {
            Vector to = target.subtract(camera.p0);
            camera.vTo = to.normalize();
            Vector right = up.crossProduct(to);
            camera.vRight = right.normalize();
            camera.vUp = to.crossProduct(right).normalize();
            return this;
        }

        public Builder setDirection(Point target) {
            Vector to = target.subtract(camera.p0);
            camera.vTo = to.normalize();
            Vector right = Vector.AXIS_Y.crossProduct(to);
            camera.vRight = right.normalize();
            camera.vUp = to.crossProduct(right).normalize();
            return this;
        }

        public Builder setVpSize(double width, double height) {
            if (Util.alignZero(width) <= 0 || Util.alignZero(height) <= 0)
                throw new IllegalArgumentException("Width and height must be positive");
            camera.width = width;
            camera.height = height;
            return this;
        }

        public Builder setVpDistance(double distance) {
            if (Util.alignZero(distance) <= 0)
                throw new IllegalArgumentException("Distance must be positive");
            camera.distance = distance;
            return this;
        }

        public Builder setResolution(int nX, int nY) {
            return this;
        }

        private static final String MISSING_RENDER_DATA = "Missing rendering data";
        private static final String CAMERA_CLASS_NAME = "Camera";

        private void checkNotNull(Object value, String fieldName) {
            if (value == null) {
                throw new MissingResourceException(MISSING_RENDER_DATA, CAMERA_CLASS_NAME, fieldName);
            }

        }

        private void checkDouble(double value, String fieldName) {
            if (Util.isZero(value)) {
                throw new MissingResourceException(MISSING_RENDER_DATA, CAMERA_CLASS_NAME, fieldName);
            }
            if (value < 0.0) {
                throw new IllegalArgumentException(fieldName + " must be positive");
            }
        }

        public Camera build() {
            checkNotNull(camera.p0, "p0");
            checkNotNull(camera.vUp, "vUp");
            checkNotNull(camera.vTo, "vTo");
            checkDouble(camera.width, "width");
            checkDouble(camera.height, "height");
            checkDouble(camera.distance, "distance");
            if (camera.vRight == null) {
                camera.vRight = camera.vUp.crossProduct(camera.vTo).normalize();
            }
            if (!Util.isZero(camera.vTo.dotProduct(camera.vRight)) || !Util.isZero(camera.vUp.dotProduct(camera.vRight)) || !Util.isZero(camera.vUp.dotProduct(camera.vTo))) {
                throw new IllegalArgumentException("The vectors must be orthogonal");
            }
            if (Util.isZero(camera.vTo.length() - 1) || Util.isZero(camera.vUp.length() - 1) || Util.isZero(camera.vRight.length() - 1)) {
                throw new IllegalArgumentException("The vectors must be normalized");
            }
            try {
                return (Camera) camera.clone();
            }
            catch (CloneNotSupportedException e) {
                throw new AssertionError(e);
            }
        }


    }
}
