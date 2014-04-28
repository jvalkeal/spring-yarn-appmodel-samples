package hello.appmaster.am.boot.actuate.endpoint.mvc;

import hello.appmaster.am.cluster.ContainerCluster;

import java.util.Collection;

import org.springframework.hateoas.ResourceSupport;

public class YarnContainerClusterEndpointResponse extends ResourceSupport {

	private Collection<ContainerCluster> clusters;

	public YarnContainerClusterEndpointResponse(Collection<ContainerCluster> clusters) {
		super();
		this.clusters = clusters;
	}

	public Collection<ContainerCluster> getClusters() {
		return clusters;
	}

}
