package com.example.cooplas.models.profile.Following;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class FollowingModel {


    @SerializedName("next_offset")
    @Expose
    private Integer nextOffset;
    @SerializedName("following")
    @Expose
    private List<Following> following = null;

    public Integer getNextOffset() {
        return nextOffset;
    }

    public void setNextOffset(Integer nextOffset) {
        this.nextOffset = nextOffset;
    }

    public List<Following> getFollowing() {
        return following;
    }

    public void setFollowing(List<Following> following) {
        this.following = following;
    }
}
