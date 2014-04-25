package hello.appmaster.am.boot.actuate.endpoint.mvc;

import org.springframework.hateoas.ResourceSupport;

public class CreateContainerClusterResponse extends ResourceSupport {

	private String clusterId;

	public String getClusterId() {
		return clusterId;
	}

	public void setClusterId(String clusterId) {
		this.clusterId = clusterId;
	}

}
