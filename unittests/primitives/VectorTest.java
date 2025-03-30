package primitives;

import org.junit.jupiter.api.Test;

import static java.lang.System.out;
import static org.junit.jupiter.api.Assertions.*;

class VectorTest {
    Vector v1 = new Vector(1, 2, 3);
    Vector v2 = new Vector(4, 5, 6);

    /**
     * Test method for {@link Vector#Vector(Double3)} and {@link Vector#Vector(double, double, double)}
     */
    @Test
    void constructorTest() {
        // ============ Equivalence Partitions Tests ==============
        assertDoesNotThrow(() -> new Vector(3, 4, 5), "ERROR: constructor() Failed constructing a correct Vector");
        assertDoesNotThrow(() -> new Vector(new Double3(1, 2, 3)), "ERROR: constructor() Failed constructing a correct Vector");
        // =============== Boundary Values Tests ==================
        assertThrows(IllegalArgumentException.class, () -> new Vector(0, 0, 0), "ERROR: constructor() zero vector is not allowed or wrong exception has been thrown");
        assertThrows(IllegalArgumentException.class, () -> new Vector(Double3.ZERO), "ERROR: constructor() zero vector is not allowed or wrong exception has been thrown");
    }

    /**
     * Test method for {@link Vector#add(Vector)}
     */
    @Test
    void add() {
        // ============ Equivalence Partitions Tests ==============
        Vector vResult = new Vector(5, 7, 9);
        assertEquals(vResult, v1.add(v2), "ERROR: add() wrong result");

        // =============== Boundary Values Tests ==================
        Vector v1Opposite = new Vector(-1, -2, -3);
        assertThrows(IllegalArgumentException.class, () -> v1.add(v1Opposite), "ERROR: add() mustn't return the 0 vector or wrong exception has been thrown");
    }

    /**
     * Test method for {@link Point#subtract(Point)} but for Vector
     */
    @Test
    void subtract() {
        // ============ Equivalence Partitions Tests ==============
        Vector vResult = new Vector(-3, -3, -3);
        assertEquals(vResult, v1.subtract(v2), "ERROR: subtract() wrong result");

        // =============== Boundary Values Tests ==================
        assertThrows(IllegalArgumentException.class, () -> v1.subtract(v1), "ERROR: subtract() mustn't return the 0 vector or wrong exception has been thrown");
    }

    /**
     * Test method for {@link Vector#scale(double)}
     */
    @Test
    void scale() {
        // ============ Equivalence Partitions Tests ==============;
        Vector vResult = new Vector(2.5, 5, 7.5);
        assertEquals(vResult, v1.scale(2.5), "ERROR: scale() wrong result");

        // =============== Boundary Values Tests ==================
        assertThrows(IllegalArgumentException.class, () -> v1.scale(0), "ERROR: scale() mustn't return the 0 vector or wrong exception has been thrown");
    }

    /**
     * Test method for {@link Vector#dotProduct(Vector)}
     */
    @Test
    void dotProduct() {
        // ============ Equivalence Partitions Tests ==============;
        double result = 32;
        assertEquals(result, v1.dotProduct(v2), "ERROR: dotProduct() wrong result");

        // =============== Boundary Values Tests ==================
        Vector v1Orthogonal = new Vector(4, -2, 0);
        assertEquals(0, v1.dotProduct(v1Orthogonal), "ERROR: dotProduct() for orthogonal vectors is not zero");
    }

    /**
     * Test method for {@link Vector#crossProduct(Vector)}
     */
    @Test
    void crossProduct() {
        // ============ Equivalence Partitions Tests ==============;
        Vector vResult = new Vector(-3, 6, -3);
        assertEquals(vResult, v1.crossProduct(v2), "ERROR: crossProduct() wrong result");

        // =============== Boundary Values Tests ==================
        Vector v1Parallel = new Vector(2, 4, 6);
        assertThrows(IllegalArgumentException.class, () -> v1.crossProduct(v1Parallel), "ERROR: crossProduct() mustn't return the 0 vector or wrong exception has been thrown");
    }

    /**
     * Test method for {@link Vector#lengthSquared()}
     */
    @Test
    void lengthSquared() {
        // ============ Equivalence Partitions Tests ==============;
        double v1LengthSquared = 14;
        assertEquals(v1LengthSquared, v1.lengthSquared(), "ERROR: lengthSquared() wrong result");
    }

    /**
     * Test method for {@link Vector#length()}
     */
    @Test
    void length() {
        // ============ Equivalence Partitions Tests ==============;
        double v1Length = Math.sqrt(14);
        double accuracy = 0.0001;
        assertEquals(v1Length, v1.length(), accuracy, "ERROR: length() wrong result");
    }

    /**
     * Test method for {@link Vector#normalize()}
     */
    @Test
    void normalize() {
        // ============ Equivalence Partitions Tests ==============;
        Vector v1normalized = v1.scale(Math.sqrt((double) 1 /14));
        assertEquals(v1normalized, v1.normalize(), "ERROR: normalize() wrong result");

        Vector normalized = new Vector(1, 0, 0);
        assertEquals(normalized, normalized.normalize(), "ERROR: normalize() wrong result");
    }
}