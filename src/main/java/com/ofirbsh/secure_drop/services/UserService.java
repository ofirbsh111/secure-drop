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
     * @param username
     * @param password
     * @return מחזיר את המשתמש אם קיים
     */
    public User validateUser(String username, String password)
    {
        return userRepo.findByUsernameAndPassword(username, password);
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

    /**
     * מחזיר את כל השמות משתמשים שנמצאים במסד נתונים חוץ משם המשתמש הבא כקלט
     * @return
     */
    public ArrayList<String> getAllUsernames(String exceptUsername)
    {
        ArrayList<String> usernames = new ArrayList<>();
        ArrayList<User> users = getAllUsers();

        for (int i = 0; i < users.size(); i++) 
        {
            if (!users.get(i).getUsername().equals(exceptUsername)) 
                usernames.add(users.get(i).getUsername());
        }
        
        return usernames;
    }
}
