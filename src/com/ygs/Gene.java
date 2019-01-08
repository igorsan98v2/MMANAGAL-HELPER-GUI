package com.ygs;

public class Gene {
    private float a;
    private float d;
    private int angle;
    private int angleStep;
    private float distance;
    private float radius;
    private int coilNum;
    public Gene(float a,int coilNum,float d, int angle, int angleStep, float distance) {
        this.a = a;
        this.d = d;
        this.coilNum = coilNum;

        this.angle = angle;
        this.angleStep = angleStep;
        this.distance = distance;
        radius= 0.9f;
    }

    public int getCoilNum() {
        return coilNum;
    }

    public float getA() {
        return a;
    }

    public float getD() {
        return d;
    }

    public int getAngle() {
        return angle;
    }

    public int getAngleStep() {
        return angleStep;
    }

    public float getDistance() {
        return distance;
    }

    public float getRadius() {
        return radius;
    }
}
