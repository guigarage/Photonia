package com.guigarage.photonia.data;

import com.drew.imaging.ImageMetadataReader;
import com.drew.imaging.ImageProcessingException;
import com.drew.metadata.Directory;
import com.drew.metadata.Metadata;
import com.drew.metadata.exif.ExifSubIFDDirectory;
import com.drew.metadata.exif.makernotes.NikonType2MakernoteDirectory;
import com.drew.metadata.file.FileMetadataDirectory;
import com.guigarage.photonia.util.Locker;
import org.apache.commons.io.FilenameUtils;

import java.io.IOException;
import java.io.Serializable;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Created by hendrikebbers on 21.09.15.
 */
public class Photo implements Serializable{

    private final AbstractAlbum album;

    private final String uuid;

    private final List<String> files;

    private final Locker filesLocker;

    private String name;

    private String lastModified;

    private String iso;

    private String lens;

    private String focalLength;

    private String exposureTime;

    private String fNumber;

    private int rating;

    public Photo(final String uuid, final AbstractAlbum album, final String... files) {
        this.album = album;
        this.uuid = uuid;
        this.files =  new CopyOnWriteArrayList<>();
        this.filesLocker = new Locker();

        for(String file : files) {
            this.files.add(file);
        }
    }

    public String getUuid() {
        return uuid;
    }

    public String getName() {
        return name;
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

    public AbstractAlbum getAlbum() {
        return album;
    }

    public void delete() {
        for (String fileName : getFileNamesReadable()) {
            Path imagePath = Paths.get(album.getPathPrefix(), album.getName(), uuid + fileName);
            try {
                Files.delete(imagePath);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public void addFile(String name) throws IOException, ImageProcessingException {
        filesLocker.runLocked(() -> files.add(name));
        updateMetadata();
    }

    public List<String> getFileNamesReadable() {
        return filesLocker.callLocked(() -> Collections.unmodifiableList(files));
    }

    public PhotoTransfer createTransfer() {
        return null;
    }

    private void updateMetadata() throws IOException, ImageProcessingException {

        //TODO: Do for all files
        Path filePath = Paths.get(album.getPathPrefix(), album.getName(), uuid + files.get(0));
        Metadata metadata = ImageMetadataReader.readMetadata(Files.newInputStream(filePath));

        FileMetadataDirectory fileMetadataDirectory = getExifDirectory(metadata, FileMetadataDirectory.class);
        if (fileMetadataDirectory != null) {
            name = getExifValue(fileMetadataDirectory, FileMetadataDirectory.TAG_FILE_NAME);
            lastModified = getExifValue(fileMetadataDirectory, FileMetadataDirectory.TAG_FILE_MODIFIED_DATE);
        }

        NikonType2MakernoteDirectory nikonType2MakernoteDirectory = getExifDirectory(metadata, NikonType2MakernoteDirectory.class);
        if (nikonType2MakernoteDirectory != null) {
            lens = getExifValue(nikonType2MakernoteDirectory, NikonType2MakernoteDirectory.TAG_LENS);
        }

        ExifSubIFDDirectory exifSubIFDDirectory = getExifDirectory(metadata, ExifSubIFDDirectory.class);
        if (exifSubIFDDirectory != null) {
            iso = getExifValue(exifSubIFDDirectory, ExifSubIFDDirectory.TAG_ISO_EQUIVALENT);
            exposureTime = getExifValue(exifSubIFDDirectory, ExifSubIFDDirectory.TAG_EXPOSURE_TIME);
            fNumber = getExifValue(exifSubIFDDirectory, ExifSubIFDDirectory.TAG_FNUMBER);
            focalLength = getExifValue(exifSubIFDDirectory, ExifSubIFDDirectory.TAG_FOCAL_LENGTH);

            Integer r = getIntegerExifValue(exifSubIFDDirectory, ExifSubIFDDirectory.TAG_RATING);
            if (r == null || r.intValue() <= 0) {
                rating = 0;
            } else if (r.intValue() > 5) {
                rating = 5;
            } else {
                rating = r.intValue();
            }
        }
    }

    private <T extends Directory> T getExifDirectory(Metadata metadata, Class<T> directoryClass) {
        Collection<T> directories = metadata.getDirectoriesOfType(directoryClass);
        if (directories == null || directories.isEmpty()) {
            return null;
        }
        return directories.iterator().next();
    }

    private String getExifValue(Directory directory, int tagId) {
        if (directory.hasTagName(tagId)) {
            return directory.getString(tagId);
        } else {
            return null;
        }
    }

    private Integer getIntegerExifValue(Directory directory, int tagId) {
        if (directory.hasTagName(tagId)) {
            return directory.getInteger(tagId);
        } else {
            return null;
        }
    }

    public String getBaseName() {
        return filesLocker.callLocked(() -> FilenameUtils.getBaseName(files.get(0)));
    }
}
