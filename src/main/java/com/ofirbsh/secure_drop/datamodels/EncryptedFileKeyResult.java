package com.ofirbsh.secure_drop.datamodels;

public class EncryptedFileKeyResult 
{
    private byte[] encryptedFileKey;
    private Point ephemeralPublicKey;

    public EncryptedFileKeyResult(byte[] encryptedFileKey, Point ephemeralPublicKey) 
    {
        this.encryptedFileKey = encryptedFileKey;
        this.ephemeralPublicKey = ephemeralPublicKey;
    }

    public byte[] getEncryptedFileKey() {
        return encryptedFileKey;
    }

    public Point getEphemeralPublicKey() {
        return ephemeralPublicKey;
    }
}