package com.guigarage.photonia.client;

import com.canoo.dolphin.client.ClientContext;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.layout.StackPane;

public class Routing {

    private StackPane mainPane;

    private ClientContext clientContext;

    public Routing(StackPane mainPane, ClientContext clientContext) {
        this.mainPane = mainPane;
        this.clientContext = clientContext;
    }

    public void showLibrary() {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(LibraryViewController.class.getResource("libraryView.fxml"));
            loader.setController(new LibraryViewController(clientContext, this));
            Parent view = loader.load();
            mainPane.getChildren().clear();
            mainPane.getChildren().add(view);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void showAlbum(String id) {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(AlbumViewController.class.getResource("albumView.fxml"));
            loader.setController(new AlbumViewController(clientContext, this, id));
            Parent view = loader.load();
            mainPane.getChildren().clear();
            mainPane.getChildren().add(view);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void showImage(String id) {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(AlbumViewController.class.getResource("imageView.fxml"));
            loader.setController(new ImageViewController(clientContext, this, id));
            Parent view = loader.load();
            mainPane.getChildren().clear();
            mainPane.getChildren().add(view);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}
