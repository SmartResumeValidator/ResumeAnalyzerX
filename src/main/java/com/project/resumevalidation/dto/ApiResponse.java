package com.project.resumevalidation.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ApiResponse {
	/**
	 * status
	 */
	private String status;
	/**
	 * message
	 */
	private String message;
	/**
	 * isSuccess
	 */
	@JsonProperty("isSuccess") 
	private boolean isSuccess;

}
