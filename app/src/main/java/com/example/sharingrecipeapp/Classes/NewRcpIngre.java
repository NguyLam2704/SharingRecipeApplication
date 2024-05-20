package com.example.sharingrecipeapp.Classes;

public class NewRcpIngre {
    public String img;
    public String Name;
    public String dv;
    public String id;

    public NewRcpIngre(String img, String name, String dv , String id) {
        this.img = img;
        this.Name = name;
        this.dv = dv;
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getDv() {
        return dv;
    }

    public void setDv(String dv) {
        this.dv = dv;
    }
}
