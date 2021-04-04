
package org.yfr.vms.vqueue;

import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.yfr.vms.common.AbstractVElement;
import org.yfr.vms.common.VElement;
import org.yfr.vms.vqueue.AbstractVQueue;

public class VQueue extends AbstractVQueue<VElement> {

	private Log logger = LogFactory.getLog(VQueue.class);

	/* store consume element and timeout element . */
	private List<VElement> consumeList = null, timeoutList = null;

	public VQueue() {
		super();
	}

	public VQueue(String queueName, boolean isLogging, String loggingFileName, boolean isCheckTimeout, long elementTimeoutTime_ms, long checkElementTimeoutPeriod_ms, int consumersCount) {
		super(queueName, isLogging, loggingFileName, isCheckTimeout, elementTimeoutTime_ms, checkElementTimeoutPeriod_ms, consumersCount);
	}

	public List<VElement> getConsumeList() {
		return consumeList;
	}

	public List<VElement> getTimeoutList() {
		return timeoutList;
	}

	public void setConsumeList(List<VElement> consumeList) {
		this.consumeList = consumeList;
	}

	public void setTimeoutList(List<VElement> timeoutList) {
		this.timeoutList = timeoutList;
	}

	@Override
	protected boolean enQueueFilter(VElement element) {
		if (element.getContent().contains("5")) {
			return false;
		} else {
			return true;
		}
	}

	@Override
	protected void timeoutHook(VElement element) {
		logger.info("[Element Timeout][" + System.currentTimeMillis() + "] " + element.toString());

		timeoutList.add(element);
	}

	@Override
	protected void consume(VElement element) {
		logger.info(Thread.currentThread().getName() + " consume " + element.toString());

		consumeList.add(element);
	}

	@Override
	public VElement transfromToElement(String receiveMessage) {
		String[] splitMsg = receiveMessage.split("@@");

		if (splitMsg[0].equals("NDL")) {
			return new VElement(splitMsg[1]);
		} else if (splitMsg[0].equals("DLO")) {
			return new VElement(splitMsg[1], Long.parseLong(splitMsg[2]));
		} else if (splitMsg[0].equals("CIL")) {
			return new VElement(splitMsg[1], AbstractVElement.CutInLineTag.TAG);
		} else {
			return null;
		}
	}

	@Override
	public String transformToString(VElement element) {
		return element.toString();
	}

}
