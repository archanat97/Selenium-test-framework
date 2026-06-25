package com.orangehrm.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

import com.orangehrm.actiondriver.ActionDriver;
import com.orangehrm.base.BaseClass;

public class LoginPage {

	private ActionDriver actionDriver;
	
	//Define locators using By class
	private By username=By.name("username");
	private By password=By.xpath("//input[@type='password']");
	private By login=By.xpath("//button[text()=' Login ']");
	private By errormessage= By.xpath("//p[text()='Invalid credentials']");
	
	public LoginPage(WebDriver driver) {
		this.actionDriver=BaseClass.getActionDriver();
	}
	
	//Method to login
	public void login(String uname, String pwd) {
		actionDriver.enterText(username, uname);
		actionDriver.enterText(password, pwd);
		actionDriver.click(login);
	}
	
	//method to check if error message is displayed
	public boolean isErrorMessageDisplayed() {
		return actionDriver.isDisplayed(errormessage);
	}
	
	//method to get the text from error message 
		public String getErrorMessageText() {
			return actionDriver.getText(errormessage);
		}
	
}
