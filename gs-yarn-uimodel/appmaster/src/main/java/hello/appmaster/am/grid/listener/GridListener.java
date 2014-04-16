package hello.appmaster.am.grid.listener;

import hello.appmaster.am.grid.GridMember;

/**
 * Listener for grid container node events.
 *
 * @author Janne Valkealahti
 *
 */
public interface GridListener {

	/**
	 * Invoked when member is added into a grid.
	 *
	 * @param member the {@link GridMember}
	 */
	void memberAdded(GridMember member);

	/**
	 * Invoked when member is removed from a grid.
	 *
	 * @param member the {@link GridMember}
	 */
	void memberRemoved(GridMember member);

}
