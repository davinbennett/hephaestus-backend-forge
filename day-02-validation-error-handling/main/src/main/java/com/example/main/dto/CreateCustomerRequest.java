package com.example.main.dto;
import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class CreateCustomerRequest {
    
    @NotBlank(message = "must not be blank")
    @Size(min = 3, max = 100, message = "length must be between 3 and 100 characters")
    @JsonProperty("full_name")
    private String fullName;

    @NotBlank(message = "must not be blank")
    @Email(message = "email format is invalid")
    private String email;

    @NotBlank(message = "must not be blank")
    @Size(min = 10, message = "minimum 10 characters")
    @JsonProperty("phone_number")
    private String phoneNumber;

    public String getFullName() { return fullName; }
    public void setFullName(String fullName) { this.fullName = fullName; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getPhoneNumber() { return phoneNumber; }
    public void setPhoneNumber(String phoneNumber) { this.phoneNumber = phoneNumber; }
}