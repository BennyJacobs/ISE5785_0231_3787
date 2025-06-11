package renderer;

import geometries.Triangle;
import primitives.Point;
import primitives.Vector;

import java.io.*;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.ArrayList;
import java.util.List;

/**
 * STL File Reader and Converter
 * Converts STL files to lists of triangular polygons with positioning capabilities
 */
public class StlReader {

    /**
     * Reads an ASCII STL file and returns list of triangles
     */
    public static List<Triangle> readASCIISTL(String filename) throws IOException {
        List<Triangle> triangles = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(filename))) {
            String line;
            Point normal = null;
            Point[] vertices = new Point[3];
            int vertexIndex = 0;

            while ((line = reader.readLine()) != null) {
                line = line.trim();

                if (line.startsWith("facet normal")) {
                    String[] parts = line.split("\\s+");
                    normal = new Point(
                            Double.parseDouble(parts[2]),
                            Double.parseDouble(parts[3]),
                            Double.parseDouble(parts[4])
                    );
                    vertexIndex = 0;
                } else if (line.startsWith("vertex")) {
                    String[] parts = line.split("\\s+");
                    vertices[vertexIndex++] = new Point(
                            Double.parseDouble(parts[1]),
                            Double.parseDouble(parts[2]),
                            Double.parseDouble(parts[3])
                    );
                } else if (line.equals("endfacet")) {
                    if (normal != null && vertexIndex == 3) {
                        try {
                            triangles.add(new Triangle(vertices[0], vertices[1], vertices[2]));
                        }
                        catch (Exception e) {
                            continue;
                        }
                    }
                }
            }
        }

        return triangles;
    }

    /**
     * Reads a Binary STL file and returns list of triangles
     */
    public static List<Triangle> readBinarySTL(String filename) throws IOException {
        List<Triangle> triangles = new ArrayList<>();

        try (FileInputStream fis = new FileInputStream(filename);
             BufferedInputStream bis = new BufferedInputStream(fis)) {

            // Skip 80-byte header
            bis.skip(80);

            // Read number of triangles (4 bytes, little-endian)
            byte[] countBytes = new byte[4];
            bis.read(countBytes);
            int triangleCount = ByteBuffer.wrap(countBytes).order(ByteOrder.LITTLE_ENDIAN).getInt();

            // Read each triangle (50 bytes each)
            for (int i = 0; i < triangleCount; i++) {
                byte[] triangleData = new byte[50];
                bis.read(triangleData);

                ByteBuffer buffer = ByteBuffer.wrap(triangleData).order(ByteOrder.LITTLE_ENDIAN);

                // Normal vector (3 floats)
                Point normal = new Point(
                        buffer.getFloat(),
                        buffer.getFloat(),
                        buffer.getFloat()
                );

                // Three vertices (9 floats total)
                Point v1 = new Point(buffer.getFloat(), buffer.getFloat(), buffer.getFloat());
                Point v2 = new Point(buffer.getFloat(), buffer.getFloat(), buffer.getFloat());
                Point v3 = new Point(buffer.getFloat(), buffer.getFloat(), buffer.getFloat());

                // Skip attribute byte count (2 bytes)
                buffer.getShort();
                try {
                    triangles.add(new Triangle(v1, v2, v3));
                }
                catch (Exception e) {
                    continue;
                }
            }
        }

        return triangles;
    }

    /**
     * Automatically detects STL format and reads the file
     */
    public static List<Triangle> readSTL(String filename) throws IOException {
        // Try to detect if file is ASCII or Binary
        try (FileInputStream fis = new FileInputStream(filename)) {
            byte[] header = new byte[80];
            fis.read(header);
            String headerStr = new String(header).toLowerCase();

            // ASCII STL files typically start with "solid"
            if (headerStr.trim().startsWith("solid")) {
                return readASCIISTL(filename);
            } else {
                return readBinarySTL(filename);
            }
        }
    }

    /**
     * Reads STL file and positions it around a specific point
     * @param filename STL file path
     * @param targetCenter The point around which to position the object
     * @return List of triangles positioned around the target center
     */
    public static List<Triangle> readSTLPositioned(String filename, Point targetCenter) throws IOException {
        List<Triangle> triangles = readSTL(filename);
        return positionTriangles(triangles, targetCenter);
    }

    /**
     * Reads STL file and positions it around origin (0,0,0)
     * @param filename STL file path
     * @return List of triangles centered around origin
     */
    public static List<Triangle> readSTLCentered(String filename) throws IOException {
        return readSTLPositioned(filename, new Point(0, 0, 0));
    }

    /**
     * Positions a list of triangles around a target center point
     * @param triangles Original list of triangles
     * @param targetCenter The point around which to position the object
     * @return New list of triangles positioned around the target center
     */
    public static List<Triangle> positionTriangles(List<Triangle> triangles, Point targetCenter) {
        if (triangles.isEmpty()) {
            return triangles;
        }

        // Calculate current centroid of the object
        Point currentCenter = calculateCentroid(triangles);

        // Calculate translation vector
        Vector translation = targetCenter.subtract(currentCenter);

        // Apply translation to all triangles
        List<Triangle> positionedTriangles = new ArrayList<>();
        for (Triangle triangle : triangles) {
            try {
                Point v1 = triangle.getVertices().get(0).add(translation);
                Point v2 = triangle.getVertices().get(1).add(translation);
                Point v3 = triangle.getVertices().get(2).add(translation);
                positionedTriangles.add(new Triangle(v1, v2, v3));
            } catch (Exception e) {
                // Skip invalid triangles
                continue;
            }
        }

        return positionedTriangles;
    }

    /**
     * Calculates the centroid (center of mass) of a collection of triangles
     * @param triangles List of triangles
     * @return The centroid point
     */
    public static Point calculateCentroid(List<Triangle> triangles) {
        if (triangles.isEmpty()) {
            return new Point(0, 0, 0);
        }

        double totalX = 0, totalY = 0, totalZ = 0;
        int vertexCount = 0;

        for (Triangle triangle : triangles) {
            List<Point> vertices = triangle.getVertices();
            for (Point vertex : vertices) {
                totalX += vertex.getX();
                totalY += vertex.getY();
                totalZ += vertex.getZ();
                vertexCount++;
            }
        }

        return new Point(
                totalX / vertexCount,
                totalY / vertexCount,
                totalZ / vertexCount
        );
    }

    /**
     * Calculates the bounding box center of a collection of triangles
     * @param triangles List of triangles
     * @return The bounding box center point
     */
    public static Point calculateBoundingBoxCenter(List<Triangle> triangles) {
        if (triangles.isEmpty()) {
            return new Point(0, 0, 0);
        }

        double minX = Double.MAX_VALUE, minY = Double.MAX_VALUE, minZ = Double.MAX_VALUE;
        double maxX = Double.MIN_VALUE, maxY = Double.MIN_VALUE, maxZ = Double.MIN_VALUE;

        for (Triangle triangle : triangles) {
            List<Point> vertices = triangle.getVertices();
            for (Point vertex : vertices) {
                minX = Math.min(minX, vertex.getX());
                minY = Math.min(minY, vertex.getY());
                minZ = Math.min(minZ, vertex.getZ());
                maxX = Math.max(maxX, vertex.getX());
                maxY = Math.max(maxY, vertex.getY());
                maxZ = Math.max(maxZ, vertex.getZ());
            }
        }

        return new Point(
                (minX + maxX) / 2,
                (minY + maxY) / 2,
                (minZ + maxZ) / 2
        );
    }

    /**
     * Positions triangles using bounding box center instead of centroid
     * @param triangles Original list of triangles
     * @param targetCenter The point around which to position the object
     * @return New list of triangles positioned around the target center
     */
    public static List<Triangle> positionTrianglesByBoundingBox(List<Triangle> triangles, Point targetCenter) {
        if (triangles.isEmpty()) {
            return triangles;
        }

        // Calculate current bounding box center
        Point currentCenter = calculateBoundingBoxCenter(triangles);

        // Calculate translation vector
        Vector translation = targetCenter.subtract(currentCenter);

        // Apply translation to all triangles
        List<Triangle> positionedTriangles = new ArrayList<>();
        for (Triangle triangle : triangles) {
            try {
                Point v1 = triangle.getVertices().get(0).add(translation);
                Point v2 = triangle.getVertices().get(1).add(translation);
                Point v3 = triangle.getVertices().get(2).add(translation);
                positionedTriangles.add(new Triangle(v1, v2, v3));
            } catch (Exception e) {
                // Skip invalid triangles
                continue;
            }
        }

        return positionedTriangles;
    }

    /**
     * Scales triangles around a center point
     * @param triangles Original list of triangles
     * @param scale Scale factor (1.0 = no change, 2.0 = double size, 0.5 = half size)
     * @param center Center point for scaling
     * @return New list of scaled triangles
     */
    public static List<Triangle> scaleTriangles(List<Triangle> triangles, double scale, Point center) {
        List<Triangle> scaledTriangles = new ArrayList<>();

        for (Triangle triangle : triangles) {
            try {
                List<Point> vertices = triangle.getVertices();
                Point v1 = scalePointAroundCenter(vertices.get(0), center, scale);
                Point v2 = scalePointAroundCenter(vertices.get(1), center, scale);
                Point v3 = scalePointAroundCenter(vertices.get(2), center, scale);
                scaledTriangles.add(new Triangle(v1, v2, v3));
            } catch (Exception e) {
                // Skip invalid triangles
                continue;
            }
        }

        return scaledTriangles;
    }

    /**
     * Scales a point around a center point
     */
    private static Point scalePointAroundCenter(Point point, Point center, double scale) {
        Vector translated = point.subtract(center);
        Vector scaled = translated.scale(scale);
        return center.add(scaled);
    }

    /**
     * Gets the bounding box dimensions of the triangles
     * @param triangles List of triangles
     * @return Array with [width, height, depth]
     */
    public static double[] getBoundingBoxDimensions(List<Triangle> triangles) {
        if (triangles.isEmpty()) {
            return new double[]{0, 0, 0};
        }

        double minX = Double.MAX_VALUE, minY = Double.MAX_VALUE, minZ = Double.MAX_VALUE;
        double maxX = Double.MIN_VALUE, maxY = Double.MIN_VALUE, maxZ = Double.MIN_VALUE;

        for (Triangle triangle : triangles) {
            List<Point> vertices = triangle.getVertices();
            for (Point vertex : vertices) {
                minX = Math.min(minX, vertex.getX());
                minY = Math.min(minY, vertex.getY());
                minZ = Math.min(minZ, vertex.getZ());
                maxX = Math.max(maxX, vertex.getX());
                maxY = Math.max(maxY, vertex.getY());
                maxZ = Math.max(maxZ, vertex.getZ());
            }
        }

        return new double[]{
                maxX - minX,  // width
                maxY - minY,  // height
                maxZ - minZ   // depth
        };
    }
}