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
import javafx.scene.control.Slider;
import org.controlsfx.control.GridView;

public class LibraryViewController extends AbstractViewController<LibraryViewBean> {

    @FXML
    private Button newAlbumButton;

    @FXML
    private GridView<LibraryViewAlbumBean> albumGrid;

    @FXML
    private Slider imageSizeSlider;

    private final Routing routing;

    public LibraryViewController(ClientContext clientContext, Routing routing) {
        super(clientContext, "LibraryViewController");
        this.routing = routing;
    }

    @FXML
    public void initialize() {
        imageSizeSlider.setMin(64);
        imageSizeSlider.setMax(400);
        imageSizeSlider.valueProperty().bindBidirectional(albumGrid.cellHeightProperty());
        imageSizeSlider.valueProperty().bindBidirectional(albumGrid.cellWidthProperty());
        albumGrid.setCellFactory(c -> new LibraryGridCell(s -> routing.showAlbum(s)));
    }

    @Override
    protected void init(ControllerProxy<LibraryViewBean> controller) {
        LibraryViewBean model = controller.getModel();
        albumGrid.setItems(FXBinder.wrapList(model.getAlbums()));
    }
}
