package com.example.cooplas.models.home.singlePost;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Post {
    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("body")
    @Expose
    private String body;
    @SerializedName("user_id")
    @Expose
    private String userId;
    @SerializedName("created_at")
    @Expose
    private String createdAt;
    @SerializedName("updated_at")
    @Expose
    private String updatedAt;
    @SerializedName("likes_count")
    @Expose
    private String likesCount;
    @SerializedName("comments_count")
    @Expose
    private String commentsCount;
    @SerializedName("is_liked")
    @Expose
    private String isLiked;

    @SerializedName("comments")
    @Expose
    private List<Comment> comments = null;
    @SerializedName("user")
    @Expose
    private User_ user;

    @SerializedName("media")
    @Expose
    private List<Medium> media = null;

    public List<Medium> getMedia() {
        return media;
    }

    public void setMedia(List<Medium> media) {
        this.media = media;
    }


    public static final int TYPE_HORIZONTAL_LIST = 1;
    public static final int TYPE_TEXT = 2;
    public static final int TYPE_IMAGES_SINGLE = 3;
    public static final int TYPE_IMAGES_DOUBLE = 4;
    public static final int TYPE_IMAGES_TRIPPLE = 5;
    public static final int TYPE_IMAGES_MULTIPLE = 6;
    public static final int TYPE_VIDEO = 7;

    int type;


    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }

    public String getLikesCount() {
        return likesCount;
    }

    public void setLikesCount(String likesCount) {
        this.likesCount = likesCount;
    }

    public String getCommentsCount() {
        return commentsCount;
    }

    public void setCommentsCount(String commentsCount) {
        this.commentsCount = commentsCount;
    }

    public String getIsLiked() {
        return isLiked;
    }

    public void setIsLiked(String isLiked) {
        this.isLiked = isLiked;
    }



    public List<Comment> getComments() {
        return comments;
    }

    public void setComments(List<Comment> comments) {
        this.comments = comments;
    }

    public User_ getUser() {
        return user;
    }

    public void setUser(User_ user) {
        this.user = user;
    }

}
