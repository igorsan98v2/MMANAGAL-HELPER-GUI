package com.ygs;

public class Result {
    private double x;
    private double y;
    private double z;
    private double x1;
    private double y1;
    private double z1;
    private double radius;

    public Result(double x, double y, double z, double x1, double y1, double z1, double radius) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.x1 = x1;
        this.y1 = y1;
        this.z1 = z1;
        this.radius = radius;

    }

    public double getX() {
        if (Double.isInfinite(x))x=0;
        return x;
    }

    public double getY() {
        if (Double.isInfinite(y))y=0;
        return y;
    }

    public double getZ() {
        return z;
    }

    public double getX1() {
        if (Double.isInfinite(x1))x1=0;
        return x1;
    }

    public double getY1() {
        if (Double.isInfinite(y1))y1=0;
        return y1;
    }

    public double getZ1() {
        return z1;
    }

    public double getRadius() {
        return radius;
    }
}
