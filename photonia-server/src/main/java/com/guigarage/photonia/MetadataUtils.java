package com.guigarage.photonia;

import com.drew.imaging.ImageMetadataReader;
import com.drew.imaging.ImageProcessingException;
import com.drew.metadata.Directory;
import com.drew.metadata.Metadata;
import com.drew.metadata.Tag;
import com.guigarage.photonia.types.JpegFileType;
import com.guigarage.photonia.types.JpegImageFile;
import com.guigarage.photonia.types.NefFileType;
import com.guigarage.photonia.types.RawImageFile;
import org.apache.commons.io.FilenameUtils;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class MetadataUtils {

    public static final JpegFileType JPEG_FILE_TYPE = new JpegFileType();

    public static boolean hasRaw(JpegImageFile jpegImageFile) {
        File folder = jpegImageFile.getParentFolder().toFile();
        for (File file : folder.listFiles()) {
            if (file.getName().toLowerCase().equals(jpegImageFile.getFileNameWithoutExtension() + ".nef".toLowerCase())) {
                return true;
            }
        }
        return false;
    }

    public static void printAll(Metadata metadata) {
        for (Directory directory : metadata.getDirectories()) {
            System.out.println("Directory: " + directory.getClass() + " - " + directory.getName() + " - " + directory);
            for (Tag tag : directory.getTags()) {
                System.out.println("Tag: " + tag.getTagType() + " - " + tag);
            }
        }
    }

    public static Metadata getMetadata(File file) throws ImageProcessingException, IOException {
        return ImageMetadataReader.readMetadata(file);
    }

    public static Metadata getMetadata(String filepath) throws ImageProcessingException, IOException {
        File jpegFile = new File(filepath);
        return getMetadata(jpegFile);
    }

    public static <T extends Directory> T getDirectory(Metadata metadata, Class<T> directoryClass) {
        Collection<T> directories = metadata.getDirectoriesOfType(directoryClass);
        if (directories == null || directories.isEmpty()) {
            return null;
        }
        return directories.iterator().next();
    }

    public static String getValue(Directory directory, int tagId) {
        if (directory.hasTagName(tagId)) {
            return directory.getString(tagId);
        } else {
            return null;
        }
    }

    public static Integer getIntegerValue(Directory directory, int tagId) {
        if (directory.hasTagName(tagId)) {
            return directory.getInteger(tagId);
        } else {
            return null;
        }
    }

    public static void setIntegerValue(Directory directory, int tagId, int value) {
        directory.setInt(tagId, value);
    }

    public static RawImageFile getRawForImage(Path parentFolder, String name) {
        //TODO: change to SPI (RawFileType)
        NefFileType nefFileType = new NefFileType();

        for (File file : parentFolder.toFile().listFiles()) {
            if (FilenameUtils.getBaseName(file.getName()).equals(FilenameUtils.getBaseName(name)) && FilenameUtils.isExtension(file.getName(), toCollection(nefFileType.getAllowedExtensions()))) {
                return nefFileType.getRawImageFile(parentFolder, file.getName());
            }
        }
        return null;
    }

    public static <E> Collection<E> toCollection(Iterable<E> iterable) {
        List<E> collection = new ArrayList<E>();
        iterable.forEach(e -> collection.add(e));
        return collection;
    }

    public static boolean hasXmp(Path parentFolder, String name) {
        for (File file : parentFolder.toFile().listFiles()) {
            if (FilenameUtils.getBaseName(file.getName()).equals(FilenameUtils.getBaseName(name)) && FilenameUtils.getExtension(name).equals("xmp")) {
                return true;
            }
        }
        return false;
    }
}
