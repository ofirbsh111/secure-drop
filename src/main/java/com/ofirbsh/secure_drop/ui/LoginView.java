package com.ofirbsh.secure_drop.ui;

import com.ofirbsh.secure_drop.datamodels.User;
import com.ofirbsh.secure_drop.services.HashService;
import com.ofirbsh.secure_drop.services.UserService;
import com.ofirbsh.secure_drop.utilities.RouterHelper;
import com.ofirbsh.secure_drop.utilities.SessionHelper;
import com.vaadin.flow.component.html.Anchor;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.login.LoginForm;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.Notification.Position;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.dom.Style.AlignItems;
import com.vaadin.flow.dom.Style.JustifyContent;
import com.vaadin.flow.router.Route;

@Route(value = "/login", layout = MainLayout.class)
public class LoginView extends VerticalLayout
{
    private final UserService userService;

    private HorizontalLayout hozLayout;

    private LoginForm loginForm;

    public LoginView(UserService userService)
    {
        // Service
        this.userService = userService;

        // אתחול
        hozLayout = new HorizontalLayout(Alignment.CENTER);
        loginForm = new LoginForm();

        // משתנים
        Span registerTxt = new Span("Don't have an account yet?");
        Anchor registerAnchor = new Anchor("/register", "Register");

        // עיצוב
        getStyle().setAlignItems(AlignItems.CENTER);
        loginForm.setForgotPasswordButtonVisible(false);
        hozLayout.getStyle().setJustifyContent(JustifyContent.SPACE_BETWEEN);

        // Build
        hozLayout.add(registerTxt);
        hozLayout.add(registerAnchor);

        add(loginForm);
        add(hozLayout);

        // Listeners
        loginForm.addLoginListener(event -> {
            String username = event.getUsername();
            String password = event.getPassword();

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

        byte[] passwordByte = HashService.sha256(password.getBytes());

        return userService.validateUser(username, HashService.bytesToHex(passwordByte));
    }
}
