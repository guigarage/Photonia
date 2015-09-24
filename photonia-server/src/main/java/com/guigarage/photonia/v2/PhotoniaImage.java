package com.guigarage.photonia.v2;

import java.io.File;

/**
 * Created by hendrikebbers on 21.09.15.
 */
public class PhotoniaImage {

    private ImageMetadata imageMetadata;

    private PhotoniaAlbum folder;

    public PhotoniaImage(ImageMetadata imageMetadata, PhotoniaAlbum folder) {
        this.imageMetadata = imageMetadata;
        this.folder = folder;
    }

    public ImageMetadata getImageMetadata() {
        return imageMetadata;
    }

    public PhotoniaAlbum getFolder() {
        return folder;
    }

    public File toLocalFile() {
        throw new RuntimeException("Not yet implemented");
    }
}
