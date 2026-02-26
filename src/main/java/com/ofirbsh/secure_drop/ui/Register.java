package com.ofirbsh.secure_drop.ui;

import com.ofirbsh.secure_drop.datamodels.User;
import com.ofirbsh.secure_drop.services.UserService;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.login.LoginForm;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.Notification.Position;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;

@Route("/register")
public class Register extends VerticalLayout
{
    // Services
    public UserService userService;

    // Components
    public LoginForm registerForm;

    public Register(UserService userService)
    {
        this.userService = userService;

        registerForm = new LoginForm();

        registerForm.getElement().setAttribute("title", "Register");

        registerForm.addLoginListener(event -> {
            String username = event.getUsername();
            String password = event.getPassword();

            boolean isSuccess = insertUser(new User(username, password));

            registerForm.setError(!isSuccess);
        });

        // Add UI
        add(new H1("Register Page"));
        add(registerForm);
    }

    /**
     * Add user to DB
     * @param user
     * @return Is Action was Successful
     */
    public boolean insertUser(User user)
    {
        if (user.getUsername().length() < 6 && user.getPassword().length() < 8) 
        {
            Notification.show("Need atleast 6 character in User, 8 character in Password", 2000, Position.BOTTOM_END);
            return false;
        }

        try 
        {
            userService.insertUserToDB(user);

            Notification.show("User Created Successfuly!", 2000, Position.BOTTOM_END);
            return true;
        } 
        catch (Exception e) 
        {
            Notification.show("Error adding user to Database", 2000, Position.BOTTOM_END);
            e.printStackTrace();

            return false;
        }
    }
}
