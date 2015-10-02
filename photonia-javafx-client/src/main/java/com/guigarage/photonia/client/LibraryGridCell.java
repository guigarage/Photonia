package com.guigarage.photonia.client;

import com.canoo.dolphin.client.javafx.FXBinder;
import com.guigarage.photonia.controller.library.LibraryViewAlbumBean;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import org.controlsfx.control.GridCell;

import java.util.function.Consumer;

/**
 * Created by hendrikebbers on 24.09.15.
 */
public class LibraryGridCell extends GridCell<LibraryViewAlbumBean> {

    private ImageView imageView;

    public LibraryGridCell(Consumer<String> openAlbumHandler) {
        this.imageView = new ImageView();
        imageView.fitHeightProperty().bind(heightProperty());
        imageView.fitWidthProperty().bind(widthProperty());
        imageView.setPreserveRatio(true);
        setGraphic(imageView);

        setOnMouseClicked(e -> {
            if(e.getClickCount() > 1) {
                openAlbumHandler.accept(getItem().getId().get());
            }
        });
    }

    @Override
    protected void updateItem(LibraryViewAlbumBean item, boolean empty) {
        super.updateItem(item, empty);

        imageView.setImage(null);
        textProperty().unbind();
        setText("");

        if (item != null) {
            textProperty().bind(FXBinder.wrapStringProperty(item.getName()));
            if (item.getCoverUrl().get() != null) {
                imageView.setImage(new Image(item.getCoverUrl().get()));
            }
        }
    }
}
