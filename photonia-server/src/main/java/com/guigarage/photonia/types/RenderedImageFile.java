package com.guigarage.photonia.types;

import com.guigarage.photonia.AllowedExtensionsProvider;
import com.guigarage.photonia.MetadataUtils;
import net.coobird.thumbnailator.Thumbnails;
import org.apache.commons.io.FileUtils;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.nio.file.Path;

public class RenderedImageFile extends ImageFile {

    private RawImageFile rawImageFile;

    public RenderedImageFile(AllowedExtensionsProvider allowedExtensionsProvider, Path parentFolder, String name) {
        this(MetadataUtils.getRawForImage(parentFolder, name), allowedExtensionsProvider, parentFolder, name);
    }

    public RenderedImageFile(RawImageFile rawImageFile, AllowedExtensionsProvider allowedExtensionsProvider, Path parentFolder, String name) {
        super(allowedExtensionsProvider, parentFolder, name);
        this.rawImageFile = rawImageFile;
    }

    public byte[] readFile() throws IOException {
        return FileUtils.readFileToByteArray(toFile());
    }

    public BufferedImage getAsBufferedImage() throws IOException {
        return ImageIO.read(toFile());
    }

    public BufferedImage getAsThumbnailImage(int width) throws IOException {
        return Thumbnails.of(toFile()).width(width).asBufferedImage();
    }

    public RawImageFile getRawImageFile() {
        return rawImageFile;
    }
}
