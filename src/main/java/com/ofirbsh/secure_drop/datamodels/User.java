package com.ofirbsh.secure_drop.datamodels;

import java.math.BigInteger;
import java.security.SecureRandom;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;

import org.springframework.data.mongodb.core.mapping.Document;

import com.ofirbsh.secure_drop.services.ECCService;
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

    public User(String username, String password, String fullname) 
    {
        this.username = username;
        this.password = password;
        this.fullname = fullname;

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        this.joinDate = LocalDate.now().format(formatter);

        SecureRandom rnd = new SecureRandom();

        ECCService curve = new ECCService(
                BigInteger.valueOf(17),
                BigInteger.valueOf(2),
                BigInteger.valueOf(2));
        
        Point G = Point.of(BigInteger.valueOf(5), BigInteger.valueOf(1));

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
    public String toString() {
        return "User [username=" + username + ", password=" + password + "]";
    }
}
