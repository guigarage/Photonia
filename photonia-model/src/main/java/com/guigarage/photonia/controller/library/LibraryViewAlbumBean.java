package com.guigarage.photonia.controller.library;

import com.canoo.dolphin.mapping.DolphinBean;
import com.canoo.dolphin.mapping.Property;

@DolphinBean
public class LibraryViewAlbumBean {

    private Property<Integer> imageCount;

    private Property<String> name;

    private Property<String> coverUrl;

    private Property<String> id;

    public Property<String> getId() {
        return id;
    }

    public Property<Integer> getImageCount() {
        return imageCount;
    }

    public Property<String> getName() {
        return name;
    }

    public Property<String> getCoverUrl() {
        return coverUrl;
    }
}
