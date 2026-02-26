package com.ofirbsh.secure_drop.services;

import java.util.ArrayList;

import org.springframework.stereotype.Service;

import com.ofirbsh.secure_drop.datamodels.User;
import com.ofirbsh.secure_drop.repositories.UserRepository;

@Service
public class UserService 
{
    private UserRepository userRepo;

    public UserService(UserRepository userRepo)
    {
        this.userRepo = userRepo;
    }

    /**
     * Insert User to DB
     * Checks if user already exists
     * @param user
     * @throws Exception
     */
    public void insertUserToDB(User user) throws Exception
    {
        if (userRepo.existsByUsername(user.getUsername())) 
            throw new Exception("User Already Exists");
        else
            userRepo.insert(user);
    }

    /**
     * Retun ArrayList of all Users
     * @return
     */
    public ArrayList<User> getAllUsers()
    {
        return (ArrayList<User>) userRepo.findAll();
    }
}
