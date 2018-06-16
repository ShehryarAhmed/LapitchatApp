package com.logic.tech.chatapp;

/**
 * Created by android on 1/8/2018.
 */

public class User {
    String name;
    String status;
    String image;
    String thumb_image;

    public String getThumb_image() {
        return thumb_image;
    }

    public void setThumb_image(String thumb__image) {
        this.thumb_image = thumb__image;
    }

    public User(){}
    public User(String name, String status, String image) {
        this.name = name;
        this.status = status;
        this.image = image;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
