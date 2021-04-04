
package org.yfr.vms.vtopic;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.yfr.vms.common.AbstractVElement;

public class TopicSubscriber extends Subscriber {

	private Log logger = LogFactory.getLog(TopicSubscriber.class);

	public TopicSubscriber(String subscriberName) {
		super(subscriberName);
	}

	@Override
	public void afterReceive(AbstractVElement element) {
		logger.info(subscriberName + " get : " + element.toString());
	}

}
