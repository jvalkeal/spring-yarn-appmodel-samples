package hello.appmaster.am.cluster;

import hello.appmaster.am.cluster.ContainerClusterState.Event;
import hello.appmaster.am.cluster.ContainerClusterState.State;
import hello.appmaster.am.grid.Grid;
import hello.appmaster.am.grid.GridMember;
import hello.appmaster.am.grid.GridProjection;
import hello.appmaster.am.grid.ProjectedGrid;
import hello.appmaster.am.grid.support.DefaultGridMember;
import hello.appmaster.am.grid.support.ProjectionData;
import hello.appmaster.am.grid.support.SatisfyStateData;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Queue;
import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.hadoop.yarn.api.records.Container;
import org.apache.hadoop.yarn.api.records.ContainerStatus;
import org.springframework.core.task.TaskExecutor;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.util.Assert;
import org.springframework.yarn.YarnSystemException;
import org.springframework.yarn.am.AbstractEventingAppmaster;
import org.springframework.yarn.am.AppmasterCmOperations;
import org.springframework.yarn.am.AppmasterCmTemplate;
import org.springframework.yarn.am.allocate.AbstractAllocator;
import org.springframework.yarn.am.monitor.ContainerAware;
import org.springframework.yarn.support.PollingTaskSupport;

/**
 * Base implementation of a {@link ContainerClusterAppmaster}.
 *
 * @author Janne Valkealahti
 *
 */
public abstract class AbstractContainerClusterAppmaster extends AbstractEventingAppmaster implements ContainerClusterAppmaster {

	private static final Log log = LogFactory.getLog(AbstractContainerClusterAppmaster.class);

	/** ClusterId to ContainerCluster map */
	private final Map<String, ContainerCluster> clusters = new HashMap<String, ContainerCluster>();

	/** Containers scheduled to be killed */
	private final Queue<Container> killQueue = new LinkedList<Container>();

	/** Grid tracking generic grid members */
	private Grid grid;

	/** Projected grid tracking container clusters */
	private ProjectedGrid projectedGrid;

	private ClusterTaskPoller clusterTaskPoller;

	/** Flag to autostart cluster after create */
	private boolean autoStartCluster;

	@Override
	protected void onInit() throws Exception {
		super.onInit();
		grid = doCreateGrid();
		Assert.notNull(grid, "Grid must be set");
		projectedGrid = doCreateProjectedGrid(grid);
		Assert.notNull(projectedGrid, "ProjectedGrid must be set");
	}

	@Override
	protected void doStart() {
		super.doStart();
		if (autoStartCluster && !clusters.isEmpty()) {
			for (String id : clusters.keySet()) {
				startContainerCluster(id);
			}
		}
	}

	@Override
	protected void doStop() {
		if (clusterTaskPoller != null) {
			clusterTaskPoller.stop();
			clusterTaskPoller = null;
		}
		super.doStop();
	}

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

		boolean accepted = grid.addMember(new DefaultGridMember(container));
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

		grid.removeMember(status.getContainerId());

		if (getMonitor() instanceof ContainerAware) {
			((ContainerAware)getMonitor()).onContainerStatus(Arrays.asList(status));
		}
	}

	@Override
	public Map<String, ContainerCluster> getContainerClusters() {
		return clusters;
	}

	@Override
	public void createContainerCluster(ContainerCluster cluster) {
		clusters.put(cluster.getId(), cluster);
		projectedGrid.addProjection(cluster.getGridProjection());
		if (autoStartCluster) {
			startContainerCluster(cluster.getId());
		}
	}

	@Override
	public void startContainerCluster(String id) {
		ContainerCluster cluster = clusters.get(id);
		if (cluster != null) {
			cluster.getContainerClusterState().command(Event.CONFIGURE);
		}
	}

	@Override
	public void stopContainerCluster(String id) {
		ContainerCluster cluster = clusters.get(id);
		if (cluster != null) {
			cluster.getContainerClusterState().command(Event.STOP);
		}
	}

	@Override
	public void modifyContainerCluster(String id, ProjectionData data) {
		ContainerCluster cluster = clusters.get(id);
		if (cluster.getContainerClusterState().getState().equals(State.RUNNING)) {
			GridProjection gridProjection = cluster.getGridProjection();
			gridProjection.setProjectionData(data);
			cluster.getContainerClusterState().command(Event.CONFIGURE);
		}
	}

	public void setInitialClusters(Map<String, ContainerCluster> initialClusters) {
		this.clusters.putAll(initialClusters);
	}

	public void setAutoStartCluster(boolean autoStartCluster) {
		this.autoStartCluster = autoStartCluster;
	}

	protected abstract Grid doCreateGrid();

	protected abstract ProjectedGrid doCreateProjectedGrid(Grid grid);

	protected abstract void doAllocation(SatisfyStateData data);

	protected abstract void doKill(SatisfyStateData data);

	protected void killContainer(Container container) {
		killQueue.add(container);
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

	/**
	 * Periodic task callback called by a {@link ClusterTaskPoller}.
	 */
	private void doTask() {
		for (Entry<ContainerCluster, SatisfyStateData> data : getSatisfyStateData().entrySet()) {

			doAllocation(data.getValue());

			if (data.getKey().getContainerClusterState().getState().equals(State.STOPPING)) {
				data.getKey().getContainerClusterState().command(Event.END);
				SatisfyStateData sdata = new SatisfyStateData(new ArrayList<GridMember>(data.getKey().getGridProjection().getMembers()), null);
				doKill(sdata);
			} else {
				doKill(data.getValue());
			}

			Container toKill = null;
			while ((toKill = killQueue.poll()) != null) {
				getCmTemplate(toKill).stopContainers();
			}

		}
	}

	/**
	 * Builds current satisfy state data for all clusters. This data
	 * contains information what kind of containers clusters will need
	 * to satisfy its target state. Additionally data may also contain
	 * containers which it doesn't need which should be killed.
	 *
	 * @return the satisfy state data for clusters
	 */
	private Map<ContainerCluster, SatisfyStateData> getSatisfyStateData() {
		Map<ContainerCluster, SatisfyStateData> data = new HashMap<ContainerCluster, SatisfyStateData>();
		for (Entry<String, ContainerCluster> entry : clusters.entrySet()) {
			if (entry.getValue().getContainerClusterState().getState().equals(State.ALLOCATING)) {
				data.put(entry.getValue(), entry.getValue().getGridProjection().getSatisfyState());
				entry.getValue().getContainerClusterState().command(Event.CONTINUE);
			} else {
				data.put(entry.getValue(), null);
			}
		}
		return data;
	}

	/**
	 * Internal poller handling cluster allocation request scheduling.
	 */
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
