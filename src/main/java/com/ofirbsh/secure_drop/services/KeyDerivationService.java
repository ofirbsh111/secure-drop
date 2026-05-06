package com.ofirbsh.secure_drop.services;

import java.math.BigInteger;
import java.util.Arrays;

import org.springframework.stereotype.Service;

import com.ofirbsh.secure_drop.datamodels.Point;

@Service
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

        byte[] hash = HashService.sha256(xBytes);

        return Arrays.copyOf(hash, keyBytes);
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
