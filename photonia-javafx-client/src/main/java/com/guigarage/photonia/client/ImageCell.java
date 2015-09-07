package com.guigarage.photonia.client;

import com.canoo.dolphin.client.javafx.FXBinder;
import com.guigarage.photonia.controller.album.AlbumViewImageBean;
import com.guigarage.photonia.controller.library.LibraryViewAlbumBean;
import javafx.scene.control.ListCell;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.shape.Rectangle;
import org.controlsfx.control.GridCell;

import java.util.function.Consumer;

public class ImageCell extends ListCell<AlbumViewImageBean> {

    private ImageView imageView;

    public ImageCell(Consumer<String> openImageHandler) {
        this.imageView = new ImageView();

        imageView.setPreserveRatio(true);
        imageView.setFitHeight(128);

        Rectangle clip = new Rectangle();
        clip.setX(0);
        clip.setY(0);
        clip.widthProperty().bind(widthProperty());
        clip.heightProperty().bind(heightProperty());
        setClip(clip);

        setGraphic(imageView);

        setOnMouseClicked(e -> {
            if(e.getClickCount() > 1) {
                openImageHandler.accept(getItem().getId().get());
            }
        });
    }

    @Override
    protected void updateItem(AlbumViewImageBean item, boolean empty) {
        super.updateItem(item, empty);

        imageView.setImage(null);
        textProperty().unbind();
        setText("");

        if (item != null) {
            if (item.getThumbnailUrl().get() != null) {
                imageView.setImage(new Image(item.getThumbnailUrl().get()));
            }
        }
    }
}
