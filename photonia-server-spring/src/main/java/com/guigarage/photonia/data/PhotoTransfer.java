package com.guigarage.photonia.data;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class PhotoTransfer {

    private List<Part> parts;

    public PhotoTransfer() {
        parts = new CopyOnWriteArrayList<>();
    }

    public List<Part> getParts() {
        return parts;
    }
}
