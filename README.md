# Hospital Admin Login API Test Suite

This project contains comprehensive API testing code for the Hospital Admin Login API endpoint.

## Project Structure

```
V2_Api_Tests/
├── src/
│   ├── main/
│   │   ├── java/org/example/
│   │   │   ├── api/
│   │   │   │   └── LoginApiClient.java          # Main API client
│   │   │   ├── models/
│   │   │   │   ├── LoginRequest.java            # Request model
│   │   │   │   └── LoginResponse.java           # Response model
│   │   │   ├── LoginApiExample.java             # Example usage
│   │   │   └── Main.java                        # Main entry point
│   │   └── resources/
│   │       └── logback.xml                      # Logging configuration
│   └── test/
│       └── java/org/example/tests/
│           └── LoginApiTest.java                # Test cases
├── pom.xml                                      # Maven configuration
└── README.md                                    # This file
```

## API Details

**Base URL:** `https://v2-services.chikitsa.dev/hospital-admin/api/v1`

**Endpoint:** `POST /auth/login`

**Full URL:** `https://v2-services.chikitsa.dev/hospital-admin/api/v1/auth/login`

### Request Payload

```json
{
  "email": "johndo",
  "password": "Chikitsa@123",
  "facility_id": "b60fae41-3a86-4d4f-8ca9-8180a7d44e0e",
  "facility_name": "SMS Hospital",
  "deviceId": "8c094825-a89a-4b96-a4c7-7d6d06359dea"
}
```

### Response Payload

```json
{
  "user_id": "...",
  "email": "johndo",
  "token": "...",
  "refresh_token": "...",
  "facility_id": "b60fae41-3a86-4d4f-8ca9-8180a7d44e0e",
  "facility_name": "SMS Hospital",
  "expires_in": 3600,
  "message": "Login successful",
  "status_code": 200
}
```

## Classes

### LoginRequest
Model class for the login request payload with the following fields:
- `email` - User email address
- `password` - User password
- `facility_id` - ID of the facility
- `facility_name` - Name of the facility
- `deviceId` - Device identifier

### LoginResponse
Model class for the login response with the following fields:
- `userId` - User ID returned by the server
- `email` - User email address
- `token` - Authentication token
- `refreshToken` - Refresh token
- `facilityId` - Facility ID
- `facilityName` - Facility name
- `expiresIn` - Token expiration time in seconds
- `message` - Response message
- `statusCode` - HTTP status code

### LoginApiClient
Main API client class with the following methods:
- `login(LoginRequest)` - Perform login with LoginRequest object
- `login(String email, String password, String facilityId, String facilityName, String deviceId)` - Perform login with parameters
- `loginRaw(LoginRequest)` - Get raw Response object from RestAssured
- `getBaseUrl()` - Get the base URL
- `getLoginEndpoint()` - Get the login endpoint

## Usage Examples

### Example 1: Using LoginApiClient with LoginRequest object

```java
LoginApiClient apiClient = new LoginApiClient();

LoginRequest loginRequest = new LoginRequest(
    "johndo",
    "Chikitsa@123",
    "b60fae41-3a86-4d4f-8ca9-8180a7d44e0e",
    "SMS Hospital",
    "8c094825-a89a-4b96-a4c7-7d6d06359dea"
);

LoginResponse response = apiClient.login(loginRequest);
String token = response.getToken();
System.out.println("Token: " + token);
```

### Example 2: Using LoginApiClient with parameters

```java
LoginApiClient apiClient = new LoginApiClient();

LoginResponse response = apiClient.login(
    "johndo",
    "Chikitsa@123",
    "b60fae41-3a86-4d4f-8ca9-8180a7d44e0e",
    "SMS Hospital",
    "8c094825-a89a-4b96-a4c7-7d6d06359dea"
);

System.out.println("Email: " + response.getEmail());
System.out.println("Token: " + response.getToken());
System.out.println("Expires In: " + response.getExpiresIn());
```

### Example 3: Getting raw response

```java
LoginApiClient apiClient = new LoginApiClient();
LoginRequest loginRequest = new LoginRequest(...);

Response rawResponse = apiClient.loginRaw(loginRequest);
System.out.println("Status Code: " + rawResponse.getStatusCode());
System.out.println("Body: " + rawResponse.getBody().asString());
```

## Running Tests

### Run all tests
```bash
mvn test
```

### Run specific test class
```bash
mvn test -Dtest=LoginApiTest
```

### Run specific test method
```bash
mvn test -Dtest=LoginApiTest#testLoginWithParameters
```

## Running the Example

```bash
mvn exec:java -Dexec.mainClass="org.example.LoginApiExample"
```

## Dependencies

- **RestAssured** 5.4.0 - For API testing
- **Gson** 2.10.1 - For JSON serialization/deserialization
- **JUnit** 4.13.2 - For unit testing
- **SLF4J** 2.0.7 - For logging abstraction
- **Logback** 1.4.11 - For logging implementation

## Configuration

### Logback Configuration
Logging is configured in `src/main/resources/logback.xml` with:
- Console appender for standard output
- File appender for writing logs to `logs/api-tests.log`
- DEBUG level for org.example and io.restassured packages
- INFO level for other packages

### API Endpoints
To change the API base URL, edit the `BASE_URL` constant in `LoginApiClient.java`:

```java
private static final String BASE_URL = "https://v2-services.chikitsa.dev/hospital-admin/api/v1";
```

## Error Handling

The API client includes comprehensive error handling:
- Logs all requests and responses at DEBUG level
- Throws RuntimeException with descriptive messages on failure
- Includes detailed error logging for troubleshooting

## Notes

- The project uses Java 23 as per the pom.xml configuration
- All API communication uses JSON content type
- Token-based authentication is used for subsequent API calls
- Logs are written to both console and file (`logs/api-tests.log`)

## Future Enhancements

- Add support for proxy configurations
- Implement retry logic for failed requests
- Add request/response validation schemas
- Implement token refresh mechanism
- Add more comprehensive test cases for edge cases

