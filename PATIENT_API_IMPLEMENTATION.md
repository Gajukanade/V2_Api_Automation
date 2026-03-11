# Patient API Implementation Guide

## Overview

This document provides comprehensive documentation for the **Patient Management API** implementation in the V2 API Tests project.

**Base Path**: `/patients`  
**Authentication Required**: ✅ Yes (with RBAC)  
**Server**: `https://v2-services.chikitsa.dev/hospital-admin/api/v1`

---

## Table of Contents

1. [API Endpoints](#api-endpoints)
2. [Patient API Client Usage](#patient-api-client-usage)
3. [Test Suite Overview](#test-suite-overview)
4. [Request/Response Models](#requestresponse-models)
5. [Error Handling](#error-handling)
6. [Complete Examples](#complete-examples)

---

## API Endpoints

### 1. POST /patients - Register Patient

**Description**: Register a new patient locally. The system automatically assigns a UHID (Unique Hospital Identifier).

**Authentication**: Required ✅

**Request**:
```http
POST /patients HTTP/1.1
Host: v2-services.chikitsa.dev
Authorization: Bearer <accessToken>
Content-Type: application/json

{
  "first_name": "John",
  "last_name": "Doe",
  "middle_name": "Michael",
  "date_of_birth": "1990-01-15",
  "gender": "M",
  "visit_type": "OPD",
  "contact_info": {
    "primary_phone": "9876543210",
    "primary_email": "john@example.com"
  }
}
```

**Response** (201 Created):
```json
{
  "success": true,
  "patient_id": "550e8400-e29b-41d4-a716-446655440000",
  "data": {
    "id": "550e8400-e29b-41d4-a716-446655440000",
    "uhid": "UH/SMS/001234",
    "global_patient_id": null,
    "first_name": "John",
    "middle_name": "Michael",
    "last_name": "Doe",
    "date_of_birth": "1990-01-15",
    "gender": "M",
    "is_active": true,
    "created_at": "2026-03-10T10:30:00.000Z"
  }
}
```

**Status Codes**:
- `201 Created` - Patient registered successfully
- `400 Bad Request` - Validation error (missing fields, invalid data)
- `401 Unauthorized` - Invalid/missing authentication token
- `403 Forbidden` - User lacks permission to register patients
- `409 Conflict` - Duplicate registration

---

### 2. GET /patients - List All Patients

**Description**: Retrieve all patients (merged view combining local patients and GPR patients from Superadmin service).

**Authentication**: Required ✅

**Query Parameters**:
| Parameter | Type | Description | Example |
|-----------|------|-------------|---------|
| `page` | integer | Page number (default: 1) | `1` |
| `limit` | integer | Items per page (default: 20) | `20` |
| `search` | string | Search term for patient name, UHID, phone, email | `"John"` |
| `is_active` | boolean | Filter by patient status | `true` |
| `visit_type` | string | Filter by visit type (OPD, IPD, EMERGENCY) | `"OPD"` |

**Request**:
```http
GET /patients?page=1&limit=20 HTTP/1.1
Host: v2-services.chikitsa.dev
Authorization: Bearer <accessToken>
```

**Response** (200 OK):
```json
{
  "success": true,
  "data": [
    {
      "id": "550e8400-e29b-41d4-a716-446655440000",
      "uhid": "UH/SMS/001234",
      "first_name": "John",
      "last_name": "Doe",
      "gender": "M",
      "is_active": true,
      "visit_type": "OPD",
      "created_at": "2026-03-10T10:30:00.000Z"
    }
  ],
  "pagination": {
    "page": 1,
    "limit": 20,
    "total": 150,
    "pages": 8
  }
}
```

---

### 3. GET /patients/local-patients - List Local Patients

**Description**: Retrieve only locally registered patients for this facility.

**Authentication**: Required ✅

**Query Parameters**:
| Parameter | Type | Description |
|-----------|------|-------------|
| `page` | integer | Page number (default: 1) |
| `limit` | integer | Items per page (default: 20) |
| `uhid` | string | Filter by UHID |
| `first_name` | string | Filter by first name |
| `last_name` | string | Filter by last name |
| `primary_phone` | string | Filter by phone number |
| `primary_email` | string | Filter by email address |
| `sort_by` | string | Sort field (default: `created_at`) |
| `sort_order` | string | `asc` or `desc` (default: `desc`) |

**Request**:
```http
GET /patients/local-patients?uhid=UH/SMS/001234 HTTP/1.1
Host: v2-services.chikitsa.dev
Authorization: Bearer <accessToken>
```

**Response** (200 OK):
```json
{
  "success": true,
  "data": [
    {
      "id": "550e8400-e29b-41d4-a716-446655440000",
      "uhid": "UH/SMS/001234",
      "first_name": "John",
      "last_name": "Doe",
      "contact_info": {
        "primary_phone": "9876543210",
        "primary_email": "john@example.com"
      },
      "is_active": true
    }
  ],
  "pagination": {
    "page": 1,
    "limit": 20,
    "total": 45,
    "pages": 3
  }
}
```

---

### 4. GET /patients/:id - Get Patient by ID

**Description**: Retrieve detailed information for a specific patient by UUID.

**Authentication**: Required ✅

**Path Parameters**:
| Parameter | Type | Description |
|-----------|------|-------------|
| `id` | UUID | Patient UUID |

**Request**:
```http
GET /patients/550e8400-e29b-41d4-a716-446655440000 HTTP/1.1
Host: v2-services.chikitsa.dev
Authorization: Bearer <accessToken>
```

**Response** (200 OK):
```json
{
  "success": true,
  "data": {
    "id": "550e8400-e29b-41d4-a716-446655440000",
    "uhid": "UH/SMS/001234",
    "global_patient_id": null,
    "honorific": "Mr.",
    "first_name": "John",
    "middle_name": "Michael",
    "last_name": "Doe",
    "date_of_birth": "1990-01-15",
    "age": 36,
    "gender": "M",
    "blood_group": "O+",
    "contact_info": {
      "primary_phone": "9876543210",
      "alternate_phones": ["8765432109"],
      "primary_email": "john@example.com",
      "alternate_emails": ["john.doe@example.com"]
    },
    "address": {
      "address_line_1": "123 Main St",
      "city": "New York",
      "state": "NY",
      "country": "USA",
      "pincode": "10001"
    },
    "insurance_info": {
      "provider": "AETNA",
      "policy_number": "POL123456789",
      "policy_holder": "John Doe"
    },
    "is_active": true,
    "created_at": "2026-03-10T10:30:00.000Z",
    "updated_at": "2026-03-10T10:30:00.000Z"
  }
}
```

**Status Codes**:
- `200 OK` - Patient found
- `401 Unauthorized` - Invalid token
- `404 Not Found` - Patient doesn't exist

---

### 5. PATCH /patients/:id - Update Patient

**Description**: Update patient demographics, contact information, or insurance details.

**Authentication**: Required ✅

**Path Parameters**:
| Parameter | Type | Description |
|-----------|------|-------------|
| `id` | UUID | Patient UUID |

**Request Body** (partial update):
```json
{
  "first_name": "Jonathan",
  "middle_name": "Michael",
  "age": 37,
  "contact_info": {
    "primary_phone": "9999999999",
    "primary_email": "jonathan@example.com"
  },
  "insurance_info": {
    "provider": "United Healthcare",
    "policy_number": "UH987654321"
  }
}
```

**Request**:
```http
PATCH /patients/550e8400-e29b-41d4-a716-446655440000 HTTP/1.1
Host: v2-services.chikitsa.dev
Authorization: Bearer <accessToken>
Content-Type: application/json

{
  "first_name": "Jonathan",
  "age": 37
}
```

**Response** (200 OK):
```json
{
  "success": true,
  "message": "Patient updated successfully",
  "data": {
    "id": "550e8400-e29b-41d4-a716-446655440000",
    "uhid": "UH/SMS/001234",
    "first_name": "Jonathan",
    "age": 37,
    "updated_at": "2026-03-11T14:45:00.000Z"
  }
}
```

**Status Codes**:
- `200 OK` - Patient updated successfully
- `400 Bad Request` - Validation error
- `401 Unauthorized` - Invalid token
- `404 Not Found` - Patient doesn't exist
- `409 Conflict` - Data conflict (e.g., duplicate email)

**Updatable Fields**:
- Demographic: `first_name`, `middle_name`, `last_name`, `honorific`, `age`, `date_of_birth`, `gender`
- Contact: `primary_phone`, `primary_email`, `alternate_phones`, `alternate_emails`
- Address: `address_line_1`, `address_line_2`, `city`, `state`, `country`, `pincode`
- Medical: `blood_group`, `allergies`, `medical_conditions`
- Insurance: `insurance_provider`, `policy_number`, `policy_holder`
- Other: `occupation`, `marital_status`, `religion`, `nationality`

---

### 6. PATCH /patients/:id/status - Update Patient Status

**Description**: Activate or deactivate a patient record. Deactivated patients appear in reports but cannot be selected for new visits or transactions.

**Authentication**: Required ✅

**Path Parameters**:
| Parameter | Type | Description |
|-----------|------|-------------|
| `id` | UUID | Patient UUID |

**Request Body**:
```json
{
  "is_active": true
}
```

**Request**:
```http
PATCH /patients/550e8400-e29b-41d4-a716-446655440000/status HTTP/1.1
Host: v2-services.chikitsa.dev
Authorization: Bearer <accessToken>
Content-Type: application/json

{
  "is_active": false
}
```

**Response** (200 OK):
```json
{
  "success": true,
  "message": "Patient status updated successfully",
  "data": {
    "id": "550e8400-e29b-41d4-a716-446655440000",
    "uhid": "UH/SMS/001234",
    "is_active": false,
    "updated_at": "2026-03-11T14:45:00.000Z"
  }
}
```

**Status Codes**:
- `200 OK` - Status updated
- `400 Bad Request` - Invalid status value
- `401 Unauthorized` - Invalid token
- `403 Forbidden` - Insufficient permissions
- `404 Not Found` - Patient doesn't exist

---

## Patient API Client Usage

### Initialization

```java
import org.example.api.PatientApiClient;
import org.example.models.PatientRegistrationRequest;

// Initialize with access token
PatientApiClient patientClient = new PatientApiClient(accessToken, facilityId);

// Or with just access token
PatientApiClient patientClient = new PatientApiClient(accessToken);
```

### Register a Patient

```java
PatientRegistrationRequest request = new PatientRegistrationRequest();
request.setFirstName("John");
request.setLastName("Doe");
request.setDateOfBirth("1990-01-15");
request.setGender("M");
request.setVisitType("OPD");

var contactInfo = new PatientRegistrationRequest.ContactInfo();
contactInfo.setPrimaryPhone("9876543210");
contactInfo.setPrimaryEmail("john@example.com");
request.setContactInfo(contactInfo);

Response response = patientClient.registerPatient(request);
if (response.getStatusCode() == 201) {
    String patientId = response.jsonPath().getString("patient_id");
    String uhid = response.jsonPath().getString("data.uhid");
    System.out.println("Patient registered: " + patientId + " - " + uhid);
}
```

### List All Patients

```java
// List all patients with pagination
Response response = patientClient.listAllPatients(1, 20);
// Page 1, 20 items per page

List<Map<String, Object>> patients = response.jsonPath().getList("data");
patients.forEach(p -> System.out.println(p.get("first_name") + " " + p.get("last_name")));
```

### Search Patients

```java
// Search by name
Response response = patientClient.listPatientsBySearch("John", 1, 20);

// Filter by active status
Response activePatients = patientClient.listPatientsByStatus(true, 1, 20);

// Filter by visit type
Response opdPatients = patientClient.listPatientsByVisitType("OPD", 1, 20);
```

### List Local Patients

```java
// Get all local patients
Response response = patientClient.listLocalPatients(1, 20);

// Search by UHID
Response response = patientClient.getLocalPatientByUhid("UH/SMS/001234", 1, 20);

// Search by name
Response response = patientClient.getLocalPatientsByFirstName("John", 1, 20);
Response response = patientClient.getLocalPatientsByLastName("Doe", 1, 20);

// Search by contact
Response response = patientClient.getLocalPatientsByPhone("9876543210", 1, 20);
Response response = patientClient.getLocalPatientsByEmail("john@example.com", 1, 20);
```

### Get Patient Details

```java
Response response = patientClient.getPatientById(patientId);
String firstName = response.jsonPath().getString("data.first_name");
String uhid = response.jsonPath().getString("data.uhid");
```

### Update Patient

```java
import com.google.gson.JsonObject;

JsonObject updateData = new JsonObject();
updateData.addProperty("first_name", "Jonathan");
updateData.addProperty("age", 37);

Response response = patientClient.updatePatient(patientId, updateData);
if (response.getStatusCode() == 200) {
    System.out.println("Patient updated successfully");
}
```

### Update Patient Status

```java
// Deactivate patient
Response response = patientClient.updatePatientStatus(patientId, false);

// Reactivate patient
Response response = patientClient.updatePatientStatus(patientId, true);
```

---

## Test Suite Overview

The `PatientApiTest` class provides 20 comprehensive test cases covering all Patient API endpoints.

### Test Categories

#### Registration Tests
- **Test 01**: Register a single patient
- **Test 02**: Register multiple patients

#### List/Search Tests
- **Test 03**: List all patients (merged view)
- **Test 04**: List with pagination
- **Test 05**: Search by name
- **Test 06**: Filter by active status
- **Test 07**: Filter by visit type
- **Test 08**: List local patients only
- **Test 09**: Search local by UHID
- **Test 10**: Search local by first name
- **Test 11**: Search local by last name
- **Test 12**: Search local by phone
- **Test 13**: Search local by email

#### Retrieval Tests
- **Test 14**: Get patient by ID

#### Update Tests
- **Test 15**: Update demographics
- **Test 16**: Update contact info
- **Test 17**: Update insurance details

#### Status Management Tests
- **Test 18**: Activate patient
- **Test 19**: Deactivate patient
- **Test 20**: Toggle status multiple times

### Running Tests

```bash
# Run all patient API tests
mvn test -Dtest=PatientApiTest

# Run specific test
mvn test -Dtest=PatientApiTest#test01_RegisterPatient

# Run tests matching pattern
mvn test -Dtest=PatientApiTest#test*Search*
```

---

## Request/Response Models

### PatientRegistrationRequest

**Location**: `org.example.models.PatientRegistrationRequest`

**Main Fields**:
```java
private String honorific;           // Mr., Ms., Dr., etc.
private String firstName;           // Required
private String middleName;
private String lastName;            // Required
private String gender;              // M, F, Other
private String dateOfBirth;         // YYYY-MM-DD
private Integer age;
private List<IdProof> idProofs;     // Government IDs
private String aadhaar;
private String abhaNumber;          // National Health ID
private String occupation;
private String maritalStatus;
private List<Scheme> schemes;       // Health schemes
private PatientCategory patientCategory;
private String religion;
private String nationality;
private String visitType;           // OPD, IPD, EMERGENCY
private MedicalInfo medicalInfo;
private ContactInfo contactInfo;
private List<KinInfo> kinInfo;      // Family members
private CorporateInfo corporateInfo;
private BillingInfo billingInfo;
```

### PatientRegistrationResponse

**Location**: `org.example.models.PatientRegistrationResponse`

**Response Fields**:
```java
private Boolean success;
private String patient_id;          // UUID assigned by server
private String message;
private String registrationNumber;
private PatientData data;           // Full patient details
```

---

## Error Handling

### Common Error Scenarios

#### 400 Bad Request - Validation Error
```json
{
  "success": false,
  "error": "VALIDATION_ERROR",
  "message": "First name is required",
  "details": {
    "field": "first_name",
    "reason": "Required field missing"
  }
}
```

#### 401 Unauthorized - Invalid Token
```json
{
  "success": false,
  "error": "UNAUTHORIZED",
  "message": "Invalid or expired access token"
}
```

#### 403 Forbidden - Insufficient Permissions
```json
{
  "success": false,
  "error": "FORBIDDEN",
  "message": "User does not have permission to register patients"
}
```

#### 404 Not Found
```json
{
  "success": false,
  "error": "NOT_FOUND",
  "message": "Patient with ID 550e8400-e29b-41d4-a716-446655440000 not found"
}
```

#### 409 Conflict - Duplicate Data
```json
{
  "success": false,
  "error": "CONFLICT",
  "message": "A patient with email john@example.com already exists"
}
```

---

## Complete Examples

### Example 1: Full Patient Registration Workflow

```java
// 1. Login
LoginApiClient loginClient = new LoginApiClient();
Response loginResponse = loginClient.login(
    "nelson@gmail.com",
    "Chikitsa@123",
    "b60fae41-3a86-4d4f-8ca9-8180a7d44e0e",
    "SMS Hospital",
    "8c094825-a89a-4b96-a4c7-7d6d06359dea"
);
String accessToken = loginResponse.jsonPath().getString("accessToken");

// 2. Initialize patient client
PatientApiClient patientClient = new PatientApiClient(accessToken);

// 3. Create patient request
PatientRegistrationRequest request = new PatientRegistrationRequest();
request.setFirstName("Alice");
request.setLastName("Smith");
request.setDateOfBirth("1985-05-20");
request.setGender("F");
request.setVisitType("OPD");
request.setMaritalStatus("Married");
request.setOccupation("Engineer");

var contactInfo = new PatientRegistrationRequest.ContactInfo();
contactInfo.setPrimaryPhone("9123456789");
contactInfo.setPrimaryEmail("alice.smith@example.com");
request.setContactInfo(contactInfo);

var addressInfo = new PatientRegistrationRequest.AddressInfo();
addressInfo.setAddressLine1("456 Park Avenue");
addressInfo.setCity("San Francisco");
addressInfo.setState("CA");
addressInfo.setCountry("USA");
addressInfo.setPincode("94102");
request.setAddressInfo(addressInfo);

// 4. Register patient
Response registerResponse = patientClient.registerPatient(request);
String patientId = registerResponse.jsonPath().getString("patient_id");
String uhid = registerResponse.jsonPath().getString("data.uhid");

System.out.println("Patient registered successfully");
System.out.println("ID: " + patientId);
System.out.println("UHID: " + uhid);

// 5. Retrieve patient details
Response getResponse = patientClient.getPatientById(patientId);
String firstName = getResponse.jsonPath().getString("data.first_name");
System.out.println("Retrieved patient: " + firstName);

// 6. Update patient information
JsonObject updateData = new JsonObject();
updateData.addProperty("occupation", "Senior Engineer");
updateData.addProperty("marital_status", "Married");

Response updateResponse = patientClient.updatePatient(patientId, updateData);
System.out.println("Patient updated successfully");

// 7. Manage patient status
patientClient.updatePatientStatus(patientId, false); // Deactivate
System.out.println("Patient deactivated");

patientClient.updatePatientStatus(patientId, true);  // Reactivate
System.out.println("Patient reactivated");
```

### Example 2: Search and Filter Workflow

```java
PatientApiClient patientClient = new PatientApiClient(accessToken);

// 1. Search all patients by name
Response searchResponse = patientClient.listPatientsBySearch("John", 1, 10);
int total = searchResponse.jsonPath().getInt("pagination.total");
System.out.println("Found " + total + " patients matching 'John'");

// 2. Get active patients only
Response activeResponse = patientClient.listPatientsByStatus(true, 1, 10);
List<String> activeUhids = activeResponse.jsonPath().getList("data.uhid");
System.out.println("Active patients: " + activeUhids.size());

// 3. Get OPD patients
Response opdResponse = patientClient.listPatientsByVisitType("OPD", 1, 10);
List<String> opdPatients = opdResponse.jsonPath().getList("data.id");
System.out.println("OPD patients: " + opdPatients.size());

// 4. Search local patients by phone
Response phoneResponse = patientClient.getLocalPatientsByPhone("9876543210", 1, 5);
String patientId = phoneResponse.jsonPath().getString("data[0].id");
System.out.println("Found patient: " + patientId);

// 5. Sort and paginate local patients
Response page1 = patientClient.listLocalPatients(1, 20);
Response page2 = patientClient.listLocalPatients(2, 20);
System.out.println("Retrieved pages 1 and 2 of local patients");
```

---

## Project Structure

```
V2_Api_Tests/
├── src/
│   ├── main/java/org/example/
│   │   ├── api/
│   │   │   ├── LoginApiClient.java
│   │   │   ├── PatientApiClient.java          ← NEW
│   │   │   └── PatientRegistrationApiClient.java
│   │   ├── models/
│   │   │   ├── LoginRequest.java
│   │   │   ├── PatientRegistrationRequest.java
│   │   │   ├── PatientRegistrationResponse.java
│   │   │   └── ...
│   │   └── config/
│   └── test/java/org/example/tests/
│       ├── PatientApiTest.java               ← NEW
│       └── PatientRegistrationTest.java
├── pom.xml
└── README.md
```

---

## Key Features

✅ **Complete API Coverage**: All 6 patient endpoints implemented  
✅ **Rich Filtering**: Support for UHID, name, phone, email, status, visit type  
✅ **Comprehensive Testing**: 20 test cases covering all scenarios  
✅ **Error Handling**: Proper exception handling and logging  
✅ **Pagination Support**: Page and limit parameters for list endpoints  
✅ **RESTful Design**: Follows REST conventions for all operations  
✅ **Well Documented**: Inline code comments and API documentation  

---

## Next Steps

1. Run the test suite: `mvn test -Dtest=PatientApiTest`
2. Review test results in the console output
3. Integrate `PatientApiClient` into your application
4. Extend test cases as needed for specific use cases
5. Check the API Reference documentation for additional endpoint details

---

## Support

For issues, questions, or clarifications:
1. Check the inline comments in `PatientApiClient.java`
2. Review test cases in `PatientApiTest.java`
3. Consult the API Reference documentation
4. Check server logs for detailed error messages

---

**Last Updated**: March 11, 2026  
**API Version**: v1  
**Implementation Status**: Complete ✅

