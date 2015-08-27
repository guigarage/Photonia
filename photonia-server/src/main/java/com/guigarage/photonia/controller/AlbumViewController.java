package com.guigarage.photonia.controller;

import com.canoo.dolphin.BeanManager;
import com.canoo.dolphin.server.DolphinAction;
import com.canoo.dolphin.server.DolphinController;
import com.canoo.dolphin.server.Param;
import com.guigarage.photonia.controller.album.AlbumViewBean;
import com.guigarage.photonia.controller.album.AlbumViewImageBean;
import com.guigarage.photonia.folder.ImageFolder;
import com.guigarage.photonia.service.AsyncService;
import com.guigarage.photonia.service.PhotoniaService;
import com.guigarage.photonia.types.JpegImageFile;

import javax.inject.Inject;
import java.io.IOException;

@DolphinController("AlbumViewController")
public class AlbumViewController extends AbstractController {

    private final BeanManager beanManager;

    private final PhotoniaService photoniaService;

    private final AsyncService asyncService;

    private AlbumViewBean bean;

    private ImageFolder folder;

    @Inject
    public AlbumViewController(BeanManager beanManager, PhotoniaService photoniaService, AsyncService asyncService) {
        this.beanManager = beanManager;
        this.photoniaService = photoniaService;
        this.asyncService = asyncService;
    }

    @DolphinAction("initView")
    public void onInitView(@Param("id") String folderId) {
        bean = beanManager.create(AlbumViewBean.class);
        updateBeanByFolder(folderId);
    }

    private void updateBeanByFolder(String folderId) {
        folder = photoniaService.getAlbum().getFolderById(folderId);
        bean.getSelectedImageId().set(null);
        bean.getName().set(folder.getName());
        bean.getImages().clear();
        beanManager.removeAll(AlbumViewImageBean.class);
        for (JpegImageFile imageFile : folder.getImages()) {
            AlbumViewImageBean imageBean = beanManager.create(AlbumViewImageBean.class);
            imageBean.getId().set(imageFile.getUuid());
            imageBean.getHasRaw().set(imageFile.getRawImageFile() != null);
            imageBean.getRating().set(imageFile.getRating());
            imageBean.getThumbnailUrl().set(photoniaService.getImageThumbnailUrl(imageFile.getUuid()));
            bean.getImages().add(imageBean);
        }
    }

    @DolphinAction("destroyView")
    public void onDestroyView() {
        beanManager.remove(bean);
    }

    @DolphinAction("backToAlbum")
    public void onBackToAlbum() {
        throw new RuntimeException("Not Implemented");
    }

    @DolphinAction("deleteSelected")
    public void onDeleteSelected() {
        try {
            folder.moveToTrash(folder.findImageById(bean.getSelectedImageId().get()), asyncService, photoniaService);
        } catch (IOException e) {
            showError("Can't move image", e);
        }
    }
}
