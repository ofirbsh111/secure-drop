package com.ofirbsh.secure_drop.repositories;

import java.util.ArrayList;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.ofirbsh.secure_drop.datamodels.FileModel;

@Repository
public interface FileRepository extends MongoRepository<FileModel, String>
{
    ArrayList<FileModel> findByOwnerUsername(String ownerUsername);
}
