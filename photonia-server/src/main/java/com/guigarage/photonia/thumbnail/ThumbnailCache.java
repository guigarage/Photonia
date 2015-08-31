package com.guigarage.photonia.thumbnail;

import com.guigarage.photonia.service.AsyncService;
import com.guigarage.photonia.types.RenderedImageFile;
import com.guigarage.photonia.util.Locker;
import org.apache.commons.io.FileUtils;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Future;
import java.util.function.Consumer;

public class ThumbnailCache {

    private final Locker thumbsMapLocker;

    private final Locker tasksMapLock;

    private final Map<String, File> thumbs;

    private final Map<String, Future<Void>> tasks;

    private final File thumbFolder;

    private final int thumbSize;

    private final Consumer<String> onThumbCreatedIdConsumer;

    public ThumbnailCache(File thumbFolder, int thumbSize, Consumer<String> onThumbCreatedIdConsumer) {
        this.thumbFolder = thumbFolder;
        this.thumbSize = thumbSize;
        this.onThumbCreatedIdConsumer = onThumbCreatedIdConsumer;
        thumbs = new HashMap<>();
        tasks = new HashMap<>();
        thumbsMapLocker = new Locker();
        tasksMapLock = new Locker();
    }

    public void createAsync(RenderedImageFile imageFile, AsyncService asyncService) {
        Future<Void> task = asyncService.run(() -> {
            try {
                createThumbnail(imageFile);
            } catch (IOException e) {
                throw new RuntimeException("Error while loading thumbnail", e);
            }
        }, f -> {
            tasksMapLock.runLocked(() -> tasks.remove(imageFile.getUuid()));
            onThumbCreatedIdConsumer.accept(imageFile.getUuid());
        });
        tasksMapLock.runLocked(() -> {
            if (!task.isDone()) {
                tasks.put(imageFile.getUuid(), task);
            }
        });
    }

    public BufferedImage getThumbnail(RenderedImageFile imageFile) throws Exception {
        File thumbFile = thumbsMapLocker.callLocked(() -> thumbs.get(imageFile.getUuid()));
        if (thumbFile != null) {
            return ImageIO.read(thumbFile);
        }
        return null;
    }

    public void removeThumbnail(RenderedImageFile imageFile) throws IOException {
        File toDelete = thumbsMapLocker.callLocked(() -> thumbs.remove(imageFile.getUuid()));
        if (toDelete != null) {
            FileUtils.forceDelete(toDelete);
        }
    }

    private void createThumbnail(RenderedImageFile imageFile) throws IOException {
        removeThumbnail(imageFile);
        BufferedImage thumbnail = imageFile.getAsThumbnailImage(thumbSize);

        File thumbFolderFile = new File(thumbFolder, imageFile.getParentFolder().toFile().getName());
        if (!thumbFolderFile.exists()) {
            thumbFolderFile.mkdirs();
        }
        File thumbFile = new File(thumbFolderFile, imageFile.getUuid() + ".jpg");
        ImageIO.write(thumbnail, "JPG", thumbFile);
        thumbsMapLocker.runLocked(() -> thumbs.put(imageFile.getUuid(), thumbFile));
    }
}
