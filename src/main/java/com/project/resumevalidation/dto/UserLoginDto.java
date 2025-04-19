package com.project.resumevalidation.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class UserLoginDto {

    private String gmail;  // Changed from userGmail to gmail for consistency
    private String phonenumber;  // Added phonenumber field for completeness
    private String password;  // Changed from userPassword to password for consistency

}
