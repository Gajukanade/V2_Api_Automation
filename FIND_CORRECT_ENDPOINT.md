# 🔍 How to Find the Correct Endpoint

## The Problem
Tests are failing with **404 Not Found** on `/patients/register`

## Solution: Try Different Endpoint Paths

### Step 1: Edit PatientRegistrationApiClient.java

Open this file:
```
C:\Users\Gajendra\IdeaProjects\V2_Api_Tests\src\main\java\org\example\api\PatientRegistrationApiClient.java
```

Find this line (around line 16):
```java
private static final String REGISTER_PATIENT_ENDPOINT = "/patients/register";
```

### Step 2: Try Each Path Below

Try each endpoint path ONE AT A TIME:

#### Path 1: Just /patients
```java
private static final String REGISTER_PATIENT_ENDPOINT = "/patients";
```

#### Path 2: Without "register"
```java
private static final String REGISTER_PATIENT_ENDPOINT = "/patient/create";
```

#### Path 3: Different base path
```java
private static final String BASE_URL = "https://v2-services.chikitsa.dev/api/v1";
private static final String REGISTER_PATIENT_ENDPOINT = "/patients/register";
```

#### Path 4: Simplified path
```java
private static final String REGISTER_PATIENT_ENDPOINT = "/patient";
```

#### Path 5: Alternative structure
```java
private static final String REGISTER_PATIENT_ENDPOINT = "/patients/new";
```

### Step 3: Compile and Test

After each change:

```bash
# Compile
mvn clean compile

# Run single test
mvn test -Dtest=PatientRegistrationTest#test01_RegisterPatientWithMinimumRequiredFields
```

### Step 4: Check Response

Look at the output for:

```
✅ 201 Created     → Endpoint found and working!
✅ 400 Bad Request → Endpoint found, validation error (expected)
❌ 404 Not Found   → Wrong path, try next one
❌ 401 Unauthorized → Authentication issue
❌ 500 Error       → Server error (ask backend)
```

---

## Likely Endpoints to Try (In Order)

Based on common API patterns, try these in this order:

```
1. /patients              (most likely)
2. /patients/register     (current - returns 404)
3. /patient/create        (alternative naming)
4. /patient/register      (singular)
5. /patient              (simplified)
6. /register-patient      (dash naming)
7. /patientRegister       (camelCase)
```

---

## Ask Backend Team

If none of these work, ask the backend team:

**Questions:**
1. "What is the endpoint for patient registration?"
2. "Does it support POST with JSON body?"
3. "What's the exact request/response format?"
4. "Are there any authentication headers needed?"
5. "Does the endpoint exist in the v1 API version?"

**Example Response Expected:**
> "The patient registration endpoint is `/api/v1/patients` (POST)"

---

## If BASE_URL is Wrong

The current BASE_URL is:
```java
private static final String BASE_URL = "https://v2-services.chikitsa.dev/hospital-admin/api/v1";
```

Try changing it:
```java
// Try 1: Remove hospital-admin
private static final String BASE_URL = "https://v2-services.chikitsa.dev/api/v1";

// Try 2: Different version
private static final String BASE_URL = "https://v2-services.chikitsa.dev/hospital-admin/api/v2";

// Try 3: Check if domain is different
private static final String BASE_URL = "https://api.chikitsa.dev/v1";
```

---

## Quick Test Script

Once you think you found the right path, run all tests:

```bash
cd C:\Users\Gajendra\IdeaProjects\V2_Api_Tests
mvn clean compile
mvn test -Dtest=PatientRegistrationTest
```

---

## Expected Results When Correct Endpoint Found

```
Tests run: 42
Successes: 40+
Failures: 0-2 (expected for negative test cases)
Success Rate: 95%+
```

---

## Still Not Found?

Check these:

1. **API is running?**
   ```bash
   curl https://v2-services.chikitsa.dev/hospital-admin/api/v1/auth/login
   ```

2. **Different protocol?**
   ```java
   private static final String BASE_URL = "http://v2-services.chikitsa.dev/hospital-admin/api/v1";
   ```

3. **Port number?**
   ```java
   private static final String BASE_URL = "https://v2-services.chikitsa.dev:8080/hospital-admin/api/v1";
   ```

4. **Check login endpoint works:**
   - If `/auth/login` works (status 200), then base URL is correct
   - Only patient endpoint is missing

---

## Summary

| Step | Action | Expected Result |
|------|--------|-----------------|
| 1 | Change endpoint | Code compiles |
| 2 | Run test | See status code |
| 3 | Check code | 201, 400, or 404 |
| 4 | If 201/400 | ✅ Found it! |
| 5 | If 404 | Try next endpoint |

**Good luck! 🔍**

