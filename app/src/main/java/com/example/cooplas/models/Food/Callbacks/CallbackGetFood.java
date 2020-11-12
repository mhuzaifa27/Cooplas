
package com.example.cooplas.models.Food.Callbacks;

import java.util.List;

import com.example.cooplas.models.Food.Category;
import com.example.cooplas.models.Food.ForYou;
import com.example.cooplas.models.Food.NearYou;
import com.example.cooplas.models.Food.PopularRestaurant;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class CallbackGetFood {

    @SerializedName("success")
    @Expose
    private Boolean success;
    @SerializedName("categories")
    @Expose
    private List<Category> categories = null;
    @SerializedName("for_you")
    @Expose
    private List<ForYou> forYou = null;
    @SerializedName("near_you")
    @Expose
    private List<NearYou> nearYou = null;
    @SerializedName("popular_restaurants")
    @Expose
    private List<PopularRestaurant> popularRestaurants = null;

    public Boolean getSuccess() {
        return success;
    }

    public void setSuccess(Boolean success) {
        this.success = success;
    }

    public List<Category> getCategories() {
        return categories;
    }

    public void setCategories(List<Category> categories) {
        this.categories = categories;
    }

    public List<ForYou> getForYou() {
        return forYou;
    }

    public void setForYou(List<ForYou> forYou) {
        this.forYou = forYou;
    }

    public List<NearYou> getNearYou() {
        return nearYou;
    }

    public void setNearYou(List<NearYou> nearYou) {
        this.nearYou = nearYou;
    }

    public List<PopularRestaurant> getPopularRestaurants() {
        return popularRestaurants;
    }

    public void setPopularRestaurants(List<PopularRestaurant> popularRestaurants) {
        this.popularRestaurants = popularRestaurants;
    }

}
