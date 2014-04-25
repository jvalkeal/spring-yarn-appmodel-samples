package hello.appmaster.cluster;

import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import hello.appmaster.MockUtils;
import hello.appmaster.TestUtils;
import hello.appmaster.am.cluster.ContainerCluster;
import hello.appmaster.am.cluster.DefaultContainerCluster;
import hello.appmaster.am.cluster.ManagedContainerClusterAppmaster;
import hello.appmaster.am.grid.support.AnyGridProjection;
import hello.appmaster.am.grid.support.SatisfyStateData;

import java.util.List;
import java.util.Map;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.yarn.api.records.Container;
import org.apache.hadoop.yarn.api.records.ContainerId;
import org.junit.Test;
import org.springframework.yarn.am.allocate.ContainerAllocateData;
import org.springframework.yarn.am.allocate.ContainerAllocator;
import org.springframework.yarn.am.container.ContainerLauncher;
import org.springframework.yarn.listener.ContainerAllocatorListener;

public class ManagedContainerClusterAppmasterTests {

	@Test
	public void testInitialState() throws Exception {
		ManagedContainerClusterAppmaster appmaster = new ManagedContainerClusterAppmaster();
		appmaster.setConfiguration(new Configuration());
		TestUtils.callMethod("onInit", appmaster);
		Map<ContainerCluster, SatisfyStateData> map = TestUtils.callMethod("getSatisfyStateData", appmaster);
		assertThat(map.size(), is(0));
	}

	@Test
	public void testCreateOneCluster() throws Exception {
		ManagedContainerClusterAppmaster appmaster = new ManagedContainerClusterAppmaster();
		appmaster.setConfiguration(new Configuration());
		TestUtils.callMethod("onInit", appmaster);
		AnyGridProjection projection = new AnyGridProjection(1);
		DefaultContainerCluster cluster = new DefaultContainerCluster("foo", projection);
		appmaster.createContainerCluster(cluster);
		Map<ContainerCluster, SatisfyStateData> map = TestUtils.callMethod("getSatisfyStateData", appmaster);
		assertThat(map.size(), is(1));
	}

	@Test
	public void testCreateStartOneCluster() throws Exception {
		ManagedContainerClusterAppmaster appmaster = new ManagedContainerClusterAppmaster();
		appmaster.setConfiguration(new Configuration());
		TestUtils.callMethod("onInit", appmaster);
		AnyGridProjection projection = new AnyGridProjection(1);
		DefaultContainerCluster cluster = new DefaultContainerCluster("foo", projection);
		appmaster.createContainerCluster(cluster);
		appmaster.startContainerCluster("foo");
		Map<ContainerCluster, SatisfyStateData> map = TestUtils.callMethod("getSatisfyStateData", appmaster);
		assertThat(map.size(), is(1));
		SatisfyStateData data = map.entrySet().iterator().next().getValue();
		assertThat(data.getAllocateData().getAny(), is(1));
		assertThat(data.getAllocateData().getHosts().size(), is(0));
		assertThat(data.getAllocateData().getRacks().size(), is(0));
	}

	@Test
	public void testCreateStartOneClusterDoAllocation() throws Exception {
		TestManagedContainerClusterAppmaster appmaster = new TestManagedContainerClusterAppmaster();
		TestContainerAllocator allocator = new TestContainerAllocator();
		appmaster.setAllocator(allocator);
		TestContainerLauncher launcher = new TestContainerLauncher();
		appmaster.setLauncher(launcher);
		appmaster.setConfiguration(new Configuration());
		TestUtils.callMethod("onInit", appmaster);

		AnyGridProjection projection = new AnyGridProjection(1);
		DefaultContainerCluster cluster = new DefaultContainerCluster("foo", projection);
		appmaster.createContainerCluster(cluster);
		appmaster.startContainerCluster("foo");

		Map<ContainerCluster, SatisfyStateData> map = TestUtils.callMethod("getSatisfyStateData", appmaster);
		assertThat(map.size(), is(1));
		SatisfyStateData data = map.entrySet().iterator().next().getValue();
		assertThat(data.getAllocateData().getAny(), is(1));
		assertThat(data.getAllocateData().getHosts().size(), is(0));
		assertThat(data.getAllocateData().getRacks().size(), is(0));

		// should get 1 any alloc and no container kills
		TestUtils.callMethod("doTask", appmaster);
		assertThat(appmaster.doAllocationData, notNullValue());
		assertThat(appmaster.doAllocationData.getAllocateData().getAny(), is(1));
		assertThat(appmaster.doKillData, notNullValue());

		// this is garbage, track dirty
		appmaster.doAllocationData = null;
		appmaster.doKillData = null;
		TestUtils.callMethod("doTask", appmaster);
		assertThat(appmaster.doAllocationData, nullValue());
		assertThat(appmaster.doKillData, nullValue());


		ContainerId id = MockUtils.getMockContainerId(MockUtils.getMockApplicationAttemptId(0, 0), 0);
		Container container = MockUtils.getMockContainer(id, null, null, null);
		TestUtils.callMethod("onContainerAllocated", appmaster, new Object[]{container}, new Class<?>[]{Container.class});

		// should get 0 any alloc and no container kills
		map = TestUtils.callMethod("getSatisfyStateData", appmaster);
		assertThat(map.size(), is(1));


		assertThat(launcher.container, notNullValue());
		assertThat(allocator.containerAllocateData, nullValue());
		assertThat(allocator.releaseContainers, nullValue());
	}

	@Test
	public void testCreateStartStopOneCluster() throws Exception {
		TestManagedContainerClusterAppmaster appmaster = new TestManagedContainerClusterAppmaster();
		appmaster.setConfiguration(new Configuration());
		TestContainerAllocator allocator = new TestContainerAllocator();
		appmaster.setAllocator(allocator);
		TestContainerLauncher launcher = new TestContainerLauncher();
		appmaster.setLauncher(launcher);
		TestUtils.callMethod("onInit", appmaster);
		AnyGridProjection projection = new AnyGridProjection(1);
		DefaultContainerCluster cluster = new DefaultContainerCluster("foo", projection);
		appmaster.createContainerCluster(cluster);
		appmaster.startContainerCluster("foo");

		TestUtils.callMethod("doTask", appmaster);
		assertThat(appmaster.doAllocationData, notNullValue());
		assertThat(appmaster.doAllocationData.getAllocateData().getAny(), is(1));
		assertThat(appmaster.doKillData, notNullValue());
		assertThat(appmaster.doKillData.getRemoveData().size(), is(0));

		ContainerId id = MockUtils.getMockContainerId(MockUtils.getMockApplicationAttemptId(0, 0), 0);
		Container container = MockUtils.getMockContainer(id, null, null, null);
		TestUtils.callMethod("onContainerAllocated", appmaster, new Object[]{container}, new Class<?>[]{Container.class});

		appmaster.stopContainerCluster("foo");

		appmaster.doAllocationData = null;
		appmaster.doKillData = null;
		TestUtils.callMethod("doTask", appmaster);
		assertThat(appmaster.doAllocationData, nullValue());
		assertThat(appmaster.doKillData, notNullValue());
		assertThat(appmaster.doKillData.getRemoveData().size(), is(1));
	}

	private static class TestManagedContainerClusterAppmaster extends ManagedContainerClusterAppmaster {

		SatisfyStateData doAllocationData;
		SatisfyStateData doKillData;

		@Override
		protected void doAllocation(SatisfyStateData data) {
			doAllocationData = data;
		}

		@Override
		protected void doKill(SatisfyStateData data) {
			doKillData = data;
		}
	}

	private static class TestContainerAllocator implements ContainerAllocator {

		ContainerAllocateData containerAllocateData;
		List<Container> releaseContainers;

		@Override
		public void allocateContainers(int count) {
		}

		@Override
		public void allocateContainers(ContainerAllocateData containerAllocateData) {
			this.containerAllocateData = containerAllocateData;
		}

		@Override
		public void releaseContainers(List<Container> containers) {
			this.releaseContainers = containers;
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

	private static class TestContainerLauncher implements ContainerLauncher {

		Container container;

		@Override
		public void launchContainer(Container container, List<String> commands) {
			this.container = container;
		}

	}

}
