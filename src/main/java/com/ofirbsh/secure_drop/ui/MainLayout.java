package com.ofirbsh.secure_drop.ui;

import com.ofirbsh.secure_drop.datamodels.User;
import com.ofirbsh.secure_drop.utilities.SessionHelper;
import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.applayout.DrawerToggle;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.H4;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.Scroller;
import com.vaadin.flow.component.orderedlayout.FlexComponent.Alignment;
import com.vaadin.flow.component.sidenav.SideNav;
import com.vaadin.flow.component.sidenav.SideNavItem;

public class MainLayout extends AppLayout
{
    // Layout
    private HorizontalLayout topNavbar;
    private SideNav nav;

    // אלמנטים
    private Button logoutBtn;

    public MainLayout()
    {
        User user = (User) SessionHelper.getAttribute("User");

        // Layout
        topNavbar = new HorizontalLayout();
        nav = new SideNav();
        Scroller scroller = new Scroller(nav);

        // אלמנטים
        DrawerToggle toggle = new DrawerToggle();
        logoutBtn = new Button("Logout");

        SideNavItem homeLink = new SideNavItem("Home", HomeView.class, VaadinIcon.HOME.create());
        SideNavItem profileLink = new SideNavItem("Profile", ProfileView.class, VaadinIcon.USER.create());
        nav.addItem(homeLink);
        nav.addItem(profileLink);

        topNavbar.setWidthFull();
        topNavbar.setAlignItems(FlexComponent.Alignment.CENTER);
        topNavbar.getStyle().setPadding("10px");

        String fullname = "Guest";

        if(user != null)
            fullname = user.getFullname();

        H4 fullName = new H4("Hello, " + fullname);
        H2 appTitle = new H2("SecureDrop");

        topNavbar.add(fullName, appTitle, logoutBtn);
        topNavbar.expand(appTitle);
        topNavbar.expand(fullName);

        addToDrawer(scroller);
        addToNavbar(toggle, topNavbar);
    }
}