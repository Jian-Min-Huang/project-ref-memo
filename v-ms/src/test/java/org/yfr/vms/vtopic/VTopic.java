
package org.yfr.vms.vtopic;

import org.yfr.vms.common.AbstractVElement;
import org.yfr.vms.common.VElement;
import org.yfr.vms.vtopic.AbstractVTopic;

public class VTopic extends AbstractVTopic<VElement> {

	public VTopic() {
		super();
	}
	
	public VTopic(String queueName, boolean isLogging, String loggingFileName, boolean isCheckTimeout, long elementTimeoutTime_ms, long checkElementTimeoutPeriod_ms, int consumersCount) {
		super(queueName, isLogging, loggingFileName, isCheckTimeout, elementTimeoutTime_ms, checkElementTimeoutPeriod_ms, consumersCount);
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
