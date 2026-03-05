# Quick Reference Guide - Login API

## Quick Start

### 1. Basic Login
```java
LoginApiClient apiClient = new LoginApiClient();
LoginResponse response = apiClient.login(
    "johndo",
    "Chikitsa@123",
    "b60fae41-3a86-4d4f-8ca9-8180a7d44e0e",
    "SMS Hospital",
    "8c094825-a89a-4b96-a4c7-7d6d06359dea"
);

// Use the token for subsequent API calls
String token = response.getToken();
```

### 2. Using with RestAssured
```java
LoginApiClient apiClient = new LoginApiClient();
Response rawResponse = apiClient.loginRaw(loginRequest);

// Chain assertions
rawResponse.then()
    .statusCode(200)
    .body("token", notNullValue())
    .body("email", equalTo("johndo"));
```

### 3. Error Handling
```java
try {
    LoginResponse response = apiClient.login(email, password, facilityId, facilityName, deviceId);
    System.out.println("Login successful: " + response.getToken());
} catch (RuntimeException e) {
    System.err.println("Login failed: " + e.getMessage());
}
```

## API Endpoint Details

| Property | Value |
|----------|-------|
| **Method** | POST |
| **Base URL** | https://v2-services.chikitsa.dev/hospital-admin/api/v1 |
| **Endpoint** | /auth/login |
| **Full URL** | https://v2-services.chikitsa.dev/hospital-admin/api/v1/auth/login |
| **Content-Type** | application/json |

## Request Fields

| Field | Type | Required | Description |
|-------|------|----------|-------------|
| email | String | Yes | User email address |
| password | String | Yes | User password |
| facility_id | String | Yes | UUID of the facility |
| facility_name | String | Yes | Name of the facility |
| deviceId | String | Yes | Device identifier |

## Response Fields

| Field | Type | Description |
|-------|------|-------------|
| user_id | String | Unique user identifier |
| email | String | User email address |
| token | String | JWT authentication token |
| refresh_token | String | Token for refreshing authentication |
| facility_id | String | Facility ID from request |
| facility_name | String | Facility name from request |
| expires_in | Long | Token expiration time in seconds |
| message | String | Response message |
| status_code | Integer | HTTP status code |

## Test Credentials

```
Email: johndo
Password: Chikitsa@123
Facility ID: b60fae41-3a86-4d4f-8ca9-8180a7d44e0e
Facility Name: SMS Hospital
Device ID: 8c094825-a89a-4b96-a4c7-7d6d06359dea
```

## Maven Commands

| Command | Purpose |
|---------|---------|
| `mvn clean compile` | Compile the project |
| `mvn test` | Run all tests |
| `mvn test -Dtest=LoginApiTest` | Run specific test class |
| `mvn clean install` | Build and install the project |
| `mvn exec:java -Dexec.mainClass="org.example.LoginApiExample"` | Run the example |

## File Locations

```
src/main/java/org/example/
├── api/LoginApiClient.java           # Main API client class
├── models/LoginRequest.java          # Request model
├── models/LoginResponse.java         # Response model
├── LoginApiExample.java              # Usage example
└── Main.java                         # Entry point

src/test/java/org/example/tests/
└── LoginApiTest.java                 # Test cases

src/main/resources/
└── logback.xml                       # Logging configuration
```

## Common Patterns

### Pattern 1: Login and Extract Token
```java
LoginApiClient apiClient = new LoginApiClient();
LoginResponse response = apiClient.login("johndo", "Chikitsa@123", "b60fae41-3a86-4d4f-8ca9-8180a7d44e0e", "SMS Hospital", "8c094825-a89a-4b96-a4c7-7d6d06359dea");
String token = response.getToken();
// Use token for next API call
```

### Pattern 2: Login with Validation
```java
LoginApiClient apiClient = new LoginApiClient();
LoginResponse response = apiClient.login(loginRequest);
assert response.getToken() != null : "Token should not be null";
assert response.getEmail().equals("johndo") : "Email mismatch";
```

### Pattern 3: Login with Custom Exception Handling
```java
LoginApiClient apiClient = new LoginApiClient();
try {
    LoginResponse response = apiClient.login(loginRequest);
    // Process response
} catch (RuntimeException e) {
    if (e.getMessage().contains("401")) {
        // Handle unauthorized
    } else if (e.getMessage().contains("400")) {
        // Handle bad request
    }
}
```

## Logging

Logs are written to:
- **Console**: Standard output with INFO level and above
- **File**: `logs/api-tests.log` with all levels

To enable DEBUG logging for API calls, check `src/main/resources/logback.xml`

## Support

For issues or questions:
1. Check the logs in `logs/api-tests.log`
2. Review the test cases in `LoginApiTest.java`
3. Refer to the example in `LoginApiExample.java`

