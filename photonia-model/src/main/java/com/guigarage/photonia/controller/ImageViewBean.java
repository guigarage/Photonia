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

    public Property<String> imageUrlProperty() {
        return imageUrl;
    }

    public Property<Integer> ratingProperty() {
        return rating;
    }

    public Property<String> idProperty() {
        return id;
    }

    public String getId() {
        return id.get();
    }

    public Property<String> isoProperty() {
        return iso;
    }

    public Property<String> lastModifiedProperty() {
        return lastModified;
    }

    public Property<String> lensProperty() {
        return lens;
    }

    public Property<String> focalLengthProperty() {
        return focalLength;
    }

    public Property<String> exposureTimeProperty() {
        return exposureTime;
    }

    public Property<String> nextIdProperty() {
        return nextId;
    }

    public Property<String> prevIdProperty() {
        return prevId;
    }

    public Property<String> folderNameProperty() {
        return folderName;
    }

    public Property<String> fNumberProperty() {
        return fNumber;
    }

    public Property<Boolean> hasRawProperty() {
        return hasRaw;
    }
}
