package com.example.sharingrecipeapp.Classes;

import android.widget.BaseAdapter;

public class ReGro {
    String id;
    private String nameReGr;

    public void setId(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    private String imgReGr;

    public ReGro(){}

    public ReGro(String id, String nameReGr, String imgReGr) {
        this.id = id;
        this.nameReGr = nameReGr;
        this.imgReGr = imgReGr;
    }

    public String getNameReGr() {
        return nameReGr;
    }

    public void setNameReGr(String nameReGr) {
        this.nameReGr = nameReGr;
    }

    public String getImgReGr() {
        return imgReGr;
    }

    public void setImgReGr(String imgReGr) {
        this.imgReGr = imgReGr;
    }
}
