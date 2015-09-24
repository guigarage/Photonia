package com.guigarage.photonia.v2;

import com.drew.imaging.ImageProcessingException;
import com.drew.metadata.Metadata;
import com.drew.metadata.exif.ExifSubIFDDirectory;
import com.drew.metadata.exif.makernotes.NikonType2MakernoteDirectory;
import com.drew.metadata.file.FileMetadataDirectory;
import com.guigarage.photonia.MetadataUtils;
import org.apache.commons.io.FilenameUtils;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

/**
 * Created by hendrikebbers on 21.09.15.
 */
public class ImageMetadata {

    private String uuid;

    private byte checksum;

    private String imageName;

    private String fileName;

    private String rawImageName;

    private String lastModified;

    private String iso;

    private String lens;

    private String focalLength;

    private String exposureTime;

    private String fNumber;

    private int rating;

    public ImageMetadata() {
    }

    public static ImageMetadata createFromFile(File file) throws ImageProcessingException, IOException {
        ImageMetadata imageMetadata = new ImageMetadata();
        imageMetadata.setUuid(UUID.randomUUID().toString());
        imageMetadata.setFileName(file.getName());

        Metadata metadata = MetadataUtils.getMetadata(file);

        FileMetadataDirectory fileMetadataDirectory = MetadataUtils.getDirectory(metadata, FileMetadataDirectory.class);
        if (fileMetadataDirectory != null) {
            imageMetadata.setImageName(MetadataUtils.getValue(fileMetadataDirectory, FileMetadataDirectory.TAG_FILE_NAME));
            imageMetadata.setLastModified(MetadataUtils.getValue(fileMetadataDirectory, FileMetadataDirectory.TAG_FILE_MODIFIED_DATE));
        }

        NikonType2MakernoteDirectory nikonType2MakernoteDirectory = MetadataUtils.getDirectory(metadata, NikonType2MakernoteDirectory.class);
        if (nikonType2MakernoteDirectory != null) {
            imageMetadata.setLens(MetadataUtils.getValue(nikonType2MakernoteDirectory, NikonType2MakernoteDirectory.TAG_LENS));
        }

        ExifSubIFDDirectory exifSubIFDDirectory = MetadataUtils.getDirectory(metadata, ExifSubIFDDirectory.class);
        if (exifSubIFDDirectory != null) {
            imageMetadata.setIso(MetadataUtils.getValue(exifSubIFDDirectory, ExifSubIFDDirectory.TAG_ISO_EQUIVALENT));
            imageMetadata.setExposureTime(MetadataUtils.getValue(exifSubIFDDirectory, ExifSubIFDDirectory.TAG_EXPOSURE_TIME));
            imageMetadata.setfNumber(MetadataUtils.getValue(exifSubIFDDirectory, ExifSubIFDDirectory.TAG_FNUMBER));
            imageMetadata.setFocalLength(MetadataUtils.getValue(exifSubIFDDirectory, ExifSubIFDDirectory.TAG_FOCAL_LENGTH));

            Integer r = MetadataUtils.getIntegerValue(exifSubIFDDirectory, ExifSubIFDDirectory.TAG_RATING);
            if (r == null || r.intValue() <= 0) {
                imageMetadata.setRating(0);
            } else if (r.intValue() > 5) {
                imageMetadata.setRating(5);
            } else {
                imageMetadata.setRating(r.intValue());
            }
        }
        return imageMetadata;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getRawImageName() {
        return rawImageName;
    }

    public void setRawImageName(String rawImageName) {
        this.rawImageName = rawImageName;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public byte getChecksum() {
        return checksum;
    }

    public void setChecksum(byte checksum) {
        this.checksum = checksum;
    }

    public String getImageName() {
        return imageName;
    }

    public void setImageName(String imageName) {
        this.imageName = imageName;
    }

    public String getLastModified() {
        return lastModified;
    }

    public void setLastModified(String lastModified) {
        this.lastModified = lastModified;
    }

    public String getIso() {
        return iso;
    }

    public void setIso(String iso) {
        this.iso = iso;
    }

    public String getLens() {
        return lens;
    }

    public void setLens(String lens) {
        this.lens = lens;
    }

    public String getFocalLength() {
        return focalLength;
    }

    public void setFocalLength(String focalLength) {
        this.focalLength = focalLength;
    }

    public String getExposureTime() {
        return exposureTime;
    }

    public void setExposureTime(String exposureTime) {
        this.exposureTime = exposureTime;
    }

    public String getfNumber() {
        return fNumber;
    }

    public void setfNumber(String fNumber) {
        this.fNumber = fNumber;
    }

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }
}
