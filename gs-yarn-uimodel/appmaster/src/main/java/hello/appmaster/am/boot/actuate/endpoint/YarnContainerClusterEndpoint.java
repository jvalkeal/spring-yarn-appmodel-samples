package hello.appmaster.am.boot.actuate.endpoint;

import hello.appmaster.am.cluster.ClusterAppmaster;
import hello.appmaster.am.cluster.ClusterDescriptor;
import hello.appmaster.am.cluster.ClusterDescriptor.State;
import hello.appmaster.am.cluster.ContainerCluster;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.actuate.endpoint.AbstractEndpoint;
import org.springframework.yarn.am.YarnAppmaster;

public class YarnContainerClusterEndpoint extends AbstractEndpoint<Map<String, ContainerCluster>> {

	private ClusterAppmaster appmaster;

	public YarnContainerClusterEndpoint() {
		super("cluster");
	}

	@Autowired
	public void setYarnAppmaster(YarnAppmaster yarnAppmaster) {
		if (yarnAppmaster instanceof ClusterAppmaster) {
			appmaster = ((ClusterAppmaster)yarnAppmaster);
		}
	}

	@Override
	public Map<String, ContainerCluster> invoke() {
		return appmaster.getClusters();
	}

	public Map<String, ContainerCluster> getClusters() {
		return appmaster.getClusters();
	}

	public void startCluster(String id) {
		Map<String, ContainerCluster> clusters = appmaster.getClusters();
		if (clusters.containsKey(id)) {
			ClusterDescriptor descriptor = clusters.get(id).getClusterDescriptor();
			descriptor.setState(State.ONLINE);
		}
	}

	public void stopCluster(String id) {
		Map<String, ContainerCluster> clusters = appmaster.getClusters();
		if (clusters.containsKey(id)) {
			ClusterDescriptor descriptor = clusters.get(id).getClusterDescriptor();
			descriptor.setState(State.OFFLINE);
		}
	}

}
