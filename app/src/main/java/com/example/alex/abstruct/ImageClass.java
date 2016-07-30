package com.example.alex.abstruct;

import java.io.Serializable;

public class ImageClass  implements Serializable {  //Serializable allow to pass this object in intent

    private String imageSmallUrl;
    private String imageRegularUrl;
    private String imageFullUrl;
    private String imageRawUrl;
    private String color;
    private int likes;
    private String authorUserName;
    private String authorName;
    private String authorImage;


    public String getImageSmallUrl() {
        return imageSmallUrl;
    }

    public void setImageSmallUrl(String imageSmallUrl) {
        this.imageSmallUrl = imageSmallUrl;
    }

    public String getImageRegularUrl() {
        return imageRegularUrl;
    }

    public void setImageRegularUrl(String imageRegularUrl) {this.imageRegularUrl = imageRegularUrl;}

    public String getImageFullUrl() {
        return imageFullUrl;
    }

    public void setImageFullUrl(String imageFullUrl) {
        this.imageFullUrl = imageFullUrl;
    }

    public String getImageRawUrl() {
        return imageRawUrl;
    }

    public void setImageRawUrl(String imageRawUrl) {
        this.imageRawUrl = imageRawUrl;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public int getLikes() {
        return likes;
    }

    public void setLikes(int likes) {
        this.likes = likes;
    }

    public String getAuthorUserName() {
        return authorUserName;
    }

    public void setAuthorUserName(String authorUserName) {
        this.authorUserName = authorUserName;
    }

    public String getAuthorName() {
        return authorName;
    }

    public void setAuthorName(String authorName) {
        this.authorName = authorName;
    }

    public String getAuthorImage() {
        return authorImage;
    }

    public void setAuthorImage(String authorImage) {
        this.authorImage = authorImage;
    }




}
