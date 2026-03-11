package org.example.models;

import com.google.gson.annotations.SerializedName;

/**
 * Patient Registration Response model
 */
public class PatientRegistrationResponse {
    private Boolean success;

    @SerializedName("patient_id")
    private String uhid;

    private String message;

    @SerializedName("registration_number")
    private String registrationNumber;

    private PatientData data;

    public PatientRegistrationResponse() {
    }

    public Boolean getSuccess() {
        return success;
    }

    public void setSuccess(Boolean success) {
        this.success = success;
    }

    public String getPatientId() {
        return uhid;
    }

    public void setPatientId(String patientId) {
        this.uhid = patientId;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getRegistrationNumber() {
        return registrationNumber;
    }

    public void setRegistrationNumber(String registrationNumber) {
        this.registrationNumber = registrationNumber;
    }

    public PatientData getData() {
        return data;
    }

    public void setData(PatientData data) {
        this.data = data;
    }

    public static class PatientData {
        private String id;
        private String uhid;

        @SerializedName("global_patient_id")
        private String globalPatientId;

        private String honorific;

        @SerializedName("first_name")
        private String firstName;

        @SerializedName("middle_name")
        private String middleName;

        @SerializedName("last_name")
        private String lastName;

        @SerializedName("primary_phone")
        private String primaryPhone;

        @SerializedName("primary_email")
        private String primaryEmail;

        private String email;
        private String phone;

        @SerializedName("gender")
        private String gender;

        @SerializedName("date_of_birth")
        private String dateOfBirth;

        @SerializedName("registration_number")
        private String registrationNumber;

        private String status;

        @SerializedName("created_at")
        private String createdAt;

        @SerializedName("updated_at")
        private String updatedAt;

        @SerializedName("visit_type")
        private Object visitType;

        private Object schemes;

        @SerializedName("patient_category")
        private Object patientCategory;

        @SerializedName("corporate_info")
        private Object corporateInfo;

        @SerializedName("billing_info")
        private Object billingInfo;

        @SerializedName("created_by")
        private CreatedByInfo createdBy;

        @SerializedName("updated_by")
        private CreatedByInfo updatedBy;

        @SerializedName("is_active")
        private Boolean isActive;

        public PatientData() {
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
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

        public String getMiddleName() {
            return middleName;
        }

        public void setMiddleName(String middleName) {
            this.middleName = middleName;
        }

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }

        public String getPhone() {
            return phone;
        }

        public void setPhone(String phone) {
            this.phone = phone;
        }

        public String getGender() {
            return gender;
        }

        public void setGender(String gender) {
            this.gender = gender;
        }

        public String getDateOfBirth() {
            return dateOfBirth;
        }

        public void setDateOfBirth(String dateOfBirth) {
            this.dateOfBirth = dateOfBirth;
        }

        public String getRegistrationNumber() {
            return registrationNumber;
        }

        public void setRegistrationNumber(String registrationNumber) {
            this.registrationNumber = registrationNumber;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
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

        public String getUhid() {
            return uhid;
        }

        public void setUhid(String uhid) {
            this.uhid = uhid;
        }

        public String getGlobalPatientId() {
            return globalPatientId;
        }

        public void setGlobalPatientId(String globalPatientId) {
            this.globalPatientId = globalPatientId;
        }

        public String getHonorific() {
            return honorific;
        }

        public void setHonorific(String honorific) {
            this.honorific = honorific;
        }

        public String getPrimaryPhone() {
            return primaryPhone;
        }

        public void setPrimaryPhone(String primaryPhone) {
            this.primaryPhone = primaryPhone;
        }

        public String getPrimaryEmail() {
            return primaryEmail;
        }

        public void setPrimaryEmail(String primaryEmail) {
            this.primaryEmail = primaryEmail;
        }


        public Object getVisitType() {
            return visitType;
        }

        public void setVisitType(Object visitType) {
            this.visitType = visitType;
        }

        public Object getSchemes() {
            return schemes;
        }

        public void setSchemes(Object schemes) {
            this.schemes = schemes;
        }

        public Object getPatientCategory() {
            return patientCategory;
        }

        public void setPatientCategory(Object patientCategory) {
            this.patientCategory = patientCategory;
        }

        public Object getCorporateInfo() {
            return corporateInfo;
        }

        public void setCorporateInfo(Object corporateInfo) {
            this.corporateInfo = corporateInfo;
        }

        public Object getBillingInfo() {
            return billingInfo;
        }

        public void setBillingInfo(Object billingInfo) {
            this.billingInfo = billingInfo;
        }

        public CreatedByInfo getCreatedBy() {
            return createdBy;
        }

        public void setCreatedBy(CreatedByInfo createdBy) {
            this.createdBy = createdBy;
        }

        public CreatedByInfo getUpdatedBy() {
            return updatedBy;
        }

        public void setUpdatedBy(CreatedByInfo updatedBy) {
            this.updatedBy = updatedBy;
        }

        public Boolean getIsActive() {
            return isActive;
        }

        public void setIsActive(Boolean isActive) {
            this.isActive = isActive;
        }
    }

    /**
     * Inner class for created_by and updated_by information
     */
    public static class CreatedByInfo {
        private String id;
        private String username;
        private java.util.List<RoleInfo> roles;

        public CreatedByInfo() {
        }

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

        public java.util.List<RoleInfo> getRoles() {
            return roles;
        }

        public void setRoles(java.util.List<RoleInfo> roles) {
            this.roles = roles;
        }
    }

    /**
     * Inner class for role information
     */
    public static class RoleInfo {
        private String id;
        private String name;

        public RoleInfo() {
        }

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
}


