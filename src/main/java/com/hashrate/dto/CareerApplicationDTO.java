package com.hashrate.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import org.springframework.web.multipart.MultipartFile;

public class CareerApplicationDTO {
    
    @NotBlank(message = "First name is required")
    @Size(min = 2, max = 50, message = "First name must be between 2 and 50 characters")
    private String firstName;
    
    @NotBlank(message = "Last name is required")
    @Size(min = 2, max = 50, message = "Last name must be between 2 and 50 characters")
    private String lastName;
    
    @NotBlank(message = "Email is required")
    @Email(message = "Please provide a valid email address")
    private String email;
    
    @NotBlank(message = "Phone number is required")
    @Pattern(regexp = "^[+]?[0-9]{10,15}$", message = "Please provide a valid phone number")
    private String phone;
    
    @NotBlank(message = "Gender is required")
    private String gender;
    
    @NotBlank(message = "Address is required")
    @Size(max = 500, message = "Address must not exceed 500 characters")
    private String location;
    
    @NotBlank(message = "Position is required")
    private String jobRole;
    
    @NotBlank(message = "Experience is required")
    private String experience;
    
    @NotBlank(message = "Introduction is required")
    @Size(min = 50, max = 500, message = "Introduction must be between 50 and 500 characters")
    private String swot;
    
    @NotNull(message = "Resume is required")
    private MultipartFile resume;
    
    // Additional fields
    private String currentCompany;
    private String currentCtc;
    private String expectedCtc;
    private String noticePeriod;
    private String linkedinProfile;
    private String portfolioUrl;
    
    // Reference
    private String referenceSource;
    private String referralEmployeeName;

    // Constructors
    public CareerApplicationDTO() {}

    public CareerApplicationDTO(String firstName, String lastName, String email, String phone, 
                                String gender, String location, String jobRole, String experience, 
                                String swot, MultipartFile resume, String currentCompany, 
                                String currentCtc, String expectedCtc, String noticePeriod, 
                                String linkedinProfile, String portfolioUrl, String referenceSource, 
                                String referralEmployeeName) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.phone = phone;
        this.gender = gender;
        this.location = location;
        this.jobRole = jobRole;
        this.experience = experience;
        this.swot = swot;
        this.resume = resume;
        this.currentCompany = currentCompany;
        this.currentCtc = currentCtc;
        this.expectedCtc = expectedCtc;
        this.noticePeriod = noticePeriod;
        this.linkedinProfile = linkedinProfile;
        this.portfolioUrl = portfolioUrl;
        this.referenceSource = referenceSource;
        this.referralEmployeeName = referralEmployeeName;
    }

    // Builder pattern
    public static CareerApplicationDTOBuilder builder() {
        return new CareerApplicationDTOBuilder();
    }

    public static class CareerApplicationDTOBuilder {
        private String firstName;
        private String lastName;
        private String email;
        private String phone;
        private String gender;
        private String location;
        private String jobRole;
        private String experience;
        private String swot;
        private MultipartFile resume;
        private String currentCompany;
        private String currentCtc;
        private String expectedCtc;
        private String noticePeriod;
        private String linkedinProfile;
        private String portfolioUrl;
        private String referenceSource;
        private String referralEmployeeName;

        public CareerApplicationDTOBuilder firstName(String firstName) {
            this.firstName = firstName;
            return this;
        }

        public CareerApplicationDTOBuilder lastName(String lastName) {
            this.lastName = lastName;
            return this;
        }

        public CareerApplicationDTOBuilder email(String email) {
            this.email = email;
            return this;
        }

        public CareerApplicationDTOBuilder phone(String phone) {
            this.phone = phone;
            return this;
        }

        public CareerApplicationDTOBuilder gender(String gender) {
            this.gender = gender;
            return this;
        }

        public CareerApplicationDTOBuilder location(String location) {
            this.location = location;
            return this;
        }

        public CareerApplicationDTOBuilder jobRole(String jobRole) {
            this.jobRole = jobRole;
            return this;
        }

        public CareerApplicationDTOBuilder experience(String experience) {
            this.experience = experience;
            return this;
        }

        public CareerApplicationDTOBuilder swot(String swot) {
            this.swot = swot;
            return this;
        }

        public CareerApplicationDTOBuilder resume(MultipartFile resume) {
            this.resume = resume;
            return this;
        }

        public CareerApplicationDTOBuilder currentCompany(String currentCompany) {
            this.currentCompany = currentCompany;
            return this;
        }

        public CareerApplicationDTOBuilder currentCtc(String currentCtc) {
            this.currentCtc = currentCtc;
            return this;
        }

        public CareerApplicationDTOBuilder expectedCtc(String expectedCtc) {
            this.expectedCtc = expectedCtc;
            return this;
        }

        public CareerApplicationDTOBuilder noticePeriod(String noticePeriod) {
            this.noticePeriod = noticePeriod;
            return this;
        }

        public CareerApplicationDTOBuilder linkedinProfile(String linkedinProfile) {
            this.linkedinProfile = linkedinProfile;
            return this;
        }

        public CareerApplicationDTOBuilder portfolioUrl(String portfolioUrl) {
            this.portfolioUrl = portfolioUrl;
            return this;
        }

        public CareerApplicationDTOBuilder referenceSource(String referenceSource) {
            this.referenceSource = referenceSource;
            return this;
        }

        public CareerApplicationDTOBuilder referralEmployeeName(String referralEmployeeName) {
            this.referralEmployeeName = referralEmployeeName;
            return this;
        }

        public CareerApplicationDTO build() {
            return new CareerApplicationDTO(firstName, lastName, email, phone, gender, location, 
                                            jobRole, experience, swot, resume, currentCompany, 
                                            currentCtc, expectedCtc, noticePeriod, linkedinProfile, 
                                            portfolioUrl, referenceSource, referralEmployeeName);
        }
    }

    // Getters and Setters
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

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getJobRole() {
        return jobRole;
    }

    public void setJobRole(String jobRole) {
        this.jobRole = jobRole;
    }

    public String getExperience() {
        return experience;
    }

    public void setExperience(String experience) {
        this.experience = experience;
    }

    public String getSwot() {
        return swot;
    }

    public void setSwot(String swot) {
        this.swot = swot;
    }

    public MultipartFile getResume() {
        return resume;
    }

    public void setResume(MultipartFile resume) {
        this.resume = resume;
    }

    public String getCurrentCompany() {
        return currentCompany;
    }

    public void setCurrentCompany(String currentCompany) {
        this.currentCompany = currentCompany;
    }

    public String getCurrentCtc() {
        return currentCtc;
    }

    public void setCurrentCtc(String currentCtc) {
        this.currentCtc = currentCtc;
    }

    public String getExpectedCtc() {
        return expectedCtc;
    }

    public void setExpectedCtc(String expectedCtc) {
        this.expectedCtc = expectedCtc;
    }

    public String getNoticePeriod() {
        return noticePeriod;
    }

    public void setNoticePeriod(String noticePeriod) {
        this.noticePeriod = noticePeriod;
    }

    public String getLinkedinProfile() {
        return linkedinProfile;
    }

    public void setLinkedinProfile(String linkedinProfile) {
        this.linkedinProfile = linkedinProfile;
    }

    public String getPortfolioUrl() {
        return portfolioUrl;
    }

    public void setPortfolioUrl(String portfolioUrl) {
        this.portfolioUrl = portfolioUrl;
    }

    public String getReferenceSource() {
        return referenceSource;
    }

    public void setReferenceSource(String referenceSource) {
        this.referenceSource = referenceSource;
    }

    public String getReferralEmployeeName() {
        return referralEmployeeName;
    }

    public void setReferralEmployeeName(String referralEmployeeName) {
        this.referralEmployeeName = referralEmployeeName;
    }
 }