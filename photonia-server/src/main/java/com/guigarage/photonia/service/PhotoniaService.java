package com.guigarage.photonia.service;

import com.guigarage.photonia.Album;
import com.guigarage.photonia.folder.TrashFolder;
import com.guigarage.photonia.thumbnail.ThumbnailCache;

public interface PhotoniaService {

    Album getAlbum();

    ThumbnailCache getThumbnailCache();

    TrashFolder getTrashFolder();

    String getImageUrl(String id);

    String getImageThumbnailUrl(String id);

}
