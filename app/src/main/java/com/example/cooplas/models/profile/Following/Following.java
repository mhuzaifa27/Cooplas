package com.example.cooplas.models.profile.Following;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Following {

    String isFollowing;

    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("first_name")
    @Expose
    private String firstName;
    @SerializedName("last_name")
    @Expose
    private String lastName;
    @SerializedName("profile_pic")
    @Expose
    private String profilePic;

    public String getIsFollowing() {
        return isFollowing;
    }

    public void setIsFollowing(String isFollowing) {
        this.isFollowing = isFollowing;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getProfilePic() {
        return profilePic;
    }

    public void setProfilePic(String profilePic) {
        this.profilePic = profilePic;
    }
}
