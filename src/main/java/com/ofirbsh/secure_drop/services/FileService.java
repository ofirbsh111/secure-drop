package com.ofirbsh.secure_drop.services;

import org.springframework.stereotype.Service;

import com.ofirbsh.secure_drop.datamodels.User;
import com.ofirbsh.secure_drop.repositories.FileRepository;
import com.vaadin.flow.server.streams.UploadMetadata;

@Service
public class FileService 
{
    private FileRepository fileRepo;

    public FileService(FileRepository fileRepo)
    {
        this.fileRepo = fileRepo;
    }
    
    /**
     * מצפין את הקובץ שהתקבל מהמשתמש, שולח לאחסון לשרת
     * @param metadata נתונים על הקובץ
     * @param data נתוני הקובץ
     */
    public void proccessFile(UploadMetadata metadata, byte[] data, User Owner)
    {
        String fileName = metadata.fileName();
        String originalName = fileName.split(".")[0];
        String fileType = fileName.split(".")[1];
        int bytesSize = data.length;
        String ownerId = Owner.getUsername();



        // CamelliaService.encrypt(data, key);
    }
}
