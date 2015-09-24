package com.guigarage.photonia.controller.album;

import com.canoo.dolphin.collections.ObservableList;
import com.canoo.dolphin.mapping.DolphinBean;
import com.canoo.dolphin.mapping.Property;

@DolphinBean("AlbumViewBean")
public class AlbumViewBean {

    private Property<String> id;

    private Property<String> name;

    private Property<String> selectedImageId;

    private ObservableList<AlbumViewImageBean> images;

    public Property<String> nameProperty() {
        return name;
    }

    public ObservableList<AlbumViewImageBean> imagesProperty() {
        return images;
    }

    public Property<String> selectedImageIdProperty() {
        return selectedImageId;
    }

    public String getSelectedImageId() {
        return selectedImageId.get();
    }

    public String getId() {
        return id.get();
    }

    public Property<String> idProperty() {
        return id;
    }
}
