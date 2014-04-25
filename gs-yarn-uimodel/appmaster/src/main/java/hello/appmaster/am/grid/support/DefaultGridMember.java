package hello.appmaster.am.grid.support;

import org.apache.hadoop.yarn.api.records.Container;

public class DefaultGridMember extends AbstractGridMember {

	public DefaultGridMember(Container container) {
		super(container);
	}


}
