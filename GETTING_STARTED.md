# Getting Started - Login API Testing

## ✅ Project Setup Checklist

### 1. Prerequisites
- [x] Java 23 installed
- [x] Maven installed
- [x] IDE (IntelliJ IDEA, Eclipse, VS Code) with Java support

### 2. Project Structure
- [x] `src/main/java/org/example/api/LoginApiClient.java` - Main API client
- [x] `src/main/java/org/example/models/LoginRequest.java` - Request model
- [x] `src/main/java/org/example/models/LoginResponse.java` - Response model
- [x] `src/main/java/org/example/config/ApiConfig.java` - Configuration
- [x] `src/main/java/org/example/LoginApiExample.java` - Usage example
- [x] `src/test/java/org/example/tests/LoginApiTest.java` - Test cases
- [x] `src/main/resources/logback.xml` - Logging configuration
- [x] `pom.xml` - Maven configuration

### 3. Dependencies
- [x] RestAssured 5.4.0
- [x] Gson 2.10.1
- [x] JUnit 4.13.2
- [x] SLF4J 2.0.7
- [x] Logback 1.4.11

## 🚀 Quick Start (5 Minutes)

### Step 1: Build the Project
```bash
cd C:\Users\Gajendra\IdeaProjects\V2_Api_Tests
mvn clean compile
```

### Step 2: Run Example
```bash
mvn exec:java -Dexec.mainClass="org.example.LoginApiExample"
```

### Step 3: Run Tests
```bash
mvn test
```

## 📝 Usage Examples

### Example 1: Basic Login
```java
import org.example.api.LoginApiClient;
import org.example.models.LoginResponse;

public class QuickTest {
    public static void main(String[] args) {
        LoginApiClient apiClient = new LoginApiClient();
        
        LoginResponse response = apiClient.login(
            "johndo",
            "Chikitsa@123",
            "b60fae41-3a86-4d4f-8ca9-8180a7d44e0e",
            "SMS Hospital",
            "8c094825-a89a-4b96-a4c7-7d6d06359dea"
        );
        
        System.out.println("Token: " + response.getToken());
        System.out.println("Email: " + response.getEmail());
    }
}
```

### Example 2: Using Configuration
```java
import org.example.api.LoginApiClient;
import org.example.config.ApiConfig;
import org.example.models.LoginResponse;

public class ConfiguredTest {
    public static void main(String[] args) {
        LoginApiClient apiClient = new LoginApiClient();
        
        LoginResponse response = apiClient.login(
            ApiConfig.getEmail(),
            ApiConfig.getPassword(),
            ApiConfig.getFacilityId(),
            ApiConfig.getFacilityName(),
            ApiConfig.getDeviceId()
        );
        
        System.out.println("Login successful!");
        System.out.println("Token: " + response.getToken());
    }
}
```

### Example 3: With Error Handling
```java
import org.example.api.LoginApiClient;
import org.example.models.LoginResponse;

public class ErrorHandlingTest {
    public static void main(String[] args) {
        try {
            LoginApiClient apiClient = new LoginApiClient();
            
            LoginResponse response = apiClient.login(
                "johndo",
                "Chikitsa@123",
                "b60fae41-3a86-4d4f-8ca9-8180a7d44e0e",
                "SMS Hospital",
                "8c094825-a89a-4b96-a4c7-7d6d06359dea"
            );
            
            System.out.println("✓ Login successful");
            System.out.println("Token: " + response.getToken());
            
        } catch (RuntimeException e) {
            System.err.println("✗ Login failed: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
```

## 📚 Documentation Files

| File | Purpose |
|------|---------|
| `README.md` | Comprehensive project documentation |
| `QUICK_REFERENCE.md` | Quick reference guide |
| `INTEGRATION_GUIDE.md` | Integration patterns and examples |
| `IMPLEMENTATION_SUMMARY.md` | Summary of what was created |
| `GETTING_STARTED.md` | This file - Quick start guide |

## 🔧 Configuration

### API Endpoint
```
Base URL: https://v2-services.chikitsa.dev/hospital-admin/api/v1
Endpoint: POST /auth/login
Full URL: https://v2-services.chikitsa.dev/hospital-admin/api/v1/auth/login
```

### Test Credentials
```
Email: johndo
Password: Chikitsa@123
Facility ID: b60fae41-3a86-4d4f-8ca9-8180a7d44e0e
Facility Name: SMS Hospital
Device ID: 8c094825-a89a-4b96-a4c7-7d6d06359dea
```

### Environment Variables (Optional)
```bash
# Set environment variables for sensitive credentials
$env:TEST_EMAIL = "johndo"
$env:TEST_PASSWORD = "Chikitsa@123"
$env:TEST_FACILITY_ID = "b60fae41-3a86-4d4f-8ca9-8180a7d44e0e"
$env:TEST_FACILITY_NAME = "SMS Hospital"
$env:TEST_DEVICE_ID = "8c094825-a89a-4b96-a4c7-7d6d06359dea"
```

Then use `ApiConfig.getEmail()`, `ApiConfig.getPassword()`, etc.

## 🧪 Running Tests

### Run All Tests
```bash
mvn test
```

### Run Specific Test Class
```bash
mvn test -Dtest=LoginApiTest
```

### Run Specific Test Method
```bash
mvn test -Dtest=LoginApiTest#testLoginWithParameters
```

### Run with Verbose Output
```bash
mvn test -DargLine="-Dorg.slf4j.simpleLogger.defaultLogLevel=debug"
```

## 📊 Expected Test Output

When running tests, you should see:
```
[INFO] Running org.example.tests.LoginApiTest
[INFO] testLoginWithLoginRequest - PASSED
[INFO] testLoginWithParameters - PASSED
[INFO] testLoginRaw - PASSED
[INFO] Tests run: 5, Failures: 0, Errors: 0
```

## 🎯 Common Tasks

### Task 1: Extract and Use Token
```java
LoginApiClient apiClient = new LoginApiClient();
LoginResponse response = apiClient.login("johndo", "Chikitsa@123", 
    "b60fae41-3a86-4d4f-8ca9-8180a7d44e0e", "SMS Hospital", "8c094825-a89a-4b96-a4c7-7d6d06359dea");

String token = response.getToken();
// Use token in subsequent API calls
String authHeader = "Bearer " + token;
```

### Task 2: Validate Response
```java
LoginApiClient apiClient = new LoginApiClient();
LoginResponse response = apiClient.login(...);

assert response.getToken() != null : "Token should not be null";
assert response.getEmail().equals("johndo") : "Email mismatch";
assert response.getFacilityId() != null : "Facility ID should not be null";
```

### Task 3: Handle Login Failure
```java
try {
    LoginResponse response = apiClient.login("invalid@email.com", "wrongpassword", ...);
} catch (RuntimeException e) {
    System.out.println("Login failed: " + e.getMessage());
    // Handle error appropriately
}
```

### Task 4: Access Response Data
```java
LoginResponse response = apiClient.login(...);

String userId = response.getUserId();
String email = response.getEmail();
String token = response.getToken();
String refreshToken = response.getRefreshToken();
Long expiresIn = response.getExpiresIn();
Integer statusCode = response.getStatusCode();
```

## 🐛 Troubleshooting

### Problem: Maven not found
**Solution**: Add Maven to system PATH or use full path to mvn.cmd

### Problem: Java version mismatch
**Solution**: Update pom.xml compiler source/target to your Java version
```xml
<maven.compiler.source>17</maven.compiler.source>
<maven.compiler.target>17</maven.compiler.target>
```

### Problem: Connection refused
**Solution**: 
1. Check if the API is accessible: `ping v2-services.chikitsa.dev`
2. Check internet connection
3. Verify VPN connection if required

### Problem: 401 Unauthorized
**Solution**: 
1. Verify credentials are correct
2. Check if the account is active
3. Check if facility_id is valid

### Problem: SSL/TLS error
**Solution**: Check if SSL certificate is valid or disable SSL verification (not recommended for production)

### Problem: Tests not running
**Solution**:
1. Run `mvn clean compile` first
2. Ensure test file is in `src/test/java` directory
3. Run `mvn clean test`

## 📖 Learn More

1. **RestAssured** - https://rest-assured.io/
2. **Gson** - https://github.com/google/gson
3. **JUnit** - https://junit.org/
4. **SLF4J** - https://www.slf4j.org/
5. **Logback** - https://logback.qos.ch/

## ✨ Next Steps

1. ✅ Review the code in the api, models, and config packages
2. ✅ Run the example: `mvn exec:java -Dexec.mainClass="org.example.LoginApiExample"`
3. ✅ Run the tests: `mvn test`
4. ✅ Integrate into your test framework (see INTEGRATION_GUIDE.md)
5. ✅ Use the token for subsequent API calls
6. ✅ Check logs in `logs/api-tests.log`

## 💡 Tips

- Use `LoginApiClient.loginRaw()` if you need detailed response assertions
- Use `ApiConfig` class to manage configuration centrally
- Check `logs/api-tests.log` for detailed debugging information
- Review `LoginApiExample.java` for practical usage patterns
- Check `LoginApiTest.java` for test examples

## 🤝 Integration Example

### Simple Integration in Your Test Suite
```java
public class MyTestSuite {
    private LoginApiClient apiClient;
    private String authToken;
    
    @Before
    public void setup() {
        apiClient = new LoginApiClient();
        LoginResponse response = apiClient.login(
            "johndo", "Chikitsa@123",
            "b60fae41-3a86-4d4f-8ca9-8180a7d44e0e", "SMS Hospital",
            "8c094825-a89a-4b96-a4c7-7d6d06359dea"
        );
        authToken = response.getToken();
    }
    
    @Test
    public void testMyApi() {
        RestAssured.given()
            .header("Authorization", "Bearer " + authToken)
            .get("https://v2-services.chikitsa.dev/hospital-admin/api/v1/hospitals")
            .then()
            .statusCode(200);
    }
}
```

## 📞 Support Resources

- Check logs: `logs/api-tests.log`
- Review examples: `src/main/java/org/example/LoginApiExample.java`
- Check tests: `src/test/java/org/example/tests/LoginApiTest.java`
- Read documentation: `README.md`, `QUICK_REFERENCE.md`, `INTEGRATION_GUIDE.md`

---

**Status**: ✅ Ready to use

Your Login API testing framework is complete and ready to use. Start with the quick start guide above and refer to the documentation files for more detailed information.

