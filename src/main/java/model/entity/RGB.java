package model.entity;

public class RGB {
    private final Double r;
    private final Double g;
    private final Double b;


    public RGB(double r, double g, double b) {
        this.r = r;
        this.g = g;
        this.b = b;
    }

    public double getR() {
        return r;
    }

    public double getG() {
        return g;
    }

    public double getB() {
        return b;
    }
}