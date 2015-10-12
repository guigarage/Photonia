package com.guigarage.photonia.controller;

import com.canoo.dolphin.BeanManager;
import com.canoo.dolphin.server.DolphinController;
import com.canoo.dolphin.server.DolphinModel;
import com.guigarage.photonia.controller.library.LibraryViewAlbumBean;
import com.guigarage.photonia.controller.library.LibraryViewBean;
import com.guigarage.photonia.service.PhotoniaService;
import com.guigarage.photonia.v2.PhotoniaAlbum;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

@DolphinController("LibraryViewController")
public class LibraryViewController extends AbstractController implements AlbumObserver {

    private final BeanManager beanManager;

    private final PhotoniaService photoniaService;

    @DolphinModel
    private LibraryViewBean model;

    @Inject
    public LibraryViewController(BeanManager beanManager, PhotoniaService photoniaService) {
        this.beanManager = beanManager;
        this.photoniaService = photoniaService;
    }

    @PostConstruct
    private void updateBean() {
        for (PhotoniaAlbum album : photoniaService.getAllAlbums()) {
            LibraryViewAlbumBean albumBean = beanManager.create(LibraryViewAlbumBean.class);
            albumBean.getId().set(album.getMetadata().getUuid());
            albumBean.getName().set(album.getMetadata().getFolderName());
            albumBean.getImageCount().set(album.getMetadata().getImages().size());
            if (!album.getMetadata().getImages().isEmpty()) {
                albumBean.getCoverUrl().set(photoniaService.getImageThumbnailUrl(album.getMetadata().getImages().get(0).getUuid()));
            }
            model.getAlbums().add(albumBean);
        }
        model.getAlbumCount().set("123");
    }


    @Override
    public void albumChanged(String id) {
        for (LibraryViewAlbumBean albumBean : model.getAlbums()) {
            if (albumBean.getId().equals(id)) {
                PhotoniaAlbum album = photoniaService.getAlbumById(id);
                albumBean.getId().set(album.getMetadata().getUuid());
                albumBean.getName().set(album.getMetadata().getFolderName());
                albumBean.getImageCount().set(album.getMetadata().getImages().size());
                if (!album.getMetadata().getImages().isEmpty()) {
                    albumBean.getCoverUrl().set(photoniaService.getImageThumbnailUrl(album.getMetadata().getImages().get(0).getUuid()));
                }
            }
        }
    }
}
