package org.example.models;

import com.google.gson.annotations.SerializedName;

/**
 * Logout Request Model
 */
public class LogoutRequest {

    @SerializedName("facility_id")
    private String facilityId;

    @SerializedName("session_id")
    private String sessionId;

    @SerializedName("user_id")
    private String userId;

    /**
     * Default constructor
     */
    public LogoutRequest() {
    }

    /**
     * Constructor with facility ID
     *
     * @param facilityId Facility ID
     */
    public LogoutRequest(String facilityId) {
        this.facilityId = facilityId;
    }

    /**
     * Constructor with all parameters
     *
     * @param facilityId Facility ID
     * @param sessionId Session ID
     * @param userId User ID
     */
    public LogoutRequest(String facilityId, String sessionId, String userId) {
        this.facilityId = facilityId;
        this.sessionId = sessionId;
        this.userId = userId;
    }

    // Getters and Setters

    public String getFacilityId() {
        return facilityId;
    }

    public void setFacilityId(String facilityId) {
        this.facilityId = facilityId;
    }

    public String getSessionId() {
        return sessionId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    @Override
    public String toString() {
        return "LogoutRequest{" +
                "facilityId='" + facilityId + '\'' +
                ", sessionId='" + sessionId + '\'' +
                ", userId='" + userId + '\'' +
                '}';
    }
}

