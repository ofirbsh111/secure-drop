package com.ofirbsh.secure_drop.ui;

import com.ofirbsh.secure_drop.datamodels.User;
import com.ofirbsh.secure_drop.services.FileService;
import com.ofirbsh.secure_drop.utilities.SessionHelper;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.select.Select;
import com.vaadin.flow.component.upload.Upload;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.streams.InMemoryUploadHandler;
import com.vaadin.flow.server.streams.UploadHandler;
import com.vaadin.flow.server.streams.UploadMetadata;

@Route("/home")
public class HomeView extends VerticalLayout
{
    // סרביסים
    private final FileService fileService;

    // אלמנטים
    private Upload uploadFile;
    private Select<String> selectFile;
    private Button downloadFile;

    public HomeView(FileService fileService)
    {
        this.fileService = fileService;

        uploadFile = new Upload();
        selectFile = new Select<>();
        downloadFile = new Button("download");

        InMemoryUploadHandler inMemoryHandler = UploadHandler
        .inMemory((metadata, data) -> {
            String fileName = metadata.fileName();
            String fileType = metadata.contentType();
            // long contentLength = metadata.contentLength();

            proccessFile(metadata, data);

            System.out.println("==> File Uploaded: [Name: " + fileName + ", Type: " + fileType + "]");
        });

        uploadFile.setUploadHandler(inMemoryHandler);

        HorizontalLayout hozLayout = new HorizontalLayout();

        selectFile.setLabel("Select File");
        selectFile.setItems(); // add file names from db

        hozLayout.setAlignItems(Alignment.BASELINE);
        hozLayout.add(selectFile);
        hozLayout.add(downloadFile);

        add(uploadFile);
        add(hozLayout);
    }

    /**
     * מעבד את הקובץ שהתקבל ושולח אותו לשרת להצפנה ושמירה
     * @param metadata מאפיינים של הקובץ
     * @param data הקובץ עצמו
     */
    public void proccessFile(UploadMetadata metadata, byte[] data)
    {
        User Owner = (User) SessionHelper.getAttribute("User");
        fileService.proccessFile(metadata, data, Owner);
    }
}
