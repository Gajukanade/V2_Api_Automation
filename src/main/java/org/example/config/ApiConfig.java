package org.example.config;

/**
 * Configuration class for API endpoints and properties
 */
public class ApiConfig {
    // API Base Configuration
    public static final String BASE_URL = "https://v2-services.chikitsa.dev/hospital-admin/api/v1";
    public static final String LOGIN_ENDPOINT = "/auth/login";

    // Test Credentials (for reference only - use environment variables for sensitive data)
    public static final String TEST_EMAIL = "johndo";
    public static final String TEST_PASSWORD = "Chikitsa@123";
    public static final String TEST_FACILITY_ID = "b60fae41-3a86-4d4f-8ca9-8180a7d44e0e";
    public static final String TEST_FACILITY_NAME = "SMS Hospital";
    public static final String TEST_DEVICE_ID = "8c094825-a89a-4b96-a4c7-7d6d06359dea";

    // API Configuration
    public static final String CONTENT_TYPE = "application/json";
    public static final int TIMEOUT_SECONDS = 30;
    public static final int CONNECT_TIMEOUT_SECONDS = 10;

    // Response Status Codes
    public static final int STATUS_OK = 200;
    public static final int STATUS_CREATED = 201;
    public static final int STATUS_BAD_REQUEST = 400;
    public static final int STATUS_UNAUTHORIZED = 401;
    public static final int STATUS_FORBIDDEN = 403;
    public static final int STATUS_NOT_FOUND = 404;
    public static final int STATUS_INTERNAL_SERVER_ERROR = 500;

    /**
     * Get the full login URL
     */
    public static String getLoginUrl() {
        return BASE_URL + LOGIN_ENDPOINT;
    }

    /**
     * Get email from environment or use test email
     */
    public static String getEmail() {
        String email = System.getenv("TEST_EMAIL");
        return email != null ? email : TEST_EMAIL;
    }

    /**
     * Get password from environment or use test password
     */
    public static String getPassword() {
        String password = System.getenv("TEST_PASSWORD");
        return password != null ? password : TEST_PASSWORD;
    }

    /**
     * Get facility ID from environment or use test facility ID
     */
    public static String getFacilityId() {
        String facilityId = System.getenv("TEST_FACILITY_ID");
        return facilityId != null ? facilityId : TEST_FACILITY_ID;
    }

    /**
     * Get facility name from environment or use test facility name
     */
    public static String getFacilityName() {
        String facilityName = System.getenv("TEST_FACILITY_NAME");
        return facilityName != null ? facilityName : TEST_FACILITY_NAME;
    }

    /**
     * Get device ID from environment or use test device ID
     */
    public static String getDeviceId() {
        String deviceId = System.getenv("TEST_DEVICE_ID");
        return deviceId != null ? deviceId : TEST_DEVICE_ID;
    }

    /**
     * Get base URL from environment or use default
     */
    public static String getBaseUrl() {
        String baseUrl = System.getenv("API_BASE_URL");
        return baseUrl != null ? baseUrl : BASE_URL;
    }
}

