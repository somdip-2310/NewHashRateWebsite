package com.hashrate.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
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
}