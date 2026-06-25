package com.orangHRM.test;

import org.testng.Assert;
import org.testng.annotations.*;

import com.orangehrm.base.BaseClass;
import com.orangehrm.pages.HomePage;
import com.orangehrm.pages.LoginPage;
import com.orangehrm.utilities.DataProviders;
import com.orangehrm.utilities.ExtentManager;

public class LoginPageTest extends BaseClass {
	
	private LoginPage lp;
	private HomePage hp;
	
	@BeforeMethod
	public void setupPages() {
		lp= new LoginPage(getDriver());
		hp= new HomePage(getDriver());
	}
	
	@Test(priority=1,dataProvider="validLoginData", dataProviderClass=DataProviders.class)
	public void verifyValidLoginTest(String username, String password) {
		
		//ExtentManager.startTest("valid login test");
		ExtentManager.logStep("navigating to login page and entering username and password");
		lp.login(username, password);
		ExtentManager.logStep("verifying admin tab is visible or not");
		Assert.assertTrue(hp.isAdminTabVisible(), "Admin Tab should be visible");
		ExtentManager.logStep("validation sucessful");
		hp.logout();
		ExtentManager.logStep("Logged out sucessfully");
		
	}
	
	@Test(priority=2,dataProvider="invalidLoginData", dataProviderClass=DataProviders.class)
	public void invalidLogintest(String username, String password) {
		//ExtentManager.startTest("Invalid login test");
		ExtentManager.logStep("navigating to login page and entering username and password");
		lp.login(username, password);
		ExtentManager.logStep("verifying admin tab is not visible");
		System.out.println("hp.isAdminTabVisible()");
		Assert.assertFalse(hp.isAdminTabVisible());
		ExtentManager.logStep("Logged out sucessfully");
	}

}
