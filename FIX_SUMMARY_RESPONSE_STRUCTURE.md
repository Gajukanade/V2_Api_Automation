# Fix Summary: Patient Registration Response Structure Mismatch

## Problem
The tests were failing because they were trying to access patient ID information from the wrong location in the API response.

### Error Details
```
java.lang.AssertionError: Patient ID should exist
    at org.junit.Assert.assertNotNull(Assert.java:713)
    at org.example.tests.PatientRegistrationTest.test04_VerifyRegistrationResponse(PatientRegistrationTest.java:139)
```

### Root Cause
The API response structure returned from the patient registration endpoint is:
```json
{
    "success": true,
    "data": {
        "id": "5feb6015-142f-4cd2-9fd4-cecb238d5967",
        "global_patient_id": "1bdf0a77-efd5-4f6c-99d0-2478a65c685b",
        "first_name": "Test",
        "last_name": "User",
        "uhid": "SH25_00418",
        "primary_phone": "9123456789",
        "primary_email": "test.user@example.com",
        ...
        "created_at": "2026-03-09T12:15:21.220Z",
        "updated_at": "2026-03-09T12:15:21.220Z"
    }
}
```

However, the `PatientRegistrationResponse` model and tests were expecting:
- A top-level `patient_id` field (mapped via `@SerializedName("patient_id")`)
- A top-level `message` field
- A top-level `registration_number` field

These fields are **not** present in the actual API response.

## Solution
Updated the following tests to correctly access the patient ID from `patientResponse.getData().getId()` instead of `patientResponse.getPatientId()`:

### Tests Modified

1. **test01_RegisterPatientWithMinimumRequiredFields** (Line 89)
   - Changed: `patientResponse.getPatientId()`
   - To: `patientResponse.getData().getId()`

2. **test02_RegisterPatientWithAllFields** (Line 104)
   - Changed: `patientResponse.getPatientId()`
   - To: `patientResponse.getData().getId()`
   - Removed: `patientResponse.getRegistrationNumber()` check (field doesn't exist in response)

3. **test04_VerifyRegistrationResponse** (Line 139)
   - Removed: `patientResponse.getPatientId()` check
   - Removed: `patientResponse.getMessage()` check
   - Reason: These fields are not in the actual API response
   - Added: Direct validation of `patientResponse.getData()` object and its fields

4. **test05_VerifyPatientIdFormat** (Line 161)
   - Changed: `patientResponse.getPatientId()`
   - To: `patientResponse.getData().getId()`

## Code Changes

### Before
```java
PatientRegistrationResponse patientResponse = response.as(PatientRegistrationResponse.class);
assertTrue("Success should be true", patientResponse.getSuccess());
assertNotNull("Patient ID should not be null", patientResponse.getPatientId());
```

### After
```java
PatientRegistrationResponse patientResponse = response.as(PatientRegistrationResponse.class);
assertTrue("Success should be true", patientResponse.getSuccess());
assertNotNull("Patient data should not be null", patientResponse.getData());
assertNotNull("Patient ID should not be null", patientResponse.getData().getId());
```

## Impact
- All 4 affected tests now correctly validate against the actual API response structure
- Tests will now pass when the patient registration API returns valid responses
- The `PatientRegistrationResponse` model remains unchanged (backward compatible)
- Only test assertions were updated to match reality

## Test Results
After this fix, tests will now:
- ✓ Properly validate the presence of patient data
- ✓ Correctly retrieve the patient ID from the nested `data` object
- ✓ Not fail on checking for non-existent top-level fields
- ✓ Validate the UUID format of the actual returned patient ID

## Validation
The fixes ensure that all assertions now check fields that actually exist in the API response:
- `patientResponse.getSuccess()` → ✓ Present at top level
- `patientResponse.getData()` → ✓ Present at top level
- `patientResponse.getData().getId()` → ✓ Present in data object
- `patientResponse.getData().getFirstName()` → ✓ Present in data object
- `patientResponse.getData().getStatus()` → ✓ Present in data object
- `patientResponse.getData().getCreatedAt()` → ✓ Present in data object

