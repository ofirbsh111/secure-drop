package com.ofirbsh.secure_drop.services;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

import org.springframework.stereotype.Service;

import com.ofirbsh.secure_drop.datamodels.FileContent;
import com.ofirbsh.secure_drop.datamodels.FileMetadata;
import com.ofirbsh.secure_drop.datamodels.User;
import com.ofirbsh.secure_drop.repositories.FileContentRepository;
import com.ofirbsh.secure_drop.repositories.FileMetadataRepository;
import com.ofirbsh.secure_drop.repositories.UserRepository;
import com.vaadin.flow.server.streams.UploadMetadata;

@Service
public class FileService 
{
    private FileMetadataRepository fileMetadataRepo;
    private FileContentRepository fileContentRepo;
    private UserRepository userRepo;

    public FileService(FileMetadataRepository fileMetadataRepo, FileContentRepository fileContentRepo, UserRepository userRepo)
    {
        this.fileMetadataRepo = fileMetadataRepo;
        this.fileContentRepo = fileContentRepo;
        this.userRepo = userRepo;
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

        FileMetadata fileMetadata = new FileMetadata(ownerUsername, fileName, originalName, fileType, bytesSize, UploadDate);
        fileMetadataRepo.insert(fileMetadata);

        FileContent fileContent = new FileContent(fileMetadata.getId(), encyptFile);
        fileContentRepo.insert(fileContent);
    }

    /**
     * מחזיר קובץ מפוענח של הבעלים
     * @param encryptFile
     * @param owner
     * @return
     */
    public byte[] donwloadFile(FileMetadata fileMetadata, User owner)
    {
        byte[] key = HashService.hexToByte(owner.getPassword());
        FileContent fileContent = fileContentRepo.findByFileId(fileMetadata.getId());

        return CamelliaService.decrypt(fileContent.getEncryptFile(), key);
    }

    /**
     * מוחק קובץ על פי מזהה
     * @param fileId
     */
    public void deleteFile(String fileId)
    {
        fileMetadataRepo.deleteById(fileId);
        fileContentRepo.deleteByFileId(fileId);
    }

    /**
     * מוסיף גישה למשתמש אחר שהוא לא הבעלים
     * @param fileId מזהה הקובץ
     * @param username המשתמש שמקבל גישה
     */
    public void addSharedUser(String fileId, String username)
    {
        FileMetadata file = fileMetadataRepo.findById(fileId).orElse(null);

        if (!userRepo.existsByUsername(username))
            return;

        if (file.getSharedUsers().contains(username))
            return;

        file.getSharedUsers().add(username);
        fileMetadataRepo.save(file);
    }
    
    /**
     * מחזיר רשימה של כל הקבצים על פי שם משתמש
     * @return
     */
    public ArrayList<FileMetadata> getAllFileByUsername(String ownerUsername)
    {
        return (ArrayList<FileMetadata>) fileMetadataRepo.findByOwnerUsername(ownerUsername);
    }
}
