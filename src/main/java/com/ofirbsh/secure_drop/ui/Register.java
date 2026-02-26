package com.ofirbsh.secure_drop.ui;

import com.ofirbsh.secure_drop.datamodels.User;
import com.ofirbsh.secure_drop.services.UserService;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.Notification.Position;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.PasswordField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.Route;

@Route("/register")
public class Register extends VerticalLayout
{
    private final UserService userService;

    private TextField usernameField;
    private PasswordField passwordField;
    private Button registerButton;

    private Grid<User> usersGrid;

    public Register(UserService userService)
    {
        this.userService = userService;

        usernameField = new TextField("Username");
        passwordField = new PasswordField("Password");
        registerButton = new Button("Register");

        usersGrid = new Grid<>(User.class);

        registerButton.addClickListener(e -> {
            String username = usernameField.getValue();
            String password = passwordField.getValue();

            boolean isSuccess = insertUser(new User(username, password));

            if (isSuccess) {
                usersGrid.setItems(userService.getAllUsers());
                usernameField.clear();
                passwordField.clear();
            }
        });

        usersGrid.setItems(userService.getAllUsers());
        usersGrid.setColumns("username", "password");

        add(new H1("Register Page"));
        add(usernameField);
        add(passwordField);
        add(registerButton);
        add(usersGrid);


    }

    /**
     * Send request to the backend to add user
     * Checks validation Before the request
     * @param user
     * @return
     */
    public boolean insertUser(User user)
    {
        // Validation
        if (user.getUsername().length() < 6 || user.getPassword().length() < 8)
        {
            Notification.show("Need at least 6 characters in Username and 8 in Password", 2000, Position.MIDDLE);
            return false;
        }

        try
        {
            userService.insertUserToDB(user);
            Notification.show("User Created Successfully!", 2000, Position.MIDDLE);
            return true;
        }
        catch (Exception e)
        {
            Notification.show("Error adding user to Database", 2000, Position.MIDDLE);
            e.printStackTrace();
            return false;
        }
    }
}