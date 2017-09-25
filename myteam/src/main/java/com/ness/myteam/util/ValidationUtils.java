package com.ness.myteam.util;

import java.util.Date;

import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

@Service
public class ValidationUtils {

	public void validateNotEmpty(String parameterName, String parameterValue) {
		if (StringUtils.isEmpty(parameterValue)) {
			throw new IllegalArgumentException(parameterName + " is empty!");
		}
	}
	
	public void validateNotEmpty(String parameterName, Double parameterValue) {
		if (parameterValue == null) {
			throw new IllegalArgumentException(parameterName + " is empty!");
		}
	}
	
	public void validateNotEmpty(String parameterName, Integer parameterValue) {
		if (parameterValue == null) {
			throw new IllegalArgumentException(parameterName + " is empty!");
		}
	}
	
	public void validateNotEmpty(String parameterName, Date parameterValue) {
		if (parameterValue == null) {
			throw new IllegalArgumentException(parameterName + " is empty!");
		}
	}
	
	public void validateNotEmpty(String parameterName, Boolean parameterValue) {
		if (parameterValue == null) {
			throw new IllegalArgumentException(parameterName + " is empty!");
		}
	}
}
