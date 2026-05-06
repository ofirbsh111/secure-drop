package com.ofirbsh.secure_drop.services;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import org.springframework.stereotype.Service;

@Service
public class HashService 
{
    /**
     * מחשב את ערך ה-Hash של הקלט על פי SHA-256
     * @param data
     * @return
     */
    public static byte[] sha256(byte[] data) 
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
     * ממחיר מערך של ביטים לערך הקסה
     * @param bytes
     * @return
     */
    public static String bytesToHex(byte[] bytes)
    {
        String result = "";

        for (int i = 0; i < bytes.length; i++) 
        {
            result += String.format("%02x", bytes[i]);
        }

        return result;
    }

    /**
     * ממחיר ערך הקסה למערך של ביטים
     * @param hex
     * @return
     */
    public static byte[] hexToByte(String hex)
    {
        byte[] bytes = new byte[hex.length() / 2];

        for (int i = 0; i < hex.length(); i+=2) 
        {
            bytes[i / 2] = (byte) Integer.parseInt(hex.substring(i, i + 2), 16);
        }
        
        return bytes;
    }
}
