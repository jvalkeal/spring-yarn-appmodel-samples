package hello.appmaster.am.grid.listener;

import hello.appmaster.am.grid.GridProjection;
import hello.appmaster.am.grid.GridMember;

/**
 * Listener for grid container group events.
 *
 * @author Janne Valkealahti
 *
 */
public interface ProjectedGridListener {

	/**
	 * Invoked when a new projection is added.
	 *
	 * @param projection the {@link GridProjection}
	 */
	void projectionAdded(GridProjection projection);

	/**
	 * Invoked when projection is removed.
	 *
	 * @param projection the {@link GridProjection}
	 */
	void projectionRemoved(GridProjection projection);

	/**
	 * Invoked when a member is added into a projection.
	 *
	 * @param projection the {@link GridProjection} a member belongs to
	 * @param member the {@link GridMember}
	 */
	void memberAdded(GridProjection projection, GridMember member);

	/**
	 * Invoked when a member is removed from a projection.
	 *
	 * @param projection the {@link GridProjection} a member belongs to
	 * @param member the {@link GridMember}
	 */
	void memberRemoved(GridProjection projection, GridMember member);

}
