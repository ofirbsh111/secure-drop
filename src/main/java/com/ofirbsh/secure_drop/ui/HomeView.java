package com.ofirbsh.secure_drop.ui;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.select.Select;
import com.vaadin.flow.component.upload.Upload;
import com.vaadin.flow.router.Route;

@Route("/home")
public class HomeView extends VerticalLayout
{
    private Upload uploadFile;
    private Select<String> selectFile;
    private Button downloadFile;

    public HomeView()
    {
        uploadFile = new Upload();
        selectFile = new Select<>();
        downloadFile = new Button("download");

        HorizontalLayout hozLayout = new HorizontalLayout();

        selectFile.setLabel("Select File");
        selectFile.setItems(); // add file names from db

        hozLayout.setAlignItems(Alignment.BASELINE);
        hozLayout.add(selectFile);
        hozLayout.add(downloadFile);

        add(uploadFile);
        add(hozLayout);
    }
}
