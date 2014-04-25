package hello.appmaster.am.grid.support;

import org.apache.hadoop.yarn.api.records.Container;
import org.apache.hadoop.yarn.api.records.ContainerId;

import hello.appmaster.am.grid.GridMember;

public abstract class AbstractGridMember implements GridMember {

	private Container container;

	public AbstractGridMember(Container container) {
		this.container = container;
	}

	@Override
	public ContainerId getId() {
		return container.getId();
	}

	@Override
	public Container getContainer() {
		return container;
	}

}
