
package org.yfr.vms.vtopic;

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
 * <p>setup time <b>Aug 3, 2015 1:50:25 PM .</b></p>
 *
 * @author Vincent Huang
 */
public class VTopicFactory {

	private static Log logger = LogFactory.getLog(VTopicFactory.class);

	private static Map<String, AbstractVTopic<AbstractVElement>> registryVTopics = new HashMap<String, AbstractVTopic<AbstractVElement>>();

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
	public static void registry(String instanceName, String implementClassName, boolean isLogging, String loggingFileName, boolean isCheckTimeout, long elementTimeoutTime_ms, long checkElementTimeoutPeriod_ms,
	        int consumersCount) {
		try {
			Constructor<?> constructor = Class.forName(implementClassName).getConstructor(String.class, boolean.class, String.class, boolean.class, long.class, long.class, int.class);
			AbstractVTopic<AbstractVElement> vTopicInstance = (AbstractVTopic<AbstractVElement>) constructor.newInstance(instanceName, isLogging, loggingFileName, isCheckTimeout, elementTimeoutTime_ms,
			        checkElementTimeoutPeriod_ms, consumersCount);
			registryVTopics.put(instanceName, vTopicInstance);
			
			logger.info("registry " + instanceName + " success !");
		} catch (Exception ex) {
			logger.error(ex.getMessage(), ex);
		}
	}

	/**
	 * get instance from registry map by name .
	 * 
	 * @param instanceName - the instance name <b>(String)</b> pass in
	 * 
	 * @return instance
	 */
	public static AbstractVTopic<AbstractVElement> getInstance(String instanceName) {
		AbstractVTopic<AbstractVElement> vTopic = (AbstractVTopic<AbstractVElement>) registryVTopics.get(instanceName);

		return vTopic;
	}

}
