package com.ofirbsh.secure_drop.ui;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import com.ofirbsh.secure_drop.datamodels.User;
import com.ofirbsh.secure_drop.services.UserService;
import com.ofirbsh.secure_drop.utilities.RouterHelper;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.html.Anchor;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.Notification.Position;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.PasswordField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.dom.Style.AlignItems;
import com.vaadin.flow.router.Route;

@Route(value = "/register", layout = MainLayout.class)
public class RegisterView extends VerticalLayout
{
    private final UserService userService;

    private VerticalLayout registerFormLayout;
    private HorizontalLayout topRegisterLayout;
    private HorizontalLayout buttomRegisterLayout;
    private HorizontalLayout buttomLayout;

    private TextField usernameField;
    private TextField fullnameField;
    private PasswordField passwordField;
    private PasswordField confPasswordField;
    private Button registerButton;

    public RegisterView(UserService userService)
    {
        // Service
        this.userService = userService;

        // אתחול
        registerFormLayout = new VerticalLayout(Alignment.CENTER);
        topRegisterLayout = new HorizontalLayout(Alignment.CENTER);
        buttomRegisterLayout = new HorizontalLayout(Alignment.CENTER);
        buttomLayout = new HorizontalLayout(Alignment.CENTER);

        usernameField = new TextField("Username");
        fullnameField = new TextField("Full name");
        passwordField = new PasswordField("Password");
        confPasswordField = new PasswordField("Confirm Password");
        registerButton = new Button("Register");

        // משתנים
        Span loginTxt = new Span("have an account?");
        Anchor loginAnchor = new Anchor("/login", "Login");

        // עיצוב
        getStyle().setAlignItems(AlignItems.CENTER);
        registerFormLayout.setWidth(null);

        confPasswordField.setRevealButtonVisible(false);
        registerButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        registerButton.setWidthFull();

        registerButton.addClickListener(e -> {
            String username = usernameField.getValue();
            String password = passwordField.getValue();
            String confPassword = confPasswordField.getValue();
            String fullname = fullnameField.getValue();

            boolean isSuccess = insertUser(username, password, confPassword, fullname);

            if (isSuccess) 
            {
                RouterHelper.navigateTo(LoginView.class);
            }
        });

        // Build
        topRegisterLayout.add(usernameField);
        topRegisterLayout.add(fullnameField);
        buttomRegisterLayout.add(passwordField);
        buttomRegisterLayout.add(confPasswordField);

        registerFormLayout.add(topRegisterLayout);
        registerFormLayout.add(buttomRegisterLayout);
        registerFormLayout.add(registerButton);

        buttomLayout.add(loginTxt);
        buttomLayout.add(loginAnchor);

        add(new H1("Register Page"));
        add(registerFormLayout);
        add(buttomLayout);
    }

    /**
     * שולח בקשה לשרת להוסיף את המשתמש למסד הנתונים
     * בודק ולידציה לפני ההוספה כדי למנוע התנגשויות
     * @param user
     * @return
     */
    public boolean insertUser(String username, String password, String confPassword, String fullname)
    {
        // ולידציה
        if (username.length() < 6 || password.length() < 8 || fullname.length() < 6)
        {
            Notification.show("Need at least 6 characters in Username & Fullname and 8 in Password", 2000, Position.MIDDLE);
            return false;
        }
        if (!password.equals(confPassword)) 
        {
            Notification.show("Password confirmation does not match", 2000, Position.MIDDLE);
            return false;
        }

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        String JoinDate = LocalDate.now().format(formatter);
        
        User user = new User(username, password, fullname, JoinDate);

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