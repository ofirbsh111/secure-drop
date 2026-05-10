package com.ofirbsh.secure_drop.repositories;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.ofirbsh.secure_drop.datamodels.FileContent;

public interface FileContentRepository extends MongoRepository<FileContent, String>
{
    FileContent findByFileId(String fileId);
    void deleteByFileId(String fileId);
}