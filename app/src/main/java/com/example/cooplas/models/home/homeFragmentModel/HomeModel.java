package com.example.cooplas.models.home.homeFragmentModel;



import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class HomeModel {



    @SerializedName("stories_next_offset")
    @Expose
    private Integer storiesNextOffset;
    @SerializedName("user_stories")
    @Expose
    private List<UserStory> userStories = null;
    @SerializedName("posts_next_offset")
    @Expose
    private Integer postsNextOffset;
    @SerializedName("posts")
    @Expose
    private List<Post> posts = null;

    public Integer getStoriesNextOffset() {
        return storiesNextOffset;
    }

    public void setStoriesNextOffset(Integer storiesNextOffset) {
        this.storiesNextOffset = storiesNextOffset;
    }

    public List<UserStory> getUserStories() {
        return userStories;
    }

    public void setUserStories(List<UserStory> userStories) {
        this.userStories = userStories;
    }

    public Integer getPostsNextOffset() {
        return postsNextOffset;
    }

    public void setPostsNextOffset(Integer postsNextOffset) {
        this.postsNextOffset = postsNextOffset;
    }

    public List<Post> getPosts() {
        return posts;
    }

    public void setPosts(List<Post> posts) {
        this.posts = posts;
    }



//
//
//    @SerializedName("stories")
//    @Expose
//    private List<Object> stories = null;
//    @SerializedName("posts_next_offset")
//    @Expose
//    private Integer postsNextOffset;
//    @SerializedName("posts")
//    @Expose
//    private List<Post> posts = null;
//
//    public List<Object> getStories() {
//        return stories;
//    }
//
//    public void setStories(List<Object> stories) {
//        this.stories = stories;
//    }
//
//    public Integer getPostsNextOffset() {
//        return postsNextOffset;
//    }
//
//    public void setPostsNextOffset(Integer postsNextOffset) {
//        this.postsNextOffset = postsNextOffset;
//    }
//
//    public List<Post> getPosts() {
//        return posts;
//    }
//
//    public void setPosts(List<Post> posts) {
//        this.posts = posts;
//    }

}
