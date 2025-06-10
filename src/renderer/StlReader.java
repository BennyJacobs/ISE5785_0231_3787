package renderer;

import geometries.Triangle;
import primitives.Point;

import java.io.*;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.ArrayList;
import java.util.List;

/**
 * STL File Reader and Converter
 * Converts STL files to lists of triangular polygons
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
                        triangles.add(new Triangle(vertices[0], vertices[1], vertices[2]));
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
                System.out.println("Triangle: " + triangles.get(i) + " Normal: " + normal + " v1: " + v1 + " v2: " + v2 + " v3: " + v3);
                }
                catch (Exception e) {
                    System.out.println("Error reading triangle " + i);
                    System.out.println("Normal: " + normal);
                    System.out.println("v1: " + v1);
                    System.out.println("v2: " + v2);
                    System.out.println("v3: " + v3);
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

  }