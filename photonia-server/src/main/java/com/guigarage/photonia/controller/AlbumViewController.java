package com.guigarage.photonia.controller;

import com.canoo.dolphin.BeanManager;
import com.canoo.dolphin.server.DolphinAction;
import com.canoo.dolphin.server.DolphinController;
import com.guigarage.photonia.folder.ImageFolder;
import com.guigarage.photonia.service.PhotoniaService;

import javax.inject.Inject;

@DolphinController("AlbumViewController")
public class AlbumViewController extends AbstractController {

    private final BeanManager beanManager;

    private final PhotoniaService photoniaService;

    private AlbumViewBean bean;

    @Inject
    public AlbumViewController(BeanManager beanManager, PhotoniaService photoniaService) {
        this.beanManager = beanManager;
        this.photoniaService = photoniaService;
    }

    @DolphinAction("initView")
    public void onInitView() {
        bean = beanManager.create(AlbumViewBean.class);
        updateBean();
    }

    private void updateBean() {
        bean.getFolders().clear();
        beanManager.removeAll(AlbumViewBean.AlbumFolderBean.class);
        for (ImageFolder folder : photoniaService.getAlbum().getFolders()) {
            AlbumViewBean.AlbumFolderBean folderBean = beanManager.create(AlbumViewBean.AlbumFolderBean.class);
            folderBean.getName().set(folder.getName());
            folderBean.getImageCount().set(folder.getImages().size());
        }
    }

    @DolphinAction("destroyView")
    public void onDestroyView() {
        beanManager.remove(bean);
    }
}
