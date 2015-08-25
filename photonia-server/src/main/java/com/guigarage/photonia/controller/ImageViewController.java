package com.guigarage.photonia.controller;

import com.canoo.dolphin.BeanManager;
import com.canoo.dolphin.server.DolphinAction;
import com.canoo.dolphin.server.DolphinController;
import com.canoo.dolphin.server.Param;
import com.guigarage.photonia.service.PhotoniaService;
import com.guigarage.photonia.types.JpegImageFile;

import javax.inject.Inject;

@DolphinController("ImageViewController")
public class ImageViewController extends AbstractController {

    private final BeanManager beanManager;

    private final PhotoniaService photoniaService;

    private ImageViewBean bean;

    private JpegImageFile imageFile;

    @Inject
    public ImageViewController(BeanManager beanManager, PhotoniaService photoniaService) {
        this.beanManager = beanManager;
        this.photoniaService = photoniaService;
    }

    @DolphinAction("initView")
    public void onInitView(@Param("id") String imageId) {
        bean = beanManager.create(ImageViewBean.class);
        bean.getRating().onChanged(e -> {
            try {
                if (bean.getRating().get() != null) {
                    imageFile.setRating(bean.getRating().get().intValue());
                } else {
                    imageFile.setRating(0);
                }
            } catch (Exception exception) {
                showError("Rating kann nicht gesetzt werden!", exception);
            }
        });
        updateBeanByImageId(imageId);
    }

    private void updateBeanByImageId(String imageId) {
        imageFile = photoniaService.getAlbum().getImageFileById(imageId);
        bean.getRating().set(imageFile.getRating());
        bean.getExposureTime().set(imageFile.getExposureTime());
        bean.getfNumber().set(imageFile.getfNumber());
        bean.getFocalLength().set(imageFile.getFocalLength());
        bean.getFolderName().set(imageFile.getParentImageFolder().getName());
        bean.getHasRaw().set(imageFile.getRawImageFile() != null);
        bean.getId().set(imageFile.getUuid());
        bean.getIso().set(imageFile.getIso());
        bean.getLastModified().set(imageFile.getLastModified());
        bean.getLens().set(imageFile.getLens());
        bean.getImageUrl().set(photoniaService.getImageUrl(imageFile.getUuid()));
        bean.getNextId().set(imageFile.getParentImageFolder().getNext(imageFile).getUuid());
        bean.getPrevId().set(imageFile.getParentImageFolder().getPrev(imageFile).getUuid());
    }

    @DolphinAction("showNext")
    public void onShowNext() {
        updateBeanByImageId(bean.getNextId().get());
    }

    @DolphinAction("showPrev")
    public void onShowPrev() {
        updateBeanByImageId(bean.getPrevId().get());
    }

    @DolphinAction("backToFolder")
    public void onBackToFolder() {
        throw new RuntimeException("Not Implemented");
    }

    @DolphinAction("backToAlbum")
    public void onBackToAlbum() {
        throw new RuntimeException("Not Implemented");
    }

    @DolphinAction("destroyView")
    public void onDestroyView() {
        beanManager.remove(bean);
    }
}
