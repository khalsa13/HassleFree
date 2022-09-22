package com.example.hasslefree;

public class Categories {
    private String title;
    private String image;

    Categories(String image, String title){
        this.image = image;
        this.title = title;
    }

    public String getTitle(){
        return this.title;
    }

    public String getImage(){
        return this.image;
    }
}
