package com.guigarage.photonia.spring.services;

import com.guigarage.photonia.service.AsyncService;
import com.guigarage.photonia.service.PhotoniaService;
import com.guigarage.photonia.v2.ImageMetadata;
import com.guigarage.photonia.v2.PhotoniaAlbum;
import com.guigarage.photonia.v2.PhotoniaThumbnailCache;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.servlet.ServletContext;
import java.io.File;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

@Service
public class SpringPhotoniaService implements PhotoniaService {

    private PhotoniaThumbnailCache thumbnailCache;

    private List<PhotoniaAlbum> albums;

    @Inject
    private AsyncService asyncService;

    @Inject
    private ServletContext servletContext;

    @Value("${server.port:8080}")
    private int port;

    // @Inject
    // private DolphinEventBus eventBus;

    @PostConstruct
    public void init() {
        File thumbBaseFolder = new File("/Users/hendrikebbers/Desktop/photonia/thumbnails");
        thumbnailCache = new PhotoniaThumbnailCache(thumbBaseFolder, 400, id -> System.out.println(""));

        File dataFolder = new File("/Users/hendrikebbers/Desktop/photonia/albums");
        albums = new ArrayList<>();
        for (File albumFolder : dataFolder.listFiles()) {
            if (albumFolder.isDirectory()) {
                try {
                    PhotoniaAlbum album = new PhotoniaAlbum(albumFolder);
                    for(ImageMetadata imageMetadata : album.getMetadata().getImages()) {
                        if(!thumbnailCache.containsThumbnail(imageMetadata)) {
                            thumbnailCache.createThumbnail(imageMetadata, asyncService);
                        }
                    }
                    albums.add(album);
                } catch (Exception e) {
                    throw new RuntimeException("Can't create album for " + albumFolder, e);
                }
            }
        }
    }


    @Override
    public List<PhotoniaAlbum> getAllAlbums() {
        return albums;
    }

    @Override
    public PhotoniaAlbum getAlbumById(String albumId) {
        for (PhotoniaAlbum album : albums) {
            if (album.getMetadata().getUuid().equals(albumId)) {
                return album;
            }
        }
        return null;
    }

    @Override
    public PhotoniaThumbnailCache getThumbnailCache() {
        return thumbnailCache;
    }

    @Override
    public String getImageUrl(String id) {
        try {
            return "http://" + InetAddress.getLocalHost().getHostAddress() + ":" + port + "/" + servletContext.getContextPath() + "/images/get/" + id;
        } catch (UnknownHostException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public String getImageThumbnailUrl(String id) {
        try {
            return "http://" + InetAddress.getLocalHost().getHostAddress() + ":" + port + "/" + servletContext.getContextPath() + "/thumbs/get/" + id;
        } catch (UnknownHostException e) {
           throw new RuntimeException(e);
        }
    }

    @Override
    public ImageMetadata getImageById(String imageId) {
        for (PhotoniaAlbum album : albums) {
            for (ImageMetadata imageMetadata : album.getMetadata().getImages()) {
                if (imageMetadata.getUuid().equals(imageId)) {
                    return imageMetadata;
                }
            }
        }
        return null;
    }

    @Override
    public ImageMetadata getPrevImage(String imageId) {
        for (PhotoniaAlbum album : albums) {
            for(int i = 0; i < album.getMetadata().getImages().size(); i++) {
                if (album.getMetadata().getImages().get(i).getUuid().equals(imageId)) {
                    if(i > 0) {
                        return album.getMetadata().getImages().get(i-1);
                    } else {
                        return album.getMetadata().getImages().get(album.getMetadata().getImages().size() - 1);
                    }
                }
            }
        }
        return null;
    }

    @Override
    public void delete(String id) {
        ImageMetadata metadata = getImageById(id);
        if(metadata != null) {
            metadata.delete();
        }
    }

    @Override
    public ImageMetadata getNextImage(String imageId) {
        for (PhotoniaAlbum album : albums) {
            for(int i = 0; i < album.getMetadata().getImages().size(); i++) {
                if (album.getMetadata().getImages().get(i).getUuid().equals(imageId)) {
                    if(i < album.getMetadata().getImages().size() - 1) {
                        return album.getMetadata().getImages().get(i+1);
                    } else {
                        return album.getMetadata().getImages().get(0);
                    }
                }
            }
        }
        return null;
    }
}
