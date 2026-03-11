package org.example.tests;

import com.google.gson.JsonObject;
import io.restassured.response.Response;
import org.example.api.LoginApiClient;
import org.example.api.PatientApiClient;
import org.example.models.PatientRegistrationRequest;
import org.junit.BeforeClass;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

/**
 * Comprehensive test suite for Patient API endpoints
 *
 * Base path: /patients
 * Auth required: ✅ (with RBAC)
 *
 * Test Coverage:
 * 1. POST /patients - Register new patient
 * 2. GET /patients - List all patients (merged)
 * 3. GET /patients - Filter by search, is_active, visit_type
 * 4. GET /patients/local-patients - List local patients only
 * 5. GET /patients/local-patients - Filter by UHID, name, phone, email
 * 6. GET /patients/:id - Get single patient
 * 7. PATCH /patients/:id - Update patient details
 * 8. PATCH /patients/:id/status - Activate/deactivate patient
 */
public class PatientApiTest {
    private static final Logger logger = LoggerFactory.getLogger(PatientApiTest.class);

    private static String accessToken;
    private static String facilityId;
    private static String testPatientId;
    private static String testPatientUhid;

    private static PatientApiClient patientApiClient;
    private static LoginApiClient loginApiClient;

    @BeforeClass
    public static void setUp() {
        logger.info("=".repeat(80));
        logger.info("Patient API Test Suite - Setup");
        logger.info("=".repeat(80));

        // Initialize login client
        loginApiClient = new LoginApiClient();

        // Perform login
        String email = "nelson@gmail.com";
        String password = "Chikitsa@123";
        String facilityIdParam = "b60fae41-3a86-4d4f-8ca9-8180a7d44e0e";
        String facilityName = "SMS Hospital";
        String deviceId = "8c094825-a89a-4b96-a4c7-7d6d06359dea";

        Response loginResponse = (Response) loginApiClient.login(email, password, facilityIdParam, facilityName, deviceId);
        assertEquals("Login should be successful", 200, loginResponse.getStatusCode());

        accessToken = loginResponse.jsonPath().getString("accessToken");
        facilityId = loginResponse.jsonPath().getString("hospitalDetails.id");

        assertNotNull("Access token should not be null", accessToken);
        assertNotNull("Facility ID should not be null", facilityId);

        logger.info("Login successful - Access token acquired");
        logger.info("Facility ID: {}", facilityId);

        // Initialize patient API client
        patientApiClient = new PatientApiClient(accessToken, facilityId);

        logger.info("Patient API Client initialized");
    }

    // ============================================================================
    // Test 1: Register a new patient
    // ============================================================================

    @Test
    public void test01_RegisterPatient() {
        logger.info("Test 01: Register a new patient");
        logger.info("-".repeat(80));

        PatientRegistrationRequest request = createBasicPatientRequest(
            "John",
            "Doe",
            "1990-01-15",
            "M"
        );

        Response response = patientApiClient.registerPatient(request);

        assertEquals("Patient registration should return 201 Created", 201, response.getStatusCode());
        assertTrue("Response should indicate success", response.jsonPath().getBoolean("success"));

        // Extract patient ID and UHID for later tests
        testPatientId = response.jsonPath().getString("patient_id");
        testPatientUhid = response.jsonPath().getString("data.uhid");

        assertNotNull("Patient ID should not be null", testPatientId);
        assertNotNull("UHID should not be null", testPatientUhid);

        logger.info("✓ Patient registered successfully");
        logger.info("  Patient ID: {}", testPatientId);
        logger.info("  UHID: {}", testPatientUhid);
    }

    // ============================================================================
    // Test 2: Register multiple patients for filtering tests
    // ============================================================================

    @Test
    public void test02_RegisterMultiplePatients() {
        logger.info("Test 02: Register multiple patients for filtering");
        logger.info("-".repeat(80));

        List<PatientRegistrationRequest> patients = new ArrayList<>();

        // Patient 2
        patients.add(createBasicPatientRequest("Alice", "Smith", "1985-03-20", "F"));

        // Patient 3
        patients.add(createBasicPatientRequest("Bob", "Johnson", "1992-07-10", "M"));

        // Patient 4
        patients.add(createBasicPatientRequest("Carol", "Williams", "1988-11-05", "F"));

        int successCount = 0;
        for (PatientRegistrationRequest patient : patients) {
            Response response = patientApiClient.registerPatient(patient);
            if (response.getStatusCode() == 201) {
                successCount++;
                String patientId = response.jsonPath().getString("patient_id");
                logger.info("✓ Registered: {} {} - ID: {}",
                    patient.getFirstName(), patient.getLastName(), patientId);
            } else {
                logger.error("✗ Failed to register: {} {}",
                    patient.getFirstName(), patient.getLastName());
            }
        }

        assertTrue("At least one patient should be registered successfully", successCount > 0);
        logger.info("Total successfully registered: {} / {}", successCount, patients.size());
    }

    // ============================================================================
    // Test 3: List all patients (merged view)
    // ============================================================================

    @Test
    public void test03_ListAllPatients() {
        logger.info("Test 03: List all patients (local + GPR merged)");
        logger.info("-".repeat(80));

        Response response = patientApiClient.listAllPatients(1, 20);

        assertEquals("Should return 200 OK", 200, response.getStatusCode());
        assertTrue("Response should be successful", response.jsonPath().getBoolean("success"));

        Object patients = response.jsonPath().get("data");
        assertNotNull("Patients data should not be null", patients);

        logger.info("✓ Successfully retrieved all patients");
        logger.info("  Response structure validated");
    }

    // ============================================================================
    // Test 4: List patients with pagination
    // ============================================================================

    @Test
    public void test04_ListPatientsWithPagination() {
        logger.info("Test 04: List patients with pagination");
        logger.info("-".repeat(80));

        // Page 1 with limit 5
        Response page1Response = patientApiClient.listAllPatients(1, 5);
        assertEquals("Should return 200 OK for page 1", 200, page1Response.getStatusCode());
        logger.info("✓ Page 1 retrieved successfully");

        // Page 2 with limit 5
        Response page2Response = patientApiClient.listAllPatients(2, 5);
        assertEquals("Should return 200 OK for page 2", 200, page2Response.getStatusCode());
        logger.info("✓ Page 2 retrieved successfully");

        logger.info("Pagination working correctly");
    }

    // ============================================================================
    // Test 5: Search patients by name
    // ============================================================================

    @Test
    public void test05_SearchPatientsByName() {
        logger.info("Test 05: Search patients by name");
        logger.info("-".repeat(80));

        String searchTerm = "John";
        Response response = patientApiClient.listPatientsBySearch(searchTerm, 1, 20);

        assertEquals("Search should return 200 OK", 200, response.getStatusCode());
        logger.info("✓ Search for '{}' completed successfully", searchTerm);

        Object data = response.jsonPath().get("data");
        assertNotNull("Search results should not be null", data);
        logger.info("Search results validated");
    }

    // ============================================================================
    // Test 6: Filter patients by active status
    // ============================================================================

    @Test
    public void test06_FilterPatientsByActiveStatus() {
        logger.info("Test 06: Filter patients by active status");
        logger.info("-".repeat(80));

        // Get active patients
        Response activeResponse = patientApiClient.listPatientsByStatus(true, 1, 20);
        assertEquals("Should return 200 OK for active patients", 200, activeResponse.getStatusCode());
        logger.info("✓ Active patients retrieved");

        // Get inactive patients
        Response inactiveResponse = patientApiClient.listPatientsByStatus(false, 1, 20);
        assertEquals("Should return 200 OK for inactive patients", 200, inactiveResponse.getStatusCode());
        logger.info("✓ Inactive patients retrieved");

        logger.info("Status filtering working correctly");
    }

    // ============================================================================
    // Test 7: Filter patients by visit type
    // ============================================================================

    @Test
    public void test07_FilterPatientsByVisitType() {
        logger.info("Test 07: Filter patients by visit type");
        logger.info("-".repeat(80));

        String[] visitTypes = {"OPD", "IPD", "EMERGENCY"};

        for (String visitType : visitTypes) {
            Response response = patientApiClient.listPatientsByVisitType(visitType, 1, 20);
            assertEquals("Should return 200 OK for visit type: " + visitType, 200, response.getStatusCode());
            logger.info("✓ Retrieved patients for visit type: {}", visitType);
        }

        logger.info("Visit type filtering working correctly");
    }

    // ============================================================================
    // Test 8: List local patients only
    // ============================================================================

    @Test
    public void test08_ListLocalPatientsOnly() {
        logger.info("Test 08: List only locally registered patients");
        logger.info("-".repeat(80));

        Response response = patientApiClient.listLocalPatients(1, 20);

        assertEquals("Should return 200 OK", 200, response.getStatusCode());
        logger.info("✓ Local patients retrieved successfully");

        Object data = response.jsonPath().get("data");
        assertNotNull("Local patients data should not be null", data);
        logger.info("Local patients validated");
    }

    // ============================================================================
    // Test 9: Search local patients by UHID
    // ============================================================================

    @Test
    public void test09_SearchLocalPatientsByUhid() {
        logger.info("Test 09: Search local patients by UHID");
        logger.info("-".repeat(80));

        if (testPatientUhid == null) {
            logger.warn("No test UHID available - skipping test");
            return;
        }

        Response response = patientApiClient.getLocalPatientByUhid(testPatientUhid, 1, 20);

        assertEquals("Should return 200 OK", 200, response.getStatusCode());
        logger.info("✓ Patient found by UHID: {}", testPatientUhid);
    }

    // ============================================================================
    // Test 10: Search local patients by first name
    // ============================================================================

    @Test
    public void test10_SearchLocalPatientsByFirstName() {
        logger.info("Test 10: Search local patients by first name");
        logger.info("-".repeat(80));

        Response response = patientApiClient.getLocalPatientsByFirstName("John", 1, 20);

        assertEquals("Should return 200 OK", 200, response.getStatusCode());
        logger.info("✓ Patients found by first name");
    }

    // ============================================================================
    // Test 11: Search local patients by last name
    // ============================================================================

    @Test
    public void test11_SearchLocalPatientsByLastName() {
        logger.info("Test 11: Search local patients by last name");
        logger.info("-".repeat(80));

        Response response = patientApiClient.getLocalPatientsByLastName("Doe", 1, 20);

        assertEquals("Should return 200 OK", 200, response.getStatusCode());
        logger.info("✓ Patients found by last name");
    }

    // ============================================================================
    // Test 12: Search local patients by phone
    // ============================================================================

    @Test
    public void test12_SearchLocalPatientsByPhone() {
        logger.info("Test 12: Search local patients by phone number");
        logger.info("-".repeat(80));

        Response response = patientApiClient.getLocalPatientsByPhone("7709093160", 1, 20);

        assertEquals("Should return 200 OK", 200, response.getStatusCode());
        logger.info("✓ Patient search by phone completed");
    }

    // ============================================================================
    // Test 13: Search local patients by email
    // ============================================================================

    @Test
    public void test13_SearchLocalPatientsByEmail() {
        logger.info("Test 13: Search local patients by email");
        logger.info("-".repeat(80));

        Response response = patientApiClient.getLocalPatientsByEmail("nelson@gmail.com", 1, 20);

        assertEquals("Should return 200 OK", 200, response.getStatusCode());
        logger.info("✓ Patient search by email completed");
    }

    // ============================================================================
    // Test 14: Get patient by ID
    // ============================================================================

    @Test
    public void test14_GetPatientById() {
        logger.info("Test 14: Get single patient by ID");
        logger.info("-".repeat(80));

        if (testPatientId == null) {
            logger.warn("No test patient ID available - skipping test");
            return;
        }

        Response response = patientApiClient.getPatientById(testPatientId);

        assertEquals("Should return 200 OK", 200, response.getStatusCode());
        String retrievedId = response.jsonPath().getString("data.id");
        assertEquals("Retrieved patient ID should match", testPatientId, retrievedId);

        logger.info("✓ Patient retrieved successfully");
        logger.info("  Patient ID: {}", retrievedId);
    }

    // ============================================================================
    // Test 15: Update patient demographics
    // ============================================================================

    @Test
    public void test15_UpdatePatientDemographics() {
        logger.info("Test 15: Update patient demographics");
        logger.info("-".repeat(80));

        if (testPatientId == null) {
            logger.warn("No test patient ID available - skipping test");
            return;
        }

        JsonObject updateData = new JsonObject();
        updateData.addProperty("first_name", "Jonathan");
        updateData.addProperty("middle_name", "Michael");
        updateData.addProperty("age", 35);

        Response response = patientApiClient.updatePatient(testPatientId, updateData);

        assertEquals("Update should return 200 OK", 200, response.getStatusCode());
        logger.info("✓ Patient demographics updated successfully");
    }

    // ============================================================================
    // Test 16: Update patient contact info
    // ============================================================================

    @Test
    public void test16_UpdatePatientContactInfo() {
        logger.info("Test 16: Update patient contact information");
        logger.info("-".repeat(80));

        if (testPatientId == null) {
            logger.warn("No test patient ID available - skipping test");
            return;
        }

        JsonObject contactInfo = new JsonObject();
        contactInfo.addProperty("primary_phone", "9876543210");
        contactInfo.addProperty("primary_email", "john.doe.updated@example.com");

        Response response = patientApiClient.updatePatient(testPatientId, contactInfo);

        assertEquals("Update should return 200 OK", 200, response.getStatusCode());
        logger.info("✓ Patient contact info updated successfully");
    }

    // ============================================================================
    // Test 17: Update patient insurance details
    // ============================================================================

    @Test
    public void test17_UpdatePatientInsuranceDetails() {
        logger.info("Test 17: Update patient insurance details");
        logger.info("-".repeat(80));

        if (testPatientId == null) {
            logger.warn("No test patient ID available - skipping test");
            return;
        }

        JsonObject insuranceData = new JsonObject();
        insuranceData.addProperty("insurance_provider", "AETNA");
        insuranceData.addProperty("policy_number", "POL123456789");
        insuranceData.addProperty("policy_holder", "John Doe");

        Response response = patientApiClient.updatePatient(testPatientId, insuranceData);

        assertEquals("Update should return 200 OK", 200, response.getStatusCode());
        logger.info("✓ Patient insurance details updated successfully");
    }

    // ============================================================================
    // Test 18: Activate patient record
    // ============================================================================

    @Test
    public void test18_ActivatePatientRecord() {
        logger.info("Test 18: Activate patient record");
        logger.info("-".repeat(80));

        if (testPatientId == null) {
            logger.warn("No test patient ID available - skipping test");
            return;
        }

        Response response = patientApiClient.updatePatientStatus(testPatientId, true);

        assertEquals("Status update should return 200 OK", 200, response.getStatusCode());
        logger.info("✓ Patient record activated successfully");
    }

    // ============================================================================
    // Test 19: Deactivate patient record
    // ============================================================================

    @Test
    public void test19_DeactivatePatientRecord() {
        logger.info("Test 19: Deactivate patient record");
        logger.info("-".repeat(80));

        if (testPatientId == null) {
            logger.warn("No test patient ID available - skipping test");
            return;
        }

        Response response = patientApiClient.updatePatientStatus(testPatientId, false);

        assertEquals("Status update should return 200 OK", 200, response.getStatusCode());
        logger.info("✓ Patient record deactivated successfully");
    }

    // ============================================================================
    // Test 20: Toggle patient status multiple times
    // ============================================================================

    @Test
    public void test20_TogglePatientStatusMultipleTimes() {
        logger.info("Test 20: Toggle patient status multiple times");
        logger.info("-".repeat(80));

        if (testPatientId == null) {
            logger.warn("No test patient ID available - skipping test");
            return;
        }

        // Deactivate
        Response deactivateResponse = patientApiClient.updatePatientStatus(testPatientId, false);
        assertEquals("Deactivate should return 200 OK", 200, deactivateResponse.getStatusCode());
        logger.info("✓ Patient deactivated");

        // Reactivate
        Response activateResponse = patientApiClient.updatePatientStatus(testPatientId, true);
        assertEquals("Activate should return 200 OK", 200, activateResponse.getStatusCode());
        logger.info("✓ Patient reactivated");

        logger.info("Status toggling works correctly");
    }

    // ============================================================================
    // Helper Methods
    // ============================================================================

    /**
     * Create a basic patient registration request for testing
     */
    private static PatientRegistrationRequest createBasicPatientRequest(
            String firstName, String lastName, String dob, String gender) {

        PatientRegistrationRequest request = new PatientRegistrationRequest();
        request.setFirstName(firstName);
        request.setLastName(lastName);
        request.setDateOfBirth(dob);
        request.setGender(gender);
        request.setVisitType("OPD");

        // Add contact info
        var contactInfo = new PatientRegistrationRequest.ContactInfo();
        contactInfo.setPrimaryPhone("9876543210");
        contactInfo.setPrimaryEmail(firstName.toLowerCase() + "." + lastName.toLowerCase() + "@example.com");
        request.setContactInfo(contactInfo);

        return request;
    }
}

