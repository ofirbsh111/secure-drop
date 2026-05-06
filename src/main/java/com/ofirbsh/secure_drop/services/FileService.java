package com.ofirbsh.secure_drop.services;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import org.springframework.stereotype.Service;

import com.ofirbsh.secure_drop.datamodels.FileModel;
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

        String[] parts = fileName.split("\\.");
        String originalName = parts[0];
        String fileType = parts[1];
        
        int bytesSize = data.length;
        String ownerUsername = Owner.getUsername();

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        String UploadDate = LocalDate.now().format(formatter);


        byte[] key = HashService.hexToByte(Owner.getPassword());
        byte[] encyptFile = CamelliaService.encrypt(data, key);

        FileModel uploadedFile = new FileModel(ownerUsername, fileName, originalName, fileType, encyptFile, bytesSize, UploadDate);
        fileRepo.insert(uploadedFile);
    }
}
