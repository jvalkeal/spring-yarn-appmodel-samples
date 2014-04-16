package hello.appmaster.am.cluster;

import hello.appmaster.am.cluster.ClusterState.State;

import java.util.Arrays;
import java.util.Collection;
import java.util.Hashtable;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.hadoop.yarn.api.protocolrecords.StopContainersResponse;
import org.apache.hadoop.yarn.api.records.Container;
import org.apache.hadoop.yarn.api.records.ContainerStatus;
import org.springframework.core.task.TaskExecutor;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.yarn.YarnSystemException;
import org.springframework.yarn.am.AbstractEventingAppmaster;
import org.springframework.yarn.am.AppmasterCmOperations;
import org.springframework.yarn.am.AppmasterCmTemplate;
import org.springframework.yarn.am.allocate.AbstractAllocator;
import org.springframework.yarn.am.allocate.ContainerAllocateData;
import org.springframework.yarn.am.monitor.ContainerAware;
import org.springframework.yarn.support.PollingTaskSupport;

public abstract class AbstractContainerClusterAppmaster extends AbstractEventingAppmaster implements ClusterAppmaster {

	private static final Log log = LogFactory.getLog(AbstractContainerClusterAppmaster.class);

//	private final Map<String, ContainerCluster> clusters = new Hashtable<String, ContainerCluster>();
//	private final Map<String, ClusterState> states = new Hashtable<String, ClusterState>();

	private ClusterTaskPoller clusterTaskPoller;

	@Override
	public void submitApplication() {
		log.info("Submitting application");
		registerAppmaster();
		start();
		if(getAllocator() instanceof AbstractAllocator) {
			((AbstractAllocator)getAllocator()).setApplicationAttemptId(getApplicationAttemptId());
		}
		clusterTaskPoller = new ClusterTaskPoller(getTaskScheduler(), getTaskExecutor());
		clusterTaskPoller.init();
		clusterTaskPoller.start();
	}

	@Override
	protected void onContainerAllocated(Container container) {
		if (getMonitor() instanceof ContainerAware) {
			((ContainerAware)getMonitor()).onContainer(Arrays.asList(container));
		}

		boolean accepted = false;
//		for (Entry<String, ContainerCluster> entry : clusters.entrySet()) {
//			if (entry.getValue().accept(container)) {
//				accepted = true;
//				break;
//			}
//		}

		if (accepted) {
			getLauncher().launchContainer(container, getCommands());
		} else {
			getAllocator().releaseContainers(Arrays.asList(container));
		}
	}

	@Override
	protected void onContainerLaunched(Container container) {
		if (getMonitor() instanceof ContainerAware) {
			((ContainerAware)getMonitor()).onContainer(Arrays.asList(container));
		}
	}

	@Override
	protected void onContainerCompleted(ContainerStatus status) {
		super.onContainerCompleted(status);

		if (getMonitor() instanceof ContainerAware) {
			((ContainerAware)getMonitor()).onContainerStatus(Arrays.asList(status));
		}

		// when cluster indicates that containers are gone,
		// move to stopped state
	}

//	@Override
//	public void startCluster(String id) {
//		log.info("XXX: startCluster");
//		ContainerCluster cluster = clusters.get(id);
//		if (cluster == null) {
//			throw new RuntimeException("Cluster with id=" + id + " doesn't exist");
//		}
//		states.get(id).setState(State.STARTING);
//	}
//
//	@Override
//	public void stopCluster(String id) {
//		ContainerCluster cluster = clusters.get(id);
//		if (cluster == null) {
//			throw new RuntimeException("Cluster with id=" + id + " doesn't exist");
//		}
//		states.get(id).setState(State.STOPPING);
//	}

	@Override
	public Map<String, ContainerCluster> getClusters() {
		return null;
//		return clusters;
	}

//	@Override
//	public void createCluster(String id, ClusterDescriptor descriptor) {
//		log.info("XXX: createCluster");
//		clusters.put(id, new DefaultContainerCluster(descriptor));
//		states.put(id, new ClusterState());
//	}
//
//	@Override
//	public void modifyCluster(String id, ClusterDescriptor descriptor) {
//		clusters.get(id).setClusterDescriptor(descriptor);
//	}

	@Override
	public void setClusterDescriptor(String id, ClusterDescriptor descriptor) {
//		ContainerCluster cluster = clusters.get(id);
//		if (cluster == null) {
//			cluster = createContainerCluster(descriptor);
//			clusters.put(id, cluster);
//			states.put(id, new ClusterState());
//		} else {
//			cluster.setClusterDescriptor(descriptor);
//		}
	}

	protected ContainerCluster createContainerCluster(ClusterDescriptor descriptor) {
		return new DefaultContainerCluster(descriptor);
	}

	protected void doTask() {
		doAllocation();
		doKill();
	}

	protected AppmasterCmOperations getCmTemplate(Container container) {
		try {
			AppmasterCmTemplate template = new AppmasterCmTemplate(getConfiguration(), container);
			template.afterPropertiesSet();
			return template;
		} catch (Exception e) {
			throw new YarnSystemException("Unable to create AppmasterCmTemplate", e);
		}
	}

	private void doAllocation() {
//		for (Entry<String, ContainerCluster> entry : clusters.entrySet()) {
//			ContainerCluster cluster = entry.getValue();
//			if (cluster.getClusterDescriptor().getState().equals(hello.appmaster.am.cluster.ClusterDescriptor.State.ONLINE)) {
//				ContainerAllocateData allocateData = cluster.getContainerAllocateData();
//				if (allocateData != null) {
//					getAllocator().allocateContainers(allocateData);
//				}
//			}
//		}
	}

	private void doKill() {
		log.info("XXX: doKill");
//		for (Entry<String, ContainerCluster> entry : clusters.entrySet()) {
//			if (states.get(entry.getKey()).getState().equals(State.STOPPING)) {
//				for (Container container : entry.getValue().getContainers()) {
//					StopContainersResponse stopContainers = getCmTemplate(container).stopContainers();
//				}
//				states.get(entry.getKey()).setState(State.STOPPED);
//			}
//		}
	}

	private class ClusterTaskPoller extends PollingTaskSupport<Void> {

		public ClusterTaskPoller(TaskScheduler taskScheduler, TaskExecutor taskExecutor) {
			super(taskScheduler, taskExecutor);
		}

		@Override
		protected Void doPoll() {
			doTask();
			return null;
		}

	}

}
