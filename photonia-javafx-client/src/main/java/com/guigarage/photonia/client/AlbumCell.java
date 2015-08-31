package com.guigarage.photonia.client;

import com.canoo.dolphin.client.javafx.FXBinder;
import com.guigarage.photonia.controller.library.LibraryViewAlbumBean;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.shape.Rectangle;
import org.controlsfx.control.GridCell;

import java.util.function.Consumer;

public class AlbumCell extends GridCell<LibraryViewAlbumBean> {

    private ImageView imageView;


    public AlbumCell(Consumer<String> openAlbumHandler) {
        this.imageView = new ImageView();

        imageView.setPreserveRatio(true);
        //imageView.fitWidthProperty().bind(widthProperty());
        //imageView.fitHeightProperty().bind(heightProperty());
        setStyle("-fx-border-width: 2; -fx-border-color: red");

        Rectangle clip = new Rectangle();
        clip.setX(0);
        clip.setY(0);
        clip.widthProperty().bind(widthProperty());
        clip.heightProperty().bind(heightProperty());
        setClip(clip);

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
            String name = item.getName().get();
            System.out.println(name);
            //textProperty().bind(FXBinder.wrapStringProperty();
            if(item.getCoverUrl().get() != null) {
                imageView.setImage(new Image(item.getCoverUrl().get()));
            }
        }
    }
}
