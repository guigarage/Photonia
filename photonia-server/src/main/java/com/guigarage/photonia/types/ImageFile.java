package com.guigarage.photonia.types;

import com.drew.imaging.ImageProcessingException;
import com.drew.metadata.Metadata;
import com.drew.metadata.exif.ExifSubIFDDirectory;
import com.drew.metadata.exif.makernotes.NikonType2MakernoteDirectory;
import com.drew.metadata.file.FileMetadataDirectory;
import com.guigarage.photonia.AllowedExtensionsProvider;
import com.guigarage.photonia.MetadataUtils;
import org.apache.commons.io.FilenameUtils;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.UUID;

public abstract class ImageFile {

    private Path parentFolder;

    private String name;

    private AllowedExtensionsProvider allowedExtensionsProvider;

    private String imageName;

    private String lastModified;

    private String iso;

    private String lens;

    private String focalLength;

    private String exposureTime;

    private String fNumber;

    private int rating;

    private String uuid;

    public ImageFile(AllowedExtensionsProvider allowedExtensionsProvider, Path parentFolder, String name) {
        this.parentFolder = parentFolder;
        this.name = name;
        this.allowedExtensionsProvider = allowedExtensionsProvider;

        boolean useAllowedExtension = false;
        for (String extension : getAllowedExtensions()) {
            if (name.endsWith(extension)) {
                useAllowedExtension = true;
            }
        }
        if (!useAllowedExtension) {
            throw new RuntimeException("File " + name + " in path " + parentFolder + " don't have allowed extension for " + getClass().getName());
        }
        createImageMetadate();
        this.uuid = UUID.randomUUID().toString();
    }

    public Path getParentFolder() {
        return parentFolder;
    }

    public String getName() {
        return name;
    }

    public String getBaseName() {
        return FilenameUtils.getBaseName(getName());
    }

    public String getFileNameWithoutExtension() {
        return getName().split(".")[0];
    }

    public String getUuid() {
        return uuid;
    }

    public File toFile() {
        return new File(parentFolder.toFile(), name);
    }

    public Iterable<String> getAllowedExtensions() {
        return allowedExtensionsProvider.getAllowedExtensions();
    }

    public boolean isRaw() {
        return false;
    }

    public void setRating(int rating) throws ImageProcessingException, IOException {
        Metadata metadata = MetadataUtils.getMetadata(toFile());
        ExifSubIFDDirectory exifSubIFDDirectory = MetadataUtils.getDirectory(metadata, ExifSubIFDDirectory.class);
        MetadataUtils.setIntegerValue(exifSubIFDDirectory, ExifSubIFDDirectory.TAG_RATING, rating);
    }

    private void createImageMetadate() {
        try {
            Metadata metadata = MetadataUtils.getMetadata(toFile());

            FileMetadataDirectory fileMetadataDirectory = MetadataUtils.getDirectory(metadata, FileMetadataDirectory.class);
            if (fileMetadataDirectory != null) {
                imageName = MetadataUtils.getValue(fileMetadataDirectory, FileMetadataDirectory.TAG_FILE_NAME);
                lastModified = MetadataUtils.getValue(fileMetadataDirectory, FileMetadataDirectory.TAG_FILE_MODIFIED_DATE);
            }

            NikonType2MakernoteDirectory nikonType2MakernoteDirectory = MetadataUtils.getDirectory(metadata, NikonType2MakernoteDirectory.class);
            if (nikonType2MakernoteDirectory != null) {
                lens = MetadataUtils.getValue(nikonType2MakernoteDirectory, NikonType2MakernoteDirectory.TAG_LENS);
            }

            ExifSubIFDDirectory exifSubIFDDirectory = MetadataUtils.getDirectory(metadata, ExifSubIFDDirectory.class);
            if (exifSubIFDDirectory != null) {
                iso = MetadataUtils.getValue(exifSubIFDDirectory, ExifSubIFDDirectory.TAG_ISO_EQUIVALENT);
                exposureTime = MetadataUtils.getValue(exifSubIFDDirectory, ExifSubIFDDirectory.TAG_EXPOSURE_TIME);
                fNumber = MetadataUtils.getValue(exifSubIFDDirectory, ExifSubIFDDirectory.TAG_FNUMBER);
                focalLength = MetadataUtils.getValue(exifSubIFDDirectory, ExifSubIFDDirectory.TAG_FOCAL_LENGTH);
                Integer r = MetadataUtils.getIntegerValue(exifSubIFDDirectory, ExifSubIFDDirectory.TAG_RATING);
                if(r == null || r.intValue() <= 0) {
                    rating = 0;
                } else if(r.intValue() > 5) {
                    rating = 5;
                } else {
                    rating = r.intValue();
                }
            }
        } catch (Exception e) {
            throw new RuntimeException("Can't create Metadata for image", e);
        }
    }

    public String getImageName() {
        return imageName;
    }

    public String getLastModified() {
        return lastModified;
    }

    public String getIso() {
        return iso;
    }

    public String getLens() {
        return lens;
    }

    public String getFocalLength() {
        return focalLength;
    }

    public String getExposureTime() {
        return exposureTime;
    }

    public String getfNumber() {
        return fNumber;
    }

    public int getRating() {
        return rating;
    }
}
