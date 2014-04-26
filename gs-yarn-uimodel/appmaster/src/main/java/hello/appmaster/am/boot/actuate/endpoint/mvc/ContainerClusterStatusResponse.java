package hello.appmaster.am.boot.actuate.endpoint.mvc;

import hello.appmaster.am.boot.actuate.endpoint.mvc.bindings.ContainerClusterType;
import hello.appmaster.am.cluster.ContainerCluster;

public class ContainerClusterStatusResponse {

	private ContainerClusterType cluster;

	public ContainerClusterStatusResponse(ContainerCluster cluster) {
		this.cluster = new ContainerClusterType(cluster);
	}

	public ContainerClusterType getCluster() {
		return cluster;
	}

}
