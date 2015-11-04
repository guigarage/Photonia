package com.guigarage.photonia.service;

import com.guigarage.photonia.v2.ImageMetadata;
import com.guigarage.photonia.v2.PhotoniaAlbum;
import com.guigarage.photonia.v2.PhotoniaThumbnailCache;

import java.util.List;

public interface PhotoniaService {

    List<PhotoniaAlbum> getAllAlbums();

    PhotoniaAlbum getAlbumById(String albumId);

    PhotoniaThumbnailCache getThumbnailCache();

    String getImageUrl(String imageId);

    String getImageThumbnailUrl(String imageId);

    ImageMetadata getImageById(String imageId);

    ImageMetadata getNextImage(String imageId);

    ImageMetadata getPrevImage(String imageId);

    void delete(String id);
}
