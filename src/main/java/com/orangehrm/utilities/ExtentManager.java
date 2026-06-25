package com.orangehrm.utilities;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;

import com.aventstack.extentreports.*;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import com.aventstack.extentreports.reporter.configuration.Theme;

public class ExtentManager {

	private static ExtentReports extent;
	private static ThreadLocal<ExtentTest> test = new ThreadLocal<>();
	private static ThreadLocal<WebDriver> driverMap = new ThreadLocal<>();

	// Initialize the extent report
	public static synchronized ExtentReports getReporter() {
		if (extent == null) {
			String reportPath = System.getProperty("user.dir") + "/src/test/resources/ExtentReport/ExtentReport.html";
			ExtentSparkReporter spark = new ExtentSparkReporter(reportPath);
			spark.config().setReportName("Automation Test Report");
			spark.config().setDocumentTitle("OrangeHRM report");
			spark.config().setTheme(Theme.DARK);

			extent = new ExtentReports();
			extent.attachReporter(spark);
			// Adding system information
			extent.setSystemInfo("Operating System", System.getProperty("os.name"));
			extent.setSystemInfo("Java Version", System.getProperty("java.version"));
			extent.setSystemInfo("User name", System.getProperty("user.name"));
		}
		return extent;
	}

	// start the Test
	public static synchronized ExtentTest startTest(String testName) {
		ExtentTest extentTest = getReporter().createTest(testName);
		test.set(extentTest);
		return extentTest;
	}

	// end the Test
	public static synchronized void endTest() {
		test.remove();
	}

	// Global flush - Call this ONLY in your Listener's onFinish() method
	public static synchronized void flushReport() {
		if (extent != null) {
			extent.flush();
		}
	}

	// get Current Thread's test
	public static synchronized ExtentTest getTest() {
		if (test.get() == null) {
			// FIX: If a parallel worker thread lost its reference, dynamically create an
			// anonymous block instead of crashing with NullPointer
			test.set(getReporter().createTest("Parallel Thread Sandbox - Auto Generated"));
		}
		return test.get();
	}

	// method to get the name of the current test
	public static String getTestName() {
		ExtentTest currentTest = getTest();
		if (currentTest != null) {
			return currentTest.getModel().getName();
		} else {
			return "No test is currently active for this thread";
		}
	}

	// Log a step
	public static void logStep(String logMessage) {
		getTest().info(logMessage);
	}

	// Log a step validation with screenshot
	public static void logWithScreenshot(String logMessage, String screenshotMessage) {
		getTest().pass(logMessage);
		// screenshot method
		attachScreenshot(screenshotMessage);
	}

	// Log a step validation with screenshot for API
		public static void logStepValidationforAPI(String logMessage) {
			getTest().pass(logMessage);
		}
		
	// log a failure
	public static void logFailure(String logMessage, String screenshotMessage) {
		String colorMessage = String.format("<span style='color:red;'>%s</span>", logMessage);
		getTest().fail(colorMessage);
		// screenshot method
		attachScreenshot(screenshotMessage);
	}

	// log a failure for API
	public static void logFailureAPI(String logMessage) {
		String colorMessage = String.format("<span style='color:red;'>%s</span>", logMessage);
		getTest().fail(colorMessage);
		
	}

	// Log a skip
	public static void logSkip(String logMessage) {
		String colorMessage = String.format("<span style='color:orange;'>%s</span>", logMessage);
		getTest().skip(colorMessage);
	}

	// take a screenshot with date and time in the file
	public static synchronized String takeScreenshot(String screenshotname) {
		System.out.println("Current Thread: " + Thread.currentThread().getId());
		WebDriver driver = getDriver();
		System.out.println("Driver from driverMap: " + driver);

		if (driver == null) {
			throw new RuntimeException("Driver is NULL in ExtentManager. Thread=" + Thread.currentThread().getId());
		}

		TakesScreenshot ts = (TakesScreenshot) driver;
		File src = ts.getScreenshotAs(OutputType.FILE);
		// Format date and time for file name
		String timeStamp = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss").format(new Date());

		// saving the sreenshot to a file
		String desPath = System.getProperty("user.dir") + "/src/test/resources/screenshots/" + screenshotname + "_"
				+ timeStamp + ".png";
		File finalPath = new File(desPath);
		try {
			FileUtils.copyFile(src, finalPath);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		// convert screenshot to base64 for embedding in the report
		String base64Format = convertToBase64(src);
		return base64Format;

	}

	// convert screenshot to base64 format
	public static String convertToBase64(File screenshotFile) {
		String base64Format = "";
		// read the file content into a byte array
		try {
			byte[] fileContent = FileUtils.readFileToByteArray(screenshotFile);
			base64Format = Base64.getEncoder().encodeToString(fileContent);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return base64Format;
	}

	// attach the screenshot to report using Base64
	public static synchronized void attachScreenshot(String message) {
		try {
			WebDriver driver = getDriver();
			String screenshotBase64 = takeScreenshot(getTestName());
			getTest().info(message, com.aventstack.extentreports.MediaEntityBuilder
					.createScreenCaptureFromBase64String(screenshotBase64).build());
		} catch (Exception e) {
			getTest().fail("failed to attach screenshot" + message);
			e.printStackTrace();
		}
	}

	// register webdriver for current thread
	public static void registerDriver(WebDriver driver) {
		System.out.println("Registering Driver: " + driver + " Thread=" + Thread.currentThread().getId());
		driverMap.set(driver);
	}

	public static WebDriver getDriver() {
		return driverMap.get();
	}

	public static void unregisterDriver() {
		driverMap.remove();
	}

}
