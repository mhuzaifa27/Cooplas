package com.example.cooplas.models.profile;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ProfileModel {



    @SerializedName("posts_next_offset")
    @Expose
    private Integer postsNextOffset;
    @SerializedName("wall")
    @Expose
    private Wall wall;

    public Integer getPostsNextOffset() {
        return postsNextOffset;
    }

    public void setPostsNextOffset(Integer postsNextOffset) {
        this.postsNextOffset = postsNextOffset;
    }

    public Wall getWall() {
        return wall;
    }

    public void setWall(Wall wall) {
        this.wall = wall;
    }



}
