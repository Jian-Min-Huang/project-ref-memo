
package org.yfr.vms.vqueue;

/**
 * Abstract V Queue MBean .
 * 
 * <p>setup time <b>Jul 6, 2015 8:49:34 AM .</b></p>
 *
 * @author Vincent Huang
 */
public interface AbstractVQueueMBean {

	/**
	 * get V Queue consumers count .
	 * 
	 * @return count of consumers
	 */
	int getConsumersCount();

	/**
	 * add a V Queue consumer .
	 * 
	 * @return consumers name
	 */
	String addConsumer();

	/**
	 * remove a V Queue consumer .
	 * 
	 * @return consumer's name
	 */
	String removeConsumer();

	/** suspend consumers . */
	void suspendConsumers();

	/** resume consumers . */
	void resumeConsumers();

	/** suspend timeout checker . */
	void suspendTimeoutChecker();

	/** resume timeout checker . */
	void resumeTimeoutChecker();

	/**
	 * enable queue logging .
	 * 
	 * @return is logging or not
	 */
	boolean enableLogging();

	/**
	 * disable queue logging .
	 * 
	 * @return is logging or not
	 */
	boolean disableLogging();

	/**
	 * show V Queue size now .
	 * 
	 * @return size of queue now
	 */
	int getVQueueNowSize();

	/**
	 * show cumulative size of queue .
	 * 
	 * @return cumulative size of queue
	 */
	int getCumulativeSize();

	/**
	 * show element .
	 * 
	 * @param index <b>(int)</b> element index pass in
	 * 
	 * @return element toString()
	 */
	String showElement(int index);

}
