package com.ofirbsh.secure_drop.datamodels;

import java.security.SecureRandom;
import java.util.ArrayList;

import org.springframework.data.mongodb.core.mapping.Document;

import com.ofirbsh.secure_drop.services.ECCService;
import com.ofirbsh.secure_drop.services.HashService;
import com.vaadin.flow.component.template.Id;

/**
 * מחלקת המשתמש, מכיל בתוכה מאפיינים של המשתמש
 */
@Document(collection = "Users")
public class User 
{
    @Id
    private String username;
    private String fullname;
    private String password;
    private String joinDate;
    private ArrayList<Integer> files;
    private ECCKeyPair Keys;

    public User(String username, String password, String fullname, String joinDate) 
    {
        this.username = username;
        this.fullname = fullname;
        this.joinDate = joinDate;

        // שומר את הסיסמא כערך Hash
        byte[] passwordByte = HashService.sha256(password.getBytes());
        this.password = HashService.bytesToHex(passwordByte);

        SecureRandom rnd = new SecureRandom();

        ECCService curve = new ECCService();
        
        Point G = ECCConfig.G;

        this.Keys = curve.generateKeyPair(G, rnd);
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getFullname() {
        return fullname;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }

    public String getJoinDate() {
        return joinDate;
    }

    public void setJoinDate(String joinDate) {
        this.joinDate = joinDate;
    }

    public ArrayList<Integer> getFiles() {
        return files;
    }

    public void setFiles(ArrayList<Integer> files) {
        this.files = files;
    }

    public ECCKeyPair getKeys() {
        return Keys;
    }

    public void setKeys(ECCKeyPair keys) {
        Keys = keys;
    }

    @Override
    public String toString() 
    {
        return "User [username=" + username + ", password=" + password + "]";
    }
}
