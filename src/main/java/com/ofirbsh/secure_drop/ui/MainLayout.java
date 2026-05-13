package com.ofirbsh.secure_drop.ui;

import com.ofirbsh.secure_drop.utilities.RouterHelper;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.FlexComponent.Alignment;

public class MainLayout extends AppLayout
{
    private HorizontalLayout hozLayout;

    private Button loginBtn;
    private Button registerBtn;

    public MainLayout()
    {
        // אתחול
        hozLayout = new HorizontalLayout(Alignment.CENTER);

        loginBtn = new Button("Login");
        registerBtn = new Button("Register");

        // משתנים
        H2 title = new H2("SecureDrop");

        // עיצוב
        hozLayout.getStyle().setPadding("10px");
        hozLayout.setWidthFull();

        title.getStyle().setCursor("pointer");

        loginBtn.getStyle().setMarginLeft("auto");
        loginBtn.addThemeVariants(ButtonVariant.LUMO_PRIMARY);

        hozLayout.add(title);
        hozLayout.add(loginBtn);
        hozLayout.add(registerBtn);

        addToNavbar(hozLayout);

        // Listeners
        loginBtn.addClickListener(event -> {
            RouterHelper.navigateTo(LoginView.class);
        });

        registerBtn.addClickListener(event -> {
            RouterHelper.navigateTo(RegisterView.class);
        });

        title.addClickListener(event ->
        {
            UI.getCurrent().navigate("");
        });
    }
}
