# Patient Registration API Response Structure Reference

## Actual API Response Format

When you register a patient, the API returns:

```json
{
    "success": true,
    "data": {
        "uhid": "SH25_00418",
        "global_patient_id": "1bdf0a77-efd5-4f6c-99d0-2478a65c685b",
        "honorific": null,
        "first_name": "Test",
        "middle_name": null,
        "last_name": "User",
        "primary_phone": "9123456789",
        "primary_email": "test.user@example.com",
        "schemes": null,
        "patient_category": null,
        "visit_type": ["OPD"],
        "corporate_info": null,
        "billing_info": null,
        "created_by": {
            "id": "91284656-0f5c-4579-a83d-4822102ed1f1",
            "username": "nelson",
            "roles": [...]
        },
        "updated_by": {
            "id": "91284656-0f5c-4579-a83d-4822102ed1f1",
            "username": "nelson",
            "roles": [...]
        },
        "id": "5feb6015-142f-4cd2-9fd4-cecb238d5967",
        "is_active": true,
        "created_at": "2026-03-09T12:15:21.220Z",
        "updated_at": "2026-03-09T12:15:21.220Z"
    }
}
```

## How to Access Data in Tests

### Deserialize the Response
```java
PatientRegistrationResponse patientResponse = response.as(PatientRegistrationResponse.class);
```

### Access Top-Level Fields
```java
Boolean success = patientResponse.getSuccess();
PatientRegistrationResponse.PatientData data = patientResponse.getData();
```

### Access Patient Data Fields
```java
// Patient ID (main unique identifier)
String patientId = data.getId();
String globalPatientId = // global_patient_id field if needed

// Patient Info
String firstName = data.getFirstName();
String lastName = data.getLastName();
String middleName = data.getMiddleName();

// Contact Info
String email = data.getEmail();
String phone = data.getPhone();

// Metadata
String status = data.getStatus();
String createdAt = data.getCreatedAt();
String updatedAt = data.getUpdatedAt();
```

## Important Notes

⚠️ **The following fields are NOT in the API response:**
- Top-level `patient_id` field
- Top-level `message` field  
- Top-level `registration_number` field

✅ **Use these instead:**
- `patientResponse.getData().getId()` for the main patient ID
- Check API documentation for message/status information
- Refer to `data.uhid` if you need the hospital's patient number

## Response Model Structure

```
PatientRegistrationResponse
├── success: Boolean
├── message: String (NOT populated - avoid using)
├── patient_id: String (NOT populated - use data.id instead)
└── data: PatientData
    ├── id: String (main patient ID - UUID format)
    ├── uhid: String (hospital patient number)
    ├── global_patient_id: String
    ├── first_name: String
    ├── last_name: String
    ├── middle_name: String
    ├── primary_email: String
    ├── primary_phone: String
    ├── visit_type: List<String>
    ├── status: String
    ├── is_active: Boolean
    ├── created_at: String (ISO 8601 timestamp)
    ├── updated_at: String (ISO 8601 timestamp)
    ├── created_by: Object
    ├── updated_by: Object
    ├── corporate_info: Object
    ├── billing_info: Object
    └── ... other fields
```

## Common Test Patterns

### Check Registration Success
```java
assertTrue("Registration should be successful", patientResponse.getSuccess());
assertNotNull("Data should exist", patientResponse.getData());
```

### Validate Patient ID
```java
String patientId = patientResponse.getData().getId();
assertNotNull("Patient ID should exist", patientId);
// UUID format validation
assertTrue("Should be valid UUID", 
    patientId.matches("^[0-9a-f]{8}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{12}$"));
```

### Check Patient Details
```java
PatientRegistrationResponse.PatientData data = patientResponse.getData();
assertEquals("John", data.getFirstName());
assertEquals("Doe", data.getLastName());
assertEquals("john.doe@example.com", data.getEmail());
```

## Endpoint Information

- **Endpoint**: POST `/patients/register`
- **Status Code**: 201 (Created) on success
- **Authentication**: Required (Bearer Token)
- **Base URL**: `https://v2-services.chikitsa.dev`

## Version History

| Date | Change |
|------|--------|
| 2026-03-09 | Fixed test expectations to match actual API response structure |

