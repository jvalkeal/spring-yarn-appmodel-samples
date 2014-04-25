package hello.appmaster.am.boot.actuate.endpoint.mvc;

import hello.appmaster.am.cluster.ContainerCluster;

public class ClusterStateResponse {

	private ContainerCluster cluster;

	public ClusterStateResponse(ContainerCluster cluster) {
		this.cluster = cluster;
	}

	public ContainerCluster getCluster() {
		return cluster;
	}

}
