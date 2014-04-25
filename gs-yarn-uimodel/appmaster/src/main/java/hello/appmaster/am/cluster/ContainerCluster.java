package hello.appmaster.am.cluster;

import hello.appmaster.am.grid.GridProjection;

public interface ContainerCluster {

	String getId();

	GridProjection getGridProjection();

	ContainerClusterState getContainerClusterState();

}
