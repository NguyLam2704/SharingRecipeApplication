package com.example.sharingrecipeapp.Classes;

public class Ingredient {
    String id, name, img, donvi, SL;

    public Ingredient(String id, String name, String img, String donvi, String soluong) {
        this.id = id;
        this.name = name;
        this.img = img;
        this.donvi = donvi;
        this.SL = soluong;
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

    public String getDonvi() {
        return donvi;
    }

    public void setDonvi(String donvi) {
        this.donvi = donvi;
    }

    public String getSL() {
        return SL;
    }

    public void setSL(String SL) {
        this.SL = SL;
    }
}

