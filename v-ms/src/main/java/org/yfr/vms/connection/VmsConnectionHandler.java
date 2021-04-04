
package org.yfr.vms.connection;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.yfr.vms.common.AbstractVElement;
import org.yfr.vms.vqueue.AbstractVQueue;
import org.yfr.vms.vqueue.VQueueFactory;
import org.yfr.vms.vtopic.AbstractVTopic;
import org.yfr.vms.vtopic.VTopicFactory;

/**
 * vms connection handler, handling each connection's sending and receiving behavior .<br>
 * 
 * <p>setup time <b>Aug 3, 2015 11:28:26 AM .</b></p>
 *
 * @author Vincent Huang
 */
public class VmsConnectionHandler implements Runnable {

	private Log logger = LogFactory.getLog(VmsConnectionHandler.class);

	private Socket clientSocket = null;

	/**
	 * constructor of VmsConnectionHandler .
	 *
	 * @param clientSocket <b>(Socket)</b> client socket to set
	 */
	public VmsConnectionHandler(Socket clientSocket) {
		this.clientSocket = clientSocket;
	}

	@Override
	public void run() {
		try {
			BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
			PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);

			String receiveMessage;
			while ((receiveMessage = in.readLine()) != null) {
				try {
					/*
					 * PQ = put queue .
					 * PT = put topic .
					 * TQ = take queue .
					 * RT = register topic .
					 */
					if (receiveMessage.substring(0, 2).toUpperCase().equals("PQ")) {
						String[] splitMsg = receiveMessage.substring(2).split("&&");
						AbstractVQueue<AbstractVElement> queueInstance = VQueueFactory.getInstance(splitMsg[0]);
						AbstractVElement element = queueInstance.transfromToElement(splitMsg[1]);
						queueInstance.put(element);
						out.println("put [" + receiveMessage + "] to " + splitMsg[0] + " success");
					} else if (receiveMessage.substring(0, 2).toUpperCase().equals("PT")) {
						String[] splitMsg = receiveMessage.substring(2).split("&&");
						AbstractVTopic<AbstractVElement> topicInstance = VTopicFactory.getInstance(splitMsg[0]);
						AbstractVElement element = topicInstance.transfromToElement(splitMsg[1]);
						topicInstance.put(element);
						out.println("put [" + receiveMessage + "] to " + splitMsg[0] + " success");
					} else if (receiveMessage.substring(0, 2).toUpperCase().equals("TQ")) {
						String[] splitMsg = receiveMessage.substring(2).split("&&");
						AbstractVQueue<AbstractVElement> queueInstance = VQueueFactory.getInstance(splitMsg[0]);
						AbstractVElement element = queueInstance.take();
						out.println(queueInstance.transformToString(element));
					} else if (receiveMessage.substring(0, 2).toUpperCase().equals("RT")) {
						String[] splitMsg = receiveMessage.substring(2).split("&&");
						AbstractVTopic<AbstractVElement> topicInstance = VTopicFactory.getInstance(splitMsg[0]);
						topicInstance.registerRemoteSubscriber(out, clientSocket.getInetAddress().toString());
						out.println(clientSocket.getInetAddress().toString() + " register [" + splitMsg[0] + "] success");
					} else {
						throw new Exception("undefined action");
					}
				} catch (Exception ex) {
					logger.error(ex.getMessage(), ex);

					out.println("processing fail");
				}
			}
		} catch (Exception ex) {
			logger.error(ex.getMessage(), ex);
		}
	}

}
