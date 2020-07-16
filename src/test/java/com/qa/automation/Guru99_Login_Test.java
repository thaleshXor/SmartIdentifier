package com.qa.automation;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;

import io.github.bonigarcia.wdm.WebDriverManager;

public class Guru99_Login_Test extends Junit_Listner{

	public static WebDriver driver;
	public static XmlReader xmlXpathBuilder;
	public static String strObjectFilePath = "";

	public static void main(String[] args) {
		WebDriverManager.firefoxdriver().setup();
		//WebDriverManager.chromedriver().setup();

		driver = new FirefoxDriver();
		//driver = new ChromeDriver();
		xmlXpathBuilder = new XmlReader();
		driver.get("https://demo.guru99.com/V4/");
		
		
		//-----------------------------Your code goes here ---------------------------------
		ReUsableActions.xorSetText("validateuserid();_10","mngr272770");
		ReUsableActions.xorSetText("validatepassword();_validatepassword();","sumYgyq");
		ReUsableActions.xorClick("btnLogin_INPUT");
		//-----------------------------Your code goes here ---------------------------------
//		ReUsableActions.xorSetText("validateuserid();_10", "mngr249135");
//		ReUsableActions.xorSetText("validatepassword();_validatepassword();", "yhYpama");
//		ReUsableActions.xorClick("btnLogin_INPUT");
		//----------------------------------------------------------------------------------
		driver.quit();
	}
}
