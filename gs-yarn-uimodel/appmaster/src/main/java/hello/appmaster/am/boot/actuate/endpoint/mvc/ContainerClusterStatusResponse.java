package hello.appmaster.am.boot.actuate.endpoint.mvc;

import hello.appmaster.am.cluster.ContainerCluster;

public class ContainerClusterStatusResponse {

	private ContainerCluster cluster;

	public ContainerClusterStatusResponse(ContainerCluster cluster) {
		this.cluster = cluster;
	}

	public ContainerCluster getCluster() {
		return cluster;
	}

}
