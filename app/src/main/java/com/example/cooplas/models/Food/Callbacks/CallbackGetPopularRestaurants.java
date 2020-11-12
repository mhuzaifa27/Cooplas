
package com.example.cooplas.models.Food.Callbacks;

import java.util.List;

import com.example.cooplas.models.Food.PopularRestaurant;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class CallbackGetPopularRestaurants {

    @SerializedName("success")
    @Expose
    private Boolean success;
    @SerializedName("popular_restaurants")
    @Expose
    private List<PopularRestaurant> popularRestaurants = null;

    public Boolean getSuccess() {
        return success;
    }

    public void setSuccess(Boolean success) {
        this.success = success;
    }

    public List<PopularRestaurant> getPopularRestaurants() {
        return popularRestaurants;
    }

    public void setPopularRestaurants(List<PopularRestaurant> popularRestaurants) {
        this.popularRestaurants = popularRestaurants;
    }

}
