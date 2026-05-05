package com.ofirbsh.secure_drop.datamodels;

import java.io.File;
import java.util.Date;

import org.springframework.data.mongodb.core.mapping.Document;

import com.vaadin.flow.component.template.Id;

/**
 * מחלקת הקבצים, מכילה בתוכה מאפיינים של הקובץ
 */
@Document(collection = "Files")
public class FileModel 
{
    @Id
    private int fileId;
    private String ownerUsername;
    private String fileName;
    private String originalName;
    private String fileType;
    private File file;
    private int bytesSize;
    private Date uploadDate;

    
}
