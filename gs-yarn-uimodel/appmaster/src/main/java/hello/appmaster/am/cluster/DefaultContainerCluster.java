package hello.appmaster.am.cluster;

import hello.appmaster.am.grid.GridProjection;

public class DefaultContainerCluster implements ContainerCluster {

	private final String id;

	private final GridProjection projection;

	private final ContainerClusterState state = new ContainerClusterState();

	public DefaultContainerCluster(String id, GridProjection projection) {
		this.id = id;
		this.projection = projection;
	}

	@Override
	public String getId() {
		return id;
	}

	@Override
	public GridProjection getGridProjection() {
		return projection;
	}

	@Override
	public ContainerClusterState getContainerClusterState() {
		return state;
	}

}
