package com.guigarage.photonia.v2;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;

import java.io.File;
import java.util.UUID;

/**
 * Created by hendrikebbers on 21.09.15.
 */
public class PhotoniaAlbum {

    private final static String STORE_FILE_NAME = "store.json";

    private File localFolder;

    private File store;

    private FolderMetadata metadata;

    public PhotoniaAlbum(File localFolder) throws Exception {
        this.localFolder = localFolder;

        if (!localFolder.exists()) {
            localFolder.mkdirs();
        }

        if (!getStore().exists()) {
            createMetadataFromFolder();
            recreateStoreByMetadata();
        } else {
            createMetadataFromStore();
        }
    }

    private File getStore() {
        return new File(localFolder, STORE_FILE_NAME);
    }

    private synchronized void createMetadataFromFolder() throws Exception {
        metadata = new FolderMetadata();
        metadata.setFolderName(localFolder.getName());
        metadata.setUuid(UUID.randomUUID().toString());
        for (File child : localFolder.listFiles()) {
            if (FilenameUtils.getExtension(child.getName()).toLowerCase().equals("jpeg") || FilenameUtils.getExtension(child.getName()).toLowerCase().equals("jpg")) {
                ImageMetadata imageMetadata = ImageMetadata.createFromFile(child);
                metadata.getImages().add(imageMetadata);
            }
        }
    }

    private synchronized void createMetadataFromStore() throws Exception {
        File store = getStore();
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        metadata = gson.fromJson(FileUtils.readFileToString(store), FolderMetadata.class);
    }

    private synchronized void recreateStoreByMetadata() throws Exception {
        File store = getStore();
        if (store.exists()) {
            FileUtils.forceDelete(store);
        }
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        String storeContent = gson.toJson(metadata);
        FileUtils.writeStringToFile(store, storeContent);
    }

    public File getLocalFolder() {
        return localFolder;
    }

    public FolderMetadata getMetadata() {
        return metadata;
    }

    public static void main(String... args) throws Exception {
       PhotoniaAlbum folder = new PhotoniaAlbum(new File("/Users/hendrikebbers/Desktop/photonia/album/Hochzeit Claudine und Marcel 2"));
    }
}
