package com.guigarage.photonia.client;

import javafx.concurrent.Task;
import javafx.scene.image.Image;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ClientImageCache {

    private Map<String, Image> cachedImages;

    private long cachedSize;

    private List<String> lastAccesses;

    private ExecutorService imageLoadExecutor;

    private static ClientImageCache instance = new ClientImageCache();

    private ClientImageCache() {
        this.cachedImages = new HashMap<>();
        this.lastAccesses = new ArrayList<>();
        imageLoadExecutor = Executors.newFixedThreadPool(4);
    }

    public Task<Image> get(String url) {
        Task<Image> imageTask = new Task<Image>() {
            @Override
            protected Image call() throws Exception {
                Image image = cachedImages.get(url);
                if (image != null) {
                    lastAccesses.remove(url);
                    lastAccesses.add(url);
                } else {
                    image = new Image(url, false);
                    System.out.println("loaded " + url);
                    cachedImages.put(url, image);
                    cachedSize = cachedSize + (int) (image.getWidth() * image.getHeight());
                    lastAccesses.add(url);
                }
                return image;
            }
        };
        imageLoadExecutor.submit(imageTask);
        return imageTask;
    }

    public static ClientImageCache getInstance() {
        return instance;
    }
}
