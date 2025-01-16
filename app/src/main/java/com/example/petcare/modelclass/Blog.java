package com.example.petcare.modelclass;

import java.io.Serializable;

public class Blog implements Serializable {

    private int id;
    private int userId;
    private int petCategoryId;
    private String blogTitle;
    private String blogText;

    // Constructor
    public Blog(int id, int userId, int petCategoryId, String blogTitle, String blogText) {
        this.id = id;
        this.userId = userId;
        this.petCategoryId = petCategoryId;
        this.blogTitle = blogTitle;
        this.blogText = blogText;
    }

    // Getter and Setter for id
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    // Getter and Setter for userId
    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    // Getter and Setter for petCategoryId
    public int getPetCategoryId() {
        return petCategoryId;
    }

    public void setPetCategoryId(int petCategoryId) {
        this.petCategoryId = petCategoryId;
    }

    // Getter and Setter for blogTitle
    public String getBlogTitle() {
        return blogTitle;
    }

    public void setBlogTitle(String blogTitle) {
        this.blogTitle = blogTitle;
    }

    // Getter and Setter for blogText
    public String getBlogText() {
        return blogText;
    }

    public void setBlogText(String blogText) {
        this.blogText = blogText;
    }

    // Optional: Override toString method to display Blog details
    @Override
    public String toString() {
        return "Blog{" +
                "id=" + id +
                ", userId=" + userId +
                ", petCategoryId=" + petCategoryId +
                ", blogTitle='" + blogTitle + '\'' +
                ", blogText='" + blogText + '\'' +
                '}';
    }
}

