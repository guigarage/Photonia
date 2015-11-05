package com.guigarage.photonia.data;

import java.io.IOException;

public class Trash extends AbstractAlbum {

    public Trash(String name, String uuid, String pathPrefix) throws IOException {
        super(name, uuid, pathPrefix);
    }

    @Override
    public void moveToTrash(Photo photo) {
        throw new RuntimeException("Already in trash");
    }

    public void clear() {
        for (Photo photo : getPhotosReadOnly()) {
            photo.delete();
        }
    }
}
