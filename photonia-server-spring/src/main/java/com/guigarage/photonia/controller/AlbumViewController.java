package com.guigarage.photonia.controller;

import com.canoo.dolphin.server.DolphinAction;
import com.canoo.dolphin.server.DolphinController;
import com.canoo.dolphin.server.DolphinModel;
import com.guigarage.photonia.controller.album.AlbumViewBean;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

@DolphinController("AlbumViewController")
public class AlbumViewController extends AbstractController implements AlbumObserver, ImageObserver {

    @DolphinModel
    private AlbumViewBean model;

    @Inject
    public AlbumViewController() {
    }

    @PostConstruct
    public void init() {
        model.idProperty().onChanged(e -> {
            update();
        });
    }

    private void update() {
        throw new RuntimeException("Not Implemented");
    }

    @DolphinAction("backToAlbum")
    public void onBackToAlbum() {
        throw new RuntimeException("Not Implemented");
    }

    @DolphinAction("deleteSelected")
    public void onDeleteSelected() {
        throw new RuntimeException("Not Implemented");
    }

    @Override
    public void albumChanged(String id) {
        throw new RuntimeException("Not Implemented");
    }

    @Override
    public void imageChanged(String id) {
        throw new RuntimeException("Not Implemented");
    }
}
