package com.orangehrm.actiondriver;

import java.time.Duration;

import org.apache.logging.log4j.EventLogger;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.orangehrm.base.BaseClass;
import com.orangehrm.utilities.ExtentManager;

public class ActionDriver {
	
	private WebDriver driver;
	private WebDriverWait wait;
	public static final Logger Logger= BaseClass.Logger;
	
	public ActionDriver(WebDriver driver) {
		this.driver=driver;
		this.wait= new WebDriverWait(driver, Duration.ofSeconds(5));
	    Logger.info("WebDriver instance is created");
	}
	
	//method to click an element
	public void click(By by) {
		String elementDescription=getElementDescription(by);
		try {
			applyBorder(by,"green");
			waitForElementToBeClickable(by);
			driver.findElement(by).click();
			ExtentManager.logStep("Clicked an Element:" +elementDescription);
			Logger.info("clicked an element"+elementDescription);
		}catch (Exception e)
		{
			applyBorder(by,"red");
			System.out.println("unable to click element"+e.getMessage());
			ExtentManager.logFailure("Unable to click element", elementDescription);
			Logger.error("unable to click the element");
		}
	}
	
	//method to enter text to an input field
		public void enterText(By by, String value) {
			try {
				waitForElementToBeVisible(by);
				applyBorder(by,"green");
				WebElement element=driver.findElement(by);
				element.clear();
				element.sendKeys(value);
				Logger.info("Enteres text on :"+getElementDescription(by)+"--> "+value);
			} catch (Exception e) {
				applyBorder(by,"red");
				Logger.error("Unable to enter the value"+e.getMessage());
			}
		}
		
	//method to get text from an input field
		public String getText(By by) {
			try {
				waitForElementToBeVisible(by);
				applyBorder(by,"green");
				return driver.findElement(by).getText();
			} catch (Exception e) {
				applyBorder(by,"red");
				Logger.error("Unable to get the text"+e.getMessage());
				return "";
			}
		}
		
	//method to compare two text
		public boolean compareText(By by,String expectedText) {
			try {
				waitForElementToBeVisible(by);
				String actualText= driver.findElement(by).getText();
				System.out.println("actualtext is " +actualText);
				if(expectedText.equals(actualText)) {
					applyBorder(by,"green");
					Logger.info("Texts are matching" +actualText+ "equals" +expectedText);
					ExtentManager.logWithScreenshot("Compare Text", "text compared sucessfully! ");
				    return true;
				}
				else {
					applyBorder(by,"red");
					Logger.error("Texts are not matching" +actualText+"not equals" +expectedText);
					ExtentManager.logFailure("text comparison failed", "text compare unsucessfull! ");
				    return false;
				}
			} catch (Exception e) {
				applyBorder(by,"red");
				Logger.error("Unable to compare texts"+e.getMessage());
			}
			return false;
		}
		
		
	 //method to check if the element is displayed
	 public boolean isDisplayed(By by) {
		 try {
				waitForElementToBeVisible(by);
				applyBorder(by,"green");
				Logger.info("Element is displayed" +getElementDescription(by));
				ExtentManager.logStep("Element is displayed"+getElementDescription(by));
				ExtentManager.logWithScreenshot("Element is displayed","Element is displayed");
				return driver.findElement(by).isDisplayed();
				
			} catch (Exception e) {
				applyBorder(by,"red");
				Logger.error("Element is not displayed" +e.getMessage());
				ExtentManager.logFailure("Element not displayed", "Element is not displayed");
				return false;
			} 
	 }
	 
	 //scroll to an element
	 public void scrollElement(By by) {
		 try {
			 applyBorder(by,"green");
			 JavascriptExecutor js= (JavascriptExecutor)driver;
			 WebElement element= driver.findElement(by);
			 js.executeScript("arguments[0].scrollIntoView(true);", element);
		 } catch (Exception e) {
			 applyBorder(by,"red");
			 Logger.error("unable to locate element" +e.getMessage());
		 }
	 }
	 
	//wait for element to be clickable
	private void waitForElementToBeClickable(By by) {
		try {
		wait.until(ExpectedConditions.elementToBeClickable(by));
		}catch (Exception e) {
			Logger.error("element not clickable" +e.getMessage());
		}
	}
	//wait for element to be visible
		private void waitForElementToBeVisible(By by) {
			try {
			wait.until(ExpectedConditions.visibilityOfElementLocated(by));
			}catch (Exception e) {
				Logger.error("element not visible" +e.getMessage());
			}
		}
		
	//Method to get the description of an element using By locator
		public String getElementDescription(By locator) {
		//Check for null driver or locator to avoid NULLPointer Exception
		 if(driver==null)
			 return "driver is null";
		 if(locator==null)
			 return "locator is null";
		 
		 try {
			//find the element using locator
			 WebElement element=driver.findElement(locator);
			 
			 //Get Element Attributes
			 String name= element.getAttribute("name");
			 String id= element.getAttribute("id");
			 String text= element.getText();
			 String classname= element.getAttribute("class");
			 String placeholder= element.getAttribute("placeholder");
			 
			 //Return the description based on element attributes
			 if(isNotEmpty(name)) {
				 return "Element with name:"+name;
			 }else if (isNotEmpty(id)){
				 return "Element with id:"+id;
			 }else if (isNotEmpty(text)) {
			     return "Element with text:"+truncate(text,50);
			}else if (isNotEmpty(classname)) {
			     return "Element with classname:"+classname;
			}else if (isNotEmpty(placeholder)) {
			     return "Element with placeholder:"+placeholder;
			}
		} catch (Exception e) {
			Logger.error("Unable to describe element"+e.getMessage());
		}
		 return "Unable to describe the element";
		}
	//utility method to check the string is not empty
		private boolean isNotEmpty(String value) {
			return value!=null && !value.isEmpty();
		}
		
	//utility method to truncate long string
		private String truncate(String value, int maxlength) {
			if(value==null || value.length()<=maxlength) {
				return value;
			}
			return value.substring(0,maxlength)+"...";
		}
		
	//utility method to border an element
		public void applyBorder(By by,String color) {
			//Locate an element
			try {
				WebElement element= driver.findElement(by);
				//Apply the border
				String script="arguments[0].style.border='3px solid "+color+"'";
				JavascriptExecutor js=(JavascriptExecutor)driver;
				js.executeScript(script, element);
				Logger.info("Applied the border with color "+color+" to element " +getElementDescription(by));
			} catch (Exception e) {
				Logger.warn("Failed to apply the border to an elememnt" +getElementDescription(by), e.getMessage());
				
			}
			
		}
		
		//method to select dropdown by visible text
		public void selectByVisibleText(By by, String value) {
			try {
				WebElement element=driver.findElement(by);
				new Select(element).selectByVisibleText(value);
				applyBorder(by,"green");
				Logger.info("Selected dropdown value" +value);
			} catch (Exception e) {
				applyBorder(by,"red");
				Logger.info("Unable to Select dropdown value" +value , e);
			}
		}
		
		//we can also add other methods as well in this java class (Eg: Frame , alert handling etc)
}
