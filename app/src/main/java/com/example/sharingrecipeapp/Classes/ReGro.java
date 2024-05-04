package com.example.sharingrecipeapp.Classes;

import android.widget.BaseAdapter;

public class ReGro {
    private String nameReGr;
    private int imgReGr;

    public ReGro(){}


    public ReGro(String nameReGr, int imgReGr) {
        this.nameReGr = nameReGr;
        this.imgReGr = imgReGr;
    }

    public String getNameReGr() {
        return nameReGr;
    }

    public void setNameReGr(String nameReGr) {
        this.nameReGr = nameReGr;
    }

    public int getImgReGr() {
        return imgReGr;
    }

    public void setImgReGr(int imgReGr) {
        this.imgReGr = imgReGr;
    }
}
