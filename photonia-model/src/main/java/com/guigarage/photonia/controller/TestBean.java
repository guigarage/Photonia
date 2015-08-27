package com.guigarage.photonia.controller;

import com.canoo.dolphin.collections.ObservableList;
import com.canoo.dolphin.mapping.DolphinBean;
import com.canoo.dolphin.mapping.Property;

import java.util.List;

@DolphinBean
public class TestBean {

    private Property<String> selectedAbteilung;

    private Property<String> selectedDienst;

    private ObservableList<String> allAbteilung;

    private ObservableList<String> allDienst;

    public Property<String> getSelectedAbteilung() {
        return selectedAbteilung;
    }

    public Property<String> getSelectedDienst() {
        return selectedDienst;
    }

    public ObservableList<String> getAllAbteilung() {
        return allAbteilung;
    }

    public ObservableList<String> getAllDienst() {
        return allDienst;
    }
}


public class Controller {

    public void init() {
        TestBean bean = null;


        bean.getSelectedAbteilung().onChanged(e -> {
            bean.getSelectedDienst().set(null);
            bean.getAllDienst().setAll(getAllDiensteForAbteiling(e.getNewValue()));
        });
    }

    public List<String> getAllDiensteForAbteiling(String abt) {
        return null;
    }
}
