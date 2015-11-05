package com.guigarage.photonia.services;

import com.guigarage.photonia.data.Album;
import com.guigarage.photonia.data.Photo;
import com.guigarage.photonia.data.Trash;

import java.util.List;

public interface LibraryManager {

    Photo getPhotoById(String id);

    Album getAlbumById(String id);

    List<Album> getAllAlbums();

    Trash getTrash();

    String getPathPrefix();
}
