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
     * בודק האם המשתמש קיים לפי שם משתמש וסיסמא (התחברות)
     * @param user
     * @return מחזיר אם הערכים נכונים
     */
    public boolean validateUser(User user)
    {
        if (userRepo.existsByUsernameAndPassword(user.getUsername(), user.getPassword())) 
        {
            return true;
        }
        return false;
    }

    /**
     * מכניס משתמש למסד הנתונים 
     * ובודק אם המשתמש קיים כבר במערכת
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
     * מחזיר רשימה של כל המשתמשים הנמצאים במסד נתונים
     * @return
     */
    public ArrayList<User> getAllUsers()
    {
        return (ArrayList<User>) userRepo.findAll();
    }
}
