package org.example.tests;

import org.example.api.LoginApiClient;
import org.example.models.LoginResponse;
import org.example.models.ProfileResponse;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static org.junit.Assert.*;

/**
 * Profile API Test Suite
 * Tests user profile retrieval using access token
 */
public class ProfileApiTest {
    private static final Logger logger = LoggerFactory.getLogger(ProfileApiTest.class);

    private LoginApiClient apiClient;
    private String email = "nelson@gmail.com";
    private String password = "Chikitsa@123";
    private String facilityId = "b60fae41-3a86-4d4f-8ca9-8180a7d44e0e";
    private String facilityName = "SMS Hospital";
    private String deviceId = "8c094825-a89a-4b96-a4c7-7d6d06359dea";

    @Before
    public void setUp() {
        logger.info("=".repeat(80));
        logger.info("Profile API Test Suite - Initialization");
        logger.info("=".repeat(80));

        apiClient = new LoginApiClient();
    }

    @Test
    public void test01_GetProfileWithValidToken() {
        logger.info("\nTest 1: Get profile with valid access token");
        logger.info("-".repeat(80));

        // Step 1: Login to get access token
        logger.info("  Step 1: Logging in...");
        LoginResponse loginResponse = apiClient.login(email, password, facilityId, facilityName, deviceId);
        assertNotNull("Login should succeed", loginResponse.getToken());
        String accessToken = loginResponse.getToken();
        logger.info("  ✓ Login successful");

        // Step 2: Get profile
        logger.info("  Step 2: Getting profile...");
        ProfileResponse profileResponse = apiClient.getProfile(accessToken);
        assertNotNull("Profile response should not be null", profileResponse);
        assertTrue("Profile response should be successful", profileResponse.getSuccess());
        assertNotNull("Profile data should not be null", profileResponse.getData());
        assertNotNull("User data should not be null", profileResponse.getData().getUser());
        logger.info("  ✓ Profile retrieved successfully");

        // Step 3: Verify user information
        logger.info("  Step 3: Verifying user information...");
        ProfileResponse.User user = profileResponse.getData().getUser();
        assertNotNull("User ID should not be null", user.getId());
        assertNotNull("Username should not be null", user.getUsername());
        assertNotNull("First name should not be null", user.getFirstName());
        assertNotNull("Last name should not be null", user.getLastName());
        assertNotNull("Email should not be null", user.getEmail());
        assertNotNull("Emails list should not be null", user.getEmails());
        assertTrue("Emails list should not be empty", user.getEmails().size() > 0);
        assertNotNull("Roles should not be null", user.getRoles());
        assertTrue("Roles should not be empty", user.getRoles().size() > 0);
        logger.info("  ✓ User information verified");

        logger.info("✓ Test 1 passed: Profile retrieved successfully");
        logger.info("  - User ID: {}", user.getId());
        logger.info("  - Username: {}", user.getUsername());
        logger.info("  - Full Name: {} {}", user.getFirstName(), user.getLastName());
        logger.info("  - Email: {}", user.getEmail());
        logger.info("  - Total Emails: {}", user.getEmails().size());
        logger.info("  - Total Roles: {}", user.getRoles().size());
    }

    @Test
    public void test02_VerifyProfileEmailsStructure() {
        logger.info("\nTest 2: Verify profile emails structure");
        logger.info("-".repeat(80));

        // Login
        LoginResponse loginResponse = apiClient.login(email, password, facilityId, facilityName, deviceId);
        String accessToken = loginResponse.getToken();

        // Get profile
        ProfileResponse profileResponse = apiClient.getProfile(accessToken);
        ProfileResponse.User user = profileResponse.getData().getUser();

        // Verify emails
        logger.info("  Verifying emails structure:");
        for (int i = 0; i < user.getEmails().size(); i++) {
            ProfileResponse.Email email = user.getEmails().get(i);
            assertNotNull("Email should have email address", email.getEmail());
            assertNotNull("Email should have isPrimary flag", email.getIsPrimary());
            logger.info("    Email {}: {} (Primary: {})", i + 1, email.getEmail(), email.getIsPrimary());
        }

        logger.info("✓ Test 2 passed: Emails structure verified");
    }

    @Test
    public void test03_VerifyProfileRolesStructure() {
        logger.info("\nTest 3: Verify profile roles structure");
        logger.info("-".repeat(80));

        // Login
        LoginResponse loginResponse = apiClient.login(email, password, facilityId, facilityName, deviceId);
        String accessToken = loginResponse.getToken();

        // Get profile
        ProfileResponse profileResponse = apiClient.getProfile(accessToken);
        ProfileResponse.User user = profileResponse.getData().getUser();

        // Verify roles
        logger.info("  Verifying roles structure:");
        for (int i = 0; i < user.getRoles().size(); i++) {
            ProfileResponse.Role role = user.getRoles().get(i);
            assertNotNull("Role should have ID", role.getId());
            assertNotNull("Role should have name", role.getName());
            logger.info("    Role {}: {} (ID: {})", i + 1, role.getName(), role.getId());
        }

        logger.info("✓ Test 3 passed: Roles structure verified");
    }

    @Test
    public void test04_VerifyProfileTimestamps() {
        logger.info("\nTest 4: Verify profile timestamps");
        logger.info("-".repeat(80));

        // Login
        LoginResponse loginResponse = apiClient.login(email, password, facilityId, facilityName, deviceId);
        String accessToken = loginResponse.getToken();

        // Get profile
        ProfileResponse profileResponse = apiClient.getProfile(accessToken);
        ProfileResponse.User user = profileResponse.getData().getUser();

        // Verify timestamps
        assertNotNull("Created timestamp should not be null", user.getCreatedAt());
        assertNotNull("Updated timestamp should not be null", user.getUpdatedAt());

        logger.info("  - Created At: {}", user.getCreatedAt());
        logger.info("  - Updated At: {}", user.getUpdatedAt());

        logger.info("✓ Test 4 passed: Profile timestamps verified");
    }

    @Test
    public void test05_VerifyProfileActiveStatus() {
        logger.info("\nTest 5: Verify profile active status");
        logger.info("-".repeat(80));

        // Login
        LoginResponse loginResponse = apiClient.login(email, password, facilityId, facilityName, deviceId);
        String accessToken = loginResponse.getToken();

        // Get profile
        ProfileResponse profileResponse = apiClient.getProfile(accessToken);
        ProfileResponse.User user = profileResponse.getData().getUser();

        // Verify active status
        assertNotNull("Is active flag should not be null", user.getIsActive());
        assertTrue("User should be active", user.getIsActive());

        logger.info("  - Is Active: {}", user.getIsActive());

        logger.info("✓ Test 5 passed: User active status verified");
    }

    @Test
    public void test06_GetProfileWithInvalidToken() {
        logger.info("\nTest 6: Get profile with invalid token");
        logger.info("-".repeat(80));

        String invalidToken = "invalid.token.here";

        try {
            apiClient.getProfile(invalidToken);
            fail("Profile request should fail with invalid token");
        } catch (RuntimeException e) {
            logger.info("✓ Test 6 passed: Profile request correctly rejected invalid token");
            logger.info("  - Error: {}", e.getMessage());
        }
    }

    @Test
    public void test07_GetProfileWithNullToken() {
        logger.info("\nTest 7: Get profile with null token");
        logger.info("-".repeat(80));

        try {
            apiClient.getProfile(null);
            fail("Profile request should fail with null token");
        } catch (Exception e) {
            logger.info("✓ Test 7 passed: Profile request correctly rejected null token");
            logger.info("  - Error: {}", e.getMessage());
        }
    }

    @Test
    public void test08_ProfileDataConsistency() {
        logger.info("\nTest 8: Verify profile data consistency");
        logger.info("-".repeat(80));

        // Login
        LoginResponse loginResponse = apiClient.login(email, password, facilityId, facilityName, deviceId);
        String accessToken = loginResponse.getToken();

        // Get profile
        ProfileResponse profileResponse = apiClient.getProfile(accessToken);
        ProfileResponse.User profileUser = profileResponse.getData().getUser();

        // Compare with login response
        LoginResponse.User loginUser = loginResponse.getUser();
        assertEquals("User ID should match", loginUser.getId(), profileUser.getId());
        assertEquals("Username should match", loginUser.getUsername(), profileUser.getUsername());
        assertEquals("First name should match", loginUser.getFirstName(), profileUser.getFirstName());
        assertEquals("Last name should match", loginUser.getLastName(), profileUser.getLastName());

        logger.info("  - User ID consistency: ✓");
        logger.info("  - Username consistency: ✓");
        logger.info("  - First name consistency: ✓");
        logger.info("  - Last name consistency: ✓");

        logger.info("✓ Test 8 passed: Profile data consistent with login response");
    }

    @Test
    public void test09_ProfileEndpointAvailability() {
        logger.info("\nTest 9: Verify profile endpoint availability");
        logger.info("-".repeat(80));

        String profileEndpoint = LoginApiClient.getProfileEndpoint();
        assertNotNull("Profile endpoint should not be null", profileEndpoint);
        assertEquals("Profile endpoint should be '/profile'", "/profile", profileEndpoint);

        logger.info("  - Profile Endpoint: {}", profileEndpoint);

        logger.info("✓ Test 9 passed: Profile endpoint available");
    }

    @Test
    public void test10_GetProfileMultipleTimes() {
        logger.info("\nTest 10: Get profile multiple times with same token");
        logger.info("-".repeat(80));

        // Login
        LoginResponse loginResponse = apiClient.login(email, password, facilityId, facilityName, deviceId);
        String accessToken = loginResponse.getToken();

        // Get profile multiple times
        int iterations = 3;
        for (int i = 0; i < iterations; i++) {
            logger.info("  Attempt {}: Getting profile...", i + 1);
            ProfileResponse profileResponse = apiClient.getProfile(accessToken);
            assertNotNull("Profile should be retrieved", profileResponse);
            assertTrue("Response should be successful", profileResponse.getSuccess());
            logger.info("    ✓ Profile retrieved");
        }

        logger.info("✓ Test 10 passed: Profile retrieved successfully {} times", iterations);
    }
}

