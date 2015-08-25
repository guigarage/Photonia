package com.guigarage.photonia;

import com.guigarage.photonia.folder.ImageFolder;
import com.guigarage.photonia.service.AsyncService;
import com.guigarage.photonia.service.PhotoniaService;
import com.guigarage.photonia.thumbnail.ThumbnailCache;
import com.guigarage.photonia.types.JpegImageFile;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class Album {

    private final List<ImageFolder> folders;

    private final Path albumPath;

    private final ThumbnailCache thumbnailCache;

    public Album(String path, ThumbnailCache thumbnailCache, AsyncService asyncService) throws IOException {
        folders = new CopyOnWriteArrayList<>();
        this.thumbnailCache = thumbnailCache;

        albumPath = Paths.get(path);
        if (!albumPath.toFile().exists()) {
            albumPath.toFile().mkdirs();
        }
        if (!albumPath.toFile().isDirectory()) {
            throw new RuntimeException("Album path is not a folder");
        }
        for (File folder : albumPath.toFile().listFiles()) {
            if (folder.isDirectory()) {
                folders.add(new ImageFolder(albumPath.resolve(folder.getName()), asyncService, thumbnailCache));
            }
        }
    }

    public void addFolder(String name, AsyncService asyncService) throws IOException {
        if (new File(albumPath.toFile(), name).exists()) {
            throw new IllegalArgumentException("Folder " + name + "already exists and can't be created!");
        }
        folders.add(new ImageFolder(albumPath.resolve(name), asyncService, thumbnailCache));
    }

    public void deleteFolder(ImageFolder folder, AsyncService asyncService, PhotoniaService photoniaService) throws IOException {
        for (JpegImageFile imageFile : folder.getImages()) {
            folder.moveToTrash(imageFile, asyncService, photoniaService);
            FileUtils.forceDelete(folder.getFolderPath().toFile());
        }
    }

    public List<ImageFolder> getFolders() {
        return Collections.unmodifiableList(folders);
    }

    public List<JpegImageFile> getAllImages() {
        List<JpegImageFile> result = new CopyOnWriteArrayList<>();
        for (ImageFolder folder : folders) {
            result.addAll(folder.getImages());
        }
        return result;
    }

    public ImageFolder getFolderById(String uuid) {
        for(ImageFolder folder : folders) {
            if(folder.getUuid().equals(uuid)) {
                return folder;
            }
        }
        return null;
    }

    public JpegImageFile getImageFileById(String id) {
        for(JpegImageFile imageFile : getAllImages()) {
            if(imageFile.getUuid().equals(id)) {
                return imageFile;
            }
        }
        return null;
    }

    public Path getAlbumPath() {
        return albumPath;
    }
}
