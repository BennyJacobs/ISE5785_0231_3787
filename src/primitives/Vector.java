package primitives;

public class Vector extends Point {
    public Vector(double x, double y, double z) {
        this(new Double3(x, y, z));
    }
    public Vector(Double3 xyz) {
        super(xyz);
        if (xyz.equals(Double3.ZERO))
            throw new IllegalArgumentException("ERROR: vector zero is illegal");
    }
    public Vector add(Vector vector) {
        return new Vector(this.xyz.add(vector.xyz));
    }
    public Vector scale(double scalar) {
        return new Vector(this.xyz.scale(scalar));
    }
    public double dotProduct(Vector vector) {
        return this.xyz.d1()*vector.xyz.d1() + this.xyz.d2()*vector.xyz.d2() + this.xyz.d3()*vector.xyz.d3();
    }
    public Vector crossProduct(Vector vector) {
        return new Vector(this.xyz.d2()*vector.xyz.d3()-this.xyz.d3()*vector.xyz.d2(), this.xyz.d3()*vector.xyz.d1()-this.xyz.d1()*vector.xyz.d3(), this.xyz.d1()*vector.xyz.d2()-this.xyz.d2()*vector.xyz.d1());
    }
    public double lengthSquared() {
        return this.dotProduct(this);
    }
    public double length() {
        return Math.sqrt(this.lengthSquared());
    }
    public Vector normalize() {
        return this.scale(1/this.length());
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        return (obj instanceof Vector other) && super.equals(other);
    }
    @Override
    public String toString() { return "v" + super.toString(); }


}
