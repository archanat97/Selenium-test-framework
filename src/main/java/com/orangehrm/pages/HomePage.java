package com.orangehrm.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

import com.orangehrm.actiondriver.ActionDriver;
import com.orangehrm.base.BaseClass;

public class HomePage {
  
private ActionDriver actionDriver;
	
	//Define locators using By class
	private By adminTab=By.xpath("//span[text()='Admin']");
	private By userIDButton=By.className("oxd-userdropdown-name");
	private By logoutButton=By.xpath("//a[text()='Logout']");
	private By orangeHRMlogo= By.xpath("//div[@class='oxd-brand-banner toggled']//img");
	private By pimTab=By.xpath("//span[text()='PIM']");
	private By employeeSearch=By.xpath("//label[text()='Employee Name']/parent::div/following-sibling::div/div/div/input");
	private By searchButton=By.xpath("//button[@type='submit']");
	private By emplFirstAndMiddleName=By.xpath("//div[@class='card-item card-body-slot']/div[1]/div/div[2]");
	private By emplLastName=By.xpath("//div[@class='card-item card-body-slot']/div[2]/div/div[2]");
	
	
	public HomePage(WebDriver driver) {
		this.actionDriver=BaseClass.getActionDriver();
	}
	
	//Method to perform logout operation
	public void logout() {
		actionDriver.click(userIDButton);
		actionDriver.click(logoutButton);
	}
	
	//method to check if admin tab is visible
	public boolean isAdminTabVisible() {
		return actionDriver.isDisplayed(adminTab);
	}
	
	///method to check if HRM logo  tab is displayed
	public boolean isHRMLogoVisible() {
		return actionDriver.isDisplayed(orangeHRMlogo);
	}
	
	//method to navigate t PIM tabe
	public void clickOnPIM() {
		actionDriver.click(pimTab);
	}
	
	//employee search
	public void employeeSearch(String value) {
		actionDriver.enterText(employeeSearch, value);
		actionDriver.click(searchButton);
		actionDriver.scrollElement(emplFirstAndMiddleName);
		System.out.println("scrolled into firstname and last name");
		
	}
	
	//verify employee firstName and middleName
	public boolean verifyEmployeeFirstAndMiddleName(String emplFirstAndMiddleNameFromDB) {
		return actionDriver.compareText(emplFirstAndMiddleName, emplFirstAndMiddleNameFromDB);
	}
	

  //verify employee lastName
   public boolean verifyEmployeeLastName(String emplLastNameFromDB) {
    return actionDriver.compareText(emplLastName, emplLastNameFromDB);
     } 
 
 
}