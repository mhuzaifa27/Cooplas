
package com.example.cooplas.models.Travel.Customer.CallbackAcceptRide;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Vehicle implements Serializable {

    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("maker")
    @Expose
    private String maker;
    @SerializedName("model")
    @Expose
    private String model;
    @SerializedName("registration_number")
    @Expose
    private String registrationNumber;
    @SerializedName("driver_id")
    @Expose
    private String driverId;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getMaker() {
        return maker;
    }

    public void setMaker(String maker) {
        this.maker = maker;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getRegistrationNumber() {
        return registrationNumber;
    }

    public void setRegistrationNumber(String registrationNumber) {
        this.registrationNumber = registrationNumber;
    }

    public String getDriverId() {
        return driverId;
    }

    public void setDriverId(String driverId) {
        this.driverId = driverId;
    }

}
