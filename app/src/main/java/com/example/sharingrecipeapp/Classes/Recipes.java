package com.example.sharingrecipeapp.Classes;

public class Recipes {
    String id, image, name, save, timecook;

    public Recipes() {
    }

    public Recipes(String id, String image, String name, String save, String timecook) {
        this.id = id;
        this.image = image;
        this.name = name;
        this.save = save;
        this.timecook = timecook;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSave() {
        return save;
    }

    public void setSave(String save) {
        this.save = save;
    }

    public String getTimecook() {
        return timecook;
    }

    public void setTimecook(String timecook) {
        this.timecook = timecook;
    }
}
