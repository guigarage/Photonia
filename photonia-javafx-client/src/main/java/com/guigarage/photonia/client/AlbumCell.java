package com.guigarage.photonia.client;

import com.canoo.dolphin.client.javafx.FXBinder;
import com.guigarage.photonia.controller.library.LibraryViewAlbumBean;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import org.controlsfx.control.GridCell;

import java.util.function.Consumer;

public class AlbumCell extends GridCell<LibraryViewAlbumBean> {

    private ImageView imageView;


    public AlbumCell(Consumer<String> openAlbumHandler) {
        this.imageView = new ImageView();

        imageView.setPreserveRatio(true);
        imageView.fitWidthProperty().bind(widthProperty());
        imageView.fitHeightProperty().bind(heightProperty());

        setGraphic(imageView);

        setOnMouseClicked(e -> openAlbumHandler.accept(getItem().getId().get()));
    }

    @Override
    protected void updateItem(LibraryViewAlbumBean item, boolean empty) {
        super.updateItem(item, empty);

        imageView.setImage(null);
        textProperty().unbind();
        setText("");

        if(item != null) {
            textProperty().bind(FXBinder.wrapStringProperty(item.getName()).concat(" (").concat(FXBinder.wrapIntProperty(item.getImageCount()).asString()).concat(")"));
            if(item.getCoverUrl().get() != null) {
                imageView.setImage(new Image(item.getCoverUrl().get()));
            }
        }
    }
}
