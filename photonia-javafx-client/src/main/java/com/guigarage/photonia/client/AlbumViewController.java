package com.guigarage.photonia.client;

import com.canoo.dolphin.client.ClientBeanManager;
import com.canoo.dolphin.client.javafx.FXBinder;
import com.guigarage.photonia.controller.album.AlbumViewBean;
import com.guigarage.photonia.controller.album.AlbumViewImageBean;
import com.guigarage.photonia.controller.library.LibraryViewBean;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import org.controlsfx.control.GridView;

public class AlbumViewController {

    @FXML
    private Label albumNameLabel;

    @FXML
    private ListView<AlbumViewImageBean> imageList;

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

    @FXML
    public void initialize() {
        imageList.setCellFactory(e -> new ImageCell(id -> routing.showImage(id)));
        backButton.setOnAction(e -> routing.showLibrary());
    }

    private void update(AlbumViewBean bean) {
        albumNameLabel.textProperty().bind(FXBinder.wrapStringProperty(bean.getName()));
        if (bean != null) {
            imageList.setItems(FXBinder.wrapList(bean.getImages()));
        }
    }

}
