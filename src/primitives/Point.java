package primitives;

import java.util.Map;
import java.util.Vector;

public class Point {
    protected final Double3 xyz;
    public static final Point ZERO = new Point(0, 0, 0);
    public Point(double x, double y, double z) {
        this.xyz = new Double3(x, y, z);
    }
    public Point(Double3 xyz) {
        this.xyz = xyz;
    }
    public Vector subtract(Point point) {
        return this.xyz.subtract(point.xyz);
    }
    public Point add(Vector vector) {
        return new Point(this.xyz.d1() + vector.xyz.d1(), this.xyz.d2() + vector.xyz.d2(), this.xyz.d2() + vector.xyz.d2());
    }
    public double distanceSquared(Point point) {
        double dx = this.xyz.d1() - point.xyz.d1();
        double dy = this.xyz.d2() - point.xyz.d2();
        double dz = this.xyz.d3() - point.xyz.d3();
        return dx * dx + dy * dy + dz * dz;
    }
    public double distance(Point point) {
        return Math.sqrt(this.distanceSquared(point));
    }
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        return (obj instanceof Point other) && this.xyz.equals(other.xyz);
    }
    @Override
    public String toString() { return this.xyz.toString(); }


}
