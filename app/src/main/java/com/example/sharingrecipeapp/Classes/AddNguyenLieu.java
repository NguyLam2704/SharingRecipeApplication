package com.example.sharingrecipeapp.Classes;

public class AddNguyenLieu {
    String name, donvi;
    Number soluong;

    public AddNguyenLieu(){}

    public AddNguyenLieu(String name, Number soluong, String donvi) {
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

    public Number getSoluong() {
        double number = (double) soluong;
        int intnum = (int) number;
        if ( number == intnum)
        {return intnum;}
        return soluong;
    }

    public void setSoluong(Number soluong) {
        this.soluong = soluong;
    }

    public String getDonvi() {
        return donvi;
    }

    public void setDonvi(String donvi) {
        this.donvi = donvi;
    }
}
