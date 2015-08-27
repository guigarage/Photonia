package com.guigarage.photonia.controller.album;

import com.canoo.dolphin.collections.ObservableList;
import com.canoo.dolphin.mapping.DolphinBean;
import com.canoo.dolphin.mapping.Property;

@DolphinBean("AlbumViewBean")
public class AlbumViewBean {

    private Property<String> name;

    private Property<String> selectedImageId;

    private ObservableList<AlbumViewImageBean> images;

    public Property<String> getName() {
        return name;
    }

    public ObservableList<AlbumViewImageBean> getImages() {
        return images;
    }

    public Property<String> getSelectedImageId() {
        return selectedImageId;
    }

}
