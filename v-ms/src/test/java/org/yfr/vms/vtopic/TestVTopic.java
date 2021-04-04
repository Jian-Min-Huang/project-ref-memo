
package org.yfr.vms.vtopic;

import java.lang.management.ManagementFactory;

import javax.management.MBeanServer;
import javax.management.ObjectName;

import org.junit.BeforeClass;
import org.junit.Test;
import org.yfr.vms.common.AbstractVElement;
import org.yfr.vms.common.VElement;

public class TestVTopic {

	private static MBeanServer mbs = ManagementFactory.getPlatformMBeanServer();

	private static VTopic vTopic = new VTopic("vTopic", false, "vTopic", true, 2000, 1000, 2);

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		mbs.registerMBean(vTopic, new ObjectName("org.yfr.queue:type=vTopic"));
	}

	/* 
	 * demo notify subscribers .
	 */
	@Test
	public void demo1() throws Exception {
		System.out.println("\ndemo1");

		vTopic.registerSubscriber(new TopicSubscriber("subscriber1"));
		vTopic.registerSubscriber(new TopicSubscriber("subscriber2"));

		for (int i = 0; i < 5; i++) {
			if (i == 2) {
				vTopic.put(new VElement("message " + i, AbstractVElement.CutInLineTag.TAG));
			} else {
				vTopic.put(new VElement("message " + i, 100));
			}

			Thread.sleep(1);
		}

		Thread.sleep(1000);
	}

}
