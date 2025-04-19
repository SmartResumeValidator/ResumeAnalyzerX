package com.project.resumevalidation.dto;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;


public interface UserLoginProjection {
	
	String getGmail();
	String getPassword();

}
