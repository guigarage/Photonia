package com.guigarage.photonia.client;

import com.canoo.dolphin.client.javafx.FXBinder;
import com.canoo.dolphin.event.Subscription;
import com.guigarage.photonia.controller.library.LibraryViewAlbumBean;
import javafx.concurrent.Task;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import org.controlsfx.control.GridCell;

import java.util.function.Consumer;

/**
 * Created by hendrikebbers on 24.09.15.
 */
public class LibraryGridCell extends GridCell<LibraryViewAlbumBean> {

    private ImageView imageView;

    private String lastImageUrl;

    private Subscription subscription;

    private LibraryViewAlbumBean lastItem;

    public LibraryGridCell(Consumer<String> openAlbumHandler) {
        System.out.println("INSTANCE!!!!!");
        this.imageView = new ImageView();
        imageView.fitHeightProperty().bind(heightProperty());
        imageView.fitWidthProperty().bind(widthProperty());
        imageView.setPreserveRatio(true);
        setGraphic(imageView);

        setOnMouseClicked(e -> {
            if (e.getClickCount() > 1) {
                openAlbumHandler.accept(getItem().getId().get());
            }
        });
        setStyle("-fx-border-color: red; -fx-border-width: 2px");

    }

    @Override
    protected void updateItem(LibraryViewAlbumBean item, boolean empty) {
        super.updateItem(item, empty);
        System.out.println("UPDATE: " + getIndex() + " - " + item);
        //Thread.dumpStack();

        indexProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal.intValue() == -1) {
                Thread.dumpStack();
            }
        });

        if (lastItem != null && !lastItem.equals(item)) {
            textProperty().unbind();
            if (subscription != null) {
                subscription.unsubscribe();
            }
        }

        if (item == null) {
            setText("");
            imageView.setImage(null);
            lastImageUrl = null;
            subscription = null;
        } else if (!item.equals(lastItem)) {
            textProperty().bind(FXBinder.wrapStringProperty(item.getName()));
            subscription = item.getCoverUrl().onChanged(e -> {
                //updateImage();
            });
            updateImage();
        }

        lastItem = item;
    }

    private void updateImage() {
        String imageUrl = getItem().getCoverUrl().get();
        if (imageUrl == null) {
            imageView.setImage(null);
            lastImageUrl = null;
        } else if (!imageUrl.equals(lastImageUrl)) {
            System.out.println("loading " + imageUrl + " last " + lastImageUrl + " in " + this);
            Task<Image> task = ClientImageCache.getInstance().get(imageUrl);
            task.setOnSucceeded(ev -> {
                try {
                    Image image = task.getValue();
                    imageView.setImage(image);
                } catch (Exception e1) {
                    e1.printStackTrace();
                    imageView.setImage(null);
                    lastImageUrl = null;
                }
            });
            lastImageUrl = imageUrl;
        }
    }

}
