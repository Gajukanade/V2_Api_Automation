package org.example.models;

import com.google.gson.annotations.SerializedName;

/**
 * Login Request model for hospital admin authentication
 */
public class LoginRequest {
    private String email;
    private String password;

    @SerializedName("facility_id")
    private String facilityId;

    @SerializedName("facility_name")
    private String facilityName;

    @SerializedName("deviceId")
    private String deviceId;

    // Constructor
    public LoginRequest() {
    }

    public LoginRequest(String email, String password, String facilityId, String facilityName, String deviceId) {
        this.email = email;
        this.password = password;
        this.facilityId = facilityId;
        this.facilityName = facilityName;
        this.deviceId = deviceId;
    }

    // Getters and Setters
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getFacilityId() {
        return facilityId;
    }

    public void setFacilityId(String facilityId) {
        this.facilityId = facilityId;
    }

    public String getFacilityName() {
        return facilityName;
    }

    public void setFacilityName(String facilityName) {
        this.facilityName = facilityName;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    @Override
    public String toString() {
        return "LoginRequest{" +
                "email='" + email + '\'' +
                ", password='" + password + '\'' +
                ", facilityId='" + facilityId + '\'' +
                ", facilityName='" + facilityName + '\'' +
                ", deviceId='" + deviceId + '\'' +
                '}';
    }
}

