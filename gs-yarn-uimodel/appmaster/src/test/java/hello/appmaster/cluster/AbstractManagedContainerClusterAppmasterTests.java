package hello.appmaster.cluster;

import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import hello.appmaster.MockUtils;
import hello.appmaster.TestUtils;
import hello.appmaster.am.cluster.ContainerCluster;
import hello.appmaster.am.cluster.ManagedContainerClusterAppmaster;
import hello.appmaster.am.grid.support.SatisfyStateData;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.yarn.api.records.Container;
import org.apache.hadoop.yarn.api.records.ContainerId;
import org.apache.hadoop.yarn.api.records.ContainerState;
import org.apache.hadoop.yarn.api.records.ContainerStatus;
import org.apache.hadoop.yarn.api.records.NodeId;
import org.springframework.yarn.am.allocate.ContainerAllocateData;
import org.springframework.yarn.am.allocate.ContainerAllocator;
import org.springframework.yarn.am.container.ContainerLauncher;
import org.springframework.yarn.listener.ContainerAllocatorListener;

public abstract class AbstractManagedContainerClusterAppmasterTests {

	protected Container allocateContainer(Object appmaster, int id) throws Exception {
		return allocateContainer(appmaster, id, null);
	}

	protected Container allocateContainer(Object appmaster, int id, String host) throws Exception {
		ContainerId containerId = MockUtils.getMockContainerId(MockUtils.getMockApplicationAttemptId(0, 0), 0);
		NodeId nodeId = MockUtils.getMockNodeId(host, 0);
		Container container = MockUtils.getMockContainer(containerId, nodeId, null, null);
		TestUtils.callMethod("onContainerAllocated", appmaster, new Object[]{container}, new Class<?>[]{Container.class});
		return container;
	}

	protected void releaseContainer(Object appmaster, Container container) throws Exception {
		ContainerStatus containerStatus = MockUtils.getMockContainerStatus(container.getId(), ContainerState.COMPLETE, 0);
		TestUtils.callMethod("onContainerCompleted", appmaster, new Object[]{containerStatus}, new Class<?>[]{ContainerStatus.class});
	}

	protected void assertDoTask(TestManagedContainerClusterAppmaster appmaster, Integer anySize, Integer hostsSize, Integer racksSize, Integer killSize, String cluster) throws Exception {
		TestUtils.callMethod("doTask", appmaster);
		if (anySize != null || hostsSize != null || racksSize != null) {
			assertThat(appmaster.getSatisfyStateDataByCluster(cluster).getAllocateData(), notNullValue());

		}
		if (anySize != null) {
			assertThat(appmaster.getSatisfyStateDataByCluster(cluster).getAllocateData().getAny(), is(anySize));
		}
		if (hostsSize != null) {
			assertThat(appmaster.getSatisfyStateDataByCluster(cluster).getAllocateData().getHosts().size(), is(hostsSize));
		}
		if (racksSize != null) {
			assertThat(appmaster.getSatisfyStateDataByCluster(cluster).getAllocateData().getRacks().size(), is(racksSize));
		}
		if (killSize != null) {
			assertThat(appmaster.getSatisfyStateDataByCluster(cluster).getRemoveData(), notNullValue());
			assertThat(appmaster.getSatisfyStateDataByCluster(cluster).getRemoveData().size(), is(killSize));
		} else {
			assertThat(appmaster.getSatisfyStateDataByCluster(cluster), nullValue());
		}
	}

	protected static void assertSatisfyStateData(Object appmaster, Integer mapSize, Integer anySize, Integer hostsSize, Integer racksSize) throws Exception {
		Map<ContainerCluster, SatisfyStateData> map = TestUtils.callMethod("getSatisfyStateData", appmaster);
		assertThat(map.size(), is(mapSize));
		SatisfyStateData data = null;
		if (mapSize != null && mapSize > 0) {
			data = map.entrySet().iterator().next().getValue();
		}
		if (anySize != null) {
			assertThat(data.getAllocateData().getAny(), is(anySize));
		}
		if (hostsSize != null) {
			assertThat(data.getAllocateData().getHosts().size(), is(hostsSize));
		}
		if (racksSize != null) {
			assertThat(data.getAllocateData().getRacks().size(), is(racksSize));
		}
	}

	protected static ManagedContainerClusterAppmaster createManagedAppmaster() throws Exception {
		ManagedContainerClusterAppmaster appmaster = new ManagedContainerClusterAppmaster();
		appmaster.setConfiguration(new Configuration());
		TestUtils.callMethod("onInit", appmaster);
		return appmaster;
	}

	protected static TestManagedContainerClusterAppmaster createTestAppmaster(TestContainerAllocator allocator, TestContainerLauncher launcher) throws Exception {
		TestManagedContainerClusterAppmaster appmaster = new TestManagedContainerClusterAppmaster();
		appmaster.setConfiguration(new Configuration());
		appmaster.setAllocator(allocator);
		appmaster.setLauncher(launcher);
		TestUtils.callMethod("onInit", appmaster);
		return appmaster;
	}

	protected static class TestManagedContainerClusterAppmaster extends ManagedContainerClusterAppmaster {

		Map<ContainerCluster, SatisfyStateData> satisfyStateData;

		public SatisfyStateData getSatisfyStateDataByCluster(String cluster) {
			if (satisfyStateData == null) {
				return null;
			}
			for (Entry<ContainerCluster, SatisfyStateData> entry : satisfyStateData.entrySet()) {
				if (entry.getKey().getId().equals(cluster)) {
					return entry.getValue();
				}
			}
			return null;
		}

		public void resetTestData() {
			satisfyStateData = null;
		}

		@Override
		protected void handleSatisfyStateData(Map<ContainerCluster, SatisfyStateData> satisfyData) {
			satisfyStateData = satisfyData;
		}

	}

	protected static class TestContainerAllocator implements ContainerAllocator {

		ArrayList<ContainerAllocateData> containerAllocateData;
		ArrayList<Container> releaseContainers;

		public void resetTestData() {
			containerAllocateData = null;
			releaseContainers = null;
		}

		@Override
		public void allocateContainers(int count) {
		}

		@Override
		public void allocateContainers(ContainerAllocateData containerAllocateData) {
			if (this.containerAllocateData == null) {
				this.containerAllocateData = new ArrayList<ContainerAllocateData>();
			}
			this.containerAllocateData.add(containerAllocateData);
		}

		@Override
		public void releaseContainers(List<Container> containers) {
			if (this.releaseContainers == null) {
				this.releaseContainers = new ArrayList<Container>();
			}
			this.releaseContainers.addAll(containers);
		}

		@Override
		public void releaseContainer(ContainerId containerId) {
		}

		@Override
		public void addListener(ContainerAllocatorListener listener) {
		}

		@Override
		public void setProgress(float progress) {
		}

	}

	protected static class TestContainerLauncher implements ContainerLauncher {

		Container container;

		public void resetTestData() {
			container = null;
		}

		@Override
		public void launchContainer(Container container, List<String> commands) {
			this.container = container;
		}

	}

}
