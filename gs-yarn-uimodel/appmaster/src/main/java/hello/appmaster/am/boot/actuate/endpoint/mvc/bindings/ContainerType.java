package hello.appmaster.am.boot.actuate.endpoint.mvc.bindings;

import org.apache.hadoop.yarn.api.records.Container;

public class ContainerType {

	private String containerId;

	private String nodeId;

	public ContainerType(Container container) {
		this.containerId = container.getId().toString();
		this.nodeId = container.getNodeId().toString();
	}

	public String getContainerId() {
		return containerId;
	}

	public String getNodeId() {
		return nodeId;
	}

}
