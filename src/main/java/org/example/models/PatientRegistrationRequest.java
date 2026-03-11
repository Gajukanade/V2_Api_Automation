package org.example.models;

import com.google.gson.annotations.SerializedName;
import java.util.List;

/**
 * Patient Registration Request model with complete API specification
 */
public class PatientRegistrationRequest {
    private String honorific;

    @SerializedName("first_name")
    private String firstName;

    @SerializedName("middle_name")
    private String middleName;

    @SerializedName("last_name")
    private String lastName;

    private String gender;

    @SerializedName("date_of_birth")
    private String dateOfBirth;

    private Integer age;
    private Integer months;
    private Integer days;

    @SerializedName("id_proofs")
    private List<IdProof> idProofs;

    private String aadhaar;

    @SerializedName("abha_number")
    private String abhaNumber;

    @SerializedName("abha_address")
    private String abhaAddress;

    @SerializedName("profile_photo")
    private String profilePhoto;

    private String occupation;

    @SerializedName("marital_status")
    private String maritalStatus;

    private List<Scheme> schemes;

    @SerializedName("patient_category")
    private PatientCategory patientCategory;

    private String religion;
    private String nationality;

    @SerializedName("visit_type")
    private String visitType;

    @SerializedName("medical_info")
    private MedicalInfo medicalInfo;

    @SerializedName("contact_info")
    private ContactInfo contactInfo;

    @SerializedName("kin_info")
    private List<KinInfo> kinInfo;

    @SerializedName("corporate_info")
    private CorporateInfo corporateInfo;

    @SerializedName("billing_info")
    private BillingInfo billingInfo;

    // Constructor
    public PatientRegistrationRequest() {
    }

    // Nested Classes
    public static class IdProof {
        private String type;
        private String value;
        private List<String> assets;

        public IdProof() {}

        public IdProof(String type, String value, List<String> assets) {
            this.type = type;
            this.value = value;
            this.assets = assets;
        }

        public String getType() { return type; }
        public void setType(String type) { this.type = type; }

        public String getValue() { return value; }
        public void setValue(String value) { this.value = value; }

        public List<String> getAssets() { return assets; }
        public void setAssets(List<String> assets) { this.assets = assets; }
    }

    public static class Scheme {
        private String name;
        private String id;

        @SerializedName("beneficiary_id")
        private String beneficiaryId;

        public Scheme() {}

        public Scheme(String name, String id, String beneficiaryId) {
            this.name = name;
            this.id = id;
            this.beneficiaryId = beneficiaryId;
        }

        public String getName() { return name; }
        public void setName(String name) { this.name = name; }

        public String getId() { return id; }
        public void setId(String id) { this.id = id; }

        public String getBeneficiaryId() { return beneficiaryId; }
        public void setBeneficiaryId(String beneficiaryId) { this.beneficiaryId = beneficiaryId; }
    }

    public static class PatientCategory {
        private String name;
        private String id;

        public PatientCategory() {}

        public PatientCategory(String name, String id) {
            this.name = name;
            this.id = id;
        }

        public String getName() { return name; }
        public void setName(String name) { this.name = name; }

        public String getId() { return id; }
        public void setId(String id) { this.id = id; }
    }

    public static class MedicalInfo {
        @SerializedName("blood_group")
        private String bloodGroup;

        private List<Allergy> allergies;

        @SerializedName("medical_conditions")
        private List<MedicalCondition> medicalConditions;

        private List<Disability> disabilities;

        public MedicalInfo() {}

        public String getBloodGroup() { return bloodGroup; }
        public void setBloodGroup(String bloodGroup) { this.bloodGroup = bloodGroup; }

        public List<Allergy> getAllergies() { return allergies; }
        public void setAllergies(List<Allergy> allergies) { this.allergies = allergies; }

        public List<MedicalCondition> getMedicalConditions() { return medicalConditions; }
        public void setMedicalConditions(List<MedicalCondition> medicalConditions) { this.medicalConditions = medicalConditions; }

        public List<Disability> getDisabilities() { return disabilities; }
        public void setDisabilities(List<Disability> disabilities) { this.disabilities = disabilities; }
    }

    public static class Allergy {
        private String id;
        private String name;
        private String code;
        private String system;
        private List<AllergyReaction> reactions;

        public Allergy() {}

        public String getId() { return id; }
        public void setId(String id) { this.id = id; }

        public String getName() { return name; }
        public void setName(String name) { this.name = name; }

        public String getCode() { return code; }
        public void setCode(String code) { this.code = code; }

        public String getSystem() { return system; }
        public void setSystem(String system) { this.system = system; }

        public List<AllergyReaction> getReactions() { return reactions; }
        public void setReactions(List<AllergyReaction> reactions) { this.reactions = reactions; }
    }

    public static class AllergyReaction {
        private String id;
        private String name;
        private String code;
        private String system;

        public AllergyReaction() {}

        public String getId() { return id; }
        public void setId(String id) { this.id = id; }

        public String getName() { return name; }
        public void setName(String name) { this.name = name; }

        public String getCode() { return code; }
        public void setCode(String code) { this.code = code; }

        public String getSystem() { return system; }
        public void setSystem(String system) { this.system = system; }
    }

    public static class MedicalCondition {
        private String id;
        private String name;
        private String code;
        private String system;

        public MedicalCondition() {}

        public String getId() { return id; }
        public void setId(String id) { this.id = id; }

        public String getName() { return name; }
        public void setName(String name) { this.name = name; }

        public String getCode() { return code; }
        public void setCode(String code) { this.code = code; }

        public String getSystem() { return system; }
        public void setSystem(String system) { this.system = system; }
    }

    public static class Disability {
        private String id;
        private String name;
        private Integer percentage;
        private String udid;

        @SerializedName("issue_date")
        private String issueDate;

        @SerializedName("valid_upto")
        private String validUpto;

        public Disability() {}

        public String getId() { return id; }
        public void setId(String id) { this.id = id; }

        public String getName() { return name; }
        public void setName(String name) { this.name = name; }

        public Integer getPercentage() { return percentage; }
        public void setPercentage(Integer percentage) { this.percentage = percentage; }

        public String getUdid() { return udid; }
        public void setUdid(String udid) { this.udid = udid; }

        public String getIssueDate() { return issueDate; }
        public void setIssueDate(String issueDate) { this.issueDate = issueDate; }

        public String getValidUpto() { return validUpto; }
        public void setValidUpto(String validUpto) { this.validUpto = validUpto; }
    }

    public static class ContactInfo {
        private List<Phone> phone;

        @SerializedName("preferred_contact_method")
        private List<String> preferredContactMethod;

        private List<Email> emails;

        @SerializedName("current_address")
        private Address currentAddress;

        @SerializedName("permanent_address")
        private Address permanentAddress;

        @SerializedName("address_proofs")
        private List<AddressProof> addressProofs;

        public ContactInfo() {}

        public List<Phone> getPhone() { return phone; }
        public void setPhone(List<Phone> phone) { this.phone = phone; }

        public List<String> getPreferredContactMethod() { return preferredContactMethod; }
        public void setPreferredContactMethod(List<String> preferredContactMethod) { this.preferredContactMethod = preferredContactMethod; }

        public List<Email> getEmails() { return emails; }
        public void setEmails(List<Email> emails) { this.emails = emails; }

        public Address getCurrentAddress() { return currentAddress; }
        public void setCurrentAddress(Address currentAddress) { this.currentAddress = currentAddress; }

        public Address getPermanentAddress() { return permanentAddress; }
        public void setPermanentAddress(Address permanentAddress) { this.permanentAddress = permanentAddress; }

        public List<AddressProof> getAddressProofs() { return addressProofs; }
        public void setAddressProofs(List<AddressProof> addressProofs) { this.addressProofs = addressProofs; }

        /**
         * Convenience method to set primary phone number
         */
        public void setPrimaryPhone(String number) {
            if (phone == null) {
                phone = new java.util.ArrayList<>();
            }
            Phone primaryPhone = new Phone();
            primaryPhone.setPhoneNo(number);
            primaryPhone.setIsPrimary(true);
            primaryPhone.setCountryCode("+91");
            phone.add(primaryPhone);
        }

        /**
         * Convenience method to set primary email
         */
        public void setPrimaryEmail(String emailAddress) {
            if (emails == null) {
                emails = new java.util.ArrayList<>();
            }
            Email primaryEmail = new Email();
            primaryEmail.setEmail(emailAddress);
            primaryEmail.setIsPrimary(true);
            emails.add(primaryEmail);
        }
    }

    public static class Phone {
        @SerializedName("country_code")
        private String countryCode;

        @SerializedName("phone_no")
        private String phoneNo;

        @SerializedName("is_primary")
        private Boolean isPrimary;

        private Boolean whatsapp;

        @SerializedName("emergency_contact")
        private Boolean emergencyContact;

        public Phone() {}

        public String getCountryCode() { return countryCode; }
        public void setCountryCode(String countryCode) { this.countryCode = countryCode; }

        public String getPhoneNo() { return phoneNo; }
        public void setPhoneNo(String phoneNo) { this.phoneNo = phoneNo; }

        public Boolean getIsPrimary() { return isPrimary; }
        public void setIsPrimary(Boolean isPrimary) { this.isPrimary = isPrimary; }

        public Boolean getWhatsapp() { return whatsapp; }
        public void setWhatsapp(Boolean whatsapp) { this.whatsapp = whatsapp; }

        public Boolean getEmergencyContact() { return emergencyContact; }
        public void setEmergencyContact(Boolean emergencyContact) { this.emergencyContact = emergencyContact; }
    }

    public static class Email {
        private String email;

        @SerializedName("is_primary")
        private Boolean isPrimary;

        public Email() {}

        public String getEmail() { return email; }
        public void setEmail(String email) { this.email = email; }

        public Boolean getIsPrimary() { return isPrimary; }
        public void setIsPrimary(Boolean isPrimary) { this.isPrimary = isPrimary; }
    }

    public static class Address {
        private String pincode;
        private String address;
        private String country;
        private String state;
        private String district;
        private String city;

        public Address() {}

        public String getPincode() { return pincode; }
        public void setPincode(String pincode) { this.pincode = pincode; }

        public String getAddress() { return address; }
        public void setAddress(String address) { this.address = address; }

        public String getCountry() { return country; }
        public void setCountry(String country) { this.country = country; }

        public String getState() { return state; }
        public void setState(String state) { this.state = state; }

        public String getDistrict() { return district; }
        public void setDistrict(String district) { this.district = district; }

        public String getCity() { return city; }
        public void setCity(String city) { this.city = city; }
    }

    public static class AddressProof {
        private String type;
        private List<String> assets;

        public AddressProof() {}

        public String getType() { return type; }
        public void setType(String type) { this.type = type; }

        public List<String> getAssets() { return assets; }
        public void setAssets(List<String> assets) { this.assets = assets; }
    }

    public static class KinInfo {
        private String relationship;
        private String honorific;

        @SerializedName("first_name")
        private String firstName;

        @SerializedName("middle_name")
        private String middleName;

        @SerializedName("last_name")
        private String lastName;

        private List<Phone> phone;

        @SerializedName("preferred_contact_method")
        private List<String> preferredContactMethod;

        private List<Email> emails;

        public KinInfo() {}

        public String getRelationship() { return relationship; }
        public void setRelationship(String relationship) { this.relationship = relationship; }

        public String getHonorific() { return honorific; }
        public void setHonorific(String honorific) { this.honorific = honorific; }

        public String getFirstName() { return firstName; }
        public void setFirstName(String firstName) { this.firstName = firstName; }

        public String getMiddleName() { return middleName; }
        public void setMiddleName(String middleName) { this.middleName = middleName; }

        public String getLastName() { return lastName; }
        public void setLastName(String lastName) { this.lastName = lastName; }

        public List<Phone> getPhone() { return phone; }
        public void setPhone(List<Phone> phone) { this.phone = phone; }

        public List<String> getPreferredContactMethod() { return preferredContactMethod; }
        public void setPreferredContactMethod(List<String> preferredContactMethod) { this.preferredContactMethod = preferredContactMethod; }

        public List<Email> getEmails() { return emails; }
        public void setEmails(List<Email> emails) { this.emails = emails; }
    }

    public static class CorporateInfo {
        @SerializedName("tax_identification_no")
        private String taxIdentificationNo;

        @SerializedName("corporate_name")
        private String corporateName;

        @SerializedName("corporate_id")
        private String corporateId;

        @SerializedName("employee_id")
        private String employeeId;

        private String relationship;

        public CorporateInfo() {}

        public String getTaxIdentificationNo() { return taxIdentificationNo; }
        public void setTaxIdentificationNo(String taxIdentificationNo) { this.taxIdentificationNo = taxIdentificationNo; }

        public String getCorporateName() { return corporateName; }
        public void setCorporateName(String corporateName) { this.corporateName = corporateName; }

        public String getCorporateId() { return corporateId; }
        public void setCorporateId(String corporateId) { this.corporateId = corporateId; }

        public String getEmployeeId() { return employeeId; }
        public void setEmployeeId(String employeeId) { this.employeeId = employeeId; }

        public String getRelationship() { return relationship; }
        public void setRelationship(String relationship) { this.relationship = relationship; }
    }

    public static class BillingInfo {
        @SerializedName("registration_fee")
        private Double registrationFee;

        @SerializedName("payment_mode")
        private String paymentMode;

        @SerializedName("transaction_id")
        private String transactionId;

        @SerializedName("fee_exempt")
        private Boolean feeExempt;

        @SerializedName("fee_exempt_reason")
        private String feeExemptReason;

        public BillingInfo() {}

        public Double getRegistrationFee() { return registrationFee; }
        public void setRegistrationFee(Double registrationFee) { this.registrationFee = registrationFee; }

        public String getPaymentMode() { return paymentMode; }
        public void setPaymentMode(String paymentMode) { this.paymentMode = paymentMode; }

        public String getTransactionId() { return transactionId; }
        public void setTransactionId(String transactionId) { this.transactionId = transactionId; }

        public Boolean getFeeExempt() { return feeExempt; }
        public void setFeeExempt(Boolean feeExempt) { this.feeExempt = feeExempt; }

        public String getFeeExemptReason() { return feeExemptReason; }
        public void setFeeExemptReason(String feeExemptReason) { this.feeExemptReason = feeExemptReason; }
    }

    // Main Class Getters and Setters
    public String getHonorific() { return honorific; }
    public void setHonorific(String honorific) { this.honorific = honorific; }

    public String getFirstName() { return firstName; }
    public void setFirstName(String firstName) { this.firstName = firstName; }

    public String getMiddleName() { return middleName; }
    public void setMiddleName(String middleName) { this.middleName = middleName; }

    public String getLastName() { return lastName; }
    public void setLastName(String lastName) { this.lastName = lastName; }

    public String getGender() { return gender; }
    public void setGender(String gender) { this.gender = gender; }

    public String getDateOfBirth() { return dateOfBirth; }
    public void setDateOfBirth(String dateOfBirth) { this.dateOfBirth = dateOfBirth; }

    public Integer getAge() { return age; }
    public void setAge(Integer age) { this.age = age; }

    public Integer getMonths() { return months; }
    public void setMonths(Integer months) { this.months = months; }

    public Integer getDays() { return days; }
    public void setDays(Integer days) { this.days = days; }

    public List<IdProof> getIdProofs() { return idProofs; }
    public void setIdProofs(List<IdProof> idProofs) { this.idProofs = idProofs; }

    public String getAadhaar() { return aadhaar; }
    public void setAadhaar(String aadhaar) { this.aadhaar = aadhaar; }

    public String getAbhaNumber() { return abhaNumber; }
    public void setAbhaNumber(String abhaNumber) { this.abhaNumber = abhaNumber; }

    public String getAbhaAddress() { return abhaAddress; }
    public void setAbhaAddress(String abhaAddress) { this.abhaAddress = abhaAddress; }

    public String getProfilePhoto() { return profilePhoto; }
    public void setProfilePhoto(String profilePhoto) { this.profilePhoto = profilePhoto; }

    public String getOccupation() { return occupation; }
    public void setOccupation(String occupation) { this.occupation = occupation; }

    public String getMaritalStatus() { return maritalStatus; }
    public void setMaritalStatus(String maritalStatus) { this.maritalStatus = maritalStatus; }

    public List<Scheme> getSchemes() { return schemes; }
    public void setSchemes(List<Scheme> schemes) { this.schemes = schemes; }

    public PatientCategory getPatientCategory() { return patientCategory; }
    public void setPatientCategory(PatientCategory patientCategory) { this.patientCategory = patientCategory; }

    public String getReligion() { return religion; }
    public void setReligion(String religion) { this.religion = religion; }

    public String getNationality() { return nationality; }
    public void setNationality(String nationality) { this.nationality = nationality; }

    public String getVisitType() { return visitType; }
    public void setVisitType(String visitType) { this.visitType = visitType; }

    public MedicalInfo getMedicalInfo() { return medicalInfo; }
    public void setMedicalInfo(MedicalInfo medicalInfo) { this.medicalInfo = medicalInfo; }

    public ContactInfo getContactInfo() { return contactInfo; }
    public void setContactInfo(ContactInfo contactInfo) { this.contactInfo = contactInfo; }

    public List<KinInfo> getKinInfo() { return kinInfo; }
    public void setKinInfo(List<KinInfo> kinInfo) { this.kinInfo = kinInfo; }

    public CorporateInfo getCorporateInfo() { return corporateInfo; }
    public void setCorporateInfo(CorporateInfo corporateInfo) { this.corporateInfo = corporateInfo; }

    public BillingInfo getBillingInfo() { return billingInfo; }
    public void setBillingInfo(BillingInfo billingInfo) { this.billingInfo = billingInfo; }
}

