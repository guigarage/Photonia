package com.guigarage.photonia.controller;

import com.canoo.dolphin.server.DolphinController;
import com.canoo.dolphin.server.DolphinModel;
import com.guigarage.photonia.controller.library.LibraryViewBean;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

@DolphinController("LibraryViewController")
public class LibraryViewController extends AbstractController implements AlbumObserver {

    @DolphinModel
    private LibraryViewBean model;

    @Inject
    public LibraryViewController() {
    }

    @PostConstruct
    private void updateBean() {

    }


    @Override
    public void albumChanged(String id) {
        throw new RuntimeException("Not Implemented");
    }
}
