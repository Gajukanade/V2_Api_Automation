# Automated Login-Logout Flow Implementation

## Overview
Complete automation of the login and logout flow for the Hospital Admin API with comprehensive test coverage.

## Files Created/Modified

### 1. Models
- **LogoutRequest.java** - Request model for logout endpoint
- **LogoutResponse.java** - Response model for logout endpoint

### 2. API Client
- **LoginApiClient.java** (UPDATED)
  - Added logout methods
  - Added LogoutRequest/LogoutResponse imports
  - Added LOGOUT_ENDPOINT constant
  - New methods:
    - `logout(String accessToken, LogoutRequest logoutRequest)` - Main logout method
    - `logout(String accessToken, String facilityId)` - Simplified logout with facility ID
    - `logout(String accessToken, String facilityId, String sessionId, String userId)` - Full logout with all params
    - `logoutRaw(String accessToken, LogoutRequest logoutRequest)` - Raw response logout
    - `getLogoutEndpoint()` - Get logout endpoint constant

### 3. Test Suite
- **LoginLogoutFlowTest.java** - Comprehensive test suite with 15+ test cases
  - Test 1: Login with valid credentials
  - Test 2: Verify token format (JWT)
  - Test 3: Verify user information
  - Test 4: Verify hospital details
  - Test 5: Login with invalid credentials
  - Test 6: Logout with valid token
  - Test 7: Logout with facility ID only
  - Test 8: Logout with invalid token
  - Test 9: Logout without token
  - Test 10: Multiple logout attempts
  - Test 11: Complete login-logout flow
  - Test 12: Concurrent login-logout sessions
  - Test 13: Reuse token after logout
  - Test 14: Session expiry handling
  - Test 15: Full authentication lifecycle

### 4. Automation Scripts
- **LoginLogoutAutomation.java** - Automated login-logout flow example
  - Main automation with phases
  - Step-by-step demonstration method
  - Comprehensive logging and reporting

## Key Features

### Login Flow
```
1. Initialize LoginApiClient
2. Create LoginRequest with credentials
3. Call login(loginRequest)
4. Receive LoginResponse with:
   - Access Token (JWT)
   - Refresh Token
   - User information (ID, username, email, roles)
   - Hospital/Facility details
```

### Logout Flow
```
1. Create LogoutRequest with facility ID (and optional session/user ID)
2. Call logout(accessToken, logoutRequest)
3. Receive LogoutResponse with:
   - Success flag
   - Message
4. Token is invalidated on server
```

### Session Lifecycle
```
[Login] -> [Use Token] -> [Operations] -> [Logout] -> [Session Terminated]
```

## API Endpoints
- **Login**: `POST /hospital-admin/api/v1/auth/login`
- **Logout**: `POST /hospital-admin/api/v1/auth/logout`

## Credentials Used
- Email: nelson@gmail.com
- Password: Chikitsa@123
- Facility ID: b60fae41-3a86-4d4f-8ca9-8180a7d44e0e
- Facility Name: SMS Hospital
- Device ID: 8c094825-a89a-4b96-a4c7-7d6d06359dea

## Running the Tests

### Run all login-logout tests:
```bash
mvn clean test -Dtest=LoginLogoutFlowTest
```

### Run the automation example:
```bash
mvn clean compile
mvn exec:java -Dexec.mainClass="org.example.LoginLogoutAutomation"
```

### Run specific test:
```bash
mvn clean test -Dtest=LoginLogoutFlowTest#test01_LoginWithValidCredentials
```

## Usage Examples

### Simple Login
```java
LoginApiClient client = new LoginApiClient();
LoginResponse response = client.login("email@example.com", "password", facilityId, facilityName, deviceId);
String token = response.getToken();
```

### Simple Logout
```java
LogoutResponse response = client.logout(token, facilityId);
System.out.println("Logout successful: " + response.getSuccess());
```

### Complete Flow
```java
LoginApiClient client = new LoginApiClient();

// Login
LoginResponse login = client.login("email@example.com", "password", facilityId, facilityName, deviceId);
String token = login.getToken();

// Use token for operations...

// Logout
LogoutResponse logout = client.logout(token, facilityId);
```

## Test Coverage

### Login Tests (5 tests)
- Valid credentials authentication
- JWT token format validation
- User information retrieval
- Hospital details retrieval
- Invalid credentials rejection

### Logout Tests (5 tests)
- Successful logout with valid token
- Logout with facility ID only
- Invalid token rejection
- Null token handling
- Multiple logout attempts

### Flow Tests (5 tests)
- Complete login-logout cycle
- Concurrent session management
- Token reuse prevention
- Session expiry handling
- Full authentication lifecycle

## Error Handling

### Login Failures
- Invalid credentials: RuntimeException
- Network errors: Exception propagation
- Status code validation (200/201)

### Logout Failures
- Invalid token: RuntimeException
- Invalid facility ID: RuntimeException
- Status code validation (200/201)

## Logging

All operations are logged using SLF4J with categories:
- INFO: Operation start/completion
- DEBUG: Request/response details
- ERROR: Failures and exceptions
- WARN: Warnings and edge cases

## Security Notes

1. **Tokens are JWT** - Format: header.payload.signature
2. **Authorization Header** - Tokens sent as `Authorization: Bearer <token>`
3. **Token Invalidation** - Tokens are server-invalidated on logout
4. **Session Management** - Each login creates a new session
5. **Facility Scoped** - Logout scoped to facility ID

## Dependencies

- io.restassured: REST API testing
- com.google.gson: JSON serialization
- org.slf4j: Logging
- junit: Unit testing

## Status

✅ **COMPLETE AND TESTED**

All components are implemented, tested, and ready for use.

