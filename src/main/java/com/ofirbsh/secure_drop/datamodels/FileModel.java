package com.ofirbsh.secure_drop.datamodels;

import org.springframework.data.mongodb.core.mapping.Document;

import com.vaadin.flow.component.template.Id;

/**
 * מחלקת הקבצים, מכילה בתוכה מאפיינים של הקובץ
 */
@Document(collection = "Files")
public class FileModel 
{
    @Id
    private String id;
    
    private String ownerUsername;
    private String fileName;
    private String originalName;
    private String fileType;
    private  byte[] encryptfile;
    private int bytesSize;
    private String uploadDate;

    public FileModel(String ownerUsername, String fileName, String originalName, String fileType, byte[] encryptfile, int bytesSize, String uploadDate) 
    {
        this.ownerUsername = ownerUsername;
        this.fileName = fileName;
        this.originalName = originalName;
        this.fileType = fileType;
        this.encryptfile = encryptfile;
        this.bytesSize = bytesSize;
        this.uploadDate = uploadDate;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getOwnerUsername() {
        return ownerUsername;
    }

    public void setOwnerUsername(String ownerUsername) {
        this.ownerUsername = ownerUsername;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getOriginalName() {
        return originalName;
    }

    public void setOriginalName(String originalName) {
        this.originalName = originalName;
    }

    public String getFileType() {
        return fileType;
    }

    public void setFileType(String fileType) {
        this.fileType = fileType;
    }

    public byte[] getEncryptfile() {
        return encryptfile;
    }

    public void setEncryptfile(byte[] encryptfile) {
        this.encryptfile = encryptfile;
    }

    public int getBytesSize() {
        return bytesSize;
    }

    public void setBytesSize(int bytesSize) {
        this.bytesSize = bytesSize;
    }

    public String getUploadDate() {
        return uploadDate;
    }

    public void setUploadDate(String uploadDate) {
        this.uploadDate = uploadDate;
    }

    

    
}
