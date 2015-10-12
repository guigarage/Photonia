package com.guigarage.photonia.controller.library;

import com.canoo.dolphin.collections.ObservableList;
import com.canoo.dolphin.mapping.DolphinBean;
import com.canoo.dolphin.mapping.Property;

@DolphinBean("LibraryViewBean")
public class LibraryViewBean {

    private Property<String> albumCount;

    public Property<String> getAlbumCount() {
        return albumCount;
    }

    private ObservableList<LibraryViewAlbumBean> albums;

    public ObservableList<LibraryViewAlbumBean> getAlbums() {
        return albums;
    }

}
