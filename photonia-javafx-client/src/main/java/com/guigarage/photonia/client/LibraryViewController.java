package com.guigarage.photonia.client;

import com.canoo.dolphin.client.AbstractViewController;
import com.canoo.dolphin.client.ClientContext;
import com.canoo.dolphin.client.ControllerProxy;
import com.canoo.dolphin.client.javafx.FXBinder;
import com.guigarage.photonia.controller.library.LibraryViewAlbumBean;
import com.guigarage.photonia.controller.library.LibraryViewBean;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;

public class LibraryViewController extends AbstractViewController<LibraryViewBean> {

    @FXML
    private Button newAlbumButton;

    @FXML
    private ListView<LibraryViewAlbumBean> albumList;

    private final Routing routing;

    public LibraryViewController(ClientContext clientContext, Routing routing) {
        super(clientContext, "LibraryViewController");
        this.routing = routing;
    }

    @FXML
    public void initialize() {
        albumList.setCellFactory(c -> new AlbumCell(s -> routing.showAlbum(s)));
    }

    @Override
    protected void init(ControllerProxy<LibraryViewBean> controller) {
        LibraryViewBean model = controller.getModel();
        albumList.setItems(FXBinder.wrapList(model.getAlbums()));
    }
}
