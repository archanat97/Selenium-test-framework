package com.orangHRM.test;

import org.testng.Assert;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import com.orangehrm.utilities.APIUtility;
import com.orangehrm.utilities.ExtentManager;
import com.orangehrm.utilities.RetryAnalyzer;

import io.restassured.response.Response;

public class APITest {
	
	@Test  //(retryAnalyzer= RetryAnalyzer.class)--can be added when applying to a test class. here retry analyzer is added globally in listener.
   public void verifyGetUserAPI() {
	   
		SoftAssert softassert= new SoftAssert();
		
	   //step1 : define api end point
	    String endpoint= "https://jsonplaceholder.typicode.com/users/1";
	    ExtentManager.logStep("API endpoint is" +endpoint);	
	    
	    //step2 : send GET request
	    ExtentManager.logStep("Sending GET request to the API");
	    Response res=APIUtility.sendGetRequest(endpoint);
	    
	    //step3:validate status code
	    ExtentManager.logStep("Validating API response status code");
	    boolean isStatusCodeValid=APIUtility.validateStatusCode(res, 201);
        
	    softassert.assertTrue(isStatusCodeValid, "Status code is not as Expected");
	    
	    if(isStatusCodeValid) {
	    	ExtentManager.logStepValidationforAPI("status code validation is passed");
	    }else {
	    	ExtentManager.logFailureAPI("status code validation failed");
	    }
	    
	    //step4: validate username
	    ExtentManager.logStep("validating response body for username");
	    String userName=APIUtility.getJsonValue(res, "username");
	    boolean isUserNameValid= "Bret".equals(userName);
	    Assert.assertTrue(isUserNameValid,"username is not valid");
	    
	    if(isUserNameValid) {
	    	ExtentManager.logStepValidationforAPI("username validation is passed");
	    }else {
	    	ExtentManager.logFailureAPI("username validation failed");
	    }
	    
	  //step4: validate email
	    ExtentManager.logStep("validating response body for email");
	    String userEmail=APIUtility.getJsonValue(res, "email");
	    boolean isEmailValid= "Sincere@april.biz".equals(userEmail);
	    softassert.assertTrue(isEmailValid,"email is not valid");
	    
	    if(isEmailValid) {
	    	ExtentManager.logStepValidationforAPI("email validation is passed");
	    }else {
	    	ExtentManager.logFailureAPI("email validation failed");
	    }
	    
	    softassert.assertAll();
	    
   }
}

