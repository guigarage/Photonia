package com.guigarage.photonia.data;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

public class Album extends AbstractAlbum {

    private final Trash trash;

    public Album(final String name, final String uuid, final String pathPrefix, final Trash trash) throws IOException {
        super(name, uuid, pathPrefix);
        this.trash = trash;
    }

    public static Album create(final String name, final String pathPrefix, final Trash trash) throws IOException {
        final String id = UUID.randomUUID().toString();
        Path albumPath = Paths.get(pathPrefix, name);
        Files.createDirectory(albumPath);
        return new Album(name, id, pathPrefix, trash);
    }

    public void rename(String newName) throws IOException {
        Path myPath = Paths.get(getPathPrefix(), getName());
        Path newPath = Paths.get(getPathPrefix(), newName);
        if(Files.exists(newPath)) {
            throw new RuntimeException("Album already created");
        }
        Files.move(myPath, newPath);
    }

    public void delete() throws IOException {
        getPhotosLocker().runLocked(() -> {
            for (Photo photo : getPhotosImpl()) {
                moveToTrash(photo);
            }
        });
        Path myPath = Paths.get(getPathPrefix(), getName());
        Files.delete(myPath);
    }

    public void moveToTrash(final Photo photo) {
        getPhotosLocker().runLocked(() -> {
            boolean wasInAlbum = getPhotosImpl().remove(photo);
            if (!wasInAlbum) {
                throw new RuntimeException("Photo " + photo + " is not in album " + this);
            }
            trash.addPhoto(photo.createTransfer());
            photo.delete();
        });
    }
}
