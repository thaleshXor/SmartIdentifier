package com.qa.automation;

import java.io.IOException;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class Temp {

	public static void main(String[] args) throws XPathExpressionException, ParserConfigurationException, SAXException, IOException {

		String category="primary";
		String filePath="src\\test\\java\\Hackathon\\Smart_Identifier\\PropertySelectionPriority.xml";
		ReUsableActions.strObjectFilePath=filePath;
		DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
		Document doc = dBuilder.parse(filePath);
	     
		doc.getDocumentElement().normalize();
		
		XPathFactory xpf = XPathFactory.newInstance();
		XPath path = xpf.newXPath();

	
	try {

						
			String x="/propertiesCollection/properties[@id='primary']";
			XPathExpression nodeTagNameExpr = path.compile(x);
			String strTextOfNode=nodeTagNameExpr.evaluate(doc);
			System.out.println(">>>>>>>>>>>" + strTextOfNode);
			
			
		} catch (XPathExpressionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
			
			

	}

}
