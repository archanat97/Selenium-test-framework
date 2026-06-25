package com.orangehrm.base;

import java.io.FileInputStream;
import java.io.IOException;
import java.time.Duration;
import java.util.Properties;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.*;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.edge.EdgeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.testng.annotations.*;

import com.orangehrm.actiondriver.ActionDriver;
import com.orangehrm.utilities.ExtentManager;
import com.orangehrm.utilities.LoggerManager;

public class BaseClass {
	
	/*if there are multiple classes added in the xml suite , 	then to load the properties file in every classes , we are 
	using static keyword . Otherwise , before suite will run only once and and for the second test case, the property file will not load.*/
	protected static Properties prop; 
	//without using ThreadLocal
	//protected static WebDriver driver;
	//private static ActionDriver actionDriver;
	
	//using ThreadLocal
	private static ThreadLocal<WebDriver> driver= new ThreadLocal<>();
	private static ThreadLocal<ActionDriver> actionDriver= new ThreadLocal<>();
	
	public static final Logger Logger=LoggerManager.getLogger(BaseClass.class);
	
			
	
	@BeforeSuite
	public void loadConfig() throws IOException {
		//Load the configuration file
		prop=new Properties();
		FileInputStream fis=new FileInputStream(System.getProperty("user.dir") +"/src/main/resources/config.properties");
		prop.load(fis);
		Logger.info("config properties file loaded");
		
		//start the extent report
		//ExtentManager.getReporter();-- this is implemented in testListener
	}
	
	@BeforeMethod
	//synchronized keyword added to make sure only 1 thread access this method at a time
	public synchronized void setup() throws IOException{
		System.out.println("BaseClass.setup()");
		launchBrowser();
		configureBrowser();
		Logger.info("WebDriver initialized and Browser Maximized");
		
		//Initialize ActionDriver for the current thread
		actionDriver.set(new ActionDriver(getDriver())); 
		Logger.info("ActionDriver instance is created"+ Thread.currentThread().getId());
		
	}

	private synchronized void launchBrowser() {
		
		//Initialize the webdriver based on browser mentioned in config file
		String browser=prop.getProperty("browser");
				
		if(browser.equalsIgnoreCase("chrome")) {
			ChromeOptions options=new ChromeOptions();
			options.addArguments("--headless");
			options.addArguments("--disable-gpu");
			options.addArguments("--disable-notifications");
			options.addArguments("--no-sandbox");
			options.addArguments("--disable-dev-shm-usage");
			
			driver.set(new ChromeDriver(options));
			ExtentManager.registerDriver(getDriver());
		    Logger.info("ChromeDriver instance is created");
		}
		else if(browser.equalsIgnoreCase("firefox")){
			FirefoxOptions options=new FirefoxOptions();
			options.addArguments("--headless");
			options.addArguments("--disable-gpu");
			options.addArguments("--disable-notifications");
			options.addArguments("--no-sandbox");
			options.addArguments("--disable-dev-shm-usage");
			driver.set(new FirefoxDriver(options));
			ExtentManager.registerDriver(getDriver());
			Logger.info("FirefoxDriver instance is created");
		}
		else if(browser.equalsIgnoreCase("edge")){
			EdgeOptions options=new EdgeOptions();
			options.addArguments("--headless");
			options.addArguments("--disable-gpu");
			options.addArguments("--disable-notifications");
			options.addArguments("--no-sandbox");
			options.addArguments("--disable-dev-shm-usage");
			driver.set(new EdgeDriver(options));
			ExtentManager.registerDriver(getDriver());
			Logger.info("EdgeDriver instance is created");
		}
		else {
			throw new IllegalArgumentException("Browser not supported"+browser);
		}
		ExtentManager.registerDriver(getDriver());
	}
	private void configureBrowser() {
	//implicitwait
	 int implictitwait= Integer.parseInt(prop.getProperty("implicitwait"));
     getDriver().manage().timeouts().implicitlyWait(Duration.ofSeconds(implictitwait) );
				
	 //navigate to url
     ExtentManager.getDriver().get(prop.getProperty("url"));
     
     //maximize the browser
     getDriver().manage().window().maximize();
	}
	
	@AfterMethod
	public synchronized void tearDown() {
		if(driver!=null)
			getDriver().quit();
		//webdriver instance is closed
		Logger.info("WebDriver instance is createdq");
		//driver=null;
		//actionDriver=null;
		driver.remove();
		actionDriver.remove();
		ExtentManager.unregisterDriver();
		//ExtentManager.endTest();---- this is implemented in testListener
	}
	
	//Driver getter method
	public static WebDriver getDriver() {
		if(driver.get()==null)
			System.out.println("Webdriver not initialized");
		return driver.get();
	}
	
	//ActionDriver getter method
		public static ActionDriver getActionDriver() {
			if(actionDriver.get()==null)
				System.out.println("Webdriver not initialized");
			return actionDriver.get();
		}
		
		
	//Driver setter method
		public void setDriver(ThreadLocal<WebDriver> driver) {
			this.driver=driver;
		}
		
    
	
}
