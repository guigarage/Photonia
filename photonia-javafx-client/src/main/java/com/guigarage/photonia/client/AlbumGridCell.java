package com.guigarage.photonia.client;

import com.guigarage.photonia.controller.album.AlbumViewImageBean;
import javafx.concurrent.Task;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import org.controlsfx.control.GridCell;

import java.util.function.Consumer;

/**
 * Created by hendrikebbers on 04.10.15.
 */
public class AlbumGridCell extends GridCell<AlbumViewImageBean> {

    private ImageView imageView;

    public AlbumGridCell(Consumer<String> openImageHandler) {
        this.imageView = new ImageView();
        imageView.fitHeightProperty().bind(heightProperty());
        imageView.fitWidthProperty().bind(widthProperty());
        imageView.setPreserveRatio(true);
        setGraphic(imageView);

        setOnMouseClicked(e -> {
            if (e.getClickCount() > 1) {
                openImageHandler.accept(getItem().getId().get());
            }
        });
        setStyle("-fx-border-color: red; -fx-border-width: 2px");
    }

    private String lastImageUrl;

    @Override
    protected void updateItem(AlbumViewImageBean item, boolean empty) {
        super.updateItem(item, empty);
        setText("");
        if (item != null) {
            if (item.getThumbnailUrl().get() != null && !item.getThumbnailUrl().get().equals(lastImageUrl)) {
                imageView.setImage(null);
                Task<Image> task = ClientImageCache.getInstance().get(item.getThumbnailUrl().get());
                task.setOnSucceeded(e -> {
                    try {
                        Image image = task.getValue();
                        imageView.setImage(image);
                        requestLayout();
                    } catch (Exception e1) {
                        e1.printStackTrace();
                    }
                });
                lastImageUrl = item.getThumbnailUrl().get();
            }
        }
    }
}
