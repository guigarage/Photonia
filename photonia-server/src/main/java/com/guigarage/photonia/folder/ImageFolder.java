package com.guigarage.photonia.folder;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

import com.guigarage.photonia.MetadataUtils;
import com.guigarage.photonia.service.AsyncService;
import com.guigarage.photonia.service.PhotoniaService;
import com.guigarage.photonia.thumbnail.ThumbnailCache;
import com.guigarage.photonia.types.JpegImageFile;
import com.guigarage.photonia.types.RenderedImageFile;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;

public class ImageFolder {

    private Path folderPath;

    private String name;

    private final List<JpegImageFile> images;

    private final String uuid;

    private final ThumbnailCache thumbnailCache;

    public ImageFolder(String folderPath, AsyncService asyncService, ThumbnailCache thumbnailCache) throws IOException {
        this(Paths.get(folderPath), asyncService, thumbnailCache);
    }

    public ImageFolder(Path folderPath, AsyncService asyncService, ThumbnailCache thumbnailCache) throws IOException {
        this.folderPath = folderPath;
        this.images = new ArrayList<>();
        this.uuid = UUID.randomUUID().toString();
        this.thumbnailCache = thumbnailCache;

        this.name = folderPath.toFile().getName();
        if (!getFolderPath().toFile().exists()) {
            getFolderPath().toFile().mkdirs();
        }

        synchWithFileSystem(asyncService);
    }

    public List<JpegImageFile> getImages() {
        return Collections.unmodifiableList(images);
    }

    public String getName() {
        return name;
    }

    public Path getFolderPath() {
        return folderPath;
    }

    public void synchWithFileSystem(AsyncService asyncService) throws IOException {
        for (RenderedImageFile imageFile : images) {
            thumbnailCache.remove(imageFile);
        }

        images.clear();

        for (File child : folderPath.toFile().listFiles()) {
            addInternal(child.getName(), asyncService);
        }
    }

    public void moveToTrash(JpegImageFile imageFile, AsyncService asyncService, PhotoniaService photoniaService) throws IOException {
        moveChildTo(imageFile, photoniaService.getTrashFolder(), asyncService);
    }

    public void moveChildTo(JpegImageFile imageFile, ImageFolder newFolder, AsyncService asyncService) throws IOException {
        if (!imageFile.getParentImageFolder().equals(this)) {
            throw new RuntimeException("Wrong Folder!");
        }
        newFolder.add(imageFile.toFile(), asyncService);
        delete(imageFile);
    }

    protected void delete(JpegImageFile imageFile) throws IOException {
        images.remove(imageFile);
        thumbnailCache.remove(imageFile);

        for (File child : folderPath.toFile().listFiles()) {
            if (FilenameUtils.getBaseName(child.getName()).equals(FilenameUtils.getBaseName(imageFile.getName()))) {
                FileUtils.forceDelete(child);
            }
        }
    }

    public void add(File externalFile, AsyncService asyncService) throws IOException {
        List<File> toCopyList = new ArrayList<>();
        for (File child : externalFile.getParentFile().listFiles()) {
            if (FilenameUtils.getBaseName(child.getName()).equals(FilenameUtils.getBaseName(externalFile.getName()))) {
                toCopyList.add(child);
            }
        }

        String baseName = FilenameUtils.getBaseName(externalFile.getName());
        int counter = 0;
        boolean canCopy = false;
        while (!canCopy) {
            canCopy = true;
            for (File child : getFolderPath().toFile().listFiles()) {
                if (FilenameUtils.getBaseName(child.getName()).equals(baseName)) {
                    canCopy = false;
                }
            }
            counter++;
            baseName = FilenameUtils.getBaseName(externalFile.getName()) + "-" + counter;
        }


        for (File toCopy : toCopyList) {
            File newFile = new File(getFolderPath().toFile(), baseName + FilenameUtils.getExtension(toCopy.getName()));
            FileUtils.copyFile(externalFile, newFile);
            addInternal(newFile.getName(), asyncService);
        }
    }

    private void addInternal(String childName, AsyncService asyncService) {
        File child = new File(getFolderPath().toFile(), childName);
        if (!child.exists()) {
            throw new RuntimeException("Image is not in the folder!");
        }
        if (MetadataUtils.JPEG_FILE_TYPE.isMatching(child.getName())) {
            JpegImageFile imageFile = new JpegImageFile(this, child.getName());
            images.add(imageFile);
            thumbnailCache.addOrUpdateAsync(imageFile, asyncService);
        }
    }

    public String getUuid() {
        return uuid;
    }

    public JpegImageFile findImageById(String id) {
        for (JpegImageFile imageFile : images) {
            if (imageFile.getUuid().equals(id)) {
                return imageFile;
            }
        }
        return null;
    }

    public JpegImageFile getNext(JpegImageFile file) {
        int i = images.indexOf(file);
        if (i < images.size()) {
            return images.get(i + 1);
        }
        return null;
    }

    public JpegImageFile getPrev(JpegImageFile file) {
        int i = images.indexOf(file);
        if (i > 0) {
            return images.get(i - 1);
        }
        return null;
    }
}
