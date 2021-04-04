
package org.yfr.vms.common;

import org.yfr.vms.common.AbstractVElement;

public class VElement extends AbstractVElement {

	private String content = null;

	public VElement(String content) {
		this(content, 0L);
	}

	public VElement(String content, long delayOffset) {
		super(delayOffset);
		this.content = content;
	}

	public VElement(String content, AbstractVElement.CutInLineTag cutInLineTag) {
		super(cutInLineTag);
		this.content = content;
	}

	public String getContent() {
		return content;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("content=");
		builder.append(content);
		builder.append(",index=");
		builder.append(index);
		builder.append(",dueTime=");
		builder.append(dueTime);
		return builder.toString();
	}

}
