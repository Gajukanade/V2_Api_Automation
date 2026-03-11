package org.example.api;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.example.models.PatientRegistrationRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * API Client for Patient Registration endpoint
 *
 * NOTE: The /patients/register endpoint appears to not be implemented on the server yet.
 * The tests are designed for when the endpoint becomes available.
 *
 * Possible endpoint paths to check:
 * - /hospital-admin/api/v1/patients/register (currently returns 404)
 * - /hospital-admin/api/v1/patients (POST)
 * - /api/v1/patients/register
 * - /api/patients/register
 */
public class PatientRegistrationApiClient {
    private static final Logger logger = LoggerFactory.getLogger(PatientRegistrationApiClient.class);
    private static final String BASE_URL = "https://v2-services.chikitsa.dev/hospital-admin/api/v1";
    private static final String REGISTER_PATIENT_ENDPOINT = "/patients";

    private String accessToken;
    private String facilityId;

    public PatientRegistrationApiClient(String accessToken, String facilityId) {
        this.accessToken = accessToken;
        this.facilityId = facilityId;
    }

    /**
     * Register a new patient
     * NOTE: Returns 404 if endpoint not implemented on server
     */
    public Response registerPatient(PatientRegistrationRequest request) {
        logger.info("Initiating patient registration request");
        logger.debug("Request patient: {}", request.getFirstName() + " " + request.getLastName());
        logger.warn("NOTE: /patients/register endpoint returns 404 - may not be implemented on server yet");

        Response response = RestAssured
            .given()
                .baseUri(BASE_URL)
                .header("Authorization", "Bearer " + accessToken)
                .header("Content-Type", "application/json")
                .body(request)
            .when()
                .post(REGISTER_PATIENT_ENDPOINT)
            .then()
                .log().all()
            .extract()
                .response();

        logger.info("Response status code: {}", response.getStatusCode());

        if (response.getStatusCode() == 404) {
            logger.error("ERROR: Patient registration endpoint not found (404)");
            logger.error("Endpoint being called: {}{}", BASE_URL, REGISTER_PATIENT_ENDPOINT);
            logger.error("This endpoint may not be implemented on the server yet");
        }

        return response;
    }

    /**
     * Register patient with facility ID in query parameter
     */
    public Response registerPatientWithFacility(PatientRegistrationRequest request) {
        logger.info("Initiating patient registration with facility ID: {}", facilityId);

        Response response = RestAssured
            .given()
                .baseUri(BASE_URL)
                .header("Authorization", "Bearer " + accessToken)
                .header("Content-Type", "application/json")
                .queryParam("facility_id", facilityId)
                .body(request)
            .when()
                .post(REGISTER_PATIENT_ENDPOINT)
            .then()
                .log().all()
            .extract()
                .response();

        logger.info("Response status code: {}", response.getStatusCode());

        return response;
    }

    /**
     * Get patient by ID
     */
    public Response getPatient(String patientId) {
        logger.info("Fetching patient with ID: {}", patientId);

        Response response = RestAssured
            .given()
                .baseUri(BASE_URL)
                .header("Authorization", "Bearer " + accessToken)
                .header("Content-Type", "application/json")
            .when()
                .get("/patients/" + patientId)
            .then()
                .log().all()
            .extract()
                .response();

        logger.info("Response status code: {}", response.getStatusCode());

        return response;
    }

    /**
     * Update patient information
     */
    public Response updatePatient(String patientId, PatientRegistrationRequest request) {
        logger.info("Updating patient with ID: {}", patientId);

        Response response = RestAssured
            .given()
                .baseUri(BASE_URL)
                .header("Authorization", "Bearer " + accessToken)
                .header("Content-Type", "application/json")
                .body(request)
            .when()
                .put("/patients/" + patientId)
            .then()
                .log().all()
            .extract()
                .response();

        logger.info("Response status code: {}", response.getStatusCode());

        return response;
    }
}

