
package org.yfr.vms.manager;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.yfr.vms.common.AbstractVElement;
import org.yfr.vms.vqueue.AbstractVQueueMBean;
import org.yfr.vms.vtopic.AbstractVTopic;
import org.yfr.vms.vtopic.TopicSubscriber;
import org.yfr.vms.vtopic.VTopicFactory;

public class TestVmsManager {

	private Log logger = LogFactory.getLog(TestVmsManager.class);

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		VmsManager.INSTANCE.init(true, "test-vms-conf.xml");

		AbstractVTopic<AbstractVElement> vTopic = VTopicFactory.getInstance("vTopic");
		vTopic.registerSubscriber(new TopicSubscriber("subscriber1"));
		vTopic.registerSubscriber(new TopicSubscriber("subscriber2"));
	}

	@Test
	public void demo1() throws Exception {
		String hostName = "127.0.0.1";
		int portNumber = 13111;

		Socket socket = new Socket();
		socket.connect(new InetSocketAddress(hostName, portNumber), 5000);

		PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
		BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

		List<String> list = new ArrayList<String>();
		list.add("PQvQueue&&NDL@@Vincent");
		list.add("PQvQueue&&DLO@@VincentDelay1000@@1000");
		list.add("PQvQueue&&CIL@@VincentCutInLine");
		list.add("PQvTakingQueue&&NDL@@Vincent");
		list.add("PQvTakingQueue&&DLO@@VincentDelay1000@@1000");
		list.add("PQvTakingQueue&&CIL@@VincentCutInLine");
		list.add("PTvTopic&&NDL@@Vincent");
		list.add("PTvTopic&&DLO@@VincentDelay1000@@1000");
		list.add("PTvTopic&&CIL@@VincentCutInLine");

		List<RemoteReceiver> remoteReceivers = new ArrayList<RemoteReceiver>();
		for (int i = 0; i < 3; i++) {
			RemoteReceiver remoteReceiver = new RemoteReceiver(hostName, portNumber);
			remoteReceivers.add(remoteReceiver);
			new Thread(remoteReceiver, "RemoteReceiver " + i).start();
		}

		for (String str : list) {
			out.println(str);
			logger.info("echo : " + in.readLine());

			Thread.sleep(100);
		}

		out.println("TQvTakingQueue&&");
		String line = in.readLine();
		Assert.assertEquals(true, line.contains("content=VincentCutInLine"));
		Thread.sleep(100);
		out.println("TQvTakingQueue&&");
		line = in.readLine();
		Assert.assertEquals(true, line.contains("content=Vincent"));
		Thread.sleep(100);
		out.println("TQvTakingQueue&&");
		line = in.readLine();
		Assert.assertEquals(true, line.contains("content=VincentDelay1000"));
		Thread.sleep(100);

		JmxClient jmxClient = new JmxClient();
		AbstractVQueueMBean mbeanProxy = jmxClient.getMbeanProxy();
		Assert.assertEquals(2, mbeanProxy.getConsumersCount());
		Assert.assertEquals(0, mbeanProxy.getVQueueNowSize());
		Assert.assertEquals(3, mbeanProxy.getCumulativeSize());

		Thread.sleep(2000);

		socket.close();
	}

	private class RemoteReceiver implements Runnable {

		private Log logger = LogFactory.getLog(getClass());

		private boolean isTerminate = false;

		private Socket socket = null;

		private PrintWriter out = null;
		private BufferedReader in = null;

		public RemoteReceiver(String hostName, int portNumber) {
			try {
				socket = new Socket();
				socket.connect(new InetSocketAddress(hostName, portNumber), 5000);

				out = new PrintWriter(socket.getOutputStream(), true);
				in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

				out.println("RTvTopic&&");
				logger.info("echo : " + in.readLine());
			} catch (Exception ex) {
				logger.error(ex.getMessage(), ex);
			}
		}

		@SuppressWarnings("unused")
		public void terminateReceiver() {
			this.isTerminate = true;
		}

		@Override
		public void run() {
			try {
				String receiveMessage;
				while (!isTerminate && (receiveMessage = in.readLine()) != null) {
					logger.info(receiveMessage);
				}
			} catch (Exception ex) {
				logger.error(ex.getMessage(), ex);
			}
		}

	}

}