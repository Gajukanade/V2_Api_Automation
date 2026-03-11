package org.example.models;

import com.google.gson.annotations.SerializedName;
import java.util.List;

/**
 * Profile Response Model for /profile endpoint
 */
public class ProfileResponse {
    @SerializedName("success")
    private Boolean success;

    @SerializedName("data")
    private ProfileData data;

    // Constructors
    public ProfileResponse() {
    }

    public ProfileResponse(Boolean success, ProfileData data) {
        this.success = success;
        this.data = data;
    }

    // Getters and Setters
    public Boolean getSuccess() {
        return success;
    }

    public void setSuccess(Boolean success) {
        this.success = success;
    }

    public ProfileData getData() {
        return data;
    }

    public void setData(ProfileData data) {
        this.data = data;
    }

    // Nested ProfileData class
    public static class ProfileData {
        @SerializedName("user")
        private User user;

        // Constructors
        public ProfileData() {
        }

        public ProfileData(User user) {
            this.user = user;
        }

        // Getters and Setters
        public User getUser() {
            return user;
        }

        public void setUser(User user) {
            this.user = user;
        }
    }

    // Nested User class
    public static class User {
        @SerializedName("id")
        private String id;

        @SerializedName("username")
        private String username;

        @SerializedName("first_name")
        private String firstName;

        @SerializedName("last_name")
        private String lastName;

        @SerializedName("emails")
        private List<Email> emails;

        @SerializedName("is_active")
        private Boolean isActive;

        @SerializedName("created_at")
        private String createdAt;

        @SerializedName("updated_at")
        private String updatedAt;

        @SerializedName("email")
        private String email;

        @SerializedName("roles")
        private List<Role> roles;

        // Constructors
        public User() {
        }

        public User(String id, String username, String firstName, String lastName,
                   List<Email> emails, Boolean isActive, String createdAt,
                   String updatedAt, String email, List<Role> roles) {
            this.id = id;
            this.username = username;
            this.firstName = firstName;
            this.lastName = lastName;
            this.emails = emails;
            this.isActive = isActive;
            this.createdAt = createdAt;
            this.updatedAt = updatedAt;
            this.email = email;
            this.roles = roles;
        }

        // Getters and Setters
        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }

        public String getFirstName() {
            return firstName;
        }

        public void setFirstName(String firstName) {
            this.firstName = firstName;
        }

        public String getLastName() {
            return lastName;
        }

        public void setLastName(String lastName) {
            this.lastName = lastName;
        }

        public List<Email> getEmails() {
            return emails;
        }

        public void setEmails(List<Email> emails) {
            this.emails = emails;
        }

        public Boolean getIsActive() {
            return isActive;
        }

        public void setIsActive(Boolean isActive) {
            this.isActive = isActive;
        }

        public String getCreatedAt() {
            return createdAt;
        }

        public void setCreatedAt(String createdAt) {
            this.createdAt = createdAt;
        }

        public String getUpdatedAt() {
            return updatedAt;
        }

        public void setUpdatedAt(String updatedAt) {
            this.updatedAt = updatedAt;
        }

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }

        public List<Role> getRoles() {
            return roles;
        }

        public void setRoles(List<Role> roles) {
            this.roles = roles;
        }
    }

    // Nested Email class
    public static class Email {
        @SerializedName("id")
        private String id;

        @SerializedName("email")
        private String email;

        @SerializedName("is_primary")
        private Boolean isPrimary;

        // Constructors
        public Email() {
        }

        public Email(String id, String email, Boolean isPrimary) {
            this.id = id;
            this.email = email;
            this.isPrimary = isPrimary;
        }

        // Getters and Setters
        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }

        public Boolean getIsPrimary() {
            return isPrimary;
        }

        public void setIsPrimary(Boolean isPrimary) {
            this.isPrimary = isPrimary;
        }
    }

    // Nested Role class
    public static class Role {
        @SerializedName("id")
        private String id;

        @SerializedName("name")
        private String name;

        // Constructors
        public Role() {
        }

        public Role(String id, String name) {
            this.id = id;
            this.name = name;
        }

        // Getters and Setters
        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }

    @Override
    public String toString() {
        return "ProfileResponse{" +
                "success=" + success +
                ", data=" + data +
                '}';
    }
}

