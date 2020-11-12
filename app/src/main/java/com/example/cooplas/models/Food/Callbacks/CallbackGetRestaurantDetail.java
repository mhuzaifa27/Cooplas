
package com.example.cooplas.models.Food.Callbacks;

import java.util.List;

import com.example.cooplas.models.Food.Menu;
import com.example.cooplas.models.Food.Review;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class CallbackGetRestaurantDetail {

    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("cover_pic")
    @Expose
    private String coverPic;
    @SerializedName("about_us")
    @Expose
    private String  aboutUs;
    @SerializedName("location_name")
    @Expose
    private String locationName;
    @SerializedName("stars")
    @Expose
    private String stars;
    @SerializedName("reviews_count")
    @Expose
    private String reviewsCount;
    @SerializedName("reviews")
    @Expose
    private List<Review> reviews = null;
    @SerializedName("menu")
    @Expose
    private List<Menu> menu = null;

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

    public String getAboutUs() {
        return aboutUs;
    }

    public void setAboutUs(String aboutUs) {
        this.aboutUs = aboutUs;
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

    public String getReviewsCount() {
        return reviewsCount;
    }

    public void setReviewsCount(String reviewsCount) {
        this.reviewsCount = reviewsCount;
    }

    public List<Review> getReviews() {
        return reviews;
    }

    public void setReviews(List<Review> reviews) {
        this.reviews = reviews;
    }

    public List<Menu> getMenu() {
        return menu;
    }

    public void setMenu(List<Menu> menu) {
        this.menu = menu;
    }

}
