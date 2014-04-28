package hello.appmaster.cluster;

import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import hello.appmaster.TestUtils;
import hello.appmaster.am.cluster.DefaultContainerCluster;
import hello.appmaster.am.cluster.ManagedContainerClusterAppmaster;
import hello.appmaster.am.grid.support.AnyGridProjection;
import hello.appmaster.am.grid.support.ProjectionData;

import org.apache.hadoop.yarn.api.records.Container;
import org.junit.Test;

/**
 * Tests for {@link ManagedContainerClusterAppmaster} using single cluster.
 *
 * @author Janne Valkealahti
 *
 */
public class ManagedContainerClusterAppmasterSingleTests extends AbstractManagedContainerClusterAppmasterTests {

	@Test
	public void testInitialState() throws Exception {
		ManagedContainerClusterAppmaster appmaster = createManagedAppmaster();
		assertSatisfyStateData(appmaster, 0, null, null, null);
	}

	@Test
	public void testCreateOneCluster() throws Exception {
		ManagedContainerClusterAppmaster appmaster = createManagedAppmaster();
		AnyGridProjection projection = new AnyGridProjection(1);
		DefaultContainerCluster cluster = new DefaultContainerCluster("cluster", projection);
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

		// should get 1 any alloc and no container kills
		TestUtils.callMethod("doTask", appmaster);

//		assertThat(appmaster.doAllocationData, notNullValue());
//		assertThat(appmaster.doAllocationData.getAllocateData().getAny(), is(1));
//		assertThat(appmaster.doKillData, notNullValue());
		assertThat(appmaster.getSatisfyStateDataByCluster("foo").getAllocateData(), notNullValue());
		assertThat(appmaster.getSatisfyStateDataByCluster("foo").getAllocateData().getAny(), is(1));
		assertThat(appmaster.getSatisfyStateDataByCluster("foo").getRemoveData(), notNullValue());

		// this is garbage, track dirty
		appmaster.resetTestData();
		TestUtils.callMethod("doTask", appmaster);
//		assertThat(appmaster.doAllocationData, nullValue());
//		assertThat(appmaster.doKillData, nullValue());
		assertThat(appmaster.getSatisfyStateDataByCluster("foo"), nullValue());

		allocateContainer(appmaster, 1);

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
		assertDoTask(appmaster, null, null, null, null, "foo");

		// start
		appmaster.startContainerCluster("foo");
		assertDoTask(appmaster, 1, 0, 0, 0, "foo");

		// allocate container 1
		allocateContainer(appmaster, 1);
		assertThat(projection.getMembers().size(), is(1));
		assertDoTask(appmaster, null, null, null, null, "foo");

		// stop
		appmaster.stopContainerCluster("foo");
		appmaster.resetTestData();
		assertDoTask(appmaster, null, null, null, 1, "foo");
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
		assertDoTask(appmaster, null, null, null, null, "foo");

		// start
		appmaster.startContainerCluster("foo");
		assertDoTask(appmaster, 1, 0, 0, 0, "foo");

		// allocate container 1
		Container container1 = allocateContainer(appmaster, 1);
		assertThat(projection.getMembers().size(), is(1));
		assertDoTask(appmaster, null, null, null, null, "foo");

		// modify - ramp up to 2
		appmaster.modifyContainerCluster("foo", new ProjectionData(2));
		assertDoTask(appmaster, 1, 0, 0, 0, "foo");

		// allocate container 2
		Container container2 = allocateContainer(appmaster, 2);
		assertThat(projection.getMembers().size(), is(2));
		assertDoTask(appmaster, null, null, null, null, "foo");

		// modify - ramp up to 4
		appmaster.modifyContainerCluster("foo", new ProjectionData(4));
		assertDoTask(appmaster, 2, 0, 0, 0, "foo");

		// allocate container 3
		Container container3 = allocateContainer(appmaster, 3);
		Container container4 = allocateContainer(appmaster, 4);
		assertThat(projection.getMembers().size(), is(4));
		assertDoTask(appmaster, null, null, null, null, "foo");

		releaseContainer(appmaster, container1);
		assertThat(projection.getMembers().size(), is(3));
		assertDoTask(appmaster, 1, 0, 0, 0, "foo");
	}

	@Test
	public void testReleaseShouldReAllocate() throws Exception {
		TestContainerAllocator allocator = new TestContainerAllocator();
		TestContainerLauncher launcher = new TestContainerLauncher();
		TestManagedContainerClusterAppmaster appmaster = createTestAppmaster(allocator, launcher);
		AnyGridProjection projection = new AnyGridProjection(1);
		DefaultContainerCluster cluster = new DefaultContainerCluster("foo", projection);

		// create and start
		appmaster.createContainerCluster(cluster);
		appmaster.startContainerCluster("foo");
		assertDoTask(appmaster, 1, 0, 0, 0, "foo");

		// allocate container 1
		Container container1 = allocateContainer(appmaster, 1);
		assertThat(projection.getMembers().size(), is(1));
		assertDoTask(appmaster, null, null, null, null, "foo");

		// release, should re-allocate
		releaseContainer(appmaster, container1);
		assertThat(projection.getMembers().size(), is(0));
		assertDoTask(appmaster, 1, 0, 0, 0, "foo");
	}

}
