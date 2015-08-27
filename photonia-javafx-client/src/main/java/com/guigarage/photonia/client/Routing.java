package com.guigarage.photonia.client;

import com.canoo.dolphin.client.ClientBeanManager;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.layout.StackPane;

public class Routing {

    private StackPane mainPane;

    private ClientBeanManager beanManager;

    public Routing(StackPane mainPane, ClientBeanManager beanManager) {
        this.mainPane = mainPane;
        this.beanManager = beanManager;
    }

    public void showLibrary() {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(LibraryViewController.class.getResource("libraryView.fxml"));
            loader.setController(new LibraryViewController(beanManager, this));
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
            loader.setController(new AlbumViewController(beanManager, this, id));
            Parent view = loader.load();
            mainPane.getChildren().clear();
            mainPane.getChildren().add(view);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void showImage(String id) {

    }

}
