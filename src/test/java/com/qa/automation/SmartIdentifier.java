package com.qa.automation;

import java.io.IOException;
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

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class SmartIdentifier extends ReUsableActions{
//	static WebDriver driver2;
	static String triggerSmartIdentifier(WebDriver driver) {
		String optimizedXpath="";
//		driver2=driver;
		String filePath=ReUsableActions.strObjectFilePath;
		List<String> li_Original_AllProperties = getListOfAllObjectProperties(filePath); 			//get list of all object properties
		List<String> li_SelectedProperties=getSelectedPropertiesList(li_Original_AllProperties);	//get list of properties which are already selected
		List<String> li_AllPrimaryProperties=getPriorityList("primary");							//get list of all Primary properties
		List<String> li_AllSecondaryProperties=getPriorityList("secondary");						//get list of all Secondary properties
		List<String> li_AllAdditionalProperties=getPriorityList("additional");						//get list of all Additional properties
		List<List<String>> li2=getRefurbishedListOfProperties(li_Original_AllProperties, driver);	
		List<String> li_New_AllUsefullProperties =li2.get(0);										//get list of only useful properties
		List<String> li_FaultyProprties =li2.get(1);												//get list of faulty properties
		String oldXpath=xpathBuilderFromListOfProperties(li_Original_AllProperties, "true");		//get Xpath based on originally selected properties
		String newXpath=xpathBuilderFromListOfProperties(li_New_AllUsefullProperties, "true");		//get Xpath based on Useful Properties
		

		String isElementPresent=ReUsableActions.checkIfElementPresent(newXpath);
		if(isElementPresent.equalsIgnoreCase("true")) 
		{
			optimizedXpath=newXpath;
			if(li_FaultyProprties.size()>0)
			{
				ReUsableActions.fixFaultyProperties(optimizedXpath, li_FaultyProprties);
			}
		}
		else
		{
							
			List<String> li_temp=ReUsableActions.checkForElementWithOtherProperties(li_SelectedProperties, li_New_AllUsefullProperties,li_AllPrimaryProperties, li_FaultyProprties);
			String tempXpath=li_temp.get(0);
			String toBeSelectedProperty=li_temp.get(1);
			
			if(tempXpath.length()!=0)
			{
				optimizedXpath=tempXpath;
				
				if(toBeSelectedProperty.length()>0)
				{
					String[] arr=toBeSelectedProperty.split(";;");
					String strNameProperty1_KeyValue=arr[0];
					String strProperty2_KeyValue="isSelected:=true";
					
					replaceExistingPropertyValueFromXmlFile(strNameProperty1_KeyValue, strProperty2_KeyValue);
				}
				
				if(li_FaultyProprties.size()>0)
				{
					ReUsableActions.fixFaultyProperties(optimizedXpath, li_FaultyProprties);
				}

				
			}
			
			else
			{
				
				li_temp=null;
				li_temp=ReUsableActions.checkForElementWithOtherProperties(li_SelectedProperties, li_New_AllUsefullProperties,li_AllSecondaryProperties, li_FaultyProprties);
				tempXpath=li_temp.get(0);
				toBeSelectedProperty=li_temp.get(1);
				
				if(tempXpath.length()!=0)
				{
					optimizedXpath=tempXpath;
					
					if(toBeSelectedProperty.length()>0)
					{
						String[] arr=toBeSelectedProperty.split(";;");
						String strNameProperty1_KeyValue=arr[0];
						String strProperty2_KeyValue="isSelected:=true";
						
						replaceExistingPropertyValueFromXmlFile(strNameProperty1_KeyValue, strProperty2_KeyValue);
					}
					
					if(li_FaultyProprties.size()>0)
					{
						ReUsableActions.fixFaultyProperties(optimizedXpath, li_FaultyProprties);
					}
				}
				
				else
					
				{
					
					li_temp=null;
					li_temp=ReUsableActions.checkForElementWithOtherProperties(li_SelectedProperties, li_New_AllUsefullProperties,li_AllAdditionalProperties, li_FaultyProprties);
					tempXpath=li_temp.get(0);
					toBeSelectedProperty=li_temp.get(1);
					
					if(tempXpath.length()!=0)
					{
						optimizedXpath=tempXpath;
						
						if(toBeSelectedProperty.length()>0)
						{
							String[] arr=toBeSelectedProperty.split(";;");
							String strNameProperty1_KeyValue=arr[0];
							String strProperty2_KeyValue="isSelected:=true";
							
							replaceExistingPropertyValueFromXmlFile(strNameProperty1_KeyValue, strProperty2_KeyValue);
						}
						
						if(li_FaultyProprties.size()>0)
						{
							ReUsableActions.fixFaultyProperties(optimizedXpath, li_FaultyProprties);
						}
					}
					
					
					
					
				} //else
				

				
			}//else
			
		}//else
			
			
			

		

		return optimizedXpath;
	}
	
	
	static List getListOfAllObjectProperties(String filePath)  {
		XPathFactory xpf = XPathFactory.newInstance();
		XPath path = xpf.newXPath();
		String strTag="*";
		String strXpath="";
		String strText="";
		String strTextCondition="";
		String strOther="";
		String strAppender="";
		int counter=0;
		List<String> li=new ArrayList();
		DocumentBuilder dBuilder;
		Document doc;
		XPathExpression webPropertiesExpr;
		NodeList nList;

//		String strObjFilePath=filePath.replaceAll("\\", "/");
		String strObjFilePath=filePath;
		

		try {
			DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
			dBuilder= dbFactory.newDocumentBuilder();
			doc = dBuilder.parse(strObjFilePath);
			doc.getDocumentElement().normalize();

			//---------------------------------------------Find total number of webElementProperties----------------------

			webPropertiesExpr = path.compile("/WebElementEntity/webElementProperties");
			nList = (NodeList)webPropertiesExpr.evaluate(doc, XPathConstants.NODESET);
			int intWebPropertiesCount=nList.getLength();

			

			for(int i=1;i<=intWebPropertiesCount;i++)
			{
				String strProperties="";
				String[] arr=new String[4];

				String propertyXpath="/WebElementEntity/webElementProperties[" + i + "]/name";
				XPathExpression nodeTagNameExpr = path.compile(propertyXpath);
				String strProperty_Name=nodeTagNameExpr.evaluate(doc);
				
				if(strProperty_Name.equals("xpath")) continue;

				propertyXpath="/WebElementEntity/webElementProperties[" + i + "]/value";
				nodeTagNameExpr = path.compile(propertyXpath);
				String strProperty_NameValue=nodeTagNameExpr.evaluate(doc);

				propertyXpath="/WebElementEntity/webElementProperties[" + i + "]/isSelected";
				nodeTagNameExpr = path.compile(propertyXpath);
				String strProperty_isSelectedValue=nodeTagNameExpr.evaluate(doc);

				propertyXpath="/WebElementEntity/webElementProperties[" + i + "]/matchCondition";
				nodeTagNameExpr = path.compile(propertyXpath);
				String strProperty_MatchConditionValue=nodeTagNameExpr.evaluate(doc);
				
				strProperties="name" + ":=" + strProperty_Name;
				strProperties=strProperties + ";;" + "value" + ":=" +strProperty_NameValue;
				strProperties=strProperties + ";;" +"isSelected" + ":=" +strProperty_isSelectedValue;
				strProperties=strProperties + ";;" +"matchCondition" + ":=" +strProperty_MatchConditionValue;

				li.add(strProperties);
			}
		} catch (XPathExpressionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ParserConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SAXException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return li;
	}	
	
	
	static List getRefurbishedListOfProperties(List<String> li, WebDriver driver) {

		String isElementPresent="";
		List<List<String>> li_Parent=new ArrayList();
		List<String> li2_FoundElementProperties=new ArrayList<String>();
		List<String> li3_FaultyProperties=new ArrayList<String>();
		
		for(int i=0;i<li.size();i++)
		{
			String propertyThread=li.get(i);
			if(propertyThread.contains("tag;;")) 
			{
				li2_FoundElementProperties.add(propertyThread);
				continue;
			}
			
			String[] multipleSubProperties=propertyThread.split(";;");
			String strName=multipleSubProperties[0].replaceAll("name:=", "");
			String strValue="" + multipleSubProperties[1].replaceAll("value:=", "");
			String strParam=strName + ":=" + strValue;
			String tempXpath= SmartIdentifier.xpathBuilder(strParam);
//			isElementPresent=checkIfElementPresent(tempXpath, driver);
			isElementPresent=ReUsableActions.checkIfElementPresent(tempXpath);
			if(isElementPresent.equals("true") || isElementPresent.equals("multiple")) li2_FoundElementProperties.add(propertyThread);
			if(isElementPresent.equals("false")) li3_FaultyProperties.add(propertyThread);
			
		}
		
		li_Parent.add(li2_FoundElementProperties);
		li_Parent.add(li3_FaultyProperties);
 		return li_Parent;
	}

	/*-----------------------------------------------xpathBuilder---------------------------------------
	input parameters count : any number of string parameters //Varargs
	Input parameter names : String... properties
	Parameter input data format >>
	String... : 	  "tag:=input
					  "id:=username
					  "class:=inLine
	-------------------------------------------------------------------------------------------------------------------------*/
	static String xpathBuilder(String property) {
		String tempXpath="";

		String prefix="//*";
		String strText="";
		String suffix="]";


			String[] arr=property.split(":=");
			
			if(arr.length>1)
			{
					
					String strPropName=arr[0] + "";
					String strPropValue=arr[1] + "";
		
					if(strPropName.equals("tag"))
					{
						prefix="//" + strPropValue;
					}
					else if(strPropName.equals("text"))
					{
						strText="contains(text(),'" + strPropValue + "')";
					}
					else
					{
						strText= "contains(@" + strPropName + ",'" + strPropValue + "')";
						
					}
		
					if(strText.length()!=0)
					{
		
						tempXpath=prefix + "["  + strText + suffix;
					}
					else
					{
						tempXpath=prefix;
						
					}
			}
			else
			{
				
				tempXpath="//*[@" + arr[0] + ",'<<invalid xpath>>']";
			}

		return tempXpath;
	}

	/*-----------------------------------------------xpathBuilderFromListOfProperties---------------------------------------
	input parameters count : 2
	Input parameter names : List<String> li, String considerSelectedPropertiesOnly
	Parameter input data format >>
	List<String> li : "name:=tag;;value:=input;;isSelected:=true;;matchCondition:=equals
					  "name:=id;;value:=username;;isSelected:=true;;matchCondition:=equals
					  "name:=class;;value:=inLine;;isSelected:=false;;matchCondition:=contains
					
	String considerSelectedPropertiesOnly : true	//will select only selected properties
										or  false	//will select only unselected properties
										or  ignore //this will select all provided properties
	
	-------------------------------------------------------------------------------------------------------------------------*/
	static String xpathBuilderFromListOfProperties(List<String> li, String considerSelectedPropertiesOnly) {
		String tempXpath="";
		String appeder="";
		String prefix="//*";
		String strText="";
		String suffix="]";
		int counter=0;
		for (String property : li)
		{
			
			if(considerSelectedPropertiesOnly.equalsIgnoreCase("true") && property.contains(":=false;;")) continue;
			if(considerSelectedPropertiesOnly.equalsIgnoreCase("false") && property.contains(":=true;;")) continue;
			
			
			if(counter>0)appeder=" and ";
			String[] arr=property.split(";;");

			if(property.contains("name:=tag;;"))
			{
				String strTagValue=arr[1].replaceAll("value:=", "");
				prefix="//" + strTagValue;
			}
			else if(property.contains("name:=text;;"))
			{
				String strTextValue=arr[1].replaceAll("value:=", "");
				strText="contains(text(),'" + strTextValue + "')";
			}
			else
			{
				String strTagNameValue=arr[0].replaceAll("name:=", "");
				String strTextValue=arr[1].replaceAll("value:=", "");
				tempXpath=tempXpath + appeder + "contains(@" + strTagNameValue + ",'" + strTextValue + "')";
				counter++;
			}


		}

		if(strText.length()==0) appeder="";
		if(tempXpath.length()!=0)
		{
			tempXpath=prefix + "[" + tempXpath + appeder + strText + suffix;
		}
		else
		{
			tempXpath=prefix;
		}

		return tempXpath;
	}	
	
	
	static boolean deleteThisProperty(String tempXpath) {
		List<WebElement> elements=driver.findElements(By.xpath(tempXpath));
		
		if(elements.size()>=0) return false;
		else return true;
	}
	
	static List getPriorityList(String propertyCategory)
	{
			
		List<String> li_propertiesPriorityList=new ArrayList();
		
		try {
			
			String category=propertyCategory;
			String filePath="src\\test\\java\\Hackathon\\Smart_Identifier\\PropertySelectionPriority.xml";

			DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
			Document doc = dBuilder.parse(filePath);
			 
			doc.getDocumentElement().normalize();
			
			XPathFactory xpf = XPathFactory.newInstance();
			XPath path = xpf.newXPath();
					
			String x="/propertiesCollection/properties[@id='" + category + "']";
			XPathExpression nodeTagNameExpr = path.compile(x);
			String strTextOfNode=nodeTagNameExpr.evaluate(doc).trim();
//			System.out.println(">>>>>>>>>>>" + strTextOfNode);
			String[] arr=strTextOfNode.split(";");
			for(String item : arr)
				li_propertiesPriorityList.add(item);
			
		} catch (XPathExpressionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ParserConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SAXException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		

		return li_propertiesPriorityList;
	}
	
	static List getSelectedPropertiesList(List<String> li)
	{
		
		List<String> li_return_selectedProperties=new ArrayList();
		
		for(String singlePropertyThread: li)
		{
//			String[] arr=singlePropertyThread.split(";;");
			if(singlePropertyThread.contains("isSelected:=true"))
			{
//				String strPropertyName=arr[0].replaceAll("name:=", "");
				li_return_selectedProperties.add(singlePropertyThread);
			}

		}
		
		
		return li_return_selectedProperties;
		
	}
}