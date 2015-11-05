package com.guigarage.photonia.data;

import com.drew.imaging.ImageProcessingException;
import com.guigarage.photonia.util.Locker;
import org.apache.commons.io.FilenameUtils;

import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.CopyOnWriteArrayList;

public abstract class AbstractAlbum {

    private final String uuid;

    private String name;

    private final List<Photo> photos;

    private final Locker photosLocker;

    private final String pathPrefix;

    public AbstractAlbum(final String name, final String uuid, final String pathPrefix) throws IOException {
        this.name = name;
        this.uuid = uuid;
        this.pathPrefix = pathPrefix;

        this.photos = new CopyOnWriteArrayList<>();
        this.photosLocker = new Locker();

        updatePhotos();
    }

    public void updatePhotos() {
        photosLocker.runLocked(() -> {
            photos.clear();
            Path myPath = Paths.get(pathPrefix, name);
            try (DirectoryStream<Path> ds = Files.newDirectoryStream(myPath)) {
                for (Path child : ds) {
                    String fileName = child.getFileName().toString();
                    String baseName = FilenameUtils.getBaseName(fileName);

                    boolean foundPhoto = false;
                    for (Photo photo : photos) {
                        if (photo.getBaseName().equals(baseName)) {
                            photo.addFile(fileName);
                            foundPhoto = true;
                            break;
                        }
                    }

                    if (!foundPhoto) {
                        Photo photo = new Photo(UUID.randomUUID().toString(), this, fileName);
                        photos.add(photo);
                        //TODO: Trigger ThumbnailCache
                    }
                }
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });
    }

    public List<Photo> getPhotosReadOnly() {
        return photosLocker.callLocked(() -> Collections.unmodifiableList(photos));
    }

    public void addPhoto(final PhotoTransfer photoTransfer) {
        String id = UUID.randomUUID().toString();
        photosLocker.runLocked(() -> {
            storeFiles(id, photoTransfer);
            String[] fileNames = photoTransfer.getParts().stream().map(p -> p.getName()).toArray(size -> new String[size]);
            Photo photo = new Photo(id, this, fileNames);
            photos.add(photo);
            //TODO: Trigger ThumbnailCache
        });
    }

    public String getPathPrefix() {
        return pathPrefix;
    }

    public String getUuid() {
        return uuid;
    }

    public String getName() {
        return name;
    }

    public void addAdditionalFiles(Photo photo, PhotoTransfer photoTransfer) throws IOException, ImageProcessingException {
        storeFiles(photo.getUuid(), photoTransfer);
        for (Part photoPart : photoTransfer.getParts()) {
            photo.addFile(photoPart.getName());
        }
    }

    protected List<Photo> getPhotosImpl() {
        return photos;
    }

    public Locker getPhotosLocker() {
        return photosLocker;
    }

    private void storeFiles(String id, PhotoTransfer photoTransfer) {
        for (Part photoPart : photoTransfer.getParts()) {
            try {
                Path file = Paths.get(pathPrefix, name, id + photoPart.getName());
                if (Files.exists(file)) {
                    Files.delete(file);
                }
                Files.createFile(file);
                Files.write(file, photoPart.getData());
            } catch (Exception e) {
                //TODO: Was nun?
                throw new RuntimeException(e);
            }
        }
    }

    public abstract void moveToTrash(final Photo photo);
}
