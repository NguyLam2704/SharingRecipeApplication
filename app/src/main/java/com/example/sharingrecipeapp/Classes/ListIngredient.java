package com.example.sharingrecipeapp.Classes;

import android.widget.CheckBox;

import com.example.sharingrecipeapp.R;

public class ListIngredient {
    private String NameIn;
    private String QuantityIn;
    private int ImageIn;
    private boolean Check ;

    public boolean isCheck() {
        return Check;
    }

    public void setCheck(boolean check) {
        Check = check;
    }




    public ListIngredient(String nameIn, String quantityIn, int imageIn, boolean check) {
        NameIn = nameIn;
        QuantityIn = quantityIn;
        ImageIn = imageIn;
        Check = check;
    }
    public ListIngredient(String nameIn, String quantityIn) {
        NameIn = nameIn;
        QuantityIn = quantityIn;
        ImageIn = R.drawable.diet;
        Check = false;
    }

    public String getNameIn() {
        return NameIn;
    }

    public void setNameIn(String nameIn) {
        NameIn = nameIn;
    }

    public String getQuantityIn() {
        return QuantityIn;
    }

    public void setQuantityIn(String quantityIn) {
        QuantityIn = quantityIn;
    }

    public int getImageIn() {
        return ImageIn;
    }

    public void setImageIn(int imageIn) {
        ImageIn = imageIn;
    }

}
