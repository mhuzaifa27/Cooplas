
package com.example.cooplas.models.Food.Callbacks;

import java.util.List;

import com.example.cooplas.models.Food.NearYou;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class CallbackGetNearbyRestaurants {

    @SerializedName("success")
    @Expose
    private Boolean success;
    @SerializedName("nearby_restaurants")
    @Expose
    private List<NearYou> nearbyRestaurants = null;

    public Boolean getSuccess() {
        return success;
    }

    public void setSuccess(Boolean success) {
        this.success = success;
    }

    public List<NearYou> getNearbyRestaurants() {
        return nearbyRestaurants;
    }

    public void setNearbyRestaurants(List<NearYou> nearbyRestaurants) {
        this.nearbyRestaurants = nearbyRestaurants;
    }

}
