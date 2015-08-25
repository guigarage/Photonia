package com.guigarage.photonia.thumbnail;

import com.guigarage.photonia.service.AsyncService;
import com.guigarage.photonia.types.RenderedImageFile;
import com.guigarage.photonia.util.Locker;
import com.guigarage.photonia.util.ObservableFutureTask;
import org.apache.commons.io.FileUtils;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Future;

public class ThumbnailCache {

    private final Locker thumbsMapLocker;

    private final Locker tasksMapLock;

    private final Map<String, File> thumbs;

    private final Map<String, Future<BufferedImage>> tasks;

    private final File thumbFolder;

    private final int thumbSize;

    public ThumbnailCache(File thumbFolder, int thumbSize) {
        this.thumbFolder = thumbFolder;
        this.thumbSize = thumbSize;
        thumbs = new HashMap<>();
        tasks = new HashMap<>();
        thumbsMapLocker = new Locker();
        tasksMapLock = new Locker();
    }

    public BufferedImage getThumb(RenderedImageFile imageFile) throws Exception {
        Future<BufferedImage> runningTask = tasksMapLock.callLocked(() -> tasks.get(imageFile.getUuid()));
        if (runningTask != null) {
            return runningTask.get();
        } else {
            File thumbFile = thumbsMapLocker.callLocked(() -> thumbs.get(imageFile.getUuid()));
            if (thumbFile != null) {
                return ImageIO.read(thumbFile);
            } else {
                ObservableFutureTask<BufferedImage> syncTask = new ObservableFutureTask<>(() -> addOrUpdate(imageFile));
                syncTask.onDone(f -> tasksMapLock.runLocked(() -> tasks.remove(imageFile.getUuid())));
                tasksMapLock.runLocked(() -> tasks.put(imageFile.getUuid(), syncTask));
                return syncTask.get();
            }
        }
    }

    public void addOrUpdateAsync(RenderedImageFile imageFile, AsyncService asyncService) {
        Future<BufferedImage> task = asyncService.call(() -> {
            try {
                return addOrUpdate(imageFile);
            } catch (IOException e) {
                throw new RuntimeException("Error while loading thumbnail", e);
            }
        }, f -> tasksMapLock.runLocked(() -> tasks.remove(imageFile.getUuid())));
        tasksMapLock.runLocked(() -> {
            if (!task.isDone()) {
                tasks.put(imageFile.getUuid(), task);
            }
        });
    }

    public void remove(RenderedImageFile imageFile) throws IOException {
        File toDelete = thumbsMapLocker.callLocked(() -> thumbs.remove(imageFile.getUuid()));
        if (toDelete != null) {
            FileUtils.forceDelete(toDelete);
        }
    }

    private BufferedImage addOrUpdate(RenderedImageFile imageFile) throws IOException {
        remove(imageFile);
        BufferedImage thumbnail = imageFile.getAsThumbnailImage(thumbSize);

        File thumbFolderFile = new File(thumbFolder, imageFile.getParentFolder().toFile().getName());
        if (!thumbFolderFile.exists()) {
            thumbFolderFile.mkdirs();
        }
        File thumbFile = new File(thumbFolderFile, imageFile.getUuid() + ".jpg");
        ImageIO.write(thumbnail, "JPG", thumbFile);
        thumbsMapLocker.runLocked(() -> thumbs.put(imageFile.getUuid(), thumbFile));
        return thumbnail;
    }
}
