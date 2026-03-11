package org.example.api;

import com.google.gson.Gson;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.example.models.LoginRequest;
import org.example.models.LoginResponse;
import org.example.models.LogoutRequest;
import org.example.models.LogoutResponse;
import org.example.models.ProfileResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * API client for Hospital Admin Login and Logout
 */
public class LoginApiClient {
    private static final Logger logger = LoggerFactory.getLogger(LoginApiClient.class);

    private static final String BASE_URL = "https://v2-services.chikitsa.dev/hospital-admin/api/v1";
    private static final String LOGIN_ENDPOINT = "/auth/login";
    private static final String LOGOUT_ENDPOINT = "/auth/logout";
    private static final String PROFILE_ENDPOINT = "/profile";

    private final Gson gson = new Gson();

    /**
     * Perform login request
     *
     * @param loginRequest Login request payload
     * @return LoginResponse object containing token and user details
     */
    public LoginResponse login(LoginRequest loginRequest) {
        logger.info("Initiating login request for email: {}", loginRequest.getEmail());

        try {
            String requestBody = gson.toJson(loginRequest);
            logger.debug("Request body: {}", requestBody);

            Response response = RestAssured.given()
                    .baseUri(BASE_URL)
                    .contentType(ContentType.JSON)
                    .body(requestBody)
                    .log().all()
                    .when()
                    .post(LOGIN_ENDPOINT)
                    .then()
                    .log().all()
                    .extract()
                    .response();

            logger.info("Response status code: {}", response.getStatusCode());

            if (response.getStatusCode() == 200 || response.getStatusCode() == 201) {
                LoginResponse loginResponse = gson.fromJson(response.getBody().asString(), LoginResponse.class);
                logger.info("Login successful for user: {}", loginResponse.getEmail());
                return loginResponse;
            } else {
                logger.error("Login failed with status code: {}", response.getStatusCode());
                logger.error("Response body: {}", response.getBody().asString());
                throw new RuntimeException("Login failed with status code: " + response.getStatusCode());
            }
        } catch (Exception e) {
            logger.error("Exception occurred during login", e);
            throw new RuntimeException("Login request failed: " + e.getMessage(), e);
        }
    }

    /**
     * Perform login request with explicit parameters
     *
     * @param email Email address
     * @param password Password
     * @param facilityId Facility ID
     * @param facilityName Facility Name
     * @param deviceId Device ID
     * @return LoginResponse object containing token and user details
     */
    public LoginResponse login(String email, String password, String facilityId, String facilityName, String deviceId) {
        LoginRequest loginRequest = new LoginRequest(email, password, facilityId, facilityName, deviceId);
        return login(loginRequest);
    }

    /**
     * Perform login request and return the raw Response object
     *
     * @param loginRequest Login request payload
     * @return Raw Response object from RestAssured
     */
    public Response loginRaw(LoginRequest loginRequest) {
        logger.info("Initiating raw login request for email: {}", loginRequest.getEmail());

        try {
            String requestBody = gson.toJson(loginRequest);

            return RestAssured.given()
                    .baseUri(BASE_URL)
                    .contentType(ContentType.JSON)
                    .body(requestBody)
                    .log().all()
                    .when()
                    .post(LOGIN_ENDPOINT)
                    .then()
                    .log().all()
                    .extract()
                    .response();
        } catch (Exception e) {
            logger.error("Exception occurred during raw login request", e);
            throw new RuntimeException("Login request failed: " + e.getMessage(), e);
        }
    }

    /**
     * Get the base URL
     *
     * @return Base URL string
     */
    public static String getBaseUrl() {
        return BASE_URL;
    }

    /**
     * Get the login endpoint
     *
     * @return Login endpoint string
     */
    public static String getLoginEndpoint() {
        return LOGIN_ENDPOINT;
    }

    /**
     * Perform logout request
     *
     * @param accessToken Access token from login response
     * @param logoutRequest Logout request payload
     * @return LogoutResponse object
     */
    public LogoutResponse logout(String accessToken, LogoutRequest logoutRequest) {
        logger.info("Initiating logout request for facility: {}", logoutRequest.getFacilityId());

        try {
            String requestBody = gson.toJson(logoutRequest);
            logger.debug("Logout request body: {}", requestBody);

            Response response = RestAssured.given()
                    .baseUri(BASE_URL)
                    .contentType(ContentType.JSON)
                    .header("Authorization", "Bearer " + accessToken)
                    .body(requestBody)
                    .log().all()
                    .when()
                    .post(LOGOUT_ENDPOINT)
                    .then()
                    .log().all()
                    .extract()
                    .response();

            logger.info("Logout response status code: {}", response.getStatusCode());

            if (response.getStatusCode() == 200 || response.getStatusCode() == 201) {
                LogoutResponse logoutResponse = gson.fromJson(response.getBody().asString(), LogoutResponse.class);
                logger.info("Logout successful: {}", logoutResponse.getMessage());
                return logoutResponse;
            } else {
                logger.error("Logout failed with status code: {}", response.getStatusCode());
                logger.error("Response body: {}", response.getBody().asString());
                throw new RuntimeException("Logout failed with status code: " + response.getStatusCode());
            }
        } catch (Exception e) {
            logger.error("Exception occurred during logout", e);
            throw new RuntimeException("Logout request failed: " + e.getMessage(), e);
        }
    }

    /**
     * Perform logout request with explicit parameters
     *
     * @param accessToken Access token from login response
     * @param facilityId Facility ID
     * @return LogoutResponse object
     */
    public LogoutResponse logout(String accessToken, String facilityId) {
        LogoutRequest logoutRequest = new LogoutRequest(facilityId);
        return logout(accessToken, logoutRequest);
    }

    /**
     * Perform logout request with facility ID, session ID, and user ID
     *
     * @param accessToken Access token from login response
     * @param facilityId Facility ID
     * @param sessionId Session ID
     * @param userId User ID
     * @return LogoutResponse object
     */
    public LogoutResponse logout(String accessToken, String facilityId, String sessionId, String userId) {
        LogoutRequest logoutRequest = new LogoutRequest(facilityId, sessionId, userId);
        return logout(accessToken, logoutRequest);
    }

    /**
     * Perform logout request and return the raw Response object
     *
     * @param accessToken Access token from login response
     * @param logoutRequest Logout request payload
     * @return Raw Response object from RestAssured
     */
    public Response logoutRaw(String accessToken, LogoutRequest logoutRequest) {
        logger.info("Initiating raw logout request for facility: {}", logoutRequest.getFacilityId());

        try {
            String requestBody = gson.toJson(logoutRequest);

            return RestAssured.given()
                    .baseUri(BASE_URL)
                    .contentType(ContentType.JSON)
                    .header("Authorization", "Bearer " + accessToken)
                    .body(requestBody)
                    .log().all()
                    .when()
                    .post(LOGOUT_ENDPOINT)
                    .then()
                    .log().all()
                    .extract()
                    .response();
        } catch (Exception e) {
            logger.error("Exception occurred during raw logout request", e);
            throw new RuntimeException("Logout request failed: " + e.getMessage(), e);
        }
    }

    /**
     * Get the logout endpoint
     *
     * @return Logout endpoint string
     */
    public static String getLogoutEndpoint() {
        return LOGOUT_ENDPOINT;
    }

    /**
     * Get user profile using access token
     *
     * @param accessToken Access token from login response
     * @return ProfileResponse object containing user profile details
     */
    public ProfileResponse getProfile(String accessToken) {
        logger.info("Initiating profile request with access token");

        try {
            Response response = RestAssured.given()
                    .baseUri(BASE_URL)
                    .contentType(ContentType.JSON)
                    .header("Authorization", "Bearer " + accessToken)
                    .log().all()
                    .when()
                    .get(PROFILE_ENDPOINT)
                    .then()
                    .log().all()
                    .extract()
                    .response();

            logger.info("Profile response status code: {}", response.getStatusCode());

            if (response.getStatusCode() == 200 || response.getStatusCode() == 201) {
                ProfileResponse profileResponse = gson.fromJson(response.getBody().asString(), ProfileResponse.class);
                logger.info("Profile retrieved successfully for user: {}", profileResponse.getData().getUser().getUsername());
                return profileResponse;
            } else {
                logger.error("Profile request failed with status code: {}", response.getStatusCode());
                logger.error("Response body: {}", response.getBody().asString());
                throw new RuntimeException("Profile request failed with status code: " + response.getStatusCode());
            }
        } catch (Exception e) {
            logger.error("Exception occurred during profile request", e);
            throw new RuntimeException("Profile request failed: " + e.getMessage(), e);
        }
    }

    /**
     * Get user profile and return raw Response object
     *
     * @param accessToken Access token from login response
     * @return Raw Response object from RestAssured
     */
    public Response getProfileRaw(String accessToken) {
        logger.info("Initiating raw profile request");

        try {
            return RestAssured.given()
                    .baseUri(BASE_URL)
                    .contentType(ContentType.JSON)
                    .header("Authorization", "Bearer " + accessToken)
                    .log().all()
                    .when()
                    .get(PROFILE_ENDPOINT)
                    .then()
                    .log().all()
                    .extract()
                    .response();
        } catch (Exception e) {
            logger.error("Exception occurred during raw profile request", e);
            throw new RuntimeException("Profile request failed: " + e.getMessage(), e);
        }
    }

    /**
     * Get the profile endpoint
     *
     * @return Profile endpoint string
     */
    public static String getProfileEndpoint() {
        return PROFILE_ENDPOINT;
    }
}

