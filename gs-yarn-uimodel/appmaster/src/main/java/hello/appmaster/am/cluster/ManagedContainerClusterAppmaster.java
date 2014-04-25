package hello.appmaster.am.cluster;

import java.util.List;

import hello.appmaster.am.grid.Grid;
import hello.appmaster.am.grid.GridMember;
import hello.appmaster.am.grid.ProjectedGrid;
import hello.appmaster.am.grid.support.DefaultGrid;
import hello.appmaster.am.grid.support.DefaultProjectedGrid;
import hello.appmaster.am.grid.support.SatisfyStateData;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class ManagedContainerClusterAppmaster extends AbstractContainerClusterAppmaster {

	private static final Log log = LogFactory.getLog(ManagedContainerClusterAppmaster.class);

	@Override
	protected Grid doCreateGrid() {
		return new DefaultGrid();
	}

	@Override
	protected ProjectedGrid doCreateProjectedGrid(Grid grid) {
		return new DefaultProjectedGrid(grid);
	}

	@Override
	protected void doAllocation(SatisfyStateData data) {
		// this adds new request, does not replace
		if(data != null) {
			getAllocator().allocateContainers(data.getAllocateData());
		}
	}

	@Override
	protected void doKill(SatisfyStateData data) {
		if(data != null) {
			List<GridMember> remove = data.getRemoveData();
			for (GridMember member : remove) {
				killContainer(member.getContainer());
			}
		}
	}

}
