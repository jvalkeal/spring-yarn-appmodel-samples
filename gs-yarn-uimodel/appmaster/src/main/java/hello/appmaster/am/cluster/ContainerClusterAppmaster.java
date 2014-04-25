package hello.appmaster.am.cluster;

import hello.appmaster.am.grid.support.ProjectionData;

import java.util.Map;

import org.springframework.yarn.am.YarnAppmaster;

public interface ContainerClusterAppmaster extends YarnAppmaster {

	Map<String, ContainerCluster> getContainerClusters();

	void createContainerCluster(ContainerCluster cluster);

	void startContainerCluster(String id);

	void stopContainerCluster(String id);

	void modifyContainerCluster(String id, ProjectionData data);

}
