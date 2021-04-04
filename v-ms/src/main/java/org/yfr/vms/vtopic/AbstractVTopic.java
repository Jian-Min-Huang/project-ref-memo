
package org.yfr.vms.vtopic;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.yfr.vms.common.AbstractVElement;
import org.yfr.vms.vqueue.AbstractVQueue;

/**
 * AbstractVTopic extends AbstractVQueue and using observer pattern to implement topic function .<br>

 * <p>setup time <b>Jul 29, 2015 9:23:39 AM .</b></p>
 *
 * @author Vincent Huang
 */
public abstract class AbstractVTopic<E extends AbstractVElement> extends AbstractVQueue<E> implements AbstractVTopicMBean {

	private Log logger = LogFactory.getLog(AbstractVTopic.class);

	private List<Subscriber> subscribers = new ArrayList<Subscriber>();

	private List<PrintWriter> remoteSubscribers = new ArrayList<PrintWriter>();

	/**
	 * default constructor of AbstractVTopic .
	 */
	public AbstractVTopic() {
		super();
	}

	/**
	 * constructor of AbstractVTopic .
	 *
	 * @param queueName <b>(String)</b> queue name pass in
	 * @param isLogging <b>(boolean)</b> is content logging pass in
	 * @param loggingFileName <b>(String)</b> logging file name pass in
	 * @param isCheckTimeout <b>(boolean)</b> is check timeout pass in
	 * @param elementTimeoutTime_ms <b>(long)</b> element time out time (ms) pass in
	 * @param checkElementTimeoutPeriod_ms <b>(long)</b> check element time out period (ms) pass in
	 * @param consumersCount <b>(int)</b> consumers count pass in
	 */
	public AbstractVTopic(String queueName, boolean isLogging, String loggingFileName, boolean isCheckTimeout, long elementTimeoutTime_ms, long checkElementTimeoutPeriod_ms, int consumersCount) {
		super(queueName, isLogging, loggingFileName, isCheckTimeout, elementTimeoutTime_ms, checkElementTimeoutPeriod_ms, consumersCount);
	}

	/**
	 * register subscriber .
	 * 
	 * @param subscriber <b>(Subscriber)</b> subscriber pass in
	 */
	public void registerSubscriber(Subscriber subscriber) {
		subscribers.add(subscriber);

		logger.info("add subscriber : " + subscriber.getSubscriberName());
	}

	/**
	 * register remote subscriber .
	 * 
	 * @param writer <b>(PrintWriter)</b> writer pass in
	 * @param name <b>(String)</b> name pass in
	 */
	public void registerRemoteSubscriber(PrintWriter writer, String name) {
		remoteSubscribers.add(writer);

		logger.info("add remote subscriber : " + name);
	}

	/**
	 * notify all local and remote subscribers .
	 * 
	 * @param element <b>(E extends AbstractVElement)</b> element pass in
	 */
	private void notifySubscribers(E element) {
		for (Subscriber subscriber : subscribers) {
			subscriber.afterReceive(element);
		}

		for (PrintWriter writer : remoteSubscribers) {
			try {
				writer.println(transformToString(element));
			} catch (Exception ex) {
				logger.error(ex.getMessage(), ex);
			}
		}
	}

	@Override
	protected void consume(E element) {
		notifySubscribers(element);
	}

}
