package com.example.cooplas.models.profile.Followers;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class FollowersModel {
    @SerializedName("next_offset")
    @Expose
    private Integer nextOffset;
    @SerializedName("followers")
    @Expose
    private List<Follower> followers = null;

    public Integer getNextOffset() {
        return nextOffset;
    }

    public void setNextOffset(Integer nextOffset) {
        this.nextOffset = nextOffset;
    }

    public List<Follower> getFollowers() {
        return followers;
    }

    public void setFollowers(List<Follower> followers) {
        this.followers = followers;
    }
}
