package hello.appmaster.cluster;

import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import hello.appmaster.MockUtils;
import hello.appmaster.TestUtils;
import hello.appmaster.am.cluster.ContainerCluster;
import hello.appmaster.am.cluster.ContainerClusterState.Event;
import hello.appmaster.am.cluster.DefaultContainerCluster;
import hello.appmaster.am.cluster.ManagedContainerClusterAppmaster;
import hello.appmaster.am.grid.support.AnyGridProjection;
import hello.appmaster.am.grid.support.ProjectionData;
import hello.appmaster.am.grid.support.SatisfyStateData;

import java.util.List;
import java.util.Map;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.yarn.api.records.Container;
import org.apache.hadoop.yarn.api.records.ContainerId;
import org.apache.hadoop.yarn.api.records.ContainerState;
import org.apache.hadoop.yarn.api.records.ContainerStatus;
import org.junit.Test;
import org.springframework.yarn.am.allocate.ContainerAllocateData;
import org.springframework.yarn.am.allocate.ContainerAllocator;
import org.springframework.yarn.am.container.ContainerLauncher;
import org.springframework.yarn.listener.ContainerAllocatorListener;

/**
 * Tests for {@link ManagedContainerClusterAppmaster}.
 *
 * @author Janne Valkealahti
 *
 */
public class ManagedContainerClusterAppmasterTests {

	@Test
	public void testInitialState() throws Exception {
		ManagedContainerClusterAppmaster appmaster = createManagedAppmaster();
		assertSatisfyStateData(appmaster, 0, null, null, null);
	}

	@Test
	public void testCreateOneCluster() throws Exception {
		ManagedContainerClusterAppmaster appmaster = createManagedAppmaster();
		AnyGridProjection projection = new AnyGridProjection(1);
		DefaultContainerCluster cluster = new DefaultContainerCluster("foo", projection);
		appmaster.createContainerCluster(cluster);
		assertSatisfyStateData(appmaster, 1, null, null, null);
	}

	@Test
	public void testCreateStartOneCluster() throws Exception {
		ManagedContainerClusterAppmaster appmaster = createManagedAppmaster();
		AnyGridProjection projection = new AnyGridProjection(1);
		DefaultContainerCluster cluster = new DefaultContainerCluster("foo", projection);
		appmaster.createContainerCluster(cluster);
		appmaster.startContainerCluster("foo");
		assertSatisfyStateData(appmaster, 1, null, null, null);
	}

	@Test
	public void testCreateStartOneClusterDoAllocation() throws Exception {
		TestContainerAllocator allocator = new TestContainerAllocator();
		TestContainerLauncher launcher = new TestContainerLauncher();
		TestManagedContainerClusterAppmaster appmaster = createTestAppmaster(allocator, launcher);

		AnyGridProjection projection = new AnyGridProjection(1);
		DefaultContainerCluster cluster = new DefaultContainerCluster("foo", projection);

		appmaster.createContainerCluster(cluster);
		appmaster.startContainerCluster("foo");

		assertSatisfyStateData(appmaster, 1, 1, 0, 0);

		cluster.getContainerClusterState().command(Event.CONFIGURE);
		// should get 1 any alloc and no container kills
		TestUtils.callMethod("doTask", appmaster);
		assertThat(appmaster.doAllocationData, notNullValue());
		assertThat(appmaster.doAllocationData.getAllocateData().getAny(), is(1));
		assertThat(appmaster.doKillData, notNullValue());

		// this is garbage, track dirty
		appmaster.resetTestData();
		TestUtils.callMethod("doTask", appmaster);
		assertThat(appmaster.doAllocationData, nullValue());
		assertThat(appmaster.doKillData, nullValue());


		ContainerId id = MockUtils.getMockContainerId(MockUtils.getMockApplicationAttemptId(0, 0), 0);
		Container container = MockUtils.getMockContainer(id, null, null, null);
		TestUtils.callMethod("onContainerAllocated", appmaster, new Object[]{container}, new Class<?>[]{Container.class});

		// should get 0 any alloc and no container kills
		assertSatisfyStateData(appmaster, 1, null, null, null);

		assertThat(launcher.container, notNullValue());
		assertThat(allocator.containerAllocateData, nullValue());
		assertThat(allocator.releaseContainers, nullValue());
	}

	@Test
	public void testCreateStartStopOneCluster() throws Exception {
		TestContainerAllocator allocator = new TestContainerAllocator();
		TestContainerLauncher launcher = new TestContainerLauncher();
		TestManagedContainerClusterAppmaster appmaster = createTestAppmaster(allocator, launcher);
		AnyGridProjection projection = new AnyGridProjection(1);
		DefaultContainerCluster cluster = new DefaultContainerCluster("foo", projection);

		// create
		appmaster.createContainerCluster(cluster);
		assertDoTask(appmaster, null, null, null, null);

		// start
		appmaster.startContainerCluster("foo");
		assertDoTask(appmaster, 1, 0, 0, 0);

		// allocate container 1
		allocateContainer(appmaster, 1);
		assertThat(projection.getMembers().size(), is(1));
		assertDoTask(appmaster, null, null, null, null);

		// stop
		appmaster.stopContainerCluster("foo");
		appmaster.resetTestData();
		assertDoTask(appmaster, null, null, null, 1);
	}

	@Test
	public void testCreateStartModifyOneCluster() throws Exception {
		TestContainerAllocator allocator = new TestContainerAllocator();
		TestContainerLauncher launcher = new TestContainerLauncher();
		TestManagedContainerClusterAppmaster appmaster = createTestAppmaster(allocator, launcher);
		AnyGridProjection projection = new AnyGridProjection(1);
		DefaultContainerCluster cluster = new DefaultContainerCluster("foo", projection);

		// create
		appmaster.createContainerCluster(cluster);
		assertDoTask(appmaster, null, null, null, null);

		// start
		appmaster.startContainerCluster("foo");
		assertDoTask(appmaster, 1, 0, 0, 0);

		// allocate container 1
		Container container1 = allocateContainer(appmaster, 1);
		assertThat(projection.getMembers().size(), is(1));
		assertDoTask(appmaster, null, null, null, null);

		// modify - ramp up to 2
		appmaster.modifyContainerCluster("foo", new ProjectionData(2));
		assertDoTask(appmaster, 1, 0, 0, 0);

		// allocate container 2
		Container container2 = allocateContainer(appmaster, 2);
		assertThat(projection.getMembers().size(), is(2));
		assertDoTask(appmaster, null, null, null, null);

		// modify - ramp up to 4
		appmaster.modifyContainerCluster("foo", new ProjectionData(4));
		assertDoTask(appmaster, 2, 0, 0, 0);

		// allocate container 3
		Container container3 = allocateContainer(appmaster, 3);
		Container container4 = allocateContainer(appmaster, 4);
		assertThat(projection.getMembers().size(), is(4));
		assertDoTask(appmaster, null, null, null, null);

		releaseContainer(appmaster, container1);
		assertThat(projection.getMembers().size(), is(3));
		assertDoTask(appmaster, 1, 0, 0, 0);
	}

	private Container allocateContainer(Object appmaster, int id) throws Exception {
		ContainerId containerId = MockUtils.getMockContainerId(MockUtils.getMockApplicationAttemptId(0, 0), 0);
		Container container = MockUtils.getMockContainer(containerId, null, null, null);
		TestUtils.callMethod("onContainerAllocated", appmaster, new Object[]{container}, new Class<?>[]{Container.class});
		return container;
	}

	private void releaseContainer(Object appmaster, Container container) throws Exception {
		ContainerStatus containerStatus = MockUtils.getMockContainerStatus(container.getId(), ContainerState.COMPLETE, 0);
		TestUtils.callMethod("onContainerCompleted", appmaster, new Object[]{containerStatus}, new Class<?>[]{ContainerStatus.class});
	}

	private void assertDoTask(TestManagedContainerClusterAppmaster appmaster, Integer anySize, Integer hostsSize, Integer racksSize, Integer killSize) throws Exception {
		TestUtils.callMethod("doTask", appmaster);
		if (anySize != null || hostsSize != null || racksSize != null) {
			assertThat(appmaster.doAllocationData, notNullValue());
		}
		if (anySize != null) {
			assertThat(appmaster.doAllocationData.getAllocateData().getAny(), is(anySize));
		}
		if (hostsSize != null) {
			assertThat(appmaster.doAllocationData.getAllocateData().getHosts().size(), is(hostsSize));
		}
		if (racksSize != null) {
			assertThat(appmaster.doAllocationData.getAllocateData().getRacks().size(), is(racksSize));
		}
		if (killSize != null) {
			assertThat(appmaster.doKillData, notNullValue());
			assertThat(appmaster.doKillData.getRemoveData().size(), is(killSize));
		} else {
			assertThat(appmaster.doKillData, nullValue());
		}
	}


	private static void assertSatisfyStateData(Object appmaster, Integer mapSize, Integer anySize, Integer hostsSize, Integer racksSize) throws Exception {
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

	private static ManagedContainerClusterAppmaster createManagedAppmaster() throws Exception {
		ManagedContainerClusterAppmaster appmaster = new ManagedContainerClusterAppmaster();
		appmaster.setConfiguration(new Configuration());
		TestUtils.callMethod("onInit", appmaster);
		return appmaster;
	}

	private static TestManagedContainerClusterAppmaster createTestAppmaster(TestContainerAllocator allocator, TestContainerLauncher launcher) throws Exception {
		TestManagedContainerClusterAppmaster appmaster = new TestManagedContainerClusterAppmaster();
		appmaster.setConfiguration(new Configuration());
		appmaster.setAllocator(allocator);
		appmaster.setLauncher(launcher);
		TestUtils.callMethod("onInit", appmaster);
		return appmaster;
	}

	private static class TestManagedContainerClusterAppmaster extends ManagedContainerClusterAppmaster {

		SatisfyStateData doAllocationData;
		SatisfyStateData doKillData;

		public void resetTestData() {
			doAllocationData = null;
			doKillData = null;
		}

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
