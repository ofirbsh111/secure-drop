package com.ofirbsh.secure_drop.ui;

import com.ofirbsh.secure_drop.datamodels.User;
import com.ofirbsh.secure_drop.utilities.SessionHelper;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.router.Route;

@Route(value = "profile", layout = MainLayout.class)
public class ProfileView extends VerticalLayout implements BeforeEnterObserver
{
    public ProfileView()
    {

    }

    @Override
    public void beforeEnter(BeforeEnterEvent event) 
    {
        User user = (User) SessionHelper.getAttribute("User");
        if (user == null) 
        { 
            event.forwardTo(LoginView.class);
        }
    }
}
