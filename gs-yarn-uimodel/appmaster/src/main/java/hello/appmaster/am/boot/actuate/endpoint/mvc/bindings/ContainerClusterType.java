package hello.appmaster.am.boot.actuate.endpoint.mvc.bindings;

import hello.appmaster.am.cluster.ContainerCluster;
import hello.appmaster.am.cluster.ContainerClusterState;

public class ContainerClusterType {

	private String id;

	private GridProjectionType projection;

	private ContainerClusterState state;

	public ContainerClusterType(ContainerCluster cluster) {
		this.id = cluster.getId();
		this.projection = new GridProjectionType(cluster.getGridProjection());
		this.state = cluster.getContainerClusterState();
	}

	public ContainerClusterType(String id, GridProjectionType projection, ContainerClusterState state) {
		this.id = id;
		this.projection = projection;
		this.state = state;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public GridProjectionType getProjection() {
		return projection;
	}

	public void setProjection(GridProjectionType projection) {
		this.projection = projection;
	}

	public ContainerClusterState getState() {
		return state;
	}

	public void setState(ContainerClusterState state) {
		this.state = state;
	}

}
