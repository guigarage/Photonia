package com.guigarage.photonia.client;

import com.canoo.dolphin.client.AbstractViewController;
import com.canoo.dolphin.client.ClientContext;
import com.canoo.dolphin.client.ControllerProxy;
import com.canoo.dolphin.client.javafx.FXBinder;
import com.guigarage.photonia.controller.album.AlbumViewBean;
import com.guigarage.photonia.controller.album.AlbumViewImageBean;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import org.controlsfx.control.GridView;

public class AlbumViewController extends AbstractViewController<AlbumViewBean> {

    @FXML
    private Label albumNameLabel;

    @FXML
    private GridView<AlbumViewImageBean> imageGrid;

    @FXML
    private Button backButton;

    @FXML
    private Slider imageSizeSlider;

    private String id;

    private final Routing routing;

    public AlbumViewController(ClientContext clientContext, Routing routing, String id) {
        super(clientContext, "AlbumViewController");
        this.routing = routing;
        this.id = id;
    }

    @FXML
    public void initialize() {
        imageGrid.setCellFactory(e -> new AlbumGridCell(id -> routing.showImage(id)));
        backButton.setOnAction(e -> {
            routing.showLibrary();
        });
        imageSizeSlider.setMin(64);
        imageSizeSlider.setMax(400);
        imageSizeSlider.valueProperty().bindBidirectional(imageGrid.cellHeightProperty());
        imageSizeSlider.valueProperty().bindBidirectional(imageGrid.cellWidthProperty());
    }

    @Override
    protected void init(ControllerProxy<AlbumViewBean> controller) {
        AlbumViewBean model = controller.getModel();
        model.idProperty().set(id);
        FXBinder.bindBidirectional(albumNameLabel.textProperty(), model.nameProperty());
        imageGrid.setItems(FXBinder.wrapList(model.imagesProperty()));
    }
}
