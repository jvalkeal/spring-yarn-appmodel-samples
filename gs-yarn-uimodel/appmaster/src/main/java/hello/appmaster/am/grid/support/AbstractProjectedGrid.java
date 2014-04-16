package hello.appmaster.am.grid.support;

import hello.appmaster.am.grid.Grid;
import hello.appmaster.am.grid.ProjectedGrid;
import hello.appmaster.am.grid.GridProjection;
import hello.appmaster.am.grid.GridMember;
import hello.appmaster.am.grid.listener.ProjectedGridListener;
import hello.appmaster.am.grid.listener.DefaultProjectedGridListener;

import java.util.Collection;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.hadoop.yarn.api.records.ContainerId;
import org.springframework.util.Assert;

/**
 * Simple {@code ContainerGridGroups} base implementation keeping {@code ContainerGroup}s
 * in a {@code ConcurrentHashMap}.
 *
 * @author Janne Valkealahti
 *
 */
public abstract class AbstractProjectedGrid implements ProjectedGrid {

	private Grid grid;

	private ConcurrentHashMap<String, GridProjection> projections = new ConcurrentHashMap<String, GridProjection>();

	/** Listener dispatcher for container group events */
	private DefaultProjectedGridListener projectionListeners = new DefaultProjectedGridListener();

	/**
	 * Instantiates a new abstract container grid groups.
	 */
	public AbstractProjectedGrid() {
	}

	/**
	 * Instantiates a new abstract container grid groups.
	 *
	 * @param grid the container grid
	 */
	public AbstractProjectedGrid(Grid grid) {
		this.grid = grid;
	}

	@Override
	public boolean addProjection(GridProjection group) {
		Assert.notNull(group, "Node must not be null");
		if (projections.putIfAbsent(group.getId(), group) == null) {
			notifyGroupAdded(group);
			return true;
		} else {
			return false;
		}
	}

	@Override
	public boolean removeProjection(String id) {
		Assert.notNull(id, "Group identifier must not be null");
		GridProjection removed = projections.remove(id);
		if (removed != null) {
			notifyGroupRemoved(removed);
			return true;
		} else {
			return false;
		}
	}

	@Override
	public GridProjection getProjection(String id) {
		return projections.get(id);
	}

	@Override
	public Collection<GridProjection> getProjections() {
		return projections.values();
	}

	@Override
	public GridProjection getProjectionByNode(ContainerId id) {
		for (GridProjection group : projections.values()) {
			if (group.hasMember(id)) {
				return group;
			}
		}
		return null;
	}

	@Override
	public void addProjectedGridListener(ProjectedGridListener listener) {
		projectionListeners.register(listener);
	}

	/**
	 * Sets the container grid.
	 *
	 * @param containerGrid the new container grid
	 */
	public void setContainerGrid(Grid containerGrid) {
		this.grid = containerGrid;
	}

	/**
	 * Notifies registered {@code ContainerGridGroupsListener}s that
	 * a {@code ContainerGroup} has been added to a {@code ContainerGridGroups}.
	 *
	 * @param group the group
	 */
	protected void notifyGroupAdded(GridProjection group) {
		projectionListeners.projectionAdded(group);
	}

	/**
	 * Notifies registered {@code ContainerGridGroupsListener}s that
	 * a {@code ContainerGroup} has been removed from a {@code ContainerGridGroups}.
	 *
	 * @param group the group
	 */
	protected void notifyGroupRemoved(GridProjection group) {
		projectionListeners.projectionRemoved(group);
	}

	/**
	 * Notifies registered {@code ContainerGridGroupsListener}s that
	 * a {@code ContainerNode} has been added to a {@code ContainerGroup}.
	 *
	 * @param group the group
	 * @param node the node
	 */
	protected void notifyNodeAdded(GridProjection group, GridMember node) {
		projectionListeners.memberAdded(group, node);
	}

	/**
	 * Notifies registered {@code ContainerGridGroupsListener}s that
	 * a {@code ContainerNode} has been removed from a {@code ContainerGroup}.
	 *
	 * @param group the group
	 * @param node the node
	 */
	protected void notifyNodeRemoved(GridProjection group, GridMember node) {
		projectionListeners.memberRemoved(group, node);
	}

}
