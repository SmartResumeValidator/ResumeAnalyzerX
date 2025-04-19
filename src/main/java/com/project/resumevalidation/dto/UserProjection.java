package com.project.resumevalidation.dto;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonPropertyOrder({ "userId", "userName", "userGmail", "userRegisterNbr" })
public interface UserProjection {
	/**
	 * @return userId
	 */
	int getUserId();

	/**
	 * @return userName
	 */
	String getUserName();

	/**
	 * @return
	 */
	String getUserGmail();

	/**
	 * @return usrRegisterNbr
	 */
	String getUserRegisterNbr();
	
	String getUserPassword();

}
