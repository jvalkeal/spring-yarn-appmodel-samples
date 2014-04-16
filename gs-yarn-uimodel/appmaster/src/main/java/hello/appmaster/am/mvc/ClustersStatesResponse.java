package hello.appmaster.am.mvc;

import hello.appmaster.am.cluster.ContainerCluster;

import java.util.Collection;

import org.springframework.hateoas.ResourceSupport;

public class ClustersStatesResponse extends ResourceSupport {

	private Collection<ContainerCluster> clusters;

	public ClustersStatesResponse(Collection<ContainerCluster> clusters) {
		this.clusters = clusters;
	}

	public Collection<ContainerCluster> getClusters() {
		return clusters;
	}

}
