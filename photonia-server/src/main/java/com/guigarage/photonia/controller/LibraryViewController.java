package com.guigarage.photonia.controller;

import com.canoo.dolphin.BeanManager;
import com.canoo.dolphin.server.DolphinAction;
import com.canoo.dolphin.server.DolphinController;
import com.guigarage.photonia.controller.library.LibraryViewAlbumBean;
import com.guigarage.photonia.controller.library.LibraryViewBean;
import com.guigarage.photonia.folder.ImageFolder;
import com.guigarage.photonia.service.PhotoniaService;

import javax.inject.Inject;

@DolphinController("LibraryViewController")
public class LibraryViewController extends AbstractController {

    private final BeanManager beanManager;

    private final PhotoniaService photoniaService;

    private LibraryViewBean bean;

    @Inject
    public LibraryViewController(BeanManager beanManager, PhotoniaService photoniaService) {
        this.beanManager = beanManager;
        this.photoniaService = photoniaService;
    }

    @DolphinAction("initView")
    public void onInitView() {
        bean = beanManager.create(LibraryViewBean.class);
        updateBean();
    }

    private void updateBean() {
        bean.getAlbums().clear();
        beanManager.removeAll(LibraryViewAlbumBean.class);

        for (ImageFolder folder : photoniaService.getAlbum().getFolders()) {
            LibraryViewAlbumBean folderBean = beanManager.create(LibraryViewAlbumBean.class);
            folderBean.getId().set(folder.getUuid());
            folderBean.getName().set(folder.getName());
            folderBean.getImageCount().set(folder.getImages().size());
            if(!folder.getImages().isEmpty()) {
                folderBean.getCoverUrl().set(photoniaService.getImageThumbnailUrl(folder.getImages().get(0).getUuid()));
            }
            bean.getAlbums().add(folderBean);
        }
    }

    @DolphinAction("destroyView")
    public void onDestroyView() {
        beanManager.remove(bean);
    }
}
