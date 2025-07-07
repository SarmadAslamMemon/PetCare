package com.example.petcare.model;

import com.example.petcare.modelclass.User;
import com.google.gson.annotations.SerializedName;
import java.io.Serializable;

public class Blog implements Serializable {
    private static final long serialVersionUID = 1L;

    @SerializedName("id")
    private int id;

    @SerializedName("userId")
    private int userId;

    @SerializedName("blogTitle")
    private String blogTitle;

    @SerializedName("blogText")
    private String blogText;

    @SerializedName("imageUrl")
    private String imageUrl;

    @SerializedName("user")
    private User user;

    // Default constructor
    public Blog() {
    }

    public Blog(int userId, String blogTitle, String blogText, String imageUrl) {
        this.userId = userId;
        this.blogTitle = blogTitle;
        this.blogText = blogText;
        this.imageUrl = imageUrl;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getBlogTitle() {
        return blogTitle;
    }

    public void setBlogTitle(String blogTitle) {
        this.blogTitle = blogTitle;
    }

    public String getBlogText() {
        return blogText;
    }

    public void setBlogText(String blogText) {
        this.blogText = blogText;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getAuthorName() {
        if (user != null) {
            return user.getFirstName();
        }
        return "Unknown Author";
    }
} 