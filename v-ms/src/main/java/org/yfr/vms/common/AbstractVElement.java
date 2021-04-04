
package org.yfr.vms.common;

import java.util.concurrent.Delayed;
import java.util.concurrent.TimeUnit;

/**
 * The element in AbstractVQueue must extends this abstract class .<br>
 * This class already implements Delayed interface, so the extended class can just pass delay offset in .  
 * 
 * <p>setup time <b>Jul 6, 2015 11:47:03 AM .</b></p>
 *
 * @author Vincent Huang
 */
public abstract class AbstractVElement implements Delayed {

	/** element index . */
	protected int index = 0;

	/** element due time . */
	protected long dueTime = 0L;

	/**
	 * constructor of AbstractVElement .
	 *
	 * @param delayOffset <b>(long)</b> element delay offset pass in
	 */
	public AbstractVElement(long delayOffset) {
		dueTime = System.currentTimeMillis() + delayOffset;
	}

	/**
	 * constructor of AbstractVElement (the AbstractVElement new by this constructor will jump to head of queue) .
	 *
	 * @param cutInLineTag <b>(CutInLineTag)</b> cut in line tag  pass in
	 */
	public AbstractVElement(CutInLineTag cutInLineTag) {
		dueTime = -1L;
	}

	/**
	 * getter of dueTime .
	 *
	 * @return dueTime
	 */
	public long getDueTime() {
		return dueTime;
	}

	/**
	 * setter of index .
	 *
	 * @param index <b>(int)</b> index to set
	 */
	public void setIndex(int index) {
		this.index = index;
	}

	@Override
	public long getDelay(TimeUnit unit) {
		long diff = dueTime - System.currentTimeMillis();
		return unit.convert(diff, TimeUnit.MILLISECONDS);
	}

	@Override
	public int compareTo(Delayed element) {
		if (this.dueTime < ((AbstractVElement) element).dueTime) {
			return -1;
		}

		if (this.dueTime > ((AbstractVElement) element).dueTime) {
			return 1;
		}

		return 0;
	}

	/**
	 * cut in line tag for marking element .
	 * 
	 * <p>setup time <b>Aug 3, 2015 2:18:58 PM .</b></p>
	 *
	 * @author Vincent Huang
	 */
	public enum CutInLineTag {

		TAG;

	}

}