package com.example.sharingrecipeapp.Classes;

import android.widget.ImageView;

public class ABrief {
    private String NameRe;
    private int ImgRe;
    private String TimeRe;
    private String Star;
    private String Save;


    public ABrief(String nameRe, int imgRe, String timeRe, String star, String save) {
        NameRe = nameRe;
        ImgRe = imgRe;
        TimeRe = timeRe;
        Star = star;
        Save = save;
    }

    public String getNameRe() {
        return NameRe;
    }

    public void setNameRe(String nameRe) {
        NameRe = nameRe;
    }

    public int getImgRe() {
        return ImgRe;
    }

    public void setImgRe(int imgRe) {
        ImgRe = imgRe;
    }

    public String getTimeRe() {
        return TimeRe;
    }

    public void setTimeRe(String timeRe) {
        TimeRe = timeRe;
    }

    public String getStar() {
        return Star;
    }

    public void setStar(String star) {
        Star = star;
    }

    public String getSave() {
        return Save;
    }

    public void setSave(String save) {
        Save = save;
    }
}
