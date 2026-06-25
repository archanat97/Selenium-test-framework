package com.orangHRM.test;

import java.util.Map;

import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import com.orangehrm.base.BaseClass;
import com.orangehrm.pages.HomePage;
import com.orangehrm.pages.LoginPage;
import com.orangehrm.utilities.DBConnection;
import com.orangehrm.utilities.DataProviders;
import com.orangehrm.utilities.ExtentManager;

public class DBVerificationTest extends BaseClass {
	private LoginPage lp;
	private HomePage hp;
	
	@BeforeMethod
	public void setupPages() {
		System.out.println("DBVerificationTest.setupPages()");
		lp= new LoginPage(getDriver());
		hp= new HomePage(getDriver());
	}
	
	@Test(dataProvider="emplVerification", dataProviderClass=DataProviders.class)
	public void verifyEmployeeNameVerificationFromDB(String emplID, String empName) {
		ExtentManager.logStep("Logging with admin credentials");
		lp.login(prop.getProperty("username"), prop.getProperty("password"));
		
		ExtentManager.logStep("click on PIM tab");
		hp.clickOnPIM();
		
		ExtentManager.logStep("search for employee");
		hp.employeeSearch(empName);
		
		ExtentManager.logStep("get the employee name from db");
		String employee_id=emplID;
		
		//fetch the data to the map
		Map<String,String> employeeDetails=DBConnection.getEmployeeDetails(employee_id);
        String emplFirstName=employeeDetails.get("firstName");
        String emplMiddleName=employeeDetails.get("middleName");
        String emplLastname=employeeDetails.get("lastName");
               
        String emplFirstAndMiddleName=(emplFirstName+" "+emplMiddleName).trim();
        
        ExtentManager.logStep("verify employee first and middlename");
        Assert.assertTrue(hp.verifyEmployeeFirstAndMiddleName(emplFirstAndMiddleName),"First and Middle name are not matching");
       
       ExtentManager.logStep("verify employee lastname");
       Assert.assertTrue(hp.verifyEmployeeLastName(emplLastname),"last name are not matching");
        
        ExtentManager.logStep("Database validation completed");
	}

}
