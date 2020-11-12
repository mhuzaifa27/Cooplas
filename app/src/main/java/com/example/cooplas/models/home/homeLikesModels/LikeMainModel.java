package com.example.cooplas.models.home.homeLikesModels;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class LikeMainModel {


        @SerializedName("next_offset")
        @Expose
        private Integer nextOffset;
        @SerializedName("likes")
        @Expose
        private List<Like> likes = null;

        public Integer getNextOffset() {
            return nextOffset;
        }

        public void setNextOffset(Integer nextOffset) {
            this.nextOffset = nextOffset;
        }

        public List<Like> getLikes() {
            return likes;
        }

        public void setLikes(List<Like> likes) {
            this.likes = likes;
        }

    }
