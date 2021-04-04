
package org.yfr.vms.connection;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketAddress;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.yfr.vms.common.AbstractVElement;
import org.yfr.vms.vqueue.AbstractVQueue;
import org.yfr.vms.vtopic.AbstractVTopic;

/**
 * Using decorator pattern to design this wrapper .<br>
 * Just pass a AbstractVQueue in then it will get communication function .
 * 
 * <p>setup time <b>Aug 3, 2015 11:03:20 AM .</b></p>
 *
 * @author Vincent Huang
 */
public class VmsConnectionWrapper {

	private Log logger = LogFactory.getLog(VmsConnectionWrapper.class);

	private ServerSocket serverSocket = null;

	/**
	 * constructor of VQueueConnectionWrapper .
	 *
	 * @param ip <b>(String)</b> ip pass in
	 * @param port <b>(int)</b> port pass in
	 * @param abstractVQueue <b>(AbstractVQueue)</b> abstract vqueue pass in
	 * @param abstractVQueueName <b>(String)</b> abstract vqueue name pass in
	 */
	public VmsConnectionWrapper(String ip, int port, AbstractVQueue<AbstractVElement> abstractVQueue, String abstractVQueueName) {
		SocketAddress address = new InetSocketAddress(ip, port);
		try {
			serverSocket = new ServerSocket();
			serverSocket.bind(address);
			logger.info("init server socket : " + address.toString());
		} catch (Exception ex) {
			ex.printStackTrace();
		}

		logger.info("wait client connecting ... ");
		while (true) {
			try {
				Socket clientSocket = serverSocket.accept();
				clientSocket.setTcpNoDelay(true);
				logger.info(clientSocket.getInetAddress().toString() + " connect !");

				/* start ConnectionHandler thread to handle connection . */
				new Thread(new ConnectionHandler(clientSocket, abstractVQueue, abstractVQueueName), clientSocket.getInetAddress().toString()).start();
			} catch (Exception ex) {
				logger.error(ex.getMessage(), ex);
			}
		}
	}

	/**
	 * connection handler, handling each connection's sending and receiving behavior .<br>
	 * 
	 * <p>setup time <b>Aug 3, 2015 11:28:26 AM .</b></p>
	 *
	 * @author Vincent Huang
	 */
	private class ConnectionHandler implements Runnable {

		private Log logger = LogFactory.getLog(getClass());

		private Socket clientSocket = null;

		private AbstractVQueue<AbstractVElement> abstractVQueue = null;

		private String abstractVQueueName = null;

		/**
		 * constructor of VmsConnectionHandler .
		 *
		 * @param clientSocket <b>(Socket)</b> client socket to set
		 * @param abstractVQueue <b>(AbstractVQueue)</b> abstract vqueue to set
		 * @param abstractVQueueName <b>(String)</b> abstract vqueue name to set
		 */
		public ConnectionHandler(Socket clientSocket, AbstractVQueue<AbstractVElement> abstractVQueue, String abstractVQueueName) {
			this.clientSocket = clientSocket;
			this.abstractVQueue = abstractVQueue;
			this.abstractVQueueName = abstractVQueueName;
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
							AbstractVElement element = abstractVQueue.transfromToElement(receiveMessage.substring(2));
							abstractVQueue.put(element);
							out.println("put [" + receiveMessage + "] to " + abstractVQueueName + " success");
						} else if (receiveMessage.substring(0, 2).toUpperCase().equals("PT")) {
							AbstractVElement element = abstractVQueue.transfromToElement(receiveMessage.substring(2));
							abstractVQueue.put(element);
							out.println("put [" + receiveMessage + "] to " + abstractVQueueName + " success");
						} else if (receiveMessage.substring(0, 2).toUpperCase().equals("TQ")) {
							AbstractVElement element = abstractVQueue.take();
							out.println(abstractVQueue.transformToString(element));
						} else if (receiveMessage.substring(0, 2).toUpperCase().equals("RT")) {
							AbstractVTopic<AbstractVElement> vTopic = (AbstractVTopic<AbstractVElement>) abstractVQueue;
							vTopic.registerRemoteSubscriber(out, clientSocket.getInetAddress().toString());
							out.println(clientSocket.getInetAddress().toString() + " register [" + abstractVQueueName + "] success");
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

}
