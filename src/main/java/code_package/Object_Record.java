package code_package;

import java.awt.Desktop;
import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;

import org.apache.commons.io.FileUtils;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;

// The tutorial can be found just here on the SSaurel's Blog : 
// https://www.ssaurel.com/blog/create-a-simple-http-web-server-in-java
// Each Client Connection will be managed in a dedicated Thread
public class Object_Record implements Runnable {

	static final File WEB_ROOT = new File(".");
	static final String DEFAULT_FILE = "index.html";
	static final String FILE_NOT_FOUND = "404.html";
	static final String METHOD_NOT_SUPPORTED = "not_supported.html";
	// port to listen connection
	static final int PORT = 5397;
	public static String page_title;
	public static Collection<File> ObjectRepo;
	public static File script_file;
	public static String script_file_path;
	public static String old_object_path;
	// verbose mode
	static final boolean verbose = true;

	// Client Connection via Socket Class
	private Socket connect;

	public Object_Record(Socket c) {
		connect = c;
	}

	public static void main(String[] args) {
		script_file_path = System.getProperty("user.dir") + File.separator + "Script.txt";
		script_file = new File(script_file_path);
		try {
			ServerSocket serverConnect = new ServerSocket(PORT);
			System.out.println("Server started.\nListening for connections on port : " + PORT + " ...\n");

			// we listen until user halts server execution
			while (true) {
				Object_Record myServer = new Object_Record(serverConnect.accept());

				if (verbose) {
//					System.out.println("Connecton opened. (" + new Date() + ")");
				}

				// create dedicated thread to manage the client connection
				Thread thread = new Thread(myServer);
				thread.start();
			}

		} catch (IOException e) {
			System.err.println("Server Connection error : " + e.getMessage());
		}
	}

	public void run() {
		// we manage our particular client connection
		BufferedReader in = null;
		PrintWriter out = null;
		BufferedOutputStream dataOut = null;
		String fileRequested = null;

		try {
			// we read characters from the client via input stream on the socket
			in = new BufferedReader(new InputStreamReader(connect.getInputStream()));
			// we get character output stream to client (for headers)
			out = new PrintWriter(connect.getOutputStream());
			// get binary output stream to client (for requested data)
			dataOut = new BufferedOutputStream(connect.getOutputStream());

			// get first line of the request from the client
			String input = in.readLine();
			// we parse the request with a string tokenizer
			StringTokenizer parse = new StringTokenizer(input);
			String method = parse.nextToken().toUpperCase(); // we get the HTTP method of the client
			// we get file requested
			fileRequested = parse.nextToken();

			System.err.println("file requested: " + fileRequested);
			if (fileRequested.contains("Page_title%%")) {
				page_title = fileRequested.split("Page_title%%")[1].replace("%20", "_").trim();
				File page_title_folder = new File(System.getProperty("user.dir") + "/Object Repository/" + page_title);
				page_title_folder.mkdirs();
			} else if (fileRequested.toLowerCase().contains("/attributes%%")) {
				Map<String, String> object_properties = new HashMap<String, String>();
				List<String> attr_list = new ArrayList<String>();
				fileRequested = fileRequested.replace("%20", " ").replace("/attributes%%,", "").trim();
				if (fileRequested.endsWith(",")) {
					fileRequested = fileRequested.substring(0, fileRequested.length() - 1);
				}
				attr_list = Arrays.asList(fileRequested.split(", "));

				for (String attr : attr_list) {
					System.err.println(attr);
					String[] key_val = attr.split("%% ");
					if (key_val.length > 1) {
						object_properties.put(key_val[0], key_val[1]);
					}
				}

				Object_Manipulation.create_object(object_properties);
			} else if (fileRequested.toLowerCase().contains("/attributes_runtime%%")) {
				Map<String, String> object_properties = new HashMap<String, String>();
				List<String> attr_list = new ArrayList<String>();
				fileRequested = fileRequested.replace("%20", " ").replace("/attributes_runtime%%,", "").trim();
				if (fileRequested.endsWith(",")) {
					fileRequested = fileRequested.substring(0, fileRequested.length() - 1);
				}
				attr_list = Arrays.asList(fileRequested.split(", "));

				for (String attr : attr_list) {
					System.err.println(attr);
					String[] key_val = attr.split("%% ");
					if (key_val.length > 1) {
						object_properties.put(key_val[0], key_val[1]);
					}
				}
				Object_Manipulation.create_object_runtime(object_properties);
			}else if (fileRequested.toLowerCase().contains("/record_operation%%")) {
				record_script(fileRequested);
			} else if (fileRequested.toLowerCase().contains("/start")) {
				FileUtils.deleteQuietly(new File(System.getProperty("user.dir") + File.separator + "Script.txt"));
			} else if (fileRequested.toLowerCase().contains("/stop")) {
				File Script_File = new File(System.getProperty("user.dir") + File.separator + "Script.txt");
				String Script_String = new String(
						Files.readAllBytes(Paths.get(System.getProperty("user.dir") + File.separator + "Script.txt")));
				StringSelection stringSelection = new StringSelection(Script_String);
				Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
				clipboard.setContents(stringSelection, null);
				Desktop.getDesktop().open(Script_File);
			}
			// we support only GET and HEAD methods, we check
			if (!method.equals("GET") && !method.equals("HEAD")) {
				if (verbose) {
					System.out.println("501 Not Implemented : " + method + " method.");
				}
				out.println("HTTP/1.1 501 Not Implemented");
				out.println("Server: Java HTTP Server from SSaurel : 1.0");
				out.println("Date: " + new Date());
				out.println("Content-type: ");
				out.println("Content-length: 0");
				out.println(); // blank line between headers and content, very important !
				out.flush(); // flush character output stream buffer
				dataOut.flush();

			} else {
				out.println("HTTP/1.1 200 OK");
				out.println("Server: Java HTTP Server from SSaurel : 1.0");
				out.println("Date: " + new Date());
				out.println("Content-type: ");
				out.println("Content-length: 0");
				out.println(); // blank line between headers and content, very important !
				out.flush(); // flush character output stream buffer
				dataOut.flush();
			}

		} catch (FileNotFoundException fnfe) {

		} catch (IOException ioe) {
			System.err.println("Server error : " + ioe);
		} catch (JDOMException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			try {
				in.close();
				out.close();
				dataOut.close();
				connect.close(); // we close socket connection
			} catch (Exception e) {
				System.err.println("Error closing stream : " + e.getMessage());
			}

			if (verbose) {
//				System.out.println("Connection closed.\n");
			}
		}
	}

	public void record_script(String fileRequested) throws JDOMException, IOException {
		String script_line = "";
		String[] ObjectRepoExtension = { "xml" };
		boolean recursive = true;
		ObjectRepo = FileUtils.listFiles(new File(System.getProperty("user.dir")), ObjectRepoExtension, recursive);
		String[] operation_string = fileRequested.replace("%20", " ").replace("/Record_Operation%% ", "").split(", ");
		String operation = operation_string[0].trim();
		String type_value = operation_string[1].split("%% ")[1];
		String tagname_value = operation_string[2].split("%% ")[1];
		String name_value = operation_string[3].split("%% ")[1];
		String object_path = get_object_name(type_value, tagname_value, name_value)
				.replace(System.getProperty("user.dir"), "");
		if (operation.equalsIgnoreCase("click")) {
			script_line = "ReUsableActions.xorClick(\"" + object_path + "\");";
			if (script_file.exists()) {
				FileWriter fileWriter = new FileWriter(script_file_path, true);
				PrintWriter writer = new PrintWriter(fileWriter);
				writer.append(script_line + "\n");
				writer.close();
			} else {
				PrintWriter writer = new PrintWriter(script_file_path, "UTF-8");
				writer.print(object_path + "\n");
				writer.close();
			}
			old_object_path = object_path;
		} else if (operation.equalsIgnoreCase("settext")) {
			String input_text = operation_string[4].split("%% ")[1];
			script_line = "ReUsableActions.xorSetText(\"" + object_path + "\",\"" + input_text + "\");";

			if (script_file.exists()) {
				BufferedReader input = new BufferedReader(new FileReader(script_file_path));
				List<String> fileList = new ArrayList<String>();
				String line;
				while ((line = input.readLine()) != null) {
					fileList.add(line);
				}

				if (fileList.get(fileList.size() - 1).contains(object_path)) {
					fileList.remove(fileList.size() - 1);
					fileList.add(script_line);
					String file_String = null;
					for (String string : fileList) {
						if (file_String == null) {
							file_String = string + "\n";
						} else {
							file_String = file_String + string + "\n";
						}
					}
					PrintWriter writer = new PrintWriter(script_file_path, "UTF-8");
					writer.print(file_String);
					writer.close();
				} else {
					FileWriter fileWriter = new FileWriter(script_file_path, true);
					PrintWriter writer = new PrintWriter(fileWriter);
					writer.append(script_line + "\n");
					writer.close();
				}
			} else {
				PrintWriter writer = new PrintWriter(script_file_path, "UTF-8");
				writer.print(object_path + "\n");
				writer.close();
			}
			old_object_path = object_path;
		}
	}

	public String get_object_name(String type_value, String tagname_value, String name_value)
			throws JDOMException, IOException {
		String object_path = "";
		for (File file : ObjectRepo) {
			boolean match = false;
			SAXBuilder builder = new SAXBuilder();
			Document doc = (Document) builder.build(file);
			Element rootNode = doc.getRootElement();
			List<Element> list_properties = rootNode.getChildren("webElementProperties");
			for (Element list : list_properties) {
//				System.err.println(list.getChild("name").getValue());
				if (type_value != null && tagname_value != null && name_value != null) {
					if (list.getChild("name").getValue().toString().trim().equalsIgnoreCase("type")) {
						if (list.getChild("value").getValue().toString().trim().equalsIgnoreCase(type_value)) {
							match = true;
						} else {
							match = false;
							continue;
						}
					} else if (list.getChild("name").getValue().toString().trim().equalsIgnoreCase("tag")) {
						if (list.getChild("value").getValue().toString().trim().equalsIgnoreCase(tagname_value)) {
							match = true;
						} else {
							match = false;
							continue;
						}
					} else if (list.getChild("name").getValue().toString().trim().equalsIgnoreCase("name")) {
						if (list.getChild("value").getValue().toString().trim().equalsIgnoreCase(name_value)) {
							match = true;
						} else {
							match = false;
							continue;
						}
					}
				}
			}
			if (match) {
				object_path = file.getAbsolutePath();
				
				break;
			}
		}
		
		//String x="C:\\thalesh\\Automation\\Selenium\\Demo\\Smart_Identifier-master\\Smart_Identifier\\Object Repository\\Guru99_Bank_Home_Page\\";
		String y=object_path.replaceAll(".*Guru99_Bank_Home_Page.", "");
		String z=y.replaceAll(".xml", "");
		object_path=z;
		return object_path;
	}
}
