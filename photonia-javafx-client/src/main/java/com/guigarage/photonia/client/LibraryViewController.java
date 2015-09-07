package com.guigarage.photonia.client;

import com.canoo.dolphin.client.ClientBeanManager;
import com.canoo.dolphin.client.javafx.FXBinder;
import com.guigarage.photonia.controller.library.LibraryViewAlbumBean;
import com.guigarage.photonia.controller.library.LibraryViewBean;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import org.controlsfx.control.GridView;

public class LibraryViewController {

    @FXML
    private Button newAlbumButton;

    @FXML
    private ListView<LibraryViewAlbumBean> albumList;

    private final ClientBeanManager beanManager;

    private final Routing routing;

    public LibraryViewController(ClientBeanManager beanManager, Routing routing) {
        this.beanManager = beanManager;
        this.routing = routing;

        beanManager.onAdded(LibraryViewBean.class, bean -> update(bean));
        beanManager.send("LibraryViewController:initView");
    }

    @FXML
    public void initialize() {
       albumList.setCellFactory(c -> new AlbumCell(s -> routing.showAlbum(s)));
    }

    private void update(LibraryViewBean libraryViewBean) {
        albumList.setItems(null);
        if (libraryViewBean != null) {
            albumList.setItems(FXBinder.wrapList(libraryViewBean.getAlbums()));
        }
    }
}
