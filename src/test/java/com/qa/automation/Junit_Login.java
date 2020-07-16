package com.qa.automation;

import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;

import junit.framework.Assert;

public class Junit_Login extends Guru99_Login_Test {

	
//	WebDriver driver;
	
	@Before
	public void LoginBeforeMethod() {
//		System.setProperty("webdriver.chrome.driver","C:\\Akshata\\SAP - Ariba\\Others\\Tool\\JUnitProject\\Jars\\chromedriver.exe");
//		driver = new ChromeDriver();
		String baseUrl = "http://demo.guru99.com/v4/";
		driver.get(baseUrl);		
	}
	
	@Test
	public void LoginMethod() {
		driver.findElement(By.name("uid")).sendKeys("mngr235494");
		driver.findElement(By.name("password")).sendKeys("rasErAb");
		driver.findElement(By.name("btnLogin")).click();
		
		Assert.assertEquals(driver.getTitle().trim(), "Guru99 Bank Manager HomePage");
		
		driver.quit();
	}
	
}
