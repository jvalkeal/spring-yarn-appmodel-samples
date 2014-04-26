package hello.appmaster.am.boot.actuate.endpoint.mvc.bindings;

import hello.appmaster.am.grid.GridMember;
import hello.appmaster.am.grid.GridProjection;
import hello.appmaster.am.grid.support.ProjectionData;

import java.util.ArrayList;
import java.util.List;

public class GridProjectionType {

	private List<ContainerType> containers = new ArrayList<ContainerType>();

	private ProjectionData projectionData;

	public GridProjectionType(GridProjection projection) {
		projectionData = projection.getProjectionData();
		for (GridMember member : projection.getMembers()) {
			containers.add(new ContainerType(member.getContainer()));
		}
	}

	public List<ContainerType> getContainers() {
		return containers;
	}

	public ProjectionData getProjectionData() {
		return projectionData;
	}

}
