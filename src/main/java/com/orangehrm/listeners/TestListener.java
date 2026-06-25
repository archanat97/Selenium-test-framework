package com.orangehrm.listeners;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;

import org.testng.IAnnotationTransformer;
import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;
import org.testng.annotations.ITestAnnotation;

import com.orangehrm.base.BaseClass;
import com.orangehrm.utilities.ExtentManager;
import com.orangehrm.utilities.RetryAnalyzer;

public class TestListener implements ITestListener, IAnnotationTransformer{

	@Override
	public void transform(ITestAnnotation annotation, Class testClass, Constructor testConstructor, Method testMethod) {
		annotation.setRetryAnalyzer(RetryAnalyzer.class);
	}

	//triggered when the test starts
	@Override
	public void onTestStart(ITestResult result) {
		String testName=result.getMethod().getMethodName();
		//Start logging in extent reports
		ExtentManager.startTest(testName);
		ExtentManager.logStep("Test Started"+testName);
	}

	//triggered when the test succeeds
	@Override
	public void onTestSuccess(ITestResult result) {
		String testName=result.getMethod().getMethodName();
		
		if(!result.getTestClass().getName().toLowerCase().contains("api")) {
			ExtentManager.logWithScreenshot("Test Passed Successfully!", "Test End: "+testName+ "- Test Passed");
		} else {
			ExtentManager.logStepValidationforAPI("Test End: "+testName+ "- Test Passed");
		}
		
		
	}

	//triggered when the test fails
	@Override
	public void onTestFailure(ITestResult result) {
		String testName=result.getMethod().getMethodName();
		String failureMessage=result.getThrowable().getMessage();
		ExtentManager.logStep(failureMessage);
		
		if(!result.getTestClass().getName().toLowerCase().contains("api")) {
		 ExtentManager.logFailure("Test Failed!", "Test End: "+testName+ "- Test Failed");
		}else {
			ExtentManager.logFailureAPI("Test End: "+testName+ "- Test Failed");
		}
	}
		

	//triggered when the test skips
	@Override
	public void onTestSkipped(ITestResult result) {
		String testName=result.getMethod().getMethodName();
		ExtentManager.logSkip("Test Skipped"+testName);
	}

	//Triggered when a suite starts
	@Override
	public void onStart(ITestContext context) {
		// Initialize the extent report
		ExtentManager.getReporter();
	}

	//Triggered when a suite ends
	@Override
	public void onFinish(ITestContext context) {
		//Flush the Extent report
		ExtentManager.flushReport();
	}

}
