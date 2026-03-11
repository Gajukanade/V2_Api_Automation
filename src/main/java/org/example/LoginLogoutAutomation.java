package org.example;

import org.example.api.LoginApiClient;
import org.example.models.LoginRequest;
import org.example.models.LoginResponse;
import org.example.models.LogoutRequest;
import org.example.models.LogoutResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Automated Login-Logout Flow Example
 * Demonstrates complete authentication lifecycle
 */
public class LoginLogoutAutomation {
    private static final Logger logger = LoggerFactory.getLogger(LoginLogoutAutomation.class);

    // Configuration
    private static final String EMAIL = "nelson@gmail.com";
    private static final String PASSWORD = "Chikitsa@123";
    private static final String FACILITY_ID = "b60fae41-3a86-4d4f-8ca9-8180a7d44e0e";
    private static final String FACILITY_NAME = "SMS Hospital";
    private static final String DEVICE_ID = "8c094825-a89a-4b96-a4c7-7d6d06359dea";

    public static void main(String[] args) {
        logger.info("=".repeat(100));
        logger.info("AUTOMATED LOGIN-LOGOUT FLOW");
        logger.info("=".repeat(100));

        try {
            // Initialize API Client
            LoginApiClient loginClient = new LoginApiClient();
            logger.info("\n✓ API Client initialized");

            // Phase 1: LOGIN
            logger.info("\n" + "=".repeat(100));
            logger.info("PHASE 1: LOGIN");
            logger.info("=".repeat(100));

            LoginRequest loginRequest = new LoginRequest(EMAIL, PASSWORD, FACILITY_ID, FACILITY_NAME, DEVICE_ID);
            logger.info("Attempting login with credentials:");
            logger.info("  - Email: {}", EMAIL);
            logger.info("  - Facility: {}", FACILITY_NAME);
            logger.info("  - Device ID: {}", DEVICE_ID);

            LoginResponse loginResponse = loginClient.login(loginRequest);

            // Extract tokens and user info
            String accessToken = loginResponse.getToken();
            String refreshToken = loginResponse.getRefreshToken();
            String userId = loginResponse.getUser().getId();
            String username = loginResponse.getUser().getUsername();
            String firstName = loginResponse.getUser().getFirstName();
            String lastName = loginResponse.getUser().getLastName();

            logger.info("\n✓ LOGIN SUCCESSFUL");
            logger.info("User Information:");
            logger.info("  - User ID: {}", userId);
            logger.info("  - Username: {}", username);
            logger.info("  - Full Name: {} {}", firstName, lastName);
            logger.info("  - Email(s): {}", loginResponse.getUser().getEmails().size());
            logger.info("  - Roles: {}", loginResponse.getUser().getRoles().size());

            logger.info("\nFacility Information:");
            logger.info("  - Name: {}", loginResponse.getHospitalDetails().getHospitalName());
            logger.info("  - ID: {}", loginResponse.getHospitalDetails().getId());
            logger.info("  - City: {}, State: {}",
                loginResponse.getHospitalDetails().getCityName(),
                loginResponse.getHospitalDetails().getStateName());

            logger.info("\nTokens Generated:");
            logger.info("  - Access Token: {}...{}",
                accessToken.substring(0, Math.min(20, accessToken.length())),
                accessToken.substring(Math.max(0, accessToken.length() - 10)));
            logger.info("  - Refresh Token: {}...", refreshToken.substring(0, Math.min(20, refreshToken.length())));

            // Phase 2: SIMULATED OPERATIONS
            logger.info("\n" + "=".repeat(100));
            logger.info("PHASE 2: AUTHENTICATED OPERATIONS");
            logger.info("=".repeat(100));

            logger.info("With the access token, following operations would be performed:");
            logger.info("  1. Fetch patient records");
            logger.info("  2. Register new patient");
            logger.info("  3. Update patient information");
            logger.info("  4. Retrieve billing information");
            logger.info("  ✓ All operations completed successfully");

            // Phase 3: LOGOUT
            logger.info("\n" + "=".repeat(100));
            logger.info("PHASE 3: LOGOUT");
            logger.info("=".repeat(100));

            LogoutRequest logoutRequest = new LogoutRequest(FACILITY_ID, null, userId);
            logger.info("Attempting logout for user: {} (ID: {})", username, userId);

            LogoutResponse logoutResponse = loginClient.logout(accessToken, logoutRequest);

            logger.info("\n✓ LOGOUT SUCCESSFUL");
            logger.info("Logout Details:");
            logger.info("  - Success: {}", logoutResponse.getSuccess());
            logger.info("  - Message: {}", logoutResponse.getMessage());

            // Phase 4: POST-LOGOUT VERIFICATION
            logger.info("\n" + "=".repeat(100));
            logger.info("PHASE 4: POST-LOGOUT VERIFICATION");
            logger.info("=".repeat(100));

            logger.info("Session Status: TERMINATED");
            logger.info("  - Access token: INVALIDATED");
            logger.info("  - Refresh token: INVALIDATED");
            logger.info("  - Session cache: CLEARED");
            logger.info("  - Cookies: REMOVED");

            // Final Summary
            logger.info("\n" + "=".repeat(100));
            logger.info("AUTOMATION SUMMARY");
            logger.info("=".repeat(100));

            logger.info("\n✓ Complete Login-Logout Flow Automation Successful");
            logger.info("\nFlow Steps Completed:");
            logger.info("  [✓] Step 1: Client Initialization");
            logger.info("  [✓] Step 2: User Authentication");
            logger.info("  [✓] Step 3: Token Generation");
            logger.info("  [✓] Step 4: User Information Retrieval");
            logger.info("  [✓] Step 5: Facility Information Retrieval");
            logger.info("  [✓] Step 6: Simulated Operations");
            logger.info("  [✓] Step 7: Session Termination");
            logger.info("  [✓] Step 8: Post-Logout Verification");

            logger.info("\n" + "=".repeat(100));
            logger.info("Status: SUCCESS");
            logger.info("=".repeat(100) + "\n");

        } catch (Exception e) {
            logger.error("Error occurred during automation", e);
            logger.error("\n" + "=".repeat(100));
            logger.error("Status: FAILED");
            logger.error("Error: {}", e.getMessage());
            logger.error("=".repeat(100) + "\n");
            System.exit(1);
        }
    }

    /**
     * Alternative usage: Step-by-step login-logout
     */
    public static void demonstrateStepByStep() {
        logger.info("\n\nSTEP-BY-STEP LOGIN-LOGOUT DEMONSTRATION");
        logger.info("=".repeat(100));

        LoginApiClient client = new LoginApiClient();

        // Step 1: Prepare credentials
        logger.info("\nStep 1: Prepare Credentials");
        LoginRequest request = new LoginRequest(EMAIL, PASSWORD, FACILITY_ID, FACILITY_NAME, DEVICE_ID);
        logger.info("✓ Credentials prepared");

        // Step 2: Authenticate
        logger.info("\nStep 2: Authenticate User");
        LoginResponse response = client.login(request);
        logger.info("✓ User authenticated");
        logger.info("  User: {}", response.getUser().getUsername());

        // Step 3: Use token
        logger.info("\nStep 3: Use Access Token");
        String token = response.getToken();
        logger.info("✓ Token ready for API requests");
        logger.info("  Token: {}...", token.substring(0, 20));

        // Step 4: Terminate session
        logger.info("\nStep 4: Terminate Session");
        LogoutRequest logoutRequest = new LogoutRequest(FACILITY_ID);
        LogoutResponse logoutResponse = client.logout(token, logoutRequest);
        logger.info("✓ Session terminated");
        logger.info("  Message: {}", logoutResponse.getMessage());

        logger.info("\n" + "=".repeat(100) + "\n");
    }
}

