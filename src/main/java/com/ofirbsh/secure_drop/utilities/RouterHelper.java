package com.ofirbsh.secure_drop.utilities;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.router.RouteConfiguration;

public class RouterHelper 
{
    public static <T extends Component> void navigateTo(Class<T> page)
    {
        String pageRoute = RouteConfiguration.forSessionScope().getUrl(page);
        if (pageRoute.isEmpty())
            pageRoute = "/";
        UI.getCurrent().getPage().setLocation(pageRoute);
    }
}
