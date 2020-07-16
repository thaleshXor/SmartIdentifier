package com.qa.automation;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.Format;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

//import org.w3c.dom.Document;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;
import org.jdom.output.XMLOutputter;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import io.github.bonigarcia.wdm.WebDriverManager;

import org.w3c.dom.NodeList;



import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathFactory;

public class ReUsableActions extends Guru99_Login_Test {
	
	
    //driver = new ChromeDriver(); 
	
	
	public static void xorClick(String strXmlObjName)
	{
		try
		{
			
			String strXpath=xmlXpathBuilder.xmlToXpathBuilder(strXmlObjName);
			String isElementPresent=checkIfElementPresent(strXpath);
			if(isElementPresent.equals("true"))
			{
				driver.findElement(By.xpath(strXpath)).click();
			}
			else
			{
				strXpath=SmartIdentifier.triggerSmartIdentifier(driver);
				driver.findElement(By.xpath(strXpath)).click();
			}
			
//			throw new NoSuchElementException("nirmal >>>>>>>>>>>>");
		 
		}
		catch(NoSuchElementException e)
		{
			
			listner();
			e.printStackTrace();
			
		}
		catch(Exception e)
		{
			e.printStackTrace();
			
		}
	}
	
	public static void xorSetText(String strXmlObjName, String textValue)
	{
		
		
		try
		{
			String strXpath=xmlXpathBuilder.xmlToXpathBuilder(strXmlObjName);
			String isElementPresent=checkIfElementPresent(strXpath);
			if(isElementPresent.equals("true"))
			{
				driver.findElement(By.xpath(strXpath)).sendKeys(textValue);
			}
			else
			{
				strXpath=SmartIdentifier.triggerSmartIdentifier(driver);
				driver.findElement(By.xpath(strXpath)).sendKeys(textValue);
			}
		 
		}
		catch(NoSuchElementException e)
		{
			e.printStackTrace();
			
		}
		catch(Exception e)
		{
			e.printStackTrace();
			
		}
		
	}
	
	
	static String checkIfElementPresent(String tempXpath) {
		
		String result="";
		try {
			List<WebElement>  elementList=driver.findElements(By.xpath(tempXpath));
			
			if(elementList.size()==0) result= "false";
			else if(elementList.size()==1) result= "true";
			else result= "multiple";
		}
		catch(NoSuchElementException e)
		{
			result="false";
		}
		return result;
		
	}	
	
	
	
	static String fixFaultyProperties(String tempXpath, List<String> li_FaultyProperties) {
	
//		int numberOfFaultyProperties=li_FaultyProperties.size();
		
		String result="";
		try {
			
			WebElement  element=driver.findElement(By.xpath(tempXpath));
			
			for(String singlePropertyThread:li_FaultyProperties)
			{
				String[] arr=singlePropertyThread.split(";;");
//				String strPropertyName=arr[0].replaceAll("name:=", "");
				
//				String strPropertyValue_Old=arr[1].replaceAll("value:=", "");
				String strPropertyValue_New="" + element.getAttribute(arr[0].replaceAll("name:=", ""));
				
				if(!strPropertyValue_New.equalsIgnoreCase("null"))
				{
					String strNameProperty1_KeyValue=arr[0];
					String strProperty2_KeyValue="value:=" + strPropertyValue_New;
				
					replaceExistingPropertyValueFromXmlFile(strNameProperty1_KeyValue, strProperty2_KeyValue);
				}
			}
			
			
		}
		catch(NoSuchElementException e)
		{
			result="false";
		}
		return result;
		
	}	
	
	
	

	public static void replaceExistingPropertyValueFromXmlFile(String strNameProperty1_KeyValue, String strProperty2_KeyValue){
		
		String file_path=strObjectFilePath;
		try {
//			if (file_path.startsWith("\\Object Repository\\") || file_path.startsWith("/Object Repository/")) {
//				file_path = System.getProperty("user.dir") + file_path;
//			} else {
//				file_path = System.getProperty("user.dir") + "/Object Repository/" + file_path;
//			}

			
			String[] arr=strNameProperty1_KeyValue.split(":=");
			String attributeName1=arr[0];
			String attributeName1_Value=arr[1];
			
			
			String[] arr2=strProperty2_KeyValue.split(":=");
			String subAttributeName1=arr2[0];
			String subAttributeName1_Value=arr2[1];
			
			
			SAXBuilder builder = new SAXBuilder();
			File xmlFile = new File(file_path);

			Document doc = (Document) builder.build(xmlFile);
			Element rootNode = doc.getRootElement();

			List<Element> list_properties = rootNode.getChildren("webElementProperties");

			for (Element list : list_properties) {
//			System.err.println(list.getChild("name").getValue());
				
//				if (list.getChild("name").getValue().toString().trim().equalsIgnoreCase(nodeName_to_update)) {
//					list.getChild("value").setText(nodeValueToUpdate);
				if (list.getChild(attributeName1).getValue().toString().trim().equalsIgnoreCase(attributeName1_Value)) {
					list.getChild(subAttributeName1).setText(subAttributeName1_Value);
				}
			}

			XMLOutputter xmlOutput = new XMLOutputter();

			// display nice nice
//			xmlOutput.setFormat(Format.getPrettyFormat());
			xmlOutput.output(doc, new FileWriter(file_path));

			// xmlOutput.output(doc, System.out);

			System.out.println("File updated!");
			
		} catch (JDOMException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		 catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static List checkForElementWithOtherProperties(List<String> li_SelectedProperties, List<String> li_AllUsefull,List<String> li_OtherProperties, List<String> li_FaultyProprties)
	{
		List<String> list_Xpath_and_ToBeselectedProperty=new ArrayList();
		String strXpath="";
//		String strAllUsefullProperties="";
//		for(String tempStr:li_AllUsefull)
//			strAllUsefullProperties=strAllUsefullProperties + "##" + tempStr;
		for(String str1:li_OtherProperties)
		{
			String tempStr="";
			boolean isPropertyMatched=false;
			for(String strUsefullPropertyThread:li_AllUsefull)	//check if proposed property is present in object (as in unselected form)
			{
				if(strUsefullPropertyThread.contains("name:=" + str1))
				{
					isPropertyMatched=true;
					tempStr=strUsefullPropertyThread;
					break;
					
				}
				
			}
			
			
			boolean isPropertyDuplicate=false;
			for(String strSelectedPropertyThread:li_SelectedProperties)	//check if proposed property is already not selected
			{
				if(strSelectedPropertyThread.contains("name:=" + str1))
				{
					isPropertyDuplicate=true;
					break;
					
				}
				
			}
				
			if(isPropertyMatched && !isPropertyDuplicate)
			{
				List<String> li_SelectedProperty_ButUseFullPropertiesONly=new ArrayList();
				for(String selectedPropertyThread:li_SelectedProperties)
				{
					boolean faultyPropertyFound=false;
					for(String faultyPropertyThread:li_FaultyProprties)
					{
						
						String[] arrFaultyP=faultyPropertyThread.split(";");
						String faultyPropertyName=arrFaultyP[0];
						if(selectedPropertyThread.contains(faultyPropertyName))
						{
							faultyPropertyFound=true;
							break;
						}
						
					}
					
					if(!faultyPropertyFound) li_SelectedProperty_ButUseFullPropertiesONly.add(selectedPropertyThread);
					
				}
				
				li_SelectedProperty_ButUseFullPropertiesONly.add(tempStr);
				strXpath=SmartIdentifier.xpathBuilderFromListOfProperties(li_SelectedProperty_ButUseFullPropertiesONly, "ignore");
				String isElementPresent=ReUsableActions.checkIfElementPresent(strXpath);
				if(isElementPresent.equals("true"))
				{
					list_Xpath_and_ToBeselectedProperty.add(strXpath);
					list_Xpath_and_ToBeselectedProperty.add(tempStr);
					break;
				}
				else
				{
					li_SelectedProperty_ButUseFullPropertiesONly.remove(tempStr);
					strXpath="";
				}
				
			}
			
		}
		
		if(strXpath.length()==0)
		{
			
			list_Xpath_and_ToBeselectedProperty.add("");
			list_Xpath_and_ToBeselectedProperty.add("");
		}
		
		return list_Xpath_and_ToBeselectedProperty;
	}

}
