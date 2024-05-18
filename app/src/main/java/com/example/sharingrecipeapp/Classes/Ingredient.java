package com.example.sharingrecipeapp.Classes;

public class Ingredient {
    String id, name, img, donvi;


    public Ingredient(String id) {
        this.id = id;
        this.name = "";
        this.img = "";
        this.donvi = "";
    }
    public Ingredient(String id, String name, String img, String donvi) {
        this.id = id;
        this.name = name;
        this.img = img;
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

    public String getDonvi() {
        return donvi;
    }

    public void setDonvi(String donvi) {
        this.donvi = donvi;
    }


}

