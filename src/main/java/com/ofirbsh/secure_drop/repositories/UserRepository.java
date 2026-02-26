package com.ofirbsh.secure_drop.repositories;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.ofirbsh.secure_drop.datamodels.User;

@Repository
public interface UserRepository extends MongoRepository<User, String>
{
    public Boolean existsByUsername(String username);
}
