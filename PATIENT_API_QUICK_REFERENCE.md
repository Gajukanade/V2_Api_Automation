# Patient API - Quick Reference Guide

## 📋 Summary

This document provides a quick reference for the Patient API implementation.

---

## 🚀 Quick Start

### 1. Initialize Patient API Client

```java
import org.example.api.PatientApiClient;

PatientApiClient patientClient = new PatientApiClient(accessToken, facilityId);
```

### 2. Register a Patient

```java
PatientRegistrationRequest request = new PatientRegistrationRequest();
request.setFirstName("John");
request.setLastName("Doe");
request.setDateOfBirth("1990-01-15");
request.setGender("M");

Response response = patientClient.registerPatient(request);
// Returns: 201 Created with patient_id and UHID
```

### 3. List All Patients

```java
Response response = patientClient.listAllPatients(1, 20);
// Returns: 200 OK with paginated list of patients
```

### 4. Search Patients

```java
Response response = patientClient.listPatientsBySearch("John", 1, 20);
// Filter by active status
Response active = patientClient.listPatientsByStatus(true, 1, 20);
// Filter by visit type
Response opd = patientClient.listPatientsByVisitType("OPD", 1, 20);
```

### 5. Get Patient Details

```java
Response response = patientClient.getPatientById(patientId);
// Returns: 200 OK with full patient details
```

### 6. Update Patient

```java
JsonObject updates = new JsonObject();
updates.addProperty("first_name", "Jonathan");
updates.addProperty("age", 35);

Response response = patientClient.updatePatient(patientId, updates);
// Returns: 200 OK with updated patient info
```

### 7. Update Patient Status

```java
Response response = patientClient.updatePatientStatus(patientId, false);
// Deactivate patient

Response response = patientClient.updatePatientStatus(patientId, true);
// Activate patient
```

---

## 📍 API Endpoints

| Method | Endpoint | Description |
|--------|----------|-------------|
| `POST` | `/patients` | Register new patient |
| `GET` | `/patients` | List all patients (with filters) |
| `GET` | `/patients/local-patients` | List local patients only |
| `GET` | `/patients/:id` | Get patient by ID |
| `PATCH` | `/patients/:id` | Update patient details |
| `PATCH` | `/patients/:id/status` | Change patient status |

---

## 🔍 Filtering Options

### List All Patients
- `page` - Page number
- `limit` - Items per page
- `search` - Search term
- `is_active` - Active/inactive
- `visit_type` - OPD, IPD, EMERGENCY

### List Local Patients
- `page` - Page number
- `limit` - Items per page
- `uhid` - Filter by UHID
- `first_name` - Filter by first name
- `last_name` - Filter by last name
- `primary_phone` - Filter by phone
- `primary_email` - Filter by email
- `sort_by` - Sort field
- `sort_order` - asc/desc

---

## 📊 HTTP Status Codes

| Code | Meaning |
|------|---------|
| `200` | Success |
| `201` | Created |
| `400` | Bad Request |
| `401` | Unauthorized |
| `403` | Forbidden |
| `404` | Not Found |
| `409` | Conflict |

---

## 🧪 Running Tests

```bash
# All tests
mvn test -Dtest=PatientApiTest

# Specific test
mvn test -Dtest=PatientApiTest#test01_RegisterPatient

# Tests matching pattern
mvn test -Dtest=PatientApiTest#test*Search*
```

---

## 📋 Test Coverage (20 Tests)

| # | Test | Endpoint | Method |
|---|------|----------|--------|
| 1 | Register Patient | POST /patients | ✅ |
| 2 | Register Multiple | POST /patients | ✅ |
| 3 | List All | GET /patients | ✅ |
| 4 | Pagination | GET /patients | ✅ |
| 5 | Search by Name | GET /patients | ✅ |
| 6 | Filter by Status | GET /patients | ✅ |
| 7 | Filter by Visit Type | GET /patients | ✅ |
| 8 | List Local | GET /patients/local-patients | ✅ |
| 9 | Search by UHID | GET /patients/local-patients | ✅ |
| 10 | Search by First Name | GET /patients/local-patients | ✅ |
| 11 | Search by Last Name | GET /patients/local-patients | ✅ |
| 12 | Search by Phone | GET /patients/local-patients | ✅ |
| 13 | Search by Email | GET /patients/local-patients | ✅ |
| 14 | Get by ID | GET /patients/:id | ✅ |
| 15 | Update Demographics | PATCH /patients/:id | ✅ |
| 16 | Update Contact | PATCH /patients/:id | ✅ |
| 17 | Update Insurance | PATCH /patients/:id | ✅ |
| 18 | Activate | PATCH /patients/:id/status | ✅ |
| 19 | Deactivate | PATCH /patients/:id/status | ✅ |
| 20 | Toggle Status | PATCH /patients/:id/status | ✅ |

---

## 📦 Files Created

### API Client
- **File**: `src/main/java/org/example/api/PatientApiClient.java`
- **Lines**: ~640
- **Methods**: 16 (covers all 6 endpoints)

### Test Suite
- **File**: `src/test/java/org/example/tests/PatientApiTest.java`
- **Lines**: ~750
- **Tests**: 20 comprehensive test cases

### Documentation
- **File**: `PATIENT_API_IMPLEMENTATION.md`
- **Complete guide with examples and best practices**

---

## 🔐 Authentication

All endpoints require:
```
Authorization: Bearer <accessToken>
```

Obtain token from Login API:
```java
LoginApiClient loginClient = new LoginApiClient();
Response loginResponse = loginClient.login(email, password, facilityId, facilityName, deviceId);
String accessToken = loginResponse.jsonPath().getString("accessToken");
```

---

## ✨ Key Features

✅ Full API coverage (6 endpoints)  
✅ Rich filtering and search capabilities  
✅ Comprehensive error handling  
✅ Detailed logging  
✅ 20 test cases  
✅ Complete documentation  
✅ Production-ready code  

---

## 💡 Usage Example

```java
// 1. Login
LoginApiClient loginClient = new LoginApiClient();
Response login = loginClient.login("nelson@gmail.com", "Chikitsa@123", 
    "b60fae41-3a86-4d4f-8ca9-8180a7d44e0e", "SMS Hospital", 
    "8c094825-a89a-4b96-a4c7-7d6d06359dea");
String token = login.jsonPath().getString("accessToken");

// 2. Initialize
PatientApiClient patientClient = new PatientApiClient(token);

// 3. Register Patient
PatientRegistrationRequest req = new PatientRegistrationRequest();
req.setFirstName("John");
req.setLastName("Doe");
req.setDateOfBirth("1990-01-15");
req.setGender("M");
Response registerResp = patientClient.registerPatient(req);
String patientId = registerResp.jsonPath().getString("patient_id");

// 4. Get Patient
Response getResp = patientClient.getPatientById(patientId);
String firstName = getResp.jsonPath().getString("data.first_name");

// 5. Update Patient
JsonObject updates = new JsonObject();
updates.addProperty("age", 34);
patientClient.updatePatient(patientId, updates);

// 6. Manage Status
patientClient.updatePatientStatus(patientId, false); // Deactivate
```

---

## 📞 Support Resources

1. **Full Documentation**: `PATIENT_API_IMPLEMENTATION.md`
2. **API Reference**: `API_Reference.md` (Section 6)
3. **Test Suite**: `PatientApiTest.java` (20 examples)
4. **Code Comments**: Inline documentation in `PatientApiClient.java`

---

## ✅ Status: Complete

| Component | Status |
|-----------|--------|
| API Client | ✅ Complete |
| Test Suite | ✅ Complete |
| Documentation | ✅ Complete |
| Error Handling | ✅ Complete |
| Logging | ✅ Complete |
| Examples | ✅ Complete |

---

**Implementation Date**: March 11, 2026  
**API Version**: v1  
**Framework**: RestAssured + Java  
**Logging**: SLF4J + Logback

