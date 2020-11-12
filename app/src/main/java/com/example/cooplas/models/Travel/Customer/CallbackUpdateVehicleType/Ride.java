
package com.example.cooplas.models.Travel.Customer.CallbackUpdateVehicleType;

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
    @SerializedName("driver_id")
    @Expose
    private Object driverId;
    @SerializedName("vehicle_id")
    @Expose
    private Object vehicleId;
    @SerializedName("vehicle_type")
    @Expose
    private String vehicleType;
    @SerializedName("time")
    @Expose
    private Object time;
    @SerializedName("price")
    @Expose
    private Integer price;
    @SerializedName("distance")
    @Expose
    private Object distance;
    @SerializedName("status")
    @Expose
    private Object status;
    @SerializedName("cancellation_reason")
    @Expose
    private Object cancellationReason;
    @SerializedName("user_stars")
    @Expose
    private Object userStars;
    @SerializedName("user_comments")
    @Expose
    private Object userComments;
    @SerializedName("driver_stars")
    @Expose
    private Object driverStars;
    @SerializedName("driver_comments")
    @Expose
    private Object driverComments;
    @SerializedName("created_at")
    @Expose
    private String createdAt;
    @SerializedName("updated_at")
    @Expose
    private String updatedAt;
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

    public Object getDriverId() {
        return driverId;
    }

    public void setDriverId(Object driverId) {
        this.driverId = driverId;
    }

    public Object getVehicleId() {
        return vehicleId;
    }

    public void setVehicleId(Object vehicleId) {
        this.vehicleId = vehicleId;
    }

    public String getVehicleType() {
        return vehicleType;
    }

    public void setVehicleType(String vehicleType) {
        this.vehicleType = vehicleType;
    }

    public Object getTime() {
        return time;
    }

    public void setTime(Object time) {
        this.time = time;
    }

    public Integer getPrice() {
        return price;
    }

    public void setPrice(Integer price) {
        this.price = price;
    }

    public Object getDistance() {
        return distance;
    }

    public void setDistance(Object distance) {
        this.distance = distance;
    }

    public Object getStatus() {
        return status;
    }

    public void setStatus(Object status) {
        this.status = status;
    }

    public Object getCancellationReason() {
        return cancellationReason;
    }

    public void setCancellationReason(Object cancellationReason) {
        this.cancellationReason = cancellationReason;
    }

    public Object getUserStars() {
        return userStars;
    }

    public void setUserStars(Object userStars) {
        this.userStars = userStars;
    }

    public Object getUserComments() {
        return userComments;
    }

    public void setUserComments(Object userComments) {
        this.userComments = userComments;
    }

    public Object getDriverStars() {
        return driverStars;
    }

    public void setDriverStars(Object driverStars) {
        this.driverStars = driverStars;
    }

    public Object getDriverComments() {
        return driverComments;
    }

    public void setDriverComments(Object driverComments) {
        this.driverComments = driverComments;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
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
