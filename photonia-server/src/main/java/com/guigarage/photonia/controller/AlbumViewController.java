package com.guigarage.photonia.controller;

import com.canoo.dolphin.BeanManager;
import com.canoo.dolphin.server.DolphinAction;
import com.canoo.dolphin.server.DolphinController;
import com.canoo.dolphin.server.DolphinModel;
import com.canoo.dolphin.server.Param;
import com.canoo.dolphin.server.event.DolphinEventBus;
import com.canoo.dolphin.server.event.TaskExecutor;
import com.guigarage.photonia.controller.album.AlbumViewBean;
import com.guigarage.photonia.controller.album.AlbumViewImageBean;
import com.guigarage.photonia.folder.ImageFolder;
import com.guigarage.photonia.service.AsyncService;
import com.guigarage.photonia.service.PhotoniaService;
import com.guigarage.photonia.types.JpegImageFile;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import java.io.IOException;

@DolphinController("AlbumViewController")
public class AlbumViewController extends AbstractController implements AlbumObserver, ImageObserver{

    private final DolphinEventBus eventBus;

    private final BeanManager beanManager;

    private final PhotoniaService photoniaService;

    private final AsyncService asyncService;

    @DolphinModel
    private AlbumViewBean model;

    private ImageFolder folder;

    @Inject
    public AlbumViewController(BeanManager beanManager, DolphinEventBus eventBus, PhotoniaService photoniaService, AsyncService asyncService) {
        this.beanManager = beanManager;
        this.eventBus = eventBus;
        this.photoniaService = photoniaService;
        this.asyncService = asyncService;
    }

    @PostConstruct
    public void init() {
        model.idProperty().onChanged(e -> {
            folder = photoniaService.getAlbum().getFolderById(model.getId());
            model.selectedImageIdProperty().set(null);
            model.nameProperty().set(folder.getName());
            model.imagesProperty().clear();
            beanManager.removeAll(AlbumViewImageBean.class);
            for (JpegImageFile imageFile : folder.getImages()) {
                AlbumViewImageBean imageBean = beanManager.create(AlbumViewImageBean.class);
                imageBean.getId().set(imageFile.getUuid());
                imageBean.getHasRaw().set(imageFile.getRawImageFile() != null);
                imageBean.getRating().set(imageFile.getRating());
                imageBean.getThumbnailUrl().set(photoniaService.getImageThumbnailUrl(imageFile.getUuid()));
                model.imagesProperty().add(imageBean);
            }
        });
    }

    @DolphinAction("backToAlbum")
    public void onBackToAlbum() {
        throw new RuntimeException("Not Implemented");
    }

    @DolphinAction("deleteSelected")
    public void onDeleteSelected() {
        try {
            folder.moveToTrash(folder.findImageById(model.getSelectedImageId()), asyncService, photoniaService);
        } catch (IOException e) {
            showError("Can't move image", e);
        }
    }

    @Override
    public void albumChanged(String id) {
        if(folder.getUuid().equals(id)) {
            //TODO: Update Name etc.
        }
    }

    @Override
    public void imageChanged(String id) {
        for(AlbumViewImageBean imageBean : model.imagesProperty()) {
            if(imageBean.getId().get().equals(id)) {
                //TODO: Update Image Data
            }
        }
    }
}
