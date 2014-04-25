package hello.appmaster;

import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.apache.hadoop.yarn.api.records.ApplicationAttemptId;
import org.apache.hadoop.yarn.api.records.ApplicationId;
import org.apache.hadoop.yarn.api.records.Container;
import org.apache.hadoop.yarn.api.records.ContainerId;
import org.apache.hadoop.yarn.api.records.ContainerState;
import org.apache.hadoop.yarn.api.records.ContainerStatus;
import org.apache.hadoop.yarn.api.records.NodeId;
import org.apache.hadoop.yarn.api.records.Priority;
import org.apache.hadoop.yarn.api.records.Resource;

public abstract class MockUtils {

	public static ContainerStatus getMockContainerStatus(ContainerId containerId, ContainerState containerState, int exitStatus) {
		ContainerStatus status = mock(ContainerStatus.class);
		when(status.getContainerId()).thenReturn(containerId);
		when(status.getState()).thenReturn(containerState);
		when(status.getExitStatus()).thenReturn(exitStatus);
		return status;
	}

	public static ApplicationId getMockApplicationId(int appId) {
		ApplicationId applicationId = mock(ApplicationId.class);
		when(applicationId.getClusterTimestamp()).thenReturn(0L);
		when(applicationId.getId()).thenReturn(appId);
		return applicationId;
	}

	public static Container getMockContainer(ContainerId containerId, NodeId nodeId, Resource resource,
			Priority priority) {
		Container container = mock(Container.class);
		when(container.getId()).thenReturn(containerId);
		when(container.getNodeId()).thenReturn(nodeId);
		when(container.getResource()).thenReturn(resource);
		when(container.getPriority()).thenReturn(priority);
		return container;
	}

	public static ContainerId getMockContainerId(ApplicationAttemptId applicationAttemptId, int id) {
		ContainerId containerId = mock(ContainerId.class);
		doReturn(applicationAttemptId).when(containerId).getApplicationAttemptId();
		doReturn(id).when(containerId).getId();
		doReturn(Integer.toString(id)).when(containerId).toString();
		return containerId;
	}

	public static NodeId getMockNodeId(String host, int port) {
		NodeId nodeId = mock(NodeId.class);
		doReturn(host).when(nodeId).getHost();
		doReturn(port).when(nodeId).getPort();
		return nodeId;
	}

	public static ApplicationAttemptId getMockApplicationAttemptId(int appId, int attemptId) {
		ApplicationId applicationId = mock(ApplicationId.class);
		when(applicationId.getClusterTimestamp()).thenReturn(0L);
		when(applicationId.getId()).thenReturn(appId);
		ApplicationAttemptId applicationAttemptId = mock(ApplicationAttemptId.class);
		when(applicationAttemptId.getApplicationId()).thenReturn(applicationId);
		when(applicationAttemptId.getAttemptId()).thenReturn(attemptId);
		return applicationAttemptId;
	}

}
