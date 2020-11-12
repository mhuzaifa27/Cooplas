package com.example.cooplas.models.home.commentModels;

import com.example.cooplas.models.home.singlePost.Comment;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class CommentMainModel {


    @SerializedName("success")
    @Expose
    private Boolean success;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("comment")
    @Expose
    private Comment comment;

    public Boolean getSuccess() {
        return success;
    }

    public void setSuccess(Boolean success) {
        this.success = success;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Comment getComment() {
        return comment;
    }

    public void setComment(Comment comment) {
        this.comment = comment;
    }
}
