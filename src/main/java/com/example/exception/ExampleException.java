package com.example.exception;

import org.apache.log4j.Logger;
import org.json.simple.JSONObject;


/**
 * @author nileshkuchekar
 *
 */
public class ExampleException extends Exception{
	
	public static final String MISSING_PARAMETERS = "Missing Parameters";
	private static final Logger logger = Logger.getLogger(ExampleException.class);
	
	
	
	private JSONObject errorObject;
	private JSONObject responseObject;

	public ExampleException(String message,Exception e) {
		
		logger.info("Exception Handling");
		logger.error("Exception", e);
		logger.info(e);
		this.responseObject = new JSONObject();
		this.errorObject = new JSONObject();
		if(null!=message){
			errorObject.put("message", message);
		}else{
			errorObject.put("class", e.getClass().getCanonicalName());
			errorObject.put("message",e.getMessage());
		}
		responseObject.put("status", "error");
		responseObject.put("error", errorObject);
	}
	
	public JSONObject getResponseObject() {
		return responseObject;
	}
}
