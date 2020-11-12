package com.example.cooplas.events.home;

public class PostLikeHome {
    int id,position,isLiked;

    public PostLikeHome() {

    }

    public PostLikeHome(int id, int position, int isLiked) {
        this.id = id;
        this.position = position;
        this.isLiked = isLiked;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public int getIsLiked() {
        return isLiked;
    }

    public void setIsLiked(int isLiked) {
        this.isLiked = isLiked;
    }
}
