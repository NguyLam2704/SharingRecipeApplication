package com.example.sharingrecipeapp.Classes;

public class AddNguyenLieu {
    String name, soluong, donvi;

    public AddNguyenLieu(){}

    public AddNguyenLieu(String name, String soluong, String donvi) {
        this.name = name;
        this.soluong = soluong;
        this.donvi = donvi;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSoluong() {
        return soluong;
    }

    public void setSoluong(String soluong) {
        this.soluong = soluong;
    }

    public String getDonvi() {
        return donvi;
    }

    public void setDonvi(String donvi) {
        this.donvi = donvi;
    }
}
