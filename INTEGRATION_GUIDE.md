# Integration Guide - Using LoginApiClient in Your Tests

## Overview
This guide shows how to integrate the LoginApiClient into your existing test frameworks and use it for API testing.

## Method 1: Standalone Usage

### Simple Login
```java
import org.example.api.LoginApiClient;
import org.example.models.LoginResponse;

public class MyApiTests {
    public void testMyApi() {
        // Create API client
        LoginApiClient loginApiClient = new LoginApiClient();
        
        // Perform login
        LoginResponse response = loginApiClient.login(
            "johndo",
            "Chikitsa@123",
            "b60fae41-3a86-4d4f-8ca9-8180a7d44e0e",
            "SMS Hospital",
            "8c094825-a89a-4b96-a4c7-7d6d06359dea"
        );
        
        // Extract token for subsequent calls
        String token = response.getToken();
        String userId = response.getUserId();
        
        // Use token in next API call
        // ...rest of your test logic...
    }
}
```

## Method 2: With Configuration Management

### Using ApiConfig for Centralized Configuration
```java
import org.example.api.LoginApiClient;
import org.example.config.ApiConfig;
import org.example.models.LoginResponse;

public class ConfiguredApiTests {
    private LoginApiClient loginApiClient;
    
    public void setup() {
        loginApiClient = new LoginApiClient();
    }
    
    public void testWithConfiguration() {
        // Use configuration from ApiConfig
        LoginResponse response = loginApiClient.login(
            ApiConfig.getEmail(),
            ApiConfig.getPassword(),
            ApiConfig.getFacilityId(),
            ApiConfig.getFacilityName(),
            ApiConfig.getDeviceId()
        );
        
        System.out.println("Token: " + response.getToken());
    }
}
```

## Method 3: Token Management Wrapper

### Create a Helper Class for Token Management
```java
import org.example.api.LoginApiClient;
import org.example.models.LoginResponse;
import org.example.config.ApiConfig;

public class AuthHelper {
    private LoginApiClient apiClient;
    private LoginResponse currentSession;
    
    public AuthHelper() {
        this.apiClient = new LoginApiClient();
    }
    
    public String getAuthToken() {
        if (currentSession == null) {
            login();
        }
        return currentSession.getToken();
    }
    
    public void login() {
        currentSession = apiClient.login(
            ApiConfig.getEmail(),
            ApiConfig.getPassword(),
            ApiConfig.getFacilityId(),
            ApiConfig.getFacilityName(),
            ApiConfig.getDeviceId()
        );
    }
    
    public void logout() {
        currentSession = null;
    }
    
    public LoginResponse getSession() {
        return currentSession;
    }
}
```

### Using the Auth Helper
```java
public class ApiTests {
    private AuthHelper authHelper = new AuthHelper();
    
    public void testWithAuth() {
        // Get token
        String token = authHelper.getAuthToken();
        
        // Use token in RestAssured calls
        RestAssured.given()
            .header("Authorization", "Bearer " + token)
            .get("https://api.example.com/hospital")
            .then()
            .statusCode(200);
    }
}
```

## Method 4: Integration with RestAssured

### Using Raw Response from LoginApiClient
```java
import io.restassured.response.Response;
import org.example.api.LoginApiClient;
import org.example.models.LoginRequest;

public class RestAssuredIntegration {
    private LoginApiClient apiClient = new LoginApiClient();
    
    public void testLoginWithRestAssuredValidations() {
        LoginRequest loginRequest = new LoginRequest(
            "johndo",
            "Chikitsa@123",
            "b60fae41-3a86-4d4f-8ca9-8180a7d44e0e",
            "SMS Hospital",
            "8c094825-a89a-4b96-a4c7-7d6d06359dea"
        );
        
        // Get raw response for detailed assertions
        Response response = apiClient.loginRaw(loginRequest);
        
        // Validate response with RestAssured
        response.then()
            .statusCode(200)
            .body("token", notNullValue())
            .body("email", equalTo("johndo"))
            .body("facility_id", equalTo("b60fae41-3a86-4d4f-8ca9-8180a7d44e0e"))
            .body("expires_in", greaterThan(0));
    }
}
```

## Method 5: Custom Login Handler with Error Recovery

```java
import org.example.api.LoginApiClient;
import org.example.models.LoginResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RobustAuthHandler {
    private static final Logger logger = LoggerFactory.getLogger(RobustAuthHandler.class);
    private LoginApiClient apiClient = new LoginApiClient();
    private LoginResponse cachedToken;
    private int maxRetries = 3;
    
    public LoginResponse loginWithRetry() {
        for (int attempt = 1; attempt <= maxRetries; attempt++) {
            try {
                logger.info("Login attempt {}/{}", attempt, maxRetries);
                cachedToken = apiClient.login(
                    "johndo",
                    "Chikitsa@123",
                    "b60fae41-3a86-4d4f-8ca9-8180a7d44e0e",
                    "SMS Hospital",
                    "8c094825-a89a-4b96-a4c7-7d6d06359dea"
                );
                logger.info("Login successful on attempt {}", attempt);
                return cachedToken;
            } catch (RuntimeException e) {
                logger.warn("Login failed on attempt {}: {}", attempt, e.getMessage());
                if (attempt == maxRetries) {
                    logger.error("All login attempts failed");
                    throw e;
                }
                // Wait before retry
                try {
                    Thread.sleep(1000 * attempt); // Exponential backoff
                } catch (InterruptedException ie) {
                    Thread.currentThread().interrupt();
                    throw new RuntimeException("Login retry interrupted", ie);
                }
            }
        }
        throw new RuntimeException("Login failed after " + maxRetries + " attempts");
    }
    
    public String getValidToken() {
        if (cachedToken == null) {
            loginWithRetry();
        }
        return cachedToken.getToken();
    }
}
```

## Method 6: Test Base Class for Inheritance

```java
import org.junit.Before;
import org.example.api.LoginApiClient;
import org.example.models.LoginResponse;

public class ApiTestBase {
    protected LoginApiClient apiClient;
    protected LoginResponse loginResponse;
    protected String authToken;
    
    @Before
    public void setUp() {
        apiClient = new LoginApiClient();
        performLogin();
    }
    
    protected void performLogin() {
        loginResponse = apiClient.login(
            "johndo",
            "Chikitsa@123",
            "b60fae41-3a86-4d4f-8ca9-8180a7d44e0e",
            "SMS Hospital",
            "8c094825-a89a-4b96-a4c7-7d6d06359dea"
        );
        authToken = loginResponse.getToken();
    }
    
    protected String getBearerToken() {
        return "Bearer " + authToken;
    }
    
    protected String getSimpleToken() {
        return authToken;
    }
}

// Usage
public class HospitalApiTests extends ApiTestBase {
    @Test
    public void testHospitalsList() {
        RestAssured.given()
            .header("Authorization", getBearerToken())
            .get("/hospitals")
            .then()
            .statusCode(200);
    }
}
```

## Method 7: Data-Driven Tests

```java
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.example.api.LoginApiClient;
import java.util.Arrays;
import java.util.Collection;

@RunWith(Parameterized.class)
public class DataDrivenLoginTests {
    private String email;
    private String password;
    private String facilityId;
    private String facilityName;
    private String deviceId;
    private LoginApiClient apiClient;
    
    @Parameterized.Parameters
    public static Collection<Object[]> data() {
        return Arrays.asList(new Object[][] {
            { "johndo", "Chikitsa@123", "b60fae41-3a86-4d4f-8ca9-8180a7d44e0e", "SMS Hospital", "8c094825-a89a-4b96-a4c7-7d6d06359dea" },
            // Add more test data sets here
        });
    }
    
    public DataDrivenLoginTests(String email, String password, String facilityId, String facilityName, String deviceId) {
        this.email = email;
        this.password = password;
        this.facilityId = facilityId;
        this.facilityName = facilityName;
        this.deviceId = deviceId;
        this.apiClient = new LoginApiClient();
    }
    
    @Test
    public void testLogin() {
        var response = apiClient.login(email, password, facilityId, facilityName, deviceId);
        assert response.getToken() != null : "Token should not be null";
    }
}
```

## Best Practices

1. **Never hardcode credentials** - Use environment variables or configuration files
2. **Reuse authentication** - Store tokens and reuse them in the same test session
3. **Handle token expiration** - Implement token refresh logic
4. **Log everything** - Use SLF4J for comprehensive logging
5. **Use dependency injection** - Make your tests more maintainable
6. **Create helper classes** - Abstract away API calls in helper methods
7. **Handle errors gracefully** - Implement proper exception handling
8. **Test edge cases** - Test with invalid credentials, network errors, etc.

## Environment Variables

Set these environment variables for sensitive data:

```bash
# On Windows PowerShell
$env:TEST_EMAIL = "johndo"
$env:TEST_PASSWORD = "Chikitsa@123"
$env:TEST_FACILITY_ID = "b60fae41-3a86-4d4f-8ca9-8180a7d44e0e"
$env:TEST_FACILITY_NAME = "SMS Hospital"
$env:TEST_DEVICE_ID = "8c094825-a89a-4b96-a4c7-7d6d06359dea"
$env:API_BASE_URL = "https://v2-services.chikitsa.dev/hospital-admin/api/v1"
```

Then use `ApiConfig.getEmail()`, etc. instead of hardcoded values.

## Troubleshooting

### Issue: Login fails with 401 Unauthorized
**Solution**: Check credentials are correct in ApiConfig or your test

### Issue: Connection timeout
**Solution**: Verify the API base URL and network connectivity

### Issue: JSON parsing error
**Solution**: Check that the response JSON structure matches LoginResponse model

### Issue: Token not found in response
**Solution**: Verify the API response includes the "token" field

## Support

- Check logs in `logs/api-tests.log` for detailed error information
- Review test examples in `LoginApiTest.java`
- Refer to the API documentation in `README.md`

