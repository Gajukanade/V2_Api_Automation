package org.example.api;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.example.models.PatientRegistrationRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.google.gson.JsonObject;

/**
 * Comprehensive API Client for Patient Management endpoints
 *
 * Base path: /patients
 * Auth required: ✅ (with RBAC)
 *
 * Manages the patient registry for the facility. Supports:
 * - Local patient registration (assigns UHID)
 * - Merged view (local + GPR patients from Superadmin service)
 * - Full CRUD operations on patient demographics and status
 */
public class PatientApiClient {
    private static final Logger logger = LoggerFactory.getLogger(PatientApiClient.class);
    private static final String BASE_URL = "https://v2-services.chikitsa.dev/hospital-admin/api/v1";
    private static final String PATIENTS_ENDPOINT = "/patients";

    private String accessToken;
    private String facilityId;

    public PatientApiClient(String accessToken, String facilityId) {
        this.accessToken = accessToken;
        this.facilityId = facilityId;
    }

    public PatientApiClient(String accessToken) {
        this.accessToken = accessToken;
        this.facilityId = null;
    }

    /**
     * POST /patients
     * Register a new patient locally. Assigns a UHID.
     *
     * @param request PatientRegistrationRequest with patient details
     * @return Response from the API
     */
    public Response registerPatient(PatientRegistrationRequest request) {
        logger.info("Registering new patient: {} {}",
            request.getFirstName(), request.getLastName());

        Response response = RestAssured
            .given()
                .baseUri(BASE_URL)
                .header("Authorization", "Bearer " + accessToken)
                .header("Content-Type", "application/json")
                .body(request)
            .when()
                .post(PATIENTS_ENDPOINT)
            .then()
                .log().all()
            .extract()
                .response();

        logger.info("Registration response status: {}", response.getStatusCode());
        if (response.getStatusCode() == 201) {
            logger.info("Patient registered successfully");
        } else {
            logger.error("Patient registration failed with status: {}", response.getStatusCode());
        }

        return response;
    }

    /**
     * GET /patients
     * List all patients (local + GPR merged).
     * Supports filtering by: search, is_active, visit_type
     *
     * @param page Page number (optional)
     * @param limit Items per page (optional)
     * @return Response containing paginated list of patients
     */
    public Response listAllPatients(Integer page, Integer limit) {
        logger.info("Fetching all patients (local + GPR merged) - Page: {}, Limit: {}",
            page != null ? page : "default", limit != null ? limit : "default");

        var request = RestAssured
            .given()
                .baseUri(BASE_URL)
                .header("Authorization", "Bearer " + accessToken)
                .header("Content-Type", "application/json");

        if (page != null) {
            request.queryParam("page", page);
        }
        if (limit != null) {
            request.queryParam("limit", limit);
        }

        Response response = request
            .when()
                .get(PATIENTS_ENDPOINT)
            .then()
                .log().all()
            .extract()
                .response();

        logger.info("List patients response status: {}", response.getStatusCode());
        return response;
    }

    /**
     * GET /patients
     * List all patients with search filter
     *
     * @param search Search term (name, UHID, phone, email, etc.)
     * @param page Page number (optional)
     * @param limit Items per page (optional)
     * @return Response containing filtered patient list
     */
    public Response listPatientsBySearch(String search, Integer page, Integer limit) {
        logger.info("Searching patients with search term: '{}'", search);

        var request = RestAssured
            .given()
                .baseUri(BASE_URL)
                .header("Authorization", "Bearer " + accessToken)
                .header("Content-Type", "application/json")
                .queryParam("search", search);

        if (page != null) {
            request.queryParam("page", page);
        }
        if (limit != null) {
            request.queryParam("limit", limit);
        }

        Response response = request
            .when()
                .get(PATIENTS_ENDPOINT)
            .then()
                .log().all()
            .extract()
                .response();

        logger.info("Search response status: {}", response.getStatusCode());
        return response;
    }

    /**
     * GET /patients
     * List patients filtered by active/inactive status
     *
     * @param isActive true for active patients, false for inactive
     * @param page Page number (optional)
     * @param limit Items per page (optional)
     * @return Response containing filtered patient list
     */
    public Response listPatientsByStatus(Boolean isActive, Integer page, Integer limit) {
        logger.info("Fetching patients with active status: {}", isActive);

        var request = RestAssured
            .given()
                .baseUri(BASE_URL)
                .header("Authorization", "Bearer " + accessToken)
                .header("Content-Type", "application/json")
                .queryParam("is_active", isActive);

        if (page != null) {
            request.queryParam("page", page);
        }
        if (limit != null) {
            request.queryParam("limit", limit);
        }

        Response response = request
            .when()
                .get(PATIENTS_ENDPOINT)
            .then()
                .log().all()
            .extract()
                .response();

        logger.info("Status filter response status: {}", response.getStatusCode());
        return response;
    }

    /**
     * GET /patients
     * List patients filtered by visit type
     *
     * @param visitType Type of visit (e.g., "OPD", "IPD", "EMERGENCY")
     * @param page Page number (optional)
     * @param limit Items per page (optional)
     * @return Response containing filtered patient list
     */
    public Response listPatientsByVisitType(String visitType, Integer page, Integer limit) {
        logger.info("Fetching patients with visit type: {}", visitType);

        var request = RestAssured
            .given()
                .baseUri(BASE_URL)
                .header("Authorization", "Bearer " + accessToken)
                .header("Content-Type", "application/json")
                .queryParam("visit_type", visitType);

        if (page != null) {
            request.queryParam("page", page);
        }
        if (limit != null) {
            request.queryParam("limit", limit);
        }

        Response response = request
            .when()
                .get(PATIENTS_ENDPOINT)
            .then()
                .log().all()
            .extract()
                .response();

        logger.info("Visit type filter response status: {}", response.getStatusCode());
        return response;
    }

    /**
     * GET /patients/local-patients
     * List only locally registered patients with rich filtering
     *
     * Supports filtering by: uhid, first_name, last_name, primary_phone, primary_email, sort_by, sort_order
     *
     * @param page Page number (optional)
     * @param limit Items per page (optional)
     * @return Response containing list of local patients only
     */
    public Response listLocalPatients(Integer page, Integer limit) {
        logger.info("Fetching local patients only - Page: {}, Limit: {}",
            page != null ? page : "default", limit != null ? limit : "default");

        var request = RestAssured
            .given()
                .baseUri(BASE_URL)
                .header("Authorization", "Bearer " + accessToken)
                .header("Content-Type", "application/json");

        if (page != null) {
            request.queryParam("page", page);
        }
        if (limit != null) {
            request.queryParam("limit", limit);
        }

        Response response = request
            .when()
                .get(PATIENTS_ENDPOINT + "/local-patients")
            .then()
                .log().all()
            .extract()
                .response();

        logger.info("List local patients response status: {}", response.getStatusCode());
        return response;
    }

    /**
     * GET /patients/local-patients
     * List local patients filtered by UHID
     *
     * @param uhid Unique Hospital Identifier
     * @param page Page number (optional)
     * @param limit Items per page (optional)
     * @return Response containing matching local patient
     */
    public Response getLocalPatientByUhid(String uhid, Integer page, Integer limit) {
        logger.info("Fetching local patient by UHID: {}", uhid);

        var request = RestAssured
            .given()
                .baseUri(BASE_URL)
                .header("Authorization", "Bearer " + accessToken)
                .header("Content-Type", "application/json")
                .queryParam("uhid", uhid);

        if (page != null) {
            request.queryParam("page", page);
        }
        if (limit != null) {
            request.queryParam("limit", limit);
        }

        Response response = request
            .when()
                .get(PATIENTS_ENDPOINT + "/local-patients")
            .then()
                .log().all()
            .extract()
                .response();

        logger.info("Get patient by UHID response status: {}", response.getStatusCode());
        return response;
    }

    /**
     * GET /patients/local-patients
     * List local patients filtered by first name
     *
     * @param firstName Patient's first name
     * @param page Page number (optional)
     * @param limit Items per page (optional)
     * @return Response containing matching local patients
     */
    public Response getLocalPatientsByFirstName(String firstName, Integer page, Integer limit) {
        logger.info("Fetching local patients by first name: {}", firstName);

        var request = RestAssured
            .given()
                .baseUri(BASE_URL)
                .header("Authorization", "Bearer " + accessToken)
                .header("Content-Type", "application/json")
                .queryParam("first_name", firstName);

        if (page != null) {
            request.queryParam("page", page);
        }
        if (limit != null) {
            request.queryParam("limit", limit);
        }

        Response response = request
            .when()
                .get(PATIENTS_ENDPOINT + "/local-patients")
            .then()
                .log().all()
            .extract()
                .response();

        logger.info("Get patients by first name response status: {}", response.getStatusCode());
        return response;
    }

    /**
     * GET /patients/local-patients
     * List local patients filtered by last name
     *
     * @param lastName Patient's last name
     * @param page Page number (optional)
     * @param limit Items per page (optional)
     * @return Response containing matching local patients
     */
    public Response getLocalPatientsByLastName(String lastName, Integer page, Integer limit) {
        logger.info("Fetching local patients by last name: {}", lastName);

        var request = RestAssured
            .given()
                .baseUri(BASE_URL)
                .header("Authorization", "Bearer " + accessToken)
                .header("Content-Type", "application/json")
                .queryParam("last_name", lastName);

        if (page != null) {
            request.queryParam("page", page);
        }
        if (limit != null) {
            request.queryParam("limit", limit);
        }

        Response response = request
            .when()
                .get(PATIENTS_ENDPOINT + "/local-patients")
            .then()
                .log().all()
            .extract()
                .response();

        logger.info("Get patients by last name response status: {}", response.getStatusCode());
        return response;
    }

    /**
     * GET /patients/local-patients
     * List local patients filtered by primary phone number
     *
     * @param primaryPhone Patient's primary phone number
     * @param page Page number (optional)
     * @param limit Items per page (optional)
     * @return Response containing matching local patient
     */
    public Response getLocalPatientsByPhone(String primaryPhone, Integer page, Integer limit) {
        logger.info("Fetching local patient by phone: {}", primaryPhone);

        var request = RestAssured
            .given()
                .baseUri(BASE_URL)
                .header("Authorization", "Bearer " + accessToken)
                .header("Content-Type", "application/json")
                .queryParam("primary_phone", primaryPhone);

        if (page != null) {
            request.queryParam("page", page);
        }
        if (limit != null) {
            request.queryParam("limit", limit);
        }

        Response response = request
            .when()
                .get(PATIENTS_ENDPOINT + "/local-patients")
            .then()
                .log().all()
            .extract()
                .response();

        logger.info("Get patient by phone response status: {}", response.getStatusCode());
        return response;
    }

    /**
     * GET /patients/local-patients
     * List local patients filtered by primary email
     *
     * @param primaryEmail Patient's primary email address
     * @param page Page number (optional)
     * @param limit Items per page (optional)
     * @return Response containing matching local patient
     */
    public Response getLocalPatientsByEmail(String primaryEmail, Integer page, Integer limit) {
        logger.info("Fetching local patient by email: {}", primaryEmail);

        var request = RestAssured
            .given()
                .baseUri(BASE_URL)
                .header("Authorization", "Bearer " + accessToken)
                .header("Content-Type", "application/json")
                .queryParam("primary_email", primaryEmail);

        if (page != null) {
            request.queryParam("page", page);
        }
        if (limit != null) {
            request.queryParam("limit", limit);
        }

        Response response = request
            .when()
                .get(PATIENTS_ENDPOINT + "/local-patients")
            .then()
                .log().all()
            .extract()
                .response();

        logger.info("Get patient by email response status: {}", response.getStatusCode());
        return response;
    }

    /**
     * GET /patients/:id
     * Get a single patient by UUID (local or merged from GPR)
     *
     * @param patientId Patient UUID
     * @return Response containing full patient details
     */
    public Response getPatientById(String patientId) {
        logger.info("Fetching patient with ID: {}", patientId);

        Response response = RestAssured
            .given()
                .baseUri(BASE_URL)
                .header("Authorization", "Bearer " + accessToken)
                .header("Content-Type", "application/json")
            .when()
                .get(PATIENTS_ENDPOINT + "/" + patientId)
            .then()
                .log().all()
            .extract()
                .response();

        logger.info("Get patient by ID response status: {}", response.getStatusCode());
        return response;
    }

    /**
     * PATCH /patients/:id
     * Update patient demographics, contact info, or insurance details
     *
     * @param patientId Patient UUID
     * @param updateData JSON object containing fields to update
     * @return Response confirming the update
     */
    public Response updatePatient(String patientId, JsonObject updateData) {
        logger.info("Updating patient with ID: {}", patientId);
        logger.debug("Update payload: {}", updateData.toString());

        Response response = RestAssured
            .given()
                .baseUri(BASE_URL)
                .header("Authorization", "Bearer " + accessToken)
                .header("Content-Type", "application/json")
                .body(updateData.toString())
            .when()
                .patch(PATIENTS_ENDPOINT + "/" + patientId)
            .then()
                .log().all()
            .extract()
                .response();

        logger.info("Update patient response status: {}", response.getStatusCode());
        if (response.getStatusCode() == 200) {
            logger.info("Patient updated successfully");
        } else {
            logger.error("Patient update failed with status: {}", response.getStatusCode());
        }

        return response;
    }

    /**
     * PATCH /patients/:id/status
     * Activate or deactivate a patient record
     *
     * @param patientId Patient UUID
     * @param isActive true to activate, false to deactivate
     * @return Response confirming the status change
     */
    public Response updatePatientStatus(String patientId, Boolean isActive) {
        logger.info("Updating patient status - ID: {}, Is Active: {}", patientId, isActive);

        JsonObject statusBody = new JsonObject();
        statusBody.addProperty("is_active", isActive);

        Response response = RestAssured
            .given()
                .baseUri(BASE_URL)
                .header("Authorization", "Bearer " + accessToken)
                .header("Content-Type", "application/json")
                .body(statusBody.toString())
            .when()
                .patch(PATIENTS_ENDPOINT + "/" + patientId + "/status")
            .then()
                .log().all()
            .extract()
                .response();

        logger.info("Update patient status response: {}", response.getStatusCode());
        if (response.getStatusCode() == 200) {
            logger.info("Patient status updated to: {}", isActive ? "ACTIVE" : "INACTIVE");
        } else {
            logger.error("Status update failed with status: {}", response.getStatusCode());
        }

        return response;
    }

    /**
     * Get access token from this client
     */
    public String getAccessToken() {
        return accessToken;
    }

    /**
     * Set access token for this client
     */
    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    /**
     * Get facility ID
     */
    public String getFacilityId() {
        return facilityId;
    }

    /**
     * Set facility ID
     */
    public void setFacilityId(String facilityId) {
        this.facilityId = facilityId;
    }
}

