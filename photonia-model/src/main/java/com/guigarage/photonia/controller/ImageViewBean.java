package com.guigarage.photonia.controller;

import com.canoo.dolphin.mapping.DolphinBean;
import com.canoo.dolphin.mapping.Property;

@DolphinBean("ImageViewBean")
public class ImageViewBean {

    private Property<Integer> rating;

    private Property<String> id;

    private Property<String> nextId;

    private Property<String> prevId;

    private Property<String> folderName;

    private Property<String> iso;

    private Property<String> lastModified;

    private Property<String> lens;

    private Property<String> focalLength;

    private Property<String> exposureTime;

    private Property<String> fNumber;

    private Property<Boolean> hasRaw;

    private Property<String> imageUrl;

    public Property<String> getImageUrl() {
        return imageUrl;
    }

    public Property<Integer> getRating() {
        return rating;
    }

    public Property<String> getId() {
        return id;
    }

    public Property<String> getIso() {
        return iso;
    }

    public Property<String> getLastModified() {
        return lastModified;
    }

    public Property<String> getLens() {
        return lens;
    }

    public Property<String> getFocalLength() {
        return focalLength;
    }

    public Property<String> getExposureTime() {
        return exposureTime;
    }

    public Property<String> getNextId() {
        return nextId;
    }

    public Property<String> getPrevId() {
        return prevId;
    }

    public Property<String> getFolderName() {
        return folderName;
    }

    public Property<String> getfNumber() {
        return fNumber;
    }

    public Property<Boolean> getHasRaw() {
        return hasRaw;
    }
}
