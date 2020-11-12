package com.example.cooplas.events.videos;

public class VideosTagEvent {

    String title;

    public VideosTagEvent(String title) {
        this.title = title;
    }

    public VideosTagEvent() {

    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
