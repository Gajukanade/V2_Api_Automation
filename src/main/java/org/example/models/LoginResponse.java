package org.example.models;

import com.google.gson.annotations.SerializedName;


/**
 * Login Response model for hospital admin authentication
 * Maps to actual API response with accessToken and refreshToken
 */
public class LoginResponse {
    @SerializedName("success")
    private Boolean success;

    @SerializedName("accessToken")
    private String token;

    @SerializedName("refreshToken")
    private String refreshToken;

    private String message;
    private Integer statusCode;

    // Extracted from user object
    private String userId;
    private String email;

    // Extracted from hospitalDetails
    private String facilityId;
    private String facilityName;
    private Long expiresIn;

    // Nested objects
    @SerializedName("hospitalDetails")
    private HospitalDetails hospitalDetails;

    @SerializedName("user")
    private User user;

    // Constructor
    public LoginResponse() {
    }

    // Nested class for hospital details
    public static class HospitalDetails {
        @SerializedName("id")
        private String id;
        @SerializedName("hospital_name")
        private String hospitalName;
        @SerializedName("city_name")
        private String cityName;
        @SerializedName("state_name")
        private String stateName;

        public String getId() { return id; }
        public String getHospitalName() { return hospitalName; }
        public String getCityName() { return cityName; }
        public String getStateName() { return stateName; }

        public Object getAddressLine1() {
            return null;
        }
    }

    // Nested class for user
    public static class User {
        @SerializedName("id")
        private String id;
        @SerializedName("username")
        private String username;
        @SerializedName("first_name")
        private String firstName;
        @SerializedName("last_name")
        private String lastName;
        @SerializedName("emails")
        private java.util.List<Email> emails;
        @SerializedName("roles")
        private java.util.List<Role> roles;

        public String getId() { return id; }
        public String getUsername() { return username; }
        public String getFirstName() { return firstName; }
        public String getLastName() { return lastName; }
        public java.util.List<Email> getEmails() { return emails; }
        public java.util.List<Role> getRoles() { return roles; }

        public static class Role {
            @SerializedName("id")
            private String id;
            @SerializedName("name")
            private String name;

            public String getId() { return id; }
            public String getName() { return name; }
        }

        public static class Email {
            @SerializedName("email")
            private String email;
            @SerializedName("is_primary")
            private Boolean isPrimary;

            public String getEmail() { return email; }
            public Boolean getIsPrimary() { return isPrimary; }
        }
    }

    // Getters and Setters
    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getRefreshToken() {
        return refreshToken;
    }

    public void setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }

    public String getUserId() {
        // Extract from user object if available
        if (user != null && user.getId() != null) {
            return user.getId();
        }
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getEmail() {
        // Extract primary email from user.emails if available
        if (user != null && user.getEmails() != null && !user.getEmails().isEmpty()) {
            for (User.Email email : user.getEmails()) {
                if (email.getIsPrimary() != null && email.getIsPrimary()) {
                    return email.getEmail();
                }
            }
            return user.getEmails().get(0).getEmail();
        }
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFacilityId() {
        // Extract from hospitalDetails if available
        if (hospitalDetails != null && hospitalDetails.getId() != null) {
            return hospitalDetails.getId();
        }
        return facilityId;
    }

    public void setFacilityId(String facilityId) {
        this.facilityId = facilityId;
    }

    public String getFacilityName() {
        // Extract from hospitalDetails if available
        if (hospitalDetails != null && hospitalDetails.getHospitalName() != null) {
            return hospitalDetails.getHospitalName();
        }
        return facilityName;
    }

    public void setFacilityName(String facilityName) {
        this.facilityName = facilityName;
    }

    public Long getExpiresIn() {
        // If expiresIn is not set, try to extract from JWT token
        if (expiresIn == null && token != null) {
            expiresIn = extractExpirationFromJWT(token);
        }
        return expiresIn;
    }

    public void setExpiresIn(Long expiresIn) {
        this.expiresIn = expiresIn;
    }

    /**
     * Extract expiration time from JWT token payload
     * JWT format: header.payload.signature
     */
    private Long extractExpirationFromJWT(String token) {
        try {
            String[] parts = token.split("\\.");
            if (parts.length == 3) {
                // Decode the payload (second part)
                String payload = parts[1];
                // Add padding if needed
                int padding = 4 - (payload.length() % 4);
                if (padding != 4) {
                    payload += "=".repeat(padding);
                }

                byte[] decodedBytes = java.util.Base64.getDecoder().decode(payload);
                String decodedPayload = new String(decodedBytes, java.nio.charset.StandardCharsets.UTF_8);

                // Parse JSON to extract exp field
                com.google.gson.JsonObject json = com.google.gson.JsonParser.parseString(decodedPayload).getAsJsonObject();
                if (json.has("exp")) {
                    return json.get("exp").getAsLong();
                }
            }
        } catch (Exception e) {
            // If extraction fails, return null
        }
        return null;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Integer getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(Integer statusCode) {
        this.statusCode = statusCode;
    }

    public Boolean getSuccess() {
        return success;
    }

    public void setSuccess(Boolean success) {
        this.success = success;
    }

    public HospitalDetails getHospitalDetails() {
        return hospitalDetails;
    }

    public void setHospitalDetails(HospitalDetails hospitalDetails) {
        this.hospitalDetails = hospitalDetails;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    @Override
    public String toString() {
        return "LoginResponse{" +
                "success=" + success +
                ", token='" + token + '\'' +
                ", refreshToken='" + refreshToken + '\'' +
                ", userId='" + getUserId() + '\'' +
                ", email='" + getEmail() + '\'' +
                ", facilityId='" + getFacilityId() + '\'' +
                ", facilityName='" + getFacilityName() + '\'' +
                ", expiresIn=" + expiresIn +
                ", message='" + message + '\'' +
                ", statusCode=" + statusCode +
                '}';
    }
}
