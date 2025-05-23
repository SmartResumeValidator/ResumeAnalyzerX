package com.project.resumevalidation.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class User {


    private Long id;

    private String username;
    
    private String password; // This will be the hashed password

    private String gmail;
}
