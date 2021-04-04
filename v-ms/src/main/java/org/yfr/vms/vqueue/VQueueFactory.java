
package org.yfr.vms.vqueue;

import java.lang.reflect.Constructor;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.yfr.vms.common.AbstractVElement;

/**
 * Using registry of singleton pattern to implement this class .<br>
 * All instance will registry by outside configuration and other class can get instance by name .
 * 
 * <p>setup time <b>Aug 3, 2015 1:53:14 PM .</b></p>
 *
 * @author Vincent Huang
 */
public class VQueueFactory {

	private static Log logger = LogFactory.getLog(VQueueFactory.class);

	private static Map<String, AbstractVQueue<AbstractVElement>> registryVQueues = new HashMap<String, AbstractVQueue<AbstractVElement>>();

	/**
	 * use reflection to dynamic create a instance and put into registry map . 
	 * 
	 * @param instanceName <b>(String)</b> instance name pass in
	 * @param implementClassName <b>(String)</b> implement class name pass in
	 * @param isLogging <b>(boolean)</b> is logging content or not pass in
	 * @param loggingFileName <b>(String)</b> logging file name pass in
	 * @param isCheckTimeout <b>(boolean)</b> is check timeout or not pass in
	 * @param elementTimeoutTime_ms <b>(long)</b> element timeout time(ms) pass in
	 * @param checkElementTimeoutPeriod_ms <b>(long)</b> check element timeout period(ms) pass in
	 * @param consumersCount <b>(int)</b> consumer count pass in
	 */
	@SuppressWarnings("unchecked")
	public static void registry(String instanceName, String implementClassName, boolean isLogging, String loggingFileName, boolean isCheckTimeout, long elementTimeoutTime_ms,
	        long checkElementTimeoutPeriod_ms, int consumersCount) {
		try {
			Constructor<?> constructor = Class.forName(implementClassName).getConstructor(String.class, boolean.class, String.class, boolean.class, long.class, long.class, int.class);
			AbstractVQueue<AbstractVElement> vQueueInstance = (AbstractVQueue<AbstractVElement>) constructor.newInstance(instanceName, isLogging, loggingFileName, isCheckTimeout, elementTimeoutTime_ms,
			        checkElementTimeoutPeriod_ms, consumersCount);
			registryVQueues.put(instanceName, vQueueInstance);

			logger.info("registry " + instanceName + " success !");
		} catch (Exception ex) {
			logger.error(ex.getMessage(), ex);
		}
	}

	/**
	 * get instance from registry map by name .
	 * 
	 * @param instanceName <b>(String)</b> instance name pass in
	 * 
	 * @return instance
	 */
	public static AbstractVQueue<AbstractVElement> getInstance(String instanceName) {
		AbstractVQueue<AbstractVElement> vQueue = (AbstractVQueue<AbstractVElement>) registryVQueues.get(instanceName);

		return vQueue;
	}

}
