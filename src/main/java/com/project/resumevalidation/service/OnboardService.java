package com.project.resumevalidation.service;

import com.project.resumevalidation.dto.UserLoginDto;

public interface OnboardService {

	boolean userLogin(UserLoginDto userCredentials);

}
