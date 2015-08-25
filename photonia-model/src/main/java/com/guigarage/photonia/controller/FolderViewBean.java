package com.guigarage.photonia.controller;

import com.canoo.dolphin.collections.ObservableList;
import com.canoo.dolphin.mapping.DolphinBean;
import com.canoo.dolphin.mapping.Property;

@DolphinBean("FolderViewBean")
public class FolderViewBean {

    private Property<String> name;

    private Property<String> selectedImageId;

    private ObservableList<ImageBean> images;

    public Property<String> getName() {
        return name;
    }

    public ObservableList<ImageBean> getImages() {
        return images;
    }

    public Property<String> getSelectedImageId() {
        return selectedImageId;
    }

    @DolphinBean
    public class ImageBean {

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
}
