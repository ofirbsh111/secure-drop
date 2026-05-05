package com.ofirbsh.secure_drop.datamodels;

import java.math.BigInteger;

public class ECCKeyPair 
{
    private final BigInteger privateKey;
    private final Point publicKey;

    public ECCKeyPair(BigInteger privateKey, Point publicKey)
    {
        this.privateKey = privateKey;
        this.publicKey = publicKey;
    }

    public BigInteger getPrivateKey() {
        return privateKey;
    }

    public Point getPublicKey() {
        return publicKey;
    }
}
