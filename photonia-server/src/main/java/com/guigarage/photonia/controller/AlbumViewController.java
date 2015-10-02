package com.guigarage.photonia.controller;

import com.canoo.dolphin.BeanManager;
import com.canoo.dolphin.server.DolphinAction;
import com.canoo.dolphin.server.DolphinController;
import com.canoo.dolphin.server.DolphinModel;
import com.canoo.dolphin.server.event.DolphinEventBus;
import com.guigarage.photonia.controller.album.AlbumViewBean;
import com.guigarage.photonia.controller.album.AlbumViewImageBean;
import com.guigarage.photonia.service.AsyncService;
import com.guigarage.photonia.service.PhotoniaService;
import com.guigarage.photonia.v2.ImageMetadata;
import com.guigarage.photonia.v2.PhotoniaAlbum;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

@DolphinController("AlbumViewController")
public class AlbumViewController extends AbstractController implements AlbumObserver, ImageObserver {

    private final DolphinEventBus eventBus;

    private final BeanManager beanManager;

    private final PhotoniaService photoniaService;

    private final AsyncService asyncService;

    @DolphinModel
    private AlbumViewBean model;

    private PhotoniaAlbum album;

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
            update();
        });
    }

    private void update() {
        this.album = photoniaService.getAlbumById(model.getId());
        model.selectedImageIdProperty().set(null);
        model.nameProperty().set(album.getMetadata().getFolderName());
        model.imagesProperty().clear();
        beanManager.removeAll(AlbumViewImageBean.class);
        for (ImageMetadata imageFile : album.getMetadata().getImages()) {
            AlbumViewImageBean imageBean = beanManager.create(AlbumViewImageBean.class);
            imageBean.getId().set(imageFile.getUuid());
            imageBean.getRating().set(imageFile.getRating());
            imageBean.getThumbnailUrl().set(photoniaService.getImageThumbnailUrl(imageFile.getUuid()));
            model.imagesProperty().add(imageBean);
        }
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
        if (model.getId().equals(id)) {
            update();
        }
    }

    @Override
    public void imageChanged(String id) {
        for (AlbumViewImageBean imageBean : model.imagesProperty()) {
            if (imageBean.getId().get().equals(id)) {
                for (ImageMetadata imageMetadata : album.getMetadata().getImages()) {
                    if (imageMetadata.getUuid().equals(id)) {
                        imageBean.getId().set(imageMetadata.getUuid());
                        imageBean.getRating().set(imageMetadata.getRating());
                        imageBean.getThumbnailUrl().set(photoniaService.getImageThumbnailUrl(imageMetadata.getUuid()));
                    }
                }
            }
        }
    }
}
