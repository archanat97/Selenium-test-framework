package com.orangHRM.test;

import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.*;

import com.orangehrm.base.BaseClass;
import com.orangehrm.pages.HomePage;
import com.orangehrm.pages.LoginPage;
import com.orangehrm.utilities.ExtentManager;

public class HomePageTest extends BaseClass{
	
	private LoginPage lp;
	private HomePage hp;
	
	@BeforeMethod
	public void setupPages() {
		lp= new LoginPage(getDriver());
		hp= new HomePage(getDriver());
	}
	
	@Test
	public void verifyOrangeHRMLogo() {
		//ExtentManager.startTest("valid login test");
		ExtentManager.logStep("navigating to login page and entering username and password");
		lp.login("admin", "admin123");
		ExtentManager.logStep("verifying logo is visible or not");
		Assert.assertTrue(hp.isHRMLogoVisible(),"Logo is not visisble");
		ExtentManager.logStep("validation sucessful");
		ExtentManager.logStep("Logged out sucessfully");
		
		
	}

}
