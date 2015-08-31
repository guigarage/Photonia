package com.guigarage.photonia.controller.album;

import com.canoo.dolphin.mapping.DolphinBean;
import com.canoo.dolphin.mapping.Property;

@DolphinBean
public class AlbumViewImageBean {

    private Property<Integer> rating;

    private Property<Boolean> hasRaw;

    private Property<String> id;

    private Property<String> thumbnailUrl;

    public Property<Integer> getRating() {
        return rating;
    }

    public Property<Boolean> getHasRaw() {
        return hasRaw;
    }

    public Property<String> getId() {
        return id;
    }

    public Property<String> getThumbnailUrl() {
        return thumbnailUrl;
    }
}
