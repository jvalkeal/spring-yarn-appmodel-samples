package hello.appmaster.am.cluster;

import java.util.List;

import org.apache.hadoop.yarn.api.records.Container;
import org.springframework.yarn.am.allocate.ContainerAllocateData;

public interface ContainerCluster {

	ClusterDescriptor getClusterDescriptor();

	void setClusterDescriptor(ClusterDescriptor descriptor);

	ContainerAllocateData getContainerAllocateData();

	boolean accept(Container container);

	List<Container> getContainers();

}
