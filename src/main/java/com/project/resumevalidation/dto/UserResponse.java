package com.project.resumevalidation.dto;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(Include.NON_NULL)
public class UserResponse {
	
	/**
	 * regResponse
	 */
	private List<UserProjection> regResponse;
	/**
	 * alert
	 */
	private EwtAlert alert;
	
}
