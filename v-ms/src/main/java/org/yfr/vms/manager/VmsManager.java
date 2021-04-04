
package org.yfr.vms.manager;

import java.io.File;
import java.lang.management.ManagementFactory;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.SocketAddress;
import java.rmi.registry.LocateRegistry;
import java.util.HashMap;
import java.util.Map;

import javax.management.MBeanServer;
import javax.management.ObjectName;
import javax.management.remote.JMXConnectorServer;
import javax.management.remote.JMXConnectorServerFactory;
import javax.management.remote.JMXServiceURL;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.yfr.vms.connection.VmsConnectionManager;
import org.yfr.vms.vqueue.VQueueFactory;
import org.yfr.vms.vtopic.VTopicFactory;

/**
 * this class manage vqueues and vtopics by outer configuration .
 * 
 * <p>setup time <b>Oct 1, 2015 9:41:37 AM .</b></p>
 *
 * @author Vincent Huang
 */
public enum VmsManager {

	/** instance . */
	INSTANCE;

	private Log logger = LogFactory.getLog(VmsManager.class);

	private MBeanServer mbs = ManagementFactory.getPlatformMBeanServer();

	private JMXConnectorServer jmxConnectorServer = null;

	private ServerSocket serverSocket = null;

	/**
	 * parsing configuration and then initializing vqueues, vtopics and connections . 
	 * 
	 * @param isTestMode <b>(boolean)</b> is test mode pass in
	 * @param filePath <b>(String)</b> file path pass in
	 * 
	 * @throws Exception when initializing vms manager fail
	 */
	public void init(boolean isTestMode, String filePath) throws Exception {
		File xmlFile = null;
		if (isTestMode) {
			String resource = getClass().getClassLoader().getResource(filePath).getFile();
			xmlFile = new File(resource);
		} else {
			xmlFile = new File(filePath);
		}
		DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
		Document doc = dBuilder.parse(xmlFile);

		doc.getDocumentElement().normalize();

		initConnection(doc);
		initJmxService(doc);
		initVQueues(doc);
		initVTopics(doc);
	}

	/**
	 * initializing connections by outer configuration .
	 * 
	 * @param doc <b>(Document)</b> configuration document pass in
	 * 
	 * @throws Exception when initializing connection fail
	 */
	private void initConnection(Document doc) throws Exception {
		String ip = null;
		String port = null;

		NodeList connectionNode = doc.getElementsByTagName("Connection");
		for (int i = 0; i < connectionNode.getLength(); i++) {
			if (connectionNode.item(i).getNodeType() == Node.ELEMENT_NODE) {
				Element connection = (Element) connectionNode.item(i);

				ip = connection.getAttribute("ip");
				port = connection.getAttribute("port");
			}
		}

		SocketAddress address = new InetSocketAddress(ip, Integer.parseInt(port));
		serverSocket = new ServerSocket();
		serverSocket.bind(address);
		logger.info("init server socket : " + address.toString());

		new Thread(new VmsConnectionManager(serverSocket), "VmsConnectionManager").start();
	}

	/**
	 * initializing jmx setting by outer configuration .
	 * 
	 * @param doc <b>(Document)</b> configuration document pass in
	 * 
	 * @throws Exception when initializing jmx service fail
	 */
	private void initJmxService(Document doc) throws Exception {
		String ip = null;
		String port = null;
		String passwordFilePath = null;
		String accessFilePath = null;

		NodeList jmxSettingNode = doc.getElementsByTagName("JmxSetting");
		for (int i = 0; i < jmxSettingNode.getLength(); i++) {
			if (jmxSettingNode.item(i).getNodeType() == Node.ELEMENT_NODE) {
				Element jmxSetting = (Element) jmxSettingNode.item(i);

				ip = jmxSetting.getAttribute("ip");
				port = jmxSetting.getAttribute("port");
				passwordFilePath = jmxSetting.getAttribute("passwordFilePath");
				accessFilePath = jmxSetting.getAttribute("accessFilePath");
			}
		}

		LocateRegistry.createRegistry(Integer.parseInt(port));
		JMXServiceURL jmxServiceURL = new JMXServiceURL("service:jmx:rmi:///jndi/rmi://" + ip + ":" + port + "/jmxrmi");
		Map<String, Object> environment = new HashMap<String, Object>();
		environment.put("jmx.remote.x.password.file", passwordFilePath);
		environment.put("jmx.remote.x.access.file", accessFilePath);

		jmxConnectorServer = JMXConnectorServerFactory.newJMXConnectorServer(jmxServiceURL, environment, mbs);
		jmxConnectorServer.start();
	}

	/**
	 * initializing vqueues by outer configuration .
	 * 
	 * @param doc <b>(Document)</b> configuration document pass in
	 * 
	 * @throws Exception when initializing vqueues fail
	 */
	private void initVQueues(Document doc) throws Exception {
		String instanceName = null;
		String implementClassName = null;
		String objectName = null;
		String isLogging = null;
		String loggingFileName = null;
		String isCheckTimeout = null;
		String elementTimeoutTime_ms = null;
		String checkElementTimeoutPeriod_ms = null;
		String consumersCount = null;

		NodeList vQueues = doc.getElementsByTagName("VQueues").item(0).getChildNodes();
		for (int i = 0; i < vQueues.getLength(); i++) {
			if (vQueues.item(i).getNodeType() == Node.ELEMENT_NODE) {
				Element vQueue = (Element) vQueues.item(i);

				instanceName = vQueue.getAttribute("instanceName");
				implementClassName = vQueue.getAttribute("implementClassName");
				objectName = vQueue.getAttribute("objectName");
				isLogging = vQueue.getAttribute("isLogging");
				loggingFileName = vQueue.getAttribute("loggingFileName");
				isCheckTimeout = vQueue.getAttribute("isCheckTimeout");
				elementTimeoutTime_ms = vQueue.getAttribute("elementTimeoutTime_ms");
				checkElementTimeoutPeriod_ms = vQueue.getAttribute("checkElementTimeoutPeriod_ms");
				consumersCount = vQueue.getAttribute("consumersCount");

				VQueueFactory.registry(instanceName, implementClassName, Boolean.parseBoolean(isLogging), loggingFileName, Boolean.parseBoolean(isCheckTimeout), Long.parseLong(elementTimeoutTime_ms),
				        Long.parseLong(checkElementTimeoutPeriod_ms), Integer.parseInt(consumersCount));

				mbs.registerMBean(VQueueFactory.getInstance(instanceName), new ObjectName(objectName));
			}
		}
	}

	/**
	 * initializing vtopics by outer configuration .
	 * 
	 * @param doc <b>(Document)</b> configuration document pass in
	 * 
	 * @throws Exception when initializing vtopics fail
	 */
	private void initVTopics(Document doc) throws Exception {
		String instanceName = null;
		String implementClassName = null;
		String objectName = null;
		String isLogging = null;
		String loggingFileName = null;
		String isCheckTimeout = null;
		String elementTimeoutTime_ms = null;
		String checkElementTimeoutPeriod_ms = null;
		String consumersCount = null;

		NodeList vTopics = doc.getElementsByTagName("VTopics").item(0).getChildNodes();
		for (int i = 0; i < vTopics.getLength(); i++) {
			if (vTopics.item(i).getNodeType() == Node.ELEMENT_NODE) {
				Element vTopic = (Element) vTopics.item(i);

				instanceName = vTopic.getAttribute("instanceName");
				implementClassName = vTopic.getAttribute("implementClassName");
				objectName = vTopic.getAttribute("objectName");
				isLogging = vTopic.getAttribute("isLogging");
				loggingFileName = vTopic.getAttribute("loggingFileName");
				isCheckTimeout = vTopic.getAttribute("isCheckTimeout");
				elementTimeoutTime_ms = vTopic.getAttribute("elementTimeoutTime_ms");
				checkElementTimeoutPeriod_ms = vTopic.getAttribute("checkElementTimeoutPeriod_ms");
				consumersCount = vTopic.getAttribute("consumersCount");

				VTopicFactory.registry(instanceName, implementClassName, Boolean.parseBoolean(isLogging), loggingFileName, Boolean.parseBoolean(isCheckTimeout), Long.parseLong(elementTimeoutTime_ms),
				        Long.parseLong(checkElementTimeoutPeriod_ms), Integer.parseInt(consumersCount));

				mbs.registerMBean(VTopicFactory.getInstance(instanceName), new ObjectName(objectName));
			}
		}
	}

}
