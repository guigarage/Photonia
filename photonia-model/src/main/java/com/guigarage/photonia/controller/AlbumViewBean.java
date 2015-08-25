package com.guigarage.photonia.controller;

import com.canoo.dolphin.collections.ObservableList;
import com.canoo.dolphin.mapping.DolphinBean;
import com.canoo.dolphin.mapping.Property;

@DolphinBean("AlbumViewBean")
public class AlbumViewBean {

    private ObservableList<FolderBean> folders;

    public ObservableList<FolderBean> getFolders() {
        return folders;
    }

    @DolphinBean
    public class FolderBean {

        private Property<Integer> imageCount;

        private Property<String> name;

        private Property<String> coverUrl;

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
}
