
package org.yfr.vms.vqueue;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.DelayQueue;
import java.util.concurrent.atomic.AtomicInteger;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.yfr.vms.common.AbstractVElement;

/**
 * Using template pattern to design this custom queue .<br>
 * This class contains a DelayQueue, list of runnable (queue consumers) and some methods (include abstract method) to define the flow .<br>
 * In the MBean interface, we defines some methods for jmx using .<br>
 * In short, this queue implement following functions .<br>
 * <ul>
 * 		<li>enqueue hook</li>
 * 		<li>enqueue filter</li>
 * 		<li>cut in line</li>
 * 		<li>content logging</li>
 * 		<li>dequeue hook</li>
 * 		<li>timeout hook</li>
 * 		<li>timeout checker suspending and resuming</li>
 * 		<li>multi-consumers</li>
 * 		<li>local and cross process take out element</li>
 * 		<li>consumers suspending and resuming</li>
 * 		<li>cross process communication (extract to vms manager)</li>
 * 		<li>replication</li>
 * 		<li>monitoring</li>
 * </ul>
 * 
 * <p>setup time <b>Jul 5, 2015 9:42:21 PM .</b></p>
 *
 * @author Vincent Huang
 */
public abstract class AbstractVQueue<E extends AbstractVElement> implements AbstractVQueueMBean {

	/* ******************************************************************
	 *                                                                  *
	 * object fields .                                                  *
	 * logger declares first, rest fields will declare by using order . *
	 *                                                                  *
	 ****************************************************************** */
	private Log logger = LogFactory.getLog(AbstractVQueue.class);

	/* base queue in AbstractVQueue, remember delay queue = priority queue + blocking queue + delayed . */
	private DelayQueue<E> queue = new DelayQueue<E>();

	private String queueName = null;

	private AtomicInteger nowElementIndex = new AtomicInteger(0);

	private List<E> filterOutList = new ArrayList<E>();

	private List<E> replicationList = new ArrayList<E>();

	private boolean isLogging = false;

	private FileOutputStream loggingFos = null;

	private AbstractVConsumer timeoutChecker = null;

	private LinkedList<AbstractVConsumer> consumers = new LinkedList<AbstractVConsumer>();

	/* ****************************
	 *                            *
	 * object constructors .      *
	 * order by argument counts . *
	 *                            *
	 **************************** */
	/**
	 * default constructor of AbstractVQueue .
	 */
	public AbstractVQueue() {}

	/**
	 * constructor of AbstractVQueue .
	 *
	 * @param queueName <b>(String)</b> queue name pass in
	 * @param isLogging <b>(boolean)</b> is content logging pass in
	 * @param loggingFileName <b>(String)</b> logging file name pass in
	 * @param isCheckTimeout <b>(boolean)</b> is check timeout pass in
	 * @param elementTimeoutTime_ms <b>(long)</b> element time out time (ms) pass in
	 * @param checkElementTimeoutPeriod_ms <b>(long)</b> check element time out period (ms) pass in
	 * @param consumersCount <b>(int)</b> consumers count pass in
	 */
	public AbstractVQueue(String queueName, boolean isLogging, String loggingFileName, boolean isCheckTimeout, long elementTimeoutTime_ms, long checkElementTimeoutPeriod_ms, int consumersCount) {
		init(queueName, isLogging, loggingFileName, isCheckTimeout, elementTimeoutTime_ms, checkElementTimeoutPeriod_ms, consumersCount);
	}

	/* ************************************************
	 *                                                *
	 * object methods .                               *
	 * declare by using order .                       *
	 * abstract method declares after simple method . *
	 *                                                *
	 ************************************************ */
	/**
	 * initialize the AbstractVQueue .
	 * 
	 * @param queueName <b>(String)</b> queue name to set
	 * @param isLogging <b>(boolean)</b> is content logging to set
	 * @param loggingFileName <b>(String)</b> logging file name pass in
	 * @param isCheckTimeout <b>(boolean)</b> is check timeout pass in
	 * @param elementTimeoutTime_ms <b>(long)</b> element time out time (ms) pass in
	 * @param checkElementTimeoutPeriod_ms <b>(long)</b> check element time out period (ms) pass in
	 * @param consumersCount <b>(int)</b> consumers count pass in
	 */
	public void init(String queueName, boolean isLogging, String loggingFileName, boolean isCheckTimeout, long elementTimeoutTime_ms, long checkElementTimeoutPeriod_ms, int consumersCount) {
		this.queueName = queueName;
		this.isLogging = isLogging;

		/* create directory if not exist . */
		File loggingDirectory = new File("v-queue-data");
		if (!loggingDirectory.exists()) {
			loggingDirectory.mkdir();
		}

		/* logging queue element to file . */
		try {
			loggingFos = new FileOutputStream(new File("./v-queue-data/" + loggingFileName), true);
		} catch (Exception ex) {
			logger.error(ex.getMessage(), ex);

			try {
				loggingFos.close();
			} catch (IOException e) {
				logger.error(e.getMessage(), e);
			}
		}

		/* initialize a timeout checker thread, it will take element out of V Queue, fire deQueueHook and timeoutHook when element is timeout . */
		timeoutChecker = new AbstractVConsumer() {

			@Override
			public void run() {
				while (!this.isTerminated()) {
					if (!this.isSuspended()) {
						/* peek the element and check timeout or not . */
						if (queue.peek() != null) {
							/* due time equals -1L means cut in line, will skip timeout check . */
							if (queue.peek().getDueTime() != -1L) {
								if (System.currentTimeMillis() - queue.peek().getDueTime() > elementTimeoutTime_ms) {
									E element;
									try {
										/* take element out, fire hooks . */
										element = queue.take();
										deQueueHook(element);
										timeoutHook(element);
									} catch (InterruptedException ex) {
										logger.error(ex.getMessage(), ex);
									}
								}
							}
						}

						/* sleep for check timeout period . */
						try {
							Thread.sleep(checkElementTimeoutPeriod_ms);
						} catch (InterruptedException ex) {
							logger.error(ex.getMessage(), ex);
						}
					} else {
						/* thread is suspending, sleep 1s and scheduling again . */
						try {
							Thread.sleep(1000);
						} catch (InterruptedException ex) {
							logger.error(ex.getMessage(), ex);
						}
					}
				}
			}

			@Override
			public String getName() {
				return queueName + " Timeout Checker";
			}
		};

		/* suspend the timeout checker thread if check timeout equals false . */
		if (!isCheckTimeout) {
			timeoutChecker.suspend();
		}

		/* start timeout checker thread . */
		new Thread(timeoutChecker, timeoutChecker.getName()).start();

		/* start consumers thread, it will take element out of V Queue, fire deQueueHook and consuming the element . */
		for (int i = 0; i < consumersCount; i++) {
			consumers.add(new QueueConsumer());
		}
		for (int i = 0; i < consumersCount; i++) {
			new Thread(consumers.get(i), consumers.get(i).getName() + "-" + i).start();
		}
	}

	/**
	 * put element into V Queue and filter it first .<br>
	 * if it pass then fire enQueueHook and put into queue (logging queue content if you needed) .<br> 
	 * if it is filtered out then put to list and log it .
	 * 
	 * @param element <b>(E extends AbstractVElement)</b> element pass in
	 */
	public void put(E element) {
		if (element == null) {
			logger.info("[Element Filter Out] element equals null");
		} else {
			if (enQueueFilter(element)) {
				element.setIndex(nowElementIndex.getAndAdd(1));
				enQueueHook(element);
				queue.put(element);
				replicationList.add(element);

				if (isLogging) {
					logging(element);
				}
			} else {
				filterOutList.add(element);

				logger.info("[Element Filter Out] " + element.toString());
			}
		}
	}

	/**
	 * enqueue filter, default return true .<br>
	 * you can override it in sub-class .
	 * 
	 * @param element <b>(E extends AbstractVElement)</b> element pass in
	 * 
	 * @return element can enqueue or not
	 */
	protected boolean enQueueFilter(E element) {
		return true;
	}

	/**
	 * the V Queue enqueue hook, provide default implement .<br>
	 * you can override it in sub-class .
	 * 
	 * @param element <b>(E extends AbstractVElement)</b> element pass in
	 */
	protected void enQueueHook(E element) {
		logger.info("[Element Enqueue][" + System.currentTimeMillis() + "] " + element.toString());
	}

	/**
	 * logging the V Queue content, provide default implement .<br>
	 * you can override it in sub-class .
	 * 
	 * @param element <b>(E extends AbstractVElement)</b> element pass in
	 * 
	 * @return logging success or not
	 */
	protected boolean logging(E element) {
		try {
			loggingFos.write(new Date().toString().getBytes());
			loggingFos.write("\t".getBytes());
			loggingFos.write(element.toString().getBytes());
			loggingFos.write("\n".getBytes());

			return true;
		} catch (Exception ex) {
			logger.error(ex.getMessage(), ex);

			try {
				loggingFos.close();
			} catch (IOException e) {
				logger.error(e.getMessage(), e);
			}

			return false;
		}
	}

	/**
	 * the V Queue timeout hook, provide default implement .<br>
	 * you can override it in sub-class .
	 * 
	 * @param element <b>(E extends AbstractVElement)</b> element pass in
	 */
	protected void timeoutHook(E element) {
		logger.info("[Element Timeout][" + System.currentTimeMillis() + "] " + element.toString());
	}

	/**
	 * the V Queue dequeue hook, provide default implement .<br>
	 * you can override it in sub-class .
	 * 
	 * @param element <b>(E extends AbstractVElement)</b> element pass in
	 */
	protected void deQueueHook(E element) {
		logger.info("[Element Dequeue][" + System.currentTimeMillis() + "] " + element.toString());
	}

	/**
	 * take element out of queue and fire deQueueHook .
	 * 
	 * @return element
	 */
	public E take() {
		E element = null;
		try {
			/* take element out, fire hooks . */
			element = queue.take();
			deQueueHook(element);
		} catch (InterruptedException ex) {
			logger.error(ex.getMessage(), ex);
		}

		return element;
	}

	/**
	 * consume the V Queue content, declare it abstract for sub-class implementing .
	 * 
	 * @param element <b>(E extends AbstractVElement)</b> element pass in
	 */
	protected abstract void consume(E element);

	/**
	 * transform socket receive message to E(extends AbstractVElement), declare it abstract for sub-class implementing .
	 * 
	 * @param receiveMessage <b>(String)</b> receive message pass in
	 * 
	 * @return E extends AbstractVElement
	 * 
	 * @throws Exception when transform error
	 */
	public abstract E transfromToElement(String receiveMessage) throws Exception;

	/**
	 * transform queue element to string for socket sending, declare it abstract for sub-class implementing .
	 * 
	 * @param element <b>(E extends AbstractVElement)</b> element pass in
	 * 
	 * @return element string format
	 * 
	 * @throws Exception when transform error
	 */
	public abstract String transformToString(E element) throws Exception;

	/* ************************************
	 *                                    *
	 * object field getters and setters . *
	 * order by field declaring order .   *
	 *                                    *
	 ************************************ */
	/**
	 * getter of filterOutList .
	 *
	 * @return filterOutList
	 */
	public List<E> getFilterOutElementlist() {
		return filterOutList;
	}

	/**
	 * getter of replicationList .
	 *
	 * @return replicationList
	 */
	public List<E> getReplicationList() {
		return replicationList;
	}

	/* ***************************
	 *                           *
	 * object override methods . *
	 *                           *
	 *************************** */
	@Override
	public int getConsumersCount() {
		return consumers.size();
	}

	@Override
	public String addConsumer() {
		/* start a consumer thread, it will take element out of V Queue, fire deQueueHook and consuming the element . */
		AbstractVConsumer consumer = new QueueConsumer();
		new Thread(consumer, consumer.getName() + "-" + consumers.size()).start();

		/* consumers list will collect all consumer thread, so you should add it to consumers list . */
		consumers.add(consumer);

		return consumer.getName() + "-" + consumers.size();
	}

	@Override
	public String removeConsumer() {
		/* get consumer from consumers list, terminate it and remove from consumers list . */
		AbstractVConsumer consumer = consumers.get(consumers.size() - 1);
		consumer.terminate();
		consumers.remove(consumers.size() - 1);

		return consumer.getName();
	}

	@Override
	public void suspendConsumers() {
		for (AbstractVConsumer consumer : consumers) {
			consumer.suspend();
		}
		logger.info("AbstractVConsumer receives suspending command, the checker will finish rest job and suspend");
	}

	@Override
	public void resumeConsumers() {
		logger.info("AbstractVConsumer receives resuming command");
		for (AbstractVConsumer consumer : consumers) {
			consumer.resume();
		}
	}

	@Override
	public void suspendTimeoutChecker() {
		timeoutChecker.suspend();
		logger.info("Timeout Checker receives suspending command, the checker will finish rest job and suspend");
	}

	@Override
	public void resumeTimeoutChecker() {
		logger.info("Timeout Checker receives resuming command");
		timeoutChecker.resume();
	}

	@Override
	public boolean enableLogging() {
		isLogging = true;

		return isLogging;
	}

	@Override
	public boolean disableLogging() {
		isLogging = false;

		return isLogging;
	}

	@Override
	public int getVQueueNowSize() {
		return queue.size();
	}

	@Override
	public int getCumulativeSize() {
		return replicationList.size();
	}

	@Override
	public String showElement(int index) {
		return replicationList.get(index).toString();
	}

	/* **********************************************
	 *                                              *
	 * object private classes .                     *
	 * declare by using order .                     *
	 * abstract class declares after simple class . *
	 *                                              *
	 ********************************************** */
	/**
	 * The consumer of AbstractVQueue must extends this abstract class .<br>
	 * It will implement run method when AbstractVQueue add consumers .<br>
	 * This class has two flag (isSuspended and isTerminated) to let we suspend or terminate the thread .<br> 
	 * 
	 * <p>setup time <b>Jul 6, 2015 11:57:48 AM .</b></p>
	 *
	 * @author Vincent Huang
	 */
	private abstract class AbstractVConsumer implements Runnable {

		private boolean isSuspended = false;

		private boolean isTerminated = false;

		/**
		 * change suspend flag to true, it will suspend the consumer thread .
		 */
		public void suspend() {
			isSuspended = true;
		}

		/**
		 * change suspend flag to false, it will resume the consumer thread .
		 */
		public void resume() {
			isSuspended = false;
		}

		/**
		 * change terminate flag to true, it will terminate the consumer thread .
		 */
		public void terminate() {
			isTerminated = true;
		}

		/**
		 * getter of isSuspended .
		 *
		 * @return isSuspended
		 */
		public boolean isSuspended() {
			return isSuspended;
		}

		/**
		 * getter of isTerminated .
		 *
		 * @return isTerminated
		 */
		public boolean isTerminated() {
			return isTerminated;
		}

		/**
		 * return the consumer's name .
		 * 
		 * @return consumer's name
		 */
		public abstract String getName();

	}

	/**
	 * queue consumer implement .
	 * 
	 * <p>setup time <b>Nov 2, 2015 4:15:20 PM .</b></p>
	 *
	 * @author Vincent Huang
	 */
	private class QueueConsumer extends AbstractVConsumer {

		@Override
		public void run() {
			while (!this.isTerminated()) {
				if (!this.isSuspended()) {
					E element = null;
					try {
						/* take element out, fire hooks . */
						element = queue.take();
						deQueueHook(element);
						consume(element);
					} catch (InterruptedException ex) {
						logger.error(ex.getMessage(), ex);
					}
				} else {
					/* thread is suspending, sleep 1s and scheduling again . */
					try {
						Thread.sleep(1000);
					} catch (InterruptedException ex) {
						logger.error(ex.getMessage(), ex);
					}
				}
			}
		}

		@Override
		public String getName() {
			return queueName + " AbstractVConsumer";
		}

	}

}
