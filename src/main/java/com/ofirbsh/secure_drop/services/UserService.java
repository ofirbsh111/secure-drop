package com.ofirbsh.secure_drop.services;

import org.springframework.stereotype.Service;

import com.ofirbsh.secure_drop.datamodels.User;
import com.ofirbsh.secure_drop.repositories.UserRepo;

@Service
public class UserService 
{
    private UserRepo userRepo;

    public UserService(UserRepo userRepo)
    {
        this.userRepo = userRepo;
    }

    public void insertUserToDB(User user) throws Exception
    {
        userRepo.insert(user);
    }
}
