
package com.example.cooplas.models.Food;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class PopularRestaurant {

    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("cover_pic")
    @Expose
    private String coverPic;
    @SerializedName("location_name")
    @Expose
    private String locationName;
    @SerializedName("stars")
    @Expose
    private String stars;
    @SerializedName("review_count")
    @Expose
    private String reviewCount;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCoverPic() {
        return coverPic;
    }

    public void setCoverPic(String coverPic) {
        this.coverPic = coverPic;
    }

    public String getLocationName() {
        return locationName;
    }

    public void setLocationName(String locationName) {
        this.locationName = locationName;
    }

    public String getStars() {
        return stars;
    }

    public void setStars(String stars) {
        this.stars = stars;
    }

    public String getReviewCount() {
        return reviewCount;
    }

    public void setReviewCount(String reviewCount) {
        this.reviewCount = reviewCount;
    }

}
