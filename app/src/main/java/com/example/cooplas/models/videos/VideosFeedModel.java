package com.example.cooplas.models.videos;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class VideosFeedModel {


    @SerializedName("next_offset")
    @Expose
    private Integer nextOffset;
    @SerializedName("videos")
    @Expose
    private List<Video> videos = null;

    public Integer getNextOffset() {
        return nextOffset;
    }

    public void setNextOffset(Integer nextOffset) {
        this.nextOffset = nextOffset;
    }

    public List<Video> getVideos() {
        return videos;
    }

    public void setVideos(List<Video> videos) {
        this.videos = videos;
    }
}
