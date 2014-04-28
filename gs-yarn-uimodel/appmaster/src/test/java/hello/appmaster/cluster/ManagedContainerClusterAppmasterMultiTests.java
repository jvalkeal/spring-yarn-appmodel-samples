package hello.appmaster.cluster;

import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

import java.util.HashMap;
import java.util.Map;

import hello.appmaster.TestUtils;
import hello.appmaster.am.cluster.DefaultContainerCluster;
import hello.appmaster.am.cluster.ManagedContainerClusterAppmaster;
import hello.appmaster.am.grid.support.AnyGridProjection;
import hello.appmaster.am.grid.support.HostsGridProjection;
import hello.appmaster.am.grid.support.ProjectionData;

import org.apache.hadoop.yarn.api.records.Container;
import org.junit.Test;

/**
 * Tests for {@link ManagedContainerClusterAppmaster} using multiple clusters.
 *
 * @author Janne Valkealahti
 *
 */
public class ManagedContainerClusterAppmasterMultiTests extends AbstractManagedContainerClusterAppmasterTests {

	@Test
	public void testCreateTwoClusters() throws Exception {
		ManagedContainerClusterAppmaster appmaster = createManagedAppmaster();

		// cluster1
		AnyGridProjection projection1 = new AnyGridProjection(1);
		DefaultContainerCluster cluster1 = new DefaultContainerCluster("cluster1", projection1);
		appmaster.createContainerCluster(cluster1);

		// cluster2
		AnyGridProjection projection2 = new AnyGridProjection(1);
		DefaultContainerCluster cluster2 = new DefaultContainerCluster("cluster2", projection2);
		appmaster.createContainerCluster(cluster2);

		assertSatisfyStateData(appmaster, 2, null, null, null);
	}

	@Test
	public void testCreateStartTwoClustersDoAllocation() throws Exception {
		TestContainerAllocator allocator = new TestContainerAllocator();
		TestContainerLauncher launcher = new TestContainerLauncher();
		TestManagedContainerClusterAppmaster appmaster = createTestAppmaster(allocator, launcher);

		AnyGridProjection projection1 = new AnyGridProjection(1);
		DefaultContainerCluster cluster1 = new DefaultContainerCluster("cluster1", projection1);
		appmaster.createContainerCluster(cluster1);
		appmaster.startContainerCluster("cluster1");

		AnyGridProjection projection2 = new AnyGridProjection(1);
		DefaultContainerCluster cluster2 = new DefaultContainerCluster("cluster2", projection2);
		appmaster.createContainerCluster(cluster2);
		appmaster.startContainerCluster("cluster2");


		// should get 1 any alloc and no container kills
		TestUtils.callMethod("doTask", appmaster);
		assertThat(appmaster.satisfyStateData.size(), is(2));

		assertThat(appmaster.getSatisfyStateDataByCluster("cluster1").getAllocateData().getAny(), is(1));
		assertThat(appmaster.getSatisfyStateDataByCluster("cluster2").getAllocateData().getAny(), is(1));
		assertThat(appmaster.getSatisfyStateDataByCluster("cluster1").getRemoveData().size(), is(0));
		assertThat(appmaster.getSatisfyStateDataByCluster("cluster2").getRemoveData().size(), is(0));

		// allocate 1
		appmaster.resetTestData();
		allocateContainer(appmaster, 1);
		TestUtils.callMethod("doTask", appmaster);
		assertThat(launcher.container, notNullValue());
		assertThat(allocator.containerAllocateData, nullValue());
		assertThat(allocator.releaseContainers, nullValue());
		launcher.resetTestData();
		allocator.resetTestData();
		assertThat(appmaster.satisfyStateData.size(), is(2));
		assertThat(appmaster.satisfyStateData.get(0), nullValue());
		assertThat(appmaster.satisfyStateData.get(1), nullValue());

		// allocate 2
		appmaster.resetTestData();
		allocateContainer(appmaster, 1);
		TestUtils.callMethod("doTask", appmaster);
		assertThat(launcher.container, notNullValue());
		assertThat(allocator.containerAllocateData, nullValue());
		assertThat(allocator.releaseContainers, nullValue());
		launcher.resetTestData();
		allocator.resetTestData();
		assertThat(appmaster.satisfyStateData.size(), is(2));
		assertThat(appmaster.satisfyStateData.get(0), nullValue());
		assertThat(appmaster.satisfyStateData.get(1), nullValue());
	}

	@Test
	public void testCreateStartModifyTwoAnyClusters() throws Exception {
		TestContainerAllocator allocator = new TestContainerAllocator();
		TestContainerLauncher launcher = new TestContainerLauncher();
		TestManagedContainerClusterAppmaster appmaster = createTestAppmaster(allocator, launcher);

		// cluster1
		AnyGridProjection projection1 = new AnyGridProjection(1);
		DefaultContainerCluster cluster1 = new DefaultContainerCluster("cluster1", projection1);
		appmaster.createContainerCluster(cluster1);

		// cluster2
		AnyGridProjection projection2 = new AnyGridProjection(1);
		DefaultContainerCluster cluster2 = new DefaultContainerCluster("cluster2", projection2);
		appmaster.createContainerCluster(cluster2);

		appmaster.startContainerCluster("cluster1");
		assertDoTask(appmaster, 1, 0, 0, 0, "cluster1");
		assertDoTask(appmaster, null, null, null, null, "cluster2");

		appmaster.startContainerCluster("cluster2");
		assertDoTask(appmaster, 1, 0, 0, 0, "cluster2");
		assertDoTask(appmaster, null, null, null, null, "cluster1");

		// allocate container 1
		Container container1 = allocateContainer(appmaster, 1);
		assertThat(projection1.getMembers().size() + projection2.getMembers().size(), is(1));
//		assertDoTask(appmaster, null, null, null, null, "cluster1");

		Container container2 = allocateContainer(appmaster, 2);
		assertThat(projection1.getMembers().size() + projection2.getMembers().size(), is(2));
//		assertDoTask(appmaster, null, null, null, null, "cluster1");

		Container container3 = allocateContainer(appmaster, 3);
		assertThat(projection1.getMembers().size(), is(1));
		assertThat(projection2.getMembers().size(), is(1));

		// modify - ramp up to 2
		appmaster.modifyContainerCluster("cluster1", new ProjectionData(2));
		assertDoTask(appmaster, 1, 0, 0, 0, "cluster1");


	}

	@Test
	public void testCreateStartModifyTwoHostsClusters() throws Exception {
		TestContainerAllocator allocator = new TestContainerAllocator();
		TestContainerLauncher launcher = new TestContainerLauncher();
		TestManagedContainerClusterAppmaster appmaster = createTestAppmaster(allocator, launcher);

		// cluster1
		Map<String, Integer> hosts1 = new HashMap<String, Integer>();
		hosts1.put("host10", 1);
		HostsGridProjection projection1 = new HostsGridProjection(hosts1);
		DefaultContainerCluster cluster1 = new DefaultContainerCluster("cluster1", projection1);
		appmaster.createContainerCluster(cluster1);

		// cluster2
		Map<String, Integer> hosts2 = new HashMap<String, Integer>();
		hosts2.put("host20", 1);
		HostsGridProjection projection2 = new HostsGridProjection(hosts2);
		DefaultContainerCluster cluster2 = new DefaultContainerCluster("cluster2", projection2);
		appmaster.createContainerCluster(cluster2);

		appmaster.startContainerCluster("cluster1");
		assertDoTask(appmaster, 0, 1, 0, 0, "cluster1");
		assertDoTask(appmaster, null, null, null, null, "cluster2");

		appmaster.startContainerCluster("cluster2");
		assertDoTask(appmaster, 0, 1, 0, 0, "cluster2");
		assertDoTask(appmaster, null, null, null, null, "cluster1");

		// allocate container 1
		Container container1 = allocateContainer(appmaster, 1, "host10");
		assertThat(projection1.getMembers().size() + projection2.getMembers().size(), is(1));
//		assertDoTask(appmaster, null, null, null, null, "cluster1");

		Container container2 = allocateContainer(appmaster, 2, "host20");
		assertThat(projection1.getMembers().size() + projection2.getMembers().size(), is(2));
//		assertDoTask(appmaster, null, null, null, null, "cluster1");

		Container container3 = allocateContainer(appmaster, 3, "hostX1");
		assertThat(projection1.getMembers().size(), is(1));
		assertThat(projection2.getMembers().size(), is(1));

		// modify - ramp up to 2
//		appmaster.modifyContainerCluster("cluster1", new ProjectionData(2));
//		assertDoTask(appmaster, 1, 0, 0, 0, "cluster1");


	}


}
