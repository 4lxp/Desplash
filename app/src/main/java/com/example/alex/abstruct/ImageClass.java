package com.example.alex.abstruct;

import java.io.Serializable;

public class ImageClass  implements Serializable {  //Serializable allow to pass this object in intent

    private String imageSmallUrl;
    private String imageRegularUrl;
    private String imageFullUrl;
    private String imageRawUrl;

    public String getImageSmallUrl() {
        return imageSmallUrl;
    }

    public void setImageSmallUrl(String imageSmallUrl) {
        this.imageSmallUrl = imageSmallUrl;
    }

    public String getImageRegularUrl() {
        return imageRegularUrl;
    }

    public void setImageRegularUrl(String imageRegularUrl) {
        this.imageRegularUrl = imageRegularUrl;
    }
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



}
