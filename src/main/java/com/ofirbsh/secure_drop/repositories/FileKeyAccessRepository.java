package com.ofirbsh.secure_drop.repositories;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.ofirbsh.secure_drop.datamodels.FileKeyAccess;

public interface FileKeyAccessRepository extends MongoRepository<FileKeyAccess, String>
{
    public FileKeyAccess findByFileIdAndUsername(String fileId, String username);

    public void deleteByFileId(String fileId);
}