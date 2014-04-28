/*
 * Copyright 2014 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package hello.appmaster.am.boot.actuate.endpoint;

import hello.appmaster.am.boot.actuate.endpoint.mvc.YarnContainerClusterEndpointResponse;
import hello.appmaster.am.cluster.ContainerCluster;
import hello.appmaster.am.cluster.ContainerClusterAppmaster;
import hello.appmaster.am.cluster.DefaultContainerCluster;
import hello.appmaster.am.grid.GridProjection;
import hello.appmaster.am.grid.support.ProjectionData;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.actuate.endpoint.AbstractEndpoint;
import org.springframework.boot.actuate.endpoint.Endpoint;
import org.springframework.yarn.am.YarnAppmaster;

/**
 * {@link Endpoint} handling operations against {@link ContainerClusterAppmaster}.
 *
 * @author Janne Valkealahti
 *
 */
public class YarnContainerClusterEndpoint extends AbstractEndpoint<Map<String, ContainerCluster>> {

	// AbstractEndpoint<YarnContainerClusterEndpointResponse>

	public final static String ENDPOINT_ID = "yarn_containercluster";

	private ContainerClusterAppmaster appmaster;

	/**
	 * Instantiates a new yarn container cluster endpoint.
	 */
	public YarnContainerClusterEndpoint() {
		super(ENDPOINT_ID);
	}

	@Autowired
	public void setYarnAppmaster(YarnAppmaster yarnAppmaster) {
		if (yarnAppmaster instanceof ContainerClusterAppmaster) {
			appmaster = ((ContainerClusterAppmaster)yarnAppmaster);
		}
	}

	@Override
	public Map<String, ContainerCluster> invoke() {

		return appmaster.getContainerClusters();

//		YarnContainerClusterEndpointResponse response = new YarnContainerClusterEndpointResponse();
//		return response;
	}

	public Map<String, ContainerCluster> getClusters() {
		return appmaster.getContainerClusters();
	}

	public void createCluster(String clusterId, GridProjection projection) {
		appmaster.createContainerCluster(new DefaultContainerCluster(clusterId, projection));
	}

	public void startCluster(String clusterId) {
		appmaster.startContainerCluster(clusterId);
	}

	public void stopCluster(String clusterId) {
		appmaster.stopContainerCluster(clusterId);
	}

	public void modifyCluster(String id, ProjectionData data) {
		appmaster.modifyContainerCluster(id, data);
	}

}
