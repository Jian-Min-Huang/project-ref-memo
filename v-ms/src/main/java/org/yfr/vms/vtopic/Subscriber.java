
package org.yfr.vms.vtopic;

import org.yfr.vms.common.AbstractVElement;

/**
 * All class who want to be notify must extends this abstract class .<br>
 * 
 * <p>setup time <b>Jul 29, 2015 9:23:54 AM .</b></p>
 *
 * @author Vincent Huang
 */
public abstract class Subscriber {

	/** subscriber name . */
	protected String subscriberName = null;

	/**
	 * constructor of Subscriber .
	 *
	 * @param subscriberName <b>(String)</b> subscriber name to set
	 */
	public Subscriber(String subscriberName) {
		this.subscriberName = subscriberName;
	}

	/**
	 * getter of subscriberName .
	 *
	 * @return subscriberName
	 */
	public String getSubscriberName() {
		return subscriberName;
	}

	/**
	 * callback method when subscriber receives element .
	 * 
	 * @param element <b>(AbstractVElement)</b> element pass in
	 */
	public abstract void afterReceive(AbstractVElement element);

}
