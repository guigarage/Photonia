package com.guigarage.photonia.services;

import com.guigarage.photonia.data.PhotoTransfer;

public interface ActionHandler {

    void moveImageToTrash(String imageId);

    void removeImageFromTrash(String imageId);

    void clearTrash();



    void createAlbum(String name);

    void deleteAlbum(String albumId);

    void renameAlbum(String albumId, String newName);



    void moveToAlbum(String imageId, String newAlbumId);

    void likeImage(String imageId);

    void unlikeImage(String imageId);

    String getNextImage(String imageId);

    String getPrevImage(String imageId);

    void uploadToAlbum(PhotoTransfer upload, String albumId);

    void uploadAdditionalDataForImage(PhotoTransfer upload, String imageId);

    PhotoTransfer download(String imageId);
}
