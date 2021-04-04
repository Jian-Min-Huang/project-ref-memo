
package org.yfr.vms.connection;

import java.net.ServerSocket;
import java.net.Socket;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * vms connection manager, accept each connection and pass to vms connection handler .<br>
 * 
 * <p>setup time <b>Aug 3, 2015 11:19:11 AM .</b></p>
 *
 * @author Vincent Huang
 */
public class VmsConnectionManager implements Runnable {

	private Log logger = LogFactory.getLog(VmsConnectionManager.class);

	private ServerSocket serverSocket = null;

	/**
	 * constructor of VmsConnectionManager .
	 *
	 * @param serverSocket <b>(ServerSocket)</b> server socket to set
	 */
	public VmsConnectionManager(ServerSocket serverSocket) {
		this.serverSocket = serverSocket;
	}

	@Override
	public void run() {
		logger.info("wait client connecting ... ");
		while (true) {
			try {
				Socket clientSocket = serverSocket.accept();
				clientSocket.setTcpNoDelay(true);
				logger.info(clientSocket.getInetAddress().toString() + " connect !");

				/* start VmsConnectionHandler thread to handle connection . */
				new Thread(new VmsConnectionHandler(clientSocket), "VmsConnectionHandler " + clientSocket.getInetAddress().toString()).start();
			} catch (Exception ex) {
				logger.error(ex.getMessage(), ex);
			}
		}
	}

}
