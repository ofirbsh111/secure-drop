package com.ofirbsh.secure_drop.datamodels;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "FilesContent")
public class FileContent 
{
    @Id
    private String id;

    private String fileId;
    private byte[] encryptFile;

    public FileContent()
    {

    }

    public FileContent(String fileId, byte[] encryptFile) 
    {
        this.fileId = fileId;
        this.encryptFile = encryptFile;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getFileId() {
        return fileId;
    }

    public void setFileId(String fileId) {
        this.fileId = fileId;
    }

    public byte[] getEncryptFile() {
        return encryptFile;
    }

    public void setEncryptFile(byte[] encryptFile) {
        this.encryptFile = encryptFile;
    }

    
}
