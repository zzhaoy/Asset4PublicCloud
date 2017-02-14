package magentobatch;
 
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.BufferedInputStream;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

public class GenerateJSON {
	public static final String KEY_KIND = "kind";
	public static final String VALUE_RC = "ReplicationController";
	public static final String VALUE_SERVICE = "Service";
	public static final String KEY_API_VERSION = "apiVersion";
	public static final String VALUE_V1 = "v1";
	public static final String KEY_METADATA = "metadata";
	public static final String KEY_NAME = "name";
	public static final String KEY_SPEC = "spec";
	public static final String KEY_REPLICAS = "replicas";
	public static final String KEY_SELECTOR = "selector";
	public static final String KEY_CCE_APPGROUP = "cce/appgroup";
	public static final String KEY_TEMPLATE = "template";
	public static final String KEY_LABELS = "labels";
	public static final String KEY_CONTAINERS = "containers";
	public static final String KEY_TYPE = "type";
	public static final String VALUE_NODE_PORT = "NodePort";
	public static final String KEY_TARGET_PORT = "targetPort";
	public static final String KEY_IMAGE = "image";
	public static final String VALUE_IMAGE = "160.44.200.121:443/otc00000000001000001449/magento:10121939";
	public static final String KEY_IMAGE_POLICY = "imagePullPolicy";
	public static final String VALUE_IMAGE_POLICY = "IfNotPresent";
	public static final String KEY_PORTS = "ports";
	public static final String KEY_PORT = "port";
	public static final int VALUE_PORT = 80;
	public static final String KEY_NODE_PORT = "nodePort";
	public static final String KEY_CONTAINER_PORT = "containerPort";
	public static final String KEY_PROTOCOL = "protocol";
	public static final String VALUE_TCP = "TCP";
	public static final String RC_PREFIX = "rc";
	public static final String APP_PREFIX = "app";
	public static final String SERVICE_PREFIX = "service";
	public static final String JSON_POSTFIX = ".json";
	public static final String VALUE_CONTAINER_NAME = "magento";
	
	public GenerateJSON() {
		
	}
	
	public String CreateServiceJSON(int id, int node_port) {
		JSONObject root = new JSONObject();
		root.put(KEY_KIND, VALUE_SERVICE);
		root.put(KEY_API_VERSION, VALUE_V1);
		JSONObject metadata = new JSONObject();
		metadata.put(KEY_NAME, SERVICE_PREFIX+id);
		root.put(KEY_METADATA, metadata);
		JSONObject spec = new JSONObject();
		JSONObject app = new JSONObject();
		app.put(KEY_CCE_APPGROUP, APP_PREFIX+id);
		spec.put(KEY_SELECTOR, app);
		spec.put(KEY_TYPE, VALUE_NODE_PORT);
		JSONObject port1 = new JSONObject();
		port1.put(KEY_PORT, VALUE_PORT);
		port1.put(KEY_TARGET_PORT, VALUE_PORT);
		port1.put(KEY_PROTOCOL, VALUE_TCP);
		port1.put(KEY_NODE_PORT, node_port);
		JSONArray ports = new JSONArray();
		ports.add(port1);
		spec.put(KEY_PORTS, ports);
		root.put(KEY_SPEC, spec);
		
		try (FileWriter file = new FileWriter(SERVICE_PREFIX+id+JSON_POSTFIX)) {
			file.write(root.toJSONString());
			System.out.println("Successfully generated JSON file "+SERVICE_PREFIX+id+JSON_POSTFIX);
		} catch (IOException e) {
			e.printStackTrace();
		}

		return SERVICE_PREFIX+id+JSON_POSTFIX;
	}
	
	public String CreateRCJSON(int id) {
		JSONObject root = new JSONObject();
		root.put(KEY_KIND, VALUE_RC);
		root.put(KEY_API_VERSION, VALUE_V1);
		JSONObject metadata = new JSONObject();
		metadata.put(KEY_NAME, RC_PREFIX+id);
		root.put(KEY_METADATA, metadata);
		
		JSONObject specroot = new JSONObject();
		specroot.put(KEY_REPLICAS, 1);
		JSONObject app = new JSONObject();
		app.put(KEY_CCE_APPGROUP, APP_PREFIX+id);
		specroot.put(KEY_SELECTOR, app);
		JSONObject template = new JSONObject();
		metadata.put(KEY_LABELS, app);
		template.put(KEY_METADATA, metadata);
		
		
		JSONObject port1 = new JSONObject();
		port1.put(KEY_CONTAINER_PORT, VALUE_PORT);
		port1.put(KEY_PROTOCOL, VALUE_TCP);
		JSONArray ports = new JSONArray();
		ports.add(port1);
		JSONObject container1 = new JSONObject();
		container1.put(KEY_NAME, VALUE_CONTAINER_NAME+id);
		container1.put(KEY_IMAGE, VALUE_IMAGE);
		container1.put(KEY_IMAGE_POLICY, VALUE_IMAGE_POLICY);
		container1.put(KEY_PORTS, ports);
		JSONArray containers = new JSONArray();
		containers.add(container1);
		JSONObject spec = new JSONObject();
		spec.put(KEY_CONTAINERS, containers);
		
		template.put(KEY_SPEC, spec);
		specroot.put(KEY_TEMPLATE, template);
		root.put(KEY_SPEC, specroot);
		
		try (FileWriter file = new FileWriter(RC_PREFIX+id+JSON_POSTFIX)) {
			file.write(root.toJSONString());
			System.out.println("Successfully generated JSON file "+RC_PREFIX+id+JSON_POSTFIX);
		} catch (IOException e) {
			e.printStackTrace();
		}

		return RC_PREFIX+id+JSON_POSTFIX;
	}
	
	static String loadStream(InputStream in) throws IOException {   
		int ptr = 0;   
		in = new BufferedInputStream(in);   
		StringBuffer buffer = new StringBuffer();   
		while( (ptr = in.read()) != -1 ) {   
		buffer.append((char)ptr);   
		}   
		return buffer.toString();   
	} 
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		GenerateJSON gj = new GenerateJSON();
		if(args.length < 5) {
			System.out.println("run command with parameters <action> <start_id> <app_number> <start_port> <sleep_sec>");
			System.out.println("\t<action> : create | delete");
			System.out.println("\t<start_id> : first ID of app, following app will have increased ID adding to this");
			System.out.println("\t<app_number> : number of apps in this batch");
			System.out.println("\t<start_port> : first port of app, following app will have increased port adding to this, port range 30000-32767");
			System.out.println("\t<sleep_sec> : seconds to sleep in operating app");
		}
		else {
			String action = args[0];
			int start_id = Integer.parseInt(args[1]);
			int app_number = Integer.parseInt(args[2]);
			int start_port = Integer.parseInt(args[3]);
			int sleep_sec = Integer.parseInt(args[4]);
			System.out.println("Action : "+action);
			System.out.println("Start ID : "+start_id);
			System.out.println("App Number : "+app_number);
			System.out.println("Start Port : "+start_port);
			System.out.println("Sleep Second : "+sleep_sec);
			if(action.equals("create")) {
				System.out.println("===== Start creating Magento apps in batch! =====");
				for(int i = 0; i < app_number; i++) {
					int current_id = start_id+i;
					String rcjson = gj.CreateRCJSON(current_id);
					String servicejson = gj.CreateServiceJSON(current_id, i+start_port);
					String createrccmd = "kubectl create -f "+rcjson;
					String createservicecmd = "kubectl create -f "+servicejson;
					try {
					Thread.sleep(sleep_sec*1000);
					Process process = Runtime.getRuntime().exec (createrccmd);
					System.out.print(loadStream(process.getInputStream()));  
					Thread.sleep(100);
					Process process2 = Runtime.getRuntime().exec (createservicecmd);
					System.out.print(loadStream(process2.getInputStream()));
					System.out.println("-------- Magento app "+current_id+" successfully created! --------");
					} catch(IOException e) {
						e.printStackTrace();
						System.out.println("-------- Magento app "+current_id+" creation failed --------");
					} catch(InterruptedException e) {
						e.printStackTrace();
						System.out.println("-------- Magento app "+current_id+" creation failed --------");
					}
				}
			} else if (action.equals("delete")) {
				System.out.println("===== Start deleting Magento apps in batch! =====");
				for(int i = 0; i < app_number; i++) {
					int current_id = start_id+i;
					String deleteservoceccmd = "kubectl delete service "+SERVICE_PREFIX+current_id;
					String deleterccmd = "kubectl delete replicationcontroller "+RC_PREFIX+current_id;
					try {
					Thread.sleep(sleep_sec*1000);
					Process process = Runtime.getRuntime().exec (deleteservoceccmd);
					System.out.print(loadStream(process.getInputStream()));  
					Thread.sleep(100);
					Process process2 = Runtime.getRuntime().exec (deleterccmd);
					System.out.print(loadStream(process2.getInputStream()));
					System.out.println("-------- Magento app "+current_id+" successfully deleted! --------");
					} catch(IOException e) {
						e.printStackTrace();
						System.out.println("-------- Magento app "+current_id+" deletion failed --------");
					} catch(InterruptedException e) {
						e.printStackTrace();
						System.out.println("-------- Magento app "+current_id+" deletion failed --------");
					}
				}
			} else {
				System.out.println("run command with parameters <action> <start_id> <app_number> <start_port> <sleep_sec>");
				System.out.println("\t<action> : create | delete");
				System.out.println("\t<start_id> : first ID of app, following app will have increased ID adding to this");
				System.out.println("\t<app_number> : number of apps in this batch");
				System.out.println("\t<start_port> : first port of app, following app will have increased port adding to this, port range 30000-32767");
				System.out.println("\t<sleep_sec> : seconds to sleep in operating app");
			}
		}
	}

}
