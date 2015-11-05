package com.guigarage.photonia.services;

import com.guigarage.photonia.data.*;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import java.io.IOException;

@Service
public class SpringActionHandler implements ActionHandler {

    @Inject
    private LibraryManager libraryManager;

    @Override
    public void moveImageToTrash(String imageId) {
        Photo photo = libraryManager.getPhotoById(imageId);
        AbstractAlbum album = photo.getAlbum();
        album.moveToTrash(photo);
    }

    @Override
    public void removeImageFromTrash(String imageId) {
        Photo photo = libraryManager.getPhotoById(imageId);
        AbstractAlbum album = photo.getAlbum();
        Trash trash = libraryManager.getTrash();
        if (album.equals(trash)) {
            photo.delete();
        } else {
            throw new RuntimeException("Not in Trash");
        }
    }

    @Override
    public void clearTrash() {
        Trash trash = libraryManager.getTrash();
        trash.clear();
    }

    @Override
    public void createAlbum(String name) {
        try {
            Album.create(name, libraryManager.getPathPrefix(), libraryManager.getTrash());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void deleteAlbum(String albumId) {
        Album album = libraryManager.getAlbumById(albumId);
        try {
            album.delete();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void renameAlbum(String albumId, String newName) {
        Album album = libraryManager.getAlbumById(albumId);
        try {
            album.rename(newName);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void moveToAlbum(String imageId, String newAlbumId) {
        Photo photo = libraryManager.getPhotoById(imageId);
        AbstractAlbum album = photo.getAlbum();
        Album newAlbum = libraryManager.getAlbumById(newAlbumId);

        if (album.equals(newAlbum)) {
            throw new RuntimeException("Same Album");
        }

        PhotoTransfer transfer = photo.createTransfer();
        newAlbum.addPhoto(transfer);
        photo.delete();
    }

    @Override
    public void likeImage(String imageId) {
        throw new RuntimeException("Not Implemented");
    }

    @Override
    public void unlikeImage(String imageId) {
        throw new RuntimeException("Not Implemented");
    }

    @Override
    public String getNextImage(String imageId) {
        return null;
    }

    @Override
    public String getPrevImage(String imageId) {
        return null;
    }

    @Override
    public void uploadToAlbum(PhotoTransfer upload, String albumId) {
        Album album = libraryManager.getAlbumById(albumId);
        album.addPhoto(upload);
    }

    @Override
    public void uploadAdditionalDataForImage(PhotoTransfer upload, String imageId) {
        try {
            Photo photo = libraryManager.getPhotoById(imageId);
            AbstractAlbum album = photo.getAlbum();
            album.addAdditionalFiles(photo, upload);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public PhotoTransfer download(String imageId) {
        Photo photo = libraryManager.getPhotoById(imageId);
        return photo.createTransfer();
    }
}
