package com.guigarage.photonia.controller;

import com.canoo.dolphin.BeanManager;
import com.canoo.dolphin.server.DolphinAction;
import com.canoo.dolphin.server.DolphinController;
import com.canoo.dolphin.server.DolphinModel;
import com.guigarage.photonia.service.PhotoniaService;
import com.guigarage.photonia.v2.ImageMetadata;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

@DolphinController("ImageViewController")
public class ImageViewController extends AbstractController implements ImageObserver {

    private final PhotoniaService photoniaService;

    @DolphinModel
    private ImageViewBean model;

    private ImageMetadata imageFile;

    @Inject
    public ImageViewController(PhotoniaService photoniaService) {
        this.photoniaService = photoniaService;
    }

    @PostConstruct
    public void init() {
        model.ratingProperty().onChanged(e -> {
            try {
                if (model.ratingProperty().get() != null) {
                    imageFile.setRating(model.ratingProperty().get().intValue());
                } else {
                    imageFile.setRating(0);
                }
            } catch (Exception exception) {
                showError("Rating kann nicht gesetzt werden!", exception);
            }
        });

        model.idProperty().onChanged(e -> {
            update();
        });
    }

    private void update() {
        imageFile = photoniaService.getImageById(model.getId());
        model.ratingProperty().set(imageFile.getRating());
        model.exposureTimeProperty().set(imageFile.getExposureTime());
        model.fNumberProperty().set(imageFile.getfNumber());
        model.focalLengthProperty().set(imageFile.getFocalLength());
        model.isoProperty().set(imageFile.getIso());
        model.lastModifiedProperty().set(imageFile.getLastModified());
        model.lensProperty().set(imageFile.getLens());
        model.imageUrlProperty().set(photoniaService.getImageUrl(imageFile.getUuid()));
//        model.nextIdProperty().set(photoniaService.getNextImage(imageFile.getUuid()).getUuid());
//        model.prevIdProperty().set(photoniaService.getPrevImage(imageFile.getUuid()).getUuid());
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

    @Override
    public void imageChanged(String id) {
        if (model.idProperty().equals(id)) {
            update();
        }
    }
}
