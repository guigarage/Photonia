package com.guigarage.photonia.client;

import com.canoo.dolphin.client.ClientBeanManager;
import com.canoo.dolphin.client.javafx.FXBinder;
import com.guigarage.photonia.controller.library.LibraryViewAlbumBean;
import com.guigarage.photonia.controller.library.LibraryViewBean;
import javafx.fxml.FXML;
import org.controlsfx.control.GridView;

public class LibraryViewController {

    @FXML
    private GridView<LibraryViewAlbumBean> albumGrid;

    private final ClientBeanManager beanManager;

    private final Routing routing;

    public LibraryViewController(ClientBeanManager beanManager, Routing routing) {
        this.beanManager = beanManager;
        this.routing = routing;

        beanManager.onAdded(LibraryViewBean.class, bean -> update(bean));
        beanManager.send("LibraryViewController:initView");
    }

    public void initialize() {
        albumGrid.setCellFactory(e -> new AlbumCell(id -> routing.showAlbum(id)));
        albumGrid.setCellHeight(340);
        albumGrid.setCellWidth(340);
    }

    private void update(LibraryViewBean libraryViewBean) {
        albumGrid.setItems(null);
        if (libraryViewBean != null) {
            albumGrid.setItems(FXBinder.wrapList(libraryViewBean.getAlbums()));
        }
    }
}
