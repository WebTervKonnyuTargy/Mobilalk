package com.example.gyumi;

public class ShoppingItem {
    private String nev;
    private String info;
    private String xd;
    private float rating;
    private int imgResource;

    public ShoppingItem(String nev, String info, String xd, float rating, int imgResource) {
        this.nev = nev;
        this.info = info;
        this.xd = xd;
        this.rating = rating;
        this.imgResource = imgResource;
    }

    public ShoppingItem() {
    }

    public String getNev() {
        return nev;
    }

    public String getInfo() {
        return info;
    }

    public String getXd() {
        return xd;
    }

    public float getRating() {
        return rating;
    }

    public int getImgResource() {
        return imgResource;
    }
}
