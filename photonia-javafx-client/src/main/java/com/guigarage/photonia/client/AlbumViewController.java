package com.guigarage.photonia.client;

import com.canoo.dolphin.client.ClientBeanManager;
import com.canoo.dolphin.client.javafx.FXBinder;
import com.guigarage.photonia.controller.album.AlbumViewBean;
import com.guigarage.photonia.controller.album.AlbumViewImageBean;
import com.guigarage.photonia.controller.library.LibraryViewBean;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import org.controlsfx.control.GridView;

public class AlbumViewController {

    @FXML
    private Label titleLabel;

    @FXML
    private GridView<AlbumViewImageBean> imageGrid;

    @FXML
    private Button backButton;

    private final ClientBeanManager beanManager;

    private final Routing routing;

    public AlbumViewController(ClientBeanManager beanManager, Routing routing, String id) {
        this.beanManager = beanManager;
        this.routing = routing;

        beanManager.onAdded(AlbumViewBean.class, bean -> update(bean));
        beanManager.send("AlbumViewController:initView", new ClientBeanManager.Param("id", id));
    }

    public void initialize() {
        imageGrid.setCellFactory(e -> new ImageCell(id -> routing.showImage(id)));
        imageGrid.setCellHeight(340);
        imageGrid.setCellWidth(340);
        backButton.setOnAction(e -> routing.showLibrary());
    }

    private void update(AlbumViewBean bean) {
        titleLabel.textProperty().bind(FXBinder.wrapStringProperty(bean.getName()));
        if (bean != null) {
            imageGrid.setItems(FXBinder.wrapList(bean.getImages()));
        }
    }

}
