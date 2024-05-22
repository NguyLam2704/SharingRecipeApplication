package com.example.sharingrecipeapp.Classes;

public class AddNguyenLieu {
    NewRcpIngre newRcpIngre;
    String name, donvi;
    Number soluong;
    String img;

    public AddNguyenLieu(){}

    public AddNguyenLieu(NewRcpIngre newRcpIngre, Number soluong) {

        this.newRcpIngre = newRcpIngre;
        this.img = newRcpIngre.getImg();
        this.name = newRcpIngre.getName();
        this.soluong = soluong;
        this.donvi = newRcpIngre.getDv();
    }

    public NewRcpIngre getNewRcpIngre() {
        return newRcpIngre;
    }

    public String getName() {
        return name;
    }
    public String getImg() {
        return img;
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
