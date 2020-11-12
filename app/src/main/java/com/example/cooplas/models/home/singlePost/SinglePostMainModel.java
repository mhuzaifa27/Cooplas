package com.example.cooplas.models.home.singlePost;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class SinglePostMainModel {


    @SerializedName("success")
    @Expose
    private Boolean success;
    @SerializedName("comments_next_offset")
    @Expose
    private Integer commentsNextOffset;
    @SerializedName("post")
    @Expose
    private Post post;

    public Boolean getSuccess() {
        return success;
    }

    public void setSuccess(Boolean success) {
        this.success = success;
    }

    public Integer getCommentsNextOffset() {
        return commentsNextOffset;
    }

    public void setCommentsNextOffset(Integer commentsNextOffset) {
        this.commentsNextOffset = commentsNextOffset;
    }

    public Post getPost() {
        return post;
    }

    public void setPost(Post post) {
        this.post = post;
    }

}
