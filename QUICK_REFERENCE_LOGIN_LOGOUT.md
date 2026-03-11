# Quick Reference: Login-Logout Automation

## 🚀 Quick Start

### 1. Login
```java
LoginApiClient client = new LoginApiClient();
LoginResponse response = client.login(
    "nelson@gmail.com", 
    "Chikitsa@123",
    "b60fae41-3a86-4d4f-8ca9-8180a7d44e0e",
    "SMS Hospital",
    "8c094825-a89a-4b96-a4c7-7d6d06359dea"
);
String token = response.getToken();
```

### 2. Use Token
```java
// Add token to requests
headers.put("Authorization", "Bearer " + token);
```

### 3. Logout
```java
LogoutResponse logout = client.logout(token, facilityId);
System.out.println(logout.getSuccess()); // true
```

---

## 📦 Files Created

| File | Type | Purpose |
|------|------|---------|
| LogoutRequest.java | Model | Logout request payload |
| LogoutResponse.java | Model | Logout response payload |
| LoginApiClient.java | Client | API client (UPDATED) |
| LoginLogoutFlowTest.java | Test | 15 test cases |
| LoginLogoutAutomation.java | Script | Automated demonstration |
| LOGIN_LOGOUT_AUTOMATION.md | Docs | Overview and usage |
| LOGIN_LOGOUT_API_DOCUMENTATION.md | Docs | Complete API docs |

---

## 🔑 Key Methods

### Login Methods
```java
// Standard login
LoginResponse login(LoginRequest request)

// Convenience method
LoginResponse login(String email, String password, String facilityId, 
                   String facilityName, String deviceId)

// Raw response
Response loginRaw(LoginRequest request)
```

### Logout Methods
```java
// Standard logout
LogoutResponse logout(String accessToken, LogoutRequest request)

// Convenience - facility ID only
LogoutResponse logout(String accessToken, String facilityId)

// Full logout with all params
LogoutResponse logout(String accessToken, String facilityId, 
                     String sessionId, String userId)

// Raw response
Response logoutRaw(String accessToken, LogoutRequest request)
```

---

## 🧪 Test Commands

```bash
# Run all tests
mvn clean test -Dtest=LoginLogoutFlowTest

# Run specific test
mvn clean test -Dtest=LoginLogoutFlowTest#test01_LoginWithValidCredentials

# Run automation
mvn exec:java -Dexec.mainClass="org.example.LoginLogoutAutomation"

# Compile only
mvn clean compile
```

---

## 📊 Test Coverage

| Category | Tests | Coverage |
|----------|-------|----------|
| Login | 5 | Valid creds, tokens, info, errors |
| Logout | 5 | Valid token, invalid token, null, etc |
| Flow | 5 | Complete cycle, concurrent, lifecycle |
| **Total** | **15** | **Comprehensive** |

---

## 🔐 API Endpoints

| Endpoint | Method | Purpose |
|----------|--------|---------|
| /auth/login | POST | Authenticate user |
| /auth/logout | POST | End session |

---

## 📝 Response Models

### LoginResponse
```java
boolean success
String token (accessToken)
String refreshToken
LoginResponse.User user
  - id, username, firstName, lastName, emails, roles
LoginResponse.HospitalDetails hospitalDetails
  - id, hospitalName, address, city, state, country
```

### LogoutResponse
```java
boolean success
String message
Object data (null)
```

---

## 🛡️ Security Notes

1. **Never log tokens** in production
2. **Always use HTTPS** for API calls
3. **Store tokens securely** (encrypted memory)
4. **Logout before exit** to invalidate tokens
5. **Check token expiration** before requests

---

## ❌ Common Errors & Fixes

| Error | Cause | Fix |
|-------|-------|-----|
| Invalid token | Expired token | Re-login to get new token |
| 401 Unauthorized | Missing token | Add Authorization header |
| AUTHENTICATION_FAILED | Wrong password | Check credentials |
| SESSION_NOT_FOUND | Session expired | Re-login |
| FACILITY_NOT_FOUND | Wrong facility ID | Verify facility ID |

---

## 📚 Documentation

- **LOGIN_LOGOUT_AUTOMATION.md** - Full overview
- **LOGIN_LOGOUT_API_DOCUMENTATION.md** - Detailed API docs
- **IMPLEMENTATION_COMPLETE.md** - Implementation summary

---

## ✅ Verification

```
✅ Code compiles
✅ Tests execute
✅ All features implemented
✅ Documentation complete
✅ Ready for production
```

---

## 📞 Getting Help

1. Check the test cases in `LoginLogoutFlowTest.java`
2. Review API docs in `LOGIN_LOGOUT_API_DOCUMENTATION.md`
3. Run automation example: `LoginLogoutAutomation.java`
4. Check logs for detailed error messages

---

## 🎯 Next Steps

1. ✅ Copy credentials to your config
2. ✅ Run tests: `mvn clean test -Dtest=LoginLogoutFlowTest`
3. ✅ Integrate into your project
4. ✅ Customize as needed
5. ✅ Deploy to production

---

## 📌 Remember

- Always logout before application exit
- Handle token expiration gracefully
- Implement proper error handling
- Log important events (not tokens)
- Test all scenarios thoroughly

