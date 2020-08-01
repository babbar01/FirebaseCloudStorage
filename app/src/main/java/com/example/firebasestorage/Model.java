package com.example.firebasestorage;

public class Model
{
    private String imageUrl;
    private String imageName;

    public Model() {
        // empty constructor needed!!
    }

    public Model(String imageUrl, String imageName) {
        this.imageUrl = imageUrl;
        this.imageName = imageName;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getImageName() {
        return imageName;
    }

    public void setImageName(String imageName) {
        this.imageName = imageName;
    }
}
