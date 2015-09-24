package com.guigarage.photonia.v2;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by hendrikebbers on 21.09.15.
 */
public class FolderMetadata implements Serializable {

    private String uuid;

    private String folderName;

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
        return folderName;
    }

    public void setFolderName(String folderName) {
        this.folderName = folderName;
    }

    public List<ImageMetadata> getImages() {
        return images;
    }

    public void setImages(List<ImageMetadata> images) {
        this.images = images;
    }
}
