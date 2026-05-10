package com.ofirbsh.secure_drop.ui;

import java.io.ByteArrayInputStream;

import com.ofirbsh.secure_drop.datamodels.FileMetadata;
import com.ofirbsh.secure_drop.datamodels.User;
import com.ofirbsh.secure_drop.services.FileService;
import com.ofirbsh.secure_drop.utilities.SessionHelper;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.Grid.SelectionMode;
import com.vaadin.flow.component.html.Anchor;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.messages.MessageInput;
import com.vaadin.flow.component.messages.MessageInputI18n;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.Notification.Position;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
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

    // Layouts
    private HorizontalLayout fileManagmentLayout;
    private Dialog shareDialog;

    // אלמנטים
    private Upload uploadFile;
    private Grid<FileMetadata> grid;
    private Button downloadBtn;
    private Button deleteBtn;
    private Button shareBtn;
    private Button cancelShare;
    private Button acceptShare;

    // משתנים
    private FileMetadata selectedFile;
    private User user;

    public HomeView(FileService fileService)
    {
        this.fileService = fileService;

        user = (User) SessionHelper.getAttribute("User");

        // אלמנטים
        uploadFile = new Upload();
        grid = new Grid<>(FileMetadata.class);
        shareDialog = new Dialog("Share File");

        fileManagmentLayout = new HorizontalLayout();
        downloadBtn = new Button("download");
        deleteBtn = new Button("Delete");
        shareBtn = new Button("Share File");
        acceptShare = new Button("Accept");
        cancelShare = new Button("Cancel");
        downloadBtn.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        acceptShare.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        deleteBtn.addThemeVariants(ButtonVariant.LUMO_ERROR);

        // משתנים
        selectedFile = null;

        uploadFile.setMaxFiles(1);
        uploadFile.setDropLabel(new Span("Drop file here"));
        uploadFile.setUploadButton(new Button("Upload File"));

        // הגדרות דיפולטיביות
        fileManagmentLayout.add(downloadBtn);
        fileManagmentLayout.add(shareBtn);
        fileManagmentLayout.add(deleteBtn);
        fileManagmentLayout.setWidthFull();
        fileManagmentLayout.setAlignItems(Alignment.CENTER);
        fileManagmentLayout.setJustifyContentMode(JustifyContentMode.CENTER);
        fileManagmentLayout.setVisible(false);

        // Upload Handler
        // FIXME: Change to InputStream
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
        grid.setColumns("fileName", "uploadDate", "sharedUsers");
        grid.addColumn(file -> formatFileSize(file.getBytesSize())).setHeader("File Size");
        grid.setSelectionMode(SelectionMode.SINGLE);

        grid.asSingleSelect().addValueChangeListener(event -> {
            this.selectedFile = event.getValue();

            if (selectedFile == null) 
            {
                System.out.println("No File Selected");
                fileManagmentLayout.setVisible(false);
                
                return;
            }

            System.out.println("File Selected: " + selectedFile.getFileName());
            fileManagmentLayout.setVisible(true);
        });

        if(user != null)
        {
            grid.setItems(fileService.getAllFileByUsername(user.getUsername()));
        }

        // Download Button
        downloadBtn.addClickListener(event -> {
            downloadSelectedFile();
        });

        // Share
        FormLayout dialogLayout = new FormLayout();
        shareDialog.add(dialogLayout);

        TextField usernameField = new TextField("Username");

        dialogLayout.add(usernameField);
        shareDialog.getFooter().add(cancelShare);
        shareDialog.getFooter().add(acceptShare);
        shareBtn.addClickListener(event -> {
            shareDialog.open();
        });

        cancelShare.addClickListener(event -> {
            shareDialog.close();
        });

        acceptShare.addClickListener(event -> {
            String username = usernameField.getValue();

            if (username.length() < 6) 
            {
                // FIXME: Add Notification
                shareDialog.close();
                return;
            }

            fileService.addSharedUser(selectedFile.getId(), username);
            grid.setItems(fileService.getAllFileByUsername(user.getUsername()));
            shareDialog.close();
        });

        // Delete Button
        deleteBtn.addClickListener(event -> {
            fileService.deleteFile(selectedFile.getId());
            grid.setItems(fileService.getAllFileByUsername(user.getUsername()));
        });

        // Page
        add(uploadFile);
        add(grid);
        add(fileManagmentLayout);
    }

    /**
     * מחזיר את גודל הקובץ לפי פורמט MB\KB
     * @param bytes מספר הבתים
     * @return
     */
    private String formatFileSize(int bytes) 
    {
        if (bytes < 1024)
            return bytes + " B";

        double kb = bytes / 1024.0;

        if (kb < 1024)
            return String.format("%.2f KB", kb);

        double mb = kb / 1024.0;

        return String.format("%.2f MB", mb);
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
