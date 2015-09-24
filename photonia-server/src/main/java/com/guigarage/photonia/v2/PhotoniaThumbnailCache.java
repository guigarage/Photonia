package com.guigarage.photonia.v2;

import com.guigarage.photonia.service.AsyncService;
import com.guigarage.photonia.types.RenderedImageFile;
import com.guigarage.photonia.util.Locker;
import net.coobird.thumbnailator.Thumbnails;
import org.apache.commons.io.FileUtils;

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

        //TODO:Read data from mapping file

    }

    public Future<Void> createThumbnail(PhotoniaImage imageFile, AsyncService asyncService) {
        Future<Void> task = asyncService.run(() -> {
            try {
                createThumbnail(imageFile);
            } catch (IOException e) {
                throw new RuntimeException("Error while loading thumbnail", e);
            }
        }, f -> {
            tasksMapLock.runLocked(() -> tasks.remove(imageFile.getImageMetadata().getUuid()));
            onThumbCreatedIdConsumer.accept(imageFile.getImageMetadata().getUuid());
        });
        tasksMapLock.runLocked(() -> {
            if (!task.isDone()) {
                tasks.put(imageFile.getImageMetadata().getUuid(), task);
            }
        });
        return task;
    }

    public BufferedImage getThumbnail(PhotoniaImage imageFile) throws Exception {
        File thumbFile = thumbsMapLocker.callLocked(() -> thumbs.get(imageFile.getImageMetadata().getUuid()));
        if (thumbFile != null) {
            return ImageIO.read(thumbFile);
        }
        return null;
    }

    public void removeThumbnail(PhotoniaImage imageFile) throws IOException {
        File toDelete = thumbsMapLocker.callLocked(() -> thumbs.remove(imageFile.getImageMetadata().getUuid()));
        if (toDelete != null) {
            FileUtils.forceDelete(toDelete);
        }
    }

    private void createThumbnail(PhotoniaImage imageFile) throws IOException {
        removeThumbnail(imageFile);
        BufferedImage thumbnail = Thumbnails.of(imageFile.toLocalFile()).width(thumbSize).asBufferedImage();

        File thumbFolderFile = new File(thumbFolder, imageFile.getFolder().getLocalFolder().getName());
        if (!thumbFolderFile.exists()) {
            thumbFolderFile.mkdirs();
        }
        File thumbFile = new File(thumbFolderFile, imageFile.getImageMetadata().getUuid() + ".jpg");
        ImageIO.write(thumbnail, "JPG", thumbFile);
        thumbsMapLocker.runLocked(() -> thumbs.put(imageFile.getImageMetadata().getUuid(), thumbFile));
    }
}
