package com.example.cooplas.models.homeFragmentModel;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class HomeModel {



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
