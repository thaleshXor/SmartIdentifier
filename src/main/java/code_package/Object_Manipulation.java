package code_package;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;
import org.jdom.output.Format;
import org.jdom.output.XMLOutputter;

public class Object_Manipulation {

	public static void create_object(Map<String, String> object_properties) {
		try {
			Element WebElementEntity = new Element("WebElementEntity");
			Document doc = new Document(WebElementEntity);
			doc.setRootElement(WebElementEntity);
			String objectname = "";

			int prop_count = 0;
			for (String key_value : object_properties.keySet()) {
				String val = object_properties.get(key_value).trim();
				if (val != null && val.equalsIgnoreCase("") == false && !val.equalsIgnoreCase("undefined")
						&& !val.equalsIgnoreCase("null")) {
//					if (!val.equalsIgnoreCase("undefined") || !val.equalsIgnoreCase("null")) {
					Element webElementProperties = new Element("webElementProperties");
					webElementProperties.addContent(new Element("matchCondition").setText("equals"));
					webElementProperties.addContent(new Element("name").setText(key_value.trim()));
					webElementProperties.addContent(new Element("value").setText(object_properties.get(key_value).trim()));
					if(key_value.trim().toLowerCase().contains("class") || key_value.trim().toLowerCase().contains("tag") || key_value.trim().toLowerCase().contains("text") || key_value.trim().toLowerCase().contains("id") || key_value.trim().toLowerCase().contains("value")) {
						webElementProperties.addContent(new Element("isSelected").setText("true"));
					}else {
						webElementProperties.addContent(new Element("isSelected").setText("false"));
					}
					doc.getRootElement().addContent(webElementProperties);
					if(prop_count < 2) {
						objectname = objectname.trim() + object_properties.get(key_value).trim() + "_";
						objectname = objectname.trim();
					}
					prop_count++;
//				}
				}

			}

			if (prop_count > 1) {
				if(objectname.endsWith("_")) {
					objectname = objectname.substring(0, objectname.length() - 1).trim();
				}
				Element object_name = new Element("Name")
						.setText(objectname.trim());

				doc.getRootElement().addContent(object_name);
				XMLOutputter xmlOutput = new XMLOutputter();
				xmlOutput.setFormat(Format.getPrettyFormat());
				xmlOutput.output(doc,
						new FileWriter(new File(System.getProperty("user.dir") + "/Object Repository/"
								+ Object_Record.page_title + "/" + objectname.replace(" ", "_").trim() + ".xml")));
				System.out.println("File Saved!");
			}

		} catch (Exception e) {
			// TODO: handle exception
		}
	}
	
	
	
	
	public static void create_object_runtime(Map<String, String> object_properties) {
		try {
			
			File page_title_folder = new File(System.getProperty("user.dir") + "/Object Repository/runtime_objects/");
			page_title_folder.mkdirs();
			Element WebElementEntity = new Element("WebElementEntity");
			Document doc = new Document(WebElementEntity);
			doc.setRootElement(WebElementEntity);
			String objectname = "";

			int prop_count = 0;
			for (String key_value : object_properties.keySet()) {
				String val = object_properties.get(key_value).trim();
				if (val != null && val.equalsIgnoreCase("") == false && !val.equalsIgnoreCase("undefined")
						&& !val.equalsIgnoreCase("null")) {
//					if (!val.equalsIgnoreCase("undefined") || !val.equalsIgnoreCase("null")) {
					Element webElementProperties = new Element("webElementProperties");
					webElementProperties.addContent(new Element("matchCondition").setText("equals"));
					webElementProperties.addContent(new Element("name").setText(key_value.trim()));
					webElementProperties.addContent(new Element("value").setText(object_properties.get(key_value).trim()));
					if(key_value.trim().toLowerCase().contains("class") || key_value.trim().toLowerCase().contains("tag") || key_value.trim().toLowerCase().contains("text") || key_value.trim().toLowerCase().contains("id") || key_value.trim().toLowerCase().contains("value")) {
						webElementProperties.addContent(new Element("isSelected").setText("true"));
					}else {
						webElementProperties.addContent(new Element("isSelected").setText("false"));
					}
					doc.getRootElement().addContent(webElementProperties);
					if(prop_count < 2) {
						objectname = objectname.trim() + object_properties.get(key_value).trim() + "_";
						objectname = objectname.trim();
					}
					prop_count++;
//				}
				}

			}

			if (prop_count > 1) {
				if(objectname.endsWith("_")) {
					objectname = objectname.substring(0, objectname.length() - 1).trim();
				}
				Element object_name = new Element("Name")
						.setText(objectname.trim());

				doc.getRootElement().addContent(object_name);
				XMLOutputter xmlOutput = new XMLOutputter();
				xmlOutput.setFormat(Format.getPrettyFormat());
				xmlOutput.output(doc,
						new FileWriter(new File(System.getProperty("user.dir") + "/Object Repository/runtime_objects/"
								+ "/" + objectname.replace(" ", "_").trim() + ".xml")));
				System.out.println("File Saved!");
			}

		} catch (Exception e) {
			// TODO: handle exception
		}
	}

	public static void update_object(String file_path, String node_to_update, String value_to_update)
			throws JDOMException {
		try {
			if (file_path.startsWith("\\Object Repository\\") || file_path.startsWith("/Object Repository/")) {
				file_path = System.getProperty("user.dir") + file_path;
			} else {
				file_path = System.getProperty("user.dir") + "/Object Repository/" + file_path;
			}

			SAXBuilder builder = new SAXBuilder();
			File xmlFile = new File(file_path);

			Document doc = (Document) builder.build(xmlFile);
			Element rootNode = doc.getRootElement();

			List<Element> list_properties = rootNode.getChildren("webElementProperties");

			for (Element list : list_properties) {
//			System.err.println(list.getChild("name").getValue());
				if (list.getChild("name").getValue().toString().trim().equalsIgnoreCase(node_to_update)) {
					list.getChild("value").setText(value_to_update);
				}
			}

			XMLOutputter xmlOutput = new XMLOutputter();

			// display nice nice
			xmlOutput.setFormat(Format.getPrettyFormat());
			xmlOutput.output(doc, new FileWriter(file_path));

			// xmlOutput.output(doc, System.out);

			System.out.println("File updated!");

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
