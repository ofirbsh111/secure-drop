package com.ofirbsh.secure_drop.services;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

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
    public void proccessFile(UploadMetadata metadata, byte[] data, User owner)
    {
        String fileName = metadata.fileName();

        String[] parts = fileName.split("\\.");
        String originalName = parts[0];
        String fileType = parts[1];
        
        int bytesSize = data.length;
        String ownerUsername = owner.getUsername();

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        String UploadDate = LocalDate.now().format(formatter);


        byte[] key = HashService.hexToByte(owner.getPassword());
        byte[] encyptFile = CamelliaService.encrypt(data, key);

        FileModel uploadedFile = new FileModel(ownerUsername, fileName, originalName, fileType, encyptFile, bytesSize, UploadDate);
        fileRepo.insert(uploadedFile);
    }

    public byte[] donwloadFile(FileModel encryptFile, User owner)
    {
        byte[] key = HashService.hexToByte(owner.getPassword());
        return CamelliaService.decrypt(encryptFile.getEncryptfile(), key);
    }
    
    /**
     * מחזיר רשימה של כל הקבצים על פי שם משתמש
     * @return
     */
    public ArrayList<FileModel> getAllFileByUsername(String ownerUsername)
    {
        return (ArrayList<FileModel>) fileRepo.findByOwnerUsername(ownerUsername);
    }
}
