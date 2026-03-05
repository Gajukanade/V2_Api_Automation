package org.example.api;

import com.google.gson.Gson;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.example.models.LoginRequest;
import org.example.models.LoginResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * API client for Hospital Admin Login
 */
public class LoginApiClient {
    private static final Logger logger = LoggerFactory.getLogger(LoginApiClient.class);

    private static final String BASE_URL = "https://v2-services.chikitsa.dev/hospital-admin/api/v1";
    private static final String LOGIN_ENDPOINT = "/auth/login";

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
}

