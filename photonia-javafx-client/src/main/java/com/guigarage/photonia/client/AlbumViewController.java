package com.guigarage.photonia.client;

import com.canoo.dolphin.client.*;
import com.canoo.dolphin.client.javafx.FXBinder;
import com.guigarage.photonia.controller.album.AlbumViewBean;
import com.guigarage.photonia.controller.album.AlbumViewImageBean;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;

public class AlbumViewController extends AbstractViewController<AlbumViewBean> {

    @FXML
    private Label albumNameLabel;

    @FXML
    private ListView<AlbumViewImageBean> imageList;

    @FXML
    private Button backButton;

    private String id;

    private final Routing routing;

    public AlbumViewController(ClientContext clientContext, Routing routing, String id) {
        super(clientContext, "AlbumViewController");
        this.routing = routing;
        this.id = id;
    }

    @FXML
    public void initialize() {
        imageList.setCellFactory(e -> new ImageCell(id -> routing.showImage(id)));
        backButton.setOnAction(e -> routing.showLibrary());
    }

    @Override
    protected void init(ControllerProxy<AlbumViewBean> controller) {
        AlbumViewBean model = controller.getModel();
        model.idProperty().set(id);
        FXBinder.bindBidirectional(albumNameLabel.textProperty(), model.nameProperty());
        imageList.setItems(FXBinder.wrapList(model.imagesProperty()));
    }
}
