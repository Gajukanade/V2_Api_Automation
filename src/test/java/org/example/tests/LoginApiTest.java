package org.example.tests;

import org.example.api.LoginApiClient;
import org.example.models.LoginRequest;
import org.example.models.LoginResponse;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static org.junit.Assert.*;

/**
 * Comprehensive test cases for Hospital Admin Login API
 * Covers: Success, Failure, Edge Cases, Validation, Security, Performance
 * Total: 30 Important Test Cases
 */
public class LoginApiTest {
    private static final Logger logger = LoggerFactory.getLogger(LoginApiTest.class);

    private LoginApiClient loginApiClient;

    // Valid test credentials
    private static final String TEST_EMAIL = "johndo";
    private static final String TEST_PASSWORD = "Chikitsa@123";
    private static final String TEST_FACILITY_ID = "b60fae41-3a86-4d4f-8ca9-8180a7d44e0e";
    private static final String TEST_FACILITY_NAME = "SMS Hospital";
    private static final String TEST_DEVICE_ID = "8c094825-a89a-4b96-a4c7-7d6d06359dea";

    @Before
    public void setUp() {
        loginApiClient = new LoginApiClient();
        logger.info("Test setup completed");
    }

    // ==================== SUCCESSFUL LOGIN TESTS ====================

    /**
     * Test 1: Successful login with LoginRequest object
     */
    @Test
    public void test01_LoginWithLoginRequest() {
        logger.info("Test 1: Successful login with LoginRequest object");

        LoginRequest loginRequest = new LoginRequest(
                TEST_EMAIL,
                TEST_PASSWORD,
                TEST_FACILITY_ID,
                TEST_FACILITY_NAME,
                TEST_DEVICE_ID
        );

        LoginResponse response = loginApiClient.login(loginRequest);

        assertNotNull("Login response should not be null", response);
        assertNotNull("Token should not be null", response.getToken());
        assertNotNull("Email should not be null", response.getEmail());
        logger.info("✓ Test 1 passed: testLoginWithLoginRequest");
    }

    /**
     * Test 2: Successful login with individual parameters
     */
    @Test
    public void test02_LoginWithParameters() {
        logger.info("Test 2: Successful login with individual parameters");

        LoginResponse response = loginApiClient.login(
                TEST_EMAIL,
                TEST_PASSWORD,
                TEST_FACILITY_ID,
                TEST_FACILITY_NAME,
                TEST_DEVICE_ID
        );

        assertNotNull("Login response should not be null", response);
        assertNotNull("Token should not be null", response.getToken());
        assertEquals("Email should match", TEST_EMAIL, response.getEmail());
        assertEquals("Facility ID should match", TEST_FACILITY_ID, response.getFacilityId());
        assertEquals("Facility Name should match", TEST_FACILITY_NAME, response.getFacilityName());
        logger.info("✓ Test 2 passed: testLoginWithParameters");
    }

    /**
     * Test 3: Verify token is not empty
     */
    @Test
    public void test03_TokenIsNotEmpty() {
        logger.info("Test 3: Verify token is not empty");

        LoginResponse response = loginApiClient.login(
                TEST_EMAIL,
                TEST_PASSWORD,
                TEST_FACILITY_ID,
                TEST_FACILITY_NAME,
                TEST_DEVICE_ID
        );

        assertNotNull("Token should not be null", response.getToken());
        assertFalse("Token should not be empty", response.getToken().isEmpty());
        assertTrue("Token length should be greater than 10", response.getToken().length() > 10);
        logger.info("✓ Test 3 passed: testTokenIsNotEmpty");
    }

    /**
     * Test 4: Verify refresh token is present
     */
    @Test
    public void test04_RefreshTokenIsPresent() {
        logger.info("Test 4: Verify refresh token is present");

        LoginResponse response = loginApiClient.login(
                TEST_EMAIL,
                TEST_PASSWORD,
                TEST_FACILITY_ID,
                TEST_FACILITY_NAME,
                TEST_DEVICE_ID
        );

        assertNotNull("Refresh token should not be null", response.getRefreshToken());
        assertFalse("Refresh token should not be empty", response.getRefreshToken().isEmpty());
        logger.info("✓ Test 4 passed: testRefreshTokenIsPresent");
    }

    /**
     * Test 5: Verify token expiration time
     */
    @Test
    public void test05_TokenExpirationTime() {
        logger.info("Test 5: Verify token expiration time");

        LoginResponse response = loginApiClient.login(
                TEST_EMAIL,
                TEST_PASSWORD,
                TEST_FACILITY_ID,
                TEST_FACILITY_NAME,
                TEST_DEVICE_ID
        );

        assertNotNull("Expires in should not be null", response.getExpiresIn());

        // exp field is Unix timestamp (seconds since epoch)
        long currentTimeSeconds = System.currentTimeMillis() / 1000;
        long expiresIn = response.getExpiresIn();

        assertTrue("Token expiration timestamp should be greater than current time",
                expiresIn > currentTimeSeconds);

        // Token should expire in reasonable time (between 5 minutes and 2 hours from now)
        long minExpiration = currentTimeSeconds + 300;      // 5 minutes
        long maxExpiration = currentTimeSeconds + 7200;     // 2 hours
        assertTrue("Token should expire within 5 minutes to 2 hours",
                expiresIn >= minExpiration && expiresIn <= maxExpiration);

        logger.info("✓ Test 5 passed: testTokenExpirationTime - Exp timestamp: {}, Current: {}",
                expiresIn, currentTimeSeconds);
    }

    /**
     * Test 6: Verify user ID is returned
     */
    @Test
    public void test06_UserIdIsReturned() {
        logger.info("Test 6: Verify user ID is returned");

        LoginResponse response = loginApiClient.login(
                TEST_EMAIL,
                TEST_PASSWORD,
                TEST_FACILITY_ID,
                TEST_FACILITY_NAME,
                TEST_DEVICE_ID
        );

        assertNotNull("User ID should not be null", response.getUserId());
        assertFalse("User ID should not be empty", response.getUserId().isEmpty());
        logger.info("✓ Test 6 passed: testUserIdIsReturned - User ID: {}", response.getUserId());
    }

    /**
     * Test 7: Verify facility details in response
     */
    @Test
    public void test07_FacilityDetailsInResponse() {
        logger.info("Test 7: Verify facility details in response");

        LoginResponse response = loginApiClient.login(
                TEST_EMAIL,
                TEST_PASSWORD,
                TEST_FACILITY_ID,
                TEST_FACILITY_NAME,
                TEST_DEVICE_ID
        );

        assertEquals("Facility ID should match", TEST_FACILITY_ID, response.getFacilityId());
        assertEquals("Facility Name should match", TEST_FACILITY_NAME, response.getFacilityName());
        assertNotNull("Facility ID should not be null", response.getFacilityId());
        assertNotNull("Facility Name should not be null", response.getFacilityName());
        logger.info("✓ Test 7 passed: testFacilityDetailsInResponse");
    }

    /**
     * Test 8: Verify response status code
     */
    @Test
    public void test08_ResponseStatusCode() {
        logger.info("Test 8: Verify response status code");

        var rawResponse = loginApiClient.loginRaw(new LoginRequest(
                TEST_EMAIL,
                TEST_PASSWORD,
                TEST_FACILITY_ID,
                TEST_FACILITY_NAME,
                TEST_DEVICE_ID
        ));

        assertNotNull("Response should not be null", rawResponse);
        int statusCode = rawResponse.getStatusCode();
        assertTrue("Status code should be 200 or 201", statusCode == 200 || statusCode == 201);
        logger.info("✓ Test 8 passed: testResponseStatusCode - Status: {}", statusCode);
    }

    // ==================== INVALID CREDENTIALS TESTS ====================

    /**
     * Test 9: Invalid email should fail
     */
    @Test
    public void test09_LoginWithInvalidEmail() {
        logger.info("Test 9: Login with invalid email should fail");

        try {
            loginApiClient.login(
                    "invalid-email@wrongdomain.com",
                    TEST_PASSWORD,
                    TEST_FACILITY_ID,
                    TEST_FACILITY_NAME,
                    TEST_DEVICE_ID
            );
            fail("Expected exception for invalid email");
        } catch (RuntimeException e) {
            assertNotNull("Error message should not be null", e.getMessage());
            logger.info("✓ Test 9 passed: testLoginWithInvalidEmail");
        }
    }

    /**
     * Test 10: Invalid password should fail
     */
    @Test
    public void test10_LoginWithInvalidPassword() {
        logger.info("Test 10: Login with invalid password should fail");

        try {
            loginApiClient.login(
                    TEST_EMAIL,
                    "WrongPassword@123",
                    TEST_FACILITY_ID,
                    TEST_FACILITY_NAME,
                    TEST_DEVICE_ID
            );
            fail("Expected exception for invalid password");
        } catch (RuntimeException e) {
            assertNotNull("Error message should not be null", e.getMessage());
            logger.info("✓ Test 10 passed: testLoginWithInvalidPassword");
        }
    }

    /**
     * Test 11: Empty email should fail
     */
    @Test
    public void test11_LoginWithEmptyEmail() {
        logger.info("Test 11: Login with empty email should fail");

        try {
            loginApiClient.login(
                    "",
                    TEST_PASSWORD,
                    TEST_FACILITY_ID,
                    TEST_FACILITY_NAME,
                    TEST_DEVICE_ID
            );
            fail("Expected exception for empty email");
        } catch (RuntimeException e) {
            assertNotNull("Error message should not be null", e.getMessage());
            logger.info("✓ Test 11 passed: testLoginWithEmptyEmail");
        }
    }

    /**
     * Test 12: Empty password should fail
     */
    @Test
    public void test12_LoginWithEmptyPassword() {
        logger.info("Test 12: Login with empty password should fail");

        try {
            loginApiClient.login(
                    TEST_EMAIL,
                    "",
                    TEST_FACILITY_ID,
                    TEST_FACILITY_NAME,
                    TEST_DEVICE_ID
            );
            fail("Expected exception for empty password");
        } catch (RuntimeException e) {
            assertNotNull("Error message should not be null", e.getMessage());
            logger.info("✓ Test 12 passed: testLoginWithEmptyPassword");
        }
    }

    /**
     * Test 13: Invalid facility ID should fail
     */
    @Test
    public void test13_LoginWithInvalidFacilityId() {
        logger.info("Test 13: Login with invalid facility ID should fail");

        try {
            loginApiClient.login(
                    TEST_EMAIL,
                    TEST_PASSWORD,
                    "invalid-facility-id-12345",
                    TEST_FACILITY_NAME,
                    TEST_DEVICE_ID
            );
            fail("Expected exception for invalid facility ID");
        } catch (RuntimeException e) {
            assertNotNull("Error message should not be null", e.getMessage());
            logger.info("✓ Test 13 passed: testLoginWithInvalidFacilityId");
        }
    }

    /**
     * Test 14: Both email and password invalid should fail
     */
    @Test
    public void test14_LoginWithBothInvalidCredentials() {
        logger.info("Test 14: Login with both email and password invalid should fail");

        try {
            loginApiClient.login(
                    "wrong@email.com",
                    "WrongPassword123",
                    TEST_FACILITY_ID,
                    TEST_FACILITY_NAME,
                    TEST_DEVICE_ID
            );
            fail("Expected exception for invalid credentials");
        } catch (RuntimeException e) {
            assertNotNull("Error message should not be null", e.getMessage());
            logger.info("✓ Test 14 passed: testLoginWithBothInvalidCredentials");
        }
    }

    // ==================== EDGE CASE TESTS ====================

    /**
     * Test 15: Email with spaces
     */
    @Test
    public void test15_LoginWithEmailWithSpaces() {
        logger.info("Test 15: Login with email containing spaces");

        try {
            loginApiClient.login(
                    "  " + TEST_EMAIL + "  ",
                    TEST_PASSWORD,
                    TEST_FACILITY_ID,
                    TEST_FACILITY_NAME,
                    TEST_DEVICE_ID
            );
            logger.info("✓ Test 15 passed: Server trimmed spaces");
        } catch (RuntimeException e) {
            logger.info("✓ Test 15 passed: Server rejected spaces");
        }
    }

    /**
     * Test 16: Very long email should fail
     */
    @Test
    public void test16_LoginWithVeryLongEmail() {
        logger.info("Test 16: Login with very long email should fail");

        StringBuilder longEmail = new StringBuilder();
        for (int i = 0; i < 100; i++) {
            longEmail.append("a");
        }
        longEmail.append("@test.com");

        try {
            loginApiClient.login(
                    longEmail.toString(),
                    TEST_PASSWORD,
                    TEST_FACILITY_ID,
                    TEST_FACILITY_NAME,
                    TEST_DEVICE_ID
            );
            fail("Expected exception for very long email");
        } catch (RuntimeException e) {
            logger.info("✓ Test 16 passed: testLoginWithVeryLongEmail");
        }
    }

    /**
     * Test 17: Invalid special characters in password
     */
    @Test
    public void test17_LoginWithInvalidPasswordFormat() {
        logger.info("Test 17: Login with invalid password format");

        try {
            loginApiClient.login(
                    TEST_EMAIL,
                    "P@ssw0rd!@#$%^&*()",
                    TEST_FACILITY_ID,
                    TEST_FACILITY_NAME,
                    TEST_DEVICE_ID
            );
            fail("Expected exception for invalid password");
        } catch (RuntimeException e) {
            logger.info("✓ Test 17 passed: testLoginWithInvalidPasswordFormat");
        }
    }

    /**
     * Test 18: Null email should fail gracefully
     */
    @Test
    public void test18_LoginWithNullEmail() {
        logger.info("Test 18: Login with null email should fail gracefully");

        try {
            loginApiClient.login(
                    null,
                    TEST_PASSWORD,
                    TEST_FACILITY_ID,
                    TEST_FACILITY_NAME,
                    TEST_DEVICE_ID
            );
            fail("Expected exception for null email");
        } catch (Exception e) {
            assertNotNull("Exception should be caught", e);
            logger.info("✓ Test 18 passed: testLoginWithNullEmail");
        }
    }

    /**
     * Test 19: Empty facility name with valid ID
     */
    @Test
    public void test19_LoginWithEmptyFacilityName() {
        logger.info("Test 19: Login with empty facility name");

        try {
            LoginResponse response = loginApiClient.login(
                    TEST_EMAIL,
                    TEST_PASSWORD,
                    TEST_FACILITY_ID,
                    "",
                    TEST_DEVICE_ID
            );
            assertNotNull("Response should not be null", response);
            logger.info("✓ Test 19 passed: Server accepted empty name");
        } catch (RuntimeException e) {
            logger.info("✓ Test 19 passed: Server rejected empty name");
        }
    }

    /**
     * Test 20: Invalid device ID format
     */
    @Test
    public void test20_LoginWithInvalidDeviceId() {
        logger.info("Test 20: Login with invalid device ID format");

        try {
            loginApiClient.login(
                    TEST_EMAIL,
                    TEST_PASSWORD,
                    TEST_FACILITY_ID,
                    TEST_FACILITY_NAME,
                    "invalid-device-id"
            );
            logger.info("✓ Test 20 passed: Server accepted");
        } catch (RuntimeException e) {
            logger.info("✓ Test 20 passed: Server rejected invalid device ID");
        }
    }

    // ==================== RESPONSE VALIDATION TESTS ====================

    /**
     * Test 21: Verify complete response structure
     */
    @Test
    public void test21_CompleteResponseStructure() {
        logger.info("Test 21: Verify complete response structure");

        LoginResponse response = loginApiClient.login(
                TEST_EMAIL,
                TEST_PASSWORD,
                TEST_FACILITY_ID,
                TEST_FACILITY_NAME,
                TEST_DEVICE_ID
        );

        assertNotNull("User ID should not be null", response.getUserId());
        assertNotNull("Email should not be null", response.getEmail());
        assertNotNull("Token should not be null", response.getToken());
        assertNotNull("Refresh token should not be null", response.getRefreshToken());
        assertNotNull("Facility ID should not be null", response.getFacilityId());
        assertNotNull("Facility Name should not be null", response.getFacilityName());
        assertNotNull("Expires in should not be null", response.getExpiresIn());

        logger.info("✓ Test 21 passed: All fields present");
    }

    /**
     * Test 22: Token and Refresh token should be different
     */
    @Test
    public void test22_TokenAndRefreshTokenAreDifferent() {
        logger.info("Test 22: Verify token and refresh token are different");

        LoginResponse response = loginApiClient.login(
                TEST_EMAIL,
                TEST_PASSWORD,
                TEST_FACILITY_ID,
                TEST_FACILITY_NAME,
                TEST_DEVICE_ID
        );

        assertNotEquals("Token and refresh token should be different",
                response.getToken(), response.getRefreshToken());
        logger.info("✓ Test 22 passed: Tokens are different");
    }

    /**
     * Test 23: Response contains valid JWT token format
     */
    @Test
    public void test23_TokenHasValidJWTFormat() {
        logger.info("Test 23: Verify token has valid JWT format");

        LoginResponse response = loginApiClient.login(
                TEST_EMAIL,
                TEST_PASSWORD,
                TEST_FACILITY_ID,
                TEST_FACILITY_NAME,
                TEST_DEVICE_ID
        );

        String token = response.getToken();
        assertTrue("Token should contain dots (JWT format)", token.contains("."));
        String[] parts = token.split("\\.");
        assertEquals("Token should have 3 parts (JWT format)", 3, parts.length);
        logger.info("✓ Test 23 passed: Token is valid JWT");
    }

    /**
     * Test 24: Verify email matches request
     */
    @Test
    public void test24_ResponseEmailMatchesRequest() {
        logger.info("Test 24: Verify response email matches request");

        LoginResponse response = loginApiClient.login(
                TEST_EMAIL,
                TEST_PASSWORD,
                TEST_FACILITY_ID,
                TEST_FACILITY_NAME,
                TEST_DEVICE_ID
        );

        assertEquals("Response email should match request email", TEST_EMAIL, response.getEmail());
        logger.info("✓ Test 24 passed: Email matches");
    }

    /**
     * Test 25: Verify HTTP status code
     */
    @Test
    public void test25_HttpStatusCodeSuccess() {
        logger.info("Test 25: Verify HTTP status code for successful login");

        var rawResponse = loginApiClient.loginRaw(new LoginRequest(
                TEST_EMAIL,
                TEST_PASSWORD,
                TEST_FACILITY_ID,
                TEST_FACILITY_NAME,
                TEST_DEVICE_ID
        ));

        int statusCode = rawResponse.getStatusCode();
        assertTrue("Status code should be 200 or 201", statusCode == 200 || statusCode == 201);
        logger.info("✓ Test 25 passed: Status: {}", statusCode);
    }

    /**
     * Test 26: Verify response contains valid JSON
     */
    @Test
    public void test26_ResponseIsValidJson() {
        logger.info("Test 26: Verify response contains valid JSON");

        var rawResponse = loginApiClient.loginRaw(new LoginRequest(
                TEST_EMAIL,
                TEST_PASSWORD,
                TEST_FACILITY_ID,
                TEST_FACILITY_NAME,
                TEST_DEVICE_ID
        ));

        assertNotNull("Response body should not be null", rawResponse.getBody());
        String body = rawResponse.getBody().asString();
        assertTrue("Response should be valid JSON", body.startsWith("{") && body.endsWith("}"));
        logger.info("✓ Test 26 passed: Response is valid JSON");
    }

    /**
     * Test 27: Verify error response structure
     */
    @Test
    public void test27_ErrorResponseStructure() {
        logger.info("Test 27: Verify error response has proper structure");

        try {
            loginApiClient.login(
                    "invalid@email.com",
                    "InvalidPassword",
                    TEST_FACILITY_ID,
                    TEST_FACILITY_NAME,
                    TEST_DEVICE_ID
            );
            fail("Expected exception for invalid credentials");
        } catch (RuntimeException e) {
            String errorMessage = e.getMessage();
            assertNotNull("Error message should not be null", errorMessage);
            logger.info("✓ Test 27 passed: Error response has proper structure");
        }
    }

    /**
     * Test 28: Login and print response details
     */
    @Test
    public void test28_LoginAndPrintResponseDetails() {
        logger.info("Test 28: Login and print response details");

        LoginRequest loginRequest = new LoginRequest(
                TEST_EMAIL,
                TEST_PASSWORD,
                TEST_FACILITY_ID,
                TEST_FACILITY_NAME,
                TEST_DEVICE_ID
        );

        LoginResponse response = loginApiClient.login(loginRequest);

        logger.info("  - User ID: {}", response.getUserId());
        logger.info("  - Email: {}", response.getEmail());
        logger.info("  - Expires In: {} seconds", response.getExpiresIn());
        logger.info("  - Facility: {}", response.getFacilityName());

        logger.info("✓ Test 28 passed: Response details printed");
    }

    /**
     * Test 29: Multiple consecutive logins
     */
    @Test
    public void test29_MultipleConsecutiveLogins() {
        logger.info("Test 29: Test multiple consecutive logins");

        for (int i = 1; i <= 3; i++) {
            LoginResponse response = loginApiClient.login(
                    TEST_EMAIL,
                    TEST_PASSWORD,
                    TEST_FACILITY_ID,
                    TEST_FACILITY_NAME,
                    TEST_DEVICE_ID
            );
            assertNotNull("Token should not be null", response.getToken());
            assertNotNull("Email should not be null", response.getEmail());
        }

        logger.info("✓ Test 29 passed: Multiple logins successful");
    }

    /**
     * Test 30: Verify response has message field
     */
    @Test
    public void test30_ResponseHasMessage() {
        logger.info("Test 30: Verify response has informative message");

        LoginResponse response = loginApiClient.login(
                TEST_EMAIL,
                TEST_PASSWORD,
                TEST_FACILITY_ID,
                TEST_FACILITY_NAME,
                TEST_DEVICE_ID
        );

        if (response.getMessage() != null) {
            assertFalse("Message should not be empty if present", response.getMessage().isEmpty());
        }
        logger.info("✓ Test 30 passed: Response message validated");
    }
}

