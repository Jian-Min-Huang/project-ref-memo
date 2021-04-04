
package org.yfr.vms.manager;

import java.util.HashMap;

import javax.management.JMX;
import javax.management.MBeanServerConnection;
import javax.management.ObjectName;
import javax.management.remote.JMXConnector;
import javax.management.remote.JMXConnectorFactory;
import javax.management.remote.JMXServiceURL;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.yfr.vms.vqueue.AbstractVQueueMBean;

public class JmxClient {

	private Log logger = LogFactory.getLog(JmxClient.class);

	private AbstractVQueueMBean mbeanProxy = null;

	public JmxClient() throws Exception {
		try {
			JMXServiceURL url = new JMXServiceURL("service:jmx:rmi:///jndi/rmi://127.0.0.1:9999/jmxrmi");
			HashMap<String, Object> env = new HashMap<String, Object>();
			String[] creds = {"JmxAdmin", "admin"};
			env.put(JMXConnector.CREDENTIALS, creds);
			JMXConnector jmxc = JMXConnectorFactory.connect(url, env);
			MBeanServerConnection mbsc = jmxc.getMBeanServerConnection();
			ObjectName mbeanName = new ObjectName("org.yfr.vms.vqueue:type=vQueue");

			mbeanProxy = JMX.newMBeanProxy(mbsc, mbeanName, AbstractVQueueMBean.class, false);
		} catch (Exception ex) {
			logger.error(ex.getMessage(), ex);
		}
	}

	public AbstractVQueueMBean getMbeanProxy() {
		return mbeanProxy;
	}

}
