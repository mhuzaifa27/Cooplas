
package com.example.cooplas.models.Travel.Customer.Callbacks;

import java.util.List;

import com.example.cooplas.models.Travel.Customer.CurrentLocation;
import com.example.cooplas.models.Travel.Customer.RecentLocation;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class CallbackGetRecentPlaces {

    @SerializedName("current_location")
    @Expose
    private CurrentLocation currentLocation;
    @SerializedName("recent_locations")
    @Expose
    private List<RecentLocation> recentLocations = null;

    public CurrentLocation getCurrentLocation() {
        return currentLocation;
    }

    public void setCurrentLocation(CurrentLocation currentLocation) {
        this.currentLocation = currentLocation;
    }

    public List<RecentLocation> getRecentLocations() {
        return recentLocations;
    }

    public void setRecentLocations(List<RecentLocation> recentLocations) {
        this.recentLocations = recentLocations;
    }

}
