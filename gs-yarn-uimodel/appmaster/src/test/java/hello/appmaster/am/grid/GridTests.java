package hello.appmaster.am.grid;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import hello.appmaster.MockUtils;
import hello.appmaster.am.grid.support.DefaultGrid;
import hello.appmaster.am.grid.support.DefaultGridMember;

import org.apache.hadoop.yarn.api.records.Container;
import org.apache.hadoop.yarn.api.records.ContainerId;
import org.junit.Test;

public class GridTests {

	@Test
	public void testSimpleOperations() {
		DefaultGrid grid = new DefaultGrid();
		ContainerId id = MockUtils.getMockContainerId(MockUtils.getMockApplicationAttemptId(0, 0), 0);
		Container container = MockUtils.getMockContainer(id, null, null, null);
		DefaultGridMember member = new DefaultGridMember(container);
		grid.addMember(member);
		assertThat(grid.getMembers().size(), is(1));
	}


}
