package com.ofirbsh.secure_drop.services;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;

import com.ofirbsh.secure_drop.datamodels.Point;

public class KeyDerivationService 
{
    /**
     * derive From the sharedSecret Key into 16 byte
     * @param sharedSecret
     * @param keyBytes
     * @return
     */
    public static byte[] deriveKeyFromSharedSecret(Point sharedSecret, int keyBytes) 
    {
        byte[] xBytes = toUnsignedFixed(sharedSecret.getX());

        byte[] hash = sha256(xBytes);

        return Arrays.copyOf(hash, keyBytes);
    }

    /**
     * Calculates the SHA-256 hash of the given input data.
     * @param data
     * @return
     */
    private static byte[] sha256(byte[] data) 
    {
        try 
        {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            return md.digest(data);
        } 
        catch (NoSuchAlgorithmException e) 
        {
            throw new RuntimeException("SHA-256 not available", e);
        }
    }

    /**
     * Transfer Bigintger into array of byte
     * @param n
     * @return
     */
    private static byte[] toUnsignedFixed(BigInteger n) 
    {
        byte[] raw = n.abs().toByteArray();

        if (raw.length > 1 && raw[0] == 0)
            raw = Arrays.copyOfRange(raw, 1, raw.length);

        return raw;
    }
}
