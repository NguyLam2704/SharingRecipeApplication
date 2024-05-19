package com.example.sharingrecipeapp.Classes;

public class NguyenLieu {
    public double getSL() {
        return SL;
    }

    public NguyenLieu(double SL, String donvi, String id, String name, String img) {
        this.SL = SL;
        this.donvi = donvi;
        this.id = id;
        this.name = name;
        this.img = img;
    }

    public void setSL(double SL) {
        this.SL = SL;
    }

    double SL;
    String donvi;
    String id;
    String name;
    String img;

    public NguyenLieu() {
    }

    public NguyenLieu(String donvi, String name, String img) {
        this.donvi = donvi;
        this.name = name;
        this.img = img;
    }

    public String getDonvi() {
        return donvi;
    }

    public void setDonvi(String donvi) {
        this.donvi = donvi;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }
}
