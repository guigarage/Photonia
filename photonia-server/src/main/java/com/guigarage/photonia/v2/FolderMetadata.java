package com.guigarage.photonia.v2;

import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by hendrikebbers on 21.09.15.
 */
public class FolderMetadata implements Serializable {

    private String uuid;

    private transient File localFolder;

    private List<ImageMetadata> images;

    public FolderMetadata() {
        this.images = new ArrayList<>();
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getFolderName() {
        return localFolder.getName();
    }

    public List<ImageMetadata> getImages() {
        return images;
    }

    public void setImages(List<ImageMetadata> images) {
        this.images = images;
    }

    public File getLocalFolder() {
        return localFolder;
    }

    public void setLocalFolder(File localFolder) {
        this.localFolder = localFolder;
    }
}
