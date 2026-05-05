package com.ofirbsh.secure_drop.datamodels;

import java.math.BigInteger;

public class Point 
{
    private BigInteger x;
    private BigInteger y;
    private boolean infinity;
    
    public Point(BigInteger x, BigInteger y, boolean flag) 
    {
        this.x = x;
        this.y = y;
        this.infinity = flag;
    }

    public static Point of(BigInteger x, BigInteger y)
    {
        return new Point(x, y, false);
    }

    public static Point infinity()
    {
        return new Point(BigInteger.ZERO, BigInteger.ZERO, true);
    }

    public BigInteger getX() {
        return x;
    }
    public void setX(BigInteger x) {
        this.x = x;
    }
    public BigInteger getY() {
        return y;
    }
    public void setY(BigInteger y) {
        this.y = y;
    }
    public boolean isInfinity() {
        return infinity;
    }
    public void setInfinity(boolean flag) {
        this.infinity = flag;
    }
    
    @Override
    public String toString() {
        if (infinity) return "Point(Infinity)";
        return "Point(" + x + ", " + y + ")";
    }
}
