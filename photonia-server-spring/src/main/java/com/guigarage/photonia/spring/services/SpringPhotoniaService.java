package com.guigarage.photonia.spring.services;

import com.canoo.dolphin.server.event.DolphinEventBus;
import com.guigarage.photonia.Album;
import com.guigarage.photonia.folder.TrashFolder;
import com.guigarage.photonia.service.AsyncService;
import com.guigarage.photonia.service.PhotoniaService;
import com.guigarage.photonia.thumbnail.ThumbnailCache;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import java.io.File;
import java.io.IOException;

@Service
public class SpringPhotoniaService implements PhotoniaService {

    private ThumbnailCache thumbnailCache;

    private Album myAlbum;

    private TrashFolder trashFolder;

    @Inject
    private AsyncService asyncService;

    @Inject
    private DolphinEventBus eventBus;

    @PostConstruct
    public void init() {
        try {
            thumbnailCache = new ThumbnailCache(new File("/Users/hendrikebbers/Desktop/photonia/thumbnails"), 400, id -> eventBus.publish("image-refresh", id));
            myAlbum = new Album("/Users/hendrikebbers/Desktop/photonia/album", thumbnailCache, asyncService);
            trashFolder = new TrashFolder("/Users/hendrikebbers/Desktop/photonia/trash", asyncService, thumbnailCache);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    @Override
    public Album getAlbum() {
        return myAlbum;
    }

    @Override
    public ThumbnailCache getThumbnailCache() {
        return thumbnailCache;
    }

    @Override
    public TrashFolder getTrashFolder() {
        return trashFolder;
    }

    @Override
    public String getImageUrl(String id) {
        return "http://localhost:8080/images/get/" + id;
    }

    @Override
    public String getImageThumbnailUrl(String id) {
        return "http://localhost:8080/thumbs/get/" + id;
    }
}
