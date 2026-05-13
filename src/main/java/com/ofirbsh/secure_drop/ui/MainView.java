package com.ofirbsh.secure_drop.ui;

import com.ofirbsh.secure_drop.utilities.RouterHelper;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.dom.Style.AlignItems;
import com.vaadin.flow.router.Route;

@Route(value = "", layout = MainLayout.class)
public class MainView extends VerticalLayout
{
    private HorizontalLayout hozLayout;

    private Button loginBtn;
    private Button registerBtn;
    
    public MainView()
    {

        // אתחול
        hozLayout = new HorizontalLayout(Alignment.CENTER);

        loginBtn = new Button("Login");
        registerBtn = new Button("Register");

        // משתנים
        H1 title = new H1("SecureDrop");
        H3 subtitle = new H3("פרויקט לשיתוף והצפנת קבצים");
        H3 credit = new H3("שם הסטודנט: אופיר בן שימול");
        Image mainImg = new Image("secure-drop.png", "Logo");

        // עיצוב
        getStyle().setAlignItems(AlignItems.CENTER);
        getStyle().setMarginTop("20px");

        title.getStyle().setFontWeight(700);

        mainImg.getStyle().setMarginTop("20px");
        hozLayout.getStyle().setMarginTop("20px");

        loginBtn.addThemeVariants(ButtonVariant.LUMO_PRIMARY);

        // Build
        hozLayout.add(loginBtn);
        hozLayout.add(registerBtn);

        add(title);
        add(subtitle);
        add(credit);
        add(mainImg);
        add(hozLayout);

        // Listeners
        loginBtn.addClickListener(event -> {
            RouterHelper.navigateTo(LoginView.class);
        });

        registerBtn.addClickListener(event -> {
            RouterHelper.navigateTo(RegisterView.class);
        });
    }
}
