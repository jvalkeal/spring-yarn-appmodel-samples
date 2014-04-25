package hello.appmaster.am.grid.support;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertThat;
import hello.appmaster.MockUtils;
import hello.appmaster.am.grid.GridProjection;

import org.apache.hadoop.yarn.api.records.Container;
import org.apache.hadoop.yarn.api.records.ContainerId;
import org.apache.hadoop.yarn.api.records.NodeId;
import org.junit.Test;

public class HostsGridProjectionTests {

	@Test
	public void testDefaults() {
		GridProjection projection = new HostsGridProjection();
		SatisfyStateData satisfyState = projection.getSatisfyState();
		assertThat(satisfyState, notNullValue());
		assertThat(satisfyState.getAllocateData(), notNullValue());
		assertThat(satisfyState.getAllocateData().getAny(), is(0));
		assertThat(satisfyState.getAllocateData().getHosts().size(), is(0));
		assertThat(satisfyState.getAllocateData().getRacks().size(), is(0));
		assertThat(satisfyState.getRemoveData(), notNullValue());
		assertThat(satisfyState.getRemoveData().size(), is(0));
	}

	@Test
	public void testAddxxx() throws Exception {
		GridProjection projection = new HostsGridProjection();

		ProjectionData projectionData = new ProjectionData();
		projectionData.setHost("host1", 2);
		projectionData.setHost("host2", 2);
		projectionData.setHost("host3", 2);
		projection.setProjectionData(projectionData);

		SatisfyStateData satisfyState = projection.getSatisfyState();
		assertThat(satisfyState, notNullValue());
		assertThat(satisfyState.getAllocateData(), notNullValue());
		assertThat(satisfyState.getAllocateData().getAny(), is(0));
		assertThat(satisfyState.getAllocateData().getHosts().size(), is(3));
		assertThat(satisfyState.getAllocateData().getRacks().size(), is(0));
		assertThat(satisfyState.getRemoveData().size(), is(0));

		ContainerId id1 = MockUtils.getMockContainerId(MockUtils.getMockApplicationAttemptId(0, 0), 1);
		NodeId nodeId1 = MockUtils.getMockNodeId("host1", 0);
		Container container1 = MockUtils.getMockContainer(id1, nodeId1, null, null);
		DefaultGridMember member1 = new DefaultGridMember(container1);
		projection.acceptMember(member1);

		satisfyState = projection.getSatisfyState();
		assertThat(satisfyState.getAllocateData().getHosts().size(), is(3));
		assertThat(satisfyState.getAllocateData().getHosts().get("host1"), is(1));
		assertThat(satisfyState.getAllocateData().getHosts().get("host2"), is(2));
		assertThat(satisfyState.getAllocateData().getHosts().get("host3"), is(2));

		projectionData = new ProjectionData();
		projectionData.setHost("host1", 0);
		projectionData.setHost("host2", 0);
		projectionData.setHost("host3", 0);
		projection.setProjectionData(projectionData);

		satisfyState = projection.getSatisfyState();
		assertThat(satisfyState.getAllocateData().getHosts().size(), is(3));
		assertThat(satisfyState.getAllocateData().getHosts().get("host1"), is(0));
		assertThat(satisfyState.getAllocateData().getHosts().get("host2"), is(0));
		assertThat(satisfyState.getAllocateData().getHosts().get("host3"), is(0));
		assertThat(satisfyState.getRemoveData().size(), is(1));

		projection.removeMember(member1);
		satisfyState = projection.getSatisfyState();
		assertThat(satisfyState.getRemoveData().size(), is(0));

	}

}
