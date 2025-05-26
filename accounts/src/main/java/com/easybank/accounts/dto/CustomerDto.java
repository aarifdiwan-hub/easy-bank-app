package com.easybank.accounts.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class CustomerDto{
        @NotEmpty(message = "Name cannot be null")
        @Size(min = 3,max=50, message = "The length of the customer name should be between 5 and 30")
        String name;

        @Email(message = "Email address is not valid")
        String email;

        @NotEmpty(message = "Mobile number cannot be null")
        @Pattern(regexp = "\\d{10}", message = "Mobile number must be 10 digits")
        String mobileNumber;

        AccountsDto accountsDto;
}
