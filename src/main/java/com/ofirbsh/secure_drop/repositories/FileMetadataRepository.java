package com.ofirbsh.secure_drop.repositories;

import java.util.ArrayList;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.ofirbsh.secure_drop.datamodels.FileMetadata;

@Repository
public interface FileMetadataRepository extends MongoRepository<FileMetadata, String>
{
    ArrayList<FileMetadata> findByOwnerUsername(String ownerUsername);
}
