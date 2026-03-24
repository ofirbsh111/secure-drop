package com.ofirbsh.secure_drop.ui;

import com.ofirbsh.secure_drop.datamodels.User;
import com.ofirbsh.secure_drop.services.UserService;
import com.ofirbsh.secure_drop.utilities.RouterHelper;
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

            boolean isCurrect = LoginUser(new User(username, password));

            if (isCurrect) 
            {
                Notification.show("Login Successful!", 2000, Position.MIDDLE);
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
    public boolean LoginUser(User user)
    {
        // Validation
        if (user.getUsername().length() < 6 || user.getPassword().length() < 8)
        {
            Notification.show("Username or password is not valid", 2000, Position.MIDDLE);
            return false;
        }

        return userService.validateUser(user);
    }
}
