package hello.appmaster.am.cluster;

import java.util.ArrayList;
import java.util.List;

import org.apache.hadoop.yarn.api.records.Container;
import org.springframework.yarn.am.allocate.ContainerAllocateData;

public class DefaultContainerCluster implements ContainerCluster {

	private ClusterDescriptor descriptor;
	private List<Container> containers = new ArrayList<Container>();

	private int count = 0;

	public DefaultContainerCluster(ClusterDescriptor descriptor) {
		this.descriptor = descriptor;
	}

	@Override
	public ClusterDescriptor getClusterDescriptor() {
		return descriptor;
	}

	@Override
	public void setClusterDescriptor(ClusterDescriptor descriptor) {
		this.descriptor = descriptor;
	}

	@Override
	public ContainerAllocateData getContainerAllocateData() {
		ContainerAllocateData allocateData = new ContainerAllocateData();
		allocateData.addAny(descriptor.getCount() - count);
		return allocateData;
	}

	@Override
	public boolean accept(Container container) {
		count++;
		containers.add(container);
		return true;
	}

	@Override
	public List<Container> getContainers() {
		return containers;
	}

}
