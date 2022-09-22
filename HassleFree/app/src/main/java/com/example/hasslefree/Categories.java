package com.example.hasslefree;

public class Categories {
    private String title;
    private int image;

    Categories(int image, String title){
        this.image = image;
        this.title = title;
    }

    public String getTitle(){
        return this.title;
    }

    public int getImage(){
        return this.image;
    }
}
