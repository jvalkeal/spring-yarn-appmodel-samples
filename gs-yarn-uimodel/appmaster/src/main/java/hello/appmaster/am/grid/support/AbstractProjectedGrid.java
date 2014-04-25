package hello.appmaster.am.grid.support;

import hello.appmaster.am.grid.Grid;
import hello.appmaster.am.grid.GridMember;
import hello.appmaster.am.grid.GridProjection;
import hello.appmaster.am.grid.ProjectedGrid;
import hello.appmaster.am.grid.listener.DefaultProjectedGridListener;
import hello.appmaster.am.grid.listener.GridListener;
import hello.appmaster.am.grid.listener.ProjectedGridListener;

import java.util.Collection;
import java.util.HashSet;

import org.springframework.util.Assert;

/**
 * Simple {@code ProjectedGrid} base implementation.
 *
 * @author Janne Valkealahti
 *
 */
public abstract class AbstractProjectedGrid implements ProjectedGrid {

//	private final ConcurrentHashMap<String, GridProjection> projections = new ConcurrentHashMap<String, GridProjection>();
	private final Collection<GridProjection> projections = new HashSet<GridProjection>();

	/** Listener dispatcher for projected grid events */
	private final DefaultProjectedGridListener listeners = new DefaultProjectedGridListener();

	private final Grid grid;

	/**
	 * Instantiates a new abstract projected grid.
	 *
	 * @param grid the grid
	 */
	public AbstractProjectedGrid(Grid grid) {
		Assert.notNull(grid, "Grid must not be null");
		this.grid = grid;
		this.grid.addInterceptor(new ProjectionAcceptInterceptor());
		this.grid.addGridListener(new ProjectionHandlingGridListener());
	}

	@Override
	public boolean addProjection(GridProjection projection) {
		Assert.notNull(projection, "Projection must not be null");
		return projections.add(projection);
	}

	@Override
	public boolean removeProjection(GridProjection projection) {
		Assert.notNull(projection, "Projection must not be null");
		boolean removed = projections.remove(projection);
		if (removed) {
			notifyGridProjectionRemoved(projection);
		}
		return removed;
	}

	@Override
	public Collection<GridProjection> getProjections() {
		return projections;
	}

	@Override
	public void addProjectedGridListener(ProjectedGridListener listener) {
		listeners.register(listener);
	}

	/**
	 * Notifies registered {@code ProjectedGridListener}s that
	 * a {@code GridProjection} has been added to a {@code ProjectedGrid}.
	 *
	 * @param projection the grid projection
	 */
	protected void notifyGridProjectionAdded(GridProjection projection) {
		listeners.projectionAdded(projection);
	}

	/**
	 * Notifies registered {@code ProjectedGridListener}s that
	 * a {@code GridProjection} has been removed from a {@code ProjectedGrid}.
	 *
	 * @param projection the grid projection
	 */
	protected void notifyGridProjectionRemoved(GridProjection projection) {
		listeners.projectionRemoved(projection);
	}

	/**
	 * Notifies registered {@code ContainerGridGroupsListener}s that
	 * a {@code ContainerNode} has been added to a {@code ContainerGroup}.
	 *
	 * @param projection the grid projection
	 * @param member the grid member
	 */
	protected void notifyMemberAdded(GridProjection projection, GridMember member) {
		listeners.memberAdded(projection, member);
	}

	/**
	 * Notifies registered {@code ContainerGridGroupsListener}s that
	 * a {@code ContainerNode} has been removed from a {@code ContainerGroup}.
	 *
	 * @param projection the group
	 * @param member the node
	 */
	protected void notifyMemberRemoved(GridProjection projection, GridMember member) {
		listeners.memberRemoved(projection, member);
	}

	private class ProjectionAcceptInterceptor implements GridMemberInterceptor {

		@Override
		public GridMember preAdd(GridMember member, Grid grid) {
			// intercept and check if any projection
			// accepts this member
			for (GridProjection projection : getProjections()) {
				if (projection.acceptMember(member)) {
					notifyMemberAdded(projection, member);
					return member;
				}
			}
			return null;
		}

	}

	private class ProjectionHandlingGridListener implements GridListener {

		@Override
		public void memberAdded(GridMember member) {
		}

		@Override
		public void memberRemoved(GridMember member) {
			for (GridProjection projection : projections) {
				projection.removeMember(member);
			}
		}

	}

}
