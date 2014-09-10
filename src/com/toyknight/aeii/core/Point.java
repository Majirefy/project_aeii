
package com.toyknight.aeii.core;

/**
 *
 * @author toyknight
 */
public class Point {
	
    public int x;
    public int y;

    public Point() {
        this(0, 0);
    }

    
    public Point(Point p) {
        this(p.x, p.y);
    }

    public Point(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

	@Override
    public boolean equals(Object obj) {
        if (obj instanceof Point) {
            Point pt = (Point)obj;
            return (x == pt.x) && (y == pt.y);
        }
        return super.equals(obj);
    }

	@Override
	public int hashCode() {
		int hash = 5;
		hash = 89 * hash + this.x;
		hash = 89 * hash + this.y;
		return hash;
	}
	
	@Override
    public String toString() {
        return getClass().getName() + "[x=" + x + ",y=" + y + "]";
    }
	
}
