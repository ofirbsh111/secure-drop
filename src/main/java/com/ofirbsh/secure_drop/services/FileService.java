package com.ofirbsh.secure_drop.services;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

import org.springframework.stereotype.Service;

import com.ofirbsh.secure_drop.datamodels.ECCConfig;
import com.ofirbsh.secure_drop.datamodels.EncryptedFileKeyResult;
import com.ofirbsh.secure_drop.datamodels.FileContent;
import com.ofirbsh.secure_drop.datamodels.FileKeyAccess;
import com.ofirbsh.secure_drop.datamodels.FileMetadata;
import com.ofirbsh.secure_drop.datamodels.User;
import com.ofirbsh.secure_drop.repositories.FileContentRepository;
import com.ofirbsh.secure_drop.repositories.FileKeyAccessRepository;
import com.ofirbsh.secure_drop.repositories.FileMetadataRepository;
import com.ofirbsh.secure_drop.repositories.UserRepository;
import com.vaadin.flow.server.streams.UploadMetadata;

@Service
public class FileService 
{
    private FileMetadataRepository fileMetadataRepo;
    private FileContentRepository fileContentRepo;
    private FileKeyAccessRepository filekeyAccessRepo;
    private UserRepository userRepo;
    private ECCService eccService;

    public FileService(FileMetadataRepository fileMetadataRepo, FileContentRepository fileContentRepo, UserRepository userRepo, FileKeyAccessRepository filekeyAccessRepo, ECCService eccService)
    {
        this.fileMetadataRepo = fileMetadataRepo;
        this.fileContentRepo = fileContentRepo;
        this.filekeyAccessRepo = filekeyAccessRepo;
        this.userRepo = userRepo;
        this.eccService = eccService;
    }
    
    /**
     * מצפין את הקובץ שהתקבל מהמשתמש, שולח לאחסון לשרת
     * @param metadata נתונים על הקובץ
     * @param data נתוני הקובץ
     */
    public void proccessFile(UploadMetadata metadata, byte[] data, User owner)
    {
        // Metadata
        String fileName = metadata.fileName();
        int lastDotIndex = fileName.lastIndexOf(".");
        String originalName = fileName.substring(0, lastDotIndex);
        String fileType = fileName.substring(lastDotIndex + 1);
        int bytesSize = data.length;
        String ownerUsername = owner.getUsername();

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        String uploadDate = LocalDate.now().format(formatter);

        // Create a random key & encrypt file
        byte[] fileKey = KeyService.generateRandomKey(32);
        byte[] encryptFile = CamelliaService.encrypt(data, fileKey);

        // Insert the Metadata to DB
        FileMetadata fileMetadata = new FileMetadata(ownerUsername, fileName, originalName, fileType, bytesSize, uploadDate);
        fileMetadataRepo.insert(fileMetadata);

        // Encrypt key with owner's public key & temp private key
        EncryptedFileKeyResult keyResult = eccService.encryptKeyWithPublicKey(fileKey, owner.getKeys().getPublicKey());
        FileKeyAccess ownerKeyAccess = new FileKeyAccess(fileMetadata.getId(), owner.getUsername(), keyResult.getEncryptedFileKey(), keyResult.getEphemeralPublicKey());

        filekeyAccessRepo.insert(ownerKeyAccess);

        FileContent fileContent = new FileContent(fileMetadata.getId(), encryptFile);
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
        FileKeyAccess keyAccess = filekeyAccessRepo.findByFileIdAndUsername(fileMetadata.getId(), owner.getUsername());
        FileContent fileContent = fileContentRepo.findByFileId(fileMetadata.getId());

        byte[] key = eccService.decryptFileKeyWithPrivateKey(keyAccess.getEncryptedFileKey(), owner.getKeys().getPrivateKey(), keyAccess.getEphemeralPublicKey());

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
        filekeyAccessRepo.deleteByFileId(fileId);
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

        User sharedUser = userRepo.findByUsername(username);
        User owner = userRepo.findByUsername(file.getOwnerUsername());
        FileKeyAccess ownerKeyAccess = filekeyAccessRepo.findByFileIdAndUsername(fileId, owner.getUsername());

        byte[] fileKey = eccService.decryptFileKeyWithPrivateKey(ownerKeyAccess.getEncryptedFileKey(), owner.getKeys().getPrivateKey(), ownerKeyAccess.getEphemeralPublicKey());
        EncryptedFileKeyResult sharedKeyResult = eccService.encryptKeyWithPublicKey(fileKey, sharedUser.getKeys().getPublicKey());

        FileKeyAccess sharedKeyAccess = new FileKeyAccess(fileId, sharedUser.getUsername(), sharedKeyResult.getEncryptedFileKey(), sharedKeyResult.getEphemeralPublicKey());

        filekeyAccessRepo.insert(sharedKeyAccess);

        file.getSharedUsers().add(username);
        fileMetadataRepo.save(file);
    }
    
    /**
     * מחזיר רשימה של כל נתוני הקבצים על פי שם משתמש
     * @return
     */
    public ArrayList<FileMetadata> getAllFileMetadataByUsername(String ownerUsername)
    {
        ArrayList<FileMetadata> ownerFiles = (ArrayList<FileMetadata>) fileMetadataRepo.findByOwnerUsername(ownerUsername);
        ArrayList<FileMetadata> sharedFiles = fileMetadataRepo.findBySharedUsersContains(ownerUsername);

        ownerFiles.addAll(sharedFiles);

        return ownerFiles;
    }
}
