package com.fasteasytrade.jrandtest.tests;

/**
 * @author Zur Aougav <p> point class is used by MinimumDistance
 * 
 */
public class Point implements Comparable<Point> {

    public double x;
    public double y;

    public Point() {
        super();
    }

    public Point(double x, double y) {
        this.x = x;
        this.y = y;
    }

    @Override
    public int compareTo(Point p) {
        return new Double(y).compareTo(p.y);
    }

    @Override
    public String toString() {
        return x + "," + y;
    }
}