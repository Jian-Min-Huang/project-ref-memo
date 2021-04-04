
package org.yfr.vms.vqueue;

import java.lang.management.ManagementFactory;
import java.util.ArrayList;
import java.util.List;

import javax.management.MBeanServer;
import javax.management.ObjectName;

import org.apache.log4j.Logger;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.yfr.vms.common.AbstractVElement;
import org.yfr.vms.common.VElement;

public class TestVQueue {

	private Logger logger = Logger.getLogger(TestVQueue.class);

	private static MBeanServer mbs = ManagementFactory.getPlatformMBeanServer();

	private static VQueue vQueue = new VQueue("vQueue", false, "vQueue", true, 2000, 1000, 2);
	private static VQueue vLoggingQueue = new VQueue("vLoggingQueue", true, "vLoggingQueue", true, 2000, 1000, 2);
	private static VQueue vNoConsumerQueue = new VQueue("vNoConsumerQueue", false, "vNoConsumerQueue", true, 200, 100, 0);
	private static VQueue vFilterQueue = new VQueue("vFilterQueue", false, "vFilterQueue", true, 2000, 1000, 2);

	private List<VElement> dataList = new ArrayList<VElement>();

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		mbs.registerMBean(vQueue, new ObjectName("org.yfr.queue:type=vQueue"));
		mbs.registerMBean(vLoggingQueue, new ObjectName("org.yfr.queue:type=vLoggingQueue"));
		mbs.registerMBean(vNoConsumerQueue, new ObjectName("org.yfr.queue:type=vNoConsumerQueue"));
		mbs.registerMBean(vFilterQueue, new ObjectName("org.yfr.queue:type=vFilterQueue"));
	}

	@Before
	public void setUpDataList() throws Exception {
		for (int i = 0; i < 5; i++) {
			// sleep for different delay offset .
			Thread.sleep(10);
			if (i == 2) {
				dataList.add(new VElement("message " + i, AbstractVElement.CutInLineTag.TAG));
			} else {
				dataList.add(new VElement("message " + i, 100));
			}
		}
	}

	/*
	 * the following three test will demo these common functions :
	 * 1. enqueue hook
	 * 2. cut in line
	 * 3. dequeue hook
	 * 4. multi-consumers
	 * 5. queue monitor
	 */

	/*
	 * demo1 will demo these extra functions :
	 * 
	 * 1. consuming order
	 * 2. queue replication
	 */
	@Test
	public void demo1() throws Exception {
		System.out.println("\ndemo1");

		List<VElement> resultList = new ArrayList<VElement>();
		vQueue.setConsumeList(resultList);

		for (int i = 0; i < 5; i++) {
			vQueue.put(dataList.get(i));
		}
		// sleep for queue consumer processing the element .
		Thread.sleep(200);

		// element 2 is cut in line
		Assert.assertEquals(dataList.get(2), vQueue.getConsumeList().get(0));
		Assert.assertEquals(dataList.get(0), vQueue.getConsumeList().get(1));
		Assert.assertEquals(dataList.get(1), vQueue.getConsumeList().get(2));
		Assert.assertEquals(dataList.get(3), vQueue.getConsumeList().get(3));
		Assert.assertEquals(dataList.get(4), vQueue.getConsumeList().get(4));

		Assert.assertEquals(dataList.get(0), vQueue.getReplicationList().get(0));
		Assert.assertEquals(dataList.get(1), vQueue.getReplicationList().get(1));
		Assert.assertEquals(dataList.get(2), vQueue.getReplicationList().get(2));
		Assert.assertEquals(dataList.get(3), vQueue.getReplicationList().get(3));
		Assert.assertEquals(dataList.get(4), vQueue.getReplicationList().get(4));
	}

	/* 
	 * demo2 will demo these extra functions :
	 * 
	 * 1. queue persisting
	 * 2. consumers suspending and resuming
	 */
	@Test
	public void demo2() throws Exception {
		System.out.println("\ndemo2");

		for (int i = 0; i < 5; i++) {
			Assert.assertEquals(true, vLoggingQueue.logging(dataList.get(i)));
		}

		List<VElement> resultList = new ArrayList<VElement>();
		vLoggingQueue.setConsumeList(resultList);

		vLoggingQueue.suspendConsumers();
		Thread.sleep(1000);
		logger.info("consumers suspend for 1s");
		for (int i = 0; i < 5; i++) {
			vLoggingQueue.put(dataList.get(i));
		}
		vLoggingQueue.resumeConsumers();

		Thread.sleep(100);
	}

	/* 
	 * demo3 will demo these extra functions :
	 * 
	 * 1. local process take out element
	 * 2. timeout hook
	 * 3. timeout checker suspending and resuming
	 */
	@Test
	public void demo3() throws Exception {
		System.out.println("\ndemo3");

		List<VElement> consumeList = new ArrayList<VElement>();
		vNoConsumerQueue.setConsumeList(consumeList);

		List<VElement> timeoutList = new ArrayList<VElement>();
		vNoConsumerQueue.setTimeoutList(timeoutList);

		vNoConsumerQueue.suspendTimeoutChecker();
		Thread.sleep(100);

		for (int i = 0; i < 5; i++) {
			vNoConsumerQueue.put(dataList.get(i));
		}
		Assert.assertEquals(dataList.get(2), vNoConsumerQueue.take());

		vNoConsumerQueue.resumeTimeoutChecker();
		Thread.sleep(2000);

		Assert.assertEquals(0, vNoConsumerQueue.getConsumeList().size());
		Assert.assertEquals(4, vNoConsumerQueue.getTimeoutList().size());

		Assert.assertEquals(dataList.get(0), vNoConsumerQueue.getTimeoutList().get(0));
		Assert.assertEquals(dataList.get(1), vNoConsumerQueue.getTimeoutList().get(1));
		Assert.assertEquals(dataList.get(3), vNoConsumerQueue.getTimeoutList().get(2));
		Assert.assertEquals(dataList.get(4), vNoConsumerQueue.getTimeoutList().get(3));
	}

	/* 
	 * demo4 will demo these extra functions :
	 * 
	 * 1. content filter
	 * 2. replication list
	 */
	@Test
	public void demo4() throws Exception {
		System.out.println("\ndemo4");

		List<VElement> consumeList = new ArrayList<VElement>();
		vFilterQueue.setConsumeList(consumeList);

		List<VElement> timeoutList = new ArrayList<VElement>();
		vFilterQueue.setTimeoutList(timeoutList);

		dataList.add(new VElement("message 5", 100));

		for (int i = 0; i < 6; i++) {
			vFilterQueue.put(dataList.get(i));
			Thread.sleep(1);
		}
		Thread.sleep(1000);

		Assert.assertEquals(5, vFilterQueue.getConsumeList().size());
		Assert.assertEquals(1, vFilterQueue.getFilterOutElementlist().size());
		Assert.assertEquals(dataList.get(5), vFilterQueue.getFilterOutElementlist().get(0));
		Assert.assertEquals(dataList.get(2), vFilterQueue.getConsumeList().get(0));
		Assert.assertEquals(dataList.get(0), vFilterQueue.getConsumeList().get(1));
		Assert.assertEquals(dataList.get(1), vFilterQueue.getConsumeList().get(2));
		Assert.assertEquals(dataList.get(3), vFilterQueue.getConsumeList().get(3));
		Assert.assertEquals(dataList.get(4), vFilterQueue.getConsumeList().get(4));

		Thread.sleep(1000);
	}

}
