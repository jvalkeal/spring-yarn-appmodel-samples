package hello.appmaster.am.boot.actuate.endpoint.mvc;

import java.util.Map;

public class ModifyContainerClusterRequest {

	private ProjectionDataType projectionData;

	public ProjectionDataType getProjectionData() {
		return projectionData;
	}

	public void setProjectionData(ProjectionDataType projectionData) {
		this.projectionData = projectionData;
	}

	public static class ProjectionDataType {
		Integer any;
		Map<String, Integer> hosts;
		Map<String, Integer> racks;
		public Integer getAny() {
			return any;
		}
		public void setAny(Integer any) {
			this.any = any;
		}
		public Map<String, Integer> getHosts() {
			return hosts;
		}
		public void setHosts(Map<String, Integer> hosts) {
			this.hosts = hosts;
		}
		public Map<String, Integer> getRacks() {
			return racks;
		}
		public void setRacks(Map<String, Integer> racks) {
			this.racks = racks;
		}
	}

}
