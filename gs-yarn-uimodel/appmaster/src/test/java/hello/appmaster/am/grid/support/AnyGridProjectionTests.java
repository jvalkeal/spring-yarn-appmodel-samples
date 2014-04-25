package hello.appmaster.am.grid.support;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertThat;
import hello.appmaster.MockUtils;
import hello.appmaster.TestUtils;
import hello.appmaster.am.grid.GridProjection;

import org.apache.hadoop.yarn.api.records.Container;
import org.apache.hadoop.yarn.api.records.ContainerId;
import org.junit.Test;

public class AnyGridProjectionTests {

	@Test
	public void testDefaults() {
		GridProjection projection = new AnyGridProjection();
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
	public void testAddOne() {
		GridProjection projection = new AnyGridProjection();
		projection.setProjectionData(new ProjectionData(1));
		SatisfyStateData satisfyState = projection.getSatisfyState();
		assertThat(satisfyState, notNullValue());
		assertThat(satisfyState.getAllocateData(), notNullValue());
		assertThat(satisfyState.getAllocateData().getAny(), is(1));
		assertThat(satisfyState.getAllocateData().getHosts().size(), is(0));
		assertThat(satisfyState.getAllocateData().getRacks().size(), is(0));
		assertThat(satisfyState.getRemoveData(), notNullValue());
		assertThat(satisfyState.getRemoveData().size(), is(0));
	}

	@Test
	public void testAddOneAddMember() {
		GridProjection projection = new AnyGridProjection();
		projection.setProjectionData(new ProjectionData(1));

		ContainerId id = MockUtils.getMockContainerId(MockUtils.getMockApplicationAttemptId(0, 0), 0);
		Container container = MockUtils.getMockContainer(id, null, null, null);
		projection.acceptMember(new DefaultGridMember(container));

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
	public void testAddTwoAddMembersRemoveMember() throws Exception {
		GridProjection projection = new AnyGridProjection();
		projection.setProjectionData(new ProjectionData(2));
		int count = TestUtils.readField("count", projection);
		assertThat(count, is(2));

		ContainerId id1 = MockUtils.getMockContainerId(MockUtils.getMockApplicationAttemptId(0, 0), 1);
		Container container1 = MockUtils.getMockContainer(id1, null, null, null);
		DefaultGridMember member1 = new DefaultGridMember(container1);
		projection.acceptMember(member1);

		ContainerId id2 = MockUtils.getMockContainerId(MockUtils.getMockApplicationAttemptId(0, 0), 2);
		Container container2 = MockUtils.getMockContainer(id2, null, null, null);
		DefaultGridMember member2 = new DefaultGridMember(container2);
		projection.acceptMember(member2);

		SatisfyStateData satisfyState = projection.getSatisfyState();
		assertThat(satisfyState, notNullValue());
		assertThat(satisfyState.getAllocateData(), notNullValue());
		assertThat(satisfyState.getAllocateData().getAny(), is(0));
		assertThat(satisfyState.getRemoveData().size(), is(0));

		projection.setProjectionData(new ProjectionData(1));
		count = TestUtils.readField("count", projection);
		assertThat(count, is(1));
		satisfyState = projection.getSatisfyState();
		assertThat(satisfyState, notNullValue());
		assertThat(satisfyState.getAllocateData(), notNullValue());
		assertThat(satisfyState.getAllocateData().getAny(), is(0));
		assertThat(satisfyState.getRemoveData().size(), is(1));

		projection.removeMember(member2);
		satisfyState = projection.getSatisfyState();
		assertThat(satisfyState.getAllocateData().getAny(), is(0));
		assertThat(satisfyState.getRemoveData().size(), is(0));

	}

}
