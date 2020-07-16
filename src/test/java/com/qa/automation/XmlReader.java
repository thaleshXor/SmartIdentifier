package com.qa.automation;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathFactory;

import org.w3c.dom.Document;
import org.w3c.dom.NodeList;


public class XmlReader 
{

    public String xmlToXpathBuilder(String objectName) throws Exception 
    {
	    	String filePath="Object Repository\\Guru99_Bank_Home_Page\\" + objectName + ".xml";
	    	ReUsableActions.strObjectFilePath=filePath;
	    	DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
    		DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
    		Document doc = dBuilder.parse(filePath);
             
    		doc.getDocumentElement().normalize();
    		
    		XPathFactory xpf = XPathFactory.newInstance();
    		XPath path = xpf.newXPath();
    		String strTag="*";
    		String strXpath="";
    		String strText="";
    		String strTextCondition="";
    		String strOther="";
    		String strAppender="";
    		int counter=0;

//---------------------------------------------Find total number of webElementProperties----------------------
    		

    		XPathExpression webPropertiesExpr = path.compile("/WebElementEntity/webElementProperties");
    		NodeList nList = (NodeList)webPropertiesExpr.evaluate(doc, XPathConstants.NODESET);
    		int intWebPropertiesCount=nList.getLength();

//----------------------------------Add each property in xpath-------------------------------------
    		
    		for(int i=1;i<=intWebPropertiesCount;i++)
    		{
    			strOther ="";
    			if(counter>0)strAppender=" and ";
    					
    			String x="/WebElementEntity/webElementProperties[" + i + "]/isSelected";
    			XPathExpression nodeTagNameExpr = path.compile(x);
    			String strTextOfNode=nodeTagNameExpr.evaluate(doc);
    			
    			if(strTextOfNode.equals("true"))
    			{
    				x="/WebElementEntity/webElementProperties[" + i + "]/name";
    				nodeTagNameExpr = path.compile(x);
    				strTextOfNode=nodeTagNameExpr.evaluate(doc);
    				
					x="/WebElementEntity/webElementProperties[" + i + "]/value";
    				nodeTagNameExpr = path.compile(x);
    				String strValue=nodeTagNameExpr.evaluate(doc);
    				
    				
					x="/WebElementEntity/webElementProperties[" + i + "]/matchCondition";
    				nodeTagNameExpr = path.compile(x);
    				String strMatchCond=nodeTagNameExpr.evaluate(doc);
    				
    				
    				
    				if(strTextOfNode.contains("tag"))
    				{
         				strTag=strValue;
    				}
    				
    				else if(strTextOfNode.equals("text"))
    				{
        				strText=strValue;
        				strTextCondition=strMatchCond;
    					
    				}
    				
    				else
    				{


        				if(strMatchCond.equals("equals"))
        				{
        					strXpath=strXpath + strAppender + "@" + strTextOfNode + "='" + strValue + "'";
         				}
        				
        				else
        				{
        					
        					strXpath=strXpath + strAppender + "contains(@" + strTextOfNode + ",'" + strValue + "')";
        				}
        				
        				counter++;
        				
    					
    				}
    			}//end if property selected 
    			
    			
    			
    		}//for
    		
    		
    		if(strXpath.length()!=0)
    		{
		    		strXpath="//" + strTag + "[" + strXpath;
		    		
		    		    		
		    		if(!strText.equals(""))
		    		{
		    			if(strTextCondition.equals("equals"))
		    			{
		    				
		    				strXpath=strXpath + " and text()='" + strText + "'";
		    			}
		    			
		    			else
		    			{
		    				
		    				strXpath=strXpath + " and contains(text(),'" + strText + "')";
		    			}
		    			
		    		}
		    		
		    		strXpath=strXpath + "]";
    		}
    		else
    		{
    			strXpath="//" + strTag;
    		}
    		
    		return strXpath;
    		
      }

}