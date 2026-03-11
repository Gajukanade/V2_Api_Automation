# Login-Logout API Documentation

## Base URL
```
https://v2-services.chikitsa.dev/hospital-admin/api/v1
```

## Authentication Endpoints

### 1. LOGIN Endpoint
**URL:** `POST /auth/login`

#### Request
```json
{
  "email": "nelson@gmail.com",
  "password": "Chikitsa@123",
  "facility_id": "b60fae41-3a86-4d4f-8ca9-8180a7d44e0e",
  "facility_name": "SMS Hospital",
  "deviceId": "8c094825-a89a-4b96-a4c7-7d6d06359dea"
}
```

#### Response (Success: 200/201)
```json
{
  "success": true,
  "accessToken": "eyJhbGciOiJIUzI1NiIs...",
  "refreshToken": "eyJhbGciOiJIUzI1NiIs...",
  "hospitalDetails": {
    "id": "b60fae41-3a86-4d4f-8ca9-8180a7d44e0e",
    "hospital_name": "SMS Hospital",
    "address_line_1": "123 Main Street",
    "city_name": "Jaipur",
    "state_name": "Rajasthan",
    "country_name": "India",
    "pincode": "302004"
  },
  "user": {
    "id": "91284656-0f5c-4579-a83d-4822102ed1f1",
    "username": "nelson",
    "emails": [
      {
        "email": "nelson@gmail.com",
        "is_primary": true
      }
    ],
    "first_name": "Nelson",
    "last_name": "Murdock",
    "phone": [
      {
        "phone_no": "7709093160",
        "country_code": "+91",
        "is_primary": true
      }
    ],
    "roles": [
      {
        "id": "f38594fe-0235-4a6b-9c0b-eb6012ba9442",
        "name": "Admin"
      },
      {
        "id": "f12db379-6d3d-4720-87b6-40f88552bf4c",
        "name": "Doctor"
      }
    ]
  },
  "sections": [
    {
      "id": "11929689-c1be-490d-9fda-39d39be8aa85",
      "name": "IAM",
      "actions": ["create", "read", "update", "status"]
    }
  ]
}
```

#### Error Response
```json
{
  "success": false,
  "message": "Invalid credentials",
  "error": "AUTHENTICATION_FAILED"
}
```

#### Status Codes
- `200 OK` - Login successful
- `201 Created` - Session created
- `400 Bad Request` - Invalid parameters
- `401 Unauthorized` - Invalid credentials
- `500 Internal Server Error` - Server error

---

### 2. LOGOUT Endpoint
**URL:** `POST /auth/logout`

#### Request Headers
```
Authorization: Bearer eyJhbGciOiJIUzI1NiIs...
Content-Type: application/json
```

#### Request Body
```json
{
  "facility_id": "b60fae41-3a86-4d4f-8ca9-8180a7d44e0e",
  "session_id": "0eebae43-9d8b-4c97-8020-85e5275ec574",
  "user_id": "91284656-0f5c-4579-a83d-4822102ed1f1"
}
```

#### Response (Success: 200/201)
```json
{
  "success": true,
  "message": "User logged out successfully",
  "data": null
}
```

#### Error Response
```json
{
  "success": false,
  "message": "Invalid token or session",
  "error": "LOGOUT_FAILED"
}
```

#### Status Codes
- `200 OK` - Logout successful
- `201 Created` - Session terminated
- `400 Bad Request` - Invalid parameters
- `401 Unauthorized` - Invalid or expired token
- `403 Forbidden` - Token invalid
- `500 Internal Server Error` - Server error

---

## Token Information

### JWT Token Structure
```
Header.Payload.Signature
```

#### Header (Base64 decoded)
```json
{
  "alg": "HS256",
  "typ": "JWT"
}
```

#### Payload (Base64 decoded)
```json
{
  "user_id": "91284656-0f5c-4579-a83d-4822102ed1f1",
  "first_name": "Nelson",
  "last_name": "Murdock",
  "username": "nelson",
  "roles": [
    {
      "id": "f38594fe-0235-4a6b-9c0b-eb6012ba9442",
      "name": "Admin"
    }
  ],
  "facility_id": "b60fae41-3a86-4d4f-8ca9-8180a7d44e0e",
  "facility_name": "SMS Hospital",
  "iat": 1773114570,
  "exp": 1773115470
}
```

### Token Lifetime
- **Access Token**: ~15-20 minutes (1200 seconds in example)
- **Refresh Token**: 7 days (604800 seconds)

### Token Usage
```
Authorization: Bearer <access_token>
```

---

## Error Codes

| Code | Description |
|------|-------------|
| AUTHENTICATION_FAILED | Invalid credentials provided |
| INVALID_TOKEN | Token is invalid or malformed |
| EXPIRED_TOKEN | Token has expired |
| INSUFFICIENT_PERMISSIONS | User lacks required permissions |
| LOGOUT_FAILED | Logout operation failed |
| SESSION_NOT_FOUND | Session doesn't exist |
| FACILITY_NOT_FOUND | Facility ID invalid |
| USER_INACTIVE | User account is inactive |

---

## Usage Examples

### cURL Login
```bash
curl -X POST https://v2-services.chikitsa.dev/hospital-admin/api/v1/auth/login \
  -H "Content-Type: application/json" \
  -d '{
    "email": "nelson@gmail.com",
    "password": "Chikitsa@123",
    "facility_id": "b60fae41-3a86-4d4f-8ca9-8180a7d44e0e",
    "facility_name": "SMS Hospital",
    "deviceId": "8c094825-a89a-4b96-a4c7-7d6d06359dea"
  }'
```

### cURL Logout
```bash
curl -X POST https://v2-services.chikitsa.dev/hospital-admin/api/v1/auth/logout \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer <access_token>" \
  -d '{
    "facility_id": "b60fae41-3a86-4d4f-8ca9-8180a7d44e0e",
    "session_id": "0eebae43-9d8b-4c97-8020-85e5275ec574",
    "user_id": "91284656-0f5c-4579-a83d-4822102ed1f1"
  }'
```

### Java/RestAssured Login
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

### Java/RestAssured Logout
```java
LogoutResponse response = client.logout(token, facilityId);
if (response.getSuccess()) {
    System.out.println("Logged out: " + response.getMessage());
}
```

---

## Security Best Practices

1. **Never expose tokens** in logs, URLs, or response bodies (except initially)
2. **Use HTTPS only** for all API calls
3. **Store tokens securely** (encrypted in memory, not in files)
4. **Implement token refresh** before expiration
5. **Logout on application exit** to invalidate sessions
6. **Validate token format** before using
7. **Check token expiration** before making requests
8. **Use secure headers** with API calls
9. **Implement rate limiting** on login attempts
10. **Monitor for suspicious activity** (multiple failed logins, etc.)

---

## Session Management

### Session Lifecycle
```
1. User logs in         -> Session created
2. Access token issued  -> Valid for ~15 minutes
3. Refresh token issued -> Valid for 7 days
4. User performs ops    -> Token in Authorization header
5. Token expires        -> Use refresh token to get new token
6. User logs out        -> Session terminated, tokens invalidated
7. All operations fail  -> User must login again
```

### Token Refresh (if supported)
```
POST /auth/refresh
Headers:
  Authorization: Bearer <refresh_token>

Response:
  new_access_token
  new_refresh_token (optional)
```

---

## Testing Checklist

- [ ] Login with valid credentials
- [ ] Login with invalid password
- [ ] Login with invalid email
- [ ] Verify token format (JWT)
- [ ] Extract user information from response
- [ ] Extract facility information from response
- [ ] Logout with valid token
- [ ] Logout with invalid token
- [ ] Multiple logout attempts
- [ ] Token reuse after logout (should fail)
- [ ] Session concurrent handling
- [ ] Token expiration handling
- [ ] Facility ID validation
- [ ] User role verification
- [ ] Session timeout handling

---

## Common Issues and Solutions

### Issue: Invalid Token Error
**Solution:** 
- Ensure token is passed in Authorization header
- Check if token has expired
- Verify token format (should be JWT with 3 parts)

### Issue: Unauthorized 401
**Solution:**
- Re-authenticate and get new token
- Check if user account is active
- Verify facility ID is correct

### Issue: Session Not Found
**Solution:**
- Ensure session ID is valid
- Check if session has expired
- Re-login to create new session

### Issue: Facility Not Found
**Solution:**
- Verify facility ID is correct
- Check if facility is active
- Ensure user has access to facility

---

## API Response Headers

```
Content-Type: application/json
Content-Length: 1234
Date: Tue, 10 Mar 2026 09:19:30 GMT
Set-Cookie: accessToken=...; Path=/
Set-Cookie: refreshToken=...; Path=/
X-Content-Type-Options: nosniff
X-Frame-Options: SAMEORIGIN
Strict-Transport-Security: max-age=31536000
```

---

## Rate Limiting

- **Login attempts**: 5 attempts per minute per IP
- **API calls**: 1000 requests per hour
- **Logout**: No limit

---

## Version Info

- **API Version**: v1
- **Date**: 2026-03-10
- **Status**: PRODUCTION

