package com.ofirbsh.secure_drop.datamodels;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "FilesKeys")
public class FileKeyAccess 
{
    @Id
    private String id;

    private String fileId;
    private String username;
    private byte[] encryptedFileKey;
    private Point ephemeralPublicKey;

    public FileKeyAccess()
    {

    }

    public FileKeyAccess(String fileId, String username, byte[] encryptedFileKey, Point ephemeralPublicKey)
    {
        this.fileId = fileId;
        this.username = username;
        this.encryptedFileKey = encryptedFileKey;
        this.ephemeralPublicKey = ephemeralPublicKey;
    }

    public String getId() {
        return id;
    }

    public String getFileId() {
        return fileId;
    }

    public String getUsername() {
        return username;
    }

    public byte[] getEncryptedFileKey() {
        return encryptedFileKey;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setFileId(String fileId) {
        this.fileId = fileId;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setEncryptedFileKey(byte[] encryptedFileKey) {
        this.encryptedFileKey = encryptedFileKey;
    }

    public Point getEphemeralPublicKey() {
        return ephemeralPublicKey;
    }

    public void setEphemeralPublicKey(Point ephemeralPublicKey) {
        this.ephemeralPublicKey = ephemeralPublicKey;
    }
}