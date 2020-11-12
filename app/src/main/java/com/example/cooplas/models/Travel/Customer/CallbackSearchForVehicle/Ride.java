
package com.example.cooplas.models.Travel.Customer.CallbackSearchForVehicle;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Ride {

    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("start_id")
    @Expose
    private String startId;
    @SerializedName("destination_id")
    @Expose
    private String destinationId;
    @SerializedName("user_id")
    @Expose
    private String userId;
    @SerializedName("user")
    @Expose
    private User user;
    @SerializedName("start")
    @Expose
    private Start start;
    @SerializedName("destination")
    @Expose
    private Destination destination;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getStartId() {
        return startId;
    }

    public void setStartId(String startId) {
        this.startId = startId;
    }

    public String getDestinationId() {
        return destinationId;
    }

    public void setDestinationId(String destinationId) {
        this.destinationId = destinationId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
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
