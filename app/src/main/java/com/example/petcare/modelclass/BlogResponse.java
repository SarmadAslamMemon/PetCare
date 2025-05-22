package com.example.petcare.modelclass;

import com.google.gson.annotations.SerializedName;

public class BlogResponse {
    @SerializedName("success")
    private boolean success;

    @SerializedName("message")
    private String message;

    @SerializedName("data")
    private BlogData data;

    public boolean isSuccess() {
        return success;
    }

    public String getMessage() {
        return message;
    }

    public BlogData getData() {
        return data;
    }

    public static class BlogData {
        @SerializedName("id")
        private int id;

        @SerializedName("blogTitle")
        private String blogTitle;

        @SerializedName("blogText")
        private String blogText;

        @SerializedName("user")
        private User user;

        @SerializedName("imageUrl")
        private String imageUrl;

        public int getId() {
            return id;
        }

        public String getBlogTitle() {
            return blogTitle;
        }

        public String getBlogText() {
            return blogText;
        }

        public User getUser() {
            return user;
        }

        public String getImageUrl() {
            return imageUrl;
        }
    }
} 