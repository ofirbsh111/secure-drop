package com.ofirbsh.secure_drop.services;

import java.math.BigInteger;
import java.security.SecureRandom;

import com.ofirbsh.secure_drop.datamodels.ECCKeyPair;
import com.ofirbsh.secure_drop.datamodels.Point;

public class ECCService
{
    private final BigInteger p;
    private final BigInteger a;
    private final BigInteger b;

    public ECCService(BigInteger p, BigInteger a, BigInteger b) 
    {
        this.p = p;
        this.a = a;
        this.b = b;
    }

    /**
     * Generate a private & public key
     * @param G Given Point
     * @param random Random Strong number
     * @return
     */
    public ECCKeyPair generateKeyPair(Point G, SecureRandom random)
    {
        BigInteger d;
        do 
        {
            d = new BigInteger(p.bitLength(), random);
            d = mod(d);
        } while (d.equals(BigInteger.ZERO));

        Point Q = multiply(G, d);
        return new ECCKeyPair(d, Q);
    }

    /**
     * Take the private and public key and derive from them the sharedSecret point
     * @param myPrivateKey
     * @param otherPublicKey
     * @return
     */
    public Point deriveSharedSecret(BigInteger myPrivateKey, Point otherPublicKey)
    {
        return multiply(otherPublicKey, myPrivateKey);
    }   

    /**
     * Modulu function that keep the number between 0 - p
     * @param n
     * @return Positive Modulu
     */
    public BigInteger mod(BigInteger n)
    {
        BigInteger r = n.mod(p);
        return r.signum() >= 0 ? r : r.add(p);
    }

    /**
     * Checks if the given point is on the curve
     * @param P Point
     * @return Is on curve
     */
    public boolean isOnCurve(Point P)
    {
        if (P.isInfinity()) return true;

        BigInteger x = mod(P.getX());
        BigInteger y = mod(P.getY());

        BigInteger left = mod(y.multiply(y)); // y^2
        BigInteger right = mod(x.multiply(x).multiply(x).add(a.multiply(x)).add(b)); // x^3 + (ax + b)

        return left.equals(right); // y^2 = x^3 + (ax + b)
    }

    /**
     * add two points that give point on the curve
     * @param P
     * @param Q
     * @return
     */
    public Point add(Point P, Point Q)
    {
        if (P.isInfinity()) return Q;
        if (Q.isInfinity()) return P;

        BigInteger x1 = P.getX();
        BigInteger y1 = P.getY();
        BigInteger x2 = Q.getX();
        BigInteger y2 = Q.getY();

        // P + (-P) = Infinity
        if (x1.equals(x2) && mod(y1.add(y2)).equals(BigInteger.ZERO))
            return Point.infinity();

        BigInteger s; // s = (y₂ − y₁) * (x₂ − x₁)⁻¹ mod(p)

        if (x1.equals(x2) && y1.equals(y2)) 
        {
            BigInteger numerator = mod(BigInteger.valueOf(3).multiply(x1.pow(2)).add(a)); // 3*x1^2+a
            BigInteger denominator = mod(BigInteger.valueOf(2).multiply(y1)); // 2*y1

            s = mod(numerator.multiply(denominator.modInverse(p))); // s = numerator / denominator -> s = (3x1^2 + a) / (2y1)
        }
        else
        {
            BigInteger numerator = mod(y2.subtract(y1));
            BigInteger denominator = mod(x2.subtract(x1));

            s = mod(numerator.multiply(denominator.modInverse(p))); // s = (y2 - y1) / (x2 - x1)
        }

        BigInteger x3 = mod(s.pow(2).subtract(x1).subtract(x2)); // x3 = s^2 − x1 − x2
        BigInteger y3 = mod(s.multiply(x1.subtract(x3)).subtract(y1)); // y3 = s(x1 − x3) − y1

        return new Point(x3, y3, false);
    }

    /**
     * Mulipy point with number using binary
     * @param G
     * @param k
     * @return
     */
    public Point multiply(Point G, BigInteger k)
    {
        Point result = Point.infinity();

        while (k.signum() > 0) 
        {
            if (k.testBit(0)) // If right bit is 1
                result = add(result, G);

            G = add(G, G);
            k = k.shiftRight(1);
        }

        return result;
    }

    public BigInteger getP() {
        return p;
    }
    public BigInteger getA() {
        return a;
    }
    public BigInteger getB() {
        return b;
    }
}
