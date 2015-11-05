package com.guigarage.photonia.controller;

import com.canoo.dolphin.server.DolphinAction;
import com.canoo.dolphin.server.DolphinController;
import com.canoo.dolphin.server.DolphinModel;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

@DolphinController("ImageViewController")
public class ImageViewController extends AbstractController implements ImageObserver {

    @DolphinModel
    private ImageViewBean model;

    @Inject
    public ImageViewController() {
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

    @DolphinAction("showNext")
    public void onShowNext() {
        model.idProperty().set(model.nextIdProperty().get());
    }

    @DolphinAction("showPrev")
    public void onShowPrev() {
        model.idProperty().set(model.prevIdProperty().get());
    }

    @DolphinAction("backToFolder")
    public void onBackToFolder() {
        throw new RuntimeException("Not Implemented");
    }

    @DolphinAction("backToAlbum")
    public void onBackToAlbum() {
        throw new RuntimeException("Not Implemented");
    }

    @DolphinAction("delete")
    public void onDelete() {
        throw new RuntimeException("Not Implemented");
    }

    @Override
    public void imageChanged(String id) {
        if (model.idProperty().equals(id)) {
            update();
        }
    }
}
