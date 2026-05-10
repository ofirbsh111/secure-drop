package com.ofirbsh.secure_drop.datamodels;

import java.util.ArrayList;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * מחלקת הקבצים, מכילה בתוכה מאפיינים של הקובץ
 */
@Document(collection = "FilesMetadata")
public class FileMetadata 
{
    @Id
    private String id;
    
    private String ownerUsername;
    private ArrayList<String> sharedUsers = new ArrayList<>();
    private String fileName;    
    private String originalName;
    private String fileType;
    private int bytesSize;
    private String uploadDate;

    public FileMetadata() 
    {
        
    }

    public FileMetadata(String ownerUsername, String fileName, String originalName, String fileType, int bytesSize, String uploadDate) 
    {
        this.ownerUsername = ownerUsername;
        this.fileName = fileName;
        this.originalName = originalName;
        this.fileType = fileType;
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

    public ArrayList<String> getSharedUsers() {
        return sharedUsers;
    }

    public void setSharedUsers(ArrayList<String> sharedUsers) {
        this.sharedUsers = sharedUsers;
    }
}
