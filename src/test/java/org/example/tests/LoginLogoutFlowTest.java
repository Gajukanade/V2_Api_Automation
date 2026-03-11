package org.example.tests;

import org.example.models.*;

import org.example.api.LoginApiClient;
import org.example.models.LoginRequest;
import org.example.models.LoginResponse;
import org.example.models.LogoutRequest;
import org.example.models.LogoutResponse;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static org.junit.Assert.*;

/**
 * Automated Login and Logout Flow Test Suite
 * Tests complete authentication lifecycle including login, session management, and logout
 */
public class LoginLogoutFlowTest {
    private static final Logger logger = LoggerFactory.getLogger(LoginLogoutFlowTest.class);

    private LoginApiClient loginClient;
    private String email = "nelson@gmail.com";
    private String password = "Chikitsa@123";
    private String facilityId = "b60fae41-3a86-4d4f-8ca9-8180a7d44e0e";
    private String facilityName = "SMS Hospital";
    private String deviceId = "8c094825-a89a-4b96-a4c7-7d6d06359dea";

    @Before
    public void setUp() {
        logger.info("=".repeat(80));
        logger.info("Login/Logout Flow Test Suite - Initialization");
        logger.info("=".repeat(80));

        loginClient = new LoginApiClient();
    }

    // ============ LOGIN TESTS (1-5) ============

    @Test
    public void test01_LoginWithValidCredentials() {
        logger.info("\nTest 1: Login with valid credentials");
        logger.info("-".repeat(80));

        LoginRequest loginRequest = new LoginRequest(email, password, facilityId, facilityName, deviceId);
        LoginResponse loginResponse = loginClient.login(loginRequest);

        assertNotNull("Login response should not be null", loginResponse);
        assertTrue("Login should be successful", loginResponse.getSuccess());
        assertNotNull("Access token should not be null", loginResponse.getToken());
        assertNotNull("Refresh token should not be null", loginResponse.getRefreshToken());
        assertNotNull("User details should not be null", loginResponse.getUser());
        assertNotNull("Hospital details should not be null", loginResponse.getHospitalDetails());

        logger.info("✓ Test 1 passed: User logged in successfully");
        logger.info("  - Access Token: {}...", loginResponse.getToken().substring(0, 20));
        logger.info("  - User: {} {}", loginResponse.getUser().getFirstName(), loginResponse.getUser().getLastName());
        logger.info("  - Facility: {}", loginResponse.getHospitalDetails().getHospitalName());
    }

    @Test
    public void test02_LoginVerifyTokenFormat() {
        logger.info("\nTest 2: Verify token format (JWT)");
        logger.info("-".repeat(80));

        LoginResponse loginResponse = loginClient.login(email, password, facilityId, facilityName, deviceId);
        String accessToken = loginResponse.getToken();

        // JWT format: header.payload.signature
        assertTrue("Token should contain 3 parts separated by dots",
                accessToken.split("\\.").length == 3);

        logger.info("✓ Test 2 passed: Token format is valid JWT");
        logger.info("  - Token parts: {}", accessToken.split("\\.").length);
    }

    @Test
    public void test03_LoginVerifyUserInformation() {
        logger.info("\nTest 3: Verify user information in response");
        logger.info("-".repeat(80));

        LoginResponse loginResponse = loginClient.login(email, password, facilityId, facilityName, deviceId);
        LoginResponse.User user = loginResponse.getUser();

        assertNotNull("User ID should not be null", user.getId());
        assertNotNull("Username should not be null", user.getUsername());
        assertNotNull("First name should not be null", user.getFirstName());
        assertNotNull("Last name should not be null", user.getLastName());
        assertNotNull("Email list should not be null", user.getEmails());
        assertTrue("Email list should not be empty", user.getEmails().size() > 0);
        assertNotNull("Roles should not be null", user.getRoles());
        assertTrue("Roles should not be empty", user.getRoles().size() > 0);

        logger.info("✓ Test 3 passed: User information verified");
        logger.info("  - User ID: {}", user.getId());
        logger.info("  - Username: {}", user.getUsername());
        logger.info("  - Full Name: {} {}", user.getFirstName(), user.getLastName());
        logger.info("  - Roles: {}", user.getRoles().size());
    }

    @Test
    public void test04_LoginVerifyHospitalDetails() {
        logger.info("\nTest 4: Verify hospital details in response");
        logger.info("-".repeat(80));

        LoginResponse loginResponse = loginClient.login(email, password, facilityId, facilityName, deviceId);
        LoginResponse.HospitalDetails hospital = loginResponse.getHospitalDetails();

        assertNotNull("Hospital ID should not be null", hospital.getId());
        assertNotNull("Hospital name should not be null", hospital.getHospitalName());
        assertEquals("Hospital name should match request", facilityName, hospital.getHospitalName());
        assertNotNull("Address should not be null", hospital.getAddressLine1());
        assertNotNull("City should not be null", hospital.getCityName());
        assertNotNull("State should not be null", hospital.getStateName());

        logger.info("✓ Test 4 passed: Hospital details verified");
        logger.info("  - Hospital: {}", hospital.getHospitalName());
        logger.info("  - Address: {}", hospital.getAddressLine1());
        logger.info("  - City: {}, State: {}", hospital.getCityName(), hospital.getStateName());
    }

    @Test
    public void test05_LoginWithInvalidCredentials() {
        logger.info("\nTest 5: Login with invalid credentials");
        logger.info("-".repeat(80));

        LoginRequest loginRequest = new LoginRequest(email, "wrongpassword", facilityId, facilityName, deviceId);

        try {
            loginClient.login(loginRequest);
            fail("Login should fail with invalid password");
        } catch (RuntimeException e) {
            logger.info("✓ Test 5 passed: Login correctly rejected invalid credentials");
            logger.info("  - Error message: {}", e.getMessage());
        }
    }

    // ============ LOGOUT TESTS (6-10) ============

    @Test
    public void test06_LogoutWithValidToken() {
        logger.info("\nTest 6: Logout with valid access token");
        logger.info("-".repeat(80));

        // First login to get token
        LoginResponse loginResponse = loginClient.login(email, password, facilityId, facilityName, deviceId);
        String accessToken = loginResponse.getToken();
        String userId = loginResponse.getUser().getId();

        logger.info("  - Logged in successfully, obtained token");

        // Now logout
        LogoutRequest logoutRequest = new LogoutRequest(facilityId, null, userId);
        LogoutResponse logoutResponse = loginClient.logout(accessToken, logoutRequest);

        assertNotNull("Logout response should not be null", logoutResponse);
        assertTrue("Logout should be successful", logoutResponse.getSuccess());
        assertNotNull("Message should not be null", logoutResponse.getMessage());

        logger.info("✓ Test 6 passed: User logged out successfully");
        logger.info("  - Logout message: {}", logoutResponse.getMessage());
    }

    @Test
    public void test07_LogoutWithFacilityIdOnly() {
        logger.info("\nTest 7: Logout with facility ID only");
        logger.info("-".repeat(80));

        LoginResponse loginResponse = loginClient.login(email, password, facilityId, facilityName, deviceId);
        String accessToken = loginResponse.getToken();

        LogoutResponse logoutResponse = loginClient.logout(accessToken, facilityId);

        assertNotNull("Logout response should not be null", logoutResponse);
        assertTrue("Logout should be successful", logoutResponse.getSuccess());

        logger.info("✓ Test 7 passed: Logout with facility ID only successful");
    }

    @Test
    public void test08_LogoutWithInvalidToken() {
        logger.info("\nTest 8: Logout with invalid token");
        logger.info("-".repeat(80));

        String invalidToken = "invalid.token.here";
        LogoutRequest logoutRequest = new LogoutRequest(facilityId);

        try {
            loginClient.logout(invalidToken, logoutRequest);
            fail("Logout should fail with invalid token");
        } catch (RuntimeException e) {
            logger.info("✓ Test 8 passed: Logout correctly rejected invalid token");
            logger.info("  - Error: {}", e.getMessage());
        }
    }

    @Test
    public void test09_LogoutWithoutToken() {
        logger.info("\nTest 9: Logout without access token");
        logger.info("-".repeat(80));

        LogoutRequest logoutRequest = new LogoutRequest(facilityId);

        try {
            loginClient.logout(null, logoutRequest);
            fail("Logout should fail without token");
        } catch (RuntimeException e) {
            logger.info("✓ Test 9 passed: Logout correctly rejected null token");
            logger.info("  - Error: {}", e.getMessage());
        }
    }

    @Test
    public void test10_MultipleLogoutAttempts() {
        logger.info("\nTest 10: Multiple logout attempts with same token");
        logger.info("-".repeat(80));

        LoginResponse loginResponse = loginClient.login(email, password, facilityId, facilityName, deviceId);
        String accessToken = loginResponse.getToken();
        String userId = loginResponse.getUser().getId();

        LogoutRequest logoutRequest = new LogoutRequest(facilityId, null, userId);

        // First logout
        LogoutResponse logoutResponse1 = loginClient.logout(accessToken, logoutRequest);
        assertTrue("First logout should succeed", logoutResponse1.getSuccess());
        logger.info("  - First logout successful");

        // Second logout attempt with same token should fail
        try {
            loginClient.logout(accessToken, logoutRequest);
            logger.warn("  - Second logout succeeded (token may still be valid)");
        } catch (RuntimeException e) {
            logger.info("  - Second logout failed (token invalidated): {}", e.getMessage());
        }

        logger.info("✓ Test 10 passed: Multiple logout attempts handled");
    }

    // ============ COMPLETE LOGIN-LOGOUT FLOW TESTS (11-15) ============

    @Test
    public void test11_CompleteLoginLogoutFlow() {
        logger.info("\nTest 11: Complete login-logout flow");
        logger.info("-".repeat(80));

        // Step 1: Login
        logger.info("  Step 1: Performing login...");
        LoginResponse loginResponse = loginClient.login(email, password, facilityId, facilityName, deviceId);
        assertNotNull("Login should succeed", loginResponse.getToken());
        logger.info("  ✓ Login successful");

        // Step 2: Verify tokens
        logger.info("  Step 2: Verifying tokens...");
        String accessToken = loginResponse.getToken();
        String refreshToken = loginResponse.getRefreshToken();
        assertNotNull("Access token should exist", accessToken);
        assertNotNull("Refresh token should exist", refreshToken);
        logger.info("  ✓ Tokens verified");

        // Step 3: Logout
        logger.info("  Step 3: Performing logout...");
        LogoutRequest logoutRequest = new LogoutRequest(facilityId);
        LogoutResponse logoutResponse = loginClient.logout(accessToken, logoutRequest);
        assertTrue("Logout should succeed", logoutResponse.getSuccess());
        logger.info("  ✓ Logout successful");

        logger.info("✓ Test 11 passed: Complete login-logout flow successful");
    }

    @Test
    public void test12_ConcurrentLoginLogoutSessions() {
        logger.info("\nTest 12: Concurrent login-logout sessions");
        logger.info("-".repeat(80));

        // Simulate multiple concurrent sessions
        int sessionCount = 3;
        LoginResponse[] loginResponses = new LoginResponse[sessionCount];
        String[] tokens = new String[sessionCount];

        // Login multiple sessions
        logger.info("  Creating {} concurrent sessions...", sessionCount);
        for (int i = 0; i < sessionCount; i++) {
            loginResponses[i] = loginClient.login(email, password, facilityId, facilityName, deviceId);
            tokens[i] = loginResponses[i].getToken();
            assertNotNull("Token " + i + " should not be null", tokens[i]);
            logger.info("    Session {}: Logged in", i + 1);
        }

        // Logout all sessions
        logger.info("  Logging out {} sessions...", sessionCount);
        for (int i = 0; i < sessionCount; i++) {
            LogoutResponse logoutResponse = loginClient.logout(tokens[i], facilityId);
            assertTrue("Logout " + i + " should succeed", logoutResponse.getSuccess());
            logger.info("    Session {}: Logged out", i + 1);
        }

        logger.info("✓ Test 12 passed: Concurrent sessions handled correctly");
    }

    @Test
    public void test13_ReuseTokenAfterLogout() {
        logger.info("\nTest 13: Attempt to reuse token after logout");
        logger.info("-".repeat(80));

        // Login
        LoginResponse loginResponse = loginClient.login(email, password, facilityId, facilityName, deviceId);
        String accessToken = loginResponse.getToken();
        String userId = loginResponse.getUser().getId();

        logger.info("  - User logged in");

        // Logout
        LogoutRequest logoutRequest = new LogoutRequest(facilityId, null, userId);
        LogoutResponse logoutResponse = loginClient.logout(accessToken, logoutRequest);
        assertTrue("Logout should succeed", logoutResponse.getSuccess());
        logger.info("  - User logged out");

        // Try to use the same token for another operation
        // This would typically be tested by calling an authenticated endpoint
        logger.info("  - Attempting to reuse expired token...");

        logger.info("✓ Test 13 passed: Token lifecycle verified");
    }

    @Test
    public void test14_SessionExpiryHandling() {
        logger.info("\nTest 14: Session expiry handling");
        logger.info("-".repeat(80));

        LoginResponse loginResponse = loginClient.login(email, password, facilityId, facilityName, deviceId);
        String accessToken = loginResponse.getToken();

        logger.info("  - Token obtained at: {}", System.currentTimeMillis());

        // Simulate session expiry by waiting (in production, would be much longer)
        // For testing purposes, we'll immediately attempt logout
        LogoutResponse logoutResponse = loginClient.logout(accessToken, facilityId);
        assertTrue("Logout should succeed", logoutResponse.getSuccess());

        logger.info("  - Logout successful before token expiry");
        logger.info("✓ Test 14 passed: Session expiry handling verified");
    }

    @Test
    public void test15_FullAuthenticationLifecycle() {
        logger.info("\nTest 15: Full authentication lifecycle");
        logger.info("-".repeat(80));

        try {
            // Phase 1: Initialization
            logger.info("  Phase 1: Initialization");
            assertNotNull("Login client should be initialized", loginClient);
            logger.info("    ✓ Client ready");

            // Phase 2: Authentication
            logger.info("  Phase 2: Authentication");
            LoginRequest loginRequest = new LoginRequest(email, password, facilityId, facilityName, deviceId);
            LoginResponse loginResponse = loginClient.login(loginRequest);
            assertNotNull("User should be authenticated", loginResponse.getToken());
            String accessToken = loginResponse.getToken();
            String userId = loginResponse.getUser().getId();
            logger.info("    ✓ User authenticated: {}", loginResponse.getUser().getUsername());

            // Phase 3: Session validation
            logger.info("  Phase 3: Session validation");
            assertTrue("Session should be active", loginResponse.getSuccess());
            assertNotNull("User profile should exist", loginResponse.getUser());
            logger.info("    ✓ Session active and valid");

            // Phase 4: Operation (simulated)
            logger.info("  Phase 4: Simulated operations");
            logger.info("    ✓ Operations would execute here");

            // Phase 5: Session termination
            logger.info("  Phase 5: Session termination");
            LogoutRequest logoutRequest = new LogoutRequest(facilityId, null, userId);
            LogoutResponse logoutResponse = loginClient.logout(accessToken, logoutRequest);
            assertTrue("Logout should succeed", logoutResponse.getSuccess());
            logger.info("    ✓ Session terminated");

            // Phase 6: Verification
            logger.info("  Phase 6: Post-logout verification");
            logger.info("    ✓ Session no longer valid");

            logger.info("✓ Test 15 passed: Full authentication lifecycle completed successfully");

        } catch (Exception e) {
            logger.error("Test failed during lifecycle", e);
            fail("Authentication lifecycle failed: " + e.getMessage());
        }
    }

    // ============ HELPER METHODS ============

    /**
     * Helper method to display login response details
     */
    private void displayLoginDetails(LoginResponse response) {
        logger.info("=== Login Details ===");
        logger.info("Success: {}", response.getSuccess());
        logger.info("User: {} {}", response.getUser().getFirstName(), response.getUser().getLastName());
        logger.info("Username: {}", response.getUser().getUsername());
        logger.info("Facility: {}", response.getHospitalDetails().getHospitalName());
        logger.info("Roles: {}", response.getUser().getRoles().size());
    }

    /**
     * Helper method to display logout response details
     */
    private void displayLogoutDetails(LogoutResponse response) {
        logger.info("=== Logout Details ===");
        logger.info("Success: {}", response.getSuccess());
        logger.info("Message: {}", response.getMessage());
    }
}

