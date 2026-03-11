# Profile API Documentation

## Overview
The Profile API (`/profile`) endpoint allows authenticated users to retrieve their complete profile information including personal details, email addresses, and assigned roles.

## API Details

### Endpoint
- **URL**: `/profile`
- **Base URL**: `https://v2-services.chikitsa.dev/hospital-admin/api/v1`
- **Full URL**: `https://v2-services.chikitsa.dev/hospital-admin/api/v1/profile`
- **HTTP Method**: `GET`
- **Authentication**: Required (Bearer Token)

### Request Headers
```
Content-Type: application/json
Authorization: Bearer <access_token>
```

### Request Example
```bash
curl -X GET \
  'https://v2-services.chikitsa.dev/hospital-admin/api/v1/profile' \
  -H 'Authorization: Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...'
```

## Response Structure

### Success Response (200/201)
```json
{
  "success": true,
  "data": {
    "user": {
      "id": "91284656-0f5c-4579-a83d-4822102ed1f1",
      "username": "nelson",
      "first_name": "Nelson",
      "last_name": "Murdock",
      "emails": [
        {
          "id": "eb8f57d4-e9f0-4384-8ab7-4b4e30559821",
          "email": "update@gmai.com",
          "is_primary": false
        },
        {
          "email": "nelson@gmail.com",
          "is_primary": true
        }
      ],
      "is_active": true,
      "created_at": "2026-03-09T05:24:56.404Z",
      "updated_at": "2026-03-09T05:24:56.404Z",
      "email": "nelson@gmail.com",
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
    }
  }
}
```

### Response Fields Explanation

#### Root Level
| Field | Type | Description |
|-------|------|-------------|
| `success` | Boolean | Indicates if the request was successful |
| `data` | Object | Contains the user profile data |

#### User Object
| Field | Type | Description |
|-------|------|-------------|
| `id` | String | Unique identifier for the user |
| `username` | String | Username of the user |
| `first_name` | String | First name of the user |
| `last_name` | String | Last name of the user |
| `emails` | Array | List of email addresses associated with the user |
| `is_active` | Boolean | Indicates if the user account is active |
| `created_at` | String | ISO 8601 timestamp of account creation |
| `updated_at` | String | ISO 8601 timestamp of last update |
| `email` | String | Primary email address |
| `roles` | Array | List of roles assigned to the user |

#### Email Object
| Field | Type | Description |
|-------|------|-------------|
| `id` | String | Unique identifier for the email |
| `email` | String | Email address |
| `is_primary` | Boolean | Indicates if this is the primary email |

#### Role Object
| Field | Type | Description |
|-------|------|-------------|
| `id` | String | Unique identifier for the role |
| `name` | String | Name of the role (e.g., "Admin", "Doctor") |

## Error Responses

### Unauthorized (401)
```json
{
  "success": false,
  "message": "Unauthorized - Invalid or expired token"
}
```

### Invalid Token
```json
{
  "success": false,
  "message": "Invalid token"
}
```

### Server Error (500)
```json
{
  "success": false,
  "message": "Internal server error"
}
```

## Java Implementation

### Using LoginApiClient

```java
// Initialize the API client
LoginApiClient apiClient = new LoginApiClient();

// Step 1: Login to get access token
LoginResponse loginResponse = apiClient.login(
    "nelson@gmail.com",
    "Chikitsa@123",
    "b60fae41-3a86-4d4f-8ca9-8180a7d44e0e",
    "SMS Hospital",
    "8c094825-a89a-4b96-a4c7-7d6d06359dea"
);

// Step 2: Get profile using the access token
ProfileResponse profileResponse = apiClient.getProfile(loginResponse.getToken());

// Step 3: Access user information
ProfileResponse.User user = profileResponse.getData().getUser();
System.out.println("Username: " + user.getUsername());
System.out.println("First Name: " + user.getFirstName());
System.out.println("Last Name: " + user.getLastName());
System.out.println("Email: " + user.getEmail());
System.out.println("Total Emails: " + user.getEmails().size());
System.out.println("Total Roles: " + user.getRoles().size());

// Step 4: Iterate through emails
for (ProfileResponse.Email email : user.getEmails()) {
    System.out.println("Email: " + email.getEmail() + 
                       " (Primary: " + email.getIsPrimary() + ")");
}

// Step 5: Iterate through roles
for (ProfileResponse.Role role : user.getRoles()) {
    System.out.println("Role: " + role.getName());
}
```

### Raw Response Access

If you need the raw HTTP response:

```java
LoginApiClient apiClient = new LoginApiClient();
LoginResponse loginResponse = apiClient.login(email, password, facilityId, facilityName, deviceId);

// Get raw response
io.restassured.response.Response rawResponse = apiClient.getProfileRaw(loginResponse.getToken());

System.out.println("Status Code: " + rawResponse.getStatusCode());
System.out.println("Response Body: " + rawResponse.getBody().asString());
```

## Test Cases

The project includes comprehensive test cases in `ProfileApiTest.java`:

1. **test01_GetProfileWithValidToken** - Retrieve profile with valid access token
2. **test02_VerifyProfileEmailsStructure** - Verify emails list structure
3. **test03_VerifyProfileRolesStructure** - Verify roles list structure
4. **test04_VerifyProfileTimestamps** - Verify created_at and updated_at timestamps
5. **test05_VerifyProfileActiveStatus** - Verify user active status
6. **test06_GetProfileWithInvalidToken** - Test error handling for invalid tokens
7. **test07_GetProfileWithNullToken** - Test error handling for null tokens
8. **test08_ProfileDataConsistency** - Verify profile data matches login response
9. **test09_ProfileEndpointAvailability** - Verify endpoint is available
10. **test10_GetProfileMultipleTimes** - Test retrieving profile multiple times

## Authentication Flow

1. **Login**: Use the `/auth/login` endpoint to authenticate and receive an access token
2. **Get Profile**: Use the `/profile` endpoint with the access token to retrieve user profile
3. **Use Profile Data**: Extract and use the user information as needed
4. **Logout**: Use the `/auth/logout` endpoint to end the session

## Model Classes

### ProfileResponse
Main response wrapper containing success flag and user data.

### ProfileResponse.ProfileData
Contains the user object.

### ProfileResponse.User
Contains user information including ID, name, emails, roles, and timestamps.

### ProfileResponse.Email
Contains email address and primary status.

### ProfileResponse.Role
Contains role ID and name.

## Status Codes

| Status Code | Description |
|------------|-------------|
| 200 | Successful profile retrieval |
| 201 | Profile created/retrieved successfully |
| 400 | Bad request |
| 401 | Unauthorized (invalid/expired token) |
| 403 | Forbidden |
| 404 | Not found |
| 500 | Internal server error |

## Usage Patterns

### Get Current User Information
```java
ProfileResponse profileResponse = apiClient.getProfile(accessToken);
ProfileResponse.User currentUser = profileResponse.getData().getUser();
```

### Get Primary Email
```java
ProfileResponse.User user = profileResponse.getData().getUser();
String primaryEmail = user.getEmails().stream()
    .filter(email -> email.getIsPrimary())
    .map(ProfileResponse.Email::getEmail)
    .findFirst()
    .orElse(null);
```

### Get User Roles
```java
List<String> roleNames = profileResponse.getData().getUser().getRoles()
    .stream()
    .map(ProfileResponse.Role::getName)
    .collect(Collectors.toList());
```

### Check User Permissions
```java
boolean isAdmin = profileResponse.getData().getUser().getRoles()
    .stream()
    .anyMatch(role -> "Admin".equals(role.getName()));
```

## Best Practices

1. **Token Management**: Always store the access token securely
2. **Error Handling**: Implement proper exception handling for network and authentication errors
3. **Token Refresh**: Handle token expiration and implement refresh logic
4. **Logging**: Log profile retrieval for audit trails
5. **Caching**: Consider caching profile data to reduce API calls
6. **Validation**: Validate email addresses and role information after retrieval

## Integration with Login/Logout Flow

The Profile API is typically used in conjunction with the Login/Logout flow:

```java
// 1. Login
LoginResponse loginResponse = apiClient.login(email, password, facilityId, facilityName, deviceId);

// 2. Get Profile
ProfileResponse profileResponse = apiClient.getProfile(loginResponse.getToken());

// 3. Use profile data
ProfileResponse.User user = profileResponse.getData().getUser();

// 4. Perform operations...

// 5. Logout
LogoutResponse logoutResponse = apiClient.logout(loginResponse.getToken(), facilityId);
```

## Troubleshooting

### "Invalid token" error
- Ensure the access token hasn't expired
- Re-login to get a fresh token
- Verify the Authorization header format: `Bearer <token>`

### "Unauthorized" error
- Check that the access token is correct
- Verify the user account is active
- Ensure the token hasn't been revoked

### Empty emails list
- User might have only one email
- Check the is_primary flag to identify the primary email

### No roles assigned
- User might not have any roles assigned yet
- Contact administrator to assign appropriate roles

## Related Endpoints

- **Login**: `POST /auth/login` - Authenticate user
- **Logout**: `POST /auth/logout` - End user session
- **Profile**: `GET /profile` - Get user profile (current endpoint)

