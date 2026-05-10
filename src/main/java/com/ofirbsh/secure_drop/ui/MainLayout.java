package com.ofirbsh.secure_drop.ui;

import com.ofirbsh.secure_drop.datamodels.User;
import com.ofirbsh.secure_drop.utilities.SessionHelper;
import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.H4;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.FlexComponent.Alignment;

public class MainLayout extends AppLayout
{
    private HorizontalLayout topNavbar;
    private Button logoutBtn;

    public MainLayout()
    {
        User user = (User) SessionHelper.getAttribute("User");

        topNavbar = new HorizontalLayout(Alignment.CENTER);
        logoutBtn = new Button("Logout");

        topNavbar.setWidthFull();
        topNavbar.getStyle().setPadding("10px");

        String fullname = "Guest";

        if(user != null)
            fullname = user.getFullname();

        H4 fullName = new H4("Hello, " + fullname);

        logoutBtn.getStyle().setMarginRight("5px");
        logoutBtn.getStyle().setMarginLeft("auto");

        topNavbar.add(fullName);
        topNavbar.add(logoutBtn);

        addToNavbar(topNavbar);
    }
}