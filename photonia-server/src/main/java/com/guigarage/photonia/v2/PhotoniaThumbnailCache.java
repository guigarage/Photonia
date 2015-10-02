package com.guigarage.photonia.v2;

import com.guigarage.photonia.service.AsyncService;
import com.guigarage.photonia.util.Locker;
import net.coobird.thumbnailator.Thumbnails;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Future;
import java.util.function.Consumer;

/**
 * Created by hendrikebbers on 21.09.15.
 */
public class PhotoniaThumbnailCache {

    private final static String MAPPING_FILE_NAME = "mapping.json";

    private final Locker thumbsMapLocker;

    private final Locker tasksMapLock;

    private final Map<String, File> thumbs;

    private final Map<String, Future<Void>> tasks;

    private final File thumbFolder;

    private final int thumbSize;

    private final Consumer<String> onThumbCreatedIdConsumer;

    public PhotoniaThumbnailCache(File thumbFolder, int thumbSize, Consumer<String> onThumbCreatedIdConsumer) {
        this.thumbFolder = thumbFolder;
        this.thumbSize = thumbSize;
        this.onThumbCreatedIdConsumer = onThumbCreatedIdConsumer;
        thumbs = new HashMap<>();
        tasks = new HashMap<>();
        thumbsMapLocker = new Locker();
        tasksMapLock = new Locker();


        for (File albumThumbFolder : thumbFolder.listFiles()) {
            if (albumThumbFolder.isDirectory()) {
                for (File thumb : albumThumbFolder.listFiles()) {
                    if (FilenameUtils.getExtension(thumb.getName()).trim().equalsIgnoreCase("jpg")) {
                        thumbs.put(FilenameUtils.getBaseName(thumb.getName()), thumb);
                    }
                }
            }
        }
    }

    public Future<Void> createThumbnail(ImageMetadata imageMetadata, AsyncService asyncService) {
        Future<Void> task = asyncService.run(() -> {
            try {
                createThumbnailImpl(imageMetadata);
            } catch (IOException e) {
                throw new RuntimeException("Error while loading thumbnail", e);
            }
        }, f -> {
            tasksMapLock.runLocked(() -> tasks.remove(imageMetadata.getUuid()));
            onThumbCreatedIdConsumer.accept(imageMetadata.getUuid());
        });
        tasksMapLock.runLocked(() -> {
            if (!task.isDone()) {
                tasks.put(imageMetadata.getUuid(), task);
            }
        });
        return task;
    }

    public BufferedImage getThumbnail(String imageId) throws Exception {
        File thumbFile = thumbsMapLocker.callLocked(() -> thumbs.get(imageId));
        if (thumbFile != null) {
            return ImageIO.read(thumbFile);
        }
        return null;
    }

    public void removeThumbnail(ImageMetadata imageMetadata) throws IOException {
        File toDelete = thumbsMapLocker.callLocked(() -> thumbs.remove(imageMetadata.getUuid()));
        if (toDelete != null) {
            FileUtils.forceDelete(toDelete);
        }
    }

    private void createThumbnailImpl(ImageMetadata imageMetadata) throws IOException {
        removeThumbnail(imageMetadata);
        BufferedImage thumbnail = Thumbnails.of(imageMetadata.toLocalFile()).width(thumbSize).asBufferedImage();

        File thumbFolderFile = new File(thumbFolder, imageMetadata.getFolderMetadata().getLocalFolder().getName());
        if (!thumbFolderFile.exists()) {
            thumbFolderFile.mkdirs();
        }
        File thumbFile = new File(thumbFolderFile, imageMetadata.getUuid() + ".jpg");
        ImageIO.write(thumbnail, "JPG", thumbFile);
        thumbsMapLocker.runLocked(() -> thumbs.put(imageMetadata.getUuid(), thumbFile));
    }

    public boolean containsThumbnail(ImageMetadata imageMetadata) {
        return thumbsMapLocker.callLocked(() -> thumbs.containsKey(imageMetadata.getUuid()));
    }
}
