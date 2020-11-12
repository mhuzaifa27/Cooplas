
package com.example.cooplas.models.Travel.Customer.CallbackUpdateVehicleType;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class CallbackUpdateVehicleType {

    @SerializedName("success")
    @Expose
    private Boolean success;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("ride")
    @Expose
    private Ride ride;

    public Boolean getSuccess() {
        return success;
    }

    public void setSuccess(Boolean success) {
        this.success = success;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Ride getRide() {
        return ride;
    }

    public void setRide(Ride ride) {
        this.ride = ride;
    }

}
