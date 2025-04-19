package com.project.resumevalidation.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class UserLoginResponse {
	
	
	UserLoginDto responseDto;
	EwtAlert alert;

}
