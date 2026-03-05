package org.example;

import org.example.api.LoginApiClient;
import org.example.models.LoginRequest;
import org.example.models.LoginResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Example demonstrating how to use the Login API Client
 */
public class LoginApiExample {
    private static final Logger logger = LoggerFactory.getLogger(LoginApiExample.class);

    public static void main(String[] args) {
        logger.info("Starting Login API Example");

        // Initialize the API client
        LoginApiClient apiClient = new LoginApiClient();

        // Create login request with credentials
        LoginRequest loginRequest = new LoginRequest(
                "johndo",                                                    // email
                "Chikitsa@123",                                             // password
                "b60fae41-3a86-4d4f-8ca9-8180a7d44e0e",                    // facility_id
                "SMS Hospital",                                             // facility_name
                "8c094825-a89a-4b96-a4c7-7d6d06359dea"                     // deviceId
        );

        try {
            logger.info("Attempting login with email: {}", loginRequest.getEmail());

            // Perform login
            LoginResponse response = apiClient.login(loginRequest);

            // Print response details
            System.out.println("\n========== LOGIN SUCCESSFUL ==========");
            System.out.println("User ID: " + response.getUserId());
            System.out.println("Email: " + response.getEmail());
            System.out.println("Facility ID: " + response.getFacilityId());
            System.out.println("Facility Name: " + response.getFacilityName());
            System.out.println("Token: " + response.getToken());
            System.out.println("Refresh Token: " + response.getRefreshToken());
            System.out.println("Expires In: " + response.getExpiresIn() + " seconds");
            System.out.println("Message: " + response.getMessage());
            System.out.println("Status Code: " + response.getStatusCode());
            System.out.println("======================================\n");

            logger.info("Login completed successfully");

        } catch (Exception e) {
            System.out.println("\n========== LOGIN FAILED ==========");
            System.out.println("Error: " + e.getMessage());
            System.out.println("==================================\n");
            logger.error("Login failed", e);
        }
    }
}

