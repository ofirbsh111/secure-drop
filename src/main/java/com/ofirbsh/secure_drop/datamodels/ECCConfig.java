package com.ofirbsh.secure_drop.datamodels;

import java.math.BigInteger;


public class ECCConfig 
{
    public static final BigInteger P = BigInteger.valueOf(17);
    public static final BigInteger A = BigInteger.valueOf(2);
    public static final BigInteger B = BigInteger.valueOf(2);

    public static final Point G = Point.of(BigInteger.valueOf(5), BigInteger.valueOf(1));
}