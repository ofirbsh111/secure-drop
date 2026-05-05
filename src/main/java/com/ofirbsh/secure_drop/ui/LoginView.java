package com.ofirbsh.secure_drop.ui;

import com.ofirbsh.secure_drop.datamodels.User;
import com.ofirbsh.secure_drop.services.UserService;
import com.ofirbsh.secure_drop.utilities.RouterHelper;
import com.ofirbsh.secure_drop.utilities.SessionHelper;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.Notification.Position;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.PasswordField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.Route;

@Route("/login")
public class LoginView extends VerticalLayout
{
    private final UserService userService;

    private TextField usernameField;
    private PasswordField passwordField;
    private Button registerButton;

    public LoginView(UserService userService)
    {
        this.userService = userService;

        usernameField = new TextField("Username");
        passwordField = new PasswordField("Password");
        registerButton = new Button("Login");

        registerButton.addClickListener(e -> {
            String username = usernameField.getValue();
            String password = passwordField.getValue();

            User User = LoginUser(username, password);

            if (User != null) 
            {
                Notification.show("Login Successful!", 2000, Position.MIDDLE);
                SessionHelper.setAttribute("User", User);
                RouterHelper.navigateTo(HomeView.class);
            }
            else
            {
                Notification.show("Username or Password is incorrect!", 2000, Position.MIDDLE);
            }
        });

        add(new H1("Login Page"));
        add(usernameField);
        add(passwordField);
        add(registerButton);
    }

    /**
     * שולח בקשה לשרת הבקאנד כדי לוודא שהמשתמש קיים ונתוני האימות אכן נכונים
     * @param user
     * @return
     */
    public User LoginUser(String username, String password)
    {
        // Validation
        if (username.length() < 6 || password.length() < 8)
        {
            Notification.show("Username or password is not valid", 2000, Position.MIDDLE);
            return null;
        }

        return userService.validateUser(username, password);
    }
}
