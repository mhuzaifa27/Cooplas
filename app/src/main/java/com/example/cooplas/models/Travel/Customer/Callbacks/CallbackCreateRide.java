
package com.example.cooplas.models.Travel.Customer.Callbacks;

import com.example.cooplas.models.Travel.Customer.Destination;
import com.example.cooplas.models.Travel.Customer.Ride;
import com.example.cooplas.models.Travel.Customer.Start;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class CallbackCreateRide implements Serializable {

    @SerializedName("success")
    @Expose
    private Boolean success;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("ride")
    @Expose
    private Ride ride;
    @SerializedName("start")
    @Expose
    private Start start;
    @SerializedName("destination")
    @Expose
    private Destination destination;

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

    public Start getStart() {
        return start;
    }

    public void setStart(Start start) {
        this.start = start;
    }

    public Destination getDestination() {
        return destination;
    }

    public void setDestination(Destination destination) {
        this.destination = destination;
    }

}
