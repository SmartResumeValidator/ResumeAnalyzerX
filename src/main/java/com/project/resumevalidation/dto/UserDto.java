package com.project.resumevalidation.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class UserDto {

    /**
     * userName
     */
    @NotNull(message = "User name cannot be null")
    private String userName;

    /**
     *userGmail 
     */
    @NotNull(message = "Email cannot be null")
    @Email(message = "Invalid email format")
    private String gmail;

    
    @NotNull(message ="Password Cannot be Empty")
    private String password;


}
