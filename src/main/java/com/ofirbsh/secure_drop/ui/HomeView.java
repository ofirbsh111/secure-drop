package com.ofirbsh.secure_drop.ui;

import java.io.ByteArrayInputStream;

import com.ofirbsh.secure_drop.datamodels.FileModel;
import com.ofirbsh.secure_drop.datamodels.User;
import com.ofirbsh.secure_drop.services.FileService;
import com.ofirbsh.secure_drop.utilities.SessionHelper;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.Grid.SelectionMode;
import com.vaadin.flow.component.html.Anchor;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.Notification.Position;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.upload.Upload;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.StreamResource;
import com.vaadin.flow.server.streams.InMemoryUploadHandler;
import com.vaadin.flow.server.streams.UploadHandler;
import com.vaadin.flow.server.streams.UploadMetadata;

@Route(value = "home", layout = MainLayout.class)
public class HomeView extends VerticalLayout implements BeforeEnterObserver
{
    // סרביסים
    private final FileService fileService;

    // אלמנטים
    private Upload uploadFile;
    private Grid<FileModel> grid;
    private Button downloadFile;

    // משתנים
    private FileModel selectedFile;

    public HomeView(FileService fileService)
    {
        this.fileService = fileService;

        User user = (User) SessionHelper.getAttribute("User");

        // אלמנטים
        uploadFile = new Upload();
        grid = new Grid<>(FileModel.class);
        downloadFile = new Button("download");

        // משתנים
        selectedFile = null;

        uploadFile.setMaxFiles(1);
        uploadFile.setDropLabel(new Span("Drop file here"));
        uploadFile.setUploadButton(new Button("Upload File"));

        // Upload Handler
        InMemoryUploadHandler inMemoryHandler = UploadHandler
        .inMemory((metadata, data) -> {
            String fileName = metadata.fileName();
            String fileType = metadata.contentType();
            // long contentLength = metadata.contentLength();

            proccessFile(metadata, data);

            System.out.println("==> File Uploaded: [Name: " + fileName + ", Type: " + fileType + "]");
        });
        uploadFile.setUploadHandler(inMemoryHandler);

        // Files Grid
        grid.setWidthFull();
        grid.setColumns("fileName", "uploadDate");
        grid.setSelectionMode(SelectionMode.SINGLE);

        grid.asSingleSelect().addValueChangeListener(event -> {
            this.selectedFile = event.getValue();

            if (selectedFile == null) 
            {
                System.out.println("No File Selected");
                return;
            }

            System.out.println("File Selected: " + selectedFile.getFileName());
        });

        if(user != null)
        {
            grid.setItems(fileService.getAllFileByUsername(user.getUsername()));
        }

        // Download Button
        downloadFile.addClickListener(event -> {
            downloadSelectedFile();
        });

        // Page
        add(uploadFile);
        add(grid);
        add(downloadFile);
    }

    /**
     * מעבד את הקובץ שהתקבל ושולח אותו לשרת להצפנה ושמירה
     * @param metadata מאפיינים של הקובץ
     * @param data הקובץ עצמו
     */
    public void proccessFile(UploadMetadata metadata, byte[] data)
    {
        User owner = (User) SessionHelper.getAttribute("User");

        UI ui = UI.getCurrent();
        uploadFile.setEnabled(false);
        Notification.show("Uploading file...", 2000, Position.BOTTOM_END);

        new Thread(() -> {
            fileService.proccessFile(metadata, data, owner);

            ui.access(() -> {
                grid.setItems(fileService.getAllFileByUsername(owner.getUsername()));
                uploadFile.setEnabled(true);
                Notification.show("File uploaded successfully", 2000, Position.BOTTOM_END);
            });
        }).start();
    }

    public void downloadSelectedFile()
    {
        if (selectedFile == null)
        {
            Notification.show("Select a File to download", 2000, Position.BOTTOM_END);
            return;
        }

        User owner = (User) SessionHelper.getAttribute("User");
        byte[] decryptFile = fileService.donwloadFile(selectedFile, owner);

        StreamResource resource = new StreamResource(selectedFile.getFileName(), () -> new ByteArrayInputStream(decryptFile));

        Anchor downloadLink = new Anchor(resource, "");
        downloadLink.getElement().setAttribute("download", true);
        downloadLink.getStyle().set("display", "none");
        
        add(downloadLink);

        downloadLink.getElement().executeJs("this.click();");
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
