package hello.appmaster.am.grid.support;

import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import hello.appmaster.MockUtils;
import hello.appmaster.am.grid.GridProjection;

import org.apache.hadoop.yarn.api.records.Container;
import org.apache.hadoop.yarn.api.records.ContainerId;
import org.junit.Test;

public class DefaultProjectedGridTests {

	@Test
	public void testSimpleOperations() {
		DefaultGrid grid = new DefaultGrid();
		DefaultProjectedGrid projectedGrid = new DefaultProjectedGrid(grid);
		AnyGridProjection projection = new AnyGridProjection(2);

		projectedGrid.addProjection(projection);

		ContainerId id = MockUtils.getMockContainerId(MockUtils.getMockApplicationAttemptId(0, 0), 0);
		Container container = MockUtils.getMockContainer(id, null, null, null);
		DefaultGridMember member = new DefaultGridMember(container);
		grid.addMember(member);

//		GridProjection projection2 = projectedGrid.getProjection("test1");
		assertThat(projection, notNullValue());

		assertThat(projection.getMembers().size(), is(1));

		SatisfyStateData satisfyState = projection.getSatisfyState();
		assertThat(satisfyState.getAllocateData().getAny(), is(1));
	}



}
