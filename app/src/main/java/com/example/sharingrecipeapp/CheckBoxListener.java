package com.example.sharingrecipeapp;

import com.example.sharingrecipeapp.Classes.Ingredient;
import com.example.sharingrecipeapp.Classes.SoLuongIngre;

import java.util.ArrayList;

public interface CheckBoxListener {
    void onCheckBoxChange(ArrayList<Ingredient> ingredients, ArrayList<SoLuongIngre> soLuongIngres);
}
