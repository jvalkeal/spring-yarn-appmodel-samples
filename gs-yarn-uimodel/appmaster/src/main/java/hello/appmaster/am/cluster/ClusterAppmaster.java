package hello.appmaster.am.cluster;

import java.util.Collection;
import java.util.Map;

import org.springframework.yarn.am.YarnAppmaster;

public interface ClusterAppmaster extends YarnAppmaster {

//	void startCluster(String id);
//
//	void stopCluster(String id);
//
//	void createCluster(String id, ClusterDescriptor descriptor);
//
//	void modifyCluster(String id, ClusterDescriptor descriptor);

	void setClusterDescriptor(String id, ClusterDescriptor descriptor);

	Map<String, ContainerCluster> getClusters();

}
