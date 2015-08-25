package com.guigarage.photonia.folder;

import com.guigarage.photonia.service.AsyncService;
import com.guigarage.photonia.thumbnail.ThumbnailCache;
import com.guigarage.photonia.types.JpegImageFile;

import java.io.IOException;
import java.nio.file.Path;

public class TrashFolder extends ImageFolder {

    public TrashFolder(String folderPath, AsyncService asyncService, ThumbnailCache thumbnailCache) throws IOException {
        super(folderPath, asyncService, thumbnailCache);
    }

    public TrashFolder(Path folderPath, AsyncService asyncService, ThumbnailCache thumbnailCache) throws IOException {
        super(folderPath, asyncService, thumbnailCache);
    }

    @Override
    public void delete(JpegImageFile imageFile) throws IOException {
        super.delete(imageFile);
    }
}
